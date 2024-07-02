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

package com.android.aaptcompiler

import java.io.File
import java.util.Locale

/**
 * Class for keeping track of Resource File of any kind.
 *
 * @param source The original source of this file.
 * @param extension The extension of the file as recognized by the Kotlin Resource Compiler. The
 *   extension will always be the lowercase of the original extension.
 * @param name The name of the file.
 * @param resourceDirectory The generic name for the directory type that this resource was found in.
 *   i.e. ".../values-en/strings.xml" would have a [resourceDirectory] or "values"
 * @param configString the configuration associated with this resource.
 */
data class ResourcePathData(
  var source: Source,
  var extension: String,
  var name: String,
  var resourceDirectory: String,
  // Keep the original and append extra configs at the end.
  var configString: String,
  var file: File,
  var config: ConfigDescription
) {
  val type: AaptResourceType? = resourceTypeFromTag(resourceDirectory)

  fun getIntermediateContainerFilename(): String {
    return compilationRename(file)
  }
}

fun extractPathData(file: File, sourcePath: String = file.absolutePath) : ResourcePathData {
  val extension = if (file.name.lowercase().endsWith(".9.png")) {
    "9.png"
  } else {
    file.name.substringAfterLast(".", "")
  }
  // Again the file name can contain multiple dots, and also because of the '.9.png' we can't just
  // get substringBeforeLast('.').
  val resName =
    if (extension.isNotEmpty()) file.name.substringBefore(".$extension")
    else file.name
  val source = Source(sourcePath)
  // TODO(b/142481190): think about obfuscation
  val parentName = file.parentFile!!.name
  val type = parentName.substringBefore("-")
  val config = if (type != parentName) {
    file.parentFile!!.name.substringAfter(type).substring(1)
  } else {
    ""
  }

  val configDescription = parse(config)

  return ResourcePathData(
    source, extension.lowercase(Locale.getDefault()), resName, type, config, file, configDescription
  )
}

/**
 * Obtains the renaming for compilation for the given file. When compiling a file, `aapt2`
 * will output a file with a name that depends on the file being compiled, as well as its path.
 * This method will compute what the output name is for a given input.
 *
 * @param f the file
 * @return the new file's name (this will take the file's path into consideration)
 * @throws IllegalStateException cannot analyze file path
 */
internal fun compilationRename(f: File): String {
  var fileName = f.name

  val fileParent = f.parentFile
    ?: error("Could not get parent of file '" + f.absolutePath + "'")

  val parentName = fileParent.name

  // Split fileName into fileName and ext. If fileName does not have an extension, make ext empty.
  val extIdx = fileName.lastIndexOf('.')
  var ext = if (extIdx == -1) "" else fileName.substring(extIdx)
  fileName = if (extIdx == -1) fileName else fileName.substring(0, extIdx)

  // Values are compiled to arsc. This mirrors the implementation of the Compile method in
  // frameworks/base/tools/aapt2/compile/Compile.cpp
  // e.g. values/strings.xml becomes values_strings.arsc.flat and not values_strings.xml.flat.
  if (parentName.startsWith("values") && ext == ".xml") {
    ext = ".arsc"
  }

  return parentName + "_" + fileName + ext + ".flat"
}
