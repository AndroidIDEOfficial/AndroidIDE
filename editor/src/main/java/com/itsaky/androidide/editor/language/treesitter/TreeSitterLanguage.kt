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
import com.itsaky.androidide.treesitter.TSLanguage
import com.itsaky.androidide.treesitter.TSParser
import com.itsaky.androidide.treesitter.TSQueryCapture
import com.itsaky.androidide.treesitter.TSQueryCursor
import com.itsaky.androidide.treesitter.TSQueryMatch
import com.itsaky.androidide.utils.ILogger
import io.github.rosemoe.sora.editor.ts.TsTheme
import io.github.rosemoe.sora.lang.Language.INTERRUPTION_LEVEL_STRONG
import io.github.rosemoe.sora.lang.analysis.AnalyzeManager
import io.github.rosemoe.sora.text.ContentReference
import io.github.rosemoe.sora.text.TextUtils
import io.github.rosemoe.sora.widget.SymbolPairMatch

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
  private val analyzer by lazy { TreeSitterAnalyzeManager(languageSpec.spec, tsTheme) }
  private val newlineHandlersLazy by lazy { createNewlineHandlers() }

  private var languageScheme: LanguageScheme? = null

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

  open fun finalizeIndent(indent: Int): Int {
    return indent * getTabSize()
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

  override fun getIndentAdvance(content: ContentReference, line: Int, column: Int): Int {
    return computeIndent(
      content.toString(),
      line,
      column,
      decrementBy = TextUtils.countLeadingSpaceCount(content.getLine(line), getTabSize())
    )
  }

  protected open fun computeIndent(
    content: String,
    line: Int,
    column: Int,
    decrementBy: Int = 0
  ): Int {
    val indentsQuery = languageSpec.indentsQuery ?: return 0
    return TSParser.create().use { parser ->
      parser.language = languageSpec.language
      return@use parser.parseString(content).use { tree ->
        TSQueryCursor.create().use { cursor ->
          cursor.exec(indentsQuery, tree.rootNode)

          var indent = 0
          var match: TSQueryMatch? = cursor.nextMatch()
          val captures = mutableListOf<TSQueryCapture>()
          while (match != null) {
            captures.addAll(match.captures)
            match = cursor.nextMatch()
          }

          captures.sortBy { it.node.startByte }

          for (capture in captures) {
            val capLine = capture.node.startPoint.row
            val capCol = capture.node.endPoint.column / 2

            if (capLine > line || (capLine == line && capCol > column)) {
              break
            }

            val captureName = indentsQuery.getCaptureNameForId(capture.index)
            if (captureName == "indent") {
              ++indent
            } else if (captureName == "outdent") {
              --indent
            }
          }

          finalizeIndent(indent) - decrementBy
        }
      }
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
