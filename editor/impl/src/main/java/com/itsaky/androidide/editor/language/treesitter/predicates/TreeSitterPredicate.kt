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
import io.github.rosemoe.sora.editor.ts.predicate.TsSyntheticCaptureContainer

/**
 * Base class for tree-sitter predicate implementations.
 *
 * @author Akash Yadav
 */
abstract class TreeSitterPredicate : TsPredicate {

  /** The name of the predicate that will be used to match. */
  abstract val name: String

  /**
   * Whether the implmentation can handle the given predicate steps.
   *
   * @param steps The predicate steps.
   * @return `true` if and only if the implementatin can handle the given predicate steps, `false`
   *   otherwise.
   */
  abstract fun canHandle(steps: List<TsClientPredicateStep>): Boolean

  /**
   * Performs the predicate check.
   *
   * @param tsQuery The [TSQuery] for the predicate.
   * @param text The editor text.
   * @param match The [TSQueryMatch] object.
   * @param predicateSteps The predicate steps.
   * @return The result of the predicate check.
   */
  internal abstract fun doPredicateInternal(
    tsQuery: TSQuery,
    text: CharSequence,
    match: TSQueryMatch,
    predicateSteps: List<TsClientPredicateStep>,
    syntheticCaptures: TsSyntheticCaptureContainer
  ): PredicateResult

  override fun doPredicate(
    tsQuery: TSQuery,
    text: CharSequence,
    match: TSQueryMatch,
    predicateSteps: List<TsClientPredicateStep>,
    syntheticCaptures: TsSyntheticCaptureContainer
  ): PredicateResult {

    if (
      predicateSteps.isEmpty() ||
      predicateSteps[0].content != "${name}?" ||
      !canHandle(predicateSteps)
    ) {
      return PredicateResult.UNHANDLED
    }

    return doPredicateInternal(tsQuery, text, match, predicateSteps, syntheticCaptures)
  }
}
