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

package com.itsaky.androidide.xml.internal.versions

import androidx.annotation.VisibleForTesting
import com.google.auto.service.AutoService
import com.itsaky.androidide.xml.versions.ApiVersion
import com.itsaky.androidide.xml.versions.ApiVersions
import com.itsaky.androidide.xml.versions.ApiVersionsRegistry
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation of [ApiVersionsRegistry].
 *
 * @author Akash Yadav
 */
@AutoService(ApiVersionsRegistry::class)
@VisibleForTesting
class DefaultApiVersionsRegistry : ApiVersionsRegistry {

  private val versions = ConcurrentHashMap<String, ApiVersions>()

  companion object {
    private val log = LoggerFactory.getLogger(DefaultApiVersionsRegistry::class.java)
  }

  override var isLoggingEnabled: Boolean = true

  override fun forPlatformDir(platform: File): ApiVersions? {
    var version = versions[platform.path]
    if (version != null) {
      return version
    }

    version = readApiVersions(platform) ?: return null
    versions[platform.path] = version
    return version
  }

  private fun readApiVersions(platform: File): ApiVersions? {
    val versionsFile = File(platform, "data/api-versions.xml")
    if (!versionsFile.exists() || !versionsFile.isFile) {
      return null
    }

    if (isLoggingEnabled) {
      log.info("Creating API versions table for platform dir: $platform")
    }

    return versionsFile.inputStream().buffered().use { inputStream ->
      // we do not implement the parsing logic in the registry itself for thread safety
      val versions = DefaultApiVersions()
      val parser = ApiVersionsParserInternal(versions)
      parser.parse(inputStream)
      versions
    }
  }

  override fun clear() {
    versions.clear()
  }

  private class ApiVersionsParserInternal(
    private val currentApiVersions: DefaultApiVersions
  ) : ApiVersionsParser() {
    override fun isDuplicateClass(name: String): Boolean {
      return currentApiVersions.containsClass(name)
    }

    override fun isDuplicateMember(className: String, memberName: String): Boolean {
      return currentApiVersions.containsClassMember(className, memberName)
    }

    override fun consumeClassVersionInfo(name: String, apiVersion: ApiVersion) {
      if (apiVersion.isSinceInception()) {
        return
      }
      currentApiVersions.putClass(name, apiVersion)
    }

    override fun consumeMemberVersionInfo(
      className: String,
      member: String,
      memberType: String,
      apiVersion: ApiVersion
    ) {
      if (apiVersion.isSinceInception()) {
        return
      }

      var identifier = member
      if (memberType == TAG_METHOD) {
        // strip return type to save some memory
        identifier = member.substring(0, member.lastIndexOf(')') + 1)
      }

      currentApiVersions.putMember(className, identifier, apiVersion)
    }
  }
}
