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

package com.itsaky.androidide.models

import androidx.annotation.DrawableRes
import com.itsaky.androidide.resources.R
import java.io.File

/**
 * Info about file extensions in the file tree view.
 *
 * @author Akash Yadav
 */
enum class FileExtension(val extension: String, @DrawableRes val icon: Int) {
  JAVA("java", R.drawable.ic_language_java),
  JAR("jar", R.drawable.ic_language_java),
  KT("kt", R.drawable.ic_language_kotlin),
  KTS("kts", R.drawable.ic_language_kotlin),
  XML("xml", R.drawable.ic_language_xml),
  GRADLE("gradle", R.drawable.ic_gradle),
  JSON("json", R.drawable.ic_language_json),
  PROPERTIES("properties", R.drawable.ic_language_properties),
  APK("apk", R.drawable.ic_file_apk),
  TXT("txt", R.drawable.ic_file_txt),
  LOG("log", R.drawable.ic_file_txt),
  CPP("cpp", R.drawable.ic_language_cpp),
  H("h", R.drawable.ic_language_cpp),
  UNKNOWN("", R.drawable.ic_file_unknown);

  /** Factory class for getting [FileExtension] instances. */
  class Factory {
    companion object {

      /** Get [FileExtension] for the given file. */
      @JvmStatic
      fun forFile(file: File?): FileExtension {
        return forExtension(file?.extension)
      }

      /** Get [FileExtension] for the given extension. */
      @JvmStatic
      fun forExtension(extension: String?): FileExtension {
        if (extension == null) {
          return UNKNOWN
        }
        
        for (value in values()) {
          if (value.extension == extension) {
            return value
          }
        }

        return UNKNOWN
      }
    }
  }
}
