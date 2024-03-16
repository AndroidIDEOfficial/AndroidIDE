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
import com.itsaky.androidide.lsp.java.compiler.SourceFileObject
import com.itsaky.androidide.lsp.java.providers.DefinitionProvider
import com.itsaky.androidide.lsp.java.utils.FindHelper
import com.itsaky.androidide.models.Location
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.progress.ICancelChecker
import com.itsaky.androidide.utils.DocumentUtils.isSameFile
import jdkx.lang.model.element.Element
import jdkx.lang.model.element.TypeElement
import jdkx.tools.JavaFileObject
import openjdk.source.util.Trees
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Finds definition for erroneous elements.
 *
 * @author Akash Yadav
 */
class ErroneousDefinitionProvider(
  position: Position,
  completingFile: Path,
  compiler: JavaCompilerService,
  settings: IServerSettings, cancelChecker: ICancelChecker,
) : IJavaDefinitionProvider(position, completingFile, compiler, settings, cancelChecker) {

  override fun doFindDefinition(element: Element): List<Location> {
    val name = element.simpleName ?: return DefinitionProvider.NOT_SUPPORTED
    val parent = element.enclosingElement as? TypeElement ?: return DefinitionProvider.NOT_SUPPORTED
    val className = parent.qualifiedName.toString()
    val memberName = name.toString()
    return findAllMembers(className, memberName)
  }

  private fun findAllMembers(className: String, memberName: String): List<Location> {
    val otherFile = compiler.findAnywhere(className)
    abortIfCancelled()
    if (!otherFile.isPresent) {
      log.error("Cannot find source file for class: {}", className)
      return emptyList()
    }

    val fileAsSource = SourceFileObject(file)
    var sources = listOf(fileAsSource, otherFile.get())
    if (isSameFile(Paths.get(otherFile.get().toUri()), file)) {
      sources = listOf<JavaFileObject>(fileAsSource)
    }

    abortIfCancelled()

    return compiler.compile(sources).get { task ->
      val locations = mutableListOf<Location>()
      val trees = Trees.instance(task.task)
      val elements = task.task.elements
      val parentClass = elements.getTypeElement(className)

      abortIfCancelled()
      for (member in elements.getAllMembers(parentClass)) {
        if (!member.simpleName.contentEquals(memberName)) continue
        val path = trees.getPath(member) ?: continue
        val location = FindHelper.location(task, path, memberName)
        abortIfCancelled()
        locations.add(location)
      }

      locations
    }
  }
}
