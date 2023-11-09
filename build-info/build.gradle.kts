import org.jetbrains.kotlin.incremental.createDirectory

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

plugins {
  id("java-library")
  id("com.vanniktech.maven.publish.base")
}

description = "Information about the AndroidIDE build"

val buildInfoGenDir: Provider<Directory> = project.layout.buildDirectory.dir("generated/buildInfo").also { it.get().asFile.createDirectory() }

sourceSets { getByName("main").java.srcDir(buildInfoGenDir) }

tasks.create("generateBuildInfo") {
  val buildInfoPath = "com/itsaky/androidide/buildinfo/BuildInfo.java"
  val buildInfo = buildInfoGenDir.get().file(buildInfoPath)
  val buildInfoIn = project.file("src/main/java/${buildInfoPath}.in")

  doLast {
    buildInfoIn.replaceContents(
      dest = buildInfo.asFile,
      comment = "//",
      candidates =
        arrayOf(
          "PACKAGE_NAME" to BuildConfig.packageName,
          "MVN_GROUP_ID" to BuildConfig.packageName,
          "VERSION_NAME" to rootProject.version.toString(),
          "VERSION_NAME_SIMPLE" to rootProject.simpleVersionName,
          "VERSION_NAME_PUBLISHING" to rootProject.publishingVersion,
          "VERSION_NAME_DOWNLOAD" to rootProject.downloadVersion,
          "CI_BUILD" to CI.isCiBuild.toString(),
          "CI_GIT_BRANCH" to CI.branchName,
          "CI_COMMIT_HASH" to CI.commitHash,
          "REPO_HOST" to ProjectConfig.REPO_HOST,
          "REPO_OWNER" to ProjectConfig.REPO_OWNER,
          "REPO_NAME" to ProjectConfig.REPO_NAME,
          "PROJECT_SITE" to ProjectConfig.PROJECT_SITE,

          "AGP_VERSION_MININUM" to AGP_VERSION_MINIMUM,
          "AGP_VERSION_LATEST" to libs.versions.agp.tooling.get(),

          "SNAPSHOTS_REPOSITORY" to VersionUtils.SNAPSHOTS_REPO,
        )
    )
  }
}

tasks.withType<JavaCompile> { dependsOn("generateBuildInfo") }
tasks.withType<Jar> { dependsOn("generateBuildInfo") }
