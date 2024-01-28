/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.plugins.tasks.javac

import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkerExecutor
import javax.inject.Inject

/**
 * @author Akash Yadav
 */
abstract class JavacRenamePackageReferencesTask : SourceTask() {

  @get:Inject
  abstract val workerExecutor: WorkerExecutor

  @TaskAction
  fun doRenameReferences() {
    val workQueue = workerExecutor.noIsolation()
    for (file in source.files) {
      workQueue.submit(JavacRenamePackgeReferencesWorker::class.java) {
        this.file.set(file)
      }
    }
  }
}