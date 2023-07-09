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
import com.itsaky.androidide.templates.Language.Java
import com.itsaky.androidide.templates.Language.Kotlin
import com.itsaky.androidide.templates.base.baseProject
import com.itsaky.androidide.templates.base.modules.android.ManifestActivity
import com.itsaky.androidide.templates.base.modules.android.defaultAppModule
import com.itsaky.androidide.templates.base.modules.createConstructor
import com.itsaky.androidide.templates.base.modules.createMethod
import com.itsaky.androidide.templates.impl.base.createRecipe
import com.itsaky.androidide.templates.impl.basicActivity.basicActivityProject
import com.itsaky.androidide.templates.impl.bottomNavActivity.bottomNavActivityProject
import com.itsaky.androidide.templates.impl.composeActivity.composeActivityProject
import com.itsaky.androidide.templates.impl.emptyActivity.emptyActivityProject
import com.itsaky.androidide.templates.impl.navDrawerActivity.navDrawerActivityProject
import com.itsaky.androidide.templates.impl.noActivity.noActivityProjectTemplate
import com.itsaky.androidide.templates.impl.noAndroidXActivity.noAndroidXActivityProject
import com.itsaky.androidide.templates.impl.tabbedActivity.tabbedActivityProject
import com.itsaky.androidide.xml.permissions.Permission.INTERNET
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import jdkx.lang.model.element.Modifier.PRIVATE
import jdkx.lang.model.element.Modifier.PUBLIC
import jdkx.lang.model.element.Modifier.STATIC
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Test template builder.
 *
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
class TemplateBuilderTest {

  @Test
  fun `root project generator test`() {
    val template = testTemplate("Root", generate = false) {
      baseProject {
        templateName = -123
        thumb = -123
      }
    }

    template.apply {

      assertThat(templateName).isEqualTo(-123)
      assertThat(thumb).isEqualTo(-123)
      assertThat(recipe).isNotNull()

      parameters.apply {
        assertThat(this).isNotEmpty()
        assertThat(this).hasSize(6)
        assertParameterTypes {
          when (it) {
            0 -> StringParameter::class
            1 -> StringParameter::class
            2 -> StringParameter::class
            3 -> EnumParameter::class
            4 -> EnumParameter::class
            5 -> BooleanParameter::class
            else -> throw IndexOutOfBoundsException("index $it")
          }
        }
      }

      widgets.apply {
        assertThat(this).isNotEmpty()
        assertThat(this).hasSize(6)
        assertWidgetTypes {
          when (it) {
            0 -> TextFieldWidget::class
            1 -> TextFieldWidget::class
            2 -> TextFieldWidget::class
            3 -> SpinnerWidget::class
            4 -> SpinnerWidget::class
            5 -> CheckBoxWidget::class
            else -> throw IndexOutOfBoundsException("index $it")
          }
        }
      }
    }
  }

  @Test
  fun `test project with module`() {
    testTemplate("Test", generate = false) {
      baseProject {

        templateName = -123
        thumb = -123

        defaultAppModule {
          manifest {
            addPermission(INTERNET)
            addActivity(
              ManifestActivity(name = ".MainActivity", isExported = true,
                isLauncher = true))
          }

          recipe = createRecipe {
            sources {
              createClass("com.itsaky", "TestClass") {
                createConstructor {
                  addModifiers(PRIVATE)
                  addStatement("throw \$T()",
                    TypeName.get(UnsupportedOperationException::class.java))
                }
                createMethod("main") {
                  addModifiers(PUBLIC, STATIC)
                  returns(TypeName.VOID)
                  addParameter(
                    ParameterSpec.builder(ArrayTypeName.get(String::class.java),
                      "args").build())
                  addStatement("System.out.println(\"Hello world!\")")
                }
              }
            }
          }

          addDependency("androidx.appcompat", "appcompat", "1.5.0")
        }
      }
    }
  }

  @Test
  fun `test empty activity template`() {
    testTemplate("EmptyActivity") {
      emptyActivityProject()
    }
  }

  @Test
  fun `test basic activity template`() {
    testTemplate("BasicActivity") {
      basicActivityProject()
    }
  }

  @Test
  fun `test navigation drawer activity template`() {
    testTemplate("NavigationDrawerActivity") {
      navDrawerActivityProject()
    }
  }

  @Test
  fun `test bottom navigation activity project`() {
    testTemplate("BottomNavigationActivity") {
      bottomNavActivityProject()
    }
  }

  @Test
  fun `test tabbed activity project`() {
    testTemplate("TabbedActivity") {
      tabbedActivityProject()
    }
  }

  @Test
  fun `test no activity project`() {
    testTemplate("NoActivity") {
      noActivityProjectTemplate()
    }
  }

  @Test
  fun `test no AndroidX activity template`() {
    testTemplate("NoAndroidXActivity") {
      noAndroidXActivityProject()
    }
  }

  @Test
  fun `test compose activity template`() {
    testTemplate("ComposeActivity", languages = arrayOf(Kotlin)) {
      composeActivityProject()
    }
  }

  @Test()
  fun `test compose activity template with Java language should fail at generation`() {
    assertThrows("Compose activity requires Kotlin language",
      IllegalArgumentException::class.java) {

      testTemplate("ComposeActivityJava", languages = arrayOf(Java)) {
        composeActivityProject()
      }
    }
  }
}