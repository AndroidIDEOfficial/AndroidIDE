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

package com.itsaky.androidide.lsp.java.indexing.apiinfo

import androidx.collection.mutableIntObjectMapOf
import jaxp.xml.namespace.QName
import jaxp.xml.stream.XMLInputFactory
import jaxp.xml.stream.events.Attribute
import jaxp.xml.stream.events.EndElement
import jaxp.xml.stream.events.StartElement
import org.slf4j.LoggerFactory
import java.io.InputStream

/**
 * Parser for parsing `api-versions.xml` file from the Android SDK and building [ApiInfo] models.
 *
 * @author Akash Yadav
 */
class ApiInfoParser {

  private val apiInfos = mutableIntObjectMapOf<ApiInfo>()
  private val apiInfo = HashMap<String, Pair<ApiInfo, HashMap<String, ApiInfo>>>()

  private var apiVersion: Int? = null
  private var currentClass: String? = null

  companion object {
    private val log = LoggerFactory.getLogger(ApiInfoParser::class.java)
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
  }

  /**
   * Returns [ApiInfo] for the given class.
   */
  fun getClassInfo(className: String): ApiInfo? {
    return apiInfo[className]?.first
  }

  /**
   * Returns [ApiInfo] for the given class and member.
   */
  fun getMemberInfo(className: String, memberName: String): ApiInfo? {
    return apiInfo[className]?.second?.get(memberName)
  }

  /**
   * Removes and returns the [ApiInfo] for the given class and member. This also removes the [ApiInfo]
   * for the class if the all the members of the class have been removed.
   */
  fun removeMemberInfo(className: String, memberName: String): ApiInfo? {
    return apiInfo[className]?.second?.remove(memberName).also {
      if (apiInfo[className]?.second?.isEmpty() == true) {
        apiInfo.remove(className)
      }
    }
  }

  private fun consumeStartElement(event: StartElement) {
    when (event.name.localPart) {
      "api" -> apiVersion = event.getAttributeByName(QName("version")).value.toInt()
      "class" -> consumeClass(event)
      "field" -> consumeMember(event, "field")
      "method" -> consumeMember(event, "method")
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
    check(!apiInfo.containsKey(name)) {
      "Duplicate class entry: $name"
    }

    val apiInfo = apiInfos.getOrPut(versions) {
      createApiInfo(versions)
    }

    this.apiInfo[name] = apiInfo to HashMap()
    this.currentClass = name
  }

  private fun consumeMember(event: StartElement, memberType: String) {
    val (name, versions) = event.parseAttrs()
    val currentClass = checkNotNull(currentClass) {
      "<${memberType}> element must be inside <class> element"
    }
    check(!apiInfo.containsKey(name)) {
      "Duplicate $memberType entry in class $currentClass: $name"
    }

    val (_, members) = apiInfo[currentClass]!!
    val existing = members.put(name, createApiInfo(versions))
    check(existing == null) {
      "Duplicate $memberType entry in class $currentClass: $name"
    }
  }

  private fun consumeEndElement(element: EndElement) {
    when (element.name.localPart) {
      "api" -> apiVersion = null
      "class" -> currentClass = null
    }
  }

  private fun StartElement.parseAttrs(): Pair<String, Int> {
    var name: String? = null
    var since = 1
    var deprecated = 0
    var removed = 0

    attributes.forEach { attribute ->
      attribute as Attribute

      when (attribute.name.localPart) {
        "name" -> name = attribute.value
        "since" -> since = attribute.value.toInt()
        "deprecated" -> deprecated = attribute.value.toInt()
        "removed" -> removed = attribute.value.toInt()
      }
    }

    checkNotNull(name) {
      "Missing name attribute"
    }

    // Android API versions would not exceed 255, right?
    check(since in 1..255 && deprecated in 0..255 && removed in 0..255) {
      "Invalid version: $since, $deprecated, $removed"
    }

    val version = (since shl 16) + (deprecated shl 8) + removed
    return name!! to version
  }

  private fun createApiInfo(versions: Int): ApiInfo {
    val since = (versions shr 16) and 0x000000FF
    val deprecated = (versions shr 8) and 0x000000FF
    val removed = versions and 0x000000FF
    return ApiInfo.newInstance(since = since, deprecatedIn = deprecated, removedIn = removed)
  }
}