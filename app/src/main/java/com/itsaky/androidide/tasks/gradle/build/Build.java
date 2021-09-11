package com.itsaky.androidide.tasks.gradle.build;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.services.IDEService;
import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.GradleTask;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Build extends BaseGradleTask {
	
	@Override
	public String getName() {
		return StudioApp.getInstance().getString(R.string.build);
	}

	@Override
	public boolean buildsApk() {
		return true;
	}

	@Override
	public File getApk(String buildFolder, String moduleName) {
		return new File(buildFolder, "outputs/apk/debug/" + moduleName + "-debug.apk");
	}
	

	@Override
	public String getCommands() {
		return "build";
	}
    
    @Override
    public List<String> getTasks() {
        return Arrays.asList(getCommands());
    }

	@Override
	public int getTaskID() {
		return IDEService.TASK_BUILD;
	}

	@Override
	public boolean canOutput() {
		return true;
	}

	@Override
	public GradleTask.Type getType() {
		return GradleTask.Type.BUILD;
	}
}
