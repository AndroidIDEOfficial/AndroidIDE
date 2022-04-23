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
package com.itsaky.lsp.java.actions.diagnostics

import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.java.JavaLanguageServer
import com.itsaky.lsp.java.R
import com.itsaky.lsp.java.actions.BaseCodeAction
import com.itsaky.lsp.java.models.DiagnosticCode
import com.itsaky.lsp.java.rewrite.ImplementAbstractMethods
import com.itsaky.lsp.java.utils.JavaDiagnosticUtils
import com.itsaky.lsp.models.DiagnosticItem
import java.io.File
import javax.tools.Diagnostic
import javax.tools.JavaFileObject

/** @author Akash Yadav */
class ImplementAbstractMethodsAction : BaseCodeAction() {
    override val id: String = "lsp_java_implementAbstractMethods"
    override var label: String = ""
    private var diagnosticCode = DiagnosticCode.DOES_NOT_OVERRIDE_ABSTRACT.id
    private val log = ILogger.newInstance(javaClass.simpleName)

    override val titleTextRes: Int = R.string.action_implement_abstract_methods

    @Suppress("UNCHECKED_CAST")
    override fun prepare(data: ActionData) {
        super.prepare(data)

        if (!visible) {
            return
        }

        if (!hasRequiredData(data, DiagnosticItem::class.java)) {
            markInvisible()
            return
        }

        val diagnostic = data.get(DiagnosticItem::class.java)!!
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
    override fun execAction(data: ActionData): Any {
        val diagnostic =
            JavaDiagnosticUtils.asJCDiagnostic(
                data.get(DiagnosticItem::class.java)!!.extra as Diagnostic<out JavaFileObject>)
        return ImplementAbstractMethods(diagnostic!!)
    }

    override fun postExec(data: ActionData, result: Any) {
        if (result !is ImplementAbstractMethods) {
            log.warn("Unable to perform action. Invalid result from execAction(..)")
            return
        }

        val server = data.get(JavaLanguageServer::class.java)!!
        val client = server.client
        if (client == null) {
            log.error("No client set to java language server")
            return
        }

        client.performCodeAction(
            data.get(File::class.java)!!, result.asCodeActions(server.compiler, label))
    }
}
