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

package com.itsaky.androidide.templates.impl.base

import com.itsaky.androidide.templates.Language
import com.itsaky.androidide.templates.base.AndroidModuleTemplateBuilder
import com.itsaky.androidide.templates.base.modules.android.ManifestActivity
import com.itsaky.androidide.templates.base.util.SourceWriter
import com.itsaky.androidide.templates.base.util.withXmlDecl

internal inline fun AndroidModuleTemplateBuilder.writeMainActivity(
  writer: SourceWriter, crossinline ktSrc: () -> String, crossinline javaSrc: () -> String
) {
  val className = "MainActivity"
  writer.apply {
    if (data.language == Language.Kotlin) {
      val src = ktSrc()
      if (src.isNotBlank()) {
        writeKtSrc(data.packageName, className, source = src)
      }
    } else {
      val src = javaSrc()
      if (src.isNotBlank()) {
        writeJavaSrc(packageName = data.packageName, className = className,
          source = src)
      }
    }
  }

  manifest {
    addActivity(
      ManifestActivity(name = className, isExported = true, isLauncher = true))
  }
}

internal fun emptyValuesFile(): String {
  return """
<resources></resources>
  """.trim().withXmlDecl()
}