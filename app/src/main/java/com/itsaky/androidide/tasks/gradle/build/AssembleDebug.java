package com.itsaky.androidide.tasks.gradle.build;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.services.IDEService;
import com.itsaky.androidide.tasks.BaseGradleTask;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AssembleDebug extends BaseGradleTask {
	
	private boolean installApk = true;

	public AssembleDebug setInstallApk(boolean installApk) {
		this.installApk = installApk;
		return this;
	}
	
	@Override
	public String getName() {
		return StudioApp.getInstance().getString(R.string.build_debug);
	}

	@Override
	public boolean buildsApk() {
		return installApk;
	}

	@Override
	public File getApk(String buildFolder, String moduleName) {
		return new File(buildFolder, "outputs/apk/debug/" + moduleName + "-debug.apk");
	}
	
	@Override
	public String getCommands() {
		return "assembleDebug";
	}
    
    @Override
    public List<String> getTasks() {
        return Arrays.asList(getCommands());
    }
	
	@Override
	public int getTaskID() {
		return IDEService.TASK_ASSEMBLE_DEBUG;
	}

	@Override
	public boolean canOutput() {
		return true;
	}
	
	@Override
	public Type getType() {
		return Type.BUILD;
	}
}
