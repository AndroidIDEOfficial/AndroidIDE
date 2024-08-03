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

package com.itsaky.androidide.levelhash

import com.google.common.io.ByteStreams
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.levelhash.LevelHash.OverflowException
import com.itsaky.androidide.levelhash.internal.PersistentLevelHash
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.KEYMAP_ENTRY_SIZE_BYTES
import com.itsaky.androidide.levelhash.internal.PersistentMetaIO
import com.itsaky.androidide.levelhash.util.DataExternalizers
import com.itsaky.androidide.levelhash.util.nullable
import com.itsaky.androidide.testing.common.LinuxOnlyTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayInputStream
import java.io.File
import kotlin.random.Random

/**
 * @author Akash Yadav
 */
@RunWith(JUnit4::class)
class PersistentLevelHashTest {

  // Persistent level hash is only supported on Linux
  @JvmField
  @Rule
  val linuxOnlyTestRule = LinuxOnlyTestRule()

  private inline fun <V : String?> persistentStringHash(fileName: String,
                                                        valueExternalizer: DataExternalizer<V>,
                                                        createNew: Boolean = true,
                                                        conf: PersistentHashBuilder<String, V>.() -> Unit = {}
  ): LevelHash<String, V> {
    val indexFile =
      File("build/level-index/hash-${fileName}/${fileName}.storage")
    if (indexFile.parentFile!!.exists() && createNew) {
      indexFile.parentFile!!.deleteRecursively()
    } else {
      indexFile.parentFile!!.mkdirs()
    }

    val hash = LevelHash.persistentHashBuilder<String, V>()
      .keyExternalizer(DataExternalizers.STRING)
      .valueExternalizer(valueExternalizer)
      .indexFile(indexFile)
      .levelSize(2)
      .hashFn(::stringHash)
      .apply { conf(this as PersistentHashBuilder<String, V>) }
      .build()

    assertThat(hash).isNotNull()

    return hash
  }

  private inline fun persistentStringHash(fileName: String,
                                          createNew: Boolean = true,
                                          conf: PersistentHashBuilder<String, String>.() -> Unit = {}
  ): LevelHash<String, String> {
    return persistentStringHash(fileName, DataExternalizers.STRING, createNew,
      conf)
  }

  @Test
  fun `test insertion`() {
    persistentStringHash("insert").use { hash ->
      val key = "key"
      val value = "value"
      assertThat(hash.insert(key, value)).isTrue()

      val slot = hash.findSlot(key)
      assertThat(slot).isNotNull()
      assertThat(slot!!.value).isEqualTo(value)
    }
  }

  @Test(expected = OverflowException::class)
  fun `test overflow`() {
    persistentStringHash("overflow") { autoExpand(false) }.use { hash ->
      for (i in 0..<hash.totalSlotCount) {
        val key = "key${i}"
        val value = "value${i}"
        assertThat(hash.insert(key, value)).isTrue()
      }

      assertThat(hash.insert("k", "v")).isFalse()
    }
  }

  @Test
  fun `test values file expands and remaps in case of buffer overflow`() {
    persistentStringHash("huge") {
      autoExpand(true)
      levelSize(15)
      bucketSize(10)
    }.use { hash ->
      for (i in 0..<hash.totalSlotCount) {
        val key = "key${i}"
        val value = "value${i}"
        if (!hash.insert(key, value)) {
          println("key: $key, value: $value, load=${hash.loadFactor()}")
          assert(false)
        }
      }
    }
  }

  @Test
  fun `test removal`() {
    persistentStringHash("remove").use { hash ->
      assertThat(hash.insert("key", "value")).isTrue()
      assertThat(hash.findSlot("key")).isNotNull()
      assertThat(hash.findSlot("key")?.value).isEqualTo("value")

      assertThat(hash.remove("key")).isEqualTo("value")
      assertThat(hash.findSlot("key")).isNull()
    }
  }

  @Test
  fun `test level hash clear`() {
    val hash = persistentStringHash("clear")
    assertThat(hash.insert("key", "value")).isTrue()
    assertThat(hash.findSlot("key")).isNotNull()

    hash.clear()

    assertThat(hash.findSlot("key")).isNull()
  }

  @Test(expected = LevelHash.EntryNotFoundException::class)
  fun `test value update for non-existing entry`() {
    persistentStringHash("update-non-existing-entry").use { hash ->
      assertThat(hash.insert("k", "v")).isTrue()

      val slot = hash.findSlot("k")
      assertThat(slot).isNotNull()
      assertThat(slot!!.key).isEqualTo("k")
      assertThat(slot.value).isEqualTo("v")

      assertThat(hash.set("kk", "newV")).isNull()
    }
  }

  @Test
  fun `test value update for existing entry with null value`() {
    persistentStringHash(fileName = "update-existing-with-null-value",
      valueExternalizer = DataExternalizers.STRING.nullable()).use { hash ->
      assertThat(hash.insert("k", null)).isTrue()

      var slot = hash.findSlot("k")
      assertThat(slot).isNotNull()
      assertThat(slot!!.key).isEqualTo("k")
      assertThat(slot.value).isNull()

      assertThat(hash.set("k", "newV")).isNull()

      slot = hash.findSlot("k")
      assertThat(slot).isNotNull()
      assertThat(slot!!.key).isEqualTo("k")
      assertThat(slot.value).isEqualTo("newV")
    }

  }

  @Test
  fun `test existing level init`() {
    persistentStringHash(fileName = "init-existing",
      valueExternalizer = DataExternalizers.STRING.nullable()).use { hash ->
      hash.insert("key", "value")
      hash.insert("null", null)
      hash.insert("long",
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

      assertThat(hash["key"]).isEqualTo("value")
      assertThat(hash["null"]).isEqualTo(null)
      assertThat(hash["long"]).isEqualTo(
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
    }

    persistentStringHash(fileName = "init-existing",
      valueExternalizer = DataExternalizers.STRING.nullable(),
      createNew = false).use { hash ->
      assertThat(hash["key"]).isEqualTo("value")
      assertThat(hash["null"]).isEqualTo(null)
      assertThat(hash["long"]).isEqualTo(
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
    }
  }

  @Test
  fun `test level hash expand`() {
    persistentStringHash(fileName = "expand",
      valueExternalizer = DataExternalizers.STRING.nullable()) {
      levelSize(5)
      bucketSize(10)
      autoExpand(false)
    }.use { hash ->
      val slots = hash.totalSlotCount - hash.bucketSize
      for (i in 0..<slots) {
        val key = "key${i}"
        val value = "value${i}"
        assertThat(hash.insert(key, value)).isTrue()
      }

      // expand the level hash
      hash.expand()

      // then check that all existing entries are still present
      for (i in 0..<slots) {
        val key = "key${i}"
        val value = "value${i}"
        assertThat(hash[key]).isEqualTo(value)
      }
    }
  }

  @Test
  fun `test expansion with huge slot count`() {
    persistentStringHash(fileName = "huge-expand",
      valueExternalizer = DataExternalizers.STRING.nullable()) {
      levelSize(15)
      bucketSize(10)
      autoExpand(false)
    }.use { hash ->
      var i = 0
      while (hash.insert("key${i}", "value${i}")) {
        i++
      }

      hash.expand()

      for (j in 0..<i) {
        val key = "key${j}"
        val value = "value${j}"
        assertThat(hash[key]).isEqualTo(value)
      }
    }
  }

  @Test
  fun `test meta after expansion`() {
    persistentStringHash(fileName = "expand",
      valueExternalizer = DataExternalizers.STRING.nullable()) {
      levelSize(5)
      bucketSize(10)
      autoExpand(false)
    }.use { hash ->
      hash as PersistentLevelHash<String, String?>

      val l0Size =
        hash.topLevelBucketCount * hash.io.metaIo.bucketSize * KEYMAP_ENTRY_SIZE_BYTES

      assertThat(hash.io.metaIo.levelSize).isEqualTo(5)
      assertThat(hash.io.metaIo.bucketSize).isEqualTo(10)
      assertThat(hash.io.metaIo.l0Addr).isEqualTo(0)
      assertThat(hash.io.metaIo.l1Addr).isEqualTo(l0Size)

      hash.expand()

      // clear cache so we read values directly from the file
      hash.io.metaIo.clearCache()

      assertThat(hash.io.metaIo.levelSize).isEqualTo(6)
      assertThat(hash.io.metaIo.bucketSize).isEqualTo(10)

      // top level (l0) is moved after the bottom level (l1)
      // the bottom level used to span from `l0Size` to `l0Size + l0Size / 2`
      // and the interim level was added AFTER the previous bottom level
      assertThat(hash.io.metaIo.l0Addr).isEqualTo(l0Size + (l0Size shr 1))

      // previous top level, which used to be located at 0,
      // becomes the new bottom level
      assertThat(hash.io.metaIo.l1Addr).isEqualTo(0L)
    }
  }

  @Test
  fun `test values file binary repr`() {
    val fileName = "values-binary-repr"
    persistentStringHash(fileName) {
      autoExpand(false)
    }.use { hash ->
      // since all the key-value pairs here have fixed size (key[0-9], value[0-9]),
      // the entry size should be constant
      val entrySize = // space allocated for the 'entrySize' field itself is not considered
        8 + // prevEntry
        8 + // nextEntry
        4 + // keySize
        2 + // 2 bytes written by writeUTF
        4 + // actual key
        4 + // valueSize
        2 + // 2 bytes written by writeUTF
        6   // actual value

      for (i in 0..<10) {
        val key = "key${i}"
        val value = "value${i}"
        assertThat(hash.insert(key, value)).isTrue()
      }

      val indexFile =
        File("build/level-index/hash-${fileName}/${fileName}.storage")
      assertThat(indexFile.exists()).isTrue()

      val binaryRepr = indexFile.readBytes()
      val inputStream =
        RandomAccessByteArrayInputStream(binaryRepr)
      val input = ByteStreams.newDataInput(inputStream)

      assertThat(input.readLong()).isEqualTo(PersistentLevelHashIO.VALUES_MAGIC_NUMBER)

      var prevEntry = 0L
      for (i in 0 ..< 10) {

        // - 8 -> size of magic number
        val pos = inputStream.position() - 8

        // entrySize
        assertThat(input.readInt()).isEqualTo(entrySize)

        // prevEntry
        assertThat(input.readLong()).isEqualTo(prevEntry)

        // nextEntry
        // + 4 -> size of entrySize field
        // + 1 -> nextEntry is 1-based
        assertThat(input.readLong()).isEqualTo(pos + entrySize + 4 + 1)

        // keySize
        assertThat(input.readInt()).isEqualTo(6)

        // key
        assertThat(input.readUTF()).isEqualTo("key${i}")

        // valueSize
        assertThat(input.readInt()).isEqualTo(8)

        // value
        assertThat(input.readUTF()).isEqualTo("value${i}")

        // + 1 -> prevEntry is 1-based
        prevEntry = pos + 1
      }
    }
  }

  @Test
  fun `test values file binary repr when head is removed`() {
    val fileName = "values-binary-repr-rem-head"
    persistentStringHash(fileName) {
      autoExpand(false)
    }.use { hash ->

      val entrySize = 38
      val count = 10

      for (i in 0..<count) {
        val key = "key${i}"
        val value = "value${i}"
        assertThat(hash.insert(key, value)).isTrue()
      }

      val indexFile =
        File("build/level-index/hash-${fileName}/${fileName}.storage")
      assertThat(indexFile.exists()).isTrue()

      val metaFile =
        File("build/level-index/hash-${fileName}/${fileName}.storage._meta")
      assertThat(metaFile.exists()).isTrue()

      run {
        val valIs =
          RandomAccessByteArrayInputStream(indexFile.readBytes())
        val valIp = ByteStreams.newDataInput(valIs)

        val metaIo = PersistentMetaIO(metaFile, hash.levelSize, hash.bucketSize)
        assertThat(metaIo.valuesHeadAddr).isEqualTo(1L)
        assertThat(metaIo.valuesTailAddr).isEqualTo(((entrySize + 4) * (count - 1)) + 1)

        assertThat(valIp.readLong()).isEqualTo(PersistentLevelHashIO.VALUES_MAGIC_NUMBER)
        valIs.seekInt()

        // no previous entry
        assertThat(valIp.readLong()).isEqualTo(0L)

        // next entry
        assertThat(valIp.readLong()).isEqualTo(entrySize + 4 + 1)
      }

      // remove head
      hash.remove("key0")

      run {
        val valIs =
          RandomAccessByteArrayInputStream(indexFile.readBytes())
        val valIp = ByteStreams.newDataInput(valIs)

        val metaIo = PersistentMetaIO(metaFile, hash.levelSize, hash.bucketSize)
        assertThat(metaIo.valuesHeadAddr).isEqualTo(entrySize + 4 + 1)
        assertThat(metaIo.valuesTailAddr).isEqualTo(((entrySize + 4) * (count - 1)) + 1)

        assertThat(valIp.readLong()).isEqualTo(PersistentLevelHashIO.VALUES_MAGIC_NUMBER)

        // check that the first entry has been deallocated
        val bytes = ByteArray(entrySize + 4) { 0 }
        valIp.readFully(bytes)
        assertThat(bytes.sum()).isEqualTo(0)

        // head entry
        assertThat(valIs.position()).isEqualTo(entrySize + 4 + 8)

        assertThat(valIp.readInt()).isEqualTo(entrySize)

        // no previous entry
        assertThat(valIp.readLong()).isEqualTo(0L)

        // nextEntry
        assertThat(valIp.readLong()).isEqualTo(((entrySize + 4) * 2) + 1)
      }
    }
  }

  @Test
  fun `test values file binary repr when tail is removed`() {
    val fileName = "values-binary-repr-rem-tail"
    persistentStringHash(fileName) {
      autoExpand(false)
    }.use { hash ->

      val entrySize = 38
      val count = 10

      for (i in 0..<count) {
        val key = "key${i}"
        val value = "value${i}"
        assertThat(hash.insert(key, value)).isTrue()
      }

      val indexFile =
        File("build/level-index/hash-${fileName}/${fileName}.storage")
      assertThat(indexFile.exists()).isTrue()

      val metaFile =
        File("build/level-index/hash-${fileName}/${fileName}.storage._meta")
      assertThat(metaFile.exists()).isTrue()

      run {
        val valIs =
          RandomAccessByteArrayInputStream(indexFile.readBytes())
        val valIp = ByteStreams.newDataInput(valIs)

        val metaIo = PersistentMetaIO(metaFile, hash.levelSize, hash.bucketSize)
        assertThat(metaIo.valuesHeadAddr).isEqualTo(1L)
        assertThat(metaIo.valuesTailAddr).isEqualTo(((entrySize + 4) * (count - 1)) + 1)

        assertThat(valIp.readLong()).isEqualTo(PersistentLevelHashIO.VALUES_MAGIC_NUMBER)

        valIs.position((entrySize + 4L) * (count - 1) + 8L) // +8 -> size of magic number
        assertThat(valIp.readInt()).isEqualTo(entrySize)
        assertThat(valIp.readLong()).isEqualTo(((entrySize + 4) * (count - 2)) + 1)

        // 'nextEntry' of last entry points to the location where new entry
        // should be added
        assertThat(valIp.readLong()).isEqualTo((entrySize + 4L) * count + 1)
      }

      // remove head
      hash.remove("key9")

      run {
        val valIs =
          RandomAccessByteArrayInputStream(indexFile.readBytes())
        val valIp = ByteStreams.newDataInput(valIs)

        val metaIo = PersistentMetaIO(metaFile, hash.levelSize, hash.bucketSize)
        assertThat(metaIo.valuesHeadAddr).isEqualTo(1L)
        assertThat(metaIo.valuesTailAddr).isEqualTo(((entrySize + 4) * (count - 2)) + 1)

        assertThat(valIp.readLong()).isEqualTo(PersistentLevelHashIO.VALUES_MAGIC_NUMBER)

        valIs.position((entrySize + 4L) * (count - 1) + 8L) // +8 -> size of magic number
        assertThat(valIp.readInt()).isEqualTo(0) // no 'key9' entry as it was removed

        valIs.position((entrySize + 4L) * (count - 2) + 8L) // +8 -> size of magic number
        assertThat(valIp.readInt()).isEqualTo(entrySize) // tail entry is 'key8'
        assertThat(valIp.readLong()).isEqualTo(((entrySize + 4) * (count - 3)) + 1)

        // 'nextEntry' of last entry points to the location where new entry
        // should be added
        assertThat(valIp.readLong()).isEqualTo((entrySize + 4L) * (count - 1) + 1)
      }
    }
  }

  @Test
  fun `test values file binary repr when entry is removed at random pos in the middle`() {
    val fileName = "values-binary-repr-rem-tail"
    persistentStringHash(fileName) {
      autoExpand(false)
    }.use { hash ->

      val entrySize = 38
      val count = 10

      for (i in 0..<count) {
        val key = "key${i}"
        val value = "value${i}"
        assertThat(hash.insert(key, value)).isTrue()
      }

      val indexFile =
        File("build/level-index/hash-${fileName}/${fileName}.storage")
      assertThat(indexFile.exists()).isTrue()

      val metaFile =
        File("build/level-index/hash-${fileName}/${fileName}.storage._meta")
      assertThat(metaFile.exists()).isTrue()

      val toRemoveIdx = Random.nextInt(1, count - 1)

      run {
        val valIs =
          RandomAccessByteArrayInputStream(indexFile.readBytes())
        val valIp = ByteStreams.newDataInput(valIs)

        val metaIo = PersistentMetaIO(metaFile, hash.levelSize, hash.bucketSize)
        assertThat(metaIo.valuesHeadAddr).isEqualTo(1L)
        assertThat(metaIo.valuesTailAddr).isEqualTo(((entrySize + 4) * (count - 1)) + 1)

        assertThat(valIp.readLong()).isEqualTo(PersistentLevelHashIO.VALUES_MAGIC_NUMBER)

        valIs.position((entrySize + 4L) * toRemoveIdx + 8L) // +8 -> size of magic number
        assertThat(valIp.readInt()).isEqualTo(entrySize)
        assertThat(valIp.readLong()).isEqualTo(((entrySize + 4) * (toRemoveIdx - 1)) + 1)
        assertThat(valIp.readLong()).isEqualTo((entrySize + 4L) * (toRemoveIdx + 1) + 1)
        assertThat(valIp.readInt()).isEqualTo(6)
        assertThat(valIp.readUTF()).isEqualTo("key${toRemoveIdx}")
      }

      hash.remove("key${toRemoveIdx}")

      run {
        val valIs =
          RandomAccessByteArrayInputStream(indexFile.readBytes())
        val valIp = ByteStreams.newDataInput(valIs)

        val metaIo = PersistentMetaIO(metaFile, hash.levelSize, hash.bucketSize)
        assertThat(metaIo.valuesHeadAddr).isEqualTo(1L)
        assertThat(metaIo.valuesTailAddr).isEqualTo(((entrySize + 4) * (count - 1)) + 1)

        assertThat(valIp.readLong()).isEqualTo(PersistentLevelHashIO.VALUES_MAGIC_NUMBER)

        valIs.position((entrySize + 4L) * toRemoveIdx + 8L) // +8 -> size of magic number
        assertThat(valIp.readInt()).isEqualTo(0) // no 'key9' entry as it was removed

        valIs.position((entrySize + 4L) * (toRemoveIdx - 1) + 8L) // +8 -> size of magic number
        assertThat(valIp.readInt()).isEqualTo(entrySize)
        assertThat(valIp.readLong()).isEqualTo(((entrySize + 4) * (toRemoveIdx - 2)) + 1)
        assertThat(valIp.readLong()).isEqualTo((entrySize + 4L) * toRemoveIdx + 1)
        assertThat(valIp.readInt()).isEqualTo(6)
        assertThat(valIp.readUTF()).isEqualTo("key${toRemoveIdx - 1}")

        valIs.position((entrySize + 4L) * (toRemoveIdx + 1) + 8L) // +8 -> size of magic number
        assertThat(valIp.readInt()).isEqualTo(entrySize)
        assertThat(valIp.readLong()).isEqualTo(((entrySize + 4) * (toRemoveIdx - 1)) + 1)
        assertThat(valIp.readLong()).isEqualTo((entrySize + 4L) * toRemoveIdx + 2)
        assertThat(valIp.readInt()).isEqualTo(6)
        assertThat(valIp.readUTF()).isEqualTo("key${toRemoveIdx + 1}")
      }
    }
  }

  private class RandomAccessByteArrayInputStream(
    buf: ByteArray,
    pos: Int = 0,
    len: Int = buf.size
  ) : ByteArrayInputStream(buf, pos, len), RandomAccessIO {

    override fun position(position: Long) {
      this.pos = position.toInt()
    }

    override fun tryPosition(position: Long): Boolean {
      this.position(position)
      return true
    }

    override fun position(): Long {
      return this.pos.toLong()
    }
  }
}
