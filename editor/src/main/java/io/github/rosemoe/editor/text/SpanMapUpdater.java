/*
 *   Copyright 2020-2021 Rosemoe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.rosemoe.editor.text;

import java.util.ArrayList;
import java.util.List;

import io.github.rosemoe.editor.struct.Span;
import io.github.rosemoe.editor.widget.EditorColorScheme;

/**
 * Update spans on text change event
 *
 * @author Rose
 */
public class SpanMapUpdater {

    public static void shiftSpansOnMultiLineDelete(List<List<Span>> map, int startLine, int startColumn, int endLine, int endColumn) {
        int lineCount = endLine - startLine - 1;
        // Remove unrelated lines
        while (lineCount > 0) {
            Span.recycleAll(map.remove(startLine + 1));
            lineCount--;
        }
        // Clean up start line
        List<Span> startLineSpans = map.get(startLine);
        int index = startLineSpans.size() - 1;
        while (index > 0) {
            if (startLineSpans.get(index).column >= startColumn) {
                startLineSpans.remove(index).recycle();
                index--;
            } else {
                break;
            }
        }
        // Shift end line
        List<Span> endLineSpans = map.get(startLine + 1);
        while (endLineSpans.size() > 1) {
            Span first = endLineSpans.get(0);
            if (first.column >= endColumn) {
                break;
            } else {
                int spanEnd = endLineSpans.get(1).column;
                if (spanEnd <= endColumn) {
                    endLineSpans.remove(first);
                    first.recycle();
                } else {
                    break;
                }
            }
        }
        for (int i = 0; i < endLineSpans.size(); i++) {
            Span span = endLineSpans.get(i);
            if (span.column < endColumn) {
                span.column = 0;
            } else {
                span.column -= endColumn;
            }
        }
    }

    public static void shiftSpansOnSingleLineDelete(List<List<Span>> map, int line, int startCol, int endCol) {
        if (map == null || map.isEmpty()) {
            return;
        }
        List<Span> spanList = map.get(line);
        int startIndex = findSpanIndexFor(spanList, 0, startCol);
        if (startIndex == -1) {
            //No span is to be updated
            return;
        }
        int endIndex = findSpanIndexFor(spanList, startIndex, endCol);
        if (endIndex == -1) {
            endIndex = spanList.size();
        }
        // Remove spans inside delete text
        int removeCount = endIndex - startIndex;
        for (int i = 0; i < removeCount; i++) {
            spanList.remove(startIndex).recycle();
        }
        // Shift spans
        int delta = endCol - startCol;
        while (startIndex < spanList.size()) {
            spanList.get(startIndex).column -= delta;
            startIndex++;
        }
        // Ensure there is span
        if (spanList.isEmpty() || spanList.get(0).column != 0) {
            spanList.add(0, Span.obtain(0, EditorColorScheme.TEXT_NORMAL));
        }
        // Remove spans with length 0
        for (int i = 0; i + 1 < spanList.size(); i++) {
            if (spanList.get(i).column >= spanList.get(i + 1).column) {
                spanList.remove(i).recycle();
                i--;
            }
        }
    }

    public static void shiftSpansOnSingleLineInsert(List<List<Span>> map, int line, int startCol, int endCol) {
        if (map == null || map.isEmpty()) {
            return;
        }
        List<Span> spanList = map.get(line);
        int index = findSpanIndexFor(spanList, 0, startCol);
        if (index == -1) {
            return;
        }
        int originIndex = index;
        // Shift spans after insert position
        int delta = endCol - startCol;
        while (index < spanList.size()) {
            spanList.get(index++).column += delta;
        }
        // Add extra span for line start
        if (originIndex == 0) {
            Span first = spanList.get(0);
            if (first.colorId == EditorColorScheme.TEXT_NORMAL && first.underlineColor == 0) {
                first.column = 0;
            } else {
                spanList.add(0, Span.obtain(0, EditorColorScheme.TEXT_NORMAL));
            }
        }
    }

    public static void shiftSpansOnMultiLineInsert(List<List<Span>> map, int startLine, int startColumn, int endLine, int endColumn) {
        // Find extended span
        List<Span> startLineSpans = map.get(startLine);
        int extendedSpanIndex = findSpanIndexFor(startLineSpans, 0, startColumn);
        if (extendedSpanIndex == -1) {
            extendedSpanIndex = startLineSpans.size() - 1;
        }
        if (startLineSpans.get(extendedSpanIndex).column > startColumn) {
            extendedSpanIndex--;
        }
        Span extendedSpan;
        if (extendedSpanIndex < 0 || extendedSpanIndex >= startLineSpans.size()) {
            extendedSpan = Span.obtain(0, EditorColorScheme.TEXT_NORMAL);
        } else {
            extendedSpan = startLineSpans.get(extendedSpanIndex);
        }
        // Create map link for new lines
        for (int i = 0; i < endLine - startLine; i++) {
            List<Span> list = new ArrayList<>();
            list.add(extendedSpan.copy().setColumn(0));
            map.add(startLine + 1, list);
        }
        // Add original spans to new line
        List<Span> endLineSpans = map.get(endLine);
        if (endColumn == 0 && extendedSpanIndex + 1 < startLineSpans.size()) {
            endLineSpans.clear();
        }
        int delta = Integer.MIN_VALUE;
        while (extendedSpanIndex + 1 < startLineSpans.size()) {
            Span span = startLineSpans.remove(extendedSpanIndex + 1);
            if (delta == Integer.MIN_VALUE) {
                delta = span.column;
            }
            endLineSpans.add(span.setColumn(span.column - delta + endColumn));
        }
    }

    private static int findSpanIndexFor(List<Span> spans, int initialPosition, int targetCol) {
        for (int i = initialPosition; i < spans.size(); i++) {
            if (spans.get(i).column >= targetCol) {
                return i;
            }
        }
        return -1;
    }

}
