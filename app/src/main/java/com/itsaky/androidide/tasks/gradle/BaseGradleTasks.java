/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/

package com.itsaky.androidide.tasks.gradle;

import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.gradle.build.AssembleDebug;
import com.itsaky.androidide.tasks.gradle.build.AssembleRelease;
import com.itsaky.androidide.tasks.gradle.build.Build;
import com.itsaky.androidide.tasks.gradle.build.Bundle;
import com.itsaky.androidide.tasks.gradle.build.Clean;
import com.itsaky.androidide.tasks.gradle.build.CleanBuild;
import com.itsaky.androidide.tasks.gradle.lint.Lint;
import com.itsaky.androidide.tasks.gradle.lint.LintDebug;
import com.itsaky.androidide.tasks.gradle.lint.LintRelease;
import com.itsaky.androidide.tasks.gradle.plugin.InitializeIDEProject;
import com.itsaky.androidide.tasks.gradle.resources.ProcessDebugResources;

public class BaseGradleTasks {

    public static final BaseGradleTask BUILD = new Build(),
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
