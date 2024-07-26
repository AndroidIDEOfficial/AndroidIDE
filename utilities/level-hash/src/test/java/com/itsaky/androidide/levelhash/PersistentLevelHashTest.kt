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
import com.itsaky.androidide.levelhash.util.DataExternalizers
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

  private fun persistentStringHash(
    fileName: String,
    conf: PersistentHashBuilder<String, String>.() -> Unit = {}
  ): LevelHash<String, String> {
    val indexFile = File("./build/sample-index/${fileName}.storage")
    if (indexFile.parentFile!!.exists()) {
      indexFile.parentFile!!.deleteRecursively()
    }

    indexFile.parentFile!!.mkdirs()

    val hash = LevelHash.persistentHashBuilder<String, String>()
      .keyExternalizer(DataExternalizers.STRING)
      .valueExternalizer(DataExternalizers.STRING)
      .indexFile(indexFile)
      .apply(conf)
      .levelSize(2)
      .hashFn(::stringHash)
      .build()

    assertThat(hash).isNotNull()

    return hash
  }

  @Test
  fun testInsertion() {
    val hash = persistentStringHash("insert")

    val key = "key"
    val value = "value"
    assertThat(hash.insert(key, value)).isTrue()

    val slot = hash.findSlot(key)
    assertThat(slot).isNotNull()
    assertThat(slot!!.value).isEqualTo(value)
  }

  @Test(expected = OverflowException::class)
  fun testOverflow() {
    val hash = persistentStringHash("overflow") { autoExpand(false) }
    for (i in 0..<hash.totalSlotCount) {
      val key = "key${i}"
      val value = "value${i}"
      assertThat(hash.insert(key, value)).isTrue()
    }

    assertThat(hash.insert("k", "v")).isFalse()
  }

  @Test
  fun testRemoval() {
    val hash = persistentStringHash("remove")
    assertThat(hash.insert("key", "value")).isTrue()
    assertThat(hash.findSlot("key")).isNotNull()
    assertThat(hash.findSlot("key")?.value).isEqualTo("value")

    assertThat(hash.remove("key")).isEqualTo("value")
    assertThat(hash.findSlot("key")).isNull()
  }

  @Test
  fun `test level hash clear`() {
    val hash = persistentStringHash("clear")
    assertThat(hash.insert("key", "value")).isTrue()
    assertThat(hash.findSlot("key")).isNotNull()

    hash.clear()

    assertThat(hash.findSlot("key")).isNull()
  }
}
