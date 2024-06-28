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
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import com.itsaky.androidide.projects.ModuleProject
import com.itsaky.androidide.projects.util.BootClasspathProvider
import com.itsaky.androidide.utils.ClassTrie
import com.itsaky.androidide.utils.ClassTrie.Node
import jdkx.lang.model.element.Element
import jdkx.lang.model.element.ElementKind
import jdkx.lang.model.element.ElementKind.ANNOTATION_TYPE
import jdkx.lang.model.element.ElementKind.CLASS
import jdkx.lang.model.element.ElementKind.CONSTRUCTOR
import jdkx.lang.model.element.ElementKind.ENUM
import jdkx.lang.model.element.ElementKind.ENUM_CONSTANT
import jdkx.lang.model.element.ElementKind.FIELD
import jdkx.lang.model.element.ElementKind.INSTANCE_INIT
import jdkx.lang.model.element.ElementKind.INTERFACE
import jdkx.lang.model.element.ElementKind.METHOD
import jdkx.lang.model.element.ElementKind.STATIC_INIT
import jdkx.lang.model.element.Modifier.STATIC
import jdkx.lang.model.element.TypeElement
import openjdk.source.util.TreePath
import openjdk.tools.javac.api.JavacTrees
import openjdk.tools.javac.code.Symbol.MethodSymbol
import openjdk.tools.javac.model.JavacTypes
import openjdk.tools.javac.tree.JCTree.JCImport
import java.nio.file.Path

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
) : IJavaCompletionProvider(cursor, completingFile, compiler, settings) {

  lateinit var importPath: String

  // TODO add tests for this
  override fun doComplete(
    task: CompileTask,
    path: TreePath,
    partial: String,
    endsWithParen: Boolean,
  ): CompletionResult {

    val importTree = path.leaf
    if (importTree !is JCImport) {
      return CompletionResult.EMPTY
    }

    log.info("...complete import for path: {}", importPath)

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
      incomplete = pkgName.substringAfterLast(delimiter = '.')
      pkgName = pkgName.substringBeforeLast(delimiter = '.')
    }

    abortIfCancelled()
    abortCompletionIfCancelled()
    run {
      val match = matchLevel("static", incomplete)
      if (match != NO_MATCH && !importTree.isStatic && pkgName.isEmpty()) {
        list.add(keyword("static", incomplete, match))
      }
    }

    abortIfCancelled()
    abortCompletionIfCancelled()
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
      val packages = collectPackageNodes(module, pkgName)
      abortIfCancelled()
      abortCompletionIfCancelled()
      if (packages.isNotEmpty()) {
        for (node in packages) {
          addDirectChildNodes(node, incomplete, list, names, false)
        }
      }
    } catch (err: RequireMemberCompletionException) {
      // If pkgName is not an existing package name, check if it is a qualified classname
      // A user might be trying to acess members of a member class. So, we keep replacing last '.'
      // until we find a valid qualified name of a class
      if (completeTypeMembers(task, path, pkgName, incomplete, list)) {
        return CompletionResult(list)
      }
    }

    try {
      // This maybe reached only in some rare cases
      tryCompleteImport(pkgName, incomplete, list, names, module)
    } catch (e: RequireMemberCompletionException) {
      // User is trying to access members of a class
      if (completeTypeMembers(task, path, pkgName, incomplete, list)) {
        return CompletionResult(list)
      }
    }

    return CompletionResult(list)
  }

  private fun completeTypeMembers(
    task: CompileTask,
    path: TreePath,
    pkgName: String,
    incomplete: String,
    list: MutableList<CompletionItem>
  ): Boolean {
    abortIfCancelled()
    abortCompletionIfCancelled()
    val elements = task.task.elements
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

    abortIfCancelled()
    abortCompletionIfCancelled()
    if (typesForPkg.isNotEmpty()) {
      // We found a valid class name
      // Add the accessible class items
      for (type in typesForPkg) {
        val result = completeTypeMembers(task, type, path, incomplete)
        if (result.isNotEmpty()) {
          list.addAll(result)
        }
      }
      return true
    }
    return false
  }

  /**
   * Collects package nodes for [pkgName] in source paths, classpaths and bootclasspaths. If any
   * segment of [pkgName] is a class, [RequireMemberCompletionException] is thrown to indicate that
   * class members must be completed.
   *
   * @param module The project module
   * @param pkgName The package name to collect nodes for.
   */
  private fun collectPackageNodes(module: ModuleProject, pkgName: String): List<Node> {
    abortIfCancelled()
    abortCompletionIfCancelled()
    val result = mutableListOf<Node>()
    val fromSource = collectPackageNode(module.compileJavaSourceClasses, pkgName)
    if (fromSource != null) {
      result.add(fromSource)
    }

    abortIfCancelled()
    abortCompletionIfCancelled()
    val fromClasspath = collectPackageNode(module.compileClasspathClasses, pkgName)
    if (fromClasspath != null) {
      result.add(fromClasspath)
    }

    BootClasspathProvider.getAllEntries().forEach {
      abortIfCancelled()
      abortCompletionIfCancelled()
      val fromBootclasspath = collectPackageNode(it, pkgName)
      if (fromBootclasspath != null) {
        result.add(fromBootclasspath)
      }
    }
    return result
  }

  /**
   * Collect package nodes from the [trie] for the given [pkgName]. If any segment of [pkgName] is a
   * class, [RequireMemberCompletionException] is thrown to indicate that class members must be
   * completed.
   *
   * @param trie The [ClassTrie] to find package names from.
   * @param pkgName The package name of the package to find node for.
   * @return The found package name. Or `null` if no package can be found.
   */
  private fun collectPackageNode(trie: com.itsaky.androidide.utils.ClassTrie, pkgName: String): Node? {
    val segments = trie.segments(pkgName)
    var node: Node? = trie.root
    for (segment in segments) {
      abortIfCancelled()
      abortCompletionIfCancelled()
      if (node == null) {
        break
      }

      if (node.isClass) {
        // If any of the segment in pkgName is a class
        // We need to complete memebers of a class
        throw RequireMemberCompletionException()
      }

      node = node.children[segment]
    }

    return node
  }

  private fun completeTypeMembers(
    task: CompileTask,
    type: TypeElement,
    path: TreePath,
    partial: String
  ): MutableList<CompletionItem> {

    abortIfCancelled()
    abortCompletionIfCancelled()

    val list = mutableListOf<CompletionItem>()
    val elements = task.task.elements
    val trees = JavacTrees.instance(task.task.context)
    val jcTypes = JavacTypes.instance(task.task.context)
    val scope = trees.getScope(path)
    val isStatic = (path.leaf as JCImport).isStatic
    if (!trees.isAccessible(scope, type)) {
      // Type not accessible
      return list
    }

    val members = elements.getAllMembers(type)
    for (member in members) {
      abortIfCancelled()
      abortCompletionIfCancelled()
      if (
        member.kind == CONSTRUCTOR || member.kind == STATIC_INIT || member.kind == INSTANCE_INIT
      ) {
        continue
      }

      val match = matchLevel(member.simpleName, partial)
      if (match == NO_MATCH) {
        continue
      }

      if (isType(member)) {
        list.add(classItem(member.simpleName.toString(), match))
        continue
      }

      if (!isStatic) {
        continue
      }

      val mods = member.modifiers
      if (!mods.contains(STATIC)) {
        continue
      }

      if (!trees.isAccessible(scope, member, jcTypes.getDeclaredType(type))) {
        continue
      }

      if (member.kind == METHOD) {
        list.add(method(task, listOf(member as MethodSymbol), false, match, partial))
        continue
      }

      if (member.kind == FIELD || member.kind == ENUM_CONSTANT) {
        list.add(item(task, member, match))
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
    abortIfCancelled()
    abortCompletionIfCancelled()
    val sourceNode =
      if (pkgName.isEmpty()) module.compileJavaSourceClasses.root
      else module.compileJavaSourceClasses.findNode(pkgName)
    if (sourceNode != null) {
      if (sourceNode.isClass) {
        throw RequireMemberCompletionException()
      }

      addDirectChildNodes(sourceNode, incomplete, list, names, packageOnly)
    }

    abortIfCancelled()
    abortCompletionIfCancelled()
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
      abortIfCancelled()
      abortCompletionIfCancelled()
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
      abortIfCancelled()
      abortCompletionIfCancelled()
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
        list.add(packageItem(child.qualifiedName, match))
      }

      names.add(child.name)
    }
  }

  private fun legacyImportPathCompletion(
    partial: String,
    names: MutableSet<String>,
    list: MutableList<CompletionItem>
  ) {
    abortIfCancelled()
    abortCompletionIfCancelled()
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

  /**
   * Internal exception to indicate that members of a class must be completed. This is thrown and
   * caught internally when completing imports.
   */
  internal class RequireMemberCompletionException : IllegalStateException()
}
