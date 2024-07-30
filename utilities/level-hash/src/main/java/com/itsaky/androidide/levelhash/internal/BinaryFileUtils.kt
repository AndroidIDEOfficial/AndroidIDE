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

import org.slf4j.LoggerFactory
import java.io.DataInputStream
import java.io.EOFException
import java.io.File
import java.io.RandomAccessFile

/**
 * @author Akash Yadav
 */
internal object BinaryFileUtils {

  private val log = LoggerFactory.getLogger(BinaryFileUtils::class.java)

  fun initSparseFile(file: File, magicNumber: Long? = null,
                     conf: (raf: RandomAccessFile) -> Unit = {}
  ) {
    if (!file.exists()) {
      doInit(file, magicNumber, conf)
      return
    }

    val canonicalPath = file.canonicalPath

    if (file.isDirectory) {
      check(file.deleteRecursively()) {
        "Failed to delete existing index directory: $canonicalPath"
      }
    } else if (magicNumber != null) {
      DataInputStream(file.inputStream()).use { dis ->
        val magic = try {
          dis.readLong()
        } catch (e: EOFException) {
          0L
        }
        if (magic != magicNumber) {
          // mismatched magic number
          // delete file and create new one
          log.warn(
            "Magic number mismatch for file: {}. Deleting existing file...",
            canonicalPath)

          check(file.delete()) {
            "Failed to delete existing index file; $canonicalPath"
          }
        }
      }
    }

    doInit(file, magicNumber, conf)
  }

  private inline fun doInit(file: File, magic: Long?,
                            conf: (raf: RandomAccessFile) -> Unit = {}
  ) {
    if (!file.exists()) {
      file.parentFile!!.mkdirs()
      check(file.createNewFile()) {
        "Failed to create file: ${file.canonicalPath}"
      }
    }

    RandomAccessFile(file, "rw").use { raf ->
      if (magic != null) {
        raf.seek(0)
        raf.writeLong(magic)
      }

      conf(raf)
    }
  }
}