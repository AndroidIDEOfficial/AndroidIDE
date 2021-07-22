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
package io.github.rosemoe.editor.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.ContentLine;
import io.github.rosemoe.editor.util.IntPair;

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
class WordwrapLayout extends AbstractLayout {

    private final List<RowRegion> rowTable;
    private final int width;

    WordwrapLayout(CodeEditor editor, Content text) {
        super(editor, text);
        rowTable = new ArrayList<>();
        width = editor.getWidth() - (int) editor.measureTextRegionOffset() - (int) editor.getDpUnit() * 5;
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
        float currentWidth = 0;
        for (int i = 0; i < sequence.length(); i++) {
            char ch = sequence.charAt(i);
            float single = fontCache.measureChar(ch, shadowPaint);
            if (ch == '\t') {
                single = fontCache.measureChar(' ', shadowPaint) * editor.getTabWidth();
            }
            if (currentWidth + single > width) {
                int lastCommit = breakpoints.size() != 0 ? breakpoints.get(breakpoints.size() - 1) : 0;
                if (i == lastCommit) {
                    i++;
                    continue;
                }
                breakpoints.add(i);
                currentWidth = 0;
                i--;
            } else {
                currentWidth += single;
            }
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
        int column = (int) orderedFindCharIndex(xOffset, text.getLine(region.line), region.startColumn, region.endColumn)[0];
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
                if (region.line != line) {
                    row--;
                    region = rowTable.get(row);
                    break;
                }
            }
            dest[0] = editor.getRowHeight() * (row + 1);
            dest[1] = measureText(text.getLine(region.line), region.startColumn, column);
        } else {
            dest[0] = dest[1] = 0;
        }
        return dest;
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

        final Row result;
        int currentRow;

        WordwrapLayoutRowItr(int initialRow) {
            currentRow = initialRow;
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

    }

}
