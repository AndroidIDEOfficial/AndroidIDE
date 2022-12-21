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

package com.itsaky.androidide.projects

import com.itsaky.androidide.eventbus.events.EventReceiver
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentCloseEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentOpenEvent
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.progress.ProcessCancelledException
import com.itsaky.androidide.progress.ProgressManager
import com.itsaky.androidide.projects.models.ActiveDocument
import com.itsaky.androidide.utils.ILogger
import java.io.BufferedReader
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import org.apache.commons.io.FileUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.BACKGROUND

/**
 * Manages active documents.
 *
 * @author Akash Yadav
 */
object FileManager : EventReceiver {

  private val log = ILogger.newInstance(javaClass.simpleName)
  private val activeDocuments: MutableMap<Path, ActiveDocument> = mutableMapOf()

  fun isActive(file: Path): Boolean {
    return this.activeDocuments.containsKey(file.normalize())
  }

  fun getActiveDocument(file: Path): ActiveDocument? {
    return this.activeDocuments[file.normalize()]
  }

  fun getActiveDocumentCount(): Int {
    return this.activeDocuments.size
  }

  fun getDocumentContents(file: Path): String {
    val document = getActiveDocument(file)
    if (document != null) {
      return document.content
    }

    return getFileContents(file)
  }

  fun getLastModified(file: Path): Instant {
    val document = getActiveDocument(file)
    if (document != null) {
      return document.modified
    }

    return getLastModifiedFromDisk(file)
  }

  fun getReader(file: Path): BufferedReader {
    val document = getActiveDocument(file)
    if (document != null) {
      return document.reader()
    }

    return createFileReader(file)
  }

  fun getInputStream(file: Path): InputStream {
    val document = getActiveDocument(file)
    if (document != null) {
      return document.inputStream()
    }

    return createFileInputStream(file)
  }

  @Subscribe(threadMode = BACKGROUND)
  @Suppress("unused")
  fun onDocumentOpen(event: DocumentOpenEvent) {
    activeDocuments[event.openedFile.normalize()] = createDocument(event)
  }

  @Subscribe(threadMode = BACKGROUND)
  @Suppress("unused")
  fun onDocumentContentChange(event: DocumentChangeEvent) {
    activeDocuments[event.changedFile.normalize()] = createDocument(event)
  }

  @Subscribe(threadMode = BACKGROUND)
  @Suppress("unused")
  fun onDocumentClose(event: DocumentCloseEvent) {
    activeDocuments.remove(event.closedFile.normalize())
  }

  private fun createDocument(event: DocumentOpenEvent): ActiveDocument {
    return ActiveDocument(
      file = event.openedFile,
      content = event.text,
      changeRange = Range.NONE,
      version = event.version,
      changDelta = 0,
      modified = Instant.now()
    )
  }

  private fun createDocument(event: DocumentChangeEvent): ActiveDocument {
    return ActiveDocument(
      file = event.changedFile,
      content = event.newText,
      changeRange = event.changeRange,
      version = event.version,
      changDelta = event.changeDelta,
      modified = Instant.now()
    )
  }

  private fun createFileReader(file: Path): BufferedReader {
    return try {
      Files.newBufferedReader(file)
    } catch (noFile: java.nio.file.NoSuchFileException) {
      log.warn("No such file", noFile)
      "".reader().buffered()
    } catch (cancelled: ProcessCancelledException) {
      "".reader().buffered()
    }
  }

  private fun createFileInputStream(file: Path): InputStream {
    return try {
      Files.newInputStream(file)
    } catch (noFile: java.nio.file.NoSuchFileException) {
      log.warn("No such file", noFile)
      "".byteInputStream()
    } catch (cancelled: ProcessCancelledException) {
      "".byteInputStream()
    }
  }

  private fun getLastModifiedFromDisk(file: Path): Instant {
    return Files.getLastModifiedTime(file).toInstant()
  }

  private fun getFileContents(file: Path): String {
    return try {
      ProgressManager.abortIfCancelled()
      FileUtils.readFileToString(file.toFile(), Charset.defaultCharset())
    } catch (noFile: java.nio.file.NoSuchFileException) {
      log.warn("No such file", noFile)
      ""
    } catch (cancelled: ProcessCancelledException) {
      ""
    }
  }
}
