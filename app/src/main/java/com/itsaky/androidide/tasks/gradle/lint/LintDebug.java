package com.itsaky.androidide.tasks.gradle.lint;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.services.builder.IDEService;
import com.itsaky.androidide.tasks.BaseGradleTask;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class LintDebug extends BaseGradleTask {
    
	@Override
	public String getName() {
		return StudioApp.getInstance().getString(R.string.lint_debug);
	}
    
    @Override
	public String getCommands() {
		return "lintDebug";
	}
    
    @Override
    public List<String> getTasks() {
        return Arrays.asList("lintDebug");
    }

	@Override
	public int getTaskID() {
		return IDEService.TASK_LINT_DEBUG;
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
