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

package com.itsaky.androidide.ui

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.doOnAttach
import androidx.core.view.updateLayoutParams
import com.itsaky.androidide.utils.getSystemBarInsets

/**
 * This view will set its height to the value of a bottom screen inset as soon
 * as it is attached to a window. It is intended to be placed as the last view in a vertical
 * scrolling pane, so elements above it are always positioned above the navigation bar.
 *
 * The width of this view is defined manually
 *
 * @author Smooth E
 */
class BottomInsetHeightDistributor : FrameLayout {

  constructor(context: Context) : super(context) {
    applyLayoutParameters()
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    applyLayoutParameters()
  }

  constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr) {
    applyLayoutParameters()
  }

  private fun applyLayoutParameters() {
    doOnAttach { view ->
      updateLayoutParams<ViewGroup.LayoutParams> {
        height = getSystemBarInsets(view).bottom
      }
    }
  }

}
