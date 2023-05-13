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

package com.itsaky.androidide.templates.impl.emptyActivity

import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.android.ResTableConfig
import com.itsaky.androidide.templates.Language
import com.itsaky.androidide.templates.ProjectTemplate
import com.itsaky.androidide.templates.base.AndroidModuleTemplateBuilder
import com.itsaky.androidide.templates.base.baseProject
import com.itsaky.androidide.templates.base.modules.android.ManifestActivity
import com.itsaky.androidide.templates.base.modules.android.defaultAppModule
import com.itsaky.androidide.templates.base.util.AndroidModuleResManager
import com.itsaky.androidide.templates.base.util.AndroidModuleResManager.ResourceType.LAYOUT
import com.itsaky.androidide.templates.base.util.AndroidModuleResManager.ResourceType.VALUES
import com.itsaky.androidide.templates.base.util.SourceWriter
import com.itsaky.androidide.templates.impl.base.emptyValuesFile
import com.itsaky.androidide.templates.impl.base.simpleMaterial3Theme

fun emptyActivityProject(): ProjectTemplate = baseProject {
  defaultAppModule {
    recipe = {
      sources {
        writeEmptyActivity(this)
      }

      res {
        writeEmptyActivity(this)
      }
    }
  }
}

internal fun AndroidModuleTemplateBuilder.writeEmptyActivity(
  resManager: AndroidModuleResManager
) {
  resManager.apply {
    val configNight = ConfigDescription().apply {
      uiMode = ResTableConfig.UI_MODE.NIGHT_YES
    }

    // layout/activity_main.xml
    writeXmlResource("activity_main", LAYOUT, source = ::emptyLayoutSrc)

    // values
    writeXmlResource("themes", VALUES,
      source = simpleMaterial3Theme(manifest.themeRes))
    writeXmlResource("colors", VALUES, source = emptyValuesFile())

    // values-night
    writeXmlResource("themes", VALUES, config = configNight,
      source = simpleMaterial3Theme(manifest.themeRes))
    writeXmlResource("colors", VALUES, config = configNight,
      source = emptyValuesFile())
  }
}

internal fun AndroidModuleTemplateBuilder.writeEmptyActivity(
  writer: SourceWriter
) {
  val className = "MainActivity"
  writer.apply {
    if (data.language == Language.Kotlin) {
      writeKtSrc(data.packageName, className,
        source = ::emptyActivityKtSrc)
    } else {
      writeJavaSrc(packageName = data.packageName, className = className,
        source = ::emptyActivityJavaSrc)
    }
  }

  manifest {
    addActivity(ManifestActivity(name = className, isExported = true, isLauncher = true))
  }
}