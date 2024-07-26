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
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.math.pow

/**
 * @author Akash Yadav
 */
@RunWith(JUnit4::class)
class InMemoryLevelHashTest {

  private fun <V: String?> stringStringLevelHash(conf: InMemoryHashBuilder<String, V>.() -> Unit = {}) =
    LevelHash.inMemoryHashBuilder<String, V>()
      .apply(conf)
      .levelSize(2)
      .hashFn(::stringHash)
      .build()

  @Test
  fun `test simple hash insert`() {
    val hash = stringStringLevelHash<String>()

    hash.insert("Hello", "World")

    val slot = hash.findSlot("Hello")
    assertThat(slot).isNotNull()
    assertThat(slot!!.key).isEqualTo("Hello")
    assertThat(slot.value).isEqualTo("World")
  }

  @Test(expected = LevelHash.DuplicateKeyException::class)
  fun `test simple hash key re-insertion`() {
    val hash = stringStringLevelHash<String> { uniqueKeys(true) }

    hash.insert("Hello", "World")

    val slot = hash.findSlot("Hello")
    assertThat(slot).isNotNull()
    assertThat(slot!!.key).isEqualTo("Hello")
    assertThat(slot.value).isEqualTo("World")

    hash.insert("Hello", "World")
  }

  @Test
  fun `test flaky key hash computation`() {
    repeat(100) {
      // sometimes, the calculated fidx and sidx values are invalid bucket indices
      // this happens due to the two randomly generated seeds are (were) not deterministic
      // this test is to ensure that changes in the default SeedGeneratorFn do not
      // lead to invalid bucket indices again
      val hash = stringStringLevelHash<String>()

      hash.insert("key1", "value1")
      hash.insert("key2", "value2")
    }
  }

  @Test(expected = LevelHash.OverflowException::class)
  fun `test bucket limit`() {
    val hash = stringStringLevelHash<String> { autoExpand(false) }

    // fill up the level hash
    repeat(hash.totalSlotCount) { idx ->
      assertThat(hash.insert("key${idx}", "value${idx}")).isTrue()
    }

    // inserting any more elements must fail
    assertThat(hash.insert("key${hash.totalSlotCount}",
      "value${hash.totalSlotCount}")).isFalse()
  }

  @Test
  fun `test level hash auto expands`() {
    val hash = stringStringLevelHash<String> { autoExpand(true) }

    val levelSize = hash.levelSize
    val slotCount = hash.totalSlotCount
    val topBucketCount = hash.topLevelBucketCount
    val bucketCount = hash.totalBucketCount

    assertThat(levelSize).isEqualTo(2)
    assertThat(topBucketCount).isEqualTo(2.0.pow(levelSize).toLong())
    assertThat(bucketCount).isEqualTo(
      (2.0.pow(levelSize) + 2.0.pow(levelSize shr 1)).toLong())
    assertThat(slotCount).isEqualTo(bucketCount * hash.bucketSize)

    // fill up the level hash
    repeat(hash.totalSlotCount) { idx ->
      assertThat(hash.insert("key${idx}", "value${idx}")).isTrue()
    }

    // inserting a new element must expand the level hash
    hash.insert("key${hash.totalSlotCount}", "value${hash.totalSlotCount}")

    // level size is incremented by 1
    assertThat(hash.levelSize).isEqualTo(levelSize + 1)
    assertThat(hash.bucketSize).isEqualTo(LevelHash.BUCKET_SIZE_DEFAULT)

    // level hash doubles its capacity after an expansion
    assertThat(hash.totalSlotCount).isEqualTo(slotCount * 2)
    assertThat(hash.totalBucketCount).isEqualTo(bucketCount * 2)
    assertThat(hash.topLevelBucketCount).isEqualTo(topBucketCount * 2)
  }

  @Test
  fun `test value update for existing entry`() {
    val hash = stringStringLevelHash<String>()

    assertThat(hash.insert("k", "v")).isTrue()

    var slot = hash.findSlot("k")
    assertThat(slot).isNotNull()
    assertThat(slot!!.key).isEqualTo("k")
    assertThat(slot.value).isEqualTo("v")

    assertThat(hash.set("k", "newV")).isEqualTo("v")

    slot = hash.findSlot("k")
    assertThat(slot).isNotNull()
    assertThat(slot!!.key).isEqualTo("k")
    assertThat(slot.value).isEqualTo("newV")
  }

  @Test(expected = LevelHash.EntryNotFoundException::class)
  fun `test value update for non-existing entry`() {
    val hash = stringStringLevelHash<String>()

    assertThat(hash.insert("k", "v")).isTrue()

    val slot = hash.findSlot("k")
    assertThat(slot).isNotNull()
    assertThat(slot!!.key).isEqualTo("k")
    assertThat(slot.value).isEqualTo("v")

    assertThat(hash.set("kk", "newV")).isNull()
  }

  @Test
  fun `test value update for existing entry with null value`() {
    val hash = stringStringLevelHash<String?>()

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

  @Test
  fun `test entry removal`() {
    val hash = stringStringLevelHash<String>()
    assertThat(hash.insert("key", "value")).isTrue()
    assertThat(hash.findSlot("key")).isNotNull()
    assertThat(hash.findSlot("key")?.value).isEqualTo("value")

    assertThat(hash.remove("key")).isEqualTo("value")
    assertThat(hash.findSlot("key")).isNull()
  }

  @Test
  fun `test level hash clear`() {
    val hash = stringStringLevelHash<String>()
    assertThat(hash.insert("key", "value")).isTrue()
    assertThat(hash.findSlot("key")).isNotNull()

    hash.clear()

    assertThat(hash.findSlot("key")).isNull()
  }
}