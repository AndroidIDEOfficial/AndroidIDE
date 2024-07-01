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
import com.github.javaparser.ast.stmt.ReturnStmt
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.requireFile
import com.itsaky.androidide.actions.requirePath
import com.itsaky.androidide.lsp.java.JavaCompilerProvider
import com.itsaky.androidide.lsp.java.actions.FieldBasedAction
import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.utils.EditHelper
import com.itsaky.androidide.preferences.internal.EditorPreferences
import com.itsaky.androidide.preferences.utils.indentationString
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.utils.flashError
import io.github.rosemoe.sora.widget.CodeEditor
import jdkx.lang.model.element.VariableElement
import openjdk.source.tree.ClassTree
import openjdk.source.tree.VariableTree
import openjdk.source.util.TreePath
import openjdk.tools.javac.api.JavacTrees
import openjdk.tools.javac.code.Symbol.ClassSymbol
import openjdk.tools.javac.code.Symbol.MethodSymbol
import openjdk.tools.javac.tree.JCTree
import openjdk.tools.javac.tree.TreeInfo
import openjdk.tools.javac.util.Names
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture

/**
 * Generates the `toString()` method for the current class.
 *
 * @author Akash Yadav
 */
class GenerateToStringMethodAction : FieldBasedAction() {

  override val titleTextRes: Int = R.string.action_generate_toString
  override val id: String = "ide.editor.lsp.java.generator.toString"
  override var label: String = ""

  companion object {
    private val log = LoggerFactory.getLogger(GenerateToStringMethodAction::class.java)
  }

  override fun onGetFields(fields: List<String>, data: ActionData) {
    showFieldSelector(fields, data) { selected ->
      CompletableFuture.runAsync { generateToString(data, selected) }
        .whenComplete { _, error ->
          if (error != null) {
            log.error("Unable to generate toString() implementation", error)
            ThreadUtils.runOnUiThread {
              flashError(
                data[Context::class.java]!!.getString(R.string.msg_cannot_generate_toString)
              )
            }
            return@whenComplete
          }
        }
    }
  }

  private fun generateToString(data: ActionData, selected: MutableSet<String>) {
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

      fields.removeIf { !selected.contains("${it.name}: ${it.type}") }

      log.debug("Creating toString() method with fields: {}", fields.map { it.name })

      generateForFields(data, task, type, fields.map { TreePath(typeFinder.path, it) })
    }
  }

  private fun generateForFields(
    data: ActionData,
    task: CompileTask,
    type: ClassTree,
    paths: List<TreePath>
  ) {
    if (isToStringOverridden(task, type)) {
      ThreadUtils.runOnUiThread {
        flashError(data[Context::class.java]!!.getString(string.msg_toString_overridden))
      }
      log.warn("toString() method has already been overridden in class {}", type.simpleName)
      return
    }

    val file = data.requirePath()
    val editor = data[CodeEditor::class.java]!!
    val trees = JavacTrees.instance(task.task)
    val indent = EditHelper.indent(task.task, task.root(), type) + EditorPreferences.tabSize
    val insert = EditHelper.insertAtEndOfClass(task.task, task.root(file), type)
    val string = StringBuilder()
    var isFirst = true

    string.append("\"")
    string.append(type.simpleName)
    string.append('[')
    for (path in paths) {
      val element = trees.getElement(path) ?: continue
      if (element !is VariableElement) {
        continue
      }

      val leaf = path.leaf as VariableTree
      if (!isFirst) {
        string.append(", ")
      }
      string.append(leaf.name)
      string.append("=")
      string.append("\" + ")
      string.append(leaf.name)
      string.append(" + \"")

      // "ClassName[field1=' + field1 + ', field2=' + field2 + ']"

      isFirst = false
    }
    string.append("]\"")

    val method = overrideToString()
    val body = method.createBody()
    body.addStatement(createReturnStatement(string.toString()))

    var text = "\n" + method.toString()
    text = text.replace("\n", "\n${indentationString(indent)}")
    text += "\n"

    ThreadUtils.runOnUiThread {
      editor.text.insert(insert.line, insert.column, text)
      editor.formatCodeAsync()
    }
  }

  private fun isToStringOverridden(task: CompileTask, type: ClassTree): Boolean {
    val names = Names.instance(task.task.context)
    val sym = TreeInfo.symbolFor(type as JCTree) as ClassSymbol
    val toStrings =
      sym.members().getSymbolsByName(names.toString).filterIsInstance<MethodSymbol>().filter {
        it.params.isEmpty()
      }

    return toStrings.isNotEmpty()
  }

  private fun createReturnStatement(string: String): ReturnStmt {
    return StaticJavaParser.parseStatement("return $string;") as ReturnStmt
  }

  private fun overrideToString(): MethodDeclaration {
    val method = MethodDeclaration()
    method.addMarkerAnnotation("Override")
    method.addModifier(Modifier.Keyword.PUBLIC)
    method.setType("String")
    method.setName("toString")
    return method
  }
}
