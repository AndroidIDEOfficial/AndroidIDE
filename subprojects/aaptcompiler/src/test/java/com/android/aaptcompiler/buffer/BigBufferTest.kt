package com.android.aaptcompiler.buffer

import com.google.common.truth.Truth
import org.junit.Test

class BigBufferTest {

  @Test
  fun testAllocateSingleBlock() {
    val buffer = BigBuffer(4)

    val entry = buffer.nextBlock(2)
    Truth.assertThat(entry.size).isEqualTo(2)
    Truth.assertThat(entry.start).isEqualTo(0)

    Truth.assertThat(buffer.size).isEqualTo(2)
  }

  @Test
  fun testSameBlockIfNextAllocationFits() {
    val buffer = BigBuffer(16)

    val entry1 = buffer.nextBlock(8)
    val entry2 = buffer.nextBlock(4)

    Truth.assertThat(entry1.size).isEqualTo(8)
    Truth.assertThat(entry1.start).isEqualTo(0)
    Truth.assertThat(entry2.size).isEqualTo(4)
    Truth.assertThat(entry2.start).isEqualTo(8)

    Truth.assertThat(entry1.block).isEqualTo(entry2.block)

    Truth.assertThat(buffer.size).isEqualTo(12)
  }

  @Test
  fun testAllocateBlockLargerThanBlockSize() {
    val buffer = BigBuffer(16)

    val entry = buffer.nextBlock(32)

    Truth.assertThat(entry.size).isEqualTo(32)
    Truth.assertThat(entry.start).isEqualTo(0)

    Truth.assertThat(buffer.size).isEqualTo(32)
  }

  @Test
  fun testAppendAndMoveBlock() {
    val buffer = BigBuffer(16)
    val entry1 = buffer.nextBlock(4)
    entry1.writeInt(33, 0)

    val buffer2 = BigBuffer(16)
    val entry2 = buffer2.nextBlock(4)
    entry2.writeInt(44, 0)

    buffer.append(buffer2)
    Truth.assertThat(buffer2.size).isEqualTo(0)

    Truth.assertThat(buffer.size).isEqualTo(8)
    Truth.assertThat(buffer.block(0).readInt(0)).isEqualTo(33)
    Truth.assertThat(buffer.block(1).readInt(0)).isEqualTo(44)
  }

  @Test
  fun testPadAndAlign() {
    val buffer = BigBuffer(16)

    buffer.nextBlock(2)
    Truth.assertThat(buffer.size).isEqualTo(2)
    buffer.pad(2)
    Truth.assertThat(buffer.size).isEqualTo(4)
    buffer.align4()
    Truth.assertThat(buffer.size).isEqualTo(4)
    buffer.pad(2)
    Truth.assertThat(buffer.size).isEqualTo(6)
    buffer.align4()
    Truth.assertThat(buffer.size).isEqualTo(8)
  }

  @Test
  fun testToByteArray() {
    val buffer = BigBuffer(8)

    val entry1 = buffer.nextBlock(4)
    val entry2 = buffer.nextBlock(8)

    Truth.assertThat(buffer.blocks).hasSize(2)

    entry1.writeByte(0x0f, 0)
    entry1.writeByte(0xf0.toByte(), 1)
    entry2.writeByte(0x11, 4)
    entry2.writeByte(0x22, 5)

    val result = buffer.toBytes()
    Truth.assertThat(result.size).isEqualTo(12)
    Truth.assertThat(result[0]).isEqualTo(0x0f)
    Truth.assertThat(result[1]).isEqualTo(0xf0.toByte())
    Truth.assertThat(result[8]).isEqualTo(0x11)
    Truth.assertThat(result[9]).isEqualTo(0x22)
  }

  @Test
  fun testWriteAndRead() {
    val buffer = BigBuffer()

    val entry = buffer.nextBlock(200)
    entry.writeByte(124, 0)
    Truth.assertThat(entry.readByte(0)).isEqualTo(124)
    entry.writeShort(0x1234, 0)
    Truth.assertThat(entry.readShort(0)).isEqualTo(0x1234)
    entry.writeInt(32768, 0)
    Truth.assertThat(entry.readInt(0)).isEqualTo(32768)
  }
}