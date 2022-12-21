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
import com.android.aaptcompiler.proto.serializeCompiledFileToPb
import com.google.protobuf.CodedOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Represents and handles the output of resource compilation.
 *
 * [Container] represents a collection of the output from a single file being compiled by the
 * ResourceCompiler. These can contain multiple entries, such as multiple flattened XMLs from
 * aapt:attr outlining. The Container should only be used once the total number of required entries
 * are known.
 */
class Container(val output: OutputStream, val totalEntryCount: Int) {
  private var currentEntryCount = 0

  enum class EntryType(val value: Int) {
    RES_TABLE(0x00),
    RES_FILE(0x01),
  }

  init {
    val codedOut = CodedOutputStream.newInstance(output)

    codedOut.writeFixed32NoTag(FORMAT_MAGIC)
    codedOut.writeFixed32NoTag(FORMAT_VERSION)
    codedOut.writeFixed32NoTag(totalEntryCount)
    codedOut.flush()
  }

  fun addResTableEntry(table: Resources.ResourceTable) {
    if (currentEntryCount >= totalEntryCount) {
      error("Too many entries being serialized.")
    }
    ++currentEntryCount

    val codedOut = CodedOutputStream.newInstance(output)

    // Write the type.
    codedOut.writeFixed32NoTag(EntryType.RES_TABLE.value)

    // Write the aligned size
    val size = table.serializedSize
    val padding = (4 - size % 4) % 4
    codedOut.writeFixed64NoTag(size.toLong())

    // Write the table
    table.writeTo(codedOut)
    // Write the padding
    writePadding(padding, codedOut)

    codedOut.flush()

    closeIfFinished()
  }

  fun addFileEntry(input: InputStream, file: ResourceFile) =
    addFileEntryImpl(input.readBytes(), file)

  fun addXmlEntry(resource: XmlResource) =
    addFileEntryImpl(resource.xmlProto.toByteArray(), resource.file)

  private fun addFileEntryImpl(content: ByteArray, file: ResourceFile) {
    if (currentEntryCount >= totalEntryCount) {
      error("Too many entries being serialized.")
    }
    ++currentEntryCount

    val codedOut = CodedOutputStream.newInstance(output)

    // Write the type.
    codedOut.writeFixed32NoTag(EntryType.RES_FILE.value)

    val compiledFile = serializeCompiledFileToPb(file)
    // Write the aligned size.
    val headerSize = compiledFile.serializedSize
    val headerPadding = (4 - (headerSize % 4)) % 4
    val dataSize = content.size
    val dataPadding = (4 - (dataSize % 4)) % 4
    val totalSize =
      RES_FILE_ENTRY_HEADER_SIZE.toLong() + headerSize + headerPadding + dataSize + dataPadding
    codedOut.writeFixed64NoTag(totalSize)

    // Write the res file header size.
    codedOut.writeFixed32NoTag(headerSize)

    // Write the data payload size.
    codedOut.writeFixed64NoTag(dataSize.toLong())

    // Write the header (config etc.)
    compiledFile.writeTo(codedOut)
    writePadding(headerPadding, codedOut)

    // Write the actual file content with padding.
    codedOut.write(content, 0, dataSize)
    writePadding(dataPadding, codedOut)

    codedOut.flush()
    closeIfFinished()
  }

  private fun writePadding(padding: Int, codedOut: CodedOutputStream) {
    for (i in 1..padding) {
      codedOut.writeRawByte(0.toByte())
    }
  }

  private fun closeIfFinished() {
    if (currentEntryCount == totalEntryCount) {
      output.close()
    }
  }

  companion object {
    const val FORMAT_MAGIC = 0x54504141
    const val FORMAT_VERSION = 1
    const val RES_FILE_ENTRY_HEADER_SIZE = 12
  }
}
