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
 * Copyright (C) 2020 The Android Open Source Project
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

import com.android.ide.common.blame.SourceFilePosition
import com.android.ide.common.blame.SourcePosition
import com.android.utils.ILogger
import jaxp.xml.stream.Location
import java.io.File

fun blameSource(
  source: Source,
  line: Int? = source.line,
  column: Int? = null
): BlameLogger.Source =
  BlameLogger.Source(source.path, line ?: -1, column ?: -1)

fun blameSource(
  source: Source,
  location: Location
): BlameLogger.Source =
  BlameLogger.Source(source.path, location.lineNumber, location.columnNumber)

class BlameLogger(
  val logger: ILogger,
  private val userVisibleSourceTransform: (String) -> String,
  val blameMap: (Source) -> Source = { it }
) {

  constructor(logger: ILogger, blameMap: (Source) -> Source = { it })
    : this(logger, { it }, blameMap)

  data class Source(
    val sourcePath: String,
    val line: Int = -1,
    val column: Int = -1
  ) {

    override fun toString(): String {
      var result = sourcePath
      if (line != -1) {
        result += ":$line"
        if (column != -1) {
          result += ":$column"
        }
      }
      return "$result: "
    }

    fun toSourceFilePosition() =
      SourceFilePosition(
        File(sourcePath), SourcePosition(
          line,
          column,
          -1,
          line,
          column,
          -1
        )
      )

    companion object {
      fun fromSourceFilePosition(filePosition: SourceFilePosition) =
        Source(
          filePosition.file.sourcePath!!,
          filePosition.position.startLine,
          filePosition.position.startColumn
        )
    }
  }

  fun error(message: String, source: Source? = null, throwable: Throwable? = null) {
    if (source != null) {
      logger.error(throwable, "${getOutputSource(source)}$message")
    } else {
      logger.error(throwable, message)
    }
  }

  fun warning(message: String, source: Source? = null) {
    if (source != null)
      logger.warning("${getOutputSource(source)}$message")
    else
      logger.warning(message)
  }

  fun info(message: String, source: Source? = null) {
    if (source != null)
      logger.info("${getOutputSource(source)}$message")
    else
      logger.info(message)
  }

  fun lifecycle(message: String, source: Source? = null) {
    if (source != null)
      logger.lifecycle("${getOutputSource(source)}$message")
    else
      logger.lifecycle(message)
  }

  fun quiet(message: String, source: Source? = null) {
    if (source != null)
      logger.quiet("${getOutputSource(source)}$message")
    else
      logger.quiet(message)
  }

  fun verbose(message: String, source: Source? = null) {
    if (source != null)
      logger.verbose("${getOutputSource(source)}$message")
    else
      logger.verbose(message)
  }

  fun getOutputSource(source: Source): Source {
    return getOriginalSource(
      source.copy(sourcePath = userVisibleSourceTransform(source.sourcePath))
    )
  }

  fun getOriginalSource(source: Source): Source {
    return blameMap(source)
  }
}
