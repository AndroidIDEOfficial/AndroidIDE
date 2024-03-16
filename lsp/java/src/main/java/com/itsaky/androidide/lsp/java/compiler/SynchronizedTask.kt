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
package com.itsaky.androidide.lsp.java.compiler

import com.itsaky.androidide.lsp.java.CompilationCancellationException
import com.itsaky.androidide.lsp.java.utils.CancelChecker.Companion.isCancelled
import org.slf4j.LoggerFactory
import java.util.concurrent.Semaphore

class SynchronizedTask {

  @Volatile
  @PublishedApi
  internal var isCompiling = false

  @PublishedApi
  internal val semaphore = Semaphore(1)

  @PublishedApi
  internal var task: CompileTask? = null
    private set

  companion object {

    @PublishedApi
    internal val log = LoggerFactory.getLogger(SynchronizedTask::class.java)
  }

  inline fun run(crossinline taskConsumer: (CompileTask) -> Unit) {
    try {
      semaphore.acquire()
    } catch (e: InterruptedException) {
      throw CompilationCancellationException(e)
    }
    try {
      taskConsumer(task!!)
    } catch (err: Throwable) {
      if (!isCancelled(err)) {
        log.error("An error occurred while working with compilation task", err)
      }
      throw err
    } finally {
      semaphore.release()
    }
  }

  inline fun <T : Any?> get(crossinline action: (CompileTask) -> T): T {
    try {
      semaphore.acquire()
    } catch (e: InterruptedException) {
      throw CompilationCancellationException(e)
    }
    return try {
      action(task!!)
    } catch (err: Throwable) {
      if (!isCancelled(err)) {
        log.error("An error occurred while working with compilation task", err)
      }
      throw err
    } finally {
      semaphore.release()
    }
  }

  fun post(action: Runnable) = post { action.run() }

  inline fun post(action: () -> Unit) {
    try {
      semaphore.acquire()
    } catch (e: InterruptedException) {
      throw CompilationCancellationException(e)
    }
    isCompiling = true
    try {
      if (task != null) {
        task!!.close()
      }
      action()
    } catch (err: Throwable) {
      if (!isCancelled(err)) {
        log.error("An error occurred", err)
      }
      throw err
    } finally {
      semaphore.release()
      isCompiling = false
    }
  }

  fun setTask(task: CompileTask?) {
    this.task = task
  }

  @get:Synchronized
  val isBusy: Boolean
    get() = isCompiling || semaphore.availablePermits() == 0

  /**
   * **FOR INTERNAL USE ONLY!**
   */
  fun logStats() {
    log.warn("[SynchronizedTask] isCompiling={} queuedLength={}", isCompiling,
      semaphore.queueLength)
  }
}