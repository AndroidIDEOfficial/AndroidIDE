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

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.levelhash.LevelHash.OverflowException
import com.itsaky.androidide.levelhash.internal.PersistentLevelHash
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.KEYMAP_ENTRY_SIZE_BYTES
import com.itsaky.androidide.levelhash.util.DataExternalizers
import com.itsaky.androidide.levelhash.util.nullable
import com.itsaky.androidide.testing.common.LinuxOnlyTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File

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
    persistentStringHash(
      fileName = "init-existing",
      valueExternalizer = DataExternalizers.STRING.nullable()
    ).use { hash ->
      hash.insert("key", "value")
      hash.insert("null", null)
      hash.insert("long", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

      assertThat(hash["key"]).isEqualTo("value")
      assertThat(hash["null"]).isEqualTo(null)
      assertThat(hash["long"]).isEqualTo("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
    }

    persistentStringHash(
      fileName = "init-existing",
      valueExternalizer = DataExternalizers.STRING.nullable(),
      createNew = false
    ).use { hash ->
      assertThat(hash["key"]).isEqualTo("value")
      assertThat(hash["null"]).isEqualTo(null)
      assertThat(hash["long"]).isEqualTo("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
    }
  }

  @Test
  fun `test level hash expand`() {
    persistentStringHash(
      fileName = "expand",
      valueExternalizer = DataExternalizers.STRING.nullable()
    ) {
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
    persistentStringHash(
      fileName = "huge-expand",
      valueExternalizer = DataExternalizers.STRING.nullable()
    ){
      levelSize(15)
      bucketSize(10)
      autoExpand(false)
    }.use { hash ->
      var i = 0
      while(hash.insert("key${i}", "value${i}")) {
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
    persistentStringHash(
      fileName = "expand",
      valueExternalizer = DataExternalizers.STRING.nullable()
    ) {
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
}
