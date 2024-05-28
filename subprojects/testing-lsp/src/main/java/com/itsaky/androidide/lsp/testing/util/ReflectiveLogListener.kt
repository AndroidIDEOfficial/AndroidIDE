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
package com.itsaky.androidide.lsp.testing.util

import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.ILogger.Level

/** @author Akash Yadav */
class ReflectiveLogListener(private val receiver: Class<out Any>) : ILogger.LogListener {
  override fun log(level: Level, tag: String, message: String) {
    val method =
      receiver.getDeclaredMethod(
        "log",
        Level::class.java,
        String::class.java,
        String::class.java
      )
    method.isAccessible = true
    method.invoke(null, level, tag, message)
  }
}
