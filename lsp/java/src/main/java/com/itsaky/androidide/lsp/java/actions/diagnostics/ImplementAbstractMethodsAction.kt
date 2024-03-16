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
import com.itsaky.androidide.actions.hasRequiredData
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.javac.services.util.JavaDiagnosticUtils
import com.itsaky.androidide.lsp.java.actions.BaseJavaCodeAction
import com.itsaky.androidide.lsp.java.models.DiagnosticCode
import com.itsaky.androidide.lsp.java.rewrite.ImplementAbstractMethods
import com.itsaky.androidide.resources.R
import jdkx.tools.Diagnostic
import jdkx.tools.JavaFileObject
import org.slf4j.LoggerFactory

/** @author Akash Yadav */
class ImplementAbstractMethodsAction : BaseJavaCodeAction() {

  override val id: String = "ide.editor.lsp.java.diagnostics.implementAbstractMethods"
  override var label: String = ""
  private var diagnosticCode = DiagnosticCode.DOES_NOT_OVERRIDE_ABSTRACT.id

  override val titleTextRes: Int = R.string.action_implement_abstract_methods

  companion object {

    private val log = LoggerFactory.getLogger(ImplementAbstractMethodsAction::class.java)
  }

  @Suppress("UNCHECKED_CAST")
  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible) {
      return
    }

    if (!data.hasRequiredData(com.itsaky.androidide.lsp.models.DiagnosticItem::class.java)) {
      markInvisible()
      return
    }

    val diagnostic = data.get(com.itsaky.androidide.lsp.models.DiagnosticItem::class.java)!!
    if (diagnosticCode != diagnostic.code || diagnostic.extra !is Diagnostic<*>) {
      markInvisible()
      return
    }

    JavaDiagnosticUtils.asJCDiagnostic(diagnostic.extra as Diagnostic<out JavaFileObject>)
      ?: run {
        markInvisible()
        return
      }

    visible = true
    enabled = true
  }

  @Suppress("UNCHECKED_CAST")
  override suspend fun execAction(data: ActionData): Any {
    val diagnostic =
      JavaDiagnosticUtils.asJCDiagnostic(
        data.get(
          com.itsaky.androidide.lsp.models.DiagnosticItem::class.java)!!.extra as Diagnostic<out JavaFileObject>
      )
    return ImplementAbstractMethods(diagnostic!!)
  }

  override fun postExec(data: ActionData, result: Any) {
    if (result !is ImplementAbstractMethods) {
      log.warn("Unable to perform action. Invalid result from execAction(..)")
      return
    }

    performCodeAction(data, result)
  }
}
