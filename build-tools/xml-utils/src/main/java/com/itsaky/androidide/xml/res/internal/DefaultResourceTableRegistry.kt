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

package com.itsaky.androidide.xml.res.internal

import com.android.aaptcompiler.BlameLogger
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.TableExtractor
import com.android.aaptcompiler.TableExtractorOptions
import com.android.aaptcompiler.extractPathData
import com.itsaky.androidide.aapt.logging.IDELogger
import com.itsaky.androidide.layoutlib.resources.ResourceVisibility.PUBLIC
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.xml.res.ResourceTableRegistry
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation of the [ResourceTableRegistry].
 *
 * @author Akash Yadav
 */
internal class DefaultResourceTableRegistry : ResourceTableRegistry {

  private val log = ILogger.newInstance(javaClass.simpleName)

  private var tables = ConcurrentHashMap<String, ResourceTable>()

  override fun forResourceDir(dir: File): ResourceTable? {
    var table = tables[dir.path]
    if (table != null) {
      return table
    }

    table = createTable(dir) ?: return null
    tables[dir.path] = table
    return table
  }

  private fun createTable(resDir: File): ResourceTable? {
    val values = File(resDir, "values")
    if (!values.exists()) {
      return null
    }

    val logger = BlameLogger(IDELogger)
    val table = ResourceTable()
    val options =
      TableExtractorOptions(translatable = true, errorOnPositionalArgs = false, visibility = PUBLIC)
    values
      .listFiles { file -> file.isFile && file.extension == "xml" }
      ?.forEach { updateFromFile(it = it, table = table, options = options, logger = logger) }
    return table
  }

  override fun removeTable(dir: File) {
    tables.remove(dir.path)
  }

  private fun updateFromDirectory(
    directory: File,
    table: ResourceTable,
    options: TableExtractorOptions,
    logger: BlameLogger = BlameLogger(IDELogger)
  ) {
    directory.listFiles()?.forEach {
      if (it.isDirectory || it.extension != "xml") {
        return@forEach
      }

      updateFromFile(it, table, options, logger)
    }
  }

  private fun updateFromFile(
    it: File,
    table: ResourceTable,
    options: TableExtractorOptions,
    logger: BlameLogger
  ) {
    val pathData = extractPathData(it)
    if (pathData.extension != "xml") {
      // Cannot parse any other file types
      return
    }

    val extractor =
      TableExtractor(
        table = table,
        source = pathData.source,
        config = pathData.config,
        options = options,
        logger = logger
      )

    pathData.file.inputStream().use { stream ->
      try {
        extractor.extract(stream)
      } catch (err: Exception) {
        log.warn("Failed to compile ${pathData.file}", err.message)
      }
    }
  }

  override fun clear() {
    tables.clear()
  }
}
