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

package com.itsaky.androidide.aaptcompiler

import com.android.aaptcompiler.BlameLogger
import com.android.aaptcompiler.ResourceCompilerOptions
import com.android.aaptcompiler.ResourceFile
import com.android.aaptcompiler.ResourceFile.Type.ProtoXml
import com.android.aaptcompiler.ResourceName
import com.android.aaptcompiler.XmlProcessor
import com.android.aaptcompiler.compileResource
import com.android.aaptcompiler.extractPathData
import com.android.utils.StdLogger
import com.android.utils.StdLogger.Level.VERBOSE
import com.itsaky.androidide.utils.FileProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.nio.file.Paths
import kotlin.io.path.absolute

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class CompilerTest {

  @Test
  fun `test simple compilatation`() {
    val input =
      FileProvider.projectRoot().resolve("core/app/src/main/res/layout/activity_editor.xml")
        .toFile()
    val output = Paths.get("./build").absolute().normalize().toFile()
    println(input)
    println(output)

    val start = System.nanoTime()
    compileResource(input, output, ResourceCompilerOptions(), BlameLogger(StdLogger(VERBOSE)))
    println("Compiled in ${System.nanoTime() - start} nano seconds")
  }

  @Test
  fun `test xml processor`() {
    val input =
      Paths.get(".", "src/test/resources/layout/activity_editor.xml").absolute().normalize()
        .toFile()
    val pathData = extractPathData(input, input.absolutePath)
    val resFile = ResourceFile(
      ResourceName("", pathData.type!!, pathData.name),
      pathData.config,
      pathData.source,
      ProtoXml
    )
    val processor = XmlProcessor(pathData.source, BlameLogger(StdLogger(VERBOSE)))
    processor.process(resFile, input.inputStream().buffered())
  }
}
