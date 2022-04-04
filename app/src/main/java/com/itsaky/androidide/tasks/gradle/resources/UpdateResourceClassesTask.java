/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
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

package com.itsaky.androidide.tasks.gradle.resources;

import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.GradleTask;

import java.util.ArrayList;
import java.util.List;

public class UpdateResourceClassesTask extends BaseGradleTask {

    private final boolean viewBinding;

    public UpdateResourceClassesTask(boolean viewBinding) {
        this.viewBinding = viewBinding;
    }

    @Override
    public boolean affectsGeneratedSources() {
        return true;
    }

    @Override
    public String getName() {
        return "Update resource classes";
    }

    @Override
    public String getCommands() {
        String command = "";
        if (viewBinding) {
            command = new DatabindingGenBaseClassesDebug().getCommands();
            command += " ";
        }
        command += new ProcessDebugResources().getCommands();
        return command;
    }

    @Override
    public List<String> getTasks() {
        List<String> tasks = new ArrayList<>();
        if (viewBinding) {
            tasks.addAll(new DatabindingGenBaseClassesDebug().getTasks());
        }
        tasks.addAll(new ProcessDebugResources().getTasks());
        return tasks;
    }

    @Override
    public int getTaskID() {
        return TASK_UPDATE_RESOURCE_CLASSES;
    }

    @Override
    public boolean canOutput() {
        return true;
    }

    @Override
    public GradleTask.Type getType() {
        return Type.BUILD;
    }
}
