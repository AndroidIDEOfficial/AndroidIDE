package com.itsaky.androidide.interfaces;

import java.io.File;

public interface ProjectWriterCallback {
	public void beforeBegin();
	public void onProcessTask(String taskName);
	public void onSuccess(File rootDir);
	public void onFailed(String reason);
}
