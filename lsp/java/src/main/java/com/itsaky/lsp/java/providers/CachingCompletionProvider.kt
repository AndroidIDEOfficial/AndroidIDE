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

import com.itsaky.lsp.api.AbstractServiceProvider
import com.itsaky.lsp.api.ICompletionProvider
import com.itsaky.lsp.internal.model.CachedCompletion
import com.itsaky.lsp.models.CompletionParams
import com.itsaky.lsp.models.CompletionResult

/**
 * A completion provider which caches the result to provide completions faster on next completion
 * request.
 *
 * @author Akash Yadav
 */
abstract class CachingCompletionProvider {

    protected var cached = CachedCompletion.EMPTY

    protected open fun cache(params: CompletionParams, result: CompletionResult) {
        this.cached = CachedCompletion.cache(params, result)
    }

    protected open fun canUseCache(params: CompletionParams): Boolean {
        return this.cached.canUseCache(params)
    }
}
