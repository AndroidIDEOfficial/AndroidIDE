package com.itsaky.androidide.tasks.gradle.plugin;

import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.GradleTask;
import java.io.File;
import java.util.List;
import java.util.Arrays;

public class InitializeIDEProject extends BaseGradleTask {

    @Override
    public String getName() {
        return "initializeIDEProject";
    }

    @Override
    public String getCommands() {
        return "initializeIDEProject";
    }

    @Override
    public List<String> getTasks() {
        return Arrays.asList(getCommands());
    }

    @Override
    public int getTaskID() {
        return TASK_INIT_PROJECT;
    }

    @Override
    public boolean canOutput() {
        return false;
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
        return Type.ANDROIDIDE_PLUGIN;
    }
}
