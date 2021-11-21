package com.itsaky.androidide.tasks.gradle.lint;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.services.builder.IDEService;
import com.itsaky.androidide.tasks.BaseGradleTask;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Lint extends BaseGradleTask {
	
	@Override
	public String getName() {
		return StudioApp.getInstance().getString(R.string.lint);
	}

	@Override
	public String getCommands() {
		return "lint";
	}
    
    @Override
    public List<String> getTasks() {
        return Arrays.asList("lint");
    }

	@Override
	public int getTaskID() {
		return IDEService.TASK_LINT;
	}

	@Override
	public boolean canOutput() {
		return true;
	}

	@Override
	public Type getType() {
		return Type.LINT;
	}
}
