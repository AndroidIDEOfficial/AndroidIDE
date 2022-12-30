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

package com.itsaky.androidide.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Manages plugins applied on the IDE's project modules.
 *
 * @author Akash Yadav
 */
class AndroidIDEPlugin : Plugin<Project> {

  companion object {
    private const val APP_PLUGIN = "com.android.application"
    private const val LIB_PLUGIN = "com.android.library"
  }

  override fun apply(target: Project) {
    target.afterEvaluate {
      if (target.plugins.hasPlugin(APP_PLUGIN) || target.plugins.hasPlugin(LIB_PLUGIN)) {

        // apply the translation checker plugin
        target.apply { plugin(TranslationCheckerPlugin::class.java) }
      }

      // apply the Google Java Format plugin
      target.apply { plugin(GoogleJavaFormatPlugin::class.java) }
    }
  }
}
