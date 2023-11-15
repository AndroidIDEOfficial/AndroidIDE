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
import io.github.rosemoe.sora.editor.ts.predicate.TsSyntheticCaptureContainer

/**
 * A [TreeSitterPredicate] which inverts the result of another predicate.
 *
 * @author Akash Yadav
 */
open class InvertingPredicate(override val name: String,
  private val predicate: TreeSitterPredicate) :
  TreeSitterPredicate() {

  override fun canHandle(steps: List<TsClientPredicateStep>): Boolean {
    return predicate.canHandle(steps)
  }

  override fun doPredicateInternal(
    tsQuery: TSQuery,
    text: CharSequence,
    match: TSQueryMatch,
    predicateSteps: List<TsClientPredicateStep>,
    syntheticCaptures: TsSyntheticCaptureContainer
  ): PredicateResult {
    return when (val result = this.predicate.doPredicateInternal(tsQuery, text, match,
      predicateSteps, syntheticCaptures)) {
      PredicateResult.ACCEPT -> PredicateResult.REJECT
      PredicateResult.REJECT -> PredicateResult.ACCEPT
      else -> result
    }
  }
}
