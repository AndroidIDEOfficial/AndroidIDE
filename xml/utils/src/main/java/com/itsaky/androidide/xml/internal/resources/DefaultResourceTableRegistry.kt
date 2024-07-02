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

package com.itsaky.androidide.xml.internal.resources

import com.android.SdkConstants
import com.android.SdkConstants.FN_INTENT_ACTIONS_ACTIVITY
import com.android.SdkConstants.FN_INTENT_ACTIONS_BROADCAST
import com.android.SdkConstants.FN_INTENT_ACTIONS_SERVICE
import com.android.SdkConstants.FN_INTENT_CATEGORIES
import com.android.SdkConstants.OS_PLATFORM_ATTRS_MANIFEST_XML
import com.android.aaptcompiler.AaptResourceType
import com.android.aaptcompiler.BlameLogger
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.ResourceName
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.Source
import com.android.aaptcompiler.TableExtractor
import com.android.aaptcompiler.TableExtractorOptions
import com.android.aaptcompiler.extractPathData
import com.google.auto.service.AutoService
import com.itsaky.androidide.aapt.logging.IDELogger
import com.itsaky.androidide.layoutlib.resources.ResourceVisibility.PUBLIC
import com.itsaky.androidide.xml.internal.resources.DefaultResourceTableRegistry.SingleLineValueEntryType.ACTIVITY_ACTIONS
import com.itsaky.androidide.xml.internal.resources.DefaultResourceTableRegistry.SingleLineValueEntryType.BROADCAST_ACTIONS
import com.itsaky.androidide.xml.internal.resources.DefaultResourceTableRegistry.SingleLineValueEntryType.CATEGORIES
import com.itsaky.androidide.xml.internal.resources.DefaultResourceTableRegistry.SingleLineValueEntryType.FEATURES
import com.itsaky.androidide.xml.internal.resources.DefaultResourceTableRegistry.SingleLineValueEntryType.SERVICE_ACTIONS
import com.itsaky.androidide.xml.res.IResourceTable
import com.itsaky.androidide.xml.resources.ResourceTableRegistry
import com.itsaky.androidide.xml.resources.ResourceTableRegistry.Companion.PCK_ANDROID
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation of the [ResourceTableRegistry].
 *
 * @author Akash Yadav
 */
@AutoService(ResourceTableRegistry::class)
class DefaultResourceTableRegistry : ResourceTableRegistry {

  /**
   * Represents the type of single line entries read from files.
   *
   * @property filename The filename which contains the single-line entries.
   */
  internal enum class SingleLineValueEntryType(val filename: String) {

    ACTIVITY_ACTIONS(FN_INTENT_ACTIONS_ACTIVITY),
    BROADCAST_ACTIONS(FN_INTENT_ACTIONS_BROADCAST),
    SERVICE_ACTIONS(FN_INTENT_ACTIONS_SERVICE),
    CATEGORIES(FN_INTENT_CATEGORIES),
    FEATURES("features.txt")
  }

  private val tables = ConcurrentHashMap<String, ResourceTable>()
  private val platformTables = ConcurrentHashMap<String, ResourceTable>()
  private val manifestAttrs = ConcurrentHashMap<String, ResourceTable>()
  private val singleLineValueEntries =
    ConcurrentHashMap<String, ConcurrentHashMap<SingleLineValueEntryType, List<String>>>()

  companion object {

    private val log = LoggerFactory.getLogger(DefaultResourceTableRegistry::class.java)
  }

  override var isLoggingEnabled: Boolean = true

  override fun forPackage(name: String, vararg resDirs: File): ResourceTable? {

    if (name == PCK_ANDROID) {
      return platformResourceTable(resDirs.iterator().next())
    }

    return tables[name]
      ?: createTable(*resDirs)?.also {
        tables[name] = it
        it.packages.firstOrNull()?.name = name

        resDirs.forEach { resDir ->
          addFileReferences(it, name, resDir)
        }
      }
  }

  override fun forPlatformDir(platform: File): IResourceTable? {
    getManifestAttrTable(platform)
    getActivityActions(platform)
    getBroadcastActions(platform)
    getServiceActions(platform)
    getCategories(platform)
    getFeatures(platform)
    return super.forPlatformDir(platform)
  }

  override fun getManifestAttrTable(platform: File): ResourceTable? {
    return manifestAttrs[platform.path]
      ?: createManifestAttrTable(platform)?.also {
        manifestAttrs[platform.path] = it
        it.packages.firstOrNull()?.name = PCK_ANDROID
      }
  }

  override fun getActivityActions(platform: File): List<String> {
    return getSingleLineEntry(platform, ACTIVITY_ACTIONS)
  }

  override fun getBroadcastActions(platform: File): List<String> {
    return getSingleLineEntry(platform, BROADCAST_ACTIONS)
  }

  override fun getServiceActions(platform: File): List<String> {
    return getSingleLineEntry(platform, SERVICE_ACTIONS)
  }

  override fun getCategories(platform: File): List<String> {
    return getSingleLineEntry(platform, CATEGORIES)
  }

  override fun getFeatures(platform: File): List<String> {
    return getSingleLineEntry(platform, FEATURES)
  }

  override fun removeTable(packageName: String) {
    tables.remove(packageName)
  }

  override fun clear() {
    tables.clear()
  }

  private fun getSingleLineEntry(platform: File, type: SingleLineValueEntryType): List<String> {
    var entries = singleLineValueEntries[platform.path]
    if (entries == null) {
      entries = readSingleLineEntry(platform, type)
      singleLineValueEntries[platform.path] = entries
    }

    return entries[type]
      ?: run {
        readSingleLineEntriesTo(platform, type, entries)
        entries[type] ?: emptyList()
      }
  }

  private fun readSingleLineEntry(
    platform: File,
    type: SingleLineValueEntryType
  ): ConcurrentHashMap<SingleLineValueEntryType, List<String>> {
    val map = ConcurrentHashMap<SingleLineValueEntryType, List<String>>()
    readSingleLineEntriesTo(platform, type, map)
    return map
  }

  private fun readSingleLineEntriesTo(
    platform: File,
    type: SingleLineValueEntryType,
    map: ConcurrentHashMap<SingleLineValueEntryType, List<String>>
  ) {
    val file = File(platform, "${SdkConstants.FD_DATA}/${type.filename}")
    if (!file.exists() || !file.canRead()) {
      return
    }

    map[type] = file.readLines()
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
      ?: createTable(dir)?.also { table ->
        platformTables[dir.path] = table
        table.packages.firstOrNull()?.name = PCK_ANDROID

        addFileReferences(table, PCK_ANDROID, dir)
      }
  }

  private fun createTable(vararg resDirs: File): ResourceTable? {
    if (resDirs.isEmpty()) {
      return null
    }

    if (isLoggingEnabled) {
      log.info("Creating resource table for {} resource directories", resDirs.size)
    }

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

  private fun addFileReferences(table: ResourceTable, pck: String, resDir: File) {
    resDir.listFiles()?.forEach { dir ->
      if (dir.name.startsWith(SdkConstants.FD_RES_VALUES)) {
        return@forEach
      }

      dir.listFiles()?.forEach { file ->
        var typeName = dir.name
        if (typeName.contains('-')) {
          typeName = typeName.substringBefore('-')
        }

        val type = try {
          AaptResourceType.valueOf(typeName.uppercase())
        } catch (error: Exception) {
          if (isLoggingEnabled) {
            log.warn("Unknown resource type: {} :: {}", typeName.uppercase(), error.message)
          }
          AaptResourceType.UNKNOWN
        }
        val resName = ResourceName(pck, type, file.nameWithoutExtension)
        table.addFileReference(
          resName, ConfigDescription(),
          Source(file.path), file.path
        )
      }
    }
  }

  private fun getDefaultOptions(): TableExtractorOptions {
    return TableExtractorOptions(
      translatable = true, errorOnPositionalArgs = false,
      visibility = PUBLIC
    )
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
        if (isLoggingEnabled) {
          log.warn("Failed to compile {}", pathData.file)
        }
      }
    }
  }
}
