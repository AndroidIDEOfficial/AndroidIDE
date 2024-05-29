/*******************************************************************************
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2023  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 ******************************************************************************/

package io.github.rosemoe.sora.editor.ts.spans

import com.itsaky.androidide.treesitter.TSQueryCapture
import io.github.rosemoe.sora.lang.styling.Span

/**
 * Factory for creating spans for the tree sitter analyze manager.
 *
 * @author Akash Yadav
 */
interface TsSpanFactory : AutoCloseable {

  /**
   * Creates the spans using the provided data.
   *
   * @param capture The query capture. More information about the node can be found using the [TSQueryCapture.node] object.
   * @param column The start column index for the span.
   * @param spanStyle The style for the spans.
   * @return The [Span] objects.
   */
  fun createSpans(capture: TSQueryCapture, column: Int, spanStyle: Long): List<Span>
}