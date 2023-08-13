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
package com.itsaky.androidide.fragments.output

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.R

class BuildOutputFragment : NonEditableEditorFragment() {
  private val unsavedLines: MutableList<String?> = ArrayList()
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    emptyStateViewModel.emptyMessage.value = getString(R.string.msg_emptyview_buildoutput)
    if (unsavedLines.isNotEmpty()) {
      for (line in unsavedLines) {
        editor?.append("${line!!.trim()}\n")
      }
      unsavedLines.clear()
    }
  }
  
  override fun onDestroyView() {
    editor?.release()
    super.onDestroyView()
  }
  
  fun appendOutput(output: String?) {
    if (editor == null) {
      unsavedLines.add(output)
      return
    }
    ThreadUtils.runOnUiThread {
      val message = if (output == null || output.endsWith("\n")) {
        output
      } else {
        "${output}\n"
      }
      editor!!.append(message).also {
        emptyStateViewModel.isEmpty.value = false
      }
    }
  }
}