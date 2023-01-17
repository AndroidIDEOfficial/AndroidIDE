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

package com.itsaky.androidide.inflater.internal

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.resolveAttr

/**
 * View used to show views that cannot be inflated.
 *
 * @author Akash Yadav
 */
class ErrorView(file: LayoutFile, name: String, context: Context, message: String) :
  ViewImpl(file = file, name = name, view = createErrView(context, message)) {
  override val tag: String = name
}

class ErrorLayout(file: LayoutFile, name: String, context: Context) :
  ViewGroupImpl(file = file, name = name, view = createErrLayout(context)) {
  override val tag: String = name
}

private fun createErrView(context: Context, message: String): View {
  return TextView(context).apply {
    text = message
    background = ColorDrawable(Color.WHITE)
    setTextColor(context.resolveAttr(R.attr.colorError))
    setTextIsSelectable(true)
  }
}

private fun createErrLayout(context: Context): ViewGroup {
  return FrameLayout(context)
}
