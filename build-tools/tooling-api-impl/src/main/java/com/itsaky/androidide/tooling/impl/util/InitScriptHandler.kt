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
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Handles the init script required by the tooling API.
 *
 * @author Akash Yadav
 */
object InitScriptHandler {

    private val log = ILogger.newInstance(javaClass.simpleName)
    private const val initDir = ".androidide/init"
    private var modelsJar = File(".")

    fun getInitScript(): File {
        // Assuming that the HOME variable is always set in AndroidIDE.
        val home = getHome()
        val script = File(home, "$initDir/init.gradle")
        this.modelsJar = File(home, "$initDir/model.jar")

        log.debug("Init script:: $script", "models:$modelsJar")

        return if (script.exists() && !isJvm()) {
            script
        } else {
            writeInitScript(script)
        }
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

    private fun writeInitScript(script: File): File {
        var contents =
            """
            import com.itsaky.androidide.tooling.plugin.AndroidIDEToolingApiPlugin
            
            initscript {
                dependencies {
                    classpath files ("${modelsJar.absolutePath}")
                }
            }
            
            allprojects {
                apply plugin: AndroidIDEToolingApiPlugin
            }
        """.trimIndent()

        if (File.separatorChar == '\\') { // If testing on Windows
            contents = contents.replace('\\', '/')
        }

        script.parentFile.mkdirs()
        val out = FileOutputStream(script)
        val input = ByteArrayInputStream(contents.toByteArray())
        val data = ByteArray(1024)

        var read = input.read(data)
        while (read != -1) {
            out.write(data, 0, read)
            read = input.read(data)
        }

        input.close()
        out.close()

        return script
    }
}
