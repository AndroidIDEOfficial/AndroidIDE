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
import com.itsaky.lsp.java.BaseJavaTest
import com.itsaky.lsp.models.CompletionParams
import com.itsaky.lsp.models.Position
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_VALUE_STRING)
class JavaCompletionProviderTest : BaseJavaTest() {

    @Test
    fun testLocals() {
        openFile("LocalsCompletionTest")

        val pos = cursorPosition()
        val items = completionTitles(pos)
        assertThat(items).containsAtLeast("aaString", "aaInt", "aaFloat", "aaDouble", "args")
    }

    @Test
    fun testMembers() {
        // Complete members of String
        openFile("MembersCompletionTest")

        val pos = cursorPosition()
        val items = completionTitles(pos)
        assertThat(items)
            .containsAtLeast("getClass", "toLowerCase", "toUpperCase", "substring", "charAt")
    }

    @Test
    fun testLambdaVariableMemberAccess() {
        // Complete members of Throwable
        openFile("LambdaMembersCompletionTest")

        val pos = cursorPosition()
        val items = completionTitles(pos)
        assertThat(items)
            .containsAtLeast("getMessage", "getCause", "getStackTrace", "printStackTrace")
    }

    @Test
    fun testStaticAccess() {
        // Complete static members of String
        openFile("StaticMembersCompletionTest")

        val pos = cursorPosition()
        val items = completionTitles(pos)
        assertThat(items)
            .containsAtLeast("format", "join", "valueOf", "CASE_INSENSITIVE_ORDER", "class")
    }

    override fun openFile(fileName: String) {
        super.openFile("completion/${fileName}")
    }

    private fun completionTitles(pos: Position): List<CharSequence> {
        return mServer.completionProvider.complete(CompletionParams(pos, file!!)).items.map {
            it.label
        }
    }
}
