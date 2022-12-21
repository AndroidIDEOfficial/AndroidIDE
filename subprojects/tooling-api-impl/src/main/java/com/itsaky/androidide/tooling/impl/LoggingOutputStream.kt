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

package com.itsaky.androidide.tooling.impl

import com.itsaky.androidide.tooling.api.IToolingApiClient
import java.io.OutputStream

/**
 * Sends the output received from Gradle build to the [IToolingApiClient].
 * @author Akash Yadav
 */
class LoggingOutputStream : OutputStream() {
  private val lineBuilder = StringBuilder()
  override fun write(b: Int) {
    val c = b.toChar()
    lineBuilder.append(c)

    if (c == '\n' && Main.client != null) {
      Main.client.logOutput(lineBuilder.toString())
      lineBuilder.clear()
    }
  }
}
