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
package com.itsaky.lsp.java

import com.google.common.truth.Truth.assertThat

/**
 * A cursor dependent test is a test that needs to have cursor positions specified in the content.
 * `@@cursor@@` denotes the position of the cursor in source files.
 *
 * @author Akash Yadav
 */
open class CursorDependentTest : LoggingTest() {
    
    protected val CURSOR = "@@cursor@@"
    
    fun requireCursor(content: String): Int {
        val cursor = content.indexOf(CURSOR)
        assertThat(cursor).isGreaterThan(-1)
        return cursor
    }
    
    fun requireCursor(content: StringBuilder): Int {
        val cursor = content.indexOf(CURSOR)
        assertThat(cursor).isGreaterThan(-1)
        return cursor
    }
}