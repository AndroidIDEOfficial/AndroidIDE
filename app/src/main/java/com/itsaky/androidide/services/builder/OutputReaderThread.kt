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

package com.itsaky.androidide.services.builder

import java.io.InputStream
import java.util.function.Consumer

/**
 * Reads output from the Tooling API server.
 *
 * @author Akash Yadav
 */
class OutputReaderThread(private val input: InputStream, private var onRead: Consumer<String>?) :
  Thread("ToolingServerOutputReader") {

  override fun run() {
    input.bufferedReader().use {
      var line: String? = it.readLine()
      while (line != null) {
        onRead?.accept(line)
        line = it.readLine()
      }
    }
  }
  
  override fun interrupt() {
    super.interrupt()
    onRead = null
  }
}
