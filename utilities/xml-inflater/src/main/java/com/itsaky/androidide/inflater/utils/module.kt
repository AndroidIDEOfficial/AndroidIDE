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

package com.itsaky.androidide.inflater.utils

import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.projects.android.AndroidModule
import java.io.File

private var currentModule: AndroidModule? = null
var isParsing: Boolean = false
  private set

val module: AndroidModule
  get() =
    currentModule ?: throw IllegalStateException("You must call startParse(AndroidModule) first")

fun startParse(file: File) {
  if (isParsing) {
    return
  }
  (IProjectManager.getInstance().getWorkspace()?.findModuleForFile(file, false) as? AndroidModule)?.let {
    startParse(it)
  }
}

fun startParse(m: AndroidModule) {
  currentModule = m
  isParsing = true
}

fun endParse() {
  currentModule = null
  isParsing = false
}
