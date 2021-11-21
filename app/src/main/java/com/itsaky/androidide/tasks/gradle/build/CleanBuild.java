package com.itsaky.androidide.tasks.gradle.build;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.services.build.IDEService;
import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.GradleTask;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CleanBuild extends BaseGradleTask {
	
	@Override
	public String getName() {
		return StudioApp.getInstance().getString(R.string.clean_amp_build);
	}
    
	@Override
	public String getCommands() {
		return "clean build";
	}
    
    @Override
    public List<String> getTasks() {
        return Arrays.asList("clean", "build");
    }

	@Override
	public int getTaskID() {
		return IDEService.TASK_CLEAN_BUILD;
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
