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

package com.itsaky.androidide.utils

import java.io.File
import java.io.PrintWriter
import java.io.StringReader
import java.util.Properties

object ListingFileRedirect {
  
  /**
   * Redirect file will have this marker as the first line as comment.
   */
  const val REDIRECT_MARKER = "#- File Locator -"
  
  /**
   * Property name in a [Properties] for the metadata file location.
   */
  const val REDIRECT_PROPERTY_NAME = "listingFile"
  
  /**
   * Redirect file name used when for the artifact.
   */
  const val REDIRECT_FILE_NAME = "redirect.txt"
  
  fun writeRedirect(listingFile: File, into: File) {
    val path = try {
      into.parentFile!!.toPath().relativize(listingFile.toPath()).toString()
    } catch(ex: IllegalArgumentException) {
      listingFile.canonicalPath
    }
    PrintWriter(into).use {
      it.println(REDIRECT_MARKER)
      it.println("${REDIRECT_PROPERTY_NAME}=${path.replace("\\", "/")}")
    }
  }
  
  private fun maybeExtractRedirectedFile(redirectFile: File, redirectFileContent: String? = null): File? {
    val fileContent = redirectFileContent ?: redirectFile.readText()
    return if (fileContent.startsWith(REDIRECT_MARKER)) {
      val fileLocator = Properties().also {
        it.load(StringReader(fileContent))
      }
      val file = File(fileLocator.getProperty(REDIRECT_PROPERTY_NAME))
      if(!file.isAbsolute)
        redirectFile.parentFile?.resolve(file)
      else
        file
    } else null
  }
  
  @JvmStatic
  fun getListingFile(inputFile: File) =
    maybeExtractRedirectedFile(inputFile) ?: inputFile
}

