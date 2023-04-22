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

package com.itsaky.androidide.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.databinding.FragmentLogBinding
import com.itsaky.androidide.editor.language.log.LogLanguage
import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.ILogger.Priority
import com.itsaky.androidide.utils.jetbrainsMono
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.min

/**
 * Fragment to show logs.
 *
 * @author Akash Yadav
 */
abstract class LogViewFragment : Fragment(), ShareableOutputFragment {

  companion object {

    /** The maximum number of characters to append to the editor in case of huge log texts. */
    const val MAX_CHUNK_SIZE = 10000

    /**
     * The time duration, in milliseconds which is used to determine whether logs are too frequent
     * or not. If the logs are produced within this time duration, they are considered as too
     * frequent. In this case, the logs are cached and appended in chunks of [MAX_CHUNK_SIZE]
     * characters in size.
     */
    const val LOG_FREQUENCY = 50L

    /**
     * The time duration, in milliseconds we wait before appending the logs. This must be greater
     * than [LOG_FREQUENCY].
     */
    const val LOG_DELAY = 100L

    /** The maximum number of lines that are shown in the log view. */
    const val MAX_LINE_COUNT = 5000
  }

  var binding: FragmentLogBinding? = null

  private var lastLog = -1L

  private val cacheLock = ReentrantLock()
  private val cache = StringBuilder()
  private var cacheLineTrack = ArrayBlockingQueue<Int>(MAX_LINE_COUNT, true)

  private val log = ILogger.newInstance("LogViewFragment")

  private val logHandler = Handler(Looper.getMainLooper())
  private val logRunnable =
    object : Runnable {
      override fun run() {
        cacheLock.withLock {
          if (cacheLineTrack.size == MAX_LINE_COUNT) {
            cache.delete(0, cacheLineTrack.poll()!!)
          }

          cacheLineTrack.clear()

          if (cache.length < MAX_CHUNK_SIZE) {
            append(cache)
            cache.clear()
          } else {
            // Append the lines in chunks to avoid UI lags
            val length = min(cache.length, MAX_CHUNK_SIZE)
            append(cache.subSequence(0, length))
            cache.delete(0, length)
          }

          if (cache.isNotEmpty()) {
            // if we still have data left to append, resechedule this
            logHandler.removeCallbacks(this)
            logHandler.postDelayed(this, LOG_DELAY)
          } else {
            log.debug("Cache has become empty, trimming lines at start")
            trimLinesAtStart()
          }
        }
      }
    }

  fun appendLog(line: LogLine) {

    var lineString =
      if (isSimpleFormattingEnabled()) {
        line.toSimpleString()
      } else {
        line.toString()
      }

    if (!lineString.endsWith("\n")) {
      lineString += "\n"
    }

    if (cache.isNotEmpty() || System.currentTimeMillis() - lastLog <= LOG_FREQUENCY) {
      cacheLock.withLock {
        logHandler.removeCallbacks(logRunnable)

        // If the log lines are too frequent, cache the lines to log them later at once
        cache.append(lineString)
        logHandler.postDelayed(logRunnable, LOG_DELAY)

        lastLog = System.currentTimeMillis()

        val length = cache.length + 1
        if (!cacheLineTrack.offer(length)) {
          cacheLineTrack.poll()
          cacheLineTrack.offer(length)
        }
      }
      return
    }

    lastLog = System.currentTimeMillis()

    append(lineString)
    trimLinesAtStart()
  }

  private fun append(chars: CharSequence?) {
    chars?.let { ThreadUtils.runOnUiThread { binding?.editor?.append(chars) } }
  }

  private fun trimLinesAtStart() {
    ThreadUtils.runOnUiThread {
      binding?.editor?.text?.apply {
        if (lineCount <= MAX_LINE_COUNT) {
          return@apply
        }

        val lastLine = lineCount - MAX_LINE_COUNT
        log.debug("Deleting log text till line $lastLine")
        delete(0, 0, lastLine, getColumnCount(lastLine))
      }
    }
  }

  abstract fun isSimpleFormattingEnabled(): Boolean

  protected open fun logLine(priority: Priority, tag: String, message: String) {
    val line = LogLine(priority, tag, message)
    appendLog(line)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = FragmentLogBinding.inflate(inflater, container, false)
    return binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val editor = this.binding!!.editor
    editor.props.autoIndent = false
    editor.isEditable = false
    editor.dividerWidth = 0f
    editor.isWordwrap = false
    editor.isUndoEnabled = false
    editor.typefaceLineNumber = jetbrainsMono()
    editor.setTextSize(12f)
    editor.typefaceText = jetbrainsMono()

    IDEColorSchemeProvider.readScheme(requireContext()) { scheme ->
      editor.applyTreeSitterLang(LogLanguage(requireContext()), LogLanguage.TS_TYPE, scheme)
    }
  }

  override fun onDestroyView() {
    binding?.editor?.release()
    super.onDestroyView()
  }

  override fun onDestroy() {
    super.onDestroy()
    this.binding = null
  }

  override fun getContent(): String {
    if (this.binding == null) {
      return ""
    }

    return this.binding!!.editor.text.toString()
  }

  override fun clearOutput() {
    binding?.editor?.setText("")
  }
}
