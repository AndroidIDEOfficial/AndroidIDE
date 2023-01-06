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

package com.itsaky.androidide.editor.language.java

import android.content.Context
import com.itsaky.androidide.editor.language.utils.CommonSymbolPairs
import com.itsaky.androidide.editor.language.treesitter.TreeSitterLanguage
import com.itsaky.androidide.editor.language.newline.TSBracketsHandler
import com.itsaky.androidide.editor.language.newline.TSCStyleBracketsHandler
import com.itsaky.androidide.lsp.api.ILanguageServer
import com.itsaky.androidide.lsp.api.ILanguageServerRegistry
import com.itsaky.androidide.lsp.java.JavaLanguageServer
import com.itsaky.androidide.treesitter.java.TSLanguageJava
import io.github.rosemoe.sora.util.MyCharacter
import io.github.rosemoe.sora.widget.SymbolPairMatch

/**
 * Tree Sitter language specification for Java.
 *
 * @author Akash Yadav
 */
class JavaLanguage(context: Context) :
  TreeSitterLanguage(context, TSLanguageJava.newInstance(), TS_TYPE) {

  companion object {
    const val TS_TYPE = "java"
  }

  override fun getLanguageServer(): ILanguageServer? {
    return ILanguageServerRegistry.getDefault().getServer(JavaLanguageServer.SERVER_ID)
  }

  override fun checkIsCompletionChar(c: Char): Boolean {
    return MyCharacter.isJavaIdentifierPart(c) || c == '.'
  }

  override fun getInterruptionLevel(): Int {
    return INTERRUPTION_LEVEL_SLIGHT
  }

  override fun getSymbolPairs(): SymbolPairMatch {
    return JavaSymbolPairs()
  }
  
  override fun createNewlineHandlers(): Array<TSBracketsHandler> {
    return arrayOf(TSCStyleBracketsHandler(this))
  }

  internal open class JavaSymbolPairs : CommonSymbolPairs() {
    init {
      super.putPair('<', SymbolPair("<", ">"))
    }
  }
}
