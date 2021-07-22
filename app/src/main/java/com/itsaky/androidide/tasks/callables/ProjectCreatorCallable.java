package com.itsaky.androidide.tasks.callables;

import com.itsaky.androidide.models.NewProjectDetails;
import com.itsaky.androidide.utils.ProjectWriter;
import java.util.concurrent.Callable;
import com.itsaky.androidide.models.ProjectTemplate;
import com.itsaky.androidide.interfaces.ProjectWriterCallback;

public class ProjectCreatorCallable implements Callable<Void> {

	private ProjectTemplate template;
	private NewProjectDetails details;
	private ProjectWriterCallback callback;

	public ProjectCreatorCallable(ProjectTemplate template, NewProjectDetails details, ProjectWriterCallback callback) {
		this.template = template;
		this.details = details;
		this.callback = callback;
	}
	
	@Override
	public Void call() throws Exception {
		ProjectWriter.write(template, details, callback);
		return null;
	}
}
