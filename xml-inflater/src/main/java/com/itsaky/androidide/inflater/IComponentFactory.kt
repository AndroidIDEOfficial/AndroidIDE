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

package com.itsaky.androidide.inflater

import android.view.View
import com.itsaky.androidide.inflater.internal.LayoutFile
import com.itsaky.androidide.lookup.Lookup.Key

/** Creates instances of views and attributes. */
interface IComponentFactory {

  /**
   * Creates a new instance of [IView].
   *
   * @param file The layout file.
   * @param name The fully qualified name of the view being created.
   * @param view The Android view object.
   * @return The [IView] instance.
   */
  fun createView(file: LayoutFile, name: String, view: View): IView

  /**
   * Creates a new [IAttribute] instance.
   *
   * @param view The view to which the attribute will be applied.
   * @param namespace The namespace of the attribute.
   * @param name The name of the attribute.
   * @param value The value of the attribute.
   * @return The [IAttribute] instance.
   */
  fun createAttr(view: IView, namespace: INamespace?, name: String, value: String): IAttribute

  companion object {
    @JvmField val LAYOUT_INFLATER_COMPONENT_FACTORY_KEY = Key<IComponentFactory>()
  }
}
