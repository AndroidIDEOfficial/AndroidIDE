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

import com.itsaky.lsp.api.CursorDependentTest
import com.itsaky.lsp.api.ILanguageServer
import com.itsaky.lsp.java.JavaLanguageServerProvider
import com.itsaky.lsp.models.CompletionParams
import com.itsaky.lsp.models.Position
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class JavaCompletionProviderTest : CursorDependentTest() {
    private val server: ILanguageServer = JavaLanguageServerProvider.INSTANCE.server()
    override fun getServer(): ILanguageServer = server
    
    @Test
    fun testLocals() {
        openFile("LocalsCompletionTest")
        
        val pos = cursorPosition()
//        val items = completionTitles(pos)
//        assertThat(items).containsAtLeast("aString", "anInt", "aFloat", "aDouble", "args")
    }
    
    override fun openFile(fileName: String) {
        super.openFile("completion/${fileName}")
    }
    
    private fun completionTitles(pos: Position): List<CharSequence> {
        return server.completionProvider.complete(CompletionParams(pos, file!!)).items.map { it.label }
    }
}