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

package com.itsaky.androidide.templates.impl.noAndroidXActivity

import com.itsaky.androidide.templates.base.modules.android.defaultAppModule
import com.itsaky.androidide.templates.base.util.AndroidModuleResManager
import com.itsaky.androidide.templates.impl.R
import com.itsaky.androidide.templates.impl.base.createRecipe
import com.itsaky.androidide.templates.impl.base.emptyThemesAndColors
import com.itsaky.androidide.templates.impl.base.emptyValuesFile
import com.itsaky.androidide.templates.impl.base.writeMainActivity
import com.itsaky.androidide.templates.impl.baseProjectImpl

fun noAndroidXActivityProject() = baseProjectImpl {
  templateName = R.string.template_no_AndroidX
  thumb = R.drawable.template_empty_noandroidx
  defaultAppModule(addAndroidX = false) {
    recipe = createRecipe {
      emptyThemesAndColors()
      res {
        writeXmlResource("colors",
          AndroidModuleResManager.ResourceType.VALUES, source = emptyValuesFile())
        writeXmlResource("themes", AndroidModuleResManager.ResourceType.VALUES,
          source = noAndroidXTheme())

        writeXmlResource("activity_main", AndroidModuleResManager.ResourceType.LAYOUT,
          source = noAndroidXActivityLayout())
      }

      sources {
        writeMainActivity(this, ktSrc = ::noAndroidXActivitySrcKt,
          javaSrc = ::noAndroidXActivitySrcJava)
      }
    }
  }
}