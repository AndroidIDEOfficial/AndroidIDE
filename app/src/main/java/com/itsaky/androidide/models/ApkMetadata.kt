/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.models

import com.google.gson.annotations.SerializedName
import com.itsaky.androidide.utils.JSONUtility.gson
import com.itsaky.androidide.utils.ListingFileRedirect.getListingFile
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader

class ApkMetadata {

  @SerializedName("version")
  var version = 0

  @SerializedName("artifactType")
  var artifactType: ArtifactType? = null

  @SerializedName("applicationId")
  var applicationId: String? = null

  @SerializedName("variantName")
  var variantName: String? = null

  @SerializedName("elements")
  var elements: List<MetadataElement>? = null

  @SerializedName("elementType")
  var elementType: String? = null

  companion object {

    private val log = LoggerFactory.getLogger(ApkMetadata::class.java)

    fun findApkFile(listingFIle: File): File? {
      return try {
        // This sometimes might be a redirect to the listing file
        // So we have to handle this case too.
        val redirectedFile = getListingFile(listingFIle).absoluteFile
        val dir = redirectedFile.parentFile

        FileReader(redirectedFile).use {
          val metadata = gson.fromJson(it, ApkMetadata::class.java)
          if (!isValid(metadata)) {
            log.warn("Invalid APK metadata: {}", metadata)
            return@use null
          }

          for (element in metadata.elements!!) {
            if (element.outputFile == null) {
              log.warn("No output file specified in APK metadata element: {}", element)
              continue
            }

            if (element.outputFile!!.endsWith(".apk")) {
              val apk = element.outputFile?.let { File(dir, it) } ?: continue
              if (apk.exists() && apk.isFile) {
                log.info("Found apk in metadata: {}", apk)
                return@use apk
              }
            }
          }

          return@use null
        }
      } catch (e: FileNotFoundException) {
        log.error("Metadata file not found...", e)
        null
      }
    }

    private fun isValid(metadata: ApkMetadata?): Boolean {
      // Null checks
      if (metadata?.artifactType?.type == null || metadata.elements == null) {
        log.warn("APK metadata null check failed. Metadata: {}", metadata)
        return false
      }

      val type = metadata.artifactType
      val elements = metadata.elements
      if (type!!.type != ArtifactType.TYPE_APK) {
        log.warn("Artifact is not of type APK. Metadata: {}", metadata)
        return false
      }
      if (elements!!.isEmpty()) {
        log.warn("No output elements found for metadata: {}", metadata)
        return false
      }
      var atLeastOneApk = false
      for (element in elements) {
        if (element.outputFile?.endsWith(".apk") == false) {
          log.warn("Skipping output element because file is not APK: {}", element)
          continue
        }
        atLeastOneApk = true
        break
      }
      log.debug("Output metadata validation succeeded")
      return atLeastOneApk
    }
  }

  override fun toString(): String {
    return "ApkMetadata(version=$version, artifactType=$artifactType, applicationId=$applicationId, variantName=$variantName, elements=$elements, elementType=$elementType)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ApkMetadata) return false

    if (version != other.version) return false
    if (artifactType != other.artifactType) return false
    if (applicationId != other.applicationId) return false
    if (variantName != other.variantName) return false
    if (elements != other.elements) return false
    if (elementType != other.elementType) return false

    return true
  }

  override fun hashCode(): Int {
    var result = version
    result = 31 * result + (artifactType?.hashCode() ?: 0)
    result = 31 * result + (applicationId?.hashCode() ?: 0)
    result = 31 * result + (variantName?.hashCode() ?: 0)
    result = 31 * result + (elements?.hashCode() ?: 0)
    result = 31 * result + (elementType?.hashCode() ?: 0)
    return result
  }
}
