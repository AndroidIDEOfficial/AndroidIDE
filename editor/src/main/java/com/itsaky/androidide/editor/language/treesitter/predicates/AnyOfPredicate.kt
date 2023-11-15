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

/**
 * [TsPredicate] implementation for '#any-of?' query predicates.
 *
 * Syntax : `"#any-of?" @capture "string" ["string", ... ] Done`
 *
 * Checks if the text of `@capture` matches (literally) any of the `"string"` defined.
 *
 * @author Akash Yadav
 */
object AnyOfPredicate : TreeSitterPredicate() {

  override val name: String
    get() = "any-of"

  override fun canHandle(steps: List<TsClientPredicateStep>): Boolean {
    return steps.size > 4 &&
        steps.let {
          it[0].predicateType == TSQueryPredicateStep.Type.String &&
              it[1].predicateType == TSQueryPredicateStep.Type.Capture &&
              it[it.lastIndex].predicateType == TSQueryPredicateStep.Type.Done &&
              it.subList(2, it.lastIndex - 1).all { step ->
                step.predicateType == TSQueryPredicateStep.Type.String
              }
        }
  }

  override fun doPredicateInternal(
    tsQuery: TSQuery,
    text: CharSequence,
    match: TSQueryMatch,
    predicateSteps: List<TsClientPredicateStep>,
    syntheticCaptures: TsSyntheticCaptureContainer
  ): PredicateResult {
    val captured = getCaptureContent(tsQuery, match, predicateSteps[1].content, text)
    val toMatch = predicateSteps.subList(2, predicateSteps.lastIndex - 1).map { it.content }
    for (capture in captured) {
      if (capture !in toMatch) {
        return PredicateResult.REJECT
      }
    }
    return PredicateResult.ACCEPT
  }
}
