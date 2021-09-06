package com.itsaky.androidide.tasks.gradle;

import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.gradle.build.AssembleDebug;
import com.itsaky.androidide.tasks.gradle.build.AssembleRelease;
import com.itsaky.androidide.tasks.gradle.build.Build;
import com.itsaky.androidide.tasks.gradle.build.Bundle;
import com.itsaky.androidide.tasks.gradle.build.Clean;
import com.itsaky.androidide.tasks.gradle.build.CleanBuild;
import com.itsaky.androidide.tasks.gradle.core.Tasks;
import com.itsaky.androidide.tasks.gradle.dex.MergeDex;
import com.itsaky.androidide.tasks.gradle.dex.MergeExtDex;
import com.itsaky.androidide.tasks.gradle.dex.MergeLibAndProjectDex;
import com.itsaky.androidide.tasks.gradle.lint.Lint;
import com.itsaky.androidide.tasks.gradle.lint.LintDebug;
import com.itsaky.androidide.tasks.gradle.lint.LintRelease;
import com.itsaky.androidide.tasks.gradle.plugin.PrintDependencies;

public class BaseGradleTasks {
	
	public static BaseGradleTask
	
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
	
	MERGE_DEX = new MergeDex(),
	MERGE_EXT_DEX = new MergeExtDex(),
	MERGE_LIB_AND_PROJECT_DEX = new MergeLibAndProjectDex(),
    
    SHOW_DEPENDENCIES = new PrintDependencies();
}
