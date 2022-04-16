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

import android.content.Context
import com.itsaky.androidide.actions.ActionData
import com.itsaky.lsp.models.DocumentOpenEvent
import java.nio.file.Path
import org.robolectric.RuntimeEnvironment

/** @author Akash Yadav */
abstract class BaseLanguageServerTest {
    protected var file: Path? = null
    protected var contents: StringBuilder? = null

    open fun openFile(fileName: String) {
        file = FileProvider.sourceFile(fileName)
        contents = FileProvider.contents(file!!)

        getServer().documentHandler.onFileOpened(DocumentOpenEvent(file!!, contents.toString(), 0))
    }

    protected open fun createActionData(vararg values: Any): ActionData {
        val data = ActionData()

        data.put(Context::class.java, RuntimeEnvironment.getApplication())
        for (value in values) {
            if (value is Path) {
                data.put(Path::class.java, value)
            } else {
                data.put(value::javaClass.get(), value)
            }
        }

        return data
    }

    protected abstract fun getServer(): ILanguageServer
}
