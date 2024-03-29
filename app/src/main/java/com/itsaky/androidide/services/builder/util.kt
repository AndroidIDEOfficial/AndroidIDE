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

package com.itsaky.androidide.services.builder

import com.itsaky.androidide.preferences.internal.BuildPreferences
import com.itsaky.androidide.tooling.api.messages.GradleDistributionParams

/**
 * The distribution params. This considers [gradleInstallationDir] preference as well.
 */
val gradleDistributionParams: GradleDistributionParams
  get() {
    if (BuildPreferences.gradleInstallationDir.isBlank()) {
      return GradleDistributionParams.WRAPPER
    }

    return GradleDistributionParams.forInstallationDir(BuildPreferences.gradleInstallationDir)
  }