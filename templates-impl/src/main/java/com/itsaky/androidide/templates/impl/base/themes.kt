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

import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.android.ResTableConfig
import com.itsaky.androidide.templates.base.AndroidModuleTemplateBuilder
import com.itsaky.androidide.templates.base.util.AndroidModuleResManager.ResourceType.VALUES

internal fun simpleMaterial3Theme(themeName: String, actionBar: Boolean = false
): String {
  return """
<resources xmlns:tools="http://schemas.android.com/tools">
  <!-- Base application theme. -->
  <style name="Base.${themeName}" parent="Theme.Material3.DayNight${if (!actionBar) ".NoActionBar" else ""}">
    <!-- Customize your theme here. -->
    <!-- <item name="colorPrimary">@color/my_light_primary</item> -->
  </style>

  <style name="$themeName" parent="Base.${themeName}" />
</resources>
  """.trim()
}

internal fun AndroidModuleTemplateBuilder.emptyThemesAndColors(
  actionBar: Boolean = false
) {
  val configNight = ConfigDescription().apply {
    uiMode = ResTableConfig.UI_MODE.NIGHT_YES
  }

  res.apply {
    // values
    writeXmlResource("themes", VALUES,
      source = simpleMaterial3Theme(manifest.themeRes, actionBar))
    writeXmlResource("colors", VALUES, source = emptyValuesFile())

    // values-night
    writeXmlResource("themes", VALUES, config = configNight,
      source = simpleMaterial3Theme(manifest.themeRes, actionBar))
    writeXmlResource("colors", VALUES, config = configNight,
      source = emptyValuesFile())
  }

}