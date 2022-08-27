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

package com.itsaky.androidide.classfile

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File

/**
 * @author Akash Yadav
 */
@RunWith(JUnit4::class)
class ClassFileReaderTest {
  
  // Extension changed to .txt to prevent git from ignoring this file
  private val codeEditor = File("./src/test/resources/CodeEditor.class.txt")
  
  @Test
  fun `basic test` () {
    val input = codeEditor.inputStream()
    val reader = ClassFileReader(input)
    val file = reader.read()
    
    assertThat(file).isNotNull()
    assertThat(file.majorVersion).isEqualTo(55)
    assertThat(file.minorVersion).isEqualTo(0)
    
    assertThat(file.accessFlags and ClassFileConstants.ACC_PUBLIC).isNotEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_SUPER).isNotEqualTo(0)
    
    assertThat(file.accessFlags and ClassFileConstants.ACC_ABSTRACT).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_FINAL).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_ANNOTATION).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_ENUM).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_ENUM).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_INTERFACE).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_MODULE).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_SYNTHETIC).isEqualTo(0)
  }
}