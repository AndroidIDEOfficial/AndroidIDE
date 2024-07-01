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
import android.graphics.drawable.Drawable
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.EditorActionItem
import com.itsaky.androidide.actions.hasRequiredData
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.actions.requireFile
import com.itsaky.androidide.lsp.api.ILanguageClient
import com.itsaky.androidide.lsp.api.ILanguageServerRegistry
import com.itsaky.androidide.lsp.java.JavaCompilerProvider
import com.itsaky.androidide.lsp.java.JavaLanguageServer
import com.itsaky.androidide.lsp.java.R
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.java.rewrite.Rewrite
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.utils.DocumentUtils
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.flashError
import java.io.File

/**
 * Base class for java code actions
 *
 * @author Akash Yadav
 */
abstract class BaseJavaCodeAction : EditorActionItem {

  override var visible: Boolean = true
  override var enabled: Boolean = true
  override var icon: Drawable? = null
  override var requiresUIThread: Boolean = false
  override var location: ActionItem.Location = ActionItem.Location.EDITOR_CODE_ACTIONS

  protected abstract val titleTextRes: Int

  override fun prepare(data: ActionData) {
    super.prepare(data)
    if (
      !data.hasRequiredData(Context::class.java, JavaLanguageServer::class.java, File::class.java)
    ) {
      markInvisible()
      return
    }

    if (titleTextRes != -1) {
      label = data[Context::class.java]!!.getString(titleTextRes)
    }

    val file = data.requireFile()
    visible = DocumentUtils.isJavaFile(file.toPath())
    enabled = visible
  }

  fun performCodeAction(data: ActionData, result: Rewrite) {
    val compiler = data.requireCompiler()

    val actions =
      try {
        result.asCodeActions(compiler, label)
      } catch (e: Exception) {
        flashError(e.cause?.message ?: e.message)
        ILogger.ROOT.error(e.cause?.message ?: e.message, e)
        return
      }

    if (actions == null) {
      onPerformCodeActionFailed(data)
      return
    }

    data.getLanguageClient()?.performCodeAction(actions)
  }

  protected open fun onPerformCodeActionFailed(data: ActionData) {
    flashError(R.string.msg_codeaction_failed)
  }

  protected fun ActionData.requireLanguageServer(): JavaLanguageServer {
    return ILanguageServerRegistry.getDefault().getServer(JavaLanguageServer.SERVER_ID)
        as JavaLanguageServer
  }

  protected fun ActionData.getLanguageClient(): ILanguageClient? {
    return requireLanguageServer().client
  }

  protected fun ActionData.requireCompiler(): JavaCompilerService {
    val module =
      IProjectManager.getInstance().getWorkspace()?.findModuleForFile(requireFile(), false)
    requireNotNull(module) {
      "Cannot get compiler instance. Unable to find module for file: ${requireFile().name}"
    }
    return JavaCompilerProvider.get(module)
  }
}
