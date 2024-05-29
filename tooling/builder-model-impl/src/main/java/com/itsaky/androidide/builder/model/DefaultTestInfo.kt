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

package com.itsaky.androidide.builder.model

import com.android.builder.model.v2.ide.TestInfo
import com.android.builder.model.v2.ide.TestInfo.Execution
import java.io.File
import java.io.Serializable

/** @author Akash Yadav */
class DefaultTestInfo : TestInfo, Serializable {
  private val serialVersionUID = 1L
  override var additionalRuntimeApks: Collection<File> = emptyList()
  override var animationsDisabled: Boolean = false
  override var execution: Execution? = null
  override var instrumentedTestTaskName: String = ""
}
