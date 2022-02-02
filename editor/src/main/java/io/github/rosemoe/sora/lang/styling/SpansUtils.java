/*
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2022  Rosemoe
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
 */
package io.github.rosemoe.sora.lang.styling;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for {@link Spans}
 *
 * @author Rosemoe
 */
public class SpansUtils {

    /**
     * Marks a region with the given flag.
     * This is a general implementation for all {@link Spans} classes.
     */
    public static void markProblemRegion(Spans src, int newFlag, int startLine, int startColumn, int endLine, int endColumn) {
        if (!src.supportsModify()) {
            throw new UnsupportedOperationException("source Spans object does not support modify()");
        }
        var mdf = src.modify();
        var reader = src.read();
        for (int line = startLine; line <= endLine; line++) {
            int start = (line == startLine ? startColumn : 0);
            int end = (line == endLine ? endColumn : Integer.MAX_VALUE);
            List<Span> spans = new ArrayList<>(reader.getSpansOnLine(line));
            int increment;
            for (int i = 0; i < spans.size(); i += increment) {
                Span span = spans.get(i);
                increment = 1;
                if (span.column >= end) {
                    break;
                }
                int spanEnd = (i + 1 >= spans.size() ? Integer.MAX_VALUE : spans.get(i + 1).column);
                if (spanEnd >= start) {
                    int regionStartInSpan = Math.max(span.column, start);
                    int regionEndInSpan = Math.min(end, spanEnd);
                    if (regionStartInSpan == span.column) {
                        if (regionEndInSpan != spanEnd) {
                            increment = 2;
                            Span nSpan = span.copy();
                            nSpan.column = regionEndInSpan;
                            spans.add(i + 1, nSpan);
                        }
                        span.problemFlags |= newFlag;
                    } else {
                        //regionStartInSpan > span.column
                        if (regionEndInSpan == spanEnd) {
                            increment = 2;
                            Span nSpan = span.copy();
                            nSpan.column = regionStartInSpan;
                            spans.add(i + 1, nSpan);
                            nSpan.problemFlags |= newFlag;
                        } else {
                            increment = 3;
                            Span span1 = span.copy();
                            span1.column = regionStartInSpan;
                            span1.problemFlags |= newFlag;
                            Span span2 = span.copy();
                            span2.column = regionEndInSpan;
                            spans.add(i + 1, span1);
                            spans.add(i + 2, span2);
                        }
                    }
                }
            }
            mdf.setSpansOnLine(line, spans);
        }
    }

}
