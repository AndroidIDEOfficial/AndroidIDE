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

package com.itsaky.androidide.lsp.java.providers.completion

import com.itsaky.androidide.lsp.api.IServerSettings
import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.java.providers.CompletionProvider.MAX_COMPLETION_ITEMS
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel.CASE_SENSITIVE_EQUAL
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.projects.api.ModuleProject
import com.itsaky.androidide.utils.BootClasspathProvider
import com.itsaky.androidide.utils.ClassTrie.Node
import com.sun.source.util.TreePath
import com.sun.tools.javac.api.JavacTrees
import com.sun.tools.javac.code.Symbol.MethodSymbol
import com.sun.tools.javac.model.JavacTypes
import com.sun.tools.javac.tree.JCTree.JCImport
import java.nio.file.Path
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ElementKind.ANNOTATION_TYPE
import javax.lang.model.element.ElementKind.CLASS
import javax.lang.model.element.ElementKind.CONSTRUCTOR
import javax.lang.model.element.ElementKind.ENUM
import javax.lang.model.element.ElementKind.ENUM_CONSTANT
import javax.lang.model.element.ElementKind.FIELD
import javax.lang.model.element.ElementKind.INSTANCE_INIT
import javax.lang.model.element.ElementKind.INTERFACE
import javax.lang.model.element.ElementKind.METHOD
import javax.lang.model.element.ElementKind.STATIC_INIT
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement

/**
 * Provides completions for imports.
 *
 * @author Akash Yadav
 */
class ImportCompletionProvider(
  completingFile: Path,
  cursor: Long,
  compiler: JavaCompilerService,
  settings: IServerSettings,
) : IJavaCompletionProvider(completingFile, cursor, compiler, settings) {

  lateinit var importPath: String

  // TODO add tests for this
  override fun doComplete(
    task: CompileTask,
    path: TreePath,
    partial: String,
    endsWithParen: Boolean,
  ): CompletionResult {
    log.info("...complete import for path:", importPath)

    val names: MutableSet<String> = HashSet()
    val list = mutableListOf<CompletionItem>()

    var pkgName = importPath
    val incomplete: String
    if (!pkgName.contains(".")) {
      pkgName = ""
      incomplete = importPath
    } else if (pkgName.endsWith(".")) {
      pkgName = pkgName.substring(0, pkgName.lastIndex)
      incomplete = ""
    } else {
      pkgName = pkgName.substringBeforeLast(delimiter = '.')
      incomplete = pkgName.substringAfterLast(delimiter = '.')
    }

    val module = compiler.module
    if (module == null) {
      legacyImportPathCompletion(partial, names, list)
      return CompletionResult(list)
    }

    if (pkgName.isEmpty() || pkgName.isBlank()) {
      // User is typing first segment of package name
      // Javac APIs will not work here
      tryCompleteImport(pkgName, incomplete, list, names, module)
      return CompletionResult(list)
    }

    try {
      // Try to complete with Javac APIS

      // Check if pkgName is a package
      val elements = task.task.elements
      val pkg = elements.getAllPackageElements(pkgName)
      val pkgItems = completeFromPackages(pkg, incomplete)
      if (pkgItems.isNotEmpty()) {
        // If any enclosed elements are found, then pkgName is an existing package name
        // But enclosedElements returns only TypeElements, so we manally search and add nested
        // package name segment in the given pkgName
        list.addAll(pkgItems)
        tryCompleteImport(pkgName, incomplete, list, names, module, packageOnly = true)
        return CompletionResult(list)
      }

      // If pkgName is not an existing package name, check if it is a qualified classname
      // A user might be trying to acess members of a member class. So, we keep replacing last '.'
      // until we find a valid qualified name of a class
      var typesForPkg: Set<TypeElement> = setOf()
      val maybeInnerName = StringBuilder(pkgName)
      while (true) {
        val types = elements.getAllTypeElements(maybeInnerName)
        if (types.isNotEmpty()) {
          typesForPkg = types
          break
        }

        if (!maybeInnerName.contains(".")) {
          break
        }
        maybeInnerName.setCharAt(maybeInnerName.lastIndexOf('.'), '$')
      }

      if (typesForPkg.isNotEmpty()) {
        // We found a valid class name
        // Add the accessible class items
        for (type in typesForPkg) {
          val result = completeTypeMembers(task, type, path, incomplete)
          if (result.isNotEmpty()) {
            list.addAll(result)
          }
        }
        return CompletionResult(list)
      }

      return CompletionResult(list)
    } catch (completeMembers: RequireMemberCompletionException) {
      val result = completeTypeMembers(task, path, pkgName, incomplete)
      if (result.isNotEmpty()) {
        list.addAll(result)
      }
    } catch (err: Throwable) {
      log.error("Failed to complete import with Javac APIs", err)
    }

    try {
      tryCompleteImport(pkgName, incomplete, list, names, module)
    } catch (e: RequireMemberCompletionException) {
      // User is trying to access members of a class
      val result = completeTypeMembers(task, path, pkgName, incomplete)
      if (result.isNotEmpty()) {
        list.addAll(result)
      }
    }

    return CompletionResult(list)
  }

  private fun completeFromPackages(
    packageElements: Set<PackageElement>,
    incomplete: String
  ): List<CompletionItem> {
    val list = mutableListOf<CompletionItem>()
    for (pkg in packageElements) {
      for (element in pkg.enclosedElements) {
        if (!isType(element)) {
          continue
        }

        val match = matchLevel(element.simpleName, incomplete)
        if (match == NO_MATCH) {
          continue
        }

        element as TypeElement
        list.add(classItem(element.qualifiedName.toString(), match))
      }
    }
    return list
  }

  private fun completeTypeMembers(
    task: CompileTask,
    path: TreePath,
    pkgName: String,
    incomplete: String
  ): MutableList<CompletionItem> {
    log.info("..complete members of $pkgName")
    val list = mutableListOf<CompletionItem>()
    val elements = task.task.elements
    val types = elements.getAllTypeElements(pkgName)
    for (type in types) {
      val result = completeTypeMembers(task, type, path, incomplete)
      if (result.isEmpty()) {
        continue
      }

      list.addAll(result)
    }

    return list
  }

  private fun completeTypeMembers(
    task: CompileTask,
    type: TypeElement,
    path: TreePath,
    incomplete: String
  ): MutableList<CompletionItem> {
    val list = mutableListOf<CompletionItem>()
    val elements = task.task.elements
    val trees = JavacTrees.instance(task.task.context)
    val jcTypes = JavacTypes.instance(task.task.context)
    val scope = trees.getScope(path)
    val isStatic = (path.leaf as JCImport).isStatic
    if (!trees.isAccessible(scope, type)) {
      // Type not accessible
      log.debug("Type ${type.qualifiedName} is not acessible from import scope")
      return list
    }

    val members = elements.getAllMembers(type)
    for (member in members) {
      if (
        member.kind == CONSTRUCTOR || member.kind == STATIC_INIT || member.kind == INSTANCE_INIT
      ) {
        continue
      }

      val match = matchLevel(member.simpleName, incomplete)
      if (match == NO_MATCH) {
        continue
      }

      if (isType(member)) {
        list.add(classItem(member.simpleName.toString(), match))
        continue
      }

      if (isStatic) {
        val mods = member.modifiers
        if (!mods.contains(STATIC)) {
          continue
        }

        if (!trees.isAccessible(scope, member, jcTypes.getDeclaredType(type))) {
          log.debug("Member ${member.simpleName} is not accessible")
          continue
        }

        if (member.kind == METHOD) {
          list.add(method(task, listOf(member as MethodSymbol), false, match))
          continue
        }

        if (member.kind == FIELD || member.kind == ENUM_CONSTANT) {
          list.add(item(task, member, match))
          continue
        }
      }
    }

    return list
  }

  @Throws(RequireMemberCompletionException::class)
  private fun tryCompleteImport(
    pkgName: String,
    incomplete: String,
    list: MutableList<CompletionItem>,
    names: MutableSet<String>,
    module: ModuleProject,
    packageOnly: Boolean = false
  ) {
    val sourceNode =
      if (pkgName.isEmpty()) module.compileJavaSourceClasses.root
      else module.compileJavaSourceClasses.findNode(pkgName)
    if (sourceNode != null) {
      if (sourceNode.isClass) {
        throw RequireMemberCompletionException()
      }

      addDirectChildNodes(sourceNode, incomplete, list, names, packageOnly)
    }

    val classpathNode =
      if (pkgName.isEmpty()) module.compileClasspathClasses.root
      else module.compileClasspathClasses.findNode(pkgName)
    if (classpathNode != null) {
      if (classpathNode.isClass) {
        throw RequireMemberCompletionException()
      }

      addDirectChildNodes(classpathNode, incomplete, list, names, packageOnly)
    }

    BootClasspathProvider.getAllEntries().forEach {
      val node =
        if (pkgName.isEmpty()) {
          it.root
        } else it.findNode(pkgName)
      if (node != null) {
        if (node.isClass) {
          throw RequireMemberCompletionException()
        }
        addDirectChildNodes(node, incomplete, list, names, packageOnly)
      }
    }
  }

  private fun addDirectChildNodes(
    sourceNode: Node,
    incomplete: String,
    list: MutableList<CompletionItem>,
    names: MutableSet<String>,
    packageOnly: Boolean
  ) {
    for (child in sourceNode.children.values) {
      val match =
        if (incomplete.isEmpty()) {
          CASE_SENSITIVE_EQUAL
        } else {
          matchLevel(child.name, incomplete)
        }

      if (match == NO_MATCH || names.contains(child.name)) {
        continue
      }

      if (packageOnly && child.isClass) {
        continue
      }

      if (child.isClass) {
        list.add(classItem(child.qualifiedName, match))
      } else {
        list.add(packageItem(child.name, match))
      }

      names.add(child.name)
    }
  }

  private fun legacyImportPathCompletion(
    partial: String,
    names: MutableSet<String>,
    list: MutableList<CompletionItem>
  ) {
    for (className in compiler.publicTopLevelTypes()) {
      val matchLevel = matchLevel(className, partial)
      if (matchLevel == NO_MATCH) {
        continue
      }

      val start = importPath.lastIndexOf('.')
      var end = className.indexOf('.', importPath.length)
      if (end == -1) {
        end = className.length
      }
      val segment = className.substring(start + 1, end)
      if (names.contains(segment)) {
        continue
      }
      names.add(segment)
      val isClass = end == importPath.length
      if (isClass) {
        list.add(classItem(className, matchLevel))
      } else {
        list.add(packageItem(segment, matchLevel))
      }

      if (list.size > MAX_COMPLETION_ITEMS) {
        break
      }
    }
  }

  internal fun isType(element: Element): Boolean {
    return isType(element.kind)
  }

  internal fun isType(kind: ElementKind): Boolean {
    return kind == ANNOTATION_TYPE || kind == CLASS || kind == INTERFACE || kind == ENUM
  }

  internal class RequireMemberCompletionException : IllegalStateException()
}
