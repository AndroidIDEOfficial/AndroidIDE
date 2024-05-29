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

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.testing.tooling.ToolingApiTestLauncher
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author Akash Yadav
 */
@RunWith(JUnit4::class)
class ProjectInitializationCancellationTest {

  @Test
  fun `test project initialization cancellation`() {
    // launch project initialization
    ToolingApiTestLauncher.launchServerAsync {

      Thread.sleep(1000L)

      // cancel initialization request
      val cancellationResult = server.cancelCurrentBuild().get()
      println("Cancellation result: $cancellationResult")
      assertThat(cancellationResult).isNotNull()
      assertThat(cancellationResult.wasEnqueued).isTrue()
      assertThat(cancellationResult.failureReason).isNull()

      // verify that the initialization failed with reason BUILD_CANCELLED
      val initResult = initializeResult.get()
      assertThat(initResult!!.isSuccessful).isFalse()
      assertThat(initResult.failure).isEqualTo(TaskExecutionResult.Failure.BUILD_CANCELLED)
    }
  }
}