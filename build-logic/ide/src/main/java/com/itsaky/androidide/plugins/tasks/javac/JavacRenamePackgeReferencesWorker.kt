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

import org.gradle.api.file.RegularFileProperty
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters

/**
 * @author Akash Yadav
 */
abstract class JavacRenamePackgeReferencesWorker: WorkAction<JavacRenamePackgeReferencesWorker.Params> {

  companion object {

    private val unsafeToReplacePackages = arrayOf(
      "com.sun",
      "jdk.internal",
      "sun.tools"
    )

    private val safeToReplacePackages = arrayOf(
      "javax.annotation",
      "javax.lang",
      "javax.tools",
    )

    private val replaceTypes = arrayOf(
      "package",
      "import",
      "import static"
    )
  }

  override fun execute() {
    val file = parameters.file.get().asFile
    var content = file.readText()

    for (pck in unsafeToReplacePackages) {
      for (typ in replaceTypes) {
        content = content.replace(Regex.fromLiteral("$typ $pck"), "$typ com.itsaky.androidide.$pck")
      }
    }

    for (pck in safeToReplacePackages) {
      content = content.replace(Regex.fromLiteral(pck), "com.itsaky.androidide.$pck")
    }

    file.writeText(content)
  }

  interface Params: WorkParameters {
    val file: RegularFileProperty
  }
}