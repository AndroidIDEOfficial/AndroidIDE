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

package com.itsaky.androidide.actions.build

import android.content.Context
import androidx.core.content.ContextCompat
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.EditorActivityAction
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.utils.ILogger

/** @author Akash Yadav */
class CancelBuildAction() : EditorActivityAction() {

  private val log = ILogger.newInstance(javaClass.simpleName)

  constructor(context: Context) : this() {
    label = context.getString(R.string.title_cancel_build)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_stop_daemons)
  }

  override val id: String = "editor_stopGradleDaemons"

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible) {
      return
    }

    val context = getActivity(data)
    val buildService = Lookup.DEFAULT.lookup(BuildService.KEY_BUILD_SERVICE)
    if (context == null || buildService == null) {
      markInvisible()
      return
    }

    visible = true
    enabled = buildService.isBuildInProgress
  }

  override fun execAction(data: ActionData): Boolean {
    log.info("Sending build cancellation request...")
    Lookup.DEFAULT.lookup(BuildService.KEY_BUILD_SERVICE)?.cancelCurrentBuild()?.whenComplete {
      result,
      error ->
      if (error != null) {
        log.error("Failed to send build cancellation request", error)
        return@whenComplete
      }

      if (!result.wasEnqueued) {
        log.warn(
          "Unable to enqueue cancellation request",
          result.failureReason,
          result.failureReason!!.message
        )
        return@whenComplete
      }

      log.info("Build cancellation request was successfully enqueued...")
    }
    return true
  }
}
