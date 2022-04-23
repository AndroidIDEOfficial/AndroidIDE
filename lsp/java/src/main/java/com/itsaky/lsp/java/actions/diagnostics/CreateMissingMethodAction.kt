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
import com.itsaky.lsp.java.rewrite.CreateMissingMethod
import com.itsaky.lsp.java.utils.CodeActionUtils.findPosition
import com.itsaky.lsp.models.DiagnosticItem

/** @author Akash Yadav */
class CreateMissingMethodAction : BaseCodeAction() {
    override val id: String = "lsp_java_createMissingMethod"
    override var label: String = ""
    private val diagnosticCode = DiagnosticCode.MISSING_METHOD.id
    private val log = ILogger.newInstance(javaClass.simpleName)

    override val titleTextRes: Int = R.string.action_create_missing_method

    override fun prepare(data: ActionData) {
        super.prepare(data)

        if (!visible || !hasRequiredData(data, DiagnosticItem::class.java)) {
            markInvisible()
            return
        }

        val diagnostic = data[DiagnosticItem::class.java]!!
        if (diagnosticCode != diagnostic.code) {
            markInvisible()
            return
        }
    }

    override fun execAction(data: ActionData): Any {
        val diagnostic = data[DiagnosticItem::class.java]!!
        val server = data[JavaLanguageServer::class.java]!!
        val file = requirePath(data)
        return server.compiler.compile(file).get {
            CreateMissingMethod(file, findPosition(it, diagnostic.range.start))
        }
    }

    override fun postExec(data: ActionData, result: Any) {
        if (result !is CreateMissingMethod) {
            log.warn("Unable to create missing method")
            return
        }

        val server = data[JavaLanguageServer::class.java]!!
        val client = server.client!!
        val file = requireFile(data)

        client.performCodeAction(file, result.asCodeActions(server.compiler, label))
    }
}
