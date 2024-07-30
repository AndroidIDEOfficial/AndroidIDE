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
import com.itsaky.androidide.levelhash.internal.PersistentKeymapIO
import com.itsaky.androidide.levelhash.internal.PersistentLevelHashIO
import com.itsaky.androidide.levelhash.internal.PersistentMetaIO
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File

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

    return PersistentMetaIO(metaFile)
  }

  @Test
  fun `test meta init with default values`() {
    val io = createMetaIo("init-with-default")
    assertThat(io.valuesVersion).isEqualTo(PersistentLevelHashIO.LEVEL_VALUES_VERSION)
    assertThat(io.keymapVersion).isEqualTo(PersistentLevelHashIO.LEVEL_KEYMAP_VERSION)
    assertThat(io.valuesFirstEntry).isEqualTo(PersistentLevelHashIO.VALUES_HEADER_SIZE_BYTES)
    assertThat(io.valuesNextEntry).isEqualTo(PersistentLevelHashIO.VALUES_HEADER_SIZE_BYTES)
    assertThat(io.valuesFileSize).isEqualTo(PersistentLevelHashIO.VALUES_INITIAL_SIZE_BYTES)
    assertThat(io.keymapSegmentCount).isEqualTo(1)
    assertThat(io.keymapSegmentSize).isEqualTo(PersistentKeymapIO.KEYMAP_SEGMENT_SIZE_BYTES)
  }

  @Test
  fun `test meta init with existing file`() {
    createMetaIo(name = "init-with-existing").use { io ->
      assertThat(io.valuesVersion).isEqualTo(PersistentLevelHashIO.LEVEL_VALUES_VERSION)
      assertThat(io.keymapVersion).isEqualTo(PersistentLevelHashIO.LEVEL_KEYMAP_VERSION)
      assertThat(io.valuesFirstEntry).isEqualTo(PersistentLevelHashIO.VALUES_HEADER_SIZE_BYTES)
      assertThat(io.valuesNextEntry).isEqualTo(PersistentLevelHashIO.VALUES_HEADER_SIZE_BYTES)
      assertThat(io.valuesFileSize).isEqualTo(PersistentLevelHashIO.VALUES_INITIAL_SIZE_BYTES)
      assertThat(io.keymapSegmentCount).isEqualTo(1)
      assertThat(io.keymapSegmentSize).isEqualTo(PersistentKeymapIO.KEYMAP_SEGMENT_SIZE_BYTES)

      io.valuesVersion = 2
      io.keymapVersion = 3
      io.valuesFirstEntry = 100
      io.valuesNextEntry = 200
      io.valuesFileSize = 1024

      io.keymapSegmentCount = 10
      io.keymapSegmentSize = 1024
    }

    createMetaIo(name = "init-with-existing", createNew = false).use { io ->
      assertThat(io.valuesVersion).isEqualTo(2)
      assertThat(io.keymapVersion).isEqualTo(3)
      assertThat(io.valuesFirstEntry).isEqualTo(100)
      assertThat(io.valuesNextEntry).isEqualTo(200)
      assertThat(io.valuesFileSize).isEqualTo(1024)
      assertThat(io.keymapSegmentCount).isEqualTo(10)
      assertThat(io.keymapSegmentSize).isEqualTo(1024)
    }
  }
}