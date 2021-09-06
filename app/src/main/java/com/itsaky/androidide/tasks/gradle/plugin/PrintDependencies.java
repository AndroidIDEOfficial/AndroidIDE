package com.itsaky.androidide.tasks.gradle.plugin;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.services.IDEService;
import com.itsaky.androidide.tasks.BaseGradleTask;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class PrintDependencies extends BaseGradleTask {
    
    @Override
    public List<String> getTasks() {
        List<String> tasks = new ArrayList<>();
        for(String task : getCommands().split("\\s")) {
            if(task != null && task.trim().length() > 0)
                tasks.add(task);
        }
        return tasks;
    }

    @Override
    public String getName() {
        return "Start Completion Services";
    }

    @Override
    public String getCommands() {
        return (StudioApp.getInstance().getPrefManager().getBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, true) ? "assembleDebug " : "") + "printDependenciesDebug";
    }

    @Override
    public int getTaskID() {
        return IDEService.TASK_SHOW_DEPENDENCIES;
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
        return Type.ANDROIDIDE_PLUGIN;
    }
}
