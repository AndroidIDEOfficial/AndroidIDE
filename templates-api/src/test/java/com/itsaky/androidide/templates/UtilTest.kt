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

package com.itsaky.androidide.templates

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.templates.base.moduleNameToDirName
import org.junit.Test

/**
 * @author Akash Yadav
 */
class UtilTest {

  @Test
  fun `test module name conversion`() {
    val tests = mapOf("2app" to "app", "app2" to "app2", "2app2" to "app2", "2 app2" to "app2",
      "app name" to "app-name", "app  name" to "app-name", "app-name" to "app-name",
      "app--name" to "app-name")

    tests.forEach { (input, expected) ->
      assertThat(moduleNameToDirName(input)).isEqualTo(expected)
    }
  }
}