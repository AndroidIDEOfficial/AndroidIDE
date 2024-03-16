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
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.hasRequiredData
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.uidesigner.R
import com.itsaky.androidide.uidesigner.ShowXmlActivity
import com.itsaky.androidide.uidesigner.utils.ViewToXml
import com.itsaky.androidide.utils.flashError

/**
 * Navigates the user to [ShowXmlActivity].
 *
 * @author Akash Yadav
 */
class ShowXmlAction(context: Context) : UiDesignerAction() {

  override val id: String = "ide.uidesigner.showXml"

  init {
    label = context.getString(R.string.xml)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_language_xml)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)
    if (!data.hasRequiredData(Context::class.java, Fragment::class.java)) {
      markInvisible()
      return
    }

    val fragment = data.requireWorkspace()

    this.visible = true
    this.enabled = fragment.workspaceView.childCount == 1
  }

  override suspend fun execAction(data: ActionData): Any {
    data.requireActivity().apply {
      val workspace = data.requireWorkspace().workspaceView
      ViewToXml.generateXml(this, workspace, { result ->
        val intent = Intent(this, ShowXmlActivity::class.java)
        intent.putExtra(ShowXmlActivity.KEY_XML, result)
        startActivity(intent)
      }) { result, error ->
        if (result == null || error != null) {
          val message = "${
            getString(R.string.msg_generate_xml_failed)
          }: ${error?.cause?.message ?: error?.message ?: "Unknown error"}"
          flashError(message)
        }
      }
    }
    return true
  }
}
