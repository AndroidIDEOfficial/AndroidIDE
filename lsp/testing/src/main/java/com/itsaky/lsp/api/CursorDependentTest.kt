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
import com.itsaky.lsp.models.DocumentChangeEvent
import com.itsaky.lsp.models.Position
import io.github.rosemoe.sora.text.Content

/**
 * A cursor dependent test is a test that needs to have cursor positions specified in the content.
 * `@@cursor@@` denotes the position of the cursor in source files.
 *
 * @author Akash Yadav
 */
abstract class CursorDependentTest : LoggingTest() {
    
    protected var cursor: Int = -1
    private val cursorText = "@@cursor@@"
    
    fun requireCursor(): Int {
        this.cursor = super.contents!!.indexOf(cursorText)
        assertThat(cursor).isGreaterThan(-1)
        return cursor
    }
    
    fun deleteCursorText() {
        contents!!.delete(this.cursor, this.cursor + cursorText.length)
        assertThat(contents!!.indexOf(cursorText)).isEqualTo(-1)
        
        // As the content has been changed, we have to
        // Update the content in language server
        getServer().documentHandler.onContentChange(DocumentChangeEvent(file!!, contents!!, 1))
    }
    
    fun cursorPosition(): Position {
        return cursorPosition(true)
    }
    
    fun cursorPosition(deleteCursorText: Boolean): Position {
        requireCursor()
        
        if (deleteCursorText) {
            deleteCursorText()
        }
        
        val pos = Content(contents!!).indexer.getCharPosition(cursor)
        return Position(pos.line, pos.column, pos.index)
    }
}