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

package com.itsaky.androidide.editor.language.treesitter

import android.content.Context
import com.itsaky.androidide.editor.language.IDELanguage
import com.itsaky.androidide.editor.language.newline.TSBracketsHandler
import com.itsaky.androidide.editor.language.utils.CommonSymbolPairs
import com.itsaky.androidide.editor.schemes.IDEColorScheme
import com.itsaky.androidide.editor.schemes.LanguageScheme
import com.itsaky.androidide.editor.schemes.LanguageSpecProvider.getLanguageSpec
import com.itsaky.androidide.editor.schemes.LocalCaptureSpecProvider.newLocalCaptureSpec
import com.itsaky.androidide.editor.utils.isNonBlankLine
import com.itsaky.androidide.treesitter.TSLanguage
import com.itsaky.androidide.utils.IntPair
import io.github.rosemoe.sora.editor.ts.TsTheme
import io.github.rosemoe.sora.lang.Language.INTERRUPTION_LEVEL_STRONG
import io.github.rosemoe.sora.lang.analysis.AnalyzeManager
import io.github.rosemoe.sora.text.ContentReference
import io.github.rosemoe.sora.widget.SymbolPairMatch
import org.slf4j.LoggerFactory

/**
 * Tree Sitter language implementation.
 *
 * @author Akash Yadav
 */
abstract class TreeSitterLanguage(
  context: Context,
  lang: TSLanguage,
  private val langType: String
) : IDELanguage() {

  private lateinit var tsTheme: TsTheme
  private lateinit var languageSpec: TreeSitterLanguageSpec
  private lateinit var _indentProvider: TreeSitterIndentProvider
  private val analyzer by lazy { TreeSitterAnalyzeManager(languageSpec.spec, tsTheme) }
  private val newlineHandlersLazy by lazy { createNewlineHandlers() }

  private var languageScheme: LanguageScheme? = null

  private val indentProvider: TreeSitterIndentProvider
    get() {
      if (!this::_indentProvider.isInitialized) {
        this._indentProvider = TreeSitterIndentProvider(
          languageSpec,
          analyzer.analyzeWorker!!,
          getTabSize()
        )
      }

      return _indentProvider
    }

  companion object {

    private val log = LoggerFactory.getLogger(TreeSitterLanguage::class.java)
    private const val DEF_IDENT_ADV = 0
  }

  init {
    this.languageSpec = getLanguageSpec(context, langType, lang, newLocalCaptureSpec(langType))
    this.tsTheme = TsTheme(languageSpec.spec.tsQuery)
  }

  fun setupWith(scheme: IDEColorScheme?) {
    val langScheme = scheme?.languages?.get(langType)
    this.languageScheme = langScheme
    this.analyzer.langScheme = languageScheme
    langScheme?.styles?.forEach { tsTheme.putStyleRule(it.key, it.value.makeStyle()) }
  }

  override fun getAnalyzeManager(): AnalyzeManager {
    return this.analyzer
  }

  override fun getSymbolPairs(): SymbolPairMatch {
    return CommonSymbolPairs()
  }

  open fun createNewlineHandlers(): Array<TSBracketsHandler> {
    return emptyArray()
  }

  override fun getNewlineHandlers(): Array<TSBracketsHandler> {
    return newlineHandlersLazy
  }

  override fun getInterruptionLevel(): Int {
    return INTERRUPTION_LEVEL_STRONG
  }

  override fun getIndentAdvance(
    content: ContentReference,
    line: Int,
    column: Int,
    spaceCountOnLine: Int,
    tabCountOnLine: Int
  ): Int {
    return try {
      if (line == content.reference.lineCount - 1) {
        // line + 1 does not exist
        // TODO(itsaky): Update this implementation when this behavior is fixed in sora-editor
        return DEF_IDENT_ADV
      }

      var linesToReq = LongArray(1)
      linesToReq[0] = IntPair.pack(line, column)

      if (content.reference.isNonBlankLine(line + 1)) {
        // consider the indentation of the next line only if it is non-blank
        linesToReq += IntPair.pack(line + 1, 0)
      }

      val indents = this.indentProvider.getIndentsForLines(
        content = content.reference,
        positions = linesToReq,
      )

      if (indents.size == 1) {
        val indent = indents[0]
        if (indent == TreeSitterIndentProvider.INDENTATION_ERR) {
          return DEF_IDENT_ADV
        }

        return indent - (spaceCountOnLine + (tabCountOnLine * getTabSize()))
      }

      val (indentLine, indentNxtLine) = indents
      if (indentLine == TreeSitterIndentProvider.INDENTATION_ERR
        || indentNxtLine == TreeSitterIndentProvider.INDENTATION_ERR) {
        log.debug(
          "expectedIndent[{}]={}, expectedIndentNextLine[{}]={}, returning default indent advance",
          line, indentLine, line + 1, indentNxtLine)
        return DEF_IDENT_ADV
      }

      return indentNxtLine - indentLine
    } catch (e: Exception) {
      log.error("An error occurred computing indentation at line:column::{}:{}", line, column, e)
      DEF_IDENT_ADV
    }

  }

  override fun destroy() {
    this.languageSpec.close()
    this.languageScheme = null
  }

  /** A [Factory] creates instance of a specific [TreeSitterLanguage] implementation. */
  fun interface Factory<T : TreeSitterLanguage> {

    /**
     * Create the instance of the [TreeSitterLanguage] implementation.
     *
     * @param context The current context.
     */
    fun create(context: Context): T
  }
}
