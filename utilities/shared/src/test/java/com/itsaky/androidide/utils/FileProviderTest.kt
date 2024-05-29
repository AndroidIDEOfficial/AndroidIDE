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

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.File
import java.nio.file.Files

class FileProviderTest {

  @Test
  fun testPath() {
    val path = FileProvider.sourceFile("SourceFileTest", "java")
    assertThat(Files.exists(path)).isTrue()
    assertThat(path.fileName.toString()).isEqualTo("SourceFileTest_template.java")
  }

  @Test
  fun testNested() {
    val path = FileProvider.sourceFile("package/SourceFileTest", "java")
    assertThat(Files.exists(path)).isTrue()
    assertThat(path.fileName.toString()).isEqualTo("SourceFileTest_template.java")
  }
}