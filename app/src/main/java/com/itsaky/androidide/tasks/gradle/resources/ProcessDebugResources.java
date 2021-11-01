package com.itsaky.androidide.tasks.gradle.resources;

import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.GradleTask;
import java.io.File;
import java.util.List;
import java.util.Arrays;

public class ProcessDebugResources extends BaseGradleTask {

    @Override
    public boolean affectsGeneratedSources() {
        return true;
    }
    
    @Override
    public String getName() {
        return "processDebugResources";
    }

    @Override
    public String getCommands() {
        return "processDebugResources";
    }

    @Override
    public List<String> getTasks() {
        return Arrays.asList(getCommands());
    }

    @Override
    public int getTaskID() {
        return TASK_PROCESS_DEBUG_RESOURCES;
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
