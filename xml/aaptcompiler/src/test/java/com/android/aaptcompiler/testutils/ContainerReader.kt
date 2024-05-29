package com.android.aaptcompiler.testutils

import android.aapt.pb.internal.ResourcesInternal
import com.android.aapt.Resources
import com.android.aaptcompiler.Container
import com.google.common.base.Preconditions
import com.google.protobuf.CodedInputStream
import java.io.File

class ContainerReader(input: File) {
  val numEntries: Int
  val inputStream = CodedInputStream.newInstance(input.inputStream())
  val entries: List<ContainerEntry>

  init {
    Preconditions.checkState(
      inputStream.readFixed32() == Container.FORMAT_MAGIC,
      "File has invalid format."
    )

    Preconditions.checkState(
      inputStream.readFixed32() == Container.FORMAT_VERSION,
      "File has invalid version."
    )

    numEntries = inputStream.readFixed32()
    val tempEntries = mutableListOf<ContainerEntry>()

    for (i in 0.until(numEntries)) {
      val entryType = inputStream.readFixed32()
      val entry = when (entryType) {
        Container.EntryType.RES_TABLE.value -> gatherTableEntry(inputStream)
        else -> gatherFileEntry(inputStream)
      }
      tempEntries.add(entry)
    }

    entries = tempEntries.toList()
  }

  companion object {
    fun gatherTableEntry(input: CodedInputStream): ContainerEntry {
      val size = input.readFixed64()
      val tableBytes = input.readRawBytes(size.toInt())
      val table = Resources.ResourceTable.parseFrom(tableBytes)

      val padding = (4 - size % 4) % 4
      input.readRawBytes(padding.toInt())

      return TableEntry(size, table)
    }

    fun gatherFileEntry(input: CodedInputStream): ContainerEntry {
      val totalSize = input.readFixed64()
      val fileHeaderSize = input.readFixed32()
      val dataSize = input.readFixed64()

      val fileHeaderBytes = input.readRawBytes(fileHeaderSize)
      val compiledFile = ResourcesInternal.CompiledFile.parseFrom(fileHeaderBytes)
      val headerPadding = (4 - fileHeaderSize % 4) % 4
      input.readRawBytes(headerPadding)

      val dataBytes = input.readRawBytes(dataSize.toInt())
      val dataPadding = (4 - dataSize % 4) % 4
      input.readRawBytes(dataPadding.toInt())

      val expectedTotalSize = 12 + fileHeaderSize + headerPadding + dataSize + dataPadding
      Preconditions.checkState(expectedTotalSize == totalSize)
      return FileEntry(totalSize, compiledFile, dataBytes)
    }
  }
}

open class ContainerEntry(val size: Long)

class TableEntry(size: Long, val table: Resources.ResourceTable): ContainerEntry(size)

class FileEntry(size: Long, val header: ResourcesInternal.CompiledFile,val data: ByteArray):
        ContainerEntry(size)