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

import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import io.github.rosemoe.sora.graphics.GraphicTextRow;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.text.ContentLine;
import io.github.rosemoe.sora.util.IntPair;
import io.github.rosemoe.sora.widget.CodeEditor;

/**
 * Wordwrap layout for editor
 * <p>
 * This layout will not let character displayed outside the editor's width
 * <p>
 * However, using this can be power-costing because we will have to recreate this layout in various
 * conditions, such as when the line number increases and its width grows or when the text size has changed
 *
 * @author Rose
 */
public class WordwrapLayout extends AbstractLayout {

    private final List<RowRegion> rowTable;
    private final int width;

    public WordwrapLayout(CodeEditor editor, Content text) {
        super(editor, text);
        rowTable = new ArrayList<>();
        width = editor.getWidth() - (int) (editor.measureTextRegionOffset() + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, editor.getResources().getDisplayMetrics()));
        breakAllLines();
    }

    private void breakAllLines() {
        List<Integer> breakpoints = new ArrayList<>();
        for (int i = 0; i < text.getLineCount(); i++) {
            breakLine(i, breakpoints);
            for (int j = -1; j < breakpoints.size(); j++) {
                int start = j == -1 ? 0 : breakpoints.get(j);
                int end = j + 1 < breakpoints.size() ? breakpoints.get(j + 1) : text.getColumnCount(i);
                rowTable.add(new RowRegion(i, start, end));
            }
            breakpoints.clear();
        }
    }

    private int findRow(int line) {
        int index = 0;
        while (index < rowTable.size()) {
            if (rowTable.get(index).line < line) {
                index++;
            } else {
                if (rowTable.get(index).line > line) {
                    index--;
                }
                break;
            }
        }
        return index;
    }

    private void breakLines(int startLine, int endLine) {
        int insertPosition = 0;
        while (insertPosition < rowTable.size()) {
            if (rowTable.get(insertPosition).line < startLine) {
                insertPosition++;
            } else {
                break;
            }
        }
        while (insertPosition < rowTable.size()) {
            int line = rowTable.get(insertPosition).line;
            if (line >= startLine && line <= endLine) {
                rowTable.remove(insertPosition);
            } else {
                break;
            }
        }
        List<Integer> breakpoints = new ArrayList<>();
        for (int i = startLine; i <= endLine; i++) {
            breakLine(i, breakpoints);
            for (int j = -1; j < breakpoints.size(); j++) {
                int start = j == -1 ? 0 : breakpoints.get(j);
                int end = j + 1 < breakpoints.size() ? breakpoints.get(j + 1) : text.getColumnCount(i);
                rowTable.add(insertPosition++, new RowRegion(i, start, end));
            }
            breakpoints.clear();
        }
    }

    private void breakLine(int line, List<Integer> breakpoints) {
        ContentLine sequence = text.getLine(line);
        int start = 0;
        int len = sequence.length();

        while (start < len) {
            var next = (int) editor.findFirstVisibleChar(width, start, len, 0, sequence, line)[0];
            // Force to break the text, though no space is available
            if (next == start) {
                next++;
            }
            breakpoints.add(next);
            start = next;
        }
        if (breakpoints.size() != 0 && breakpoints.get(breakpoints.size() - 1) == sequence.length()) {
            breakpoints.remove(breakpoints.size() - 1);
        }
    }

    @Override
    public void beforeReplace(Content content) {
        // Intentionally empty
    }

    @Override
    public void afterInsert(Content content, int startLine, int startColumn, int endLine, int endColumn, CharSequence insertedContent) {
        super.afterInsert(content, startLine, startColumn, endLine, endColumn, insertedContent);
        // Update line numbers
        int delta = endLine - startLine;
        if (delta != 0) {
            for (int row = findRow(startLine + 1); row < rowTable.size(); row++) {
                rowTable.get(row).line += delta;
            }
        }
        // Re-break
        breakLines(startLine, endLine);
    }

    @Override
    public void afterDelete(Content content, int startLine, int startColumn, int endLine, int endColumn, CharSequence deletedContent) {
        super.afterDelete(content, startLine, startColumn, endLine, endColumn, deletedContent);
        int delta = endLine - startLine;
        if (delta != 0) {
            int startRow = findRow(startLine);
            while (startRow < rowTable.size()) {
                int line = rowTable.get(startRow).line;
                if (line >= startLine && line <= endLine) {
                    rowTable.remove(startRow);
                } else {
                    break;
                }
            }
            for (int row = findRow(endLine + 1); row < rowTable.size(); row++) {
                rowTable.get(row).line -= delta;
            }
        }
        breakLines(startLine, startLine);
    }

    @Override
    public void onRemove(Content content, ContentLine line) {

    }

    @Override
    public void destroyLayout() {
        super.destroyLayout();
        rowTable.clear();
    }

    @Override
    public int getLineNumberForRow(int row) {
        return row >= rowTable.size() ? rowTable.get(rowTable.size() - 1).line : rowTable.get(row).line;
    }

    @Override
    public RowIterator obtainRowIterator(int initialRow) {
        return new WordwrapLayoutRowItr(initialRow);
    }

    @Override
    public int getLayoutWidth() {
        return 0;
    }

    @Override
    public int getLayoutHeight() {
        return rowTable.size() * editor.getRowHeight();
    }

    @Override
    public long getCharPositionForLayoutOffset(float xOffset, float yOffset) {
        int row = (int) (yOffset / editor.getRowHeight());
        row = Math.max(0, Math.min(row, rowTable.size() - 1));
        RowRegion region = rowTable.get(row);
        int column = (int) orderedFindCharIndex(xOffset, text.getLine(region.line), region.line, region.startColumn, region.endColumn)[0];
        return IntPair.pack(region.line, column);
    }

    @Override
    public float[] getCharLayoutOffset(int line, int column, float[] dest) {
        if (dest == null || dest.length < 2) {
            dest = new float[2];
        }
        int row = findRow(line);
        if (row < rowTable.size()) {
            RowRegion region = rowTable.get(row);
            if (region.line != line) {
                dest[0] = dest[1] = 0;
                return dest;
            }
            while (region.startColumn < column && row + 1 < rowTable.size()) {
                row++;
                region = rowTable.get(row);
                if (region.line != line || region.startColumn > column) {
                    row--;
                    region = rowTable.get(row);
                    break;
                }
            }
            dest[0] = editor.getRowHeight() * (row + 1);
            var sequence = text.getLine(region.line);
            var gtr = GraphicTextRow.obtain();
            gtr.set(sequence, region.startColumn, region.endColumn, editor.getTabWidth(), getSpans(line), editor.getTextPaint());
            dest[1] = gtr.measureText(region.startColumn, column);
            GraphicTextRow.recycle(gtr);
        } else {
            dest[0] = dest[1] = 0;
        }
        return dest;
    }

    @Override
    public int getRowCountForLine(int line) {
        int row = findRow(line);
        int count = 0;
        while (row < rowTable.size() && rowTable.get(row).line == line) {
            count++;
            row++;
        }
        return count;
    }

    static class RowRegion {

        final int startColumn;
        final int endColumn;
        int line;

        RowRegion(int line, int start, int end) {
            this.line = line;
            startColumn = start;
            endColumn = end;
        }

    }

    class WordwrapLayoutRowItr implements RowIterator {

        private final Row result;
        private int currentRow;
        private final int initRow;

        WordwrapLayoutRowItr(int initialRow) {
            initRow = currentRow = initialRow;
            result = new Row();
        }

        @Override
        public Row next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            RowRegion region = rowTable.get(currentRow);
            result.lineIndex = region.line;
            result.startColumn = region.startColumn;
            result.endColumn = region.endColumn;
            result.isLeadingRow = currentRow <= 0 || rowTable.get(currentRow - 1).line != region.line;
            currentRow++;
            return result;
        }

        @Override
        public boolean hasNext() {
            return currentRow >= 0 && currentRow < rowTable.size();
        }

        @Override
        public void reset() {
            currentRow = initRow;
        }
    }

}
