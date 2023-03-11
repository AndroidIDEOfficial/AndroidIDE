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

package com.itsaky.androidide.editor.ui

import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import com.blankj.utilcode.util.SizeUtils.dp2px
import io.github.rosemoe.sora.widget.component.DefaultCompletionLayout
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme

/**
 * An implementation of [DefaultCompletionLayout] which customizes some attributes of the default
 * completion window layout.
 *
 * @author Akash Yadav
 */
class EditorCompletionLayout : DefaultCompletionLayout() {

  override fun onApplyColorScheme(colorScheme: EditorColorScheme) {
    super.onApplyColorScheme(colorScheme)

    val gd =
      (completionList.parent as? ViewGroup?)?.background as? GradientDrawable?
        ?: throw RuntimeException(
          "CompletionLayout implementation changed. Please report this issue."
        )

    gd.setStroke(dp2px(1f), colorScheme.getColor(EditorColorScheme.COMPLETION_WND_CORNER))
  }
}
