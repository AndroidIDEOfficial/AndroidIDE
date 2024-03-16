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

import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.projects.builder.BuildService

/**
 * Marker class for actions that execute build related tasks.
 *
 * @author Akash Yadav
 */
abstract class BaseBuildAction : EditorActivityAction() {

  protected val buildService: BuildService?
    get() = Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE)

  override fun prepare(data: ActionData) {
    super.prepare(data)
    val context = data.getActivity()
    if (context == null) {
      visible = false
      return
    } else {
      visible = true
    }

    enabled = buildService?.let { !it.isBuildInProgress } == true
  }
}
