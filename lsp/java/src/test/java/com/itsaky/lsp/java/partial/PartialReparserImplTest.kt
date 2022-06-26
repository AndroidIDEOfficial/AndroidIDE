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

package com.itsaky.lsp.java.partial

import com.google.common.truth.Truth
import com.itsaky.lsp.java.BaseJavaTest
import com.itsaky.lsp.java.JavaLanguageServer
import com.itsaky.lsp.java.visitors.FindMethodAt
import com.sun.source.tree.MethodTree
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class PartialReparserImplTest : BaseJavaTest() {

  val jls = mServer as JavaLanguageServer

  @Test
  fun parseMethod() {
    openFile("PartialReparserTest")
    val task =
      jls.compiler.compile(file).get { task ->
        val reparser = PartialReparserImpl()
        val method = FindMethodAt(task.task).scan(task.root(), 133)
        val reparsed = reparser.parseMethod(
          CompilationInfo(task.task, task.diagnosticListener, task.root()),
          method!!.leaf as MethodTree,
          "{System.out.println(\"Hello world!\"); var klass = String.class;}"
        )
        
        Truth.assertThat(reparsed).isTrue()
      }
  }

  override fun openFile(fileName: String) {
    super.openFile("partial/$fileName")
  }
}
