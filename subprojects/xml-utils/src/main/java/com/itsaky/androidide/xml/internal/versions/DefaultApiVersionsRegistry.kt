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

import com.google.auto.service.AutoService
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.xml.versions.ApiVersions
import com.itsaky.androidide.xml.versions.ApiVersionsRegistry
import com.itsaky.androidide.xml.versions.ClassInfo
import com.itsaky.androidide.xml.versions.FieldInfo
import com.itsaky.androidide.xml.versions.Info
import com.itsaky.androidide.xml.versions.MethodInfo
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation of [ApiVersionsRegistry].
 *
 * @author Akash Yadav
 */
@AutoService(ApiVersionsRegistry::class)
class DefaultApiVersionsRegistry : ApiVersionsRegistry {

  private val log = ILogger.newInstance(javaClass.simpleName)
  private val versions = ConcurrentHashMap<String, ApiVersions>()

  override var isLoggingEnabled: Boolean
    get() = log.isEnabled
    set(value) {
      log.isEnabled = value
    }

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

    log.info("Creating API versions table for platform dir: $platform")
    return versionsFile.bufferedReader().use {
      val parser =
        XmlPullParserFactory.newInstance().run {
          isNamespaceAware = false
          return@run newPullParser().run {
            setInput(it)
            this
          }
        }
      readApiVersions(parser)
    }
  }

  private fun readApiVersions(parser: XmlPullParser): ApiVersions {
    val versions = DefaultApiVersions()
    var event = parser.eventType
    var apiEncountered = false
    while (event != XmlPullParser.END_DOCUMENT) {
      if (event == XmlPullParser.START_TAG) {
        val tag = parser.name
        if (tag == "api") {
          apiEncountered = true
          event = parser.next()
          continue
        }

        if (!apiEncountered) {
          throw IllegalStateException("<api> tag not found")
        }

        val info = readTag(parser)
        if (info != null && info is ClassInfo) {
          versions.putClass(info.name, info)
        }
      }
      event = parser.next()
    }
    return versions
  }

  private fun readTag(parser: XmlPullParser): Info? {
    return when (parser.name) {
      "class" -> readClassInfo(parser)
      "method" -> readMethodInfo(parser)
      "field" -> readFieldInfo(parser)
      else -> null
    }
  }

  private fun readMethodInfo(parser: XmlPullParser): Info {
    val name = parser.readName()
    return DefaultMethodInfo(
      name = name,
      since = parser.readSince(),
      removed = parser.readRemoved(),
      deprecated = parser.readDeprecated(),
      simpleName = name.substringBefore('(')
    )
  }

  private fun readFieldInfo(parser: XmlPullParser): Info {
    return DefaultFieldInfo(
      name = parser.readName(),
      since = parser.readSince(),
      removed = parser.readRemoved(),
      deprecated = parser.readDeprecated()
    )
  }

  private fun readClassInfo(parser: XmlPullParser): ClassInfo {
    return DefaultClassInfo(
      name = parser.readName(),
      since = parser.readSince(),
      removed = parser.readRemoved(),
      deprecated = parser.readDeprecated()
    )
      .apply {
        val depth = parser.depth
        var event = parser.next()
        while (event != XmlPullParser.END_DOCUMENT) {
          if (event == XmlPullParser.END_TAG && parser.depth == depth) {
            break
          }

          if (event != XmlPullParser.START_TAG) {
            event = parser.next()
            continue
          }

          val info = readTag(parser)
          if (info == null) {
            event = parser.next()
            continue
          }

          when (info) {
            is FieldInfo -> this.fields[info.name] = info
            is MethodInfo -> {
              var methods = this.methods[info.simpleName]
              if (methods == null) {
                methods = mutableListOf()
              }
              methods.add(info)

              this.methods[info.simpleName] = methods
            }
          }

          event = parser.next()
        }
      }
  }

  private fun XmlPullParser.readName(): String {
    return readString("name")
  }

  private fun XmlPullParser.readSince(): Int {
    return readInt("since")
  }

  private fun XmlPullParser.readRemoved(): Int {
    return readInt("removed")
  }

  private fun XmlPullParser.readDeprecated(): Int {
    return readInt("deprecated")
  }

  private fun XmlPullParser.readInt(name: String, default: Int = -1): Int {
    return read(this, name) {
      if (it.isNullOrBlank()) {
        return@read default
      }

      return@read it.toInt()
    }
  }

  private fun XmlPullParser.readString(name: String, default: String = ""): String {
    return read(this, name) {
      if (it.isNullOrBlank()) {
        return@read default
      }

      return@read it
    }
  }

  private fun <T> read(parser: XmlPullParser, name: String, convert: (String?) -> T): T {
    val index = parser.attrIndex(name)
    if (index != -1) {
      return convert(parser.value(index))
    }
    return convert("")
  }

  override fun clear() {
    versions.clear()
  }

  /**
   * Find the index of the attribute with the given name.
   *
   * @param name The name of the attribute to look for.
   * @return The index of the attribute or `-1`.
   */
  private fun XmlPullParser.attrIndex(name: String): Int {
    for (i in 0 until this.attributeCount) {
      if (this.getAttributeName(i) == name) {
        return i
      }
    }
    return -1
  }

  /**
   * Get the value of the attribute at the given index.
   *
   * @param index The index of the attribute.
   * @return The value of the attribute.
   */
  private fun XmlPullParser.value(index: Int): String? {
    return getAttributeValue(index)
  }
}
