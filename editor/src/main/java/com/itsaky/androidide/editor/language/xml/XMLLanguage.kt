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

package com.itsaky.androidide.editor.language.xml

import android.content.Context
import com.itsaky.androidide.editor.language.treesitter.TreeSitterLanguage
import com.itsaky.androidide.lsp.api.ILanguageServer
import com.itsaky.androidide.lsp.api.ILanguageServerRegistry
import com.itsaky.androidide.lsp.xml.XMLLanguageServer
import com.itsaky.androidide.treesitter.xml.TSLanguageXml
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler
import io.github.rosemoe.sora.text.ContentReference
import io.github.rosemoe.sora.text.TextUtils.countLeadingSpaceCount
import io.github.rosemoe.sora.util.MyCharacter

/**
 * Tree Sitter language XML language.
 *
 * @author Akash Yadav
 */
class XMLLanguage(context: Context) :
  TreeSitterLanguage(context, lang = TSLanguageXml.newInstance(), type = TS_TYPE) {

  companion object {
    const val TS_TYPE = "xml"
  }

  override fun getLanguageServer(): ILanguageServer? {
    return ILanguageServerRegistry.getDefault().getServer(XMLLanguageServer.SERVER_ID)
  }

  override fun checkIsCompletionChar(c: Char): Boolean {
    return MyCharacter.isJavaIdentifierPart(c) || c == '<' || c == '/'
  }

  override fun getInterruptionLevel(): Int {
    return INTERRUPTION_LEVEL_STRONG
  }
}
