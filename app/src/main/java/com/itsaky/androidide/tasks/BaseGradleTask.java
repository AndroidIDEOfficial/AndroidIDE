package com.itsaky.androidide.tasks;

import com.itsaky.androidide.services.builder.IDEService;

public abstract class BaseGradleTask implements GradleTask {

    @Override
    public boolean affectsGeneratedSources() {
        return false;
    }
    
	@Override
	public boolean shouldSaveFiles() {
		return getType() == Type.BUILD
			|| getType() == Type.LINT
			|| getTaskID() == IDEService.TASK_DEX;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null
			&& obj instanceof GradleTask
			&& getTaskID() == ((GradleTask) obj).getTaskID()
			&& getType() == ((GradleTask) obj).getType();
	}
}
