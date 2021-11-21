package com.itsaky.androidide.tasks.gradle.build;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.services.build.IDEService;
import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.GradleTask;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Clean extends BaseGradleTask {
	
	@Override
	public String getName() {
		return StudioApp.getInstance().getString(R.string.clean_project);
	}
    
	@Override
	public String getCommands() {
		return "clean";
	}
    
    @Override
    public List<String> getTasks() {
        return Arrays.asList(getCommands());
    } 

	@Override
	public int getTaskID() {
		return IDEService.TASK_CLEAN;
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
