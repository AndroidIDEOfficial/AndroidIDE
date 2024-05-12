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

import android.os.Parcel
import android.os.Parcelable

/**
 * The preferences for the IDE.
 *
 * @author Akash Yadav
 */
data object IDEPreferences : BaseIDEPreferences() {

  override val children: List<IPreference> = mutableListOf()
  override fun describeContents(): Int = 0
  override fun writeToParcel(dest: Parcel, flags: Int) {}
  
  @JvmField
  val CREATOR = object : Parcelable.Creator<IDEPreferences> {
    override fun createFromParcel(source: Parcel?): IDEPreferences {
      return IDEPreferences
    }
  
    override fun newArray(size: Int): Array<IDEPreferences> {
      return Array(size) { IDEPreferences }
    }
  }
}