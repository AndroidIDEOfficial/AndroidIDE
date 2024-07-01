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
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.SimpleName
import com.github.javaparser.ast.stmt.BlockStmt
import com.github.javaparser.ast.type.Type
import com.github.javaparser.ast.type.VoidType
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.requireFile
import com.itsaky.androidide.actions.requirePath
import com.itsaky.androidide.lsp.java.JavaCompilerProvider
import com.itsaky.androidide.lsp.java.actions.FieldBasedAction
import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.utils.EditHelper
import com.itsaky.androidide.lsp.java.utils.JavaParserUtils
import com.itsaky.androidide.lsp.java.utils.TypeUtils.toType
import com.itsaky.androidide.preferences.internal.EditorPreferences
import com.itsaky.androidide.preferences.utils.indentationString
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.flashError
import io.github.rosemoe.sora.widget.CodeEditor
import jdkx.lang.model.element.Modifier.FINAL
import jdkx.lang.model.element.VariableElement
import openjdk.source.tree.ClassTree
import openjdk.source.util.TreePath
import openjdk.source.util.Trees
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture

/**
 * Allows the user to select fields from the current class, then generates setters and getters for
 * the selected fields.
 *
 * @author Akash Yadav
 */
class GenerateSettersAndGettersAction : FieldBasedAction() {

  override val id: String = "ide.editor.lsp.java.generator.settersAndGetters"
  override var label: String = ""

  override val titleTextRes: Int = R.string.action_generate_setters_getters

  companion object {

    private val log = LoggerFactory.getLogger(GenerateSettersAndGettersAction::class.java)
  }

  override fun onGetFields(fields: List<String>, data: ActionData) {

    showFieldSelector(fields, data) { checkedNames ->
      CompletableFuture.runAsync { generateForFields(data, checkedNames) }
        .whenComplete {
            _, error,
          ->
          if (error != null) {
            log.error("Unable to generate setters and getters", error)
            ThreadUtils.runOnUiThread {
              flashError(
                data[Context::class.java]!!.getString(R.string.msg_cannot_generate_setters_getters)
              )
            }
            return@whenComplete
          }
        }
    }
  }

  private fun generateForFields(data: ActionData, names: MutableSet<String>) {
    val compiler =
      JavaCompilerProvider.get(
        IProjectManager.getInstance().getWorkspace()?.findModuleForFile(data.requireFile(), false)
          ?: return
      )
    val range = data[com.itsaky.androidide.models.Range::class.java]!!
    val file = data.requirePath()

    compiler.compile(file).run { task ->
      val triple = findFields(task, file, range)
      val typeFinder = triple.first
      val type = triple.second
      val fields = triple.third

      fields.removeIf { !names.contains("${it.name}: ${it.type}") }

      log.debug("Creating setters/getters for fields: {}", fields.map { it.name })

      generateForFields(data, task, type, fields.map { TreePath(typeFinder.path, it) })
    }
  }

  private fun generateForFields(
    data: ActionData,
    task: CompileTask,
    type: ClassTree,
    paths: List<TreePath>,
  ) {
    val file = data.requirePath()
    val editor = data[CodeEditor::class.java]!!
    val trees = Trees.instance(task.task)
    val insert = EditHelper.insertAtEndOfClass(task.task, task.root(file), type)
    val sb = StringBuilder()

    for (path in paths) {
      val element = trees.getElement(path) ?: continue
      if (element !is VariableElement) {
        continue
      }

      val leaf = path.leaf
      val indent = EditHelper.indent(task.task, task.root(file), leaf) + EditorPreferences.tabSize
      sb.append(createGetter(element, indent))
      if (!element.modifiers.contains(FINAL)) {
        sb.append(createSetter(element, indent))
      }
    }

    ThreadUtils.runOnUiThread {
      editor.text.insert(insert.line, insert.column, sb)
      editor.formatCodeAsync()
    }
  }

  private fun createGetter(variable: VariableElement, indent: Int): String {
    val name = variable.simpleName.toString()
    val method =
      createMethod(variable, "get", toType(variable.asType())) { _, body ->
        body.addStatement(createReturnStmt(name))
      }
    var text = "\n" + JavaParserUtils.prettyPrint(method) { false }
    text = text.replace("\n", "\n${indentationString(indent)}")

    return text
  }

  private fun createReturnStmt(name: String) = StaticJavaParser.parseStatement("return this.$name;")

  private fun createSetter(variable: VariableElement, indent: Int): String {
    val name: String = variable.simpleName.toString()
    val method =
      createMethod(variable, "set", VoidType()) { method, body ->
        method.addParameter(toType(variable.asType()), name)
        body.addStatement(createAssignmentStmt(name))
      }

    var text = "\n" + JavaParserUtils.prettyPrint(method) { false }
    text = text.replace("\n", "\n${indentationString(indent)}")

    return text
  }

  private fun createMethod(
    variable: VariableElement,
    prefix: String,
    returnType: Type,
    vararg modifiers: Modifier.Keyword = arrayOf(Modifier.Keyword.PUBLIC),
    block: (MethodDeclaration, BlockStmt) -> Unit
  ): MethodDeclaration {
    val name = variable.simpleName.toString()
    val method = MethodDeclaration()
    val body = method.createBody()
    method.name = SimpleName(createName(name, prefix))
    method.type = returnType
    method.addModifier(*modifiers)
    block(method, body)
    return method
  }

  private fun createAssignmentStmt(name: String) =
    StaticJavaParser.parseStatement("this.$name = $name;")

  private fun createName(name: String, prefix: String): String {
    val sb = StringBuilder(name)
    sb.setCharAt(0, Character.toUpperCase(sb[0]))
    sb.insert(0, prefix)
    return sb.toString()
  }
}
