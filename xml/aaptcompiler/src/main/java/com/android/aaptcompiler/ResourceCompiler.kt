/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JvmName("ResourceCompiler")

package com.android.aaptcompiler

import com.android.aaptcompiler.ResourceFile.Type.ProtoXml
import com.android.aaptcompiler.ResourceFile.Type.Unknown
import com.android.aaptcompiler.proto.serializeTableToPb
import com.itsaky.androidide.layoutlib.resources.ResourceType
import com.itsaky.androidide.layoutlib.resources.ResourceVisibility
import com.android.utils.FileUtils
import java.io.File

private const val VALUES_DIRECTORY_PREFIX = "values"
private const val XML_EXTENSION = "xml"
private const val RESOURCE_TABLE_EXTENSION = "arsc"
private const val PATCH_9_EXTENSION = "9.png"
private const val PNG_EXTENSION = "png"

/**
 * The different options available while compiling resources.
 *
 * @param generateSymbolsPathData Path where the R.txt file should be written to. If null, symbols
 *   will not be generated.
 * @param visibility The visibility of resources in the given file to process. If specified, then
 *   compilation of table files will fail if either the "public" or "public group" tags are
 *   encountered.
 * @param requirePngCrunching whether PNG file crunching is enabled
 * @param pseudolocalize Whether or not the pseudolocales en-XA and ar-XB are generated for default
 *   string resources.
 * @param partialRFile file to output symbols defined in the resource file (currently only
 *   supported for non-raw XML files (e.g. values or layouts) to match AAPT2's behaviour)
 * @param legacyMode Whether or not to error or warn on positional args not being specified in a
 *   translatable string with parameters.
 * @param verbose Whether or not to show verbose logs.
 * @param sourcePath Specified source path to be written in the compiled resource.
 */
data class ResourceCompilerOptions(
  val generateSymbolsPathData: ResourcePathData?= null,
  val visibility: ResourceVisibility? = null,
  val requirePngCrunching: Boolean = false,
  val pseudolocalize: Boolean = false,
  val partialRFile: File? = null,
  val legacyMode: Boolean = false,
  val verbose: Boolean = false,
  val sourcePath: String? = null)

/**
 * Shows whether the ResourceCompiler can compile the given Resource File.
 *
 * @param file The file to check
 * @return true if and only if the ResourceCompiler currently supports the ResourceFile.
 */
fun canCompileResourceInJvm(file: File, requirePngCrunching: Boolean): Boolean {
  // Hidden files, while skipped, are still supported.
  if (file.isHidden) return true

  val pathData = extractPathData(file)
  if (pathData.resourceDirectory == VALUES_DIRECTORY_PREFIX
    && pathData.extension == XML_EXTENSION) {
    // file is a values table.
    return true
  } else {
    val type = ResourceType.fromFolderName(pathData.resourceDirectory) ?: return false
    if (type != ResourceType.RAW) {
      if (pathData.extension == XML_EXTENSION) {
        return true
      } else if (pathData.extension.endsWith(PNG_EXTENSION)) {
        // If we don't need to perform patch9 processing or png crunching we can process.
        return pathData.extension != PATCH_9_EXTENSION && !requirePngCrunching
      }
    }
  }
  return true
}

/**
 * Compiles the given resource file and puts the compiled process in the given output directory.
 *
 * @param file file to processed. How it is processed is determined by the resource compiler.
 * @param options the [ResourceCompilerOptions] that should be considered while compiling the
 *   resource.
 *
 * Values files are compiled by extracting the all the resources and their values into a Resource
 * Table to be used in Linking.
 *
 * Xml resource files are compiled by extracting all new (@+) ids and extracting all inlined
 * aapt:attr attributes. The resulting xmls and aapt:attr are flattened.
 *
 * Png go through crunching (if not debug) and additional processing if the png is a patch-9 file.
 *
 * Lastly raw files are flattened straight to protocol buffers.
 *
 * See [canCompileResourceInJvm] to see what is supported.
 */
fun compileResource(
  file: File, outputDirectory: File, options: ResourceCompilerOptions, logger: BlameLogger
) {

  // Skip hidden files.
  if (file.isHidden) {
    logger.warning("Omitting file ${file.absolutePath} because it is hidden.")
    return
  }

  // Extract resource type information from the full path.
  val pathData = extractPathData(file, options.sourcePath ?: file.absolutePath)
  // Determine how to compile the file based on its type.
  val compileFunction = getCompileMethod(pathData, logger)

  try {
    compileFunction(pathData, outputDirectory, options, logger)
  } catch (e: Exception) {
    logger.info("Failed to compile file", blameSource(pathData.source))
      val message =
          "Resource compilation failed (${e.message}. Cause: ${e.cause}). " +
                  "Check logs for more details."
      throw ResourceCompilationException(message, e)
  }
}

private fun getCompileMethod(pathData: ResourcePathData, logger: BlameLogger):
    (ResourcePathData, File, ResourceCompilerOptions, BlameLogger) -> Unit {
  if (pathData.resourceDirectory == VALUES_DIRECTORY_PREFIX &&
      pathData.extension == XML_EXTENSION) {
    pathData.extension = RESOURCE_TABLE_EXTENSION
    return ::compileTable
  } else {
    val type = ResourceType.fromFolderName(pathData.resourceDirectory)
    if (type == null) {
      val errorMsg = "Invalid resource type '${pathData.resourceDirectory}'" +
        " for file ${pathData.file.absolutePath}"
      logger.warning(errorMsg)
      error(errorMsg)
    }
    if (type != ResourceType.RAW) {
      if (pathData.extension == XML_EXTENSION) {
        return ::compileXml
      } else if (pathData.extension.endsWith(PNG_EXTENSION)) {
        return ::compilePng
      }
    }
  }
  return ::compileFile
}

/**
 * Compiles a xml values file into a flattened [ResourceTable] proto.
 *
 * After all resources are extracted from the given xml, if pseudolocalization is set,
 * pseudolocale strings are created for each default string resource in the xml. The result is
 * flattened to a file in the output directory.
 *
 * @param pathData the file to be processed.
 * @param outputDirectory the directory to which the compiled file is to be placed.
 * @throws IllegalStateException A failure occurred in processing this resource.
 */
private fun compileTable(
  pathData: ResourcePathData,
  outputDirectory: File,
  options: ResourceCompilerOptions,
  logger: BlameLogger
) {
  val outputFile = File(outputDirectory, pathData.getIntermediateContainerFilename())
  logger.info("Compiling XML table ${pathData.file.absolutePath} to $outputFile")

  val table = ResourceTable(logger = logger)

  val extractorOptions = TableExtractorOptions(
    translatable = !pathData.name.contains("donottranslate"),
    errorOnPositionalArgs = !options.legacyMode,
    visibility = options.visibility)
  val tableExtractor =
    TableExtractor(table, pathData.source, pathData.config, extractorOptions, logger)

  pathData.file.inputStream().use {
      try {
          tableExtractor.extract(it)
      } catch (e: Exception) {
          // For merged values there's no need to re-write as we don't know which line failed. The
          // actual error will be raised by the table extractor.
          throw ResourceCompilationException(
              "Failed to compile values resource file ${pathData.file}", e
          )
      }

    // Adds the fake locales: en-XA and ar-XB for each default-defined string resource. This is used
    // for debugging apps with long text (en-XA) or rtl (ar-XB) language support.
    if (options.pseudolocalize && extractorOptions.translatable) {
      PseudolocaleGenerator().consume(table)
    }
  }

  // Ensure we have the compilation package at least.
  table.createPackage("")
  table.sort()

  val container = Container(outputFile.outputStream(), 1)
  val pbTable = serializeTableToPb(table)

  container.addResTableEntry(pbTable)

  if (options.partialRFile != null) {
    val builder = StringBuilder()
    table.packages.forEach { pkg ->
      // Only print resources defined locally, e.g. don't write android attributes
      if (pkg.name.isEmpty()) {
        pkg.groups.forEach { group ->
          // Macros do not define resources and therefore don't belong in the R.txt.
          if (group.type != AaptResourceType.MACRO) {
              val javaType = if (group.type == AaptResourceType.STYLEABLE) "int[]" else "int"
              group.entries.forEach { entry ->
                val visibility = getVisibility(entry.value.values, group.type)
                builder.appendLine("${visibility.getName()} $javaType ${group.type.tagName} ${entry.key}")
                if (group.type == AaptResourceType.STYLEABLE) {
                  // We also need to write down styleable children (entries)
                  group.getStyleable(entry).entries.forEach {
                    val childPackage =
                      if (!it.name.pck.isNullOrEmpty()) "_${it.name.pck}" else ""
                    // styleables and their children are always public
                    builder.appendLine("public int styleable ${entry.key}${childPackage}_${it.name.entry}")
                  }
                }
              }
          }
        }
      }
      FileUtils.writeToFile(options.partialRFile, builder.toString())
    }
  }
}

private fun getVisibility(values: Collection<ResourceEntry>, type: AaptResourceType): ResourceVisibility {
    // all declare styleables need to be marked as public so they're visible in the R classes
    if (type == AaptResourceType.STYLEABLE) return ResourceVisibility.PUBLIC
    var foundPublic = false
    var foundPrivate = false
    values.forEach {
        // We only care about public and private since only these can clash
        if (it.visibility.level == ResourceVisibility.PUBLIC) foundPublic = true
        else if (it.visibility.level == ResourceVisibility.PRIVATE) foundPrivate = true
    }
    if (foundPublic && foundPrivate)
        error("Resource cannot be both public and private: ${type.tagName} ${values.first().name}")
    if (foundPublic) return ResourceVisibility.PUBLIC
    if (foundPrivate) return ResourceVisibility.PRIVATE
    return ResourceVisibility.PRIVATE_XML_ONLY // default
}

/**
 * Compiles the raw file to proto, with no extra processing
 *
 * @param pathData the file to flatten.
 * @param outputDirectory the directory to which the compiled file is to be placed.
 */
private fun compileFile(
  pathData: ResourcePathData,
  outputDirectory: File,
  options: ResourceCompilerOptions,
  logger: BlameLogger?) {
  val outputFile = File(outputDirectory, pathData.getIntermediateContainerFilename())
  logger?.info("Compiling file ${pathData.file.absolutePath} to $outputFile")
  pathData.file.inputStream().use {
    val resourceFile = ResourceFile(
      ResourceName("", resourceTypeFromTag(pathData.resourceDirectory)!!, pathData.name),
      pathData.config,
      pathData.source,
      Unknown
    )

    val container = Container(outputFile.outputStream(), 1)
    container.addFileEntry(it, resourceFile)

    if (options.partialRFile != null) {
      val partialR = "default int ${pathData.type!!.tagName} ${pathData.name}"
      FileUtils.writeToFile(options.partialRFile, partialR)
    }
  }
}

/**
 * Compiles the XML resource file (such as layouts, drawables, ect. to a flattened proto file.
 *
 * This process has two primary steps:
 *
 * 1. Extract all new ids from the xml into a symbol table. "@+id/myView" for example.
 *
 * 2. Find and extract all inline (aapt:attr) XML resources. This includes assigning the declared
 * attribute on the (aapt:attr) to the generated xml resource name.
 *
 * After both these steps, the symbol table and both the extracted XMLs and modified original XML
 * are flattened to a single [Container] and written to the output directory.
 *
 * @param pathData the file to be processed.
 * @param outputDirectory the directory to which the compiled file is to be placed.
 * @throws IllegalStateException A failure occurred in processing this resource.
 */
private fun compileXml(
  pathData: ResourcePathData,
  outputDirectory: File,
  options: ResourceCompilerOptions,
  logger: BlameLogger?) {
  val outputFile = File(outputDirectory, pathData.getIntermediateContainerFilename())
  logger?.info("Compiling xml file ${pathData.file.absolutePath} to $outputFile")

  val fileToProcess = ResourceFile(
    ResourceName("", pathData.type!!, pathData.name),
    pathData.config,
    pathData.source,
    ProtoXml
  )

  pathData.file.inputStream().use {
    val xmlProcessor = XmlProcessor(source = pathData.source, logger = logger)
    try {
        xmlProcessor.process(fileToProcess, it)
    } catch (e: Exception) {
        throw ResourceCompilationException(
            "Failed to compile resource file: " +
                    logger?.getOutputSource(blameSource(pathData.source)),
            e
        )
    }

    val container =
      Container(outputFile.outputStream(), xmlProcessor.xmlResources.size)

    for (resource in xmlProcessor.xmlResources) {
      container.addXmlEntry(resource)
    }

    if (options.partialRFile != null) {
      val builder = StringBuilder()
      builder.appendLine("default int ${pathData.type!!.tagName} ${pathData.name}")
      fileToProcess.exportedSymbols.forEach { id ->
        builder.appendLine("default int id ${id.name.entry}")
      }
      FileUtils.writeToFile(options.partialRFile, builder.toString())
    }
  }
}

/**
 * Compiles the given png file.
 *
 * This is broken up into 2 primary steps:
 *
 * If the input is a patch9 file, (*.9.png), it will undergo patch9 processing.
 *
 * If we are not running in debug mode, the png will undergo additional crunching.
 *
 * Finally, the processed file is written to the output directory. It is possible that no
 * processing is done to the png file, and if so it will just be written exactly as if it were
 * a raw file.
 *
 * @param pathData the file to process.
 * @param outputDirectory the directory in which the processed file will be placed.
 */
private fun compilePng(
  pathData: ResourcePathData,
  outputDirectory: File,
  options: ResourceCompilerOptions,
  logger: BlameLogger?) {
  logger?.info("Compiling image file ${pathData.file.absolutePath}")
  if (pathData.extension == PATCH_9_EXTENSION) {
    error("Patch 9 PNG processing is not supported with the JVM Android resource compiler.")
  }
  if (options.requirePngCrunching) {
    error("PNG crunching is not supported with the JVM Android resource compiler.")
  }
  compileFile(pathData, outputDirectory, options, logger)
}
