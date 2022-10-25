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

package com.itsaky.androidide.lsp.java.actions

import android.content.Context
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.hasRequiredData
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.actions.newDialogBuilder
import com.itsaky.androidide.actions.requirePath
import com.itsaky.androidide.lsp.java.JavaCompilerProvider
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.visitors.FindTypeDeclarationAt
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.utils.ILogger
import com.itsaky.toaster.Toaster
import com.itsaky.toaster.toast
import com.sun.source.tree.ClassTree
import com.sun.source.tree.Tree.Kind.VARIABLE
import com.sun.source.tree.VariableTree
import io.github.rosemoe.sora.widget.CodeEditor
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException
import javax.lang.model.element.Modifier.STATIC

/**
 * Any action that has to work with fields in the current class can inherit this action.
 *
 * @author Akash Yadav
 */
abstract class FieldBasedAction : BaseJavaCodeAction() {

  private val log = ILogger.newInstance(javaClass.simpleName)

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (
      !visible ||
        !hasRequiredData(
          data,
          com.itsaky.androidide.models.Range::class.java,
          CodeEditor::class.java
        ) ||
        ProjectManager.rootProject == null
    ) {
      markInvisible()
      return
    }

    visible = true
    enabled = true
  }

  override fun execAction(data: ActionData): Any {
    val range = data[com.itsaky.androidide.models.Range::class.java]!!
    val file = requirePath(data)
    val module = ProjectManager.findModuleForFile(file) ?: return Any()

    return JavaCompilerProvider.get(module).compile(file).get { task ->
      val triple = findFields(task, file, range)
      val type = triple.second
      val fields = triple.third
      val fieldNames = fields.map { "${it.name}: ${it.type}" } // Get the names

      log.debug("Found ${fieldNames.size} fields in class ${type.simpleName}")

      return@get fieldNames
    }
  }

  protected fun findFields(
    task: CompileTask,
    file: Path,
    range: com.itsaky.androidide.models.Range
  ): Triple<FindTypeDeclarationAt, ClassTree, MutableList<VariableTree>> {
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
        RuntimeException("Unable to find position for the given selection range")
      )
    }

    val typeFinder = FindTypeDeclarationAt(task.task)
    var type = typeFinder.scan(task.root(file), start)
    if (type == null) {
      type = typeFinder.scan(task.root(file), end)
    }

    if (type == null) {
      throw CompletionException(
        RuntimeException("Unable to find class declaration within cursor range")
      )
    }

    val fields =
      type.members
        .filter { it.kind == VARIABLE }
        .map { it as VariableTree }
        .filter { !it.modifiers.flags.contains(STATIC) }
        .toMutableList()
    return Triple(typeFinder, type, fields)
  }

  @Suppress("UNCHECKED_CAST")
  override fun postExec(data: ActionData, result: Any) {
    if (result !is List<*>) {
      log.error("Unable to find fields in the current class")
      return
    }

    if (result.isEmpty()) {
      toast(data[Context::class.java]!!.getString(R.string.msg_no_fields_found), Toaster.Type.INFO)
      return
    }

    onGetFields(result as List<String>, data)
  }

  /**
   * Called when the fields of the current class are found. As this method is called inside
   * [postExec], the current thread is the UI thread.
   */
  abstract fun onGetFields(fields: List<String>, data: ActionData)

  /**
   * Shows the field selector dialog. Returns a [CompletableFuture] which is completed when the user
   * confirms the selected fields.
   */
  protected fun showFieldSelector(
    fields: List<String>,
    data: ActionData,
    listener: OnFieldsSelectedListener?
  ) {
    val names = fields.toTypedArray()
    val checkedNames = mutableSetOf<String>()
    val builder = newDialogBuilder(data)
    builder.setTitle(data[Context::class.java]!!.getString(R.string.msg_select_fields))
    builder.setMultiChoiceItems(names, BooleanArray(fields.size)) { _, which, checked ->
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
        toast(
          data[Context::class.java]!!.getString(R.string.msg_no_fields_selected),
          Toaster.Type.ERROR
        )
        return@setPositiveButton
      }

      listener?.onFieldsSelected(checkedNames)
    }
    builder.setNegativeButton(android.R.string.cancel, null)
    builder.show()
  }

  /** Listener to get callback when fields are selected by the user. */
  fun interface OnFieldsSelectedListener {

    /**
     * Called when the user is done selecting fields.
     *
     * @param fields The selected field names.
     */
    fun onFieldsSelected(fields: MutableSet<String>)
  }
}
