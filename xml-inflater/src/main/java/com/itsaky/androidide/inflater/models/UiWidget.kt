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

package com.itsaky.androidide.inflater.models

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.updatePaddingRelative
import com.blankj.utilcode.util.SizeUtils
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.internal.LayoutFile
import com.itsaky.androidide.inflater.internal.ViewGroupImpl
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.inflater.internal.utils.ViewFactory
import com.itsaky.androidide.inflater.internal.utils.ViewFactory.generateLayoutParams
import com.itsaky.androidide.inflater.utils.lookupComponentFactory
import com.itsaky.androidide.inflater.viewAdapter

open class UiWidget(val name: String, @StringRes val label: Int, @DrawableRes val icon: Int) {

  constructor(
    klass: Class<out View>,
    @StringRes label: Int,
    @DrawableRes icon: Int
  ) : this(klass.name, label, icon)

  /**
   * Creates an [IView] for this widget.
   *
   * @param context The context that will be used for creating (View)[android.view.View] instance.
   * @param layoutFile The layout file that is being edited.
   */
  open fun createView(context: Context, parent: ViewGroup, layoutFile: LayoutFile): IView {
    val v = ViewFactory.createViewInstance(name, context)

    val view: IView = createView(layoutFile, v)
    
    (view as? IViewGroup)?.apply {
      val dp8 = SizeUtils.dp2px(8f)
      this.view.updatePaddingRelative(start = dp8, top = dp8, end = dp8, bottom = dp8)
    }

    view.view.layoutParams = generateLayoutParams(parent)
    val adapter =
      view.viewAdapter
        ?: throw IllegalStateException("No attribute adapter found for '${view.name}'")
    adapter.applyBasic(view)
    return view
  }

  private fun createView(layoutFile: LayoutFile, v: View) =
    (lookupComponentFactory()?.createView(layoutFile, name, v)
      ?: if (v is ViewGroup) {
        ViewGroupImpl(layoutFile, name, v)
      } else ViewImpl(layoutFile, name, v))

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is UiWidget) return false

    if (name != other.name) return false
    if (label != other.label) return false
    if (icon != other.icon) return false

    return true
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + label
    result = 31 * result + icon
    return result
  }

  override fun toString(): String {
    return "UiWidget(name='$name', label=$label, icon=$icon)"
  }
}
