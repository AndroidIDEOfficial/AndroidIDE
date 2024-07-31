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
import com.itsaky.androidide.levelhash.LevelHash.Companion.BUCKET_SIZE_DEFAULT
import com.itsaky.androidide.levelhash.LevelHash.Companion.LEVEL_SIZE_DEFAULT
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.KEYMAP_ENTRY_SIZE_BYTES
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.LEVEL_KEYMAP_VERSION
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.LEVEL_VALUES_VERSION
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.VALUES_HEADER_SIZE_BYTES
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO.Companion.VALUES_SEGMENT_SIZE_BYTES
import com.itsaky.androidide.levelhash.internal.PersistentMetaIO
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import kotlin.math.pow

/**
 * @author Akash Yadav
 */
@RunWith(JUnit4::class)
class PersistentMetaIOTest {

  private fun createMetaIo(
    name: String,
    createNew: Boolean = true,
  ): PersistentMetaIO {
    val metaFile = File("build/level-hash/meta-${name}/${name}.storage._meta")
    if (metaFile.parentFile!!.exists() && createNew) {
      metaFile.parentFile!!.deleteRecursively()
    } else {
      metaFile.parentFile!!.mkdirs()
    }

    return PersistentMetaIO(metaFile = metaFile,
      levelSize = LEVEL_SIZE_DEFAULT,
      bucketSize = BUCKET_SIZE_DEFAULT)
  }

  @Test
  fun `test meta init with default values`() {
    val io = createMetaIo("init-with-default")
    assertThat(io.valuesVersion).isEqualTo(LEVEL_VALUES_VERSION)
    assertThat(io.keymapVersion).isEqualTo(LEVEL_KEYMAP_VERSION)
    assertThat(io.valuesFirstEntry).isEqualTo(VALUES_HEADER_SIZE_BYTES)
    assertThat(io.valuesNextEntry).isEqualTo(VALUES_HEADER_SIZE_BYTES)
    assertThat(io.valuesFileSize).isEqualTo(VALUES_SEGMENT_SIZE_BYTES)
    assertThat(io.levelSize).isEqualTo(LEVEL_SIZE_DEFAULT)
    assertThat(io.bucketSize).isEqualTo(BUCKET_SIZE_DEFAULT)
    assertThat(io.l0Addr).isEqualTo(0L)
    assertThat(io.l1Addr).isEqualTo(2.0.pow(LEVEL_SIZE_DEFAULT).toLong() * BUCKET_SIZE_DEFAULT * KEYMAP_ENTRY_SIZE_BYTES)
  }

  @Test
  fun `test meta init with existing file`() {
    createMetaIo(name = "init-with-existing").use { io ->
      io.valuesVersion = 2
      io.keymapVersion = 3
      io.valuesFirstEntry = 100
      io.valuesNextEntry = 200
      io.valuesFileSize = 1024

      io.levelSize = 10
      io.bucketSize = 20
    }

    createMetaIo(name = "init-with-existing", createNew = false).use { io ->
      assertThat(io.valuesVersion).isEqualTo(2)
      assertThat(io.keymapVersion).isEqualTo(3)
      assertThat(io.valuesFirstEntry).isEqualTo(100)
      assertThat(io.valuesNextEntry).isEqualTo(200)
      assertThat(io.valuesFileSize).isEqualTo(1024)
      assertThat(io.levelSize).isEqualTo(10)
      assertThat(io.bucketSize).isEqualTo(20)
      assertThat(io.l0Addr).isEqualTo(0L)
      assertThat(io.l1Addr).isEqualTo(2.0.pow(io.levelSize).toLong() * io.bucketSize * KEYMAP_ENTRY_SIZE_BYTES)
    }
  }
}