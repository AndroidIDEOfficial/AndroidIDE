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

/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.aaptcompiler

import com.android.aapt.Resources
import com.android.aaptcompiler.AaptResourceType.ATTR
import com.android.aaptcompiler.AaptResourceType.ID
import com.android.aaptcompiler.AaptResourceType.STYLE
import com.android.aaptcompiler.Reference.Type.ATTRIBUTE
import com.android.aaptcompiler.android.ResValue
import com.android.aaptcompiler.android.ResValue.DataType.INT_BOOLEAN
import com.android.aaptcompiler.android.ResValue.DataType.NULL
import com.android.aaptcompiler.android.ResValue.NullFormat
import com.android.aaptcompiler.android.parseHex
import com.android.aaptcompiler.android.stringToFloat
import com.android.aaptcompiler.android.stringToInt

fun tryParseBool(string: String): BinaryPrimitive? {
  val boolean = parseAsBool(string)
  boolean ?: return null

  val data = if (boolean) -1 else 0
  return BinaryPrimitive(ResValue(INT_BOOLEAN, data))
}

fun parseAsBool(string: String): Boolean? =
  when (string.trim()) {
    "true", "True", "TRUE" -> true
    "false", "False", "FALSE" -> false
    else -> null
  }

fun tryParseNullOrEmpty(value: String): Item? {
  val trimmedValue = value.trim()
  return when (trimmedValue) {
    "@null" -> makeNull()
    "@empty" -> makeEmpty()
    else -> null
  }
}

fun makeNull(): Reference {
  return Reference()
}

fun makeEmpty(): BinaryPrimitive {
  return BinaryPrimitive(ResValue(NULL, NullFormat.EMPTY))
}

fun tryParseInt(value: String): BinaryPrimitive? {
  val trimmedValue = value.trim()
  val resValue = stringToInt(trimmedValue)
  return if (resValue != null) BinaryPrimitive(resValue) else null
}

fun parseResourceId(value: String): Int? {
  val resValue = stringToInt(value)
  if (resValue != null &&
    resValue.dataType == ResValue.DataType.INT_HEX &&
    resValue.data.isValidDynamicId()
  ) {
    return resValue.data
  }
  return null
}

fun tryParseFloat(value: String): BinaryPrimitive? {
  val floatResource = stringToFloat(value)
  floatResource ?: return null

  return BinaryPrimitive(floatResource)
}

fun tryParseColor(value: String): BinaryPrimitive? {
  val colorStr = value.trim()
  if (colorStr.isEmpty() || colorStr[0] != '#') {
    return null
  }

  val dataType: ResValue.DataType
  var data = 0
  var error = false

  when (colorStr.length) {
    4 -> {
      dataType = ResValue.DataType.INT_COLOR_RGB4
      for (i in 1..3) {
        val hexValue = parseHex(colorStr.codePointAt(i))
        if (hexValue == -1) {
          error = true
          break
        }
        data = (data shl 8) or (hexValue + (hexValue shl 4))
      }
      data = data or 0xff000000.toInt()
    }

    5 -> {
      dataType = ResValue.DataType.INT_COLOR_ARGB4
      for (i in 1..4) {
        val hexValue = parseHex(colorStr.codePointAt(i))
        if (hexValue == -1) {
          error = true
          break
        }
        data = (data shl 8) or (hexValue + (hexValue shl 4))
      }
    }

    7 -> {
      dataType = ResValue.DataType.INT_COLOR_RGB8
      for (i in 1..6) {
        val hexValue = parseHex(colorStr.codePointAt(i))
        if (hexValue == -1) {
          error = true
          break
        }
        data = (data shl 4) or hexValue
      }
      data = data or 0xff000000.toInt()
    }

    9 -> {
      dataType = ResValue.DataType.INT_COLOR_ARGB8
      for (i in 1..8) {
        val hexValue = parseHex(colorStr.codePointAt(i))
        if (hexValue == -1) {
          error = true
          break
        }
        data = (data shl 4) or hexValue
      }
    }

    else -> return null
  }
  return if (error)
    throw Exception("Unable to parse hex color '$value'.")
  else
    BinaryPrimitive(ResValue(dataType, data))
}

data class ReferenceInfo(val reference: Reference, val createNew: Boolean = false)

fun tryParseReference(value: String): ReferenceInfo? {

  val parsedReference = parseReference(value)
  if (parsedReference != null) {
    return parsedReference
  }

  val parsedAttribute = parseAttributeReference(value)
  if (parsedAttribute != null) {
    return parsedAttribute
  }

  return null
}

/**
 * Parses the attribute of a xml element as a resource (attr) reference.
 *
 * <p> The reference must be of the form "<entry>" or "<package>:<entry>". If the type is specified,
 * it can only be an attribute type. That is, something of the form "attr/<entry>" or
 * "<package>:attr/<entry>". If the value fails to parse {@code null} is returned.
 *
 * @param value: String the name of the xml attribute to be parsed as a attribute reference.
 * @return A [ReferenceInfo] representing the reference, or {@code null} if the input was invalid.
 *
 * [ReferenceInfo.createNew] will be false, as Attributes cannot be created.
 */
fun parseAttributeReference(value: String): ReferenceInfo? {
  val trimmedValue = value.trim()
  if (trimmedValue.isEmpty()) {
    return null
  }

  if (trimmedValue[0] != '?') {
    return null
  }

  val possibleResourceName = extractResourceName(trimmedValue.substring(1))
  if (!possibleResourceName.success) {
    return null
  }

  if (!possibleResourceName.typeName.isEmpty() && possibleResourceName.typeName != "attr") {
    return null
  }

  if (possibleResourceName.entry.isEmpty()) {
    return null
  }

  val reference = Reference()
  reference.name =
    ResourceName(
      possibleResourceName.packageName, ATTR, possibleResourceName.entry
    )
  reference.referenceType = ATTRIBUTE
  return ReferenceInfo(reference, false)
}

/**
 * Attempts to parse the string as a reference. This may be a value for an attribute in a layout, or
 * may be a value for a resource defined in a values file.
 *
 * <p> The reference must be of one of the following forms:
 *
 * + "@<entry>"
 * + "@<type>/<entry>"
 * + "@<package>:<entry>"
 * + "@<package>:<type>/<entry>"
 *
 * <p> Optionally, the '@' symbol can be followed by one of '+' or '*'. '+' means that the resource
 * will be created and added to the resource table. This is only usable with id resources. The '*',
 * however, represents that this resource has private visibility. A resource cannot be both private
 * and created.
 *
 * @param value The string to be parsed.
 * @return A [ReferenceInfo] representing the reference, or {@code null} if the input was invalid.
 *
 * + [ReferenceInfo.createNew] will be set if and only if a '+' followed the '@' and the reference
 * had a type of id.
 * + [ReferenceInfo.reference] will be set as private if and only if a '*' followed the '@' in the
 * input.
 */
fun parseReference(value: String): ReferenceInfo? {
  val trimmedValue = value.trim()
  if (trimmedValue.isEmpty()) {
    return null
  }

  if (trimmedValue[0] == '@') {
    var create = false

    var offset = 1
    if (trimmedValue.length > 1 && trimmedValue[1] == '+') {
      create = true
      offset += 1
    }

    val referenceNameInfo = parseResourceName(trimmedValue.substring(offset))
    referenceNameInfo ?: return null

    if (create && referenceNameInfo.isPrivate) {
      return null
    }

    if (create && referenceNameInfo.resourceName.type != ID) {
      return null
    }

    val reference = Reference()
    reference.name = referenceNameInfo.resourceName
    reference.isPrivate = referenceNameInfo.isPrivate

    return ReferenceInfo(reference, create)
  }
  return null
}

data class ResourceNameInfo(val resourceName: ResourceName, val isPrivate: Boolean)

/**
 * Attempts to parse the string as a Android Resource identifier. I.e. "lib:string/foo",
 * "string/foo", or "*lib:string/foo".
 *
 * <p> The resource name must be in one of the following forms:
 *
 * + "<entry>"
 * + "<type>/<entry>"
 * + "<package>:<entry>"
 * + "<package>:<type>/<entry>"
 *
 * The value may optionally be preceded by a '*' to represent that the resource name is private.
 *
 * @param value the value to be parsed.
 * @return If parsing was successful, this function returns the [ResourceNameInfo] holding both the
 *   valid [ResourceName] and whether the parsed name identifies a private resource. If the value
 *   failed to parsed {@code null} is returned instead.
 */
fun parseResourceName(value: String): ResourceNameInfo? {

  if (value.isEmpty()) {
    return null
  }

  var offset = 0
  var isPrivate = false
  if (value[0] == '*') {
    isPrivate = true
    offset = 1
  }

  val possibleResourceName = extractResourceName(value.substring(offset))
  if (!possibleResourceName.success) {
    return null
  }

  val resourceType = resourceTypeFromTag(possibleResourceName.typeName)
  resourceType ?: return null

  if (possibleResourceName.entry.isEmpty()) {
    return null
  }

  return ResourceNameInfo(
    ResourceName(possibleResourceName.packageName, resourceType, possibleResourceName.entry),
    isPrivate
  )
}

data class PossibleResourceName(
  val packageName: String, val typeName: String, val entry: String, val success: Boolean = true
)


fun extractResourceName(value: String): PossibleResourceName {
  var packageName = ""
  var typeName = ""
  var hasPackageSeparator = false
  var hasTypeSeparator = false

  var offsetCurrent = 0
  if (value.isNotEmpty() && value[0] == '@') {
    ++offsetCurrent
  }

  var currentChar = offsetCurrent
  while (currentChar < value.length) {
    when {
      typeName.isEmpty() && value[currentChar] == '/' -> {
        hasTypeSeparator = true
        typeName = value.substring(offsetCurrent, currentChar)
        offsetCurrent = currentChar + 1
      }

      packageName.isEmpty() && value[currentChar] == ':' -> {
        hasPackageSeparator = true
        packageName = value.substring(offsetCurrent, currentChar)
        offsetCurrent = currentChar + 1
      }
    }
    ++currentChar
  }
  val entryName = value.substring(offsetCurrent)

  val success = !(hasPackageSeparator && packageName.isEmpty()) &&
    !((hasTypeSeparator) && typeName.isEmpty())

  return PossibleResourceName(packageName, typeName, entryName, success)
}

fun tryParseItemForAttribute(
  value: String,
  resourceTypeMask: Int,
  onCreateReference: ((name: ResourceName) -> Boolean)? = null
): Item? {

  val nullOrEmpty = tryParseNullOrEmpty(value)
  if (nullOrEmpty != null) {
    return nullOrEmpty
  }

  val reference = tryParseReference(value)
  if (reference != null) {
    reference.reference.typeFlags = resourceTypeMask
    if (reference.createNew) {
      val result = onCreateReference?.invoke(reference.reference.name)
      if (result != null && !result) {
        return null
      }
    }
    return reference.reference
  }

  if ((resourceTypeMask and Resources.Attribute.FormatFlags.COLOR_VALUE) != 0) {
    val color = tryParseColor(value)
    if (color != null) {
      return color
    }
  }

  if ((resourceTypeMask and Resources.Attribute.FormatFlags.BOOLEAN_VALUE) != 0) {
    val boolean = tryParseBool(value)
    if (boolean != null) {
      return boolean
    }
  }

  if ((resourceTypeMask and Resources.Attribute.FormatFlags.INTEGER_VALUE) != 0) {
    val integer = tryParseInt(value)
    if (integer != null) {
      return integer
    }
  }

  val floatMask = Resources.Attribute.FormatFlags.FLOAT_VALUE or
    Resources.Attribute.FormatFlags.DIMENSION_VALUE or
    Resources.Attribute.FormatFlags.FRACTION_VALUE
  if ((resourceTypeMask and floatMask) != 0) {
    val floatingPoint = tryParseFloat(value)
    if (floatingPoint != null &&
      (androidTypeToAttributeTypeMask(floatingPoint.resValue.dataType) and resourceTypeMask) != 0
    ) {
      return floatingPoint
    }
  }

  return null
}

fun androidTypeToAttributeTypeMask(type: ResValue.DataType) =
  when (type) {
    ResValue.DataType.NULL,
    ResValue.DataType.REFERENCE,
    ResValue.DataType.ATTRIBUTE,
    ResValue.DataType.DYNAMIC_REFERENCE -> Resources.Attribute.FormatFlags.REFERENCE_VALUE

    ResValue.DataType.STRING -> Resources.Attribute.FormatFlags.STRING_VALUE
    ResValue.DataType.FLOAT -> Resources.Attribute.FormatFlags.FLOAT_VALUE
    ResValue.DataType.DIMENSION -> Resources.Attribute.FormatFlags.DIMENSION_VALUE
    ResValue.DataType.FRACTION -> Resources.Attribute.FormatFlags.FRACTION_VALUE
    ResValue.DataType.INT_DEC,
    ResValue.DataType.INT_HEX -> {
      Resources.Attribute.FormatFlags.INTEGER_VALUE or
        Resources.Attribute.FormatFlags.ENUM_VALUE or
        Resources.Attribute.FormatFlags.FLAGS_VALUE
    }

    ResValue.DataType.INT_BOOLEAN -> Resources.Attribute.FormatFlags.BOOLEAN_VALUE
    ResValue.DataType.INT_COLOR_ARGB8,
    ResValue.DataType.INT_COLOR_RGB8,
    ResValue.DataType.INT_COLOR_ARGB4,
    ResValue.DataType.INT_COLOR_RGB4 -> Resources.Attribute.FormatFlags.COLOR_VALUE

    else -> 0
  }

fun verifyJavaStringFormat(string: String): Boolean {
  var argumentCount = 0
  var nonpositionalArgs = false
  var currentIndex = 0
  while (currentIndex < string.length) {
    val codePoint = string.codePointAt(currentIndex)

    if (codePoint == '%'.code && currentIndex + 1 < string.length) {
      ++currentIndex

      if (string.codePointAt(currentIndex) == '%'.code) {
        ++currentIndex
        continue
      }

      ++argumentCount
      val numDigits = consumeDigits(string.substring(currentIndex))
      when {
        numDigits > 0 -> {
          currentIndex += numDigits
          if (currentIndex < string.length && string.codePointAt(currentIndex) != '$'.code) {
            // The digits were a size, but not a positional argument
            nonpositionalArgs = true
          }
        }

        currentIndex < string.length && string.codePointAt(currentIndex) == '<'.code -> {
          // Reusing last argument, bad idea since positions can be moved around during translation
          nonpositionalArgs = true
          ++currentIndex

          // Optionally we can have a $ after
          if (currentIndex < string.length && string.codePointAt(currentIndex) == '$'.code) {
            ++currentIndex
          }
        }

        else -> nonpositionalArgs = true
      }
      // Ignore size, width, flags, etc.
      search@ while (currentIndex < string.length) {
        when (string.codePointAt(currentIndex)) {
          '-'.code,
          '#'.code,
          '+'.code,
          ' '.code,
          ','.code,
          '('.code,
          in '0'.code..'9'.code -> ++currentIndex

          else -> break@search
        }
      }

      /*
       * This is a shortcut to detect strings that are going to Time.format()
       * instead of String.format()
       *
       * Comparison of String.format() and Time.format() args:
       *
       * String: ABC E GH  ST X abcdefgh  nost x
       *   Time:    DEFGHKMS W Za  d   hkm  s w yz
       *
       * Therefore we know it's definitely Time if we have:
       *     DFKMWZkmwyz
       */
      if (currentIndex < string.length) {
        when (string.codePointAt(currentIndex)) {
          'D'.code,
          'F'.code,
          'K'.code,
          'M'.code,
          'W'.code,
          'Z'.code,
          'k'.code,
          'm'.code,
          'w'.code,
          'y'.code,
          'z'.code -> return true
        }
      }
    }

    ++currentIndex
  }

  if (argumentCount > 1 && nonpositionalArgs) {
    // Multiple arguments were specified, but some or all were non positional. Translated strings
    // may rearrange the order of the arguments, which will break the string.
    return false
  }
  return true
}

fun consumeDigits(string: String): Int {
  var currentIndex = 0
  while (currentIndex < string.length && string[currentIndex].isDigit()) {
    ++currentIndex
  }
  return currentIndex
}

data class ParsedParentInfo(val parent: Reference?, val errorString: String) {
  companion object {
    val EMPTY = ParsedParentInfo(null, "")
  }
}

fun parseStyleParentReference(str: String): ParsedParentInfo {
  if (str.isEmpty()) {
    ParsedParentInfo.EMPTY
  }

  var name = str

  var hasLeadingIdentifiers = false
  var privateRef = false

  // Skip over these identifiers. A style's parent is a normal reference.
  val nameStart = name.codePointAt(0)
  if (nameStart == '@'.code || nameStart == '?'.code) {
    hasLeadingIdentifiers = true
    name = name.substring(1)
  }

  if (name.codePointAt(0) == '*'.code) {
    privateRef = true
    name = name.substring(1)
  }

  val possibleResourceName = extractResourceName(name)

  // if we have a type make sure it is a Style
  if (possibleResourceName.typeName.isNotEmpty()) {
    val parsedType = resourceTypeFromTag(possibleResourceName.typeName)
    if (parsedType != AaptResourceType.STYLE) {
      val errorString = "Invalid resource type ${possibleResourceName.typeName} for parent of style"
      return ParsedParentInfo(null, errorString)
    }
  }

  val resourceName =
    ResourceName(
      possibleResourceName.packageName, STYLE, possibleResourceName.entry
    )

  if (!hasLeadingIdentifiers &&
    resourceName.pck!!.isEmpty() &&
    possibleResourceName.typeName.isNotEmpty()
  ) {
    val errorString = "Invalid parent reference '$str'"
    return ParsedParentInfo(null, errorString)
  }

  val result = Reference()
  result.name = resourceName
  result.isPrivate = privateRef


  return ParsedParentInfo(result, "")
}


fun parseXmlAttributeName(str: String): Reference {
  val name = str.trim()

  val result = Reference()

  var startOffset = 0
  if (name.isNotEmpty() && name.codePointAt(0) == '*'.code) {
    ++startOffset
    result.isPrivate = true
  }

  var packageName = ""
  var entryName = ""
  for (i in startOffset..(name.length - 1)) {
    if (name.codePointAt(i) == ':'.code) {
      packageName = name.substring(startOffset, i)
      entryName = name.substring(i + 1)
    }
  }

  result.name =
    ResourceName(packageName, ATTR, if (entryName.isEmpty()) name else entryName)
  return result
}

fun tryParseFlagSymbol(attribute: AttributeResource, value: String): BinaryPrimitive? {
  val flagsType = ResValue.DataType.INT_HEX
  var flagsData = 0

  if (value.trim().isEmpty()) {
    // Empty string is a valid flag (0)
    return BinaryPrimitive(ResValue(flagsType, flagsData))
  }

  for (part in value.split('|')) {
    val trimmedPart = part.trim()

    var flagSet = false
    for (symbol in attribute.symbols) {
      // Flag symbols are stored as @package:id/symbol resources,
      // so we need to match against the 'entry' part of the identifier
      val flagResourceName = symbol.symbol.name
      if (trimmedPart == flagResourceName.entry) {
        flagsData = flagsData or symbol.value
        flagSet = true
        break
      }
    }
    if (!flagSet) {
      return null
    }
  }
  return BinaryPrimitive(ResValue(flagsType, flagsData))
}
