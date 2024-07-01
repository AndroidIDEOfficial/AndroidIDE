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

package com.itsaky.androidide.projects.android

import com.itsaky.androidide.projects.IWorkspace

/**
 * Get a sequence of all Android application projects in this workspace.
 */
fun IWorkspace.androidAppProjects() = androidProjects().filterIsAndroidApp()

/**
 * Get a sequence of all Android library projects in this workspace.
 */
fun IWorkspace.androidLibraryProjects() = androidProjects().filterIsAndroidLib()

/**
 * Returns a sequence containing only Android application projects.
 */
fun Sequence<AndroidModule>.filterIsAndroidApp() = filter(AndroidModule::isApplication)

/**
 * Returns a sequence containing only Android library projects.
 */
fun Sequence<AndroidModule>.filterIsAndroidLib() = filter(AndroidModule::isApplication)