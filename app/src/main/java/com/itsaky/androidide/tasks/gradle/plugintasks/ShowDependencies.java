package com.itsaky.androidide.tasks.gradle.plugintasks;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.services.IDEService;
import com.itsaky.androidide.tasks.BaseGradleTask;
import java.io.File;
import com.itsaky.androidide.utils.PreferenceManager;

public class ShowDependencies extends BaseGradleTask {
	
	@Override
	public String getName() {
		return "Start Completion Services";
	}
	
	@Override
	public String getCommands() {
		return (StudioApp.getInstance().getPrefManager().getBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, true) ? "assembleDebug " : "") + "showDependenciesDebug";
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
