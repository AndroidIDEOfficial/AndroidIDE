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
 * [TsPredicate] implementation for '#eq?' query predicates.
 *
 * Syntax : `"#eq?" @capture @capture | "string" Done`
 *
 * Checks if the contents of the first capture is equal to the given string or contents of the
 * second capture.
 *
 * @author Akash Yadav
 */
object EqualPredicate : TreeSitterPredicate() {

  override val name: String
    get() = "eq"

  override fun canHandle(steps: List<TsClientPredicateStep>): Boolean {
    return steps.size == 4 &&
        steps[0].predicateType == TSQueryPredicateStep.Type.String &&
        steps[1].predicateType == TSQueryPredicateStep.Type.Capture &&
        steps[2].predicateType.let {
          it == TSQueryPredicateStep.Type.Capture || it == TSQueryPredicateStep.Type.String
        } &&
        steps[3].predicateType == TSQueryPredicateStep.Type.Done
  }

  override fun doPredicateInternal(
    tsQuery: TSQuery,
    text: CharSequence,
    match: TSQueryMatch,
    predicateSteps: List<TsClientPredicateStep>,
    syntheticCaptures: TsSyntheticCaptureContainer
  ): PredicateResult {
    val first = getCaptureContent(tsQuery, match, predicateSteps[1].content, text)
    val second =
      predicateSteps[2].let {
        check(
          it.predicateType == TSQueryPredicateStep.Type.String ||
              it.predicateType == TSQueryPredicateStep.Type.Capture
        ) {
          "Second predicate step of #eq? predicate must be a string or a capture"
        }

        if (it.predicateType == TSQueryPredicateStep.Type.Capture) {
          getCaptureContent(tsQuery, match, it.content, text)
        } else {
          it.content
        }
      }

    return if (first == second) {
      PredicateResult.ACCEPT
    } else {
      PredicateResult.REJECT
    }
  }
}
