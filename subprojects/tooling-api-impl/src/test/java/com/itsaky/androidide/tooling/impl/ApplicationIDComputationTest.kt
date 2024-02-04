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
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.models.params.StringParameter
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author Akash Yadav
 */
@RunWith(JUnit4::class)
class ApplicationIDComputationTest {

  @Test
  fun `test variant-specific application ID computation for default applicationId`() {
    // no custom applicationIdSuffix or applicationId is set
    ToolingApiTestLauncher.launchServer {

      assertThat(result).isNotNull()
      assertThat(result?.isSuccessful).isTrue()
      assertThat(result?.failure).isNull()

      assertThat(project).isNotNull()

      val selectionResult = project.selectProject(StringParameter(":app")).get()
      assertThat(selectionResult).isNotNull()
      assertThat(selectionResult.isSuccessful).isTrue()

      val app = project.asAndroidProject()
      assertThat(app).isNotNull()
      assertThat(app).isInstanceOf(IAndroidProject::class.java)

      val variants = app.getVariants().get()
      assertThat(variants).isNotNull()
      assertThat(variants).isNotEmpty()


      assertThat(variants).hasSize(2)
      assertThat(variants.map { it.name }).containsExactly("debug", "release")

      variants.forEach { variant ->
        val applicationId = variant.mainArtifact.applicationId
        assertThat(applicationId).isNotNull()
        assertThat(applicationId?.trim()).isNotEmpty()
        assertThat(applicationId).isEqualTo("com.itsaky.test.app")
      }
    }
  }

  @Test
  fun `test variant-specific application ID computation for product flavors with applicationIdSuffix`() {
    // only the flavors have applicationIdSuffix set, not the build types
    val androidBlockConfig = """
        flavorDimensions.add("default")
        
        productFlavors {
          create("full") {
            dimension = "default"
            applicationIdSuffix = ".full"
          }
          create("demo") {
            dimension = "default"
            applicationIdSuffix = ".demo"
          }
        }
      """.trimIndent()

    val client = ToolingApiTestLauncher.MultiVersionTestClient(
      androidBlockConfig = androidBlockConfig)
    ToolingApiTestLauncher.launchServer(client = client) {

      assertThat(result).isNotNull()
      assertThat(result?.isSuccessful).isTrue()
      assertThat(result?.failure).isNull()

      assertThat(project).isNotNull()

      val selectionResult = project.selectProject(StringParameter(":app")).get()
      assertThat(selectionResult).isNotNull()
      assertThat(selectionResult.isSuccessful).isTrue()

      val app = project.asAndroidProject()
      assertThat(app).isNotNull()
      assertThat(app).isInstanceOf(IAndroidProject::class.java)

      val variants = app.getVariants().get()
      assertThat(variants).isNotNull()
      assertThat(variants).isNotEmpty()


      assertThat(variants).hasSize(4)
      assertThat(variants.map { it.name }).containsExactly("fullDebug", "fullRelease", "demoDebug",
        "demoRelease")

      variants.forEach { variant ->
        val applicationId = variant.mainArtifact.applicationId
        assertThat(applicationId).isNotNull()
        assertThat(applicationId?.trim()).isNotEmpty()
        assertThat(applicationId).isAnyOf("com.itsaky.test.app.full", "com.itsaky.test.app.demo")
      }
    }
  }

  @Test
  fun `test variant-specific application ID computation for product flavors with applicationIdSuffix and multiple build types`() {
    // only the flavors have applicationIdSuffix set, but additional build types have been created
    val androidBlockConfig = """
        flavorDimensions.add("default")
        
        productFlavors {
          create("full") {
            dimension = "default"
            applicationIdSuffix = ".full"
          }
          create("demo") {
            dimension = "default"
            applicationIdSuffix = ".demo"
          }
        }
        
        buildTypes {
          create("staging") {}
          create("experimental") {}
        }
      """.trimIndent()

    val client = ToolingApiTestLauncher.MultiVersionTestClient(
      androidBlockConfig = androidBlockConfig)
    ToolingApiTestLauncher.launchServer(client = client) {

      assertThat(result).isNotNull()
      assertThat(result?.isSuccessful).isTrue()
      assertThat(result?.failure).isNull()

      assertThat(project).isNotNull()

      val selectionResult = project.selectProject(StringParameter(":app")).get()
      assertThat(selectionResult).isNotNull()
      assertThat(selectionResult.isSuccessful).isTrue()

      val app = project.asAndroidProject()
      assertThat(app).isNotNull()
      assertThat(app).isInstanceOf(IAndroidProject::class.java)

      val variants = app.getVariants().get()
      assertThat(variants).isNotNull()
      assertThat(variants).isNotEmpty()


      assertThat(variants).hasSize(8)
      assertThat(variants.map { it.name }).containsExactly("fullDebug", "fullRelease",
        "fullStaging",
        "fullExperimental", "demoDebug", "demoRelease", "demoStaging", "demoExperimental")

      variants.forEach { variant ->
        val applicationId = variant.mainArtifact.applicationId
        assertThat(applicationId).isNotNull()
        assertThat(applicationId?.trim()).isNotEmpty()

        // the build types don't have any applicationIdSuffix set
        // so only suffix from the flavors should be applied here
        assertThat(applicationId).isAnyOf("com.itsaky.test.app.full", "com.itsaky.test.app.demo")
      }
    }
  }

  @Test
  fun `test variant-specific application ID computation for build types with applicationIdSuffix`() {
    // only the build types have applicationIdSuffix set, not the product flavors
    val androidBlockConfig = """
        buildTypes {
          create("staging") {
            applicationIdSuffix = ".staging"
          }
          create("experimental") {
            applicationIdSuffix = ".experimental"
          }
        }
      """.trimIndent()

    val client = ToolingApiTestLauncher.MultiVersionTestClient(
      androidBlockConfig = androidBlockConfig)
    ToolingApiTestLauncher.launchServer(client = client) {

      assertThat(result).isNotNull()
      assertThat(result?.isSuccessful).isTrue()
      assertThat(result?.failure).isNull()

      assertThat(project).isNotNull()

      val selectionResult = project.selectProject(StringParameter(":app")).get()
      assertThat(selectionResult).isNotNull()
      assertThat(selectionResult.isSuccessful).isTrue()

      val app = project.asAndroidProject()
      assertThat(app).isNotNull()
      assertThat(app).isInstanceOf(IAndroidProject::class.java)

      val variants = app.getVariants().get()
      assertThat(variants).isNotNull()
      assertThat(variants).isNotEmpty()


      assertThat(variants).hasSize(4)
      assertThat(variants.map { it.name }).containsExactly("debug", "release", "staging",
        "experimental")

      variants.forEach { variant ->
        val applicationId = variant.mainArtifact.applicationId
        assertThat(applicationId).isNotNull()
        assertThat(applicationId?.trim()).isNotEmpty()
        assertThat(applicationId).isAnyOf("com.itsaky.test.app", "com.itsaky.test.app.staging",
          "com.itsaky.test.app.experimental")
      }
    }
  }

  @Test
  fun `test variant-specific application ID computation for build types with applicationIdSuffix and multiple product flavors`() {
    // only the flavors have applicationIdSuffix set, but additional build types have been created
    val androidBlockConfig = """
        flavorDimensions.add("default")
        
        productFlavors {
          create("full") {
            dimension = "default"
          }
          create("demo") {
            dimension = "default"
          }
        }
        
        buildTypes {
          create("staging") {
            applicationIdSuffix = ".staging"
          }
          create("experimental") {
            applicationIdSuffix = ".experimental"
          }
        }
      """.trimIndent()

    val client = ToolingApiTestLauncher.MultiVersionTestClient(
      androidBlockConfig = androidBlockConfig)
    ToolingApiTestLauncher.launchServer(client = client) {

      assertThat(result).isNotNull()
      assertThat(result?.isSuccessful).isTrue()
      assertThat(result?.failure).isNull()

      assertThat(project).isNotNull()

      val selectionResult = project.selectProject(StringParameter(":app")).get()
      assertThat(selectionResult).isNotNull()
      assertThat(selectionResult.isSuccessful).isTrue()

      val app = project.asAndroidProject()
      assertThat(app).isNotNull()
      assertThat(app).isInstanceOf(IAndroidProject::class.java)

      val variants = app.getVariants().get()
      assertThat(variants).isNotNull()
      assertThat(variants).isNotEmpty()


      assertThat(variants).hasSize(8)
      assertThat(variants.map { it.name }).containsExactly("fullDebug", "fullRelease",
        "fullStaging",
        "fullExperimental", "demoDebug", "demoRelease", "demoStaging", "demoExperimental")

      variants.forEach { variant ->
        val applicationId = variant.mainArtifact.applicationId
        assertThat(applicationId).isNotNull()
        assertThat(applicationId?.trim()).isNotEmpty()

        // the flavors don't have any applicationIdSuffix set
        // so only suffix from the build types should be applied here
        assertThat(applicationId).isAnyOf("com.itsaky.test.app", "com.itsaky.test.app.staging",
          "com.itsaky.test.app.experimental")
      }
    }
  }

  @Test
  fun `test variant-specific application ID computation for build types and product flavors with applicationIdSuffix`() {
    // only the flavors have applicationIdSuffix set, but additional build types have been created
    val androidBlockConfig = """
        flavorDimensions.add("default")
        
        productFlavors {
          create("full") {
            dimension = "default"
            applicationIdSuffix = ".full"
          }
          create("demo") {
            dimension = "default"
            applicationIdSuffix = ".demo"
          }
        }
        
        buildTypes {
          create("staging") {
            applicationIdSuffix = ".staging"
          }
          create("experimental") {
            applicationIdSuffix = ".experimental"
          }
        }
      """.trimIndent()

    val client = ToolingApiTestLauncher.MultiVersionTestClient(
      androidBlockConfig = androidBlockConfig)
    ToolingApiTestLauncher.launchServer(client = client) {

      assertThat(result).isNotNull()
      assertThat(result?.isSuccessful).isTrue()
      assertThat(result?.failure).isNull()

      assertThat(project).isNotNull()

      val selectionResult = project.selectProject(StringParameter(":app")).get()
      assertThat(selectionResult).isNotNull()
      assertThat(selectionResult.isSuccessful).isTrue()

      val app = project.asAndroidProject()
      assertThat(app).isNotNull()
      assertThat(app).isInstanceOf(IAndroidProject::class.java)

      val variants = app.getVariants().get()
      assertThat(variants).isNotNull()
      assertThat(variants).isNotEmpty()


      assertThat(variants).hasSize(8)
      assertThat(variants.map { it.name }).containsExactly("fullDebug", "fullRelease",
        "fullStaging",
        "fullExperimental", "demoDebug", "demoRelease", "demoStaging", "demoExperimental")

      variants.forEach { variant ->
        val applicationId = variant.mainArtifact.applicationId
        assertThat(applicationId).isNotNull()
        assertThat(applicationId?.trim()).isNotEmpty()

        // the flavors don't have any applicationIdSuffix set
        // so only suffix from the build types should be applied here
        assertThat(applicationId).isAnyOf(
          // 'debug' and 'release' build types
          "com.itsaky.test.app.full", "com.itsaky.test.app.demo",

          // custom build types with applicationIdSuffix
          "com.itsaky.test.app.full.staging", "com.itsaky.test.app.demo.staging",
          "com.itsaky.test.app.full.experimental", "com.itsaky.test.app.demo.experimental")
      }
    }
  }

  @Test
  fun `test variant-specific application ID computation for product flavors with custom applicationId`() {
    // only the flavors have applicationIdSuffix set, not the build types
    val androidBlockConfig = """
        flavorDimensions.add("default")
        
        productFlavors {
          create("full") {
            dimension = "default"
            applicationId = "com.androidide.app.full"
          }
          create("demo") {
            dimension = "default"
            applicationId = "com.androidide.app.demo"
          }
        }
      """.trimIndent()

    val client = ToolingApiTestLauncher.MultiVersionTestClient(
      androidBlockConfig = androidBlockConfig)
    ToolingApiTestLauncher.launchServer(client = client) {

      assertThat(result).isNotNull()
      assertThat(result?.isSuccessful).isTrue()
      assertThat(result?.failure).isNull()

      assertThat(project).isNotNull()

      val selectionResult = project.selectProject(StringParameter(":app")).get()
      assertThat(selectionResult).isNotNull()
      assertThat(selectionResult.isSuccessful).isTrue()

      val app = project.asAndroidProject()
      assertThat(app).isNotNull()
      assertThat(app).isInstanceOf(IAndroidProject::class.java)

      val variants = app.getVariants().get()
      assertThat(variants).isNotNull()
      assertThat(variants).isNotEmpty()


      assertThat(variants).hasSize(4)
      assertThat(variants.map { it.name }).containsExactly("fullDebug", "fullRelease", "demoDebug",
        "demoRelease")

      variants.forEach { variant ->
        val applicationId = variant.mainArtifact.applicationId
        assertThat(applicationId).isNotNull()
        assertThat(applicationId?.trim()).isNotEmpty()
        assertThat(applicationId).isAnyOf("com.androidide.app.full", "com.androidide.app.demo")
      }
    }
  }

  @Test
  fun `test variant-specific application ID computation for product flavors with custom applicationId and applicationIdSuffix`() {
    // only the flavors have applicationIdSuffix set, not the build types
    val androidBlockConfig = """
        flavorDimensions.add("default")
        
        productFlavors {
          create("full") {
            dimension = "default"
            applicationId = "com.androidide.app.full"
          }
          create("demo") {
            dimension = "default"
            applicationIdSuffix = ".demo"
          }
        }
      """.trimIndent()

    val client = ToolingApiTestLauncher.MultiVersionTestClient(
      androidBlockConfig = androidBlockConfig)
    ToolingApiTestLauncher.launchServer(client = client) {

      assertThat(result).isNotNull()
      assertThat(result?.isSuccessful).isTrue()
      assertThat(result?.failure).isNull()

      assertThat(project).isNotNull()

      val selectionResult = project.selectProject(StringParameter(":app")).get()
      assertThat(selectionResult).isNotNull()
      assertThat(selectionResult.isSuccessful).isTrue()

      val app = project.asAndroidProject()
      assertThat(app).isNotNull()
      assertThat(app).isInstanceOf(IAndroidProject::class.java)

      val variants = app.getVariants().get()
      assertThat(variants).isNotNull()
      assertThat(variants).isNotEmpty()


      assertThat(variants).hasSize(4)
      assertThat(variants.map { it.name }).containsExactly("fullDebug", "fullRelease", "demoDebug",
        "demoRelease")

      variants.forEach { variant ->
        val applicationId = variant.mainArtifact.applicationId
        assertThat(applicationId).isNotNull()
        assertThat(applicationId?.trim()).isNotEmpty()
        assertThat(applicationId).isAnyOf("com.androidide.app.full", "com.itsaky.test.app.demo")
      }
    }
  }
}