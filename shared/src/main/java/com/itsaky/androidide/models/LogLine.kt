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
package com.itsaky.androidide.models

import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.LogTagUtils
import com.itsaky.androidide.utils.newRecyclableObjectPool

class LogLine private constructor() : DefaultRecyclable() {

  var unformatted: String? = null
  var date: String? = null
  var time: String? = null
  var pid: String? = null
  var tid: String? = null
  var tag: String? = null
  var message: String? = null
  var level: ILogger.Level? = null
  var formatted = false

  // For JSONRpc and Recyclable
  init {
    resetToDefault()
  }

  fun toSimpleString(): String {
    return if (formatted) String.format(
      "%-25s %-2s %s", LogTagUtils.trimTagIfNeeded(tag, 25), level?.levelChar ?: 'U',
      message) else unformatted!!
  }

  fun formattedTagAndMessage(): String {
    return if (formatted) String.format("%-25s %-2s", LogTagUtils.trimTagIfNeeded(tag, 25),
      message) else unformatted!!
  }

  private fun resetToDefault() {
    message = null
    tag = null
    tid = null
    pid = null
    time = null
    date = null
    unformatted = null
    level = ILogger.Level.DEBUG
    formatted = false
  }

  override fun recycle() {
    resetToDefault()
    logLinePool.recycle(this)
  }

  override fun toString(): String {
    return if (formatted) String.format(
      "%s %s %s %s %-2s %-25s %s",
      date, time, pid, tid, level?.levelChar ?: 'U', LogTagUtils.trimTagIfNeeded(tag, 25),
      message) else unformatted!!
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is LogLine) return false

    if (unformatted != other.unformatted) return false
    if (date != other.date) return false
    if (time != other.time) return false
    if (pid != other.pid) return false
    if (tid != other.tid) return false
    if (tag != other.tag) return false
    if (message != other.message) return false
    if (level != other.level) return false
    if (formatted != other.formatted) return false

    return true
  }

  override fun hashCode(): Int {
    var result = unformatted?.hashCode() ?: 0
    result = 31 * result + (date?.hashCode() ?: 0)
    result = 31 * result + (time?.hashCode() ?: 0)
    result = 31 * result + (pid?.hashCode() ?: 0)
    result = 31 * result + (tid?.hashCode() ?: 0)
    result = 31 * result + (tag?.hashCode() ?: 0)
    result = 31 * result + (message?.hashCode() ?: 0)
    result = 31 * result + (level?.hashCode() ?: 0)
    result = 31 * result + formatted.hashCode()
    return result
  }

  companion object {

    // do not cache too many LogLine items
    // LogLines should be recycled as soon as they are appended to the log view
    private val logLinePool = newRecyclableObjectPool(
      capacity = 16,
      factory = ::LogLine
    )

    @JvmOverloads
    @JvmStatic
    fun obtain(level: ILogger.Level?, tag: String?, message: String?,
      formatted: Boolean = true): LogLine {
      val logLine = logLinePool.obtain()
      logLine.level = level
      logLine.tag = tag
      logLine.message = message
      logLine.formatted = formatted
      return logLine
    }

    @JvmStatic
    fun forLogString(log: String?): LogLine? {
      if (log == null) {
        return null
      }
      val logLine = logLinePool.obtain()
      try {
        val split = log.split("\\s".toRegex(), limit = 7).toTypedArray()
        logLine.level = ILogger.Level.forChar(split[4][0])
        logLine.date = split[0]
        logLine.time = split[1] // time
        logLine.pid = split[2] // process id
        logLine.tid = split[3] // thread id
        logLine.tag = split[5] // tag
        logLine.message = split[6] // message
        logLine.formatted = true
      } catch (th: Throwable) { // do not log the exception with ILogger
        logLine.unformatted = log
        logLine.formatted = false
      }
      return logLine
    }
  }
}