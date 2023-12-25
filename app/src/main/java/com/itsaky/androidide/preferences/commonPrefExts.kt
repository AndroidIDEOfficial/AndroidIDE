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

package com.itsaky.androidide.preferences

import android.content.Context
import kotlinx.parcelize.IgnoredOnParcel
import kotlin.reflect.KMutableProperty0

internal abstract class PropertyBasedMultiChoicePreference : MultiChoicePreference() {

  @IgnoredOnParcel
  private val choices by lazy { getProperties() }

  abstract fun getProperties(): Map<String, KMutableProperty0<Boolean>>

  override fun getCheckedItems(choices: Array<String>): BooleanArray {
    return BooleanArray(choices.size) { this.choices[choices[it]]?.get() == true }
  }

  override fun getChoices(context: Context): Array<String> {
    return choices.keys.toTypedArray()
  }

  override fun onChoicesConfirmed(selectedPositions: IntArray, selections: Map<String, Boolean>) {
    selections.forEach { (key, value) ->
      choices[key]?.set(value)
    }
  }
}