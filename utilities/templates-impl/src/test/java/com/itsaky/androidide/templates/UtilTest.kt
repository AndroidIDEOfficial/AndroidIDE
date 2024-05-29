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
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.templates.ParameterConstraint.CLASS
import com.itsaky.androidide.templates.ParameterConstraint.CLASS_NAME
import com.itsaky.androidide.templates.ParameterConstraint.DIRECTORY
import com.itsaky.androidide.templates.ParameterConstraint.EXISTS
import com.itsaky.androidide.templates.ParameterConstraint.FILE
import com.itsaky.androidide.templates.ParameterConstraint.LAYOUT
import com.itsaky.androidide.templates.ParameterConstraint.MODULE_NAME
import com.itsaky.androidide.templates.ParameterConstraint.NONEMPTY
import com.itsaky.androidide.templates.ParameterConstraint.PACKAGE
import com.itsaky.androidide.templates.base.util.isValidModuleName
import com.itsaky.androidide.templates.base.util.moduleNameToDirName
import com.itsaky.androidide.templates.impl.ConstraintVerifier
import com.itsaky.androidide.utils.FileProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

/**
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = BaseApplication::class)
class UtilTest {

  @Test
  fun `test module name conversion`() {
    val tests = mapOf("2app" to "app", "app2" to "app2", "2app2" to "app2",
      "2 app2" to "app2", "app name" to "app-name", "app  name" to "app-name",
      "app-name" to "app-name", "app--name" to "app-name",
      "my_module" to "my_module")

    tests.forEach { (input, expected) ->
      assertThat(moduleNameToDirName(input)).isEqualTo(expected)
    }
  }

  @Test
  fun `test module name validation`() {
    val tests = mapOf("2app" to false, "app2" to true, "2app2" to false,
      "2 app2" to false, "app name" to false, "app  name" to false,
      "app-name" to true, "app--name" to false)

    tests.forEach { (name, result) ->
      println("Check $name")
      assertThat(isValidModuleName(":$name")).isEqualTo(result)
    }
  }

  @Test
  fun `test constraint verifier`() {
    ConstraintVerifier.apply {

      assertThat(isValid("", listOf(NONEMPTY))).isFalse()
      assertThat(isValid("something", listOf(NONEMPTY))).isTrue()

      assertThat(isValid("activity_main", listOf(LAYOUT))).isTrue()
      assertThat(isValid("2activity_main", listOf(LAYOUT))).isFalse()

      assertThat(isValid("2.invalid.package", listOf(PACKAGE))).isFalse()
      assertThat(isValid("invalid", listOf(PACKAGE))).isFalse()
      assertThat(isValid("2invalid.package", listOf(PACKAGE))).isFalse()
      assertThat(isValid("invalid.package", listOf(PACKAGE))).isFalse()
      assertThat(isValid("inval0d.PacKage", listOf(PACKAGE))).isFalse()
      assertThat(isValid("com.itsaky.androidide", listOf(PACKAGE))).isTrue()

      assertThat(isValid("Class", listOf(CLASS))).isTrue()
      assertThat(isValid("pck.name.Class", listOf(CLASS))).isTrue()
      assertThat(isValid("pck.name.Class_Name", listOf(CLASS))).isTrue()
      assertThat(isValid("pck.name.Class____Name", listOf(CLASS))).isTrue()
      assertThat(isValid("p443ackage.Class_Name", listOf(CLASS))).isTrue()
      assertThat(isValid("package.2Class", listOf(CLASS))).isFalse()
      assertThat(isValid("package.Class", listOf(CLASS))).isFalse()
      assertThat(isValid("package.name.Class", listOf(CLASS))).isFalse()

      assertThat(isValid("ClassName", listOf(CLASS_NAME))).isTrue()
      assertThat(isValid("classname", listOf(CLASS_NAME))).isTrue()
      assertThat(isValid("class_name", listOf(CLASS_NAME))).isTrue()
      assertThat(isValid("class__name", listOf(CLASS_NAME))).isTrue()
      assertThat(isValid("2class__name", listOf(CLASS_NAME))).isFalse()
      assertThat(isValid("2class.name", listOf(CLASS_NAME))).isFalse()

      assertThat(isValid(":app", listOf(MODULE_NAME))).isTrue()
      assertThat(isValid(":app-name", listOf(MODULE_NAME))).isTrue()
      assertThat(isValid(":app_name", listOf(MODULE_NAME))).isTrue()
      assertThat(isValid(":my_module_num_2", listOf(MODULE_NAME))).isTrue()
      assertThat(isValid(":2app", listOf(MODULE_NAME))).isFalse()
      assertThat(isValid("2app", listOf(MODULE_NAME))).isFalse()
      assertThat(isValid(":_app", listOf(MODULE_NAME))).isFalse()

      assertThat(isValid("activity_main", listOf(LAYOUT))).isTrue()
      assertThat(isValid("fragment__main", listOf(LAYOUT))).isTrue()
      assertThat(isValid("layout_main", listOf(LAYOUT))).isTrue()
      assertThat(isValid("Activity_Main", listOf(LAYOUT))).isFalse()
      assertThat(isValid("ActivityMain", listOf(LAYOUT))).isFalse()
      assertThat(isValid("2activity_main", listOf(LAYOUT))).isFalse()
      assertThat(isValid("_activity_main", listOf(LAYOUT))).isFalse()

      val build = FileProvider.currentDir().resolve("build").toFile()
      val file = File(build, "constraint_test_file.txt").also { it.writeText("Test file") }
      val nonExisting = File(build, "non_existing_constraint_test_file.txt")

      assertThat(isValid(build.absolutePath, listOf(EXISTS))).isTrue()
      assertThat(isValid(build.absolutePath, listOf(DIRECTORY))).isTrue()
      assertThat(isValid(build.absolutePath, listOf(EXISTS, DIRECTORY))).isTrue()
      assertThat(isValid(build.absolutePath, listOf(FILE))).isFalse()

      assertThat(isValid(file.absolutePath, listOf(EXISTS))).isTrue()
      assertThat(isValid(file.absolutePath, listOf(FILE))).isTrue()
      assertThat(isValid(file.absolutePath, listOf(EXISTS, FILE))).isTrue()
      assertThat(isValid(file.absolutePath, listOf(DIRECTORY))).isFalse()

      assertThat(isValid(nonExisting.absolutePath, listOf(EXISTS))).isFalse()
      assertThat(isValid(nonExisting.absolutePath, listOf(FILE))).isFalse()
      assertThat(isValid(nonExisting.absolutePath, listOf(EXISTS, FILE))).isFalse()
    }
  }
}