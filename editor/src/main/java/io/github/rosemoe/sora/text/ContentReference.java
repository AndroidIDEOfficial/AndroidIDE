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
package io.github.rosemoe.sora.text;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Reader;

/**
 * Reference of a content due to accessed in read-only mode.
 * Access can be validated during accesses.
 * {@link io.github.rosemoe.sora.text.TextReference.ValidateFailedException} may be thrown if the check is failed.
 *
 * @author Rosemoe
 */
public class ContentReference extends TextReference {

    private final Content content;
    private final Indexer indexer;
    private final CharPosition cached;

    public ContentReference(@NonNull Content ref) {
        super(ref);
        this.content = ref;
        cached = new CharPosition();
        // Use another Indexer to query characters by index, avoiding concurrent modification to cache list
        this.indexer = new CachedIndexer(content);
    }

    @Override
    public char charAt(int index) {
        validateAccess();
        indexer.getCharPosition(index, cached);
        return content.charAt(cached.line, cached.column);
    }

    /**
     * @see Content#getLineCount()
     */
    public int getLineCount() {
        validateAccess();
        return content.getLineCount();
    }

    /**
     * @see Content#getColumnCount(int)
     */
    public int getColumnCount(int line) {
        validateAccess();
        return content.getColumnCount(line);
    }

    /**
     * @see Content#getLineString(int)
     */
    public String getLine(int line) {
        validateAccess();
        return content.getLineString(line);
    }

    /**
     * @see Content#getLineChars(int, char[])
     */
    public void getLineChars(int line, char[] dest) {
        validateAccess();
        content.getLineChars(line, dest);
    }

    /**
     * @see Content#getLine(int)#appendLineTo(StringBuilder, int)
     */
    public void appendLineTo(StringBuilder sb, int line) {
        validateAccess();
        content.getLine(line).appendTo(sb);
    }

    /**
     * Create a reader to read the text
     */
    public Reader createReader() {
        return new RefReader();
    }

    @NonNull
    @Override
    public Content getReference() {
        return (Content) super.getReference();
    }

    @Override
    public ContentReference setValidator(Validator validator) {
        super.setValidator(validator);
        return this;
    }

    private class RefReader extends Reader {

        private int markedLine, markedColumn;
        private int line;
        private int column;

        @Override
        public int read(char[] chars, int offset, int length) {
            if (chars.length < offset + length) {
                throw new IllegalArgumentException("size not enough");
            }
            int read = 0;
            while (read < length && line < getLineCount()) {
                var columnCount = getColumnCount(line);
                int toRead = Math.min(columnCount - column, length - read);
                if (toRead > 0) {
                    content.getRegionOnLine(line, column, column + toRead, chars, offset + read);
                }
                column += toRead;
                read += toRead;
                if (read < length && columnCount == column) {
                    chars[offset + read] = '\n';
                    read ++;
                    line++;
                    column = 0;
                }
            }
            if (read == 0) {
                return -1;
            }
            return read;
        }

        @Override
        public void close() {
            line = Integer.MAX_VALUE;
            column = Integer.MAX_VALUE;
        }

        @Override
        public boolean markSupported() {
            return true;
        }

        @Override
        public void mark(int readAheadLimit) throws IOException {
            markedLine = line;
            markedColumn = column;
        }

        @Override
        public void reset() throws IOException {
            line = markedLine;
            column = markedColumn;
        }
    }
}
