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
package com.itsaky.androidide.lsp.java.actions.generators

import android.content.Context
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.hasRequiredData
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.actions.newDialogBuilder
import com.itsaky.androidide.actions.requireFile
import com.itsaky.androidide.actions.requirePath
import com.itsaky.androidide.lsp.java.JavaCompilerProvider
import com.itsaky.androidide.lsp.java.actions.BaseJavaCodeAction
import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.compiler.CompilerProvider
import com.itsaky.androidide.lsp.java.parser.ParseTask
import com.itsaky.androidide.lsp.java.rewrite.AddImport
import com.itsaky.androidide.lsp.java.utils.EditHelper
import com.itsaky.androidide.lsp.java.utils.FindHelper
import com.itsaky.androidide.lsp.java.utils.JavaParserUtils
import com.itsaky.androidide.lsp.java.utils.MethodPtr
import com.itsaky.androidide.lsp.java.visitors.FindTypeDeclarationAt
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.preferences.internal.EditorPreferences
import com.itsaky.androidide.preferences.utils.indentationString
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.flashError
import io.github.rosemoe.sora.widget.CodeEditor
import jdkx.lang.model.element.ElementKind
import jdkx.lang.model.element.ExecutableElement
import jdkx.lang.model.element.Modifier
import jdkx.lang.model.element.TypeElement
import jdkx.lang.model.type.DeclaredType
import jdkx.lang.model.type.ExecutableType
import jdkx.tools.JavaFileObject
import openjdk.source.tree.MethodTree
import openjdk.source.util.Trees
import org.slf4j.LoggerFactory
import java.util.Arrays
import java.util.Optional
import java.util.concurrent.CompletableFuture

/**
 * Allows the user to override multiple methods from superclass at once.
 *
 * @author Akash Yadav
 */
class OverrideSuperclassMethodsAction : BaseJavaCodeAction() {

  override val titleTextRes: Int = R.string.action_override_superclass_methods
  override val id: String = "ide.editor.lsp.java.generator.overrideSuperclassMethods"
  override var label: String = ""
  private var position: Long = -1

  companion object {

    private val log = LoggerFactory.getLogger(OverrideSuperclassMethodsAction::class.java)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (
      !visible ||
      !data.hasRequiredData(
        com.itsaky.androidide.models.Range::class.java,
        CodeEditor::class.java
      )
    ) {
      markInvisible()
      return
    }

    visible = true
    enabled = true
  }

  override suspend fun execAction(data: ActionData): Any {
    val range = data[com.itsaky.androidide.models.Range::class.java]!!
    val compiler =
      JavaCompilerProvider.get(
        IProjectManager.getInstance().getWorkspace()?.findModuleForFile(data.requireFile(), false)
          ?: return Any()
      )
    val file = data.requirePath()

    return compiler.compile(file).get { task ->
      // 1-based line and column index
      val startLine = range.start.line + 1
      val startColumn = range.start.column + 1
      val endLine = range.end.line + 1
      val endColumn = range.end.column + 1
      val lines = task.root().lineMap
      val start = lines.getPosition(startLine.toLong(), startColumn.toLong())
      val end = lines.getPosition(endLine.toLong(), endColumn.toLong())

      if (start == (-1).toLong() || end == (-1).toLong()) {
        return@get false
      }

      this.position = start
      val typeFinder = FindTypeDeclarationAt(task.task)
      var type = typeFinder.scan(task.root(file), start)
      if (type == null) {
        type = typeFinder.scan(task.root(file), end)
        position = end
      }

      if (type == null) {
        return@get false
      }

      val overridable = mutableListOf<MethodPtr>()
      val trees = Trees.instance(task.task)
      val elements = task.task.elements
      val classPath = typeFinder.path
      val element = trees.getElement(classPath) as TypeElement

      for (member in elements.getAllMembers(element)) {
        if (member.modifiers.contains(Modifier.FINAL) || member.kind != ElementKind.METHOD) {
          continue
        }

        val method = member as ExecutableElement
        val methodSource = member.getEnclosingElement() as TypeElement
        if (
          methodSource.qualifiedName.contentEquals("java.lang.Object") || methodSource == element
        ) {
          continue
        }

        val pointer = MethodPtr(task.task, method)
        overridable.add(pointer)
      }

      return@get overridable
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun postExec(data: ActionData, result: Any) {
    if (result !is List<*> || result.isEmpty() || position < 0) {
      log.warn("Unable to find any overridable method")
      flashError(data[Context::class.java]!!.getString(R.string.msg_no_overridable_methods))
      return
    }

    val methods = (result as List<MethodPtr>).sortedBy { it.methodName }
    val checkedMethods = mutableListOf<MethodPtr>()
    val names = mapMethodPointers(methods)
    val builder = newDialogBuilder(data)
    builder.setTitle(data[Context::class.java]!!.getString(R.string.msg_select_methods))
    builder.setMultiChoiceItems(names, BooleanArray(names.size)) { _, which, isChecked ->
      checkedMethods.apply {
        if (isChecked) {
          add(methods[which])
        } else {
          remove(methods[which])
        }
      }
    }
    builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
      dialog.dismiss()

      if (checkedMethods.isEmpty()) {
        flashError(data[Context::class.java]!!.getString(R.string.msg_no_methods_selected))
        return@setPositiveButton
      }

      CompletableFuture.runAsync { overrideMethods(data, checkedMethods) }
        .whenComplete {
            _, error,
          ->
          if (error != null) {
            log.error("An error occurred overriding methods")

            ThreadUtils.runOnUiThread {
              flashError(
                data[Context::class.java]!!.getString(R.string.msg_cannot_override_methods)
              )
            }
          }
        }
    }
    builder.setNegativeButton(android.R.string.cancel, null)
    builder.show()
  }

  private fun overrideMethods(data: ActionData, checkedMethods: MutableList<MethodPtr>) {
    val compiler =
      JavaCompilerProvider.get(
        IProjectManager.getInstance().getWorkspace()?.findModuleForFile(data.requireFile(), false)
          ?: return
      )
    val file = data.requirePath()

    compiler.compile(file).run { task ->
      val types = task.task.types
      val trees = Trees.instance(task.task)
      val sb = StringBuilder()
      val imports = mutableSetOf<String>()

      val typeFinder = FindTypeDeclarationAt(task.task)
      val classTree = typeFinder.scan(task.root(), position)
      val thisClass = trees.getElement(typeFinder.path) as TypeElement
      val indent = EditHelper.indent(task.task, task.root(), classTree) + EditorPreferences.tabSize
      val fileImports = task.root(file).imports.map { it.qualifiedIdentifier.toString() }.toSet()
      val filePackage = task.root(file).`package`.packageName.toString()

      for (pointer in checkedMethods) {
        val superMethod =
          FindHelper.findMethod(
            task,
            pointer.className,
            pointer.methodName,
            pointer.erasedParameterTypes
          ) ?: continue

        val thisDeclaredType = thisClass.asType() as DeclaredType
        val executableType = types.asMemberOf(thisDeclaredType, superMethod) as ExecutableType
        val source = findSource(compiler, task, superMethod)
        val method =
          if (source != null) {
            JavaParserUtils.printMethod(superMethod, executableType, source)
          } else {
            JavaParserUtils.printMethod(superMethod, executableType, superMethod)
          }

        val newImports = JavaParserUtils.collectImports(executableType)
        sb.append("\n")
        sb.append(method.toString())
        sb.replace(Regex(Regex.escape("\n")), "\n${indentationString(indent)}")
        sb.append("\n")

        newImports.removeIf {
          it.startsWith("java.lang.") || it.startsWith(filePackage) || fileImports.contains(it)
        }

        imports.addAll(newImports)
      }

      ThreadUtils.runOnUiThread {
        performEdits(
          data,
          sb,
          imports,
          EditHelper.insertAtEndOfClass(task.task, task.root(file), classTree)
        )
      }
    }
  }

  private fun performEdits(
    data: ActionData,
    sb: StringBuilder,
    imports: MutableSet<String>,
    position: Position,
  ) {
    val compiler =
      JavaCompilerProvider.get(
        IProjectManager.getInstance().getWorkspace()?.findModuleForFile(data.requireFile(), false)
          ?: return
      )
    val editor = data[CodeEditor::class.java]!!
    val file = data.requirePath()
    val text = editor.text

    text.beginBatchEdit()

    text.insert(position.line, position.column, sb)

    for (name in imports) {
      val rewrite = AddImport(file, name)
      val edits = rewrite.rewrite(compiler)[file]
      if (edits.isNullOrEmpty()) {
        continue
      }

      for (edit in edits) {
        if (edit.range.start == edit.range.end) {
          text.insert(edit.range.start.line, edit.range.start.column, edit.newText)
        } else {
          text.replace(
            edit.range.start.line,
            edit.range.start.column,
            edit.range.end.line,
            edit.range.end.column,
            edit.newText
          )
        }
      }
    }

    text.endBatchEdit()
    editor.formatCodeAsync()
  }

  private fun mapMethodPointers(methods: List<MethodPtr>): Array<CharSequence> {
    return methods
      .map { Arrays.toString(it.simplifiedErasedParameterTypes) }
      .map {
        val arr = it.toCharArray()
        arr[0] = '('
        arr[arr.size - 1] = ')'

        String(arr)
      }
      .mapIndexed { index, params -> "${methods[index].methodName}$params" }
      .toTypedArray()
  }

  private fun findSource(
    compiler: CompilerProvider,
    task: CompileTask,
    method: ExecutableElement,
  ): MethodTree? {
    val superClass = method.enclosingElement as TypeElement
    val superClassName = superClass.qualifiedName.toString()
    val methodName = method.simpleName.toString()
    val erasedParameterTypes = FindHelper.erasedParameterTypes(task, method)
    val sourceFile: Optional<JavaFileObject> = compiler.findAnywhere(superClassName)
    if (!sourceFile.isPresent) return null
    val parse: ParseTask = compiler.parse(sourceFile.get())
    return FindHelper.findMethod(parse, superClassName, methodName, erasedParameterTypes)
  }
}
