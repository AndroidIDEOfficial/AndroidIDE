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

package com.itsaky.androidide.editor.language.treesitter.predicates

import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryMatch
import com.itsaky.androidide.treesitter.TSQueryPredicateStep
import io.github.rosemoe.sora.editor.ts.predicate.PredicateResult
import io.github.rosemoe.sora.editor.ts.predicate.TsClientPredicateStep
import io.github.rosemoe.sora.editor.ts.predicate.TsPredicate
import io.github.rosemoe.sora.editor.ts.predicate.TsSyntheticCaptureContainer
import io.github.rosemoe.sora.editor.ts.predicate.builtin.getCaptureContent
import io.github.rosemoe.sora.editor.ts.predicate.builtin.parametersMatch
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.PatternSyntaxException

/**
 * [TsPredicate] implementation for '#match?' query predicates.
 *
 * @author Akash Yadav
 */
object MatchPredicate : TreeSitterPredicate() {

  override val name: String
    get() = "match"

  @JvmField
  val PARAMETERS =
    arrayOf(
      TSQueryPredicateStep.Type.String,
      TSQueryPredicateStep.Type.Capture,
      TSQueryPredicateStep.Type.String,
      TSQueryPredicateStep.Type.Done
    )

  private val cache = ConcurrentHashMap<String, Regex>()

  override fun doPredicateInternal(
    tsQuery: TSQuery,
    text: CharSequence,
    match: TSQueryMatch,
    predicateSteps: List<TsClientPredicateStep>,
    syntheticCaptures: TsSyntheticCaptureContainer
  ): PredicateResult {
    val captured = getCaptureContent(tsQuery, match, predicateSteps[1].content, text)
    try {
      var regex = cache[predicateSteps[2].content]
      if (regex == null) {
        regex = Regex(predicateSteps[2].content)
        cache[predicateSteps[2].content] = regex
      }
      for (str in captured) {
        if (regex.find(str) == null) {
          return PredicateResult.REJECT
        }
      }
      return PredicateResult.ACCEPT
    } catch (e: PatternSyntaxException) {
      e.printStackTrace()
      return PredicateResult.UNHANDLED
    }
  }

  override fun canHandle(steps: List<TsClientPredicateStep>): Boolean {
    return parametersMatch(steps, PARAMETERS)
  }
}
