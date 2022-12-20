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

package com.itsaky.androidide.inflater.internal.adapters

import android.widget.CompoundButton
import com.itsaky.androidide.inflater.AttributeHandlerScope
import com.itsaky.androidide.inflater.IView

/**
 * Attribute adapter for [CompoundButton].
 *
 * @author Akash Yadav
 */
abstract class CompoundButtonAdapter<T : CompoundButton> : ButtonAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("buttonTint") { view.buttonTintList = parseColorStateList(context, value) }
    create("button") { view.buttonDrawable = parseDrawable(context, value) }
    create("buttonTintMode") { view.buttonTintMode = parsePorterDuffMode(value) }
    create("checked") { view.isChecked = parseBoolean(value = value, def = true) }
  }

  override fun applyBasic(view: IView) {
    super.applyBasic(view)
    (view.view as CompoundButton).isChecked = true
  }
}
