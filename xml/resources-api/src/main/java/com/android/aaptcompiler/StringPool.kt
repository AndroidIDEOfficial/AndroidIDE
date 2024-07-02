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

import com.android.aaptcompiler.StringPool.Context.Priority.NORMAL
import com.android.aaptcompiler.android.ChunkType
import com.android.aaptcompiler.android.ResChunkHeader
import com.android.aaptcompiler.android.ResStringPoolHeader
import com.android.aaptcompiler.android.ResStringPoolRef
import com.android.aaptcompiler.android.ResStringPoolSpan
import com.android.aaptcompiler.android.hostToDevice
import com.android.aaptcompiler.buffer.BigBuffer
import com.android.utils.ILogger

data class Span(val name: String, var firstChar: Int, var lastChar: Int = firstChar)

data class StyleString(val str : String, val spans : List<Span>)

class StringPool {

  private val strings = mutableListOf<Entry>()
  private val styles = mutableListOf<StyleEntry>()
  private val indexedStrings = mutableMapOf<String, MutableList<Entry>>()

  class Context(
    var priority: Long = NORMAL.priority,
    var config: ConfigDescription = ConfigDescription()
  ) {
    enum class Priority(val priority: Long) {
      HIGH(1),
      NORMAL(0x7fffffffL),
      LOW(0xffffffffL)
    }
  }

  class Ref (internal val entry: Entry) {
    init {
      ++entry.ref
    }

    fun value(): String = entry.value

    // must account for styles which come first.
    fun index(): Int = entry.pool.styles.size + entry.index

    fun context(): Context = entry.context
  }

  class Span(val name: Ref, var firstChar: Int, var lastChar: Int = firstChar) {
    override fun equals(other: Any?): Boolean {
      if (other is Span) {
        return name.value() == other.name.value() &&
          firstChar == other.firstChar &&
          lastChar == other.lastChar
      }
      return false
    }
  }

  class StyleRef(internal val styleEntry: StyleEntry) {

    fun value(): String = styleEntry.value

    // style strings come first
    fun index(): Int = styleEntry.index

    fun spans(): List<Span> = styleEntry.spans
  }

  class Entry(val value: String, val context: Context) {
    var index: Int = 0
      internal set
    var ref: Int = 0
      internal set
    lateinit var pool: StringPool
      internal set
  }

  class StyleEntry(val value: String, val context: Context, val spans: List<Span>) {
    var index: Int = 0
      internal set
    var ref: Int = 0
      internal set
  }

  fun size(): Int = styles.size + strings.size

  fun entryAt(index: Int): Entry {
    return strings[index]
  }

  fun makeRef(str: String, context: Context = Context()) : Ref {
    return makeRefImpl(str, context, true)
  }

  fun makeRef(ref: Ref): Ref {
    if (ref.entry.pool == this) {
      return ref
    }

    return makeRef(ref.value(), ref.context())
  }

  private fun makeRefImpl(str: String, context: Context, isUnique: Boolean) : Ref {
    if (isUnique) {
      val entries = indexedStrings[str]
      if (entries != null) {
        for (entry in entries) {
          if (entry.context.priority == context.priority) {
            return Ref(entry)
          }
        }
      }
    }

    val entry = Entry(str, context)
    entry.index = strings.size
    entry.ref = 0
    entry.pool = this

    strings.add(entry)
    if (!indexedStrings.contains(str)) {
      indexedStrings[str] = mutableListOf()
    }
    indexedStrings[str]!!.add(entry)
    return Ref(entry)
  }

  fun makeRef(styleString: StyleString, context: Context = Context()): StyleRef {
    val spanList = mutableListOf<Span>()

    for (span in styleString.spans) {
      spanList.add(Span(makeRef(span.name), span.firstChar, span.lastChar))
    }

    val entry = StyleEntry(styleString.str, context, spanList)
    entry.index = styles.size
    entry.ref = 0

    styles.add(entry)
    return StyleRef(entry)
  }

  fun makeRef(styleRef: StyleRef): StyleRef {
    val styleEntry =
      StyleEntry(styleRef.styleEntry.value, styleRef.styleEntry.context, styleRef.styleEntry.spans)
    styleEntry.index = styles.size
    styleEntry.ref = 0
    for (span in styleEntry.spans) {
      makeRef(span.name)
    }
    styles.add(styleEntry)
    return StyleRef(styleEntry)
  }

  fun flattenUtf8(out: BigBuffer, logger: ILogger?) = flatten(out, true, logger)

  fun flattenUtf16(out: BigBuffer, logger: ILogger?) = flatten(out, false, logger)

  fun sort(comparator: Comparator<Context>? = null) {
    sortEntries(comparator)
    sortStyles(comparator)
    reAssignIndices()
  }

  private fun flatten(out: BigBuffer, utf8: Boolean, logger: ILogger?) {
    val header = ResStringPoolHeader(
      ResChunkHeader(
        ChunkType.STRING_POOL_TYPE.id.hostToDevice(),
        ResStringPoolHeader.SIZE.hostToDevice()
      ),
      size().hostToDevice(),
      styles.size.hostToDevice())
    if (utf8) {
      header.flags = ResStringPoolHeader.UTF8_FLAG or header.flags
    }
    val headerStart = out.size
    val headerBlock = out.nextBlock(ResStringPoolHeader.SIZE.toInt())

    var currentStringIndex = 0
    val stringIndexBlock =
      if (size() != 0) out.nextBlock(4*size()) else null

    var currentStyleIndex = 0
    val stylesIndexBlock =
      if (styles.size != 0) out.nextBlock(4*styles.size) else null

    val beginStringsIndex = out.size
    header.stringsStart = (out.size - headerStart).hostToDevice()

    if (stringIndexBlock != null) {
      // Styles come first
      for (entry in styles) {
        stringIndexBlock
          .writeInt((out.size - beginStringsIndex).hostToDevice(), currentStringIndex)
        currentStringIndex += 4
        encodeString(entry.value, utf8, out)
      }

      // Strings come next
      for (entry in strings) {
        stringIndexBlock.writeInt((out.size - beginStringsIndex).hostToDevice(), currentStringIndex)
        currentStringIndex += 4
        encodeString(entry.value, utf8, out)
      }

      out.align4()
    }

    if (stylesIndexBlock != null) {
      header.stylesStart = (out.size - headerStart).hostToDevice()

      for (entry in styles) {
        stylesIndexBlock.writeInt((out.size - header.stylesStart).hostToDevice(), currentStyleIndex)
        currentStyleIndex +=4

        if (entry.spans.isNotEmpty()) {
          val spansBlock = out.nextBlock(ResStringPoolSpan.SIZE*entry.spans.size)

          var currentSpanLocation = 0
          for (span in entry.spans) {
            val resSpan = ResStringPoolSpan(
              ResStringPoolRef(
                span.name.index().hostToDevice()),
              span.firstChar.hostToDevice(),
              span.lastChar.hostToDevice())
            encodeSpan(resSpan, spansBlock, currentSpanLocation)
            currentSpanLocation += ResStringPoolSpan.SIZE
          }
        }

        val spanEnd = out.nextBlock(4)
        spanEnd.writeInt(-1, 0)
      }
      // The error checking code in the platform looks for an entire span structure worth of
      // 0xffffffff so fill in the remaining 2 32 bit words.
      val padding = out.nextBlock(8)
      padding.writeInt(-1, 0)
      padding.writeInt(-1, 4)

      out.align4()
    }
    // now we can set the size of the output in the header
    header.header.size = (out.size - headerStart).hostToDevice()
    encodeHeader(header, headerBlock)
  }

  private fun encodeHeader(resHeader: ResStringPoolHeader, out: BigBuffer.BlockRef) {
    out.writeShort(resHeader.header.typeId, 0)
    out.writeShort(resHeader.header.headerSize, 2)
    out.writeInt(resHeader.header.size, 4)

    out.writeInt(resHeader.stringCount, 8)
    out.writeInt(resHeader.styleCount, 12)
    out.writeInt(resHeader.flags, 16)
    out.writeInt(resHeader.stringsStart, 20)
    out.writeInt(resHeader.stylesStart, 24)
  }

  private fun encodeSpan(resSpan: ResStringPoolSpan, out: BigBuffer.BlockRef, location: Int) : Int {
    out.writeInt(resSpan.name.index, location)
    out.writeInt(resSpan.firstChar, location + 4)
    out.writeInt(resSpan.lastChar, location + 8)
    return 12
  }

  private fun encodeString(str: String, utf8: Boolean, out: BigBuffer) {
      if (utf8) {
      val utf16Length = str.length
      val utf8Str = str.toByteArray()
      val utf8Length = utf8Str.size

      // Make sure the lengths to be encoded do not exceed the maximum length that can be encoded
      // using chars
      if (utf8Length > UTF8_ENCODE_LENGTH_MAX ) {
        error("String of size $utf8Length bytes is too large to encode using UTF-8 " +
                "($UTF8_ENCODE_LENGTH_MAX bytes). " +
                "Affected string begins with: '${str.take(STRING_PREFIX_LENGTH_FOR_ERRORS)}'...")
      }

      val blockSize = getLengthUtf8(utf8Length) + getLengthUtf8(utf16Length) + utf8Length + 1
      val stringBlock = out.nextBlock(blockSize)

      var locationToWrite = 0

      // first encode the UTF16 length
      locationToWrite += encodeLengthUtf8(utf16Length, stringBlock, locationToWrite)

      // Now encode the size of the real UTF8 string
      locationToWrite += encodeLengthUtf8(utf8Length, stringBlock, locationToWrite)

      // Now write the string
      for (i in 0.until(utf8Length)) {
        stringBlock.writeByte(utf8Str[i], locationToWrite)
        ++locationToWrite
      }

      // Now write null terminator
      stringBlock.writeByte(0x00, locationToWrite)
    } else {
      val utf16Length = str.length

      // Make sure the length to be encoded does not exceed the maximum possible length that can be
      // encoded
      if (utf16Length > UTF16_ENCODE_LENGTH_MAX) {
        error("String of size ${str.toByteArray().size} is too large to encode using " +
                "UTF-16 ($UTF16_ENCODE_LENGTH_MAX bytes). " +
                "Affected string begins with: '${str.take(STRING_PREFIX_LENGTH_FOR_ERRORS)}'...")
      }

      val blockSize = getLengthUtf16(utf16Length) + utf16Length*2 + 2
      val stringBlock = out.nextBlock(blockSize)

      var locationToWrite = 0

      // first encode the length
      locationToWrite += encodeLengthUtf16(utf16Length, stringBlock, locationToWrite)

      // Now encode the string
      for (i in 0.until(utf16Length)) {
        stringBlock.writeShort(str[i].toShort().hostToDevice(), locationToWrite)
        locationToWrite += 2
      }

      // now write null terminator
      stringBlock.writeShort(0x0000.toShort(), locationToWrite)
    }
  }

  private fun getLengthUtf8(length: Int) =
    if (length <= ONE_BYTE_UTF8_ENCODE_LENGTH_MAX) 1 else 2

  private fun encodeLengthUtf8(length: Int, out: BigBuffer.BlockRef, location: Int): Int {
    return if (length <= ONE_BYTE_UTF8_ENCODE_LENGTH_MAX) {
      out.writeByte(length.toByte(), location)
      1
    } else {
      out.writeByte(((length shr 8) or TWO_BYTE_UTF8_LENGTH_SIGNIFIER).toByte(), location)
      out.writeByte(length.toByte(), location + 1)
      2
    }
  }

  private fun getLengthUtf16(length: Int) =
    if (length <= ONE_CHAR_UTF16_ENCODE_LENGTH_MAX) 2 else 4

  private fun encodeLengthUtf16(length: Int, out: BigBuffer.BlockRef, location: Int): Int {
    return if (length <= ONE_CHAR_UTF16_ENCODE_LENGTH_MAX) {
      out.writeShort(length.toShort().hostToDevice(), location)
      2
    } else {
      out.writeShort(
        ((length shr 16) or TWO_CHAR_UTF16_LENGTH_SIGNIFIER).toShort().hostToDevice(), location)
      out.writeShort(length.toShort().hostToDevice(), location + 2)
      4
    }
  }

  private fun sortEntries(comparator: Comparator<Context>?) {
    val entryComparator = if (comparator!= null) {
      compareBy<Entry, Context>(comparator) {it.context}.thenComparing(ENTRY_ON_VALUE)
    } else {
      ENTRY_ON_VALUE
    }
    strings.sortWith(entryComparator)
  }

  private fun sortStyles(comparator: Comparator<Context>?) {
    val entryComparator = if (comparator!= null) {
      compareBy<StyleEntry, Context>(comparator) {it.context}.thenComparing(STYLE_ON_VALUE)
    } else {
      STYLE_ON_VALUE
    }
    styles.sortWith(entryComparator)
  }

  private fun reAssignIndices() {
    // assign styles
    for (index in 0.until(styles.size)) {
      styles[index].index = index
    }
    // assign strings
    for (index in 0.until(strings.size)) {
      strings[index].index = index
    }
  }

  companion object {
    const val UTF8_ENCODE_LENGTH_MAX = 0x7fff
    const val TWO_BYTE_UTF8_LENGTH_SIGNIFIER = 0x80
    const val ONE_BYTE_UTF8_ENCODE_LENGTH_MAX = 0x7f

    const val UTF16_ENCODE_LENGTH_MAX = 0x7fffffff
    const val TWO_CHAR_UTF16_LENGTH_SIGNIFIER = 0x8000
    const val ONE_CHAR_UTF16_ENCODE_LENGTH_MAX = 0x7fff

    // The maximum number of chars that will be presented to the user if the size of the string
    // exceeds the maximum permitted utf-8 or utf-16 byte size.
    const val STRING_PREFIX_LENGTH_FOR_ERRORS = 42

    val ENTRY_ON_VALUE = compareBy<Entry> {it.value}
    val STYLE_ON_VALUE = compareBy<StyleEntry> {it.value}
  }
}

