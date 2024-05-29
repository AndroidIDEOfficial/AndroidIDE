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

import android.app.ActivityManager
import android.os.Debug
import android.os.Debug.MemoryInfo
import androidx.collection.IntObjectMap
import androidx.collection.MutableIntObjectMap
import androidx.core.content.getSystemService
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.tasks.cancelIfActive
import com.termux.shared.reflection.ReflectionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Handles memory usage information of the IDE.
 *
 * @property updateInterval The interval at which to update the memory usage.
 * @author Akash Yadav
 */
class MemoryUsageWatcher(
  private val updateInterval: Long = DEFAULT_UPDATE_INTERVAL
) {

  @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
  private val coroutineDispatcher = newSingleThreadContext("MemoryUsageWatcher")
  private val coroutineScope = CoroutineScope(coroutineDispatcher)
  private val memoryUsage = ConcurrentHashMap<Int, ProcessMemoryInfo>()
  private val watching = AtomicBoolean(false)

  /**
   * Whether the memory usage watcher is watching processes for their memory usage.
   */
  val isWatching: Boolean
    get() = watching.get()

  /**
   * The listener to be notified when the memory usage of a process changes.
   */
  var listener: MemoryUsageListener? = null

  companion object {

    private val android_os_Debug_getMemoryInfo by lazy {
      checkNotNull(ReflectionUtils.getDeclaredMethod(Debug::class.java, "getMemoryInfo",
        Int::class.javaPrimitiveType, MemoryInfo::class.java)) {
        "Unable to find getMemoryInfo method in android.os.Debug class"
      }
    }

    const val MAX_USAGE_ENTRIES = 30
    const val DEFAULT_UPDATE_INTERVAL = 1000L
    private val log = LoggerFactory.getLogger(MemoryUsageWatcher::class.java)
  }

  /**
   * Start watching processes for their memory usage.
   */
  fun startWatching() {
    if (isWatching) {
      log.warn("Processes are already being watched for memory usage")
      return
    }

    watching.set(true)

    coroutineScope.launch(context = SupervisorJob() + coroutineDispatcher) {
      while (isWatching) {
        readUsages()

        // don't bother to update if no listeners are set
        listener?.also { listener ->
          val usages = MutableIntObjectMap<ProcessMemoryInfo>(memoryUsage.size)
          for ((pid, usage) in this@MemoryUsageWatcher.memoryUsage) {
            usages[pid] = usage
          }
          withContext(Dispatchers.Main.immediate) {
            listener.onMemoryUsageChanged(usages)
          }
        }

        delay(1000)
      }
    }
  }

  private fun readUsages() {
    val activityManager = BaseApplication.getBaseInstance().getSystemService<ActivityManager>()
    if (activityManager == null) {
      log.error("ActivityManager is null")
      return
    }

    val pids = memoryUsage.keys.toIntArray()
    pids.forEach { pid ->

      // ActivityManager.getProcessMemoryInfo is rate-limited
      // but it internally uses Debug.getMemoryInfo to get the memory info
      // we use it directly using reflection to bypass the rate limit
      val proc = memoryUsage[pid] ?: run {
        log.warn("Process {} is not being watched, but readUsages() was called for the process", pid)
        return@forEach
      }

      ReflectionUtils.invokeMethod(android_os_Debug_getMemoryInfo, null, pid, proc.memInfo)

      // From https://developer.android.com/tools/dumpsys#meminfo
      // "PSS is a good measure for the actual RAM weight of a process and for comparison against
      // the RAM use of other processes and the total available RAM."
      val usage = proc.memInfo.totalPss

      // values are in kB, convert to bytes
      val usageBytes = usage * 1024L
      memoryUsage[pid]!!.apply {
        // we insert the usage entry at the start of the array, then increment the shift amount by 1
        // this makes the newly inserted usage entry the last element in the array
        // and the oldest usage entry the first element in the array

        // this means that _history[_history.size - 1] will be the newest usage entry

        // the "shift" amount basically indicates what is the start index of the array
        // for example, if shift is 1, then _history[0] will actually return _history[1] (index shifted by 1 to the right)
        // when the shift amount exceeds the size of the array, it will be reset to 0 (wrapped around)

        _history[0] = usageBytes
        _history.shift(1)
      }
    }
  }

  /**
   * Watches the memory usage of the given process.
   *
   * @param pid The process ID.
   * @param pname The process name.
   * @param unique Whether to unwatch the process with the same process name.
   */
  fun watchProcess(pid: Int, pname: String, unique: Boolean = true) {
    if (memoryUsage.containsKey(pid)) {
      log.warn("Process {} is already being watched", pid)
      return
    }

    if (unique) {
      // unwatch the process with the given process name
      unwatchProcess(pname)
    }

    memoryUsage[pid] = ProcessMemoryInfo(pid, pname,
      MutableShiftedLongArray(MAX_USAGE_ENTRIES))
  }

  /**
   * Returns the memory usage of all the registered processes.
   */
  fun getMemoryUsages(): Array<ProcessMemoryInfo> {
    return memoryUsage.values.toTypedArray()
  }

  /**
   * Returns the memory usage of the given process (in bytes).
   */
  fun getMemoryUsage(processId: Int): ProcessMemoryInfo? {
    return memoryUsage[processId]
  }

  /**
   * Removes the given process from the watch list.
   */
  fun unwatchProcess(processId: Int) {
    memoryUsage.remove(processId)
  }

  /**
   * Removes the process with the given process name from the watch list.
   */
  fun unwatchProcess(procName: String) {
    memoryUsage.values.forEach {
      if (it.pname == procName) {
        memoryUsage.remove(it.pid)
      }
    }
  }

  /**
   * Unwatches all the registered processes.
   */
  fun unwatchAll() {
    memoryUsage.clear()
  }

  /**
   * Stop watching processes for their memory usage.
   */
  fun stopWatching(unwatchAll: Boolean = true) {
    if (unwatchAll) {
      unwatchAll()
    }
    watching.set(false)
    coroutineScope.cancelIfActive("Cancellation requested")
  }

  /**
   * Registers a listener to be notified when the memory usage of a process changes.
   */
  fun interface MemoryUsageListener {

    /**
     * Called when the memory usage of a process changes.
     *
     * @param memoryUsage The memory usage of all the registered processes.
     */
    fun onMemoryUsageChanged(memoryUsage: IntObjectMap<ProcessMemoryInfo>)
  }

  /**
   * Represents the memory usage of a process.
   *
   * @property pid The process ID.
   * @property memInfo The latest [MemoryInfo] object. Stored here to ensure that we only allocate
   * a single [MemoryInfo] object for a process.
   * @property usageHistory The memory usage history of the process.
   */
  data class ProcessMemoryInfo(
    val pid: Int,
    val pname: String,
    internal val _history: MutableShiftedLongArray
  ) {

    internal val memInfo: MemoryInfo = MemoryInfo()

    val usageHistory: ShiftedLongArray
      get() = _history

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is ProcessMemoryInfo) return false

      if (pid != other.pid) return false
      if (!_history.contentEquals(other._history)) return false

      return true
    }

    override fun hashCode(): Int {
      var result = pid
      result = 31 * result + _history.contentHashCode()
      return result
    }
  }
}