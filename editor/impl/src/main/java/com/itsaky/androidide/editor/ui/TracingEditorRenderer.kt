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

package com.itsaky.androidide.editor.ui

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.RenderNode
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.collection.MutableIntList
import com.itsaky.androidide.editor.BuildConfig
import io.github.rosemoe.sora.lang.styling.CodeBlock
import io.github.rosemoe.sora.lang.styling.Spans
import io.github.rosemoe.sora.text.ContentLine
import io.github.rosemoe.sora.util.LongArrayList
import io.github.rosemoe.sora.util.MutableInt
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.EditorRenderer

/**
 * An implementation of [EditorRenderer] which traces the whole drawing process for [IDEEditor].
 *
 * @author Akash Yadav
 */
class TracingEditorRenderer(
  private val enabled: Boolean = BuildConfig.DEBUG,
  editor: CodeEditor
) : EditorRenderer(editor) {

  override fun draw(canvas: Canvas) = trace("draw") {
    super.draw(canvas)
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  override fun updateLineDisplayList(renderNode: RenderNode?, line: Int, spans: Spans.Reader?) =
    trace("updateLineDisplayList") {
      super.updateLineDisplayList(renderNode, line, spans)
    }

  override fun drawSingleTextLine(canvas: Canvas?, line: Int, offsetX: Float, offsetY: Float,
    spans: Spans.Reader?, visibleOnly: Boolean) = trace("drawSingleTextLine") {
    super.drawSingleTextLine(canvas, line, offsetX, offsetY, spans, visibleOnly)
  }

  override fun drawView(canvas: Canvas?) = trace("drawView") {
    super.drawView(canvas)
  }

  override fun drawUserGutterBackground(canvas: Canvas?, right: Int) =
    trace("drawUserGutterBackground") {
      super.drawUserGutterBackground(canvas, right)
    }

  override fun drawStuckLineNumbers(canvas: Canvas?, candidates: MutableList<CodeBlock>?,
    offset: Float, lineNumberWidth: Float, lineNumberColor: Int) = trace("drawStuckLineNumbers") {
    super.drawStuckLineNumbers(canvas, candidates, offset, lineNumberWidth, lineNumberColor)
  }

  override fun drawStuckLines(canvas: Canvas?, candidates: MutableList<CodeBlock>?, offset: Float) =
    trace("drawStuckLines") {
      super.drawStuckLines(canvas, candidates, offset)
    }

  override fun drawHardwrapMarker(canvas: Canvas?, offset: Float) = trace("drawHardwrapMarker") {
    super.drawHardwrapMarker(canvas, offset)
  }

  override fun drawSideIcons(canvas: Canvas?, offset: Float) = trace("drawSideIcons") {
    super.drawSideIcons(canvas, offset)
  }

  override fun drawFormatTip(canvas: Canvas?) = trace("drawFormatTip") {
    super.drawFormatTip(canvas)
  }

  override fun drawColor(canvas: Canvas?, color: Int, rect: RectF?) = trace("drawColor") {
    super.drawColor(canvas, color, rect)
  }

  override fun drawColor(canvas: Canvas?, color: Int, rect: Rect?) = trace("drawColor") {
    super.drawColor(canvas, color, rect)
  }

  override fun drawColorRound(canvas: Canvas?, color: Int, rect: RectF?) = trace("drawColorRound") {
    super.drawColorRound(canvas, color, rect)
  }

  override fun drawRowBackground(canvas: Canvas?, color: Int, row: Int) =
    trace("drawRowBackground") {
      super.drawRowBackground(canvas, color, row)
    }

  override fun drawRowBackground(canvas: Canvas?, color: Int, row: Int, right: Int) =
    trace("drawRowBackground") {
      super.drawRowBackground(canvas, color, row, right)
    }

  override fun drawLineNumber(canvas: Canvas?, line: Int, row: Int, offsetX: Float, width: Float,
    color: Int) = trace("drawLineNumber") {
    super.drawLineNumber(canvas, line, row, offsetX, width, color)
  }

  override fun drawLineNumberBackground(canvas: Canvas?, offsetX: Float, width: Float, color: Int) =
    trace("drawLineNumberBackground") {
      super.drawLineNumberBackground(canvas, offsetX, width, color)
    }

  override fun drawDivider(canvas: Canvas?, offsetX: Float, color: Int) = trace("drawDivider") {
    super.drawDivider(canvas, offsetX, color)
  }

  override fun drawRows(canvas: Canvas?, offset: Float, postDrawLineNumbers: LongArrayList?,
    postDrawCursor: MutableList<DrawCursorTask>?, postDrawCurrentLines: MutableIntList?,
    requiredFirstLn: MutableInt?) = trace("drawRows") {
    super.drawRows(canvas, offset, postDrawLineNumbers, postDrawCursor, postDrawCurrentLines,
      requiredFirstLn)
  }

  override fun drawDiagnosticIndicators(canvas: Canvas?, offset: Float) =
    trace("drawDiagnosticIndicators") {
      super.drawDiagnosticIndicators(canvas, offset)
    }

  override fun drawWhitespaces(canvas: Canvas?, offset: Float, line: Int, row: Int, rowStart: Int,
    rowEnd: Int, min: Int, max: Int) = trace("drawWhitespaces") {
    super.drawWhitespaces(canvas, offset, line, row, rowStart, rowEnd, min, max)
  }

  override fun drawMiniGraph(canvas: Canvas?, offset: Float, row: Int, graph: String?) =
    trace("drawMiniGraph") {
      super.drawMiniGraph(canvas, offset, row, graph)
    }

  override fun getRowTopForBackground(row: Int) = trace("getRowTopForBackground") {
    return@trace super.getRowTopForBackground(row)
  }

  override fun getRowBottomForBackground(row: Int) = trace("getRowBottomForBackground") {
    return@trace super.getRowBottomForBackground(row)
  }

  override fun drawRowRegionBackground(canvas: Canvas?, row: Int, line: Int, highlightStart: Int,
    highlightEnd: Int, rowStart: Int, rowEnd: Int, color: Int) = trace("drawRowRegionBackground") {
    super.drawRowRegionBackground(canvas, row, line, highlightStart, highlightEnd, rowStart, rowEnd,
      color)
  }

  override fun drawRowBackgroundRect(canvas: Canvas?, rect: RectF?) =
    trace("drawRowBackgroundRect") {
      super.drawRowBackgroundRect(canvas, rect)
    }

  override fun drawRegionText(canvas: Canvas?, offsetX: Float, baseline: Float, line: Int,
    startIndex: Int, endIndex: Int, contextStart: Int, contextEnd: Int, isRtl: Boolean,
    columnCount: Int, color: Int) = trace("drawRegionText") {
    super.drawRegionText(canvas, offsetX, baseline, line, startIndex, endIndex, contextStart,
      contextEnd, isRtl, columnCount, color)
  }

  override fun drawRegionTextDirectional(canvas: Canvas?, offsetX: Float, baseline: Float,
    line: Int, startIndex: Int, endIndex: Int, contextStart: Int, contextEnd: Int, columnCount: Int,
    color: Int) = trace("drawRegionTextDirectional") {
    super.drawRegionTextDirectional(canvas, offsetX, baseline, line, startIndex, endIndex,
      contextStart, contextEnd, columnCount, color)
  }

  override fun drawFunctionCharacter(canvas: Canvas?, offsetX: Float, offsetY: Float, width: Float,
    ch: Char) = trace("drawFunctionCharacter") {
    super.drawFunctionCharacter(canvas, offsetX, offsetY, width, ch)
  }

  override fun drawText(canvas: Canvas?, line: ContentLine?, index: Int, count: Int,
    contextStart: Int, contextCount: Int, isRtl: Boolean, offX: Float, offY: Float,
    lineNumber: Int) = trace("drawText") {
    super.drawText(canvas, line, index, count, contextStart, contextCount, isRtl, offX, offY,
      lineNumber)
  }

  override fun drawTextRunDirect(canvas: Canvas?, src: CharArray?, index: Int, count: Int,
    contextStart: Int, contextCount: Int, offX: Float, offY: Float, isRtl: Boolean) =
    trace("drawTextRunDirect") {
      super.drawTextRunDirect(canvas, src, index, count, contextStart, contextCount, offX, offY,
        isRtl)
    }

  override fun drawEdgeEffect(canvas: Canvas?) = trace("drawEdgeEffect") {
    super.drawEdgeEffect(canvas)
  }

  override fun drawBlockLines(canvas: Canvas?, offsetX: Float) = trace("drawBlockLines") {
    super.drawBlockLines(canvas, offsetX)
  }

  override fun drawSideBlockLine(canvas: Canvas?) = trace("drawSideBlockLine") {
    super.drawSideBlockLine(canvas)
  }

  override fun drawScrollBars(canvas: Canvas?) = trace("drawScrollBars") {
    super.drawScrollBars(canvas)
  }

  override fun drawScrollBarTrackVertical(canvas: Canvas?) = trace("drawScrollBarTrackVertical") {
    super.drawScrollBarTrackVertical(canvas)
  }

  override fun drawScrollBarVertical(canvas: Canvas?) = trace("drawScrollBarVertical") {
    super.drawScrollBarVertical(canvas)
  }

  override fun drawLineInfoPanel(canvas: Canvas?, topY: Float, length: Float) =
    trace("drawLineInfoPanel") {
      super.drawLineInfoPanel(canvas, topY, length)
    }

  override fun drawScrollBarTrackHorizontal(canvas: Canvas?) =
    trace("drawScrollBarTrackHorizontal") {
      super.drawScrollBarTrackHorizontal(canvas)
    }

  override fun patchSnippetRegions(canvas: Canvas?, textOffset: Float) =
    trace("patchSnippetRegions") {
      super.patchSnippetRegions(canvas, textOffset)
    }

  override fun patchHighlightedDelimiters(canvas: Canvas?, textOffset: Float) =
    trace("patchHighlightedDelimiters") {
      super.patchHighlightedDelimiters(canvas, textOffset)
    }

  override fun patchTextRegionWithColor(canvas: Canvas?, textOffset: Float, start: Int, end: Int,
    color: Int, backgroundColor: Int, underlineColor: Int) = trace("patchTextRegionWithColor") {
    super.patchTextRegionWithColor(canvas, textOffset, start, end, color, backgroundColor,
      underlineColor)
  }

  override fun patchTextRegions(canvas: Canvas?, textOffset: Float,
    positions: MutableList<TextDisplayPosition>?, patch: PatchDraw) = trace("patchTextRegions") {
    super.patchTextRegions(canvas, textOffset, positions, patch)
  }

  override fun drawSelectionOnAnimation(canvas: Canvas?) = trace("drawSelectionOnAnimation") {
    super.drawSelectionOnAnimation(canvas)
  }

  override fun drawScrollBarHorizontal(canvas: Canvas?) = trace("drawScrollBarHorizontal") {
    super.drawScrollBarHorizontal(canvas)
  }

  override fun buildMeasureCacheForLines(startLine: Int, endLine: Int, timestamp: Long,
    useCachedContent: Boolean) = trace("buildMeasureCacheForLines") {
    super.buildMeasureCacheForLines(startLine, endLine, timestamp, useCachedContent)
  }

  override fun buildMeasureCacheForLines(startLine: Int, endLine: Int) =
    trace("buildMeasureCacheForLines") {
      super.buildMeasureCacheForLines(startLine, endLine)
    }

  override fun measureText(text: ContentLine?, line: Int, index: Int, count: Int) =
    trace("measureText") {
      return@trace super.measureText(text, line, index, count)
    }

  private inline fun <T : Any?> trace(section: String, crossinline action: () -> T): T =
    if (enabled) {
      androidx.tracing.trace(section, action)
    } else {
      action()
    }
}