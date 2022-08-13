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

package com.itsaky.androidide.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.databinding.FragmentLogBinding
import com.itsaky.androidide.language.log.LogLanguage
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE
import com.itsaky.androidide.utils.ILogger.Priority
import com.itsaky.androidide.utils.TypefaceUtils

/**
 * Fragment to show logs.
 * @author Akash Yadav
 */
abstract class LogViewFragment : Fragment(), ShareableOutputFragment {

  var binding: FragmentLogBinding? = null

  fun appendLog(line: LogLine) {
    if (this.binding == null) {
      System.err.println("Cannot append log line. Binding is null.")
      return
    }

    var lineString =
      if (isSimpleFormattingEnabled()) {
        line.toSimpleString()
      } else {
        line.toString()
      }

    if (!lineString.endsWith("\n")) {
      lineString += "\n"
    }

    ThreadUtils.runOnUiThread { this.binding!!.editor.append(lineString) }
  }

  abstract fun isSimpleFormattingEnabled(): Boolean

  protected open fun logLine(priority: Priority, tag: String, message: String) {
    val line = LogLine(priority, tag, message)
    appendLog(line)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = FragmentLogBinding.inflate(inflater, container, false)
    return binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val editor = this.binding!!.editor
    editor.props.autoIndent = false
    editor.isEditable = false
    editor.dividerWidth = 0f
    editor.isWordwrap = false
    editor.isUndoEnabled = false
    editor.typefaceLineNumber = TypefaceUtils.jetbrainsMono()
    editor.setTextSize(12f)
    editor.typefaceText = TypefaceUtils.jetbrainsMono()
    editor.colorScheme = SchemeAndroidIDE()
    editor.setEditorLanguage(LogLanguage())
  }

  override fun onDestroy() {
    super.onDestroy()
    this.binding = null
  }

  override fun getContent(): String {
    if (this.binding == null) {
      return ""
    }

    return this.binding!!.editor.text.toString()
  }

  override fun clearOutput() {
    binding?.editor?.setText("")
  }
}
