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

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lsp.java.JavaLSPTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.Instant

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CompilerTest {

  @Before
  fun setup() {
    JavaLSPTest.setup()
  }

  @Test
  fun testMultipleThreads() {
    JavaLSPTest.apply {
      openFile("completion/MembersCompletionTest")
      val threads = mutableListOf<Thread>()
      val thread = Thread {
        getCompiler().compile(file!!).run {
          println(Thread.currentThread().name)
          delay(1000)
        }
      }
      thread.name = "Long running task"
      threads.add(thread)
      thread.start()

      for (i in 0..300) {
        val th = Thread {
          getCompiler().compile(file!!).run { println(Thread.currentThread().name) }
        }
        th.name = "Thread #$i"
        threads.add(th)
        th.start()
      }

      threads.forEach { it.join() }
    }
  }

  @Test
  fun testClosedFileChannel() {
    JavaLSPTest.apply {
      openFile("completion/MembersCompletionTest")

      Thread { getCompiler().compile(file!!).run { delay(500) } }.start()
      Thread { getCompiler().compile(file!!).run { delay(200) } }.start()

      getCompiler().compile(file!!).run { assertThat(it.diagnostics).isNotEmpty() }
    }
  }

  private fun delay(millis: Long) {
    Thread.sleep(millis)
  }

  @Test
  fun testConcurrentAccess() {
    JavaLSPTest.apply {
      openFile("completion/MembersCompletionTest")

      var task = getCompiler().compile(file!!)
      var fileObject = SourceFileObject(file!!)
      val threads = mutableListOf<Thread>()
      for (i in 1..10) {
        threads.add(
          Thread {
              task.run {
                delay(100)
                println(Thread.currentThread())
              }
            }
            .apply { name = "CompileTask Acessor #$i" }
        )
      }

      fileObject = SourceFileObject(file!!, fileObject.contents, Instant.now())
      task = getCompiler().compile(listOf(fileObject))
      threads.forEach { it.start() }

      Thread { task.run { println("Writer thread") } }.start()
      threads.forEach { it.join() }
    }
  }
}
