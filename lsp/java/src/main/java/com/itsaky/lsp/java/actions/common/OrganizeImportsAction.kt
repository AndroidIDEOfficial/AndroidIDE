package com.itsaky.lsp.java.actions.common

import com.google.googlejavaformat.java.FormatterException
import com.google.googlejavaformat.java.ImportOrderer
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.StopWatch
import com.itsaky.lsp.java.JavaLanguageServer
import com.itsaky.lsp.java.R.string
import com.itsaky.lsp.java.actions.BaseCodeAction
import com.itsaky.lsp.java.models.JavaServerSettings
import io.github.rosemoe.sora.widget.CodeEditor

class OrganizeImportsAction : BaseCodeAction() {
    private val log = ILogger.newInstance(javaClass.simpleName)
    override val id: String = "lsp_java_organizeImports"
    override var label: String = ""
    override val titleTextRes: Int = string.action_organize_imports

    override fun prepare(data: ActionData) {
        super.prepare(data)
        if (!visible) {
            return
        }

        if (!hasRequiredData(data, CodeEditor::class.java)) {
            markInvisible()
            return
        }

        visible = true
        enabled = true
    }

    override fun execAction(data: ActionData): Any {
        val watch = StopWatch("Organize imports")
        return try {
            val editor = requireEditor(data)
            val content = editor.text
            val server = data[JavaLanguageServer::class.java]
            val settings = server!!.settings as JavaServerSettings
            val output = ImportOrderer.reorderImports(content.toString(), settings.style)
            watch.log()
            output
        } catch (e: FormatterException) {
            log.error("Failed to reorder imports", e)
            false
        }
    }

    override fun postExec(data: ActionData, result: Any) {
        super.postExec(data, result)
        if (result is String) {
            if (result.isNotEmpty()) {
                val editor = requireEditor(data)
                editor.setText(result)
            }
        }
    }
}