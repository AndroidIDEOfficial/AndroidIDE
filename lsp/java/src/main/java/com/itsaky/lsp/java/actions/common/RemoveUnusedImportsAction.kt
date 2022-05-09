package com.itsaky.lsp.java.actions.common

import com.google.googlejavaformat.java.FormatterException
import com.google.googlejavaformat.java.RemoveUnusedImports
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.StopWatch
import com.itsaky.lsp.java.R.string
import com.itsaky.lsp.java.actions.BaseCodeAction
import io.github.rosemoe.sora.widget.CodeEditor

class RemoveUnusedImportsAction : BaseCodeAction() {

    private val log = ILogger.newInstance(javaClass.simpleName)
    override val id: String = "lsp_java_remove_unused_imports"
    override var label: String = ""
    override val titleTextRes: Int = string.action_remove_unused_imports

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
        val watch = StopWatch("Remove unused imports")
        return try {
            val editor = requireEditor(data)
            val content = editor.text
            val output = RemoveUnusedImports.removeUnusedImports(content.toString())
            watch.log()
            output
        } catch (e: FormatterException) {
            log.error("Failed to remove unused imports", e)
            false
        }
    }

    override fun postExec(data: ActionData, result: Any) {
        if (result is String && result.isNotEmpty()) {
            val editor = requireEditor(data)
            editor.setText(result)
        }
    }
}
