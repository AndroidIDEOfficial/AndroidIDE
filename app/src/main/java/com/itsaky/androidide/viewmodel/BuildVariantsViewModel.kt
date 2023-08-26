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

package com.itsaky.androidide.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itsaky.androidide.tooling.api.models.BuildVariantInfo

/**
 * [ViewModel] for the build variants fragment.
 *
 * @author Akash Yadav
 */
class BuildVariantsViewModel : ViewModel() {

  internal val _buildVariants = MutableLiveData<Map<String, BuildVariantInfo>>(null)
  internal val _updatedBuildVariants = MutableLiveData<MutableMap<String, BuildVariantInfo>>(null)

  var buildVariants: Map<String, BuildVariantInfo>
    get() = this._buildVariants.value ?: emptyMap()
    set(value) {
      this._buildVariants.value = value
    }

  var updatedBuildVariants: MutableMap<String, BuildVariantInfo>
    get() = this._updatedBuildVariants.value ?: mutableMapOf()
    set(value) {
      this._updatedBuildVariants.value = value
    }

  /**
   * Resets the updated selections.
   */
  internal fun resetUpdatedSelections() {
    updatedBuildVariants = updatedBuildVariants.also { it.clear() }
  }
}