package com.itsaky.androidide.tasks.gradle.build;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.services.IDEService;
import com.itsaky.androidide.tasks.GradleTask;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Build extends ApkGeneratingTask {
	
	@Override
	public String getName() {
		return StudioApp.getInstance().getString(R.string.build);
	}

	@Override
    protected Set<File> provideApkDirectories(File buildDir) {
        final Set<File> dirs = new HashSet<>();
        dirs.add(new File(buildDir, "outputs/apk/debug"));
        dirs.add(new File(buildDir, "outputs/apk/release"));
        return dirs;
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
