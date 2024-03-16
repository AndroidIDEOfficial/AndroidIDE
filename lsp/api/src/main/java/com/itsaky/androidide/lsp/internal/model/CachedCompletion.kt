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

package com.itsaky.androidide.lsp.internal.model

import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.progress.ICancelChecker
import com.itsaky.androidide.utils.DocumentUtils
import org.slf4j.LoggerFactory
import java.nio.file.Paths

/**
 * Cached version of a completion result. Keeps only required properties from [params].
 *
 * @author Akash Yadav
 */
class CachedCompletion
private constructor(
  val params: CompletionParams,
  val result: com.itsaky.androidide.lsp.models.CompletionResult
) {

  companion object {

    private val log = LoggerFactory.getLogger(CachedCompletion::class.java)

    /** Empty cached completion. Could be used to represent "no cache available". */
    @JvmField
    val EMPTY =
      cache(
        CompletionParams(
          com.itsaky.androidide.models.Position.NONE,
          Paths.get(""), ICancelChecker.CANCELLED
        ),
        com.itsaky.androidide.lsp.models.CompletionResult.EMPTY
      )

    /**
     * Creates cached version of the result from the given params and result.
     *
     * @param _params The [CompletionParams] used to trigger the completion request. A shallow copy
     * of this request is created.
     * @param result The result of the completion to cache.
     */
    @JvmStatic
    fun cache(
      _params: CompletionParams,
      result: com.itsaky.androidide.lsp.models.CompletionResult
    ): CachedCompletion {
      val params =
        CompletionParams(_params.position, _params.file, ICancelChecker.CANCELLED).apply {
          prefix = _params.prefix ?: ""
          content = ""
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

    if (!DocumentUtils.isSameFile(file, params.file)) {
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
