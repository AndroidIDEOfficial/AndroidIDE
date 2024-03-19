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

package io.github.rosemoe.sora.editor.ts

import com.itsaky.androidide.treesitter.TSLanguage
import com.itsaky.androidide.treesitter.TSParser
import com.itsaky.androidide.treesitter.TSTree
import com.itsaky.androidide.treesitter.string.UTF16String
import com.itsaky.androidide.treesitter.string.UTF16StringFactory

/**
 * A text document which maintains a [TSTree] and the associated [UTF16String].
 *
 * @author Akash Yadav
 */
class TsTextDocument(
  language: TSLanguage
) : AutoCloseable {

  @Volatile
  private var documentVersion = 1L

  /**
   * The version of this text document.
   */
  val version: Long
    get() = documentVersion

  /**
   * The source text.
   */
  val text = UTF16StringFactory.newString()

  /**
   * The parser used to parse the source text into a syntax tree.
   */
  val parser = TSParser.create().also {
    it.language = language
  }

  /**
   * The syntax tree.
   */
  var tree: TSTree? = null
    internal set

  /**
   * Request the parser to cancel parsing if a parsing is in progress.
   */
  fun requestCancellationAndWaitIfParsing() {
    if (parser.isParsing) {
      parser.requestCancellationAndWait()
    }
  }

  /**
   * Initialize the source text with the given initialization message. The caller is responsible
   * for handling the source text state i.e. this method does not check whether the text is already
   * initialized or not.
   */
  internal fun doInit(init: TextInit) {
    text.append(init.text)
    documentVersion = init.contentVersion
  }

  /**
   * Apply the given [text modification][TextMod] to the source text.
   *
   * @param mod The text modification.
   */
  internal fun doMod(mod: TextMod) {
    val edit = mod.edit
    val newText = mod.changedText

    if (newText == null) {
      text.deleteBytes(edit.startByte, edit.oldEndByte)
    } else {
      if (mod.start == text.length) {
        text.append(newText)
      } else {
        text.insert(mod.start, newText)
      }
    }

    documentVersion = mod.contentVersion
  }

  /**
   * Parse the source text into a syntax tree, using the given [oldTree] for incremental parsing.
   */
  internal fun reparse(oldTree: TSTree? = null): TSTree? {
    tree = parser.parseString(oldTree, text)
    return tree
  }

  override fun close() {
    text?.close()
    tree?.close()
    parser.close()
  }
}