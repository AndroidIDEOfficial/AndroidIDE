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

package com.itsaky.androidide.lsp.java.indexing

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lsp.java.indexing.models.IJavaType
import com.itsaky.androidide.utils.FileProvider
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.File

/**
 * @author Akash Yadav
 */
class AndroidJarModelingRule : TestRule {

  private val androidJar: File by lazy {
    FileProvider.projectRoot()
      .resolve("subprojects")
      .resolve("framework-stubs")
      .resolve("libs")
      .resolve("android.jar")
      .toFile()
  }

  private val androidJarIndexer: JavaIndexModelBuilder by lazy {
    assertThat(androidJar.exists()).isTrue()
    JavaIndexModelBuilder(androidJar)
  }

  val types: List<IJavaType<*, *>> by lazy {
    androidJarIndexer.buildTypes()
  }

  override fun apply(base: Statement?, description: Description?): Statement {

    // call 'types' to build the type models
    types

    base?.evaluate()

    return object : Statement() {
      override fun evaluate() {}
    }
  }
}