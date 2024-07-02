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

package com.android.aaptcompiler.buffer

import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.IndexOutOfBoundsException
import kotlin.math.max

/**
 * Inspired by protobuf's ZeroCopyOutputStream, offers blocks of memory in which to write without
 * knowing the full size of the entire payload. At the core of BigBuffer, it is a list of memory
 * blocks. As one fills up, another block is allocated and appended to the end of the list.
 *
 * <p> This class is primarily used to flatten StringPool blobs which do not their size, until after
 * it has been written.
 *
 * @property blockSize the minimum block size per block. If an entry to the BigBuffer through a
 * call to [nextBlock] would be larger than this, a block will be created large enough to fit the
 * entry.
 */
class BigBuffer(val blockSize: Int = 1024) {
  var size: Int = 0
    private set

  val blocks = mutableListOf<Block>()

  class Block(
    internal var size: Int, internal  val blockSize: Int, internal val data: ByteBuffer)

  data class BlockRef internal constructor(
    val start: Int, val size: Int, val block: Block) {

    fun writeByte(value: Byte, location: Int) {
      if (location + 1 > size) {
        throw IndexOutOfBoundsException()
      }
      val actualLocation = start + location
      block.data.put(actualLocation, value)
    }

    fun writeShort(value: Short, location: Int) {
      if (location + 2 > size) {
        throw IndexOutOfBoundsException()
      }
      val actualLocation = start + location
      block.data.putShort(actualLocation, value)
    }

    fun writeInt(value: Int, location: Int) {
      if (location + 4 > size) {
        throw IndexOutOfBoundsException()
      }
      val actualLocation = start + location
      block.data.putInt(actualLocation, value)
    }

    fun readByte(location: Int): Byte {
      if (location + 1 > size) {
        throw IndexOutOfBoundsException()
      }
      val actualLocation = start + location
      return block.data.get(actualLocation)
    }

    fun readShort(location: Int): Short {
      if (location + 2 > size) {
        throw IndexOutOfBoundsException()
      }
      val actualLocation = start + location
      return block.data.getShort(actualLocation)
    }

    fun readInt(location: Int): Int {
      if (location + 4 > size) {
        throw IndexOutOfBoundsException()
      }
      val actualLocation = start + location
      return block.data.getInt(actualLocation)
    }
  }

  /**
   * Retrieves a block ref large enough to contain an entry with element size. Allocated a new block
   * if necessary.
   *
   * @param elementSize the size of the entry that needs to be created.
   * @return a reference to a portion of a block of the required size. Writing to this reference
   * will modify the underlying [BigBuffer].
   */
  fun nextBlock(elementSize: Int): BlockRef {
    if (blocks.isNotEmpty()) {
      val back = blocks.last()
      // We still have room in the last block so we can use it.
      if (back.blockSize - back.size >= elementSize) {
        val start = back.size
        back.size += elementSize
        size += elementSize
        return BlockRef(start, elementSize, back)
      }
    }

    val actualSize = max(this.blockSize, elementSize)
    val block = Block(
      elementSize,
      actualSize,
      ByteBuffer.wrap(ByteArray(actualSize)).order(ByteOrder.nativeOrder()))
    blocks.add(block)
    size += elementSize
    return BlockRef(0, elementSize, block)
  }

  /**
   * Creates an empty entry of the given size.
   *
   * @param bytes the number of bytes to be padded.
   */
  fun pad(bytes: Int) {
    nextBlock(bytes)
  }

  /**
   * Pads the [BigBuffer] so that the next created entry is aligned to a 4 byte chunk.
   */
  fun align4() {
    val unaligned = size % 4
    if (unaligned != 0) {
      pad(4-unaligned)
    }
  }

  /**
   * Retrieves a reference to the block on the given index.
   *
   * @param index the index of the block to be retrieved.
   * @return a reference to this block.
   *
   * <p> It is not guaranteed, nor is it expected that the {@code n}th block will be equivalent to
   * the {@code n}th entry. This is because multiple entries, if small enough, will be collected
   * to the same block.
   */
  fun block(index: Int): BlockRef {
    return BlockRef(0, blocks[index].size, blocks[index])
  }

  /**
   * Appends the given BigBuffer to the current blocks. The given buffer will be empty after this
   * operation is complete.
   *
   * @param other the [BigBuffer] to be appended to the current buffer. This buffer will be empty,
   * after this method is complete.
   */
  fun append(other: BigBuffer) {
    blocks.addAll(other.blocks)
    size += other.size

    other.blocks.clear()
    other.size = 0
  }

  /**
   * Returns the contents of the [BigBuffer] as one cohesive array of bytes.
   */
  fun toBytes(): ByteArray {
    val array = ByteArray(size)
    var currentLocation = 0
    for (block in blocks) {
      val data = block.data.position(0) as ByteBuffer
      data.get(array, currentLocation, block.size)
      currentLocation += block.size
    }
    return array
  }
}