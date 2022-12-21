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

package com.itsaky.androidide.tooling.api.messages.result

/**
 * Result for a build cancellation request.
 *
 * @param wasEnqueued `true`, if the cancellation request was enqueued,`false` otherwise.
 * @param failureReason The reason for the cancellation request enqueue failure.
 * @author Akash Yadav
 */
data class BuildCancellationRequestResult(
  val wasEnqueued: Boolean,
  val failureReason: Reason? = null
) {

  /** Reason for build cancellation request failure. */
  enum class Reason private constructor(var message: String) {
    NO_RUNNING_BUILD("No running builds"),
    CANCELLATION_ERROR("An error occurred performing the cancellation request"),
    UNKNOWN("Unknown error")
  }
}
