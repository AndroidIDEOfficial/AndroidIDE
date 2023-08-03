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

package com.itsaky.androidide.tooling.api.models.result

/**
 * Result sent to the client when selecting a project in [IProjectQueries][com.itsaky.androidide.tooling.api.IProjectQueries].
 *
 * @param isSuccessful Whether the project selection was successful. If the value is `false`, it is
 *      because the project with the provided path was not found.
 * @author Akash Yadav
 */
data class SelectProjectResult(val isSuccessful: Boolean)