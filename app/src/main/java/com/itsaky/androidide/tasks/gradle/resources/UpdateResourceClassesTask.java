package com.itsaky.androidide.tasks.gradle.resources;

import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.GradleTask;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

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
        if(viewBinding) {
            command = new DatabindingGenBaseClassesDebug().getCommands();
            command += " ";
        }
        command += new ProcessDebugResources().getCommands();
        return command;
    }

    @Override
    public List<String> getTasks() {
        List<String> tasks = new ArrayList<>();
        if(viewBinding) {
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
    public boolean buildsApk() {
        return false;
    }

    @Override
    public File getApk(String buildFolder, String moduleName) {
        return null;
    }

    @Override
    public GradleTask.Type getType() {
        return Type.BUILD;
    }
}
