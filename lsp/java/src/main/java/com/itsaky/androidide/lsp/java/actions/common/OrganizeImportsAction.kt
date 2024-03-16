package com.itsaky.androidide.lsp.java.actions.common

import com.google.googlejavaformat.java.FormatterException
import com.google.googlejavaformat.java.ImportOrderer
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.hasRequiredData
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.actions.requireEditor
import com.itsaky.androidide.editor.api.IEditor
import com.itsaky.androidide.lsp.java.JavaLanguageServer
import com.itsaky.androidide.lsp.java.actions.BaseJavaCodeAction
import com.itsaky.androidide.lsp.java.models.JavaServerSettings
import com.itsaky.androidide.resources.R.string
import io.github.rosemoe.sora.widget.CodeEditor
import org.slf4j.LoggerFactory

class OrganizeImportsAction : BaseJavaCodeAction() {

  override val id: String = "lsp_java_organizeImports"
  override var label: String = ""
  override val titleTextRes: Int = string.action_organize_imports

  companion object {

    private val log = LoggerFactory.getLogger(OrganizeImportsAction::class.java)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)
    if (!visible) {
      return
    }

    if (!data.hasRequiredData(CodeEditor::class.java)) {
      markInvisible()
      return
    }

    visible = true
    enabled = true
  }

  override suspend fun execAction(data: ActionData): Any {
    val watch = com.itsaky.androidide.utils.StopWatch("Organize imports")
    return try {
      val editor = data.requireEditor()
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
        val editor = data.requireEditor()
        val cursor = editor.cursor.left()

        editor.text.apply {
          val endLine = getLine(lineCount - 1)
          replace(0, 0, lineCount - 1, endLine.length + endLine.lineSeparator.length, result)
        }

        (editor as? IEditor?)?.also {
          it.setSelectionAround(cursor)
          editor.ensureSelectionVisible()
        }
      }
    }
  }
}
