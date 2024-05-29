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

package com.itsaky.androidide.lsp.java.utils

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lsp.java.JavaLSPTest
import com.itsaky.androidide.lsp.models.DefinitionParams
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.progress.ICancelChecker
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_VALUE_STRING)
class FindHelperTest {

  @Before
  fun setup() {
    JavaLSPTest.setup()
  }

  @Test
  fun `test FindHelper#findNameIn behavior with on-demand import`() {
    JavaLSPTest.apply {
      openFile("utils/FindHelperRegexElements")

      // Find definition for 'field' class of type 'String'
      val position = Position(9, 7)
      val params = DefinitionParams(file!!, position, ICancelChecker.NOOP)
      val definitions = runBlocking { server.findDefinition(params) }
      assertThat(definitions).isNotNull()
      assertThat(definitions.locations).hasSize(1)
      assertThat(definitions.locations[0].range.contains(Position(6, 20))).isTrue()
    }
  }
}