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

package com.android.aaptcompiler.android

import com.android.aaptcompiler.StringPool
import com.android.aaptcompiler.parseFloat
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Definitions of resource data structures.
 * <p>Transliterated from: *
 * https://android.googlesource.com/platform/frameworks/base/+/android-9.0.0_r12/libs/androidfw/ResourceTypes.cpp
 */

/**
 * Header that appears at the front of every data chunk in a resource.
 */
class ResChunkHeader(
  // Type identifier for this chunk. The meaning of this value depends on the containing chunk.
  val typeId: Short,
  // Size of the chunk header (in bytes). Adding this value to the address of the chunk will be
  // the address of the associated data if any.
  val headerSize: Short
) {

  constructor() : this(0, 0)

  // Total size of this chunk (in bytes). This is the chunkSize plus the size of any data
  // associated with this chunk. Adding the this to the address of this chunk will completely skip
  // its contents (including any child chunks).
  var size: Int = 0
}

enum class ChunkType(val id: Short) {
  NULL_TYPE(0x0000),
  STRING_POOL_TYPE(0x0001),
  TABLE_TYPE(0x0002),
  XML_TYPE(0x0003),

  // Marker for the beginning of XML types.
  XML_FIRST_CHUNK_TYPE(0x0100),

  // Child types of XML_TYPE
  XML_START_NAMESPACE_TYPE(0x0100),
  XML_END_NAMESPACE_TYPE(0x0101),
  XML_START_ELEMENT_TYPE(0x0102),
  XML_END_ELEMENT_TYPE(0x0103),
  XML_CDATA_TYPE(0x0104),
  XML_LAST_CHUNK_TYPE(0x017f),

  // This contains a uint32_t array mapping strings in the string
  // pool back to resource identifiers.  It is optional.
  XML_RESOURCE_MAP_TYPE(0x0180),

  // Child types of TABLE_TYPE
  TABLE_PACKAGE_TYPE(0x0200),
  TABLE_TYPE_TYPE(0x0201),
  TABLE_TYPE_SPEC_TYPE(0x0202),
  TABLE_LIBRARY_TYPE(0x0203),
  TABLE_OVERLAYABLE_TYPE(0x0204),
  TABLE_OVERLAYABLE_POLICY_TYPE(0x0205),
}

/**
 * Representation of a value in a resource, supplying type information.
 */
data class ResValue(val dataType: DataType, val data: Int, val size: Short = 0) {

  constructor() : this(DataType.NULL, 0)

  // type of the data value.
  enum class DataType(val byteValue: Byte) {
    // The 'data' is either 0 or 1, specifying this resource is undefined or empty, respectively.
    NULL(0X00),

    // The 'data' holds a ResTable reference, i.e. a reference to another resource.
    REFERENCE(0x01),

    // The 'data' holds an attribute resource identifier.
    ATTRIBUTE(0x02),

    // The 'data' holds an index into the containing resource table's string pool.
    STRING(0x03),

    // The 'data' holds a single-precision floating point number.
    FLOAT(0x04),

    // The 'data' holds a fixed point number encoding a dimension value, e.g. 100in.
    DIMENSION(0x05),

    // The 'data' holds a fixed point number encoding a fraction of a container.
    FRACTION(0x06),

    // The 'data' holds a dynamic ResTable reference, which needs to be resolved before it can be
    // used as a REFERENCE.
    DYNAMIC_REFERENCE(0x07),

    // The 'data' holds an attribute resource identifier, which needs to be resolved before it can
    // be used like a ATTRIBUTE.
    DYNAMIC_ATTRIBUTE(0x08),

    // The 'data' is a raw integer of the form n..n.
    INT_DEC(0x10),

    // The 'data' is a raw integer value of the form 0xn..n.
    INT_HEX(0x11),

    // The 'data' is either 0 or 1, for input 'false' or 'true' respectively.
    INT_BOOLEAN(0x12),

    // The 'data' is a raw integer value of the form #aarrggbb.
    INT_COLOR_ARGB8(0x1c),

    // The 'data' is a raw integer value of the form #rrggbb.
    INT_COLOR_RGB8(0x1d),

    // The 'data' is a raw integer value of the form #argb
    INT_COLOR_ARGB4(0x1e),

    // The 'data is a raw integer value of the form #rgb
    INT_COLOR_RGB4(0x1f);

    companion object {
      val FIRST_INT = INT_DEC
      val FIRST_COLOR_INT = INT_COLOR_ARGB8
      val LAST_COLOR_INT = INT_COLOR_RGB4
      val LAST_INT = LAST_COLOR_INT
    }
  }

  // Structure of complex data values (TYPE_DIMENSION and TYPE_FRACTION)
  object ComplexFormat {
    // Where the unit type information is.  This gives us 16 possible
    // types, as defined below.
    const val UNIT_SHIFT = 0
    const val UNIT_MASK = 0xf

    // TYPE_DIMENSION: Value is raw pixels.
    const val UNIT_PX = 0

    // TYPE_DIMENSION: Value is Device Independent Pixels.
    const val UNIT_DIP = 1

    // TYPE_DIMENSION: Value is a Scaled device independent Pixels.
    const val UNIT_SP = 2

    // TYPE_DIMENSION: Value is in points.
    const val UNIT_PT = 3

    // TYPE_DIMENSION: Value is in inches.
    const val UNIT_IN = 4

    // TYPE_DIMENSION: Value is in millimeters.
    const val UNIT_MM = 5

    // TYPE_FRACTION: A basic fraction of the overall size.
    const val UNIT_FRACTION = 0

    // TYPE_FRACTION: A fraction of the parent size.
    const val UNIT_FRACTION_PARENT = 1

    // Where the radix information is, telling where the decimal place
    // appears in the mantissa.  This give us 4 possible fixed point
    // representations as defined below.
    const val RADIX_SHIFT = 4
    const val RADIX_MASK = 0x3

    // The mantissa is an integral number -- i.e., 0xnnnnnn.0
    const val RADIX_23p0 = 0

    // The mantissa magnitude is 16 bits -- i.e, 0xnnnn.nn
    const val RADIX_16p7 = 1

    // The mantissa magnitude is 8 bits -- i.e, 0xnn.nnnn
    const val RADIX_8p15 = 2

    // The mantissa magnitude is 0 bits -- i.e, 0x0.nnnnnn
    const val RADIX_0p23 = 3

    // Where the actual value is.  This gives us 23 bits of
    // precision.  The top bit is the sign.
    const val MANTISSA_SHIFT = 8
    const val MANTISSA_MASK = 0xffffff
  }

  // Possible Data values for DataType NULL.
  object NullFormat {
    const val UNDEFINED = 0
    const val EMPTY = 1
  }
}


/**
 * Reference to a string in a string pool
 */
data class ResStringPoolRef(
  // Index into the string pool table (unsigned 32 bit offset from the indices immediately after
  // ResStringPoolHeader) at which to find the location of the string data in the pool
  val index: Int
)

/**
 * Definition of a pool of strings.
 *
 * <p> The data of this chunk is an array of 32 bit unsigned integers providing indices into the
 * pool, relative to the start of the string values. This start, {@code stringStart}, are of all the
 * UTF-16 Strings concatenated together; each starts with a short representing the string's length
 * and ends in a terminating 0x0000u character. If a string is > 32767 characters, the size will be
 * stored as two 16 bit values, a high word and a low word, with the high bit set in the first value
 * to indicate this format is being used.
 *
 * <p> If {@code styleCount} is not zero, then immediately following the array of indices into the
 * string table is another array of indices into a style table starting at {@code stylesStart}.
 * Each entry in the style table is an array of {@code ResStringPoolSpan} structures.
 */
data class ResStringPoolHeader(
  val header: ResChunkHeader,
  // Number of strings in this pool (number of 32 bit indices that follow in the data).
  val stringCount: Int,
  // Number of style span arrays in the pool (number of 32 bit indices that follow the string
  // indices).
  val styleCount: Int
) {

  constructor() : this(ResChunkHeader(), 0, 0)

  var flags: Int = 0

  // Index from header of the string data.
  var stringsStart: Int = 0

  // Index from header of the style data.
  var stylesStart: Int = 0

  companion object {
    const val SORTED_FLAG = 1 shl 0
    const val UTF8_FLAG = 1 shl 8

    const val SIZE = 28.toShort()
  }
}

/**
 * This structure defines a span of style information associated with a string in the pool.
 */
data class ResStringPoolSpan(
  // this is the name of the span  -- that is, the name of the XML tag that defined it. The special
  // value END indicates the end of an array of spans.
  val name: ResStringPoolRef,
  // The range of characters in the string that this span applies to.
  val firstChar: Int,
  val lastChar: Int
) {
  companion object {
    const val END = -1
    const val SIZE = 12
  }
}

/** Convenience class for accessing data in a String Pool Flattened Resource */
class ResStringPool private constructor(
  val data: ByteBuffer,
  val header: ResStringPoolHeader,
  val stringPoolSize: Int,
  val strings: List<String>,
  val stylesPoolSize: Int,
  val styles: List<List<ResStringPoolSpan>>
) {

  companion object {
    fun get(buffer: ByteBuffer, length: Int): ResStringPool {
      buffer.order(ByteOrder.nativeOrder())

      if (length < ResStringPoolHeader.SIZE) {
        error("Invalid StringPool: buffer too small to store a string pool.")
      }

      val typeId = buffer.getShort(0).deviceToHost()
      val headerSize = buffer.getShort(2).deviceToHost()
      val resourceSize = buffer.getInt(4).deviceToHost()

      if (typeId != ChunkType.STRING_POOL_TYPE.id ||
        headerSize != ResStringPoolHeader.SIZE ||
        resourceSize < headerSize ||
        resourceSize > length
      ) {
        error("Invalid StringPool: Header has invalid format.")
      }

      // The size has been checked, so we can start reading the ResStringPoolHeader Fields.
      val chunkHeader = ResChunkHeader(typeId, headerSize)
      chunkHeader.size = resourceSize

      val header =
        ResStringPoolHeader(
          chunkHeader, buffer.getInt(8).deviceToHost(), buffer.getInt(12).deviceToHost()
        )
      header.flags = buffer.getInt(16).deviceToHost()
      header.stringsStart = buffer.getInt(20).deviceToHost()
      header.stylesStart = buffer.getInt(24).deviceToHost()

      var stringPoolSize = 0
      val strings = mutableListOf<String>()
      if (header.stringCount != 0) {
        // we need to check overflow and ensure the string indexes can fit.
        if (header.stringCount * 4 < header.stringCount ||
          (header.header.headerSize + (header.stringCount * 4)) > resourceSize
        ) {
          error("Invalid StringPool: Buffer not large enough for string indices.")
        }

        val charSize = if ((header.flags and ResStringPoolHeader.UTF8_FLAG) != 0) 1 else 2

        // There should at least be enough space for the smallest string.
        // (2 bytes length, null terminator)
        if (header.stringsStart + 2 >= resourceSize) {
          error("Invalid StringPool: Buffer not large enough for strings.")
        }

        if (header.styleCount == 0) {
          stringPoolSize = (resourceSize - header.stringsStart) / charSize
        } else {
          // check invariant: styles starts before end of data
          if (header.stylesStart >= resourceSize - 2) {
            error("Invalid StringPool: Style start specified in header too large.")
          }
          // check invariant: styles follow strings
          if (header.stylesStart <= header.stringsStart) {
            error("Invalid StringPool: ")
          }
          stringPoolSize = (header.stylesStart - header.stringsStart) / charSize
        }

        if (stringPoolSize == 0) {
          error("Invalid StringPool: Space for strings in header is too small.")
        }

        var currentStringIndex = headerSize.toInt()
        for (i in 0.until(header.stringCount)) {
          val stringLocation =
            buffer.getInt(currentStringIndex).deviceToHost() + header.stringsStart
          currentStringIndex += 4

          val string = decodeString(buffer, stringLocation, charSize == 1)
          strings.add(string)
        }
      }

      var stylePoolSize = 0
      val styles = mutableListOf<List<ResStringPoolSpan>>()
      if (header.styleCount != 0) {

        var currentStyleIndex = headerSize.toInt() + header.stringCount * 4
        // invariant: integer overflow in calculating styles
        if (currentStyleIndex < headerSize.toInt()) {
          error("Invalid StringPool: Integer overflow encountered while decoding styles.")
        }

        stylePoolSize = (resourceSize - header.stylesStart) / 4

        for (i in 0.until(header.styleCount)) {
          val styleLocation = buffer.getInt(currentStyleIndex).deviceToHost() + header.stylesStart
          currentStyleIndex += 4

          val style = decodeStyle(buffer, styleLocation)
          styles.add(style)
        }
      }
      return ResStringPool(buffer, header, stringPoolSize, strings, stylePoolSize, styles)
    }

    private fun decodeString(buffer: ByteBuffer, location: Int, utf8: Boolean): String {
      var stringPosition = location

      if (utf8) {
        // In UTF8 mode, the length of UTF16 comes first)
        val firstByteUTF16 = buffer.get(stringPosition)
        ++stringPosition

        val utf16Length = when {
          (firstByteUTF16.toInt() and StringPool.TWO_BYTE_UTF8_LENGTH_SIGNIFIER) != 0 -> {
            val secondByte = buffer.get(stringPosition).toInt() and 0xff
            ++stringPosition

            ((firstByteUTF16.toInt() shl 8) + secondByte) and StringPool.UTF8_ENCODE_LENGTH_MAX
          }

          else -> firstByteUTF16.toInt()
        }

        // In UTF8 mode, the length in UTF8 comes next
        val firstByteUTF8 = buffer.get(stringPosition)
        ++stringPosition

        val utf8Length = when {
          (firstByteUTF8.toInt() and StringPool.TWO_BYTE_UTF8_LENGTH_SIGNIFIER) != 0 -> {
            val secondByte = buffer.get(stringPosition).toInt() and 0xff
            ++stringPosition

            ((firstByteUTF8.toInt() shl 8) + secondByte) and StringPool.UTF8_ENCODE_LENGTH_MAX
          }

          else -> firstByteUTF8.toInt()
        }

        // pull the bytes out of the buffer.
        val array = ByteArray(utf8Length)
        for (i in 0.until(utf8Length)) {
          array[i] = buffer.get(stringPosition)
          ++stringPosition
        }

        // ensure the string is null terminated
        if (buffer.get(stringPosition) != 0.toByte()) {
          error("Invalid StringPool: UTF8 string value is not null-terminated.")
        }

        val utf16String = String(array, Charsets.UTF_8)
        // ensure the utf16 size is correct
        if (utf16String.length != utf16Length) {
          error("Invalid StringPool: specified UTF16 does not match actual string length.")
        }

        return utf16String
      } else {
        // first get the utf16 length, as that is the first component regardless of mode.
        val firstShort = buffer.getShort(stringPosition).deviceToHost()
        stringPosition += 2

        val utf16Length = when {
          (firstShort.toInt() and StringPool.TWO_CHAR_UTF16_LENGTH_SIGNIFIER) != 0 -> {
            val secondShort = buffer.getShort(stringPosition).deviceToHost().toInt() and 0xffff
            stringPosition += 2
            ((firstShort.toInt() shl 16) + secondShort) and StringPool.UTF16_ENCODE_LENGTH_MAX
          }

          else -> firstShort.toInt()
        }

        // pull the chars out of the buffer
        val array = CharArray(utf16Length)
        for (i in 0.until(utf16Length)) {
          array[i] = buffer.getChar(stringPosition).deviceToHost()
          stringPosition += 2
        }

        // ensure the string is null terminated
        if (buffer.getChar(stringPosition) != 0.toChar()) {
          error("Invalid StringPool: UTF16 string value is not null-terminated.")
        }

        return String(array)
      }
    }

    private fun decodeStyle(buffer: ByteBuffer, location: Int): List<ResStringPoolSpan> {
      var currentIndex = location

      val result = mutableListOf<ResStringPoolSpan>()
      while (true) {
        val refIndex = buffer.getInt(currentIndex).deviceToHost()
        if (refIndex == ResStringPoolSpan.END) {
          break
        }
        val firstChar = buffer.getInt(currentIndex + 4).deviceToHost()
        val lastChar = buffer.getInt(currentIndex + 8).deviceToHost()
        currentIndex += 12
        result.add(ResStringPoolSpan(ResStringPoolRef(refIndex), firstChar, lastChar))
      }
      return result
    }
  }
}

fun parseHex(codePoint: Int) =
  when (codePoint) {
    in '0'.code..'9'.code -> codePoint - '0'.code
    in 'A'.code..'F'.code -> codePoint - 'A'.code + 10
    in 'a'.code..'f'.code -> codePoint - 'a'.code + 10
    else -> -1
  }

fun stringToInt(string: String): ResValue? {
  val trimmedString = string.trimStart()
  if (trimmedString.isEmpty()) {
    return null
  }

  val codePointCount = trimmedString.codePointCount(0, trimmedString.length)

  var index = 0
  var value = 0L
  var isNegative = false

  if (trimmedString.codePointAt(0) == '-'.code) {
    ++index
    isNegative = true
  }

  if (codePointCount == index
    || trimmedString.codePointAt(index) !in '0'.code..'9'.code
  ) {

    return null
  }

  val isHex: Boolean
  if (codePointCount >= index + 2
    && trimmedString.codePointAt(index) == '0'.code
    && trimmedString.codePointAt(index + 1) == 'x'.code
  ) {
    isHex = true
    index += 2

    if (isNegative) {
      // Hex values are not allowed to be negative.
      return null
    }

    if (index == codePointCount) {
      // Just "0x", not a valid format.
      return null
    }

    while (index < codePointCount) {
      val hexValue = parseHex(trimmedString.codePointAt(index))
      if (hexValue == -1) {
        return null
      }
      ++index

      value = (value shl 4) + hexValue

      if (value > 0xffffffffL) {
        return null
      }
    }
  } else {
    isHex = false

    while (index < codePointCount) {
      val codePoint = trimmedString.codePointAt(index)
      if (codePoint !in '0'.code..'9'.code) {
        return null
      }
      ++index

      val decValue = codePoint - '0'.code
      value = (value * 10) + decValue

      val outOfBounds =
        if (isNegative) -value < Int.MIN_VALUE.toLong() else value > Int.MAX_VALUE.toLong()
      if (outOfBounds) {
        return null
      }
    }
  }

  if (isNegative) {
    value = -value
  }

  return ResValue(
    if (isHex) ResValue.DataType.INT_HEX else ResValue.DataType.INT_DEC,
    value.toInt()
  )
}

private data class UnitEntry(
  val name: String, val dataType: ResValue.DataType, val unitValue: Int, val scale: Float = 1.0f
)

private val unitSuffixes = listOf(
  UnitEntry("px", ResValue.DataType.DIMENSION, ResValue.ComplexFormat.UNIT_PX),
  UnitEntry("dip", ResValue.DataType.DIMENSION, ResValue.ComplexFormat.UNIT_DIP),
  UnitEntry("dp", ResValue.DataType.DIMENSION, ResValue.ComplexFormat.UNIT_DIP),
  UnitEntry("sp", ResValue.DataType.DIMENSION, ResValue.ComplexFormat.UNIT_SP),
  UnitEntry("pt", ResValue.DataType.DIMENSION, ResValue.ComplexFormat.UNIT_PT),
  UnitEntry("in", ResValue.DataType.DIMENSION, ResValue.ComplexFormat.UNIT_IN),
  UnitEntry("mm", ResValue.DataType.DIMENSION, ResValue.ComplexFormat.UNIT_MM),
  UnitEntry("%", ResValue.DataType.FRACTION, ResValue.ComplexFormat.UNIT_FRACTION, 1.0f / 100),
  UnitEntry(
    "%p", ResValue.DataType.FRACTION, ResValue.ComplexFormat.UNIT_FRACTION_PARENT, 1.0f / 100
  )
)

private fun parseUnitType(string: String): UnitEntry? {
  for (entry in unitSuffixes) {
    if (string.endsWith(entry.name)) {
      return entry
    }
  }
  return null
}

fun stringToFloat(string: String): ResValue? {
  val trimmedString = string.trim()

  if (trimmedString.isEmpty() || trimmedString.length > 128) {
    return null
  }

  // need to find the unit type suffix first, so we can trim it and interpret the rest as a float
  val entry = parseUnitType(trimmedString)
  val suffixIndex =
    if (entry != null) trimmedString.lastIndexOf(entry.name) else trimmedString.length

  // no spaces allowed between suffix and floating point value
  if (suffixIndex == 0 || Character.isWhitespace(trimmedString.codePointAt(suffixIndex - 1))) {
    return null
  }

  var stringToParse = trimmedString.substring(0, suffixIndex)
  // We have an issue where the suffix could be considered part of a hex floating point number
  // and would result in a valid dimension value which is normally considered invalid.
  // i.e "0x1d.dp" should be considered: "0x1d.d p" not "0x1d. dp"
  if (parseHex(entry?.name?.codePointAt(0) ?: 0) != -1) {
    // try to parse the float value with the beginning of the suffix considered.
    if (trimmedString.contains("0x", true) &&
      !stringToParse.contains('p')
    ) {
      // ambiguous value string, resulting in a parse failure.
      return null
    }
  }

  // attempt fast floating point parsing first
  var parsedValue = parseFloat(stringToParse)

  // Resort to back up parsing if the fast parsing fails to create a floating point value.
  if (parsedValue == null) {
    // Kotlin does not parse hex floating point numbers that do not have exponent identifier. I.e.
    // "0x23.b" does not parse. "0x23.bp0" will. So we append "p0" where we need it, to enforce
    // consistency with C++ floating point parsing.
    if (stringToParse.contains("0x", true) && !stringToParse.contains('p', true)) {
      stringToParse += "p0"
    }

    parsedValue = stringToParse.toFloatOrNull()
    parsedValue ?: return null
  }

  if (entry != null) {
    // Treat as a Unit.
    parsedValue *= entry.scale
    val negative = parsedValue < 0
    if (negative) {
      parsedValue = -parsedValue
    }

    // Transform the float to (rounded) integer with the lower 23 bits representing the fraction
    // and bits 45 to 23 representing the whole number part.
    val bits = (parsedValue * (1 shl 23) + .5f).toLong()

    val (radix, shift) = when {
      bits and ((1L shl 23) - 1) == 0L ->
        // Always use 23p0 if there is no fraction, as it is easier to read.
        Pair(ResValue.ComplexFormat.RADIX_23p0, 23)

      bits and ((1L shl 23) - 1).inv() == 0L ->
        // Whole number part is zero -- can fit into 0 leading bits of precision.
        Pair(ResValue.ComplexFormat.RADIX_0p23, 0)

      bits and ((1L shl 31) - 1).inv() == 0L ->
        // Magnitude can fit in 8 leading bits of precision.
        Pair(ResValue.ComplexFormat.RADIX_8p15, 8)

      bits and ((1L shl 39) - 1).inv() == 0L ->
        // Magnitude can fit in 16 leading bits of precision.
        Pair(ResValue.ComplexFormat.RADIX_16p7, 16)

      else ->
        // Need whole range, so no fractional part.
        Pair(ResValue.ComplexFormat.RADIX_23p0, 23)
    }

    var mantissa = (bits ushr shift).toInt() and ResValue.ComplexFormat.MANTISSA_MASK
    if (negative) {
      mantissa = (-mantissa) and ResValue.ComplexFormat.MANTISSA_MASK
    }

    val dataValue = (entry.unitValue shl ResValue.ComplexFormat.UNIT_SHIFT) or
      (radix shl ResValue.ComplexFormat.RADIX_SHIFT) or
      (mantissa shl ResValue.ComplexFormat.MANTISSA_SHIFT)

    return ResValue(entry.dataType, dataValue)
  }

  return ResValue(ResValue.DataType.FLOAT, parsedValue.toRawBits())
}
