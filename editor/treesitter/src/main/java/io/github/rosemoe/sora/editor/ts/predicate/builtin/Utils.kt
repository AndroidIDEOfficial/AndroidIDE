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
import com.itsaky.androidide.treesitter.TSQueryPredicateStep.Type
import com.itsaky.androidide.treesitter.string.UTF16String
import io.github.rosemoe.sora.editor.ts.predicate.TsClientPredicateStep
import io.github.rosemoe.sora.text.Content

fun parametersMatch(
  predicate: List<TsClientPredicateStep>,
  types: Array<Type>
): Boolean {
  if (predicate.size == types.size) {
    for (i in types.indices) {
      if (predicate[i].predicateType != types[i]) {
        return false
      }
    }
    return true
  }
  return false
}

fun getCaptureContent(
  tsQuery: TSQuery,
  match: TSQueryMatch,
  captureName: String,
  text: CharSequence
) = match.captures.filter { tsQuery.getCaptureNameForId(it.index) == captureName }
  .map { capture ->
    when (text) {
      is UTF16String -> text.substringBytes(capture.node.startByte, capture.node.endByte)
      is Content -> text.substring(capture.node.startByte shr 1, capture.node.endByte shr 1)
      else -> text.substring(capture.node.startByte shr 1, capture.node.endByte shr 1)
    }
  }