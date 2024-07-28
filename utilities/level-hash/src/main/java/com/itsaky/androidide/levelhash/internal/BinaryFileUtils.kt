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

package com.itsaky.androidide.levelhash.internal

import com.itsaky.androidide.levelhash.internal.PersistentKeymapIO.Companion.KEYMAP_HEADER_SIZE_BYTES
import com.itsaky.androidide.levelhash.internal.PersistentKeymapIO.Companion.KEYMAP_MAGIC_NUMBER
import org.slf4j.LoggerFactory
import java.io.DataInputStream
import java.io.File
import java.io.RandomAccessFile

/**
 * @author Akash Yadav
 */
internal object BinaryFileUtils {

  private val log = LoggerFactory.getLogger(BinaryFileUtils::class.java)

  fun initSparseFile(file: File, magicNumber: Long, maxLength: Long,
                     conf: (raf: RandomAccessFile) -> Unit = {}
  ) {
    val raf = RandomAccessFile(file, "rw")
    if (!file.exists()) {
      doInit(file, raf, magicNumber, maxLength, conf)
      return
    }

    if (file.isDirectory) {
      file.deleteRecursively()
    } else if (file.isFile && file.length() < KEYMAP_HEADER_SIZE_BYTES) {
      check(file.delete()) { "Failed to delete existing index file" }
    } else {
      DataInputStream(file.inputStream()).use { dis ->
        val magic = dis.readLong()
        if (magic != magicNumber) {
          // mismatched magic number
          // delete file and create new one
          log.warn(
            "Magic number mismatch for index: {}. Deleting existing file...",
            file.canonicalPath)
          check(file.delete()) { "Failed to delete existing index file" }
        }
      }
    }

    doInit(file, raf, magicNumber, maxLength, conf)
  }

  private inline fun doInit(file: File, raf: RandomAccessFile, magic: Long,
                            maxLength: Long,
                            conf: (raf: RandomAccessFile) -> Unit = {}
  ) {
    check(file.createNewFile()) {
      "Failed to create keymap file: ${file.canonicalPath}"
    }

//    raf.setLength(maxLength)

    raf.seek(0)
    raf.writeLong(magic)
    raf.use(conf)
  }
}