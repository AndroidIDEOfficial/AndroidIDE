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

package com.itsaky.lsp.java.providers

import com.google.common.truth.Truth.assertThat
import com.itsaky.lsp.api.ILanguageServer
import com.itsaky.lsp.api.LoggingTest
import com.itsaky.lsp.java.JavaLanguageServerProvider
import com.itsaky.lsp.models.CodeActionParams
import com.itsaky.lsp.models.DiagnosticItem
import com.itsaky.lsp.models.Range
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

/**
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class JavaCodeActionProviderTest : LoggingTest() {
    
    private val server: ILanguageServer = JavaLanguageServerProvider.INSTANCE.server();
    
    override fun getServer(): ILanguageServer = server
    
    @Test
    fun testAddImport() {
        openFile("AddImportAction")
        val diagnostics = server.analyze(file!!).filter { it.code == "compiler.err.cant.resolve.location" }
        val actions = actions(diagnostics)
        assertThat(actions).contains("Import 'java.util.stream.Stream'")
    }
    
    private fun actions(range: Range): List<String> = actions(range, Collections.emptyList())
    private fun actions(diagnostics: List<DiagnosticItem>): List<String> =
        actions(Range(), diagnostics)
    
    private fun actions(range: Range, diagnostics: List<DiagnosticItem>): List<String> {
        val params = CodeActionParams(file!!, range, diagnostics)
        return server.codeActions(params).actions.map { it.title }
    }
    
    override fun openFile(fileName: String) {
        super.openFile("actions/${fileName}")
    }
}