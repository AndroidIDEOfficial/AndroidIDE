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

import androidx.collection.mutableIntObjectMapOf
import com.itsaky.androidide.xml.versions.ApiVersion
import jaxp.xml.namespace.QName
import jaxp.xml.stream.XMLInputFactory
import jaxp.xml.stream.events.Attribute
import jaxp.xml.stream.events.EndElement
import jaxp.xml.stream.events.StartElement
import org.slf4j.LoggerFactory
import java.io.InputStream

/**
 * Parser for parsing `api-versions.xml` file from the Android SDK and building [ApiVersion] models.
 *
 * @author Akash Yadav
 */
open class ApiVersionsParser {

  // A cache to store the ApiVersion instances with different values
  // For example, if two classes have the same value for `since`,
  // 'removed' and 'deprecated', then the same ApiVersion instance
  // will be used for the two classes. To prevent value corruption, ApiVersion instances are immutable.
  // The keys in this map are the packed (bitwise ORed) values of the three version integers.
  // The first 8 bits are 0, the next 8 bits represent 'since', the next 8 bits represent 'deprecated'
  // and the last 8 bits represent 'removed'.
  private val apiInfosCache = mutableIntObjectMapOf<ApiVersion>()

  private var apiVersion: Int? = null
  private var currentClass: String? = null

  companion object {
    internal const val TAG_API = "api"
    internal const val TAG_CLASS = "class"
    internal const val TAG_FIELD = "field"
    internal const val TAG_METHOD = "method"
    internal const val ATTR_NAME = "name"
    internal const val ATTR_DEPR = "deprecated"
    internal const val ATTR_REM = "removed"
    internal const val ATTR_SIN = "since"
    private val log = LoggerFactory.getLogger(ApiVersionsParser::class.java)
  }

  fun parse(apiVersionsXml: InputStream) {
    if (apiVersionsXml.available() <= 0) {
      log.warn("api-versions.xml InputStream is empty")
      return
    }

    val inputFactory = XMLInputFactory.newInstance()
    val reader = inputFactory.createXMLEventReader(apiVersionsXml)

    while (reader.hasNext()) {
      val event = reader.nextEvent()
      if (event.isStartElement) {
        consumeStartElement(event.asStartElement())
        continue
      }

      if (event.isEndElement) {
        consumeEndElement(event.asEndElement())
      }
    }

    onFinishParse()
  }

  /**
   * Called when the parser is done parsing the `api-versions.xml` file.
   */
  protected open fun onFinishParse() {
  }

  private fun consumeStartElement(event: StartElement) {
    when (event.name.localPart) {
      TAG_API -> apiVersion = event.getAttributeByName(QName("version")).value.toInt()
      TAG_CLASS -> consumeClass(event)
      TAG_FIELD -> consumeMember(event, TAG_FIELD)
      TAG_METHOD -> consumeMember(event, TAG_METHOD)
    }
  }

  private fun consumeClass(event: StartElement) {
    checkNotNull(apiVersion) {
      "<class> element must be inside <api> element"
    }

    check(currentClass == null) {
      "<class> elements cannot be nested"
    }

    val (name, versions) = event.parseAttrs()
    check(!isDuplicateClass(name)) {
      "Duplicate class entry: $name"
    }

    val apiInfo = apiInfosCache.getOrPut(versions) {
      createApiInfo(versions)
    }

    consumeClassVersionInfo(name, apiInfo)
    this.currentClass = name
  }

  /**
   * Check if the given class name is a duplicate.
   */
  protected open fun isDuplicateClass(name: String): Boolean {
    return false
  }

  /**
   * Consume [ApiVersion] info of a class.
   *
   * @param name The fully qualified name of the class in its internal form.
   * @param apiVersion The [ApiVersion] info of the class.
   */
  protected open fun consumeClassVersionInfo(name: String, apiVersion: ApiVersion) {}

  private fun consumeMember(event: StartElement, memberType: String) {
    val (name, versions) = event.parseAttrs()
    val currentClass = checkNotNull(currentClass) {
      "<${memberType}> element must be inside <class> element"
    }
    check(!isDuplicateMember(currentClass, name)) {
      "Duplicate $memberType entry in class $currentClass: $name"
    }
    consumeMemberVersionInfo(currentClass, name, memberType, createApiInfo(versions))
  }

  /**
   * Check if the given member of the given class is a duplicate.
   */
  protected open fun isDuplicateMember(className: String, memberName: String): Boolean {
    return false
  }

  /**
   * Consume [ApiVersion] info for a member of a class.
   *
   * @param className The name of the class which declares this member.
   * @param member The identifier of the member. This is the name of the field and the method signature for methods.
   * @param memberType The type of member. This may be "field" or "method".
   * @param apiVersion The [ApiVersion] info.
   */
  protected open fun consumeMemberVersionInfo(
    className: String,
    member: String,
    memberType: String,
    apiVersion: ApiVersion
  ) {
  }

  private fun consumeEndElement(element: EndElement) {
    when (element.name.localPart) {
      TAG_API -> apiVersion = null
      TAG_CLASS -> currentClass = null
    }
  }

  private fun StartElement.parseAttrs(): Pair<String, Int> {
    var name: String? = null
    var since = 1
    var deprecated = ApiVersion.NONE
    var removed = ApiVersion.NONE

    attributes.forEach { attribute ->
      attribute as Attribute

      when (attribute.name.localPart) {
        ATTR_NAME -> name = attribute.value
        ATTR_SIN -> since = attribute.value.toInt()
        ATTR_DEPR -> deprecated = attribute.value.toInt()
        ATTR_REM -> removed = attribute.value.toInt()
      }
    }

    checkNotNull(name) {
      "Missing name attribute"
    }

    // Android API versions would not exceed 255, right?
    check(since in 1..255 && deprecated in 0..255 && removed in 0..255) {
      "Invalid version: $since, $deprecated, $removed"
    }

    val version = (since shl 16) or (deprecated shl 8) or removed
    return name!! to version
  }

  private fun createApiInfo(versions: Int): ApiVersion {
    val since = (versions shr 16) and 0x000000FF
    val deprecated = (versions shr 8) and 0x000000FF
    val removed = versions and 0x000000FF
    return ApiVersion(since = since, deprecatedIn = deprecated, removedIn = removed)
  }
}