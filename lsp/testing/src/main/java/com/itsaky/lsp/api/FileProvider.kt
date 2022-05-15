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
package com.itsaky.lsp.api

import com.google.common.truth.Truth.assertThat
import org.bouncycastle.crypto.agreement.srp.SRP6Client
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Test

/**
 * Provides paths to source files in 'resources' directory.
 *
 * @author Akash Yadav
 */
class FileProvider {
    @Test
    fun testPath() {
        val path = sourceFile("SourceFileTest")
        assertThat(Files.exists(path)).isTrue()
        assertThat(path.fileName.toString()).isEqualTo("SourceFileTest.java")
    }

    @Test
    fun testNested() {
        val path = sourceFile("package/SourceFileTest")
        assertThat(Files.exists(path)).isTrue()
        assertThat(path.fileName.toString()).isEqualTo("SourceFileTest.java")
    }

    @Test
    fun testExtension() {
        val folder = File(".").canonicalFile

        assertThat(extension).isNotEmpty()
        assertThat(extension)
            .isEqualTo(
                when (folder.name) {
                    "xml" -> "xml"
                    "java" -> "java"
                    else -> ""
                })
    }

    companion object {

        @JvmField
        val extension = run {
            val file = File(".").canonicalFile
            when (file.name) {
                "xml" -> "xml" // Testing in ':lsp:xml' module
                "java" -> "java" // Testing in ':lsp:java' module
                else -> ""
            }
        }

        /**
         * Get the path to the 'resources' directory.
         *
         * @return The the resources directory.
         */
        @JvmStatic
        fun resources(): Path {
            return Paths.get(".", "src", "test", "resources")
        }

        /**
         * Get the path to the file in resources.
         *
         * @param name The name of the file. Nested file paths can be separated using '/'.
         * @return The path to the file.
         */
        @JvmStatic
        fun sourceFile(name: String): Path {
            return resources().resolve("${name}_template.$extension")
        }

        @JvmStatic fun contents(file: Path): StringBuilder = StringBuilder(file.readText())
    }
}
