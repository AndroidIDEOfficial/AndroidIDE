package com.itsaky.androidide.tasks.callables;

import com.itsaky.androidide.models.gradle.Artifact;
import com.itsaky.androidide.utils.Environment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class ArtifactFinderCallable implements Callable<List<File>> {

	private final List<Artifact> artifacts;

	public ArtifactFinderCallable(List<Artifact> artifacts) {
		this.artifacts = artifacts;
	}

	@Override
	public List<File> call() throws Exception {
		final List<File> jars = new ArrayList<>();
		for(Artifact artifact : artifacts) {
			jars.addAll(Environment.findJarForArtifact(artifact));
		}
		return jars;
	}
}
