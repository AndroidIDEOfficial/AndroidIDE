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

import android.os.Bundle
import com.itsaky.androidide.treesitter.TSInputEdit
import io.github.rosemoe.sora.editor.ts.spans.DefaultSpanFactory
import io.github.rosemoe.sora.editor.ts.spans.TsSpanFactory
import io.github.rosemoe.sora.lang.analysis.AnalyzeManager
import io.github.rosemoe.sora.lang.analysis.StyleReceiver
import io.github.rosemoe.sora.lang.styling.Styles
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.ContentReference

/**
 * @author Akash Yadav
 */
open class TsAnalyzeManager(val languageSpec: TsLanguageSpec, var theme: TsTheme) : AnalyzeManager {

  var stylesReceiver: StyleReceiver? = null
  var reference: ContentReference? = null
  var spanFactory: TsSpanFactory = DefaultSpanFactory()

  open var styles = Styles()

  private var _analyzeWorker: TsAnalyzeWorker? = null
  val analyzeWorker: TsAnalyzeWorker?
    get() = _analyzeWorker

  open fun updateTheme(theme: TsTheme) {
    this.theme = theme
    (styles.spans as LineSpansGenerator?)?.also {
      it.theme = theme
    }
  }

  override fun setReceiver(receiver: StyleReceiver?) {
    stylesReceiver = receiver
    _analyzeWorker?.stylesReceiver = receiver
  }

  override fun reset(content: ContentReference, extraArguments: Bundle) {
    reference = content
    rerun()
  }

  override fun insert(start: CharPosition, end: CharPosition, insertedContent: CharSequence) {
    val edit = TSInputEdit.create(
      start.index shl 1,
      start.index shl 1,
      end.index shl 1,
      start.toTSPoint(),
      start.toTSPoint(),
      end.toTSPoint()
    )!!
    (styles.spans as LineSpansGenerator?)?.apply {
      lineCount = reference!!.lineCount
      edit(edit)
    }
    _analyzeWorker?.onMod(Mod(TextMod(
      start.index,
      end.index,
      edit,
      insertedContent.toString(),
      reference?.documentVersion ?: 0
    )))
  }

  override fun delete(start: CharPosition, end: CharPosition, deletedContent: CharSequence) {
    val edit = TSInputEdit.create(
      start.index shl 1,
      end.index shl 1,
      start.index shl 1,
      start.toTSPoint(),
      end.toTSPoint(),
      start.toTSPoint()
    )!!
    (styles.spans as LineSpansGenerator?)?.apply {
      lineCount = reference!!.lineCount
      edit(edit)
    }
    _analyzeWorker?.onMod(Mod(TextMod(
      start.index,
      end.index,
      edit,
      null,
      reference?.documentVersion ?: 0
    )))
  }

  override fun rerun() {
    _analyzeWorker?.stop()
    _analyzeWorker = null

    (styles.spans as LineSpansGenerator?)?.tree?.close()
    styles.spans = null
    styles = Styles()

    val initText = reference?.reference?.toString() ?: ""

    _analyzeWorker = TsAnalyzeWorker(this, languageSpec, theme, styles, reference!!, spanFactory)
    _analyzeWorker!!.apply {
      this.stylesReceiver = this@TsAnalyzeManager.stylesReceiver
      init(Init(TextInit(initText, reference?.documentVersion ?: 0)))
      start()
    }
  }

  override fun destroy() {
    _analyzeWorker?.stop()
    _analyzeWorker = null

    (styles.spans as LineSpansGenerator?)?.tree?.close()
    styles.spans = null

    spanFactory.close()
  }
}