/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.services.builder;

import static com.itsaky.androidide.managers.ToolsManager.getCommonAsset;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.google.gson.Gson;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.project.IDEModule;
import com.itsaky.androidide.project.IDEProject;
import com.itsaky.androidide.shell.IProcessExecutor;
import com.itsaky.androidide.shell.IProcessExitListener;
import com.itsaky.androidide.shell.ProcessExecutorFactory;
import com.itsaky.androidide.shell.ProcessStreamsHolder;
import com.itsaky.androidide.tasks.GradleTask;
import com.itsaky.androidide.tasks.gradle.BaseGradleTasks;
import com.itsaky.androidide.tasks.gradle.resources.UpdateResourceClassesTask;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.InputStreamLineReader;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.toaster.Toaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;

public class IDEService {

    public static final int TASK_SHOW_DEPENDENCIES = GradleTask.TASK_SHOW_DEPENDENCIES;
    public static final int TASK_ASSEMBLE_DEBUG = GradleTask.TASK_ASSEMBLE_DEBUG;
    public static final int TASK_ASSEMBLE_RELEASE = GradleTask.TASK_ASSEMBLE_RELEASE;
    public static final int TASK_BUILD = GradleTask.TASK_BUILD;
    public static final int TASK_BUNDLE = GradleTask.TASK_BUNDLE;
    public static final int TASK_CLEAN = GradleTask.TASK_CLEAN;
    public static final int TASK_CLEAN_BUILD = GradleTask.TASK_CLEAN_BUILD;
    public static final int TASK_DEX = GradleTask.TASK_DEX;
    public static final int TASK_LINT = GradleTask.TASK_LINT;
    public static final int TASK_LINT_DEBUG = GradleTask.TASK_LINT_DEBUG;
    public static final int TASK_LINT_RELEASE = GradleTask.TASK_LINT_RELEASE;
    public static final Logger LOG = Logger.newInstance("IDEService");
    private final File projectRoot;
    private final IProcessExecutor processExecutor;
    private BuildListener listener;
    private GradleTask currentTask;
    private IDEProject mIDEProject;
    private IDEModule mAppModule;
    private final InputStreamLineReader.OnReadListener mOutputReadListener = this::onBuildOutput;
    private boolean isBuilding = false;
    private final IProcessExitListener mProcessExitListener =
            new IProcessExitListener() {
                @Override
                public void onExit(int code) {
                    isBuilding = false;
                    if (listener != null) {
                        final String msg = getString(R.string.msg_gradle_terminated, code);
                        if (code == 0) {
                            onBuildSuccessful(currentTask, msg);
                        } else {
                            onBuildFailed(currentTask, msg);
                        }
                    }
                }
            };

    public IDEService(File projectRoot) {
        this.projectRoot = projectRoot;
        this.processExecutor = ProcessExecutorFactory.commonExecutor();
    }

    public IDEService setListener(final BuildListener listener) {
        this.listener = listener == null ? null : new MainThreadBuildListener(listener);
        return this;
    }

    protected void onBuildOutput(String line) {
        if (listener == null || line == null) return;
        line = line.trim();

        String PROJECT_INITIALIZED = ">>> PROJECT INITIALIZED <<<";
        if (line.contains(PROJECT_INITIALIZED)) {
            readIdeProject();
            return;
        }

        String text = line;

        final var app = StudioApp.getInstance();
        // Try to shorten common file paths
        if (line.contains(app.getRootDir().getAbsolutePath())) {
            text = line.replace(app.getRootDir().getAbsolutePath(), "HOME");
        }
        if (line.contains(
                Objects.requireNonNull(app.getRootDir().getParentFile()).getAbsolutePath())) {
            text = line.replace(app.getRootDir().getParentFile().getAbsolutePath(), "ROOT");
        }

        listener.appendOutput(currentTask, text);

        String RUN_TASK = "> Task";
        String STARTING_DAEMON = "Starting a ";
        if (line.startsWith(RUN_TASK)) {
            listener.onRunTask(currentTask, line.trim());
        } else if (line.startsWith(STARTING_DAEMON)) {
            listener.onStartingGradleDaemon(currentTask);
        }
    }

    protected void onBuildSuccessful(GradleTask task, String msg) {
        if (currentTask != null && currentTask.getTaskID() != TASK_SHOW_DEPENDENCIES) {
            listener.onBuildSuccessful(task, msg.trim());
        }
        appendOutputSeparator();

        if (currentTask != null && currentTask.affectsGeneratedSources()) {
            notifyExternalSourceChange();
        }
    }

    protected void onBuildFailed(GradleTask task, @NonNull String msg) {
        isBuilding = false;
        listener.onBuildFailed(task, msg.trim());
        appendOutputSeparator();
    }

    private void readIdeProject() {
        // Parsing project data is meaningless if there is no one listening...
        if (listener == null) {
            return;
        }

        final BuildListener local = listener;
        new Thread(
                        () -> {
                            mIDEProject =
                                    new Gson()
                                            .fromJson(
                                                    FileIOUtils.readFile2String(
                                                            Environment.PROJECT_DATA_FILE),
                                                    IDEProject.class);
                            if (mIDEProject != null) {
                                Optional<IDEModule> appModule = mIDEProject.getModuleByPath(":app");
                                appModule.ifPresent(ideModule -> mAppModule = ideModule);
                                local.onProjectLoaded(mIDEProject, appModule);
                            } else {
                                StudioApp.getInstance()
                                        .toast(
                                                com.itsaky.androidide.R.string
                                                        .msg_init_project_failed,
                                                Toaster.Type.ERROR);
                            }
                        })
                .start();
    }

    private void appendOutputSeparator() {
        listener.appendOutput(currentTask, "\n\n");
    }

    public void exit() {
        // Delete the tmp directory. It is not needed anymore...
        FileUtils.delete(Environment.TMP_DIR);
    }

    @NonNull
    private String currentTime() {
        String pattern = "HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern, Locale.US);
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }

    private String getString(@StringRes int id) {
        return StudioApp.getInstance().getString(id);
    }

    private String getString(@StringRes int id, Object... format) {
        return StudioApp.getInstance().getString(id, format);
    }

    private void notifyExternalSourceChange() {
        // TODO Watch for file changes using a file observer
        //    instead of depending on the build
        if (listener != null) {
            listener.onBuildModified();
        }
    }

    @NonNull
    private String[] getArguments(List<String> tasks) {
        final PreferenceManager prefs = StudioApp.getInstance().getPrefManager();
        final List<String> args = new ArrayList<>();

        // 'bash' shell is always preferred
        if (Environment.SHELL.exists()) {
            args.add(Environment.SHELL.getAbsolutePath());
        } else {
            args.add(Environment.BUSYBOX.getAbsolutePath());
            args.add("sh");
        }

        args.add(new File(projectRoot, "gradlew").getAbsolutePath());
        args.addAll(asAppTasks(tasks));
        args.add("--init-script");
        args.add(Environment.INIT_SCRIPT.getAbsolutePath());

        if (prefs.isStackTraceEnabled()) {
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

        return args.toArray(new String[0]);
    }

    private Collection<? extends String> asAppTasks(@NonNull List<String> tasks) {
        return tasks.stream()
                .filter(Objects::nonNull)
                .map(this::asAppModuleTask)
                .collect(Collectors.toList());
    }

    @Contract(pure = true)
    private String asAppModuleTask(@NonNull String name) {
        return name.startsWith(":app:") ? name : ":app:" + name;
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

        execTask(
                task,
                isGradleWrapperAvailable()
                        ? CompletableFuture.completedFuture(Boolean.TRUE)
                        : installWrapper());
    }

    private void execTask(GradleTask task, CompletableFuture<Boolean> installing) {

        final Runnable taskRunner =
                () -> {
                    if (installing != null) {
                        Boolean result;
                        String failure = "";
                        try {
                            result = installing.get();
                        } catch (Throwable th) {
                            result = Boolean.FALSE;
                            failure = th.getMessage();
                        }

                        if (result.equals(Boolean.FALSE)) {
                            if (listener != null) {
                                listener.appendOutput(
                                        task, "-------------------- ERROR --------------------");
                                listener.appendOutput(
                                        task,
                                        getString(
                                                R.string.msg_gradlew_installation_failed, failure));
                                listener.appendOutput(
                                        task, "-----------------------------------------------");
                            }
                            return;
                        }
                    }

                    if (listener != null) {

                        final ProcessStreamsHolder holder = new ProcessStreamsHolder();

                        listener.saveFiles();

                        currentTask = task;
                        listener.appendOutput(
                                task,
                                getString(R.string.msg_task_begin, currentTime(), task.getName()));
                        try {

                            Environment.mkdirIfNotExits(Environment.TMP_DIR);

                            processExecutor.execAsync(
                                    holder,
                                    mProcessExitListener,
                                    projectRoot.getAbsolutePath(),
                                    true,
                                    getArguments(task.getTasks()));
                            isBuilding = true;

                            startReading(holder.in);

                            listener.prepareBuild();
                        } catch (Throwable e) {
                            listener.onBuildFailed(task, e.getMessage());
                        }
                    }
                };

        final Thread thread = new Thread(taskRunner, "GradleTaskExecutor");
        thread.setDaemon(true);
        thread.start();
    }

    private void startReading(InputStream in) {
        final Thread reader =
                new Thread(
                        new InputStreamLineReader(in, mOutputReadListener),
                        "GradleProcessOutputReader");
        reader.setDaemon(true);
        reader.start();
    }

    private CompletableFuture<Boolean> installWrapper() {
        if (listener != null) {
            listener.appendOutput(null, "-------------------- NOTE --------------------");
            listener.appendOutput(null, getString(R.string.msg_installing_gradlew));
            listener.appendOutput(null, "----------------------------------------------");
        }

        return CompletableFuture.supplyAsync(
                () -> {
                    final File extracted = new File(Environment.TMP_DIR, "gradle-wrapper.zip");
                    if (ResourceUtils.copyFileFromAssets(
                            getCommonAsset("gradle-wrapper.zip"), extracted.getAbsolutePath())) {
                        try {
                            final List<File> files = ZipUtils.unzipFile(extracted, projectRoot);
                            if (files != null && !files.isEmpty()) {
                                return Boolean.TRUE;
                            }
                        } catch (IOException e) {
                            return Boolean.FALSE;
                        }
                    }

                    return Boolean.FALSE;
                });
    }

    public boolean isGradleWrapperAvailable() {
        if (projectRoot == null || !projectRoot.exists()) {
            return false;
        }
        final File gradlew = new File(projectRoot, "gradlew");
        final File gradleWrapperJar = new File(projectRoot, "gradle/wrapper/gradle-wrapper.jar");
        final File gradleWrapperProps =
                new File(projectRoot, "gradle/wrapper/gradle-wrapper.properties");

        return gradlew.exists() && gradleWrapperJar.exists() && gradleWrapperProps.exists();
    }

    public void assembleDebug() {
        execTask(BaseGradleTasks.ASSEMBLE_DEBUG);
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
        StudioApp.getInstance()
                .newShell(null)
                .bgAppend(
                        String.format(
                                Locale.US,
                                "cd '%s' && sh gradlew --stop",
                                projectRoot.getAbsolutePath()));
        if (isBuilding()) {
            if (listener != null)
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
        execTask(
                new UpdateResourceClassesTask(mAppModule != null && mAppModule.viewBindingEnabled));
    }

    public boolean isBuilding() {
        return isBuilding;
    }

    public String typeString(int type) {
        switch (type) {
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
            case TASK_BUNDLE:
                return getString(R.string.create_aab);
            case TASK_DEX:
                return getString(R.string.compiling);
            default:
                return "";
        }
    }
}
