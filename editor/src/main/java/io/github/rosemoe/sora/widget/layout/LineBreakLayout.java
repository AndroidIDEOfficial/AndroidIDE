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
package io.github.rosemoe.sora.widget.layout;

import java.util.NoSuchElementException;

import io.github.rosemoe.sora.graphics.GraphicTextRow;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.text.ContentLine;
import io.github.rosemoe.sora.util.BinaryHeap;
import io.github.rosemoe.sora.util.IntPair;
import io.github.rosemoe.sora.widget.CodeEditor;

/**
 * Layout implementation of editor
 * This layout is never broke unless there is actually a newline character
 *
 * @author Rose
 */
public class LineBreakLayout extends AbstractLayout {

    private BinaryHeap widthMaintainer;

    public LineBreakLayout(CodeEditor editor, Content text) {
        super(editor, text);
        measureAllLines();
    }

    private void measureAllLines() {
        if (text == null) {
            return;
        }
        widthMaintainer = new BinaryHeap();
        widthMaintainer.ensureCapacity(text.getLineCount());
        var gtr = GraphicTextRow.obtain();
        for (int i = 0; i < text.getLineCount(); i++) {
            ContentLine line = text.getLine(i);
            gtr.set(line, 0, line.length(), editor.getTabWidth(), getSpans(i), editor.getTextPaint());
            int width = (int) gtr.measureText(0, line.length());
            line.setWidth(width);
            line.setId(widthMaintainer.push(width));
        }
        GraphicTextRow.recycle(gtr);
    }

    private void measureLines(int startLine, int endLine) {
        if (text == null) {
            return;
        }
        var gtr = GraphicTextRow.obtain();
        while (startLine <= endLine && startLine < text.getLineCount()) {
            ContentLine line = text.getLine(startLine);
            gtr.set(line, 0, line.length(), editor.getTabWidth(), getSpans(startLine), editor.getTextPaint());
            int width = (int) gtr.measureText(0, line.length());
            if (line.getId() != -1) {
                if (line.getWidth() == width) {
                    startLine++;
                    continue;
                }
                widthMaintainer.update(line.getId(), width);
                startLine++;
                continue;
            }
            line.setId(widthMaintainer.push(width));
            line.setWidth(width);
            startLine++;
        }
        GraphicTextRow.recycle(gtr);
    }

    @Override
    public RowIterator obtainRowIterator(int initialRow) {
        return new LineBreakLayoutRowItr(initialRow);
    }

    @Override
    public void beforeReplace(Content content) {
        // Intentionally empty
    }

    @Override
    public void afterInsert(Content content, int startLine, int startColumn, int endLine, int endColumn, CharSequence insertedContent) {
        super.afterInsert(content, startLine, startColumn, endLine, endColumn, insertedContent);
        measureLines(startLine, endLine);
    }

    @Override
    public void afterDelete(Content content, int startLine, int startColumn, int endLine, int endColumn, CharSequence deletedContent) {
        super.afterDelete(content, startLine, startColumn, endLine, endColumn, deletedContent);
        measureLines(startLine, startLine);
    }

    @Override
    public void onRemove(Content content, ContentLine line) {
        widthMaintainer.remove(line.getId());
    }

    @Override
    public void destroyLayout() {
        super.destroyLayout();
        widthMaintainer = null;
    }

    @Override
    public int getLineNumberForRow(int row) {
        return Math.max(0, Math.min(row, text.getLineCount() - 1));
    }

    @Override
    public int getLayoutWidth() {
        return widthMaintainer.top();
    }

    @Override
    public int getLayoutHeight() {
        return text.getLineCount() * editor.getRowHeight();
    }

    @Override
    public long getCharPositionForLayoutOffset(float xOffset, float yOffset) {
        int lineCount = text.getLineCount();
        int line = Math.min(lineCount - 1, Math.max((int) (yOffset / editor.getRowHeight()), 0));
        ContentLine str = text.getLine(line);
        float[] res = orderedFindCharIndex(xOffset, str, line);
        return IntPair.pack(line, (int) res[0]);
    }

    @Override
    public float[] getCharLayoutOffset(int line, int column, float[] dest) {
        if (dest == null || dest.length < 2) {
            dest = new float[2];
        }
        var sequence = text.getLine(line);
        dest[0] = editor.getRowHeight() * (line + 1);
        var gtr = GraphicTextRow.obtain();
        gtr.set(sequence, 0, sequence.length(), editor.getTabWidth(), getSpans(line), editor.getTextPaint());
        dest[1] = gtr.measureText(0, column);
        GraphicTextRow.recycle(gtr);
        return dest;
    }

    @Override
    public int getRowCountForLine(int line) {
        return 1;
    }

    class LineBreakLayoutRowItr implements RowIterator {

        private final Row result;
        private int currentRow;
        private final int initRow;

        LineBreakLayoutRowItr(int initialRow) {
            initRow = currentRow = initialRow;
            result = new Row();
            result.isLeadingRow = true;
            result.startColumn = 0;
        }

        @Override
        public Row next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            result.lineIndex = currentRow;
            result.endColumn = text.getColumnCount(currentRow++);
            return result;
        }

        @Override
        public boolean hasNext() {
            return currentRow >= 0 && currentRow < text.getLineCount();
        }

        @Override
        public void reset() {
            currentRow = initRow;
        }
    }

}
