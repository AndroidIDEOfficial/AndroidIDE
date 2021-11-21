package com.itsaky.androidide.tasks.gradle.build;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.services.builder.IDEService;
import com.itsaky.androidide.tasks.BaseGradleTask;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;

public class AssembleDebug extends ApkGeneratingTask {
	
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
    protected Set<File> provideApkDirectories(File buildDir) {
        final Set<File> dirs = new HashSet<>();
        dirs.add(new File(buildDir, "outputs/apk/debug"));
        return dirs;
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
