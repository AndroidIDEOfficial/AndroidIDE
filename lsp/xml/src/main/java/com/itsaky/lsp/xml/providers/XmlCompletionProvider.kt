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

import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.api.ICompletionProvider
import com.itsaky.lsp.models.CompletionParams
import com.itsaky.lsp.models.CompletionResult

/**
 * Completion provider for XMl files.
 *
 * @author Akash Yadav
 */
class XmlCompletionProvider : ICompletionProvider {

    private val cachedResult: CompletionResult? = null
    private val log = ILogger.newInstance(javaClass.simpleName)

    override fun complete(params: CompletionParams): CompletionResult {
        return try {
            CompletionResult()
        } catch (error: Throwable) {
            log.error("An error occurred while computing XML completions", error)
            CompletionResult()
        }
    }
}
