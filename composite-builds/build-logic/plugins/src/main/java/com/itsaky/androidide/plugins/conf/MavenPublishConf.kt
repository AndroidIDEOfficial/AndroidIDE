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

package com.itsaky.androidide.plugins.conf

import com.itsaky.androidide.build.config.ProjectConfig
import com.itsaky.androidide.build.config.publishingVersion
import com.vanniktech.maven.publish.AndroidMultiVariantLibrary
import com.vanniktech.maven.publish.GradlePlugin
import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost.Companion.S01
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType
import java.io.File

private val projectsRequiringMavenLocalForTests = arrayOf(":tooling:plugin")
private val mavenLocalRepos = hashMapOf<String, String>()

@Suppress("UnstableApiUsage")
fun Project.configureMavenPublish() {
  assert(plugins.hasPlugin("com.vanniktech.maven.publish.base")) {
    "${javaClass.simpleName} can only be applied to maven publish projects."
  }

  gradle.projectsEvaluated {
    for (path in projectsRequiringMavenLocalForTests) {
      checkNotNull(rootProject.findProject(path)) {
        "Unable to configure maven local for project '$path' (project cannot be found)."
      }
    }
    rootProject.subprojects {
      if (project.path in projectsRequiringMavenLocalForTests) {
        tasks.withType<Test> {
          for ((project, _) in mavenLocalRepos) {
            dependsOn(
              project(project).tasks.getByName("publishAllPublicationsToBuildMavenLocalRepository")
            )
          }
        }
      }
    }
  }

  afterEvaluate {
    if (project.description.isNullOrBlank()) {
      throw GradleException("Project ${project.path} must have a description")
    }
  }

  configure<MavenPublishBaseExtension> {

    project.configureMavenLocal()

    pom {
      name.set(project.name)
      description.set(project.description)
      inceptionYear.set("2021")
      url.set(ProjectConfig.REPO_URL)
      licenses {
        license {
          name.set("The GNU General Public License, v3.0")
          url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
          distribution.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
        }
      }

      developers {
        developer {
          id.set("androidide")
          name.set("AndroidIDE")
          url.set(ProjectConfig.PROJECT_SITE)
        }
      }

      scm {
        url.set(ProjectConfig.REPO_URL)
        connection.set(ProjectConfig.SCM_GIT)
        developerConnection.set(ProjectConfig.SCM_SSH)
      }
    }

    coordinates(project.group.toString(), project.name, project.publishingVersion)
    publishToMavenCentral(host = S01)
    signAllPublications()

    if (plugins.hasPlugin("com.android.library")) {
      configure(AndroidMultiVariantLibrary())
    } else if (plugins.hasPlugin("java-gradle-plugin")) {
      configure(GradlePlugin(javadocJar = JavadocJar.Javadoc()))
    } else if (plugins.hasPlugin("java-library")) {
      configure(JavaLibrary(javadocJar = JavadocJar.Javadoc()))
    }
  }
}

private fun Project.configureMavenLocal() {
  val mavenLocalPath = layout.buildDirectory.dir("maven-local")
  mavenLocalRepos[project.path] = mavenLocalPath.get().asFile.absolutePath

  extensions.findByType(PublishingExtension::class.java)?.run {
    repositories {
      maven {
        name = "buildMavenLocal"
        url = uri(mavenLocalPath)
      }
    }
  }

  tasks.create<Delete>("deleteBuildMavenLocal") {
    delete(mavenLocalPath)
  }

  if (project.path in projectsRequiringMavenLocalForTests) {
    tasks.withType<Test> {
      dependsOn(tasks.getByName("publishAllPublicationsToBuildMavenLocalRepository"))
      doFirst {
        val file = mavenLocalPath.get().file("repos.txt").asFile
        file.writeText(mavenLocalRepos.values.joinToString(separator = File.pathSeparator))
      }
    }
  }

  afterEvaluate {
    tasks.getByName("publishAllPublicationsToBuildMavenLocalRepository") {
      dependsOn(tasks.getByName("deleteBuildMavenLocal"))
    }
  }
}