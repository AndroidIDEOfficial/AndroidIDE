/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.utils

import com.itsaky.androidide.models.Symbol
import java.io.File

object Symbols {

  @JvmStatic
  fun forFile(file: File?): List<Symbol> {
    if (file == null || !file.isFile) {
      return emptyList()
    }

    return when (file.extension) {
      "java",
      "gradle",
      "kt",
      "kts" -> javaSymbols

      "xml" -> xmlSymbols
      else -> plainTextSymbols
    }
  }

  private val javaSymbols by lazy {
    listOf(
      TabSymbol(),
      Symbol("{", "{}"),
      Symbol("}"),
      Symbol("(", "()"),
      Symbol(")"),
      Symbol(";"),
      Symbol("="),
      Symbol("\"", "\"\""),
      Symbol("|"),
      Symbol("&"),
      Symbol("!"),
      Symbol("[", "[]"),
      Symbol("]"),
      Symbol("<", "<>"),
      Symbol(">"),
      Symbol("+"),
      Symbol("-"),
      Symbol("/"),
      Symbol("*"),
      Symbol("?"),
      Symbol(":"),
      Symbol("_")
    )
  }

  private val xmlSymbols by lazy {
    listOf(
      TabSymbol(),
      Symbol("<", "<>"),
      Symbol(">"),
      Symbol("/"),
      Symbol("="),
      Symbol("\"", "\"\""),
      Symbol(":"),
      Symbol("@"),
      Symbol("+"),
      Symbol("(", "()"),
      Symbol(")"),
      Symbol(";"),
      Symbol(","),
      Symbol("."),
      Symbol("?"),
      Symbol("|"),
      Symbol("\\"),
      Symbol("&"),
      Symbol("[", "[]"),
      Symbol("]"),
      Symbol("{", "{}"),
      Symbol("}"),
      Symbol("_"),
      Symbol("-")
    )
  }

  val plainTextSymbols by lazy {
    listOf(
      TabSymbol(),
      Symbol("{", "{}"),
      Symbol("}"),
      Symbol("(", "()"),
      Symbol(")"),
      Symbol("="),
      Symbol("\"", "\"\""),
      Symbol("'", "''"),
      Symbol("|"),
      Symbol("&"),
      Symbol("!"),
      Symbol("[", "[]"),
      Symbol("]"),
      Symbol("<", "<>"),
      Symbol(">"),
      Symbol("+"),
      Symbol("-"),
      Symbol("/"),
      Symbol("~"),
      Symbol("`"),
      Symbol(":"),
      Symbol("_")
    )
  }

  private class TabSymbol : Symbol("↹") {

    override val commit: String
      get() = "\t"

    override val offset: Int
      get() = 1
  }
}
