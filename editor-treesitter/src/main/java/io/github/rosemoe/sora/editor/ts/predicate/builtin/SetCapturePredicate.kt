/*******************************************************************************
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2023  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 ******************************************************************************/

package io.github.rosemoe.sora.editor.ts.predicate.builtin

import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryMatch
import com.itsaky.androidide.treesitter.TSQueryPredicateStep
import io.github.rosemoe.sora.editor.ts.predicate.PredicateResult
import io.github.rosemoe.sora.editor.ts.predicate.TsClientPredicateStep
import io.github.rosemoe.sora.editor.ts.predicate.TsPredicate
import io.github.rosemoe.sora.editor.ts.predicate.TsSyntheticCapture
import io.github.rosemoe.sora.editor.ts.predicate.TsSyntheticCaptureContainer

object SetCapturePredicate : TsPredicate {

  private val PARAMETERS_1 = arrayOf(
    TSQueryPredicateStep.Type.String,
    TSQueryPredicateStep.Type.Capture,
    TSQueryPredicateStep.Type.String,
    TSQueryPredicateStep.Type.Done
  )
  private val PARAMETERS_2 = arrayOf(
    TSQueryPredicateStep.Type.String,
    TSQueryPredicateStep.Type.Capture,
    TSQueryPredicateStep.Type.Capture,
    TSQueryPredicateStep.Type.Done
  )

  override fun doPredicate(
    tsQuery: TSQuery,
    text: CharSequence,
    match: TSQueryMatch,
    predicateSteps: List<TsClientPredicateStep>,
    syntheticCaptures: TsSyntheticCaptureContainer
  ): PredicateResult {
    if (predicateSteps[0].content == "set!") {
      if (parametersMatch(predicateSteps, PARAMETERS_1)) {
        syntheticCaptures.addSyntheticCapture(
          TsSyntheticCapture(
            predicateSteps[1].content,
            predicateSteps[2].content
          )
        )
      } else if (parametersMatch(predicateSteps, PARAMETERS_2)) {
        val captureTexts = getCaptureContent(tsQuery, match, predicateSteps[2].content, text)
        if (captureTexts.size == 1) {
          syntheticCaptures.addSyntheticCapture(
            TsSyntheticCapture(
              predicateSteps[1].content,
              captureTexts[0]
            )
          )
        }
      }
    }
    // As this does not affect whether the match is actually valid, we always return UNHANDLED
    return PredicateResult.UNHANDLED
  }

}