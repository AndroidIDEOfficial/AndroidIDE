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
import io.github.rosemoe.sora.editor.ts.predicate.PredicateResult
import io.github.rosemoe.sora.editor.ts.predicate.TsClientPredicateStep
import io.github.rosemoe.sora.editor.ts.predicate.TsPredicate

/**
 * [TsPredicate] implementation for '#not-match?' query predicates.
 *
 * @author Akash Yadav
 */
object  NotMatchPredicate : MatchPredicate() {

  override val name: String
    get() = "not-match"

  override fun doPredicateInternal(
    tsQuery: TSQuery,
    text: CharSequence,
    match: TSQueryMatch,
    predicate: List<TsClientPredicateStep>
  ): PredicateResult {

    // Invert the result of the match predicate
    return when (val result = super.doPredicateInternal(tsQuery, text, match, predicate)) {
      PredicateResult.ACCEPT -> PredicateResult.REJECT
      PredicateResult.REJECT -> PredicateResult.ACCEPT
      else -> result
    }
  }
}
