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
import com.itsaky.androidide.classfile.constants.ClassConstant
import com.itsaky.androidide.classfile.constants.Utf8Constant
import java.io.File
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/** @author Akash Yadav */
@RunWith(JUnit4::class)
class ClassFileReaderTest {

  // Extension changed to .txt to prevent git from ignoring this file
  private val codeEditor = File("./src/test/resources/CodeEditor.class.txt")

  @Test
  fun `basic test`() {
    val input = codeEditor.inputStream()
    val reader = ClassFileReader(input)
    val file = reader.read()

    assertThat(file).isNotNull()
    assertThat(file.version).isEqualTo(ClassFileVersion.JAVA_11)

    checkBaseNames(file)
    checkInterfaces(file)
    checkFIelds(file)
    checkAccessFlags(file)
  }

  private fun checkAccessFlags(file: IClassFile) {
    assertThat(file.accessFlags and ClassFileConstants.ACC_PUBLIC).isNotEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_SUPER).isNotEqualTo(0)

    assertThat(file.accessFlags and ClassFileConstants.ACC_ABSTRACT).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_FINAL).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_ANNOTATION).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_ENUM).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_INTERFACE).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_MODULE).isEqualTo(0)
    assertThat(file.accessFlags and ClassFileConstants.ACC_SYNTHETIC).isEqualTo(0)
  }

  private fun checkFIelds(file: IClassFile) {
    file.fields
      .firstOrNull {
        (file.constantPool[it.nameIndex] as Utf8Constant).content() == "mTextActionWindow"
      }
      .apply {
        assertThat(this).isNotNull()

        val name = (file.constantPool[this!!.nameIndex] as Utf8Constant).content()
        assertThat(name).isEqualTo("mTextActionWindow")

        val descriptor = (file.constantPool[this.descriptorIndex] as Utf8Constant).content()
        assertThat(descriptor)
          .isEqualTo("Lio/github/rosemoe/sora/widget/component/EditorTextActionWindow;")

        assertThat(this.accessFlags and ClassFileConstants.ACC_PROTECTED).isNotEqualTo(0)
        assertThat(this.accessFlags and ClassFileConstants.ACC_PRIVATE).isEqualTo(0)
        assertThat(this.accessFlags and ClassFileConstants.ACC_PUBLIC).isEqualTo(0)
      }
  }

  private fun checkInterfaces(file: IClassFile) {
    file.interfaces
      .map { file.constantPool[it] }
      .filterIsInstance<ClassConstant>()
      .map { file.constantPool[it.nameIndex] }
      .filterIsInstance<Utf8Constant>()
      .map { it.bytes.decodeToString() }
      .apply {
        assertThat(this).isNotEmpty()
        assertThat(this.size).isEqualTo(3)
        assertThat(this)
          .containsExactly(
            "io/github/rosemoe/sora/text/ContentListener",
            "io/github/rosemoe/sora/text/FormatThread\$FormatResultReceiver",
            "io/github/rosemoe/sora/text/LineRemoveListener"
          )
      }
  }

  private fun checkBaseNames(file: IClassFile) {
    assertThat(file.getName()).isEqualTo("io/github/rosemoe/sora/widget/CodeEditor")
    assertThat(file.getSuperClassName()).isEqualTo("android/view/View")
  }
}
