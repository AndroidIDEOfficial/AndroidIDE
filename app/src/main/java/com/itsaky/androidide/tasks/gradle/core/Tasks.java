package com.itsaky.androidide.tasks.gradle.core;

import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.GradleTask;
import java.io.File;
import java.util.List;
import java.util.Arrays;

public class Tasks extends BaseGradleTask {
    
    @Override
    public String getName() {
        return "Show tasks";
    }
    
    @Override
    public String getCommands() {
        return "tasks";
    }
    
    @Override
    public List<String> getTasks() {
        return Arrays.asList(getCommands());
    }
    
    @Override
    public int getTaskID() {
        return GradleTask.TASK_TASKS;
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
        return Type.HELP;
    }
}
