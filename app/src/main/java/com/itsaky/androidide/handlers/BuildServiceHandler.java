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
package com.itsaky.androidide.handlers;

import android.view.View;

import com.itsaky.androidide.R;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.project.AndroidProject;
import com.itsaky.androidide.project.IDEModule;
import com.itsaky.androidide.project.IDEProject;
import com.itsaky.androidide.services.builder.BuildListener;
import com.itsaky.androidide.services.builder.IDEService;
import com.itsaky.androidide.tasks.GradleTask;
import com.itsaky.androidide.tasks.gradle.build.ApkGeneratingTask;
import com.itsaky.androidide.utils.Environment;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class BuildServiceHandler extends IDEHandler implements BuildListener {

    private IDEService service;
    private boolean shouldInstallApk = false;

    public BuildServiceHandler(Provider provider) {
        super(provider);
    }

    public boolean isBuilding() {
        return getService().isBuilding();
    }

    public IDEService getService() {
        return service;
    }

    @Override
    public void start() {
        Objects.requireNonNull(this.activity());

        AndroidProject project = provider.provideAndroidProject();
        Objects.requireNonNull(project);
        Objects.requireNonNull(project.getProjectPath());

        this.service = new IDEService(new File(project.getProjectPath()));
        this.service.setListener(this);
    }

    @Override
    public void stop() {
        this.service.stopAllDaemons();
        this.service.exit();
    }

    @Override
    public void onBuildModified() {
        // Does nothing currently
    }

    @Override
    public void onProjectLoaded(IDEProject project, Optional<IDEModule> appModule) {
        project.iconPath = provider.provideAndroidProject().getIconPath();
        activity().setIDEProject(project);

        if (appModule.isPresent()) {
            IDEModule app = appModule.get();
            activity().setStatus(activity().getString(R.string.msg_starting_completion));

            final List<String> paths =
                    app.dependencies.stream()
                            .filter(this::isClasspathValid)
                            .collect(Collectors.toList());

            provider.provideAndroidProject().setClassPaths(paths);

            File androidJar =
                    new File(
                            Environment.ANDROID_HOME,
                            String.format("platforms/%s/android.jar", app.compileSdkVersion));
            if (androidJar.exists()) {
                Environment.setBootClasspath(androidJar);
                provider.provideAndroidProject().addClasspath(androidJar.getAbsolutePath());
                activity().updateServices();
            } else {
                activity().setStatus("android.jar not found!");
            }
        } else {
            activity().setStatus("Cannot get :app module...");
        }
    }

    @Override
    public void onStartingGradleDaemon(GradleTask task) {
        activity().setStatus(activity().getString(R.string.msg_starting_daemon));
    }

    @Override
    public void onRunTask(GradleTask task, String name) {
        if (task != null && task.canOutput()) {
            activity().setStatus(service.typeString(task.getTaskID()) + " " + name);
        }
    }

    @Override
    public void onBuildSuccessful(GradleTask task, String msg) {
        appendOutput(task, msg);
        analyzeCurrentFile();
        if (task == null || activity() == null) {
            return;
        }
        if (task.canOutput()) {
            activity().setStatus(msg);
        }

        // If this task generates APK, ask user if he/she wants to install them
        if (shouldInstallApk && task instanceof ApkGeneratingTask) {
            final IDEProject project = activity().provideIDEProject();
            final Optional<IDEModule> app =
                    project.getModuleByPath(":app"); // TODO Handle multiple application modules
            if (app.isPresent()) {
                final String path = app.get().projectDir;
                if (path != null) {
                    final File buildDir = new File(path, "build");
                    //                    installApks(((ApkGeneratingTask) task).getApks(buildDir));
                }
            }
        }

        activity()
                .getApp()
                .getPrefManager()
                .putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false);
        activity().getBinding().buildProgressIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onBuildFailed(GradleTask task, String msg) {
        appendOutput(task, msg);
        analyzeCurrentFile();
        if (task == null) {
            return;
        }
        if (task.canOutput()) {
            activity().setStatus(msg);
        }

        activity()
                .getApp()
                .getPrefManager()
                .putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false);
        activity().getBinding().buildProgressIndicator.setVisibility(View.GONE);
    }

    @Override
    public void saveFiles() {
        activity().saveAll(false, false);
    }

    @Override
    public void appendOutput(GradleTask task, CharSequence text) {
        if (text == null) {
            return;
        }
        if (task != null && !task.canOutput()) {
            return;
        }

        activity().appendBuildOut(text.toString());
    }

    @Override
    public void prepareBuild() {}

    private void analyzeCurrentFile() {
        final var editorView = activity().getCurrentEditor();
        if (editorView != null) {
            final var editor = editorView.getEditor();
            if (editor != null) {
                editor.analyze();
            }
        }
    }

    private boolean isClasspathValid(String path) {
        if (path == null || path.trim().length() <= 0 || path.trim().equals("/")) {
            return false;
        }
        File file = new File(path);
        return file.isFile()
                && file.getName().endsWith(".jar")
                // Release R.jar shouldn't be included in classpath
                && !file.getAbsolutePath().endsWith("/release/R.jar");
    }

    public void assembleDebug(boolean shouldInstallApk) {
        setShouldInstallApk(shouldInstallApk);
        getService().assembleDebug();
    }

    public void setShouldInstallApk(boolean install) {
        this.shouldInstallApk = install;
    }
}
