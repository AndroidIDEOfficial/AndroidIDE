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

package com.itsaky.androidide.tooling.api.model.internal

import com.itsaky.androidide.tooling.api.model.IGradleProject
import com.itsaky.androidide.tooling.api.model.IGradleTask
import java.io.File

/**
 * Default implementation for [IGradleProject].
 * @author Akash Yadav
 */
open class DefaultGradleProject(
    override val name: String,
    override val description: String,
    override val path: String,
    override val projectDir: File,
    override val buildDir: File,
    override val buildScript: File,
    override val parent: IGradleProject?,
    override val subprojects: List<IGradleProject>,
    override val tasks: List<IGradleTask>,
) : IGradleProject {

    override val isAndroidModule: Boolean = false

    override fun findByPath(path: String): IGradleProject? {
        if (path == this.path) {
            return this
        }

        for (sub in subprojects) {
            val subSub = sub.findByPath(path)
            if (subSub != null) {
                return subSub
            }
        }

        return null
    }
}
