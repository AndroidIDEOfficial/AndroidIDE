package com.itsaky.androidide.tasks.gradle.resources;

import com.itsaky.androidide.tasks.BaseGradleTask;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DatabindingGenBaseClassesDebug extends BaseGradleTask {

    @Override
    public boolean affectsGeneratedSources() {
        return true;
    }
    
    @Override
    public String getName() {
        return "Generate databinding classes";
    }

    @Override
    public String getCommands() {
        return "databindingGenBaseClassesDebug";
    }

    @Override
    public List<String> getTasks() {
        return Arrays.asList(getCommands());
    }

    @Override
    public int getTaskID() {
        return DATABINDING_GEN_BASE_CLASSES_DEBUG;
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
    public Type getType() {
        return Type.BUILD;
    }
}
