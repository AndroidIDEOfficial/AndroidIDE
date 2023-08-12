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

package com.itsaky.androidide.actions

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import kotlin.reflect.KClass

/**
 * An [ActionItem] that is shown in the editor activity's sidebar. If the action's [fragmentClass] is null,
 * the [ActionItem.execAction] is called when the user clicks on the navigation item in the navigation rail.
 *
 * The [id] property of the action is used as the route for the navigation graph.
 *
 * @author Akash Yadav
 */
interface SidebarActionItem : ActionItem {

  /**
   * The fragment class for this action item. Implementations can provide a fragment class which
   * will be shown when this action item is selected.
   */
  val fragmentClass: KClass<out Fragment>?

  /**
   * Build/setup the fragment navigation for this action item. This method is called only if the [fragmentClass] property
   * is non-null.
   */
  fun FragmentNavigatorDestinationBuilder.buildNavigation() {}
}