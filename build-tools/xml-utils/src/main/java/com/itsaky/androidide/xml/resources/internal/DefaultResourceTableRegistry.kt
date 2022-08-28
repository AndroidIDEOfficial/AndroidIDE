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

package com.itsaky.androidide.xml.resources.internal

import com.android.SdkConstants.OS_PLATFORM_ATTRS_MANIFEST_XML
import com.android.aaptcompiler.BlameLogger
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.TableExtractor
import com.android.aaptcompiler.TableExtractorOptions
import com.android.aaptcompiler.extractPathData
import com.itsaky.androidide.aapt.logging.IDELogger
import com.itsaky.androidide.layoutlib.resources.ResourceVisibility.PUBLIC
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.xml.resources.ResourceTableRegistry
import com.itsaky.androidide.xml.resources.ResourceTableRegistry.Companion.PCK_ANDROID
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation of the [ResourceTableRegistry].
 *
 * @author Akash Yadav
 */
internal object DefaultResourceTableRegistry : ResourceTableRegistry {

  private val log = ILogger.newInstance(javaClass.simpleName)

  private val tables = ConcurrentHashMap<String, ResourceTable>()
  private val platformTables = ConcurrentHashMap<String, ResourceTable>()
  private val manifestAttrs = ConcurrentHashMap<String, ResourceTable>()

  override fun forPackage(name: String, vararg dirs: File): ResourceTable? {

    if (name == PCK_ANDROID) {
      return platformResourceTable(dirs.iterator().next())
    }

    return tables[name]
      ?: createTable(*dirs)?.also {
        tables[name] = it
        it.packages.firstOrNull()?.name = name
      }
  }

  override fun forPlatformDir(platform: File): ResourceTable? {
    getManifestAttrTable(platform)
    return super.forPlatformDir(platform)
  }

  override fun getManifestAttrTable(platform: File): ResourceTable? {
    return manifestAttrs[platform.path]
      ?: createManifestAttrTable(platform)?.also {
        manifestAttrs[platform.path] = it
        it.packages.firstOrNull()?.name = PCK_ANDROID
      }
  }

  private fun createManifestAttrTable(platform: File): ResourceTable? {
    val attrs = File(platform, OS_PLATFORM_ATTRS_MANIFEST_XML)
    if (!attrs.exists()) {
      return null
    }

    val logger = BlameLogger(IDELogger)
    val table = ResourceTable(logger = logger)
    val options = getDefaultOptions()
    extractTable(attrs, table, options, logger)

    return table
  }

  private fun platformResourceTable(dir: File): ResourceTable? {
    return platformTables[dir.path]
      ?: createTable(dir)?.also {
        platformTables[dir.path] = it
        it.packages.firstOrNull()?.name = PCK_ANDROID
      }
  }

  private fun createTable(vararg resDirs: File): ResourceTable? {
    if (resDirs.isEmpty()) {
      return null
    }

    log.info("Creating resource table for resource directories $resDirs")

    val logger = BlameLogger(IDELogger)
    val table = ResourceTable()
    val options = getDefaultOptions()

    for (resDir in resDirs) {
      val values = File(resDir, "values")
      if (!values.exists() || !values.isDirectory) {
        continue
      }
      updateFromDirectory(values, table, options, logger)
    }

    return table
  }

  private fun getDefaultOptions(): TableExtractorOptions {
    val options =
      TableExtractorOptions(translatable = true, errorOnPositionalArgs = false, visibility = PUBLIC)
    return options
  }

  override fun removeTable(packageName: String) {
    tables.remove(packageName)
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

    if (it.path.endsWith(OS_PLATFORM_ATTRS_MANIFEST_XML)) {
      // This is stored in another resource table
      return
    }

    extractTable(it, table, options, logger)
    return
  }

  private fun extractTable(
    file: File,
    table: ResourceTable,
    options: TableExtractorOptions,
    logger: BlameLogger
  ) {
    val pathData = extractPathData(file)
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
