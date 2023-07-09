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

import org.gradle.api.Project

/** @author Akash Yadav */
object ProjectConfig {

  const val REPO_HOST = "github.com"
  const val REPO_OWNER = "AndroidIDEOfficial"
  const val REPO_NAME = "AndroidIDE"
  const val REPO_URL = "https://${REPO_HOST}/${REPO_OWNER}/${REPO_NAME}"
  const val SCM_GIT =
    "scm:git:git://${REPO_HOST}/${REPO_OWNER}/${REPO_NAME}.git"
  const val SCM_SSH =
    "scm:git:ssh://git@${REPO_HOST}/${REPO_OWNER}/${REPO_NAME}.git"

  const val PROJECT_SITE = "https://androidide.com"
}

private var simpleVersion: String? = null

val Project.simpleVersionName: String
  get() {
    if (simpleVersion != null) {
      return simpleVersion!!
    }

    val version = rootProject.version.toString()
    val regex = Regex("^v\\d+\\.?\\d+\\.?\\d+-\\w+")

    simpleVersion = regex.find(version)?.value?.substring(1)?.also {
      logger.warn("Simple version name is '$it' (from version $version)")
    }

    if (simpleVersion == null) {
      if (CI.isTestEnv) {
        return "1.0.0-beta"
      }

      throw IllegalStateException(
        "Cannot extract simple version name. Invalid version string '$version'. Version names must be SEMVER with 'v' prefix")
    }

    return simpleVersion!!
  }

private var versionCode: Int? = null
val Project.projectVersionCode: Int
  get() {
    if (versionCode != null) {
      return versionCode!!
    }

    val version = simpleVersionName
    val regex = Regex("^\\d+\\.?\\d+\\.?\\d+")

    versionCode = regex.find(version)?.value?.replace(".", "")?.toInt()?.also {
      logger.warn("Version code is '$it' (from version ${version}).")
    }

    if (versionCode == null) {
      throw IllegalStateException(
        "Cannot extract version code. Invalid version string '$version'. Version names must be SEMVER with 'v' prefix")
    }

    return versionCode!!
  }

private var publishing: String? = null
val Project.publishingVersion: String
  get() {
    if (publishing != null) {
      return publishing!!
    }

    publishing = simpleVersionName
    if (CI.isCiBuild && CI.branchName != "main") {
      publishing += "-${CI.commitHash}-SNAPSHOT"
    }

    return publishing!!
  }

private var download: String? = null

/**
 * The version name which is used to download the artifacts at runtime.
 *
 * The value varies based on the following cases :
 * - For CI builds: same as [publishingVersion].
 * - For local builds: `latest.integration` to make sure that Gradle downloads the latest snapshots.
 */
val Project.downloadVersion: String
  get() {
    if (download != null) {
      return download!!
    }

    download = if (CI.isCiBuild) {
      publishingVersion
    } else {
      "latest.integration"
    }

    return download!!
  }
