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

package com.itsaky.lsp.java.actions.generators

import android.content.Context
import com.blankj.utilcode.util.ThreadUtils
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.StopWatch
import com.itsaky.lsp.java.JavaLanguageServer
import com.itsaky.lsp.java.R.string
import com.itsaky.lsp.java.actions.FieldBasedAction
import com.itsaky.lsp.java.compiler.CompileTask
import com.itsaky.lsp.java.utils.EditHelper
import com.itsaky.lsp.models.Range
import com.itsaky.toaster.Toaster.Type.ERROR
import com.sun.source.tree.ClassTree
import com.sun.source.tree.VariableTree
import com.sun.source.util.TreePath
import com.sun.tools.javac.api.JavacTrees
import com.sun.tools.javac.code.Symbol.ClassSymbol
import com.sun.tools.javac.code.Symbol.VarSymbol
import com.sun.tools.javac.code.Type
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.TreeInfo
import com.sun.tools.javac.util.ListBuffer
import io.github.rosemoe.sora.widget.CodeEditor
import java.util.concurrent.*

/**
 * Allows the user to select fields and generate a constructor which has parameters same as the
 * selected fields.
 *
 * The generated constructor has statements which assigns the fields to the parameter types.
 *
 * @author Akash Yadav
 */
class GenerateConstructorAction : FieldBasedAction() {

    override fun onGetFields(fields: List<String>, data: ActionData) {
        showFieldSelector(fields, data) { selected ->
            CompletableFuture.runAsync { generateConstructor(data, selected) }.whenComplete {
                _,
                error ->
                if (error != null) {
                    log.error("Unable to generate constructor for the selected fields", error)
                    BaseApplication.getBaseInstance()
                        .toast(string.msg_cannot_generate_constructor, ERROR)
                }
            }
        }
    }

    private fun generateConstructor(data: ActionData, selected: MutableSet<String>) {
        val server = data[JavaLanguageServer::class.java]!!
        val range = data[Range::class.java]!!
        val file = requirePath(data)

        server.compiler.compile(file).run { task ->
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
        val editor = data[CodeEditor::class.java]!!
        val trees = JavacTrees.instance(task.task)
        val sym = TreeInfo.symbolFor(type as JCTree) as ClassSymbol
        val varTypes = mapTypes(paths)
        val varNames = paths.map { it.leaf as VariableTree }.map { it.name.toString() }

        if (paths.isEmpty() || trees.findConstructor(sym, varTypes) != null) {
            log.warn(
                "A constructor with same parameter types is already available in class ${type.simpleName}")
            BaseApplication.getBaseInstance()
                .toastLong(
                    data[Context::class.java]!!.getString(string.msg_constructor_available), ERROR)
            return
        }

        val stopWatch = StopWatch("generateConstructorForFields()")
        val constructor =
            newConstructor(
                type.simpleName.toString(), varTypes.toTypedArray(), varNames.toTypedArray())
        val body = constructor.createBody()
        for (varName in varNames) {
            body.addStatement(StaticJavaParser.parseStatement("this.$varName = $varName;"))
        }

        stopWatch.lap("Constructor generated")
        log.info("Inserting constructor into editor...")

        val insertAt = EditHelper.insertAfter(task.task, task.root(), paths.last().leaf)
        val indent = EditHelper.indent(task.task, task.root(), paths.last().leaf)
        var text = constructor.toString()
        text = text.replace("\n", "\n${EditHelper.repeatSpaces(indent)}")
        text += "\n"

        ThreadUtils.runOnUiThread {
            editor.text.insert(insertAt.line, insertAt.column, text)
            editor.formatCodeAsync()
            stopWatch.log()
        }
    }

    private fun newConstructor(
        name: String,
        paramTypes: Array<Type>,
        paramNames: Array<String>
    ): ConstructorDeclaration {
        val constructor = ConstructorDeclaration()
        constructor.setName(name)
        constructor.addModifier(Modifier.Keyword.PUBLIC)

        for (i in paramTypes.indices) {
            val paramType = paramTypes[i]
            val paramName = paramNames[i]

            constructor.addParameter(paramType.tsym.simpleName.toString(), paramName)
        }

        return constructor
    }

    private fun mapTypes(paths: List<TreePath>): com.sun.tools.javac.util.List<Type> {
        val buffer = ListBuffer<Type>()
        for (path in paths) {
            val leaf = path.leaf
            val sym = TreeInfo.symbolFor(leaf as JCTree) as VarSymbol
            buffer.add(sym.type)
        }

        return buffer.toList()
    }

    override val titleTextRes: Int = string.action_generate_constructor
    override val id: String = "lsp_java_generateConstructor"
    override var label: String = ""
    private val log = ILogger.newInstance(javaClass.simpleName)
}
