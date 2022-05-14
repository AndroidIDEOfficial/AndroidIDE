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

package com.itsaky.lsp.xml

import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.lsp.api.CursorDependentTest
import com.itsaky.lsp.api.ILanguageServer
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/** @author Akash Yadav */
open class BaseXMLTest : CursorDependentTest() {
    protected val mServer = XmlLanguageServerProvider.INSTANCE.server()
    override fun getServer(): ILanguageServer = mServer

    protected fun mockModuleProject(): IdeAndroidModule {
        val mocked = mock(IdeAndroidModule::class.java)
        `when`(mocked.packageName).thenReturn("com.itsaky.androidide.test")
        return mocked
    }
}
