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

package com.itsaky.androidide.lsp.java.providers.definition

import com.itsaky.androidide.lsp.api.IServerSettings
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.java.utils.FindHelper
import com.itsaky.androidide.models.Location
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.progress.ICancelChecker
import jdkx.lang.model.element.Element
import openjdk.source.util.Trees
import java.nio.file.Path

/**
 * Provides definition for local elements.
 *
 * @author Akash Yadav
 */
class LocalDefinitionProvider(
  position: Position,
  completingFile: Path,
  compiler: JavaCompilerService,
  settings: IServerSettings, cancelChecker: ICancelChecker,
) : IJavaDefinitionProvider(position, completingFile, compiler, settings, cancelChecker) {

  override fun doFindDefinition(element: Element): List<Location> {
    return compiler.compile(file).get {
      val trees = Trees.instance(it.task)
      val path = trees.getPath(element)
      if (path == null) {
        log.error("TreePath of element is null. Cannot find definition. Element is {}", element)
        return@get emptyList<Location>()
      }

      var name = element.simpleName
      if (name.contentEquals("<init>")) {
        name = element.enclosingElement.simpleName
      }

      abortIfCancelled()
      return@get listOf(FindHelper.location(it, path, name))
    }
  }
}
