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

package com.itsaky.androidide.utils

import android.app.Activity
import androidx.annotation.StringRes
import com.blankj.utilcode.util.ActivityUtils
import com.itsaky.androidide.flashbar.Flashbar

fun flashbarBuilder(): Flashbar.Builder? {
  return withActivity { flashbarBuilder() }
}

fun flashMessage(msg: String?, type: FlashType) {
  withActivity { flashMessage(msg, type) }
}

fun flashMessage(@StringRes msg: Int, type: FlashType) {
  withActivity { flashMessage(msg, type) }
}

fun flashSuccess(msg: String?) {
  withActivity { flashSuccess(msg) }
}

fun flashSuccess(@StringRes msg: Int) {
  withActivity { flashSuccess(msg) }
}

fun flashError(msg: String?) {
  withActivity { flashError(msg) }
}

fun flashError(@StringRes msg: Int) {
  withActivity { flashError(msg) }
}

fun flashInfo(msg: String?) {
  withActivity { flashInfo(msg) }
}

fun flashInfo(@StringRes msg: Int) {
  withActivity { flashInfo(msg) }
}

@JvmOverloads
fun <R> flashProgress(
  configure: (Flashbar.Builder.() -> Unit)? = null,
  action: (Flashbar) -> R?
) : R? {
  return withActivity { flashProgress(configure, action) }
}

private fun <T> withActivity(action: Activity.() -> T?): T? {
  return ActivityUtils.getTopActivity()?.let { it.action() }
    ?: run {
      ILogger.ROOT.warn("Cannot show flashbar message. Cannot get top activity.")
      null
    }
}

/** The type of flashbar message. */
enum class FlashType {

  ERROR,
  INFO,
  SUCCESS
}
