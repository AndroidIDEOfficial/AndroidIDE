package com.itsaky.androidide.services;

import android.text.TextUtils;
import androidx.annotation.StringRes;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.google.gson.Gson;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.lsp.LSP;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.models.project.IDEModule;
import com.itsaky.androidide.models.project.IDEProject;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.tasks.GradleTask;
import com.itsaky.androidide.tasks.gradle.BaseGradleTasks;
import com.itsaky.androidide.tasks.gradle.build.AssembleDebug;
import com.itsaky.androidide.tasks.gradle.resources.UpdateResourceClassesTask;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.toaster.Toaster;
import java.io.File;
import java.io.FileFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.FileChangeType;
import org.eclipse.lsp4j.FileEvent;
import com.blankj.utilcode.util.ThreadUtils;
import java.util.Collection;
import java.util.stream.Collectors;

public class IDEService implements ShellServer.Callback {

    private ShellServer shell;
    private BuildListener listener;
    private GradleTask currentTask;
    private StudioApp app;

    private IDEProject mIDEProject;
    private IDEModule mAppModule;
    
    private final File projectRoot;
    
    private boolean isBuilding = false;
    private boolean isRunning = false;
	private boolean recreateShellOnDone = false;
    
    private final String RUN_TASK = "> Task";
    private final String STARTING_DAEMON = "Starting a ";
    private final String BUILD_SUCCESS = "BUILD SUCCESSFUL";
    private final String BUILD_FAILED = "BUILD FAILED";
    private final String PROJECT_INITIALIZED = ">>> PROJECT INITIALIZED <<<";
    
    public static final int TASK_SHOW_DEPENDENCIES       = GradleTask.TASK_SHOW_DEPENDENCIES;
    public static final int TASK_ASSEMBLE_DEBUG          = GradleTask.TASK_ASSEMBLE_DEBUG;
    public static final int TASK_ASSEMBLE_RELEASE        = GradleTask.TASK_ASSEMBLE_RELEASE;
    public static final int TASK_BUILD                   = GradleTask.TASK_BUILD;
    public static final int TASK_BUNDLE                  = GradleTask.TASK_BUNDLE;
    public static final int TASK_CLEAN                   = GradleTask.TASK_CLEAN;
    public static final int TASK_CLEAN_BUILD             = GradleTask.TASK_CLEAN_BUILD;
    public static final int TASK_COMPILE_JAVA            = GradleTask.TASK_COMPILE_JAVA;
    public static final int TASK_DEX                     = GradleTask.TASK_DEX;
    public static final int TASK_LINT                    = GradleTask.TASK_LINT;
    public static final int TASK_LINT_DEBUG              = GradleTask.TASK_LINT_DEBUG;
    public static final int TASK_LINT_RELEASE            = GradleTask.TASK_LINT_RELEASE;
    
    public static final Logger LOG = Logger.instance("IDEService");
    
    public IDEService(File projectRoot) {
        this.projectRoot = projectRoot;
        this.app = StudioApp.getInstance();
        this.shell = app.newShell(this);
        this.isRunning = true;
    }
    
    public IDEService setListener(final BuildListener listener) {
        this.listener = listener == null ? null : new MainThreadBuildListener(listener);
        return this;
    }

    @Override
    public void output(CharSequence charSequence) {
        if(listener == null || charSequence == null) return;
        final String line = charSequence
            .toString()
            .replace(":showDependenciesDebug", ":startCompletionServices")
            .trim();
            
        if(line.contains(PROJECT_INITIALIZED)) {
            readIdeProject();
            return;
        }
            
        boolean shouldOutput = true;
        
        if(shouldOutput) {
            String text = line.replace(StudioApp.getInstance().getRootDir().getAbsolutePath(), "ANDROIDIDE_HOME");
            listener.appendOutput(currentTask, text);
        }
        if(charSequence.toString().contains(RUN_TASK)) {
            listener.onRunTask(currentTask, charSequence.toString().trim());
        } else
        if(charSequence.toString().startsWith(STARTING_DAEMON)) {
            listener.onStartingGradleDaemon(currentTask);
        } else
        if(charSequence.toString().contains(BUILD_SUCCESS)) {
            isBuilding = false;
            if(recreateShellOnDone)
                createShell();
            if(currentTask != null && currentTask.getTaskID() != TASK_SHOW_DEPENDENCIES)
                listener.onBuildSuccessful(currentTask, charSequence.toString().trim());
            appendOutputSeparator();
            
            if(currentTask != null && currentTask.affectsGeneratedSources()) {
                /**
                 * If a task affects generated sources,
                 * we have to notify Java Language Server
                 */
                notifyExternalSourceChange();
            }
        } else
        if(charSequence.toString().contains(BUILD_FAILED)) {
            isBuilding = false;
            if(recreateShellOnDone)
                createShell();
            listener.onBuildFailed(currentTask, line.trim());
            appendOutputSeparator();
        }
	}

    private void readIdeProject() {
        // Parsing project data is meaningless if there is no one listening...
        if(listener == null)
            return;
        
        final BuildListener local = listener;
        new Thread(() -> {
            mIDEProject = new Gson()
                .fromJson(
                    FileIOUtils.readFile2String(Environment.PROJECT_DATA_FILE),
                    IDEProject.class
                );
            if(local != null && mIDEProject != null) {
                Optional<IDEModule> appModule = mIDEProject.getModuleByPath(":app");
                if(appModule.isPresent()) {
                    mAppModule = appModule.get();
                }
                local.onProjectLoaded(mIDEProject, appModule);
            } else {
                app.toast(com.itsaky.androidide.R.string.msg_init_project_failed, Toaster.Type.ERROR);
            }
        }).start();
    }

    private void appendOutputSeparator() {
        listener.appendOutput(currentTask, "\n\n");
    }
    
    private void createShell() {
        if(shell != null)
            shell.exit();
        shell = app.newShell(this);
        recreateShellOnDone = false;
    }

    public void exit() {
        // Delete the tmp directory. It is not needed anymore...
        FileUtils.delete(Environment.TMP_DIR);
        isRunning = false;
    }

    private String currentTime() {
        String pattern = "HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern, Locale.US);
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }

    private String getString(@StringRes int id) {
        return app.getString(id);
    }

    private String getString(@StringRes int id, Object... format) {
        return app.getString(id, format);
    }
    
    /**
     * Notifies about an external change event in generated .java files
     * Generated source files can be:
     * 1. ViewBinding/Databinding sources
     * 2. BuildConfig
     * 
     * Also, we tell the Java language server to recreate compiler
     * This is because R.jar may have been changed and the server,
     * must recreate the JavaCompilerService in order to
     * get completions from updated classpaths
     */
    private void notifyExternalSourceChange() {
        LSP.notifyWatchedFilesChanged(new DidChangeWatchedFilesParams(createSourceChangeEvents()));
        
        if(listener != null) {
            listener.onBuildModified();
        }
    }
    
    /**
     * Returns a list of FileEvent representing the change in generated sources
     *
     * Takes care of adding changes from all modules
     */
    private List<FileEvent> createSourceChangeEvents() {
        final List<FileEvent> events = new ArrayList<>();
        for(int i=0;i<mIDEProject.modules.size();i++) {
            IDEModule module = mIDEProject.modules.get(i);
            if(module == null
            || module.projectDir == null
            || module.projectDir.trim().length() <= 0)
                continue;
                
            File generated = new File(module.projectDir, "build/generated");
            if(!(generated.exists() && generated.isDirectory())) continue;
            
            final List<File> sources = FileUtils.listFilesInDirWithFilter(generated, JAVA_FILTER, true);
            if(sources == null) continue;
            for(File source : sources) {
                events.add(new FileEvent(source.toURI().toString(), FileChangeType.Changed));
            }
        }
        return events;
    }

    private String getArguments(List<String> tasks) {
        final PreferenceManager prefs = app.getPrefManager();
        final List<String> args = new ArrayList<>();
        
        args.add("sh");
        args.add("gradlew");
        args.addAll(asAppTasks(tasks));
        args.add("--init-script");
        args.add(Environment.INIT_SCRIPT.getAbsolutePath());

        if (prefs.isStracktraceEnabled()) {
            args.add("--stacktrace");
        }
        if (prefs.isGradleInfoEnabled()) {
            args.add("--info");
        }
        if (prefs.isGradleDebugEnabled()) {
            args.add("--debug");
        }
        if (prefs.isGradleScanEnabled()) {
            args.add("--scan");
        }
        if (prefs.isGradleWarningEnabled()) {
            args.add("--warning-mode");
            args.add("all");
        }

        return TextUtils.join(" ", args);
    }

    private Collection<? extends String> asAppTasks(List<String> tasks) {
        return tasks.stream()
            .filter(t -> t != null)
            .map(t -> asAppModuleTask(t))
            .collect(Collectors.toList());
    }
    
    private String asAppModuleTask (String name) {
        return name.startsWith(":app:") ? name : ":app:" + name;
    }
    
    public IDEProject getIDEProject () {
        return mIDEProject;
    }
    
    public void execTask(GradleTask task) {
        execTask(task, false);
    }

    public void execTask(GradleTask task, boolean stopIfRunning) {
        if (stopIfRunning && isBuilding()) {
            stopAllDaemons();
        } else if (isBuilding()) {
            return;
        }
        
        if (listener != null) {
            listener.saveFiles();
            Environment.mkdirIfNotExits(Environment.TMP_DIR);
            currentTask = task;
            listener.appendOutput(task, getString(R.string.msg_task_begin, currentTime(), task.getName()));
            shell.bgAppend(String.format("cd '%s'", projectRoot.getAbsolutePath()));
            shell.bgAppend(getArguments(task.getTasks()));
			isBuilding = true;
            listener.prepareBuild();
        }
    }
    
    /**
     * @return An int[] containing minimumSdk at index 0 and targetSdk at index 1 or {1, 1}
     */
    public int[] getSdkInfo() {
        if(mAppModule != null) {
            return new int[]{mAppModule.minSdk.apiLevel, mAppModule.targetSdk.apiLevel};
        }
        
        return new int[]{1, 1};
    }
    
    public void initProject() {
        execTask(BaseGradleTasks.INITIALIZE_IDE_PROJECT);
	}

    public void assembleDebug(boolean installApk) {
        execTask(((AssembleDebug) BaseGradleTasks.ASSEMBLE_DEBUG).setInstallApk(installApk));
    }

    public void assembleRelease() {
        execTask(BaseGradleTasks.ASSEMBLE_RELEASE);
    }

    public void build() {
        execTask(BaseGradleTasks.BUILD);
    }

    public void bundle() {
        execTask(BaseGradleTasks.BUNDLE);
    }
    
    public void clean() {
        execTask(BaseGradleTasks.CLEAN);
    }

    public void cleanAndRebuild() {
        execTask(BaseGradleTasks.CLEAN_BUILD);
    }

    public void stopAllDaemons() {
        app.newShell(null).bgAppend(String.format(Locale.US, "cd '%s' && sh gradlew --stop", projectRoot.getAbsolutePath()));
        if(isBuilding()) {
            if(listener != null)
                listener.onBuildFailed(currentTask, getString(R.string.msg_daemons_stopped));
            isBuilding = false;
        }
    }

    public void lint() {
        execTask(BaseGradleTasks.LINT);
    }

    public void lintDebug() {
        execTask(BaseGradleTasks.LINT_DEBUG);
    }

    public void lintRelease() {
        execTask(BaseGradleTasks.LINT_RELEASE);
    }
    
    public void updateResourceClasses() {
        execTask(new UpdateResourceClassesTask(mAppModule != null && mAppModule.viewBindingEnabled));
    }
    
    public String typeString(int type) {
        switch (type) {
            case TASK_SHOW_DEPENDENCIES:
                return "";
            case TASK_ASSEMBLE_DEBUG:
                return getString(R.string.build_debug);
            case TASK_ASSEMBLE_RELEASE:
                return getString(R.string.build_release);
            case TASK_BUILD:
                return getString(R.string.build);
            case TASK_CLEAN_BUILD:
                return getString(R.string.clean_amp_build);
            case TASK_CLEAN:
                return getString(R.string.clean_project);
            case TASK_BUNDLE :
                return getString(R.string.create_aab);
            case TASK_DEX:
                return getString(R.string.compiling);
            default: return "";
        }
	}

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isBuilding() {
        return isBuilding;
    }
    
    private class MainThreadBuildListener implements BuildListener {
        
        private final BuildListener listener;
        
        public MainThreadBuildListener(BuildListener listener) {
            this.listener = listener;
        }
        
        @Override
        public void onBuildModified() {
            ThreadUtils.runOnUiThread(() -> {
                listener.onBuildModified();
            });
        }

        @Override
        public void onProjectLoaded(IDEProject project, Optional<IDEModule> appModule) {
            ThreadUtils.runOnUiThread(() -> {
                listener.onProjectLoaded(project, appModule);
            });
        }

        @Override
        public void onStartingGradleDaemon(GradleTask task) {
            ThreadUtils.runOnUiThread(() -> {
                listener.onStartingGradleDaemon(task);
            });
        }

        @Override
        public void onRunTask(GradleTask task, String name) {
            ThreadUtils.runOnUiThread(() -> {
                listener.onRunTask(task, name);
            });
        }

        @Override
        public void onBuildSuccessful(GradleTask task, String msg) {
            ThreadUtils.runOnUiThread(() -> {
                listener.onBuildSuccessful(task, msg);
            });
        }

        @Override
        public void onBuildFailed(GradleTask task, String msg) {
            ThreadUtils.runOnUiThread(() -> {
                listener.onBuildFailed(task, msg);
            });
        }

        @Override
        public void saveFiles() {
            ThreadUtils.runOnUiThread(() -> {
                listener.saveFiles();
            });
        }

        @Override
        public void appendOutput(GradleTask task, CharSequence text) {
            ThreadUtils.runOnUiThread(() -> {
                listener.appendOutput(task, text);
            });
        }

        @Override
        public void prepareBuild() {
            ThreadUtils.runOnUiThread(() -> {
                listener.prepareBuild();
            });
        }
    }
    
    /**
     * A client should implement this interface to get callbacks about a build
     */
    public static interface BuildListener {
        /**
         * Called to notify the client about the modification of build/generated files
         */
        void onBuildModified();
        
        /**
         * Called when the project has been loaded and its data has been read
         *
         * @param project The loaded project
         * @param appModule The main application module
         */
        void onProjectLoaded(IDEProject project, Optional<IDEModule> appModule);
        
        /**
         * Called when a Gradle Daemon is being started
         *
         * @param task Task that is currently running
         */
        void onStartingGradleDaemon(GradleTask task);
        
        /**
         * Called when a task is started
         *
         * @param task Task that is currently running
         * @param name Name of this task that should be shown to user
         */
        void onRunTask(GradleTask task, String name);
        
        /**
         * Notifys about a successful build
         *
         * @param task Task that succeeded
         * @param msg Successful message
         */
        void onBuildSuccessful(GradleTask task, String msg);
        
        /**
         * Notifys about a failed build
         *
         * @param task Task that failed
         * @param msg Failure message
         */
        void onBuildFailed(GradleTask task, String msg);
        
        /**
         * Called to notify client to save any opened files
         */
        void saveFiles();
        
        /**
         * Notifys the client to append the build output that should be shown to user
         *
         * @param task Task that is currently running
         * @param text Output to append
         */
        void appendOutput(GradleTask task, CharSequence text);
        
        /**
         * Called to notify the client to prepare for a build
         */
        void prepareBuild();
    }
    
    /**
     * This FileFilter is used to list java source files in 'generated' dir
     */
    private static final FileFilter JAVA_FILTER = new FileFilter(){

        @Override
        public boolean accept(File p1) {
            return p1.isFile()
                && !p1.isHidden()
                && p1.getName().endsWith(".java")
                && !p1.getName().equals("package-info.java");
        }
    };
}
