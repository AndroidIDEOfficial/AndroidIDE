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
import com.itsaky.androidide.actions.requireFile
import com.itsaky.androidide.actions.requirePath
import com.itsaky.androidide.lsp.java.JavaCompilerProvider
import com.itsaky.androidide.lsp.java.actions.BaseJavaCodeAction
import com.itsaky.androidide.lsp.java.models.DiagnosticCode
import com.itsaky.androidide.lsp.java.rewrite.RemoveMethod
import com.itsaky.androidide.lsp.java.utils.CodeActionUtils.findMethod
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.resources.R
import org.slf4j.LoggerFactory

/** @author Akash Yadav */
class RemoveMethodAction : BaseJavaCodeAction() {

  override val id: String = "ide.editor.lsp.java.diagnostics.removeMethod"
  override var label: String = ""
  private val diagnosticCode = DiagnosticCode.UNUSED_METHOD.id

  override val titleTextRes: Int = R.string.action_remove_method

  companion object {

    private val log = LoggerFactory.getLogger(RemoveMethodAction::class.java)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible || !data.hasRequiredData(
        com.itsaky.androidide.lsp.models.DiagnosticItem::class.java)
    ) {
      markInvisible()
      return
    }

    val diagnostic = data[com.itsaky.androidide.lsp.models.DiagnosticItem::class.java]!!
    if (diagnosticCode != diagnostic.code) {
      markInvisible()
      return
    }
  }

  override suspend fun execAction(data: ActionData): Any {
    val diagnostic = data[com.itsaky.androidide.lsp.models.DiagnosticItem::class.java]!!
    val compiler =
      JavaCompilerProvider.get(
        IProjectManager.getInstance().getWorkspace()?.findModuleForFile(data.requireFile(), false)
          ?: return Any()
      )
    val file = data.requirePath()

    return compiler.compile(file).get {
      val unusedMethod = findMethod(it, diagnostic.range)
      RemoveMethod(
        unusedMethod.className,
        unusedMethod.methodName,
        unusedMethod.erasedParameterTypes
      )
    }
  }

  override fun postExec(data: ActionData, result: Any) {
    if (result !is RemoveMethod) {
      log.warn("Unable to remove method")
      return
    }

    performCodeAction(data, result)
  }
}
