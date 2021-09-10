package com.itsaky.androidide.services;

import android.text.TextUtils;
import androidx.annotation.StringRes;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.google.gson.Gson;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.project.IDEProject;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.tasks.GradleTask;
import com.itsaky.androidide.tasks.gradle.BaseGradleTasks;
import com.itsaky.androidide.tasks.gradle.build.AssembleDebug;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.toaster.Toaster;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IDEService implements ShellServer.Callback {

    private ShellServer shell;
    private BuildListener listener;
    private GradleTask currentTask;
    private StudioApp app;

    private IDEProject mIDEProject;
    private AndroidProject project;
    
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
    
    public IDEService(AndroidProject project) {
        this.app = StudioApp.getInstance();
        this.project = project;
        this.shell = app.newShell(this);
        this.isRunning = true;
    }
    
    public IDEService setListener(BuildListener listener) {
        this.listener = listener;
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
        } else
        if(charSequence.toString().contains(BUILD_FAILED)) {
            isBuilding = false;
            if(recreateShellOnDone)
                createShell();
            if(currentTask != null) {
                if(currentTask.getTaskID() == TASK_SHOW_DEPENDENCIES) 
                    listener.onGetDependenciesFailed();
                else listener.onBuildFailed(currentTask, line.trim());
            }
            appendOutputSeparator();
            
            execTask(BaseGradleTasks.TASKS);
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
            if(local != null) {
                local.onProjectLoaded(mIDEProject);
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

    private String getArguments(List<String> tasks) {
        final PreferenceManager prefs = app.getPrefManager();
        final List<String> args = new ArrayList<>();
        
        args.add("gradle");
        args.addAll(tasks);
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
            shell.bgAppend(String.format("cd \"%s\"", new File(project.getProjectPath(), "app").getAbsolutePath()));
            shell.bgAppend(getArguments(task.getTasks()));
			isBuilding = true;
            listener.prepare();
        }
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

    public void mergeLibAndProjectDex() {
        execTask(BaseGradleTasks.MERGE_LIB_AND_PROJECT_DEX);
    }

    public void mergeExtDex() {
        execTask(BaseGradleTasks.MERGE_EXT_DEX);
    }

    public void mergeDex() {
        execTask(BaseGradleTasks.MERGE_DEX);
    }

    public void clean() {
        execTask(BaseGradleTasks.CLEAN);
    }

    public void cleanAndRebuild() {
        execTask(BaseGradleTasks.CLEAN_BUILD);
    }

    public void stopAllDaemons() {
        app.newShell(null).bgAppend("gradle --stop");
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

    public static interface BuildListener {
        void onProjectLoaded(IDEProject project);
        void onGetDependencies(List<String> dependencies);
        void onGetDependenciesFailed();
        
        void onStartingGradleDaemon(GradleTask task);
        void onRunTask(GradleTask task, String name);
        void onBuildSuccessful(GradleTask task, String msg);
        void onBuildFailed(GradleTask task, String msg);
        void saveFiles();
        
        void appendOutput(GradleTask task, CharSequence text);
        void prepare();
    }
}
