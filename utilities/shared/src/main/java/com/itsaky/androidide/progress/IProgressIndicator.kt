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

package com.itsaky.androidide.progress

import androidx.annotation.FloatRange

/**
 * A progress indicator reports progress of a specific task.
 *
 * @author Akash Yadav
 */
interface IProgressIndicator {

  /**
   * Called when the task begins its execution.
   */
  fun onStart()

  /**
   * Called each time the task progresses.
   *
   * @param progress The progress of the task. Must be a value between 0 (start) and 1 (finish).
   */
  fun onProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float)

  /**
   * Called when the task finishes its execution.
   */
  fun onFinish()
}