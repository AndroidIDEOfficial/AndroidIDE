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

package com.itsaky.androidide.tooling.impl.util

import com.itsaky.androidide.utils.ILogger
import java.io.File

/**
 * Handles the init script required by the tooling API.
 *
 * @author Akash Yadav
 */
object InitScriptHandler {

    private val log = ILogger.newInstance(javaClass.simpleName)
    private const val initScript = ".androidide/init/androidide.init.gradle"

    fun getInitScript(): File {
        val file = File(getHome(), initScript)
        return if (file.exists()) {
            file
        } else {
            writeInitScript()
            file
        }
    }

    private fun writeInitScript() {
        val script = File(getHome(), initScript)
        script.writeBytes("project.rootProject.afterEvaluate { subprojects { apply plugin: 'idea' } }".toByteArray())
    }

    private fun getHome(): String {
        return if (isJvm()) {
            // Probably running a test
            "./src/test/test-home"
        } else {
            System.getenv("HOME")
        }
    }

    private fun isJvm(): Boolean {
        return try {
            Class.forName("android.content.Context")
            false
        } catch (e: ClassNotFoundException) {
            true
        }
    }
}
