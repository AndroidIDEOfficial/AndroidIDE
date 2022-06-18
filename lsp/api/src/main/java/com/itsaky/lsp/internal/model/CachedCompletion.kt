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

package com.itsaky.lsp.internal.model

import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.models.CompletionParams
import com.itsaky.lsp.models.CompletionResult
import com.itsaky.lsp.models.Position
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Cached version of a completion result. Keeps only required properties from [params].
 *
 * @author Akash Yadav
 */
class CachedCompletion
private constructor(val params: CompletionParams, val result: CompletionResult) {

    private val log = ILogger.newInstance(javaClass.simpleName)
    companion object {

        /** Empty cached completion. Could be used to represent "no cache available". */
        @JvmField
        val EMPTY = cache(CompletionParams(Position.NONE, Paths.get("")), CompletionResult.EMPTY)

        /**
         * Creates cached version of the result from the given params and result.
         *
         * @param _params The [CompletionParams] used to trigger the completion request. A shallow
         * copy of this request is created.
         * @param result The result of the completion to cache.
         */
        @JvmStatic
        fun cache(_params: CompletionParams, result: CompletionResult): CachedCompletion {
            val params =
                CompletionParams(_params.position, _params.file).apply {
                    prefix = _params.prefix ?: ""
                    content = ""
                    module = null
                }

            return CachedCompletion(params, result)
        }
    }

    fun canUseCache(params: CompletionParams): Boolean {
        val partial = params.requirePrefix()
        val position = this.params.position
        val file = this.params.file
        val prefix = this.params.requirePrefix()

        // The change in the length of the prefix
        val deltaPrefix = prefix.length - params.prefix!!.length

        // The change in the column index
        val deltaColumn = position.column - params.position.column

        // The changes must be of same length
        if (deltaPrefix != deltaColumn) {
            log.info("...unequal change in prefix and column")
            return false
        }

        if (position.line == -1 || position.column == -1) {
            log.info("...invalid cached completion position")
            return false
        }

        if (position.line != params.position.line || position.column > params.position.column) {
            log.info("...cursor line changed")
            return false
        }

        if (!Files.isSameFile(file, params.file)) {
            log.info("...no cache available for current file")
            return false
        }

        if (!partial.startsWith(prefix) || partial.endsWith(".")) {
            log.info("...incompatible partial identifier")
            return false
        }
        
        return true
    }
}
