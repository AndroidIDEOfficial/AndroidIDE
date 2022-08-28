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

package com.itsaky.androidide.lsp.xml.providers.completion

import com.itsaky.androidide.lsp.models.CompletionItem.Companion.matchLevel
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import kotlin.math.min

fun match(simpleName: String, qualifiedName: String, prefix: String): MatchLevel {
  val simpleNameMatchLevel = matchLevel(simpleName, prefix)
  val nameMatchLevel = matchLevel(qualifiedName, prefix)
  if (simpleNameMatchLevel == NO_MATCH && nameMatchLevel == NO_MATCH) {
    return NO_MATCH
  }

  return MatchLevel.values()[min(simpleNameMatchLevel.ordinal, nameMatchLevel.ordinal)]
}
