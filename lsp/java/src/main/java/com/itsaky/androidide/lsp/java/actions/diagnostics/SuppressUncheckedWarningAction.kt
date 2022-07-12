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
package com.itsaky.androidide.lsp.java.actions.diagnostics

import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.lsp.java.JavaCompilerProvider
import com.itsaky.androidide.lsp.java.R
import com.itsaky.androidide.lsp.java.actions.BaseCodeAction
import com.itsaky.androidide.lsp.java.models.DiagnosticCode
import com.itsaky.androidide.lsp.java.rewrite.AddSuppressWarningAnnotation
import com.itsaky.androidide.lsp.java.utils.CodeActionUtils
import com.itsaky.androidide.lsp.models.DiagnosticItem
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.utils.ILogger

/** @author Akash Yadav */
class SuppressUncheckedWarningAction : BaseCodeAction() {
  override val id = "lsp_java_suppressUncheckedWarning"
  override var label: String = ""
  private val diagnosticCode = DiagnosticCode.UNCHECKED.id
  private val log = ILogger.newInstance(javaClass.simpleName)

  override val titleTextRes: Int = R.string.action_suppress_unchecked_warning

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible || !hasRequiredData(data, com.itsaky.androidide.lsp.models.DiagnosticItem::class.java)) {
      markInvisible()
      return
    }

    val diagnostic = data[com.itsaky.androidide.lsp.models.DiagnosticItem::class.java]!!
    if (diagnosticCode != diagnostic.code) {
      markInvisible()
      return
    }
  }

  override fun execAction(data: ActionData): Any {
    val diagnostic = data[com.itsaky.androidide.lsp.models.DiagnosticItem::class.java]!!
    val compiler =
      JavaCompilerProvider.get(ProjectManager.findModuleForFile(requireFile(data)) ?: return Any())
    val file = requirePath(data)
    return compiler.compile(file).get { task ->
      val warnedMethod = CodeActionUtils.findMethod(task, diagnostic.range)
      return@get AddSuppressWarningAnnotation(
        warnedMethod.className,
        warnedMethod.methodName,
        warnedMethod.erasedParameterTypes
      )
    }
  }

  override fun postExec(data: ActionData, result: Any) {
    if (result !is AddSuppressWarningAnnotation) {
      log.warn("Unable to suppress 'unchecked' warning")
      return
    }

    performCodeAction(data, result)
  }
}
