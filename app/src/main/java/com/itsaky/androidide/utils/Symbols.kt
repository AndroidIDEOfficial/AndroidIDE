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

import com.itsaky.androidide.ui.SymbolInputView.Symbol
import java.io.File

object Symbols {

  @JvmStatic
  fun forFile(file: File?): Array<Symbol?> {
    if (file == null || !file.isFile) {
      return emptyArray()
    }

    return when (file.extension) {
      "java",
      "gradle",
      "kt" -> javaSymbols()
      "xml" -> xmlSymbols()
      else -> plainTextSymbols()
    }
  }

  fun javaSymbols(): Array<Symbol?> {
    return arrayOf(
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

  fun xmlSymbols(): Array<Symbol?> {
    return arrayOf(
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

  fun plainTextSymbols(): Array<Symbol?> {
    return arrayOf(
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
    override fun getCommit(): String {
      return "\t"
    }

    override fun getOffset(): Int {
      return 1
    }
  }
}
