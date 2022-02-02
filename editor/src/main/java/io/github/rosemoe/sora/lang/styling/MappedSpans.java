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

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

/**
 * Store spans by map.
 *
 * @see Builder
 */
public class MappedSpans implements Spans {

    private final List<List<Span>> spanMap;

    private MappedSpans(@NonNull List<List<Span>> spanMap) {
        this.spanMap = spanMap;
    }

    @Override
    public void adjustOnInsert(CharPosition start, CharPosition end) {
        var startLine = start.line;
        var endLine = end.line;
        var startColumn = start.column;
        var endColumn = end.column;
        if (startLine == endLine) {
            MappedSpanUpdater.shiftSpansOnSingleLineInsert(spanMap, startLine, startColumn, endColumn);
        } else {
            MappedSpanUpdater.shiftSpansOnMultiLineInsert(spanMap, startLine, startColumn, endLine, endColumn);
        }
    }

    @Override
    public void adjustOnDelete(CharPosition start, CharPosition end) {
        var startLine = start.line;
        var endLine = end.line;
        var startColumn = start.column;
        var endColumn = end.column;
        if (startLine == endLine) {
            MappedSpanUpdater.shiftSpansOnSingleLineDelete(spanMap, startLine, startColumn, endColumn);
        } else {
            MappedSpanUpdater.shiftSpansOnMultiLineDelete(spanMap, startLine, startColumn, endLine, endColumn);
        }
    }

    @Override
    public Reader read() {
        return new Mdf();
    }

    @Override
    public boolean supportsModify() {
        return true;
    }

    @Override
    public Modifier modify() {
        return new Mdf();
    }

    /**
     * Allow you to build a span map linearly.
     */
    public static class Builder {

        private final List<List<Span>> spans;
        private Span last;
        private boolean determined = false;

        public Builder() {
            this(128);
        }

        public Builder(int lineCapacity) {
            spans = new ArrayList<>(lineCapacity);
        }

        /**
         * Add a new span if required.
         * <p>
         * If no special style is specified, you can use colorId as style long integer
         *
         * @param spanLine Line
         * @param column   Column
         * @param style    Style of text
         */
        public void addIfNeeded(int spanLine, int column, long style) {
            if (last != null && last.style == style) {
                return;
            }
            add(spanLine, Span.obtain(column, style));
        }

        /**
         * Add a span directly
         * <p>
         * Note: the line should always >= the line of span last committed
         * <p>
         * If two spans are on the same line, you must add them in order by their column
         *
         * @param spanLine The line position of span
         * @param span     The span
         */
        public void add(int spanLine, Span span) {
            int mapLine = spans.size() - 1;
            if (spanLine == mapLine) {
                spans.get(spanLine).add(span);
            } else if (spanLine > mapLine) {
                Span extendedSpan = last;
                if (extendedSpan == null) {
                    extendedSpan = Span.obtain(0, EditorColorScheme.TEXT_NORMAL);
                }
                while (mapLine < spanLine) {
                    List<Span> lineSpans = new ArrayList<>();
                    lineSpans.add(extendedSpan.copy().setColumn(0));
                    spans.add(lineSpans);
                    mapLine++;
                }
                List<Span> lineSpans = spans.get(spanLine);
                if (span.column == 0) {
                    lineSpans.clear();
                }
                lineSpans.add(span);
            } else {
                throw new IllegalStateException("Invalid position");
            }
            last = span;
        }

        /**
         * This method must be called when whole text is analyzed.
         * <strong>Note that it is not the line count but line index!</strong>
         *
         * @param line The line is the line last of text
         */
        public void determine(int line) {
            int mapLine = spans.size() - 1;
            var extendedSpan = last;
            if (extendedSpan == null) {
                extendedSpan = Span.obtain(0, EditorColorScheme.TEXT_NORMAL);
            }
            while (mapLine < line) {
                List<Span> lineSpans = new ArrayList<>();
                lineSpans.add(extendedSpan.copy().setColumn(0));
                spans.add(lineSpans);
                mapLine++;
            }
            determined = true;
        }

        /**
         * Ensure the list not empty
         */
        public void addNormalIfNull() {
            if (spans.isEmpty()) {
                List<Span> spanList = new ArrayList<>();
                spanList.add(Span.obtain(0, EditorColorScheme.TEXT_NORMAL));
                spans.add(spanList);
            }
        }

        /**
         * Marks a region with the given flag.
         * This can only be called after {@link Builder#determine(int)} is called.
         */
        public void markProblemRegion(int newFlag, int startLine, int startColumn, int endLine, int endColumn) {
            if (!determined) {
                throw new IllegalStateException("determine() has not been successfully called");
            }
            for (int line = startLine; line <= endLine; line++) {
                int start = (line == startLine ? startColumn : 0);
                int end = (line == endLine ? endColumn : Integer.MAX_VALUE);
                List<Span> spans = this.spans.get(line);
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
            }
        }

        public MappedSpans build() {
            return new MappedSpans(spans);
        }
    }

    private class Mdf implements Reader, Modifier {

        private List<Span> span;

        private void checkLine() {
            if (span == null) {
                throw new IllegalStateException("line must be set first");
            }
        }

        @Override
        public void moveToLine(int line) {
            if (line == -1) {
                span = null;
                return;
            }
            span = spanMap.get(line);
        }

        @Override
        public int getSpanCount() {
            checkLine();
            return span.size();
        }

        @Override
        public Span getSpanAt(int index) {
            checkLine();
            return span.get(index);
        }

        @Override
        public List<Span> getSpansOnLine(int line) {
            return Collections.unmodifiableList(spanMap.get(line));
        }

        @Override
        public void setSpansOnLine(int line, List<Span> spans) {
            var last = spanMap.get(spanMap.size() - 1);
            var extend = last.get(last.size() - 1);
            while (spanMap.size() <= line) {
                var list = new ArrayList<Span>();
                list.add(extend.copy().setColumn(0));
                spanMap.add(list);
            }
            spanMap.set(line, spans);
        }

        @Override
        public void addLineAt(int line, List<Span> spans) {
            spanMap.add(line, spans);
        }

        @Override
        public void deleteLineAt(int line) {
            spanMap.remove(line);
        }
    }

}
