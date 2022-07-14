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

package com.itsaky.androidide.lsp.java.actions

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lsp.java.JavaLSPTest
import com.itsaky.androidide.lsp.java.actions.diagnostics.AddImportAction

/** @author Akash Yadav */
class AddImportTester : JavaLSPTest() {

  override fun test() {
    addImport()
  }

  @Suppress("UNCHECKED_CAST")
  fun addImport() {
    openFile("AddImportAction")

    val diagnostic =
      server.analyze(file!!).diagnostics.first { it.code == "compiler.err.cant.resolve.location" }
    val file = this.file!!.toFile()
    val data = createActionData(diagnostic, file, this.file!!, this.server)

    val action = AddImportAction()
    action.prepare(data)
    assertThat(action.visible).isTrue()
    assertThat(action.enabled).isTrue()

    val execResult = action.execAction(data)
    assertThat(execResult::class.java).isAssignableTo(Pair::class.java)

    val result = execResult as Pair<List<String>, *>
    assertThat(result.first).contains("java.util.stream.Stream")
  }

  override fun openFile(fileName: String) {
    super.openFile("actions/$fileName")
  }
}
