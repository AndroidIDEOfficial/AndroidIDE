package com.itsaky.androidide.tasks.gradle;

import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.gradle.build.AssembleDebug;
import com.itsaky.androidide.tasks.gradle.build.AssembleRelease;
import com.itsaky.androidide.tasks.gradle.build.Build;
import com.itsaky.androidide.tasks.gradle.build.Bundle;
import com.itsaky.androidide.tasks.gradle.build.Clean;
import com.itsaky.androidide.tasks.gradle.build.CleanBuild;
import com.itsaky.androidide.tasks.gradle.core.Tasks;
import com.itsaky.androidide.tasks.gradle.lint.Lint;
import com.itsaky.androidide.tasks.gradle.lint.LintDebug;
import com.itsaky.androidide.tasks.gradle.lint.LintRelease;
import com.itsaky.androidide.tasks.gradle.plugin.InitializeIDEProject;
import com.itsaky.androidide.tasks.gradle.resources.ProcessDebugResources;

public class BaseGradleTasks {
	
	public static final BaseGradleTask
	
    TASKS = new Tasks(),
    
	BUILD = new Build(),
	ASSEMBLE_DEBUG = new AssembleDebug(),
	ASSEMBLE_RELEASE = new AssembleRelease(),
	BUNDLE = new Bundle(),
	CLEAN = new Clean(),
	CLEAN_BUILD = new CleanBuild(),
    
	LINT = new Lint(),
	LINT_DEBUG = new LintDebug(),
	LINT_RELEASE = new LintRelease(),
    
    PROCESS_DEBUG_RESOURCES = new ProcessDebugResources(),
    
    INITIALIZE_IDE_PROJECT = new InitializeIDEProject();
}
