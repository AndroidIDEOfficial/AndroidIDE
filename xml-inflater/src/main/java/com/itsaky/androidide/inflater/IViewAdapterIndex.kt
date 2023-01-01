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
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import com.itsaky.androidide.inflater.internal.ViewAdapterIndexImpl

/**
 * Provides access to all view adapters.
 *
 * @author Akash Yadav
 */
interface IViewAdapterIndex {

  companion object {
    @JvmField val instance: IViewAdapterIndex = ViewAdapterIndexImpl.INSTANCE
  }

  /**
   * Get the adapter for the given fully qualified view name.
   *
   * @param view The fully qualified name of the view class.
   * @return The [IViewAdapter] for the given view name. If no adapter is found for the given view
   * name, returns adapter for `android.view.View` if available or `null`.
   */
  fun getAdapter(view: String): IViewAdapter<out View>?

  /**
   * Get the adapter for the given view.
   *
   * @param view The fully qualified name of the view.
   * @return The view adapter instance or `null` if not found.
   */
  fun getViewAdapter(view: String): IViewAdapter<out View>?

  /**
   * Get the view adapters which provide widgets to the UI designer.
   *
   * @param group The group to get widget providers for.
   * @return The widget providers for the [group].
   */
  fun getWidgetProviders(group: IncludeInDesigner.Group): List<IViewAdapter<out View>>?
}

/**
 * Get the adapter for the given fully qualified view name.
 *
 * @param view The fully qualified name of the view class.
 * @return The [IViewAdapter] for the given view name. If no adapter is found for the given view
 * name, returns adapter for `android.view.View` if available or `null`.
 */
fun getAdapter(view: String): IViewAdapter<out View>? {
  return IViewAdapterIndex.instance.getAdapter(view)
}

/**
 * Get the adapter for the given view.
 *
 * @param view The fully qualified name of the view.
 * @return The view adapter instance or `null` if not found.
 */
fun getViewAdapter(view: String): IViewAdapter<out View>? {
  return IViewAdapterIndex.instance.getViewAdapter(view)
}

/** The view adapter for this view. */
val IView.viewAdapter: IViewAdapter<out View>?
  get() = IViewAdapterIndex.instance.getAdapter(this.name)
