package com.itsaky.androidide.interfaces;

public interface ProjectWriterCallback {
	public void beforeBegin();
	public void onProcessTask(String taskName);
	public void onSuccess();
	public void onFailed(String reason);
}
