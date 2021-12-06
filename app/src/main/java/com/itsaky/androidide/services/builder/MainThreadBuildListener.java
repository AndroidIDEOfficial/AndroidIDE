/************************************************************************************
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
**************************************************************************************/
package com.itsaky.androidide.services.builder;

import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.project.IDEModule;
import com.itsaky.androidide.project.IDEProject;
import com.itsaky.androidide.tasks.GradleTask;
import java.util.Optional;

class MainThreadBuildListener implements BuildListener {

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
