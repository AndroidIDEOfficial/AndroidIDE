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

package com.itsaky.lsp.xml.providers

import android.text.TextUtils
import com.google.common.truth.Truth.assertThat
import com.itsaky.lsp.models.CompletionParams
import com.itsaky.lsp.xml.BaseXMLTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class XMLCompletionProviderTest : BaseXMLTest() {

    @Test
    fun testTagCompletion() {
        openFile("TagCompletion")

        val (isIncomplete, items) = complete()

        assertThat(isIncomplete).isTrue()
        assertThat(items).isNotEmpty()

        assertThat(items).containsAtLeast("ImageView", "ImageButton")
    }

    @Test
    fun testAttrValueCompletion() {
        openFile("AttributeValueCompletion")

        val (isIncomplete, items) = complete()

        assertThat(isIncomplete).isFalse()
        assertThat(items).isNotEmpty()

        assertThat(items).containsAtLeast("center", "fitCenter", "fitXY", "matrix")
    }

    @Test
    fun testAttrCompletion() {
        openFile("AttributeCompletion")

        val (isIncomplete, items) = complete()

        assertThat(isIncomplete).isTrue()
        assertThat(items).isNotEmpty()
        
        assertThat(items).containsAtLeast("text", "textColor", "textAlignment", "textAllCaps")
    }

    override fun openFile(fileName: String) {
        super.openFile("completion/${fileName}")
    }

    private fun complete(): Pair<Boolean, List<String>> {
        val createCompletionParams = createCompletionParams()
        val result = mServer.completionProvider.complete(createCompletionParams)
        return result.isIncomplete to
            result.items
                .filter { it.label != null }
                .map { it.label.toString() }
                .filter { it.isNotBlank() }
                .toList()
    }

    private fun createCompletionParams(): CompletionParams {
        val cursor = cursorPosition(true)
        val completionParams = CompletionParams(cursor, file!!)
        completionParams.module = mockModuleProject()
        completionParams.position.index = this.cursor
        completionParams.content = contents
        return completionParams
    }
}
