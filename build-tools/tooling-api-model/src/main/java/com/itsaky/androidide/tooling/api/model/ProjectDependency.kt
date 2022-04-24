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

package com.itsaky.androidide.tooling.api.model

import java.io.File

/**
 * A project dependency. Holds data about the jar, sources and javadoc files of a dependency. Also
 * some other useful metadata.
 *
 * If you do any changes here, please update IdeProjectCopier too.
 *
 * @author Akash Yadav
 */
class ProjectDependency {
    var group: String? = null
    var artifact: String? = null
    var version: String? = null
    var jar: File? = null
    var sourcesJar: File? = null
    var javadocJar: File? = null
}
