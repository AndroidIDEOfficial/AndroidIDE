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

package com.itsaky.androidide.templates

import android.content.Context
import android.view.View
import com.itsaky.androidide.utils.ServiceLoader

/**
 * [ITemplateWidgetViewProvider] creates [views][View] for various type of [widgets][Widget].
 *
 * @author Akash Yadav
 */
interface ITemplateWidgetViewProvider {

  companion object {

    private var service: ITemplateWidgetViewProvider? = null

    @JvmStatic
    @JvmOverloads
    fun getInstance(reload: Boolean = false): ITemplateWidgetViewProvider {
      if (reload) {
        service = null
      }
      return service ?: ServiceLoader.load(
        ITemplateWidgetViewProvider::class.java)
        .findFirstOrThrow()
        .also { service = it }
    }
  }

  /**
   * Creates [View] objects for the given widget.
   *
   * @param context The context used to create the views.
   * @param widget The widget to create the view for.
   * @return The view.
   */
  fun <T> createView(context: Context, widget: Widget<T>): View
}