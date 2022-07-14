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
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.lsp.java.JavaCompilerProvider
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.lsp.java.R
import com.itsaky.androidide.lsp.java.R.string
import com.itsaky.androidide.lsp.java.actions.FieldBasedAction
import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.utils.EditHelper
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.toaster.Toaster
import com.itsaky.toaster.Toaster.Type.ERROR
import com.sun.source.tree.ClassTree
import com.sun.source.tree.VariableTree
import com.sun.source.util.TreePath
import com.sun.tools.javac.api.JavacTrees
import com.sun.tools.javac.code.Symbol.ClassSymbol
import com.sun.tools.javac.code.Symbol.MethodSymbol
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.TreeInfo
import com.sun.tools.javac.util.Names
import io.github.rosemoe.sora.widget.CodeEditor
import java.util.concurrent.*
import javax.lang.model.element.VariableElement

/**
 * Generates the `toString()` method for the current class.
 *
 * @author Akash Yadav
 */
class GenerateToStringMethodAction : FieldBasedAction() {
  override val titleTextRes: Int = R.string.action_generate_toString
  override val id: String = "lsp_java_generateToString"
  override var label: String = ""

  private val log = ILogger.newInstance(javaClass.simpleName)

  override fun onGetFields(fields: List<String>, data: ActionData) {
    showFieldSelector(fields, data) { selected ->
      CompletableFuture.runAsync { generateToString(data, selected) }
        .whenComplete { _, error ->
          if (error != null) {
            log.error("Unable to generate toString() implementation", error)
            ThreadUtils.runOnUiThread {
              BaseApplication.getBaseInstance()
                .toast(
                  data[Context::class.java]!!.getString(R.string.msg_cannot_generate_toString),
                  Toaster.Type.ERROR
                )
            }
            return@whenComplete
          }
        }
    }
  }

  private fun generateToString(data: ActionData, selected: MutableSet<String>) {
    val compiler =
      JavaCompilerProvider.get(ProjectManager.findModuleForFile(requireFile(data)) ?: return)
    val range = data[com.itsaky.androidide.models.Range::class.java]!!
    val file = requirePath(data)

    compiler.compile(file).run { task ->
      val triple = findFields(task, file, range)
      val typeFinder = triple.first
      val type = triple.second
      val fields = triple.third

      fields.removeIf { !selected.contains("${it.name}: ${it.type}") }

      log.debug("Creating toString() method with fields: ", fields.map { it.name })

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
        BaseApplication.getBaseInstance()
          .toast(data[Context::class.java]!!.getString(string.msg_toString_overridden), ERROR)
      }
      log.warn("toString() method has already been overridden in class ${type.simpleName}")
      return
    }

    val file = requirePath(data)
    val editor = data[CodeEditor::class.java]!!
    val trees = JavacTrees.instance(task.task)
    val indent = EditHelper.indent(task.task, task.root(), type) + 4
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
    text = text.replace("\n", "\n${EditHelper.repeatSpaces(indent)}")
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
