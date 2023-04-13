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

tasks.create("generateBuildInfo") {
  val buildInfo = project.file("src/main/java/com/itsaky/androidide/buildinfo/BuildInfo.java")
  val buildInfoIn = project.file("${buildInfo.path}.in")

  inputs.file(buildInfoIn)
  outputs.file(buildInfo)

  doLast {
    buildInfoIn.replaceContents(
      dest = buildInfo,
      comment = "//",
      candidates = arrayOf(
        "MVN_GROUP_ID" to BuildConfig.packageName,

        "VERSION_NAME" to rootProject.version.toString(),
        "VERSION_NAME_SIMPLE" to rootProject.simpleVersionName,
        "VERSION_NAME_PUBLISHING" to rootProject.publishingVersion,

        "CI_BUILD" to CI.isCiBuild.toString(),
        "CI_GIT_BRANCH" to CI.branchName,
        "CI_COMMIT_HASH" to CI.commitHash,

        "REPO_HOST" to ProjectConfig.REPO_HOST,
        "REPO_OWNER" to ProjectConfig.REPO_OWNER,
        "REPO_NAME" to ProjectConfig.REPO_NAME,

        "PROJECT_SITE" to ProjectConfig.PROJECT_SITE,
      )
    )
  }
}

tasks.withType<JavaCompile> {
  dependsOn("generateBuildInfo")
}