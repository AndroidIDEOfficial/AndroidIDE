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

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.managers.PreferenceManager
import com.itsaky.androidide.preferences.internal.prefManager
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.FileProvider
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkConstructor
import java.io.File
import kotlin.reflect.KClass

val testProjectsDir: File by lazy {
  FileProvider.currentDir().resolve("build/templateTest").toFile().apply {
    mkdirs()
  }
}

fun mockPrefManager(configure: PreferenceManager.() -> Unit = {}) {
  mockkStatic(::prefManager)

  val manager = PreferenceManager(ApplicationProvider.getApplicationContext())
  every { prefManager } answers { manager }
  manager.configure()
}

fun mockTemplateDatas(useKts: Boolean) {
  mockTemplateDataConstructor(BaseTemplateData::class, useKts)
  mockTemplateDataConstructor(ProjectTemplateData::class, useKts)
  mockTemplateDataConstructor(ModuleTemplateData::class, useKts)
}

fun unmockTemplateDatas() {
  unmockkConstructor(BaseTemplateData::class)
  unmockkConstructor(ProjectTemplateData::class)
  unmockkConstructor(ModuleTemplateData::class)
}

private inline fun <reified T : BaseTemplateData> mockTemplateDataConstructor(
  kClass: KClass<T>, useKts: Boolean
) {
  mockConstructors(klass = kClass) {
    every { anyConstructed<T>().useKts } returns useKts
  }
}

private fun <T : Any> mockConstructors(klass: KClass<T>,
                                       configure: () -> Unit = {}
) {
  mockkConstructor(klass)
  configure()
}

fun testTemplate(name: String, generate: Boolean = true,
                 languages: Array<Language> = Language.values(),
                 builder: () -> Template<*>
): Template<*> {
  mockPrefManager()
  Environment.PROJECTS_DIR = testProjectsDir

  val template = builder()

  if (generate) {
    generateTemplateProject(name, languages, template)
  }

  return template
}

private fun generateTemplateProject(name: String, languages: Array<Language>,
                                    template: Template<*>
) {
  for (language in languages) {
    val packageName = "com.itsaky.androidide.template.${language.lang}"
    run {
      val projectName = "${name}GradleProject${language.name}WithoutKts"

      File(testProjectsDir, projectName).apply {
        if (exists()) deleteRecursively()
      }

      // Test with language without Kotlin Script
      mockTemplateDatas(useKts = false)
      template.setupRootProjectParams(name = projectName,
        packageName = packageName, language = language)
      template.executeRecipe()
      unmockTemplateDatas()
    }

    run {
      val projectName = "${name}GradleProject${language.name}WithKts"

      File(testProjectsDir, projectName).apply {
        if (exists()) deleteRecursively()
      }

      // Test with language + Kotlin Script
      mockTemplateDatas(useKts = true)
      template.setupRootProjectParams(name = projectName,
        packageName = "${packageName}.kts", language = language)
      template.executeRecipe()
      unmockTemplateDatas()
    }
  }
}

fun Template<*>.setupRootProjectParams(name: String = "TestTemplate",
                                       packageName: String = "com.itsaky.androidide.template",
                                       language: Language = Language.Kotlin,
                                       minSdk: Sdk = Sdk.Lollipop
) {
  val iterator = parameters.iterator()

  // name
  var param = iterator.next()
  (param as StringParameter).setValue(name)

  // package
  param = iterator.next()
  (param as StringParameter).setValue(packageName)

  // save location
  param = iterator.next()
  (param as StringParameter).setValue(testProjectsDir.absolutePath)

  // language
  param = iterator.next()
  (param as EnumParameter<Language>).setValue(language)

  // Min SDK
  param = iterator.next()
  (param as EnumParameter<Sdk>).setValue(minSdk)
}

fun Template<*>.executeRecipe() {
  recipe.execute(TestRecipeExecutor())
}

fun Collection<Parameter<*>>.assertParameterTypes(
  checker: (Int) -> KClass<out Parameter<*>>
) = assertTypes(checker)

fun Collection<Widget<*>>.assertWidgetTypes(
  checker: (Int) -> KClass<out Widget<*>>
) = assertTypes(checker)


fun <T : Any> Collection<T>.assertTypes(checker: (Int) -> KClass<out T>) {
  forEachIndexed { index, element ->
    assertThat(element).isInstanceOf(checker(index).java)
  }
}