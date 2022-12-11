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

package com.itsaky.androidide.uidesigner.actions

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.hasRequiredData
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.tasks.executeAsyncProvideError
import com.itsaky.androidide.uidesigner.R
import com.itsaky.androidide.uidesigner.ShowXmlActivity
import com.itsaky.androidide.uidesigner.utils.ViewToXml
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.ILogger
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException

/**
 * Navigates the user to [ShowXmlActivity].
 *
 * @author Akash Yadav
 */
class ShowXmlAction(context: Context) : UiDesignerAction() {
  override val id: String = "ide.uidesigner.showXml"

  private val log = ILogger.newInstance("ShowXmlAction")

  init {
    label = context.getString(R.string.xml)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_language_xml)
  }

  override fun prepare(data: ActionData) {
    if (!data.hasRequiredData(Context::class.java, Fragment::class.java)) {
      markInvisible()
      return
    }

    val fragment = data.requireWorkspace()

    this.visible = true
    this.enabled = fragment.workspaceView.childCount == 1
  }

  override fun execAction(data: ActionData): Any {
    data.requireActivity().apply {
      val workspace = data.requireWorkspace().workspaceView
      val future: CompletableFuture<String?> =
        executeAsyncProvideError({
          if (workspace.childCount == 0) {
            throw CompletionException(
              IllegalStateException("No views have been added to workspace")
            )
          }

          if (workspace.childCount > 1) {
            throw CompletionException(
              IllegalStateException("Invalid view hierarchy. More than one root views found.")
            )
          }

          return@executeAsyncProvideError ViewToXml.generateXml(workspace[0] as ViewImpl)
        }) { _, _ ->
        }

      val progress =
        DialogUtils.newProgressDialog(
            this,
            getString(R.string.title_generating_xml),
            getString(R.string.please_wait),
            false
          ) { _, _ ->
            future.cancel(true)
          }
          .show()

      future.whenComplete { result, error ->
        ThreadUtils.runOnUiThread {
          progress.dismiss()

          if (result.isNullOrBlank() || error != null) {
            log.error("Unable to generate XML code", error)
            return@runOnUiThread
          }

          val intent =
            Intent(this, ShowXmlActivity::class.java).apply {
              putExtra(ShowXmlActivity.KEY_XML, result)
            }
          startActivity(intent)
        }
      }
    }
    return true
  }
}
