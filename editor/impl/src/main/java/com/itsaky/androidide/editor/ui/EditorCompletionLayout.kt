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
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import com.itsaky.androidide.editor.R
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

    val resources = completionList.context.resources
    val cornerRadius = resources.getDimensionPixelSize(R.dimen.completion_window_corner_radius)
      .toFloat()

    val strokeWidth = resources
      .getDimensionPixelSize(R.dimen.completion_window_stroke_width)

    (completionList.parent as? ViewGroup?)?.background = GradientDrawable().apply {
      setCornerRadius(cornerRadius)
      setStroke(strokeWidth, colorScheme.getColor(EditorColorScheme.COMPLETION_WND_CORNER))
      setColor(colorScheme.getColor(EditorColorScheme.COMPLETION_WND_BACKGROUND))
    }

    if (completionList.layoutParams is MarginLayoutParams) {
      completionList.updateLayoutParams<MarginLayoutParams> {
        marginStart = strokeWidth
        topMargin = strokeWidth
        marginEnd = strokeWidth
        bottomMargin = strokeWidth
      }
    }
  }
}
