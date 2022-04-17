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

package com.itsaky.lsp.java.actions

import android.content.Context
import com.blankj.utilcode.util.ThreadUtils
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.SimpleName
import com.github.javaparser.ast.stmt.Statement
import com.github.javaparser.ast.type.VoidType
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.utils.Logger
import com.itsaky.lsp.java.JavaLanguageServer
import com.itsaky.lsp.java.R
import com.itsaky.lsp.java.compiler.CompileTask
import com.itsaky.lsp.java.utils.EditHelper
import com.itsaky.lsp.java.utils.JavaParserUtils
import com.itsaky.lsp.java.utils.TypeUtils.toType
import com.itsaky.lsp.java.visitors.FindTypeDeclarationAt
import com.itsaky.lsp.models.Range
import com.itsaky.toaster.Toaster
import com.sun.source.tree.ClassTree
import com.sun.source.tree.Tree
import com.sun.source.tree.VariableTree
import com.sun.source.util.TreePath
import com.sun.source.util.Trees
import io.github.rosemoe.sora.widget.CodeEditor
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement

/** @author Akash Yadav */
class GenerateSettersAndGettersAction : BaseCodeAction() {
    override val id: String = "lsp_java_generateSettersAndGetters"
    override var label: String = ""
    private val log = Logger.newInstance(javaClass.simpleName)

    override val titleTextRes: Int = R.string.action_generate_setters_getters

    override fun prepare(data: ActionData) {
        super.prepare(data)

        if (!visible || !hasRequiredData(data, Range::class.java, CodeEditor::class.java)) {
            markInvisible()
            return
        }

        visible = true
        enabled = true
    }

    override fun execAction(data: ActionData): Any {
        val range = data[Range::class.java]!!
        val server = data[JavaLanguageServer::class.java]!!
        val file = requirePath(data)

        return server.compiler.compile(file).get { task ->
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

            val typeFinder = FindTypeDeclarationAt(task.task)
            var type = typeFinder.scan(task.root(file), start)
            if (type == null) {
                type = typeFinder.scan(task.root(file), end)
            }

            if (type == null) {
                return@get false
            }

            val fieldNames =
                type.members
                    .filter { it.kind == Tree.Kind.VARIABLE } // Collect fields
                    .map { it as VariableTree } // Map the fields as VariableTree
                    .filter {
                        !it.modifiers.flags.contains(Modifier.STATIC)
                    } // Remove all fields that are static
                    .map { "${it.name}: ${it.type}" } // Get the names
            log.debug("Found ${fieldNames.size} variables in class ${type.simpleName}")

            return@get fieldNames
        }
    }

    override fun postExec(data: ActionData, result: Any) {
        if (result !is List<*>) {
            log.warn("Unable to generate setters/getters")
            return
        }

        if (result.isEmpty()) {
            BaseApplication.getBaseInstance()
                .toast(
                    data[Context::class.java]!!.getString(R.string.msg_no_fields_found),
                    Toaster.Type.INFO)
            return
        }

        @Suppress("UNCHECKED_CAST") val names = (result as List<String>).toTypedArray()

        val checkedNames = mutableSetOf<String>()
        val builder = newDialogBuilder(data)
        builder.setTitle(data[Context::class.java]!!.getString(R.string.msg_select_fields))
        builder.setMultiChoiceItems(names, BooleanArray(result.size)) { _, which, checked ->
            checkedNames.apply {
                val item = names[which]
                if (checked) {
                    add(item)
                } else {
                    remove(item)
                }
            }
        }
        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()

            if (checkedNames.isEmpty()) {
                BaseApplication.getBaseInstance()
                    .toast(
                        data[Context::class.java]!!.getString(R.string.msg_no_fields_selected),
                        Toaster.Type.ERROR)
                return@setPositiveButton
            }

            CompletableFuture.runAsync { generateForFields(data, checkedNames) }.whenComplete {
                _,
                error,
                ->
                if (error != null) {
                    log.error("Unable to generate setters and getters", error)
                    ThreadUtils.runOnUiThread {
                        BaseApplication.getBaseInstance()
                            .toast(
                                data[Context::class.java]!!.getString(
                                    R.string.msg_cannot_generate_setters_getters),
                                Toaster.Type.ERROR)
                    }
                    return@whenComplete
                }
            }
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()
    }

    fun generateForFields(data: ActionData, names: MutableSet<String>) {
        val server = data[JavaLanguageServer::class.java]!!
        val range = data[Range::class.java]!!
        val file = requirePath(data)

        server.compiler.compile(file).run { task ->
            // 1-based line and column index
            val startLine = range.start.line + 1
            val startColumn = range.start.column + 1
            val endLine = range.end.line + 1
            val endColumn = range.end.column + 1
            val lines = task.root().lineMap
            val start = lines.getPosition(startLine.toLong(), startColumn.toLong())
            val end = lines.getPosition(endLine.toLong(), endColumn.toLong())

            if (start == (-1).toLong() || end == (-1).toLong()) {
                throw CompletionException(
                    RuntimeException("Unable to find position for the given selection range"))
            }

            val typeFinder = FindTypeDeclarationAt(task.task)
            var type = typeFinder.scan(task.root(file), start)
            if (type == null) {
                type = typeFinder.scan(task.root(file), end)
            }

            if (type == null) {
                throw CompletionException(
                    RuntimeException("Unable to find class declaration within cursor range"))
            }

            val fields =
                type.members
                    .filter { it.kind == Tree.Kind.VARIABLE }
                    .map { it as VariableTree }
                    .filter { !it.modifiers.flags.contains(Modifier.STATIC) }
                    .toMutableList()
            fields.removeIf { !names.contains("${it.name}: ${it.type}") }

            log.debug("Creating setters/getters for fields", fields.map { it.name })

            generateForFields(data, task, type, fields.map { TreePath(typeFinder.path, it) })
        }
    }

    private fun generateForFields(
        data: ActionData,
        task: CompileTask,
        type: ClassTree,
        paths: List<TreePath>,
    ) {
        val file = requirePath(data)
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
            val indent = EditHelper.indent(task.task, task.root(file), leaf) + 4
            sb.append(createGetter(element, indent))

            if (!element.modifiers.contains(Modifier.FINAL)) {
                sb.append(createSetter(element, indent))
            }
        }

        ThreadUtils.runOnUiThread {
            editor.text.insert(insert.line, insert.column, sb)
            editor.formatCodeAsync()
        }
    }

    private fun createGetter(variable: VariableElement, indent: Int): String {
        val name: String = variable.simpleName.toString()
        val method = MethodDeclaration()
        val body = method.createBody()
        method.name = SimpleName(createName(name, "get"))
        method.type = toType(variable.asType())
        body.addStatement(createReturnStmt(name))
        method.setBody(body)

        var text = "\n" + JavaParserUtils.prettyPrint(method) { false }
        text = text.replace("\n", "\n${EditHelper.repeatSpaces(indent)}")

        return text
    }

    private fun createReturnStmt(name: String) =
        StaticJavaParser.parseStatement("return this.$name;")

    private fun createSetter(variable: VariableElement, indent: Int): String {
        val name: String = variable.simpleName.toString()
        val method = MethodDeclaration()
        val body = method.createBody()
        method.name = SimpleName(createName(name, "set"))
        method.type = VoidType()
        method.addParameter(toType(variable.asType()), name)
        body.addStatement(createAssignmentStmt(name))

        var text = "\n" + method.toString()
        text = text.replace("\n", "\n${EditHelper.repeatSpaces(indent)}")

        return text
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