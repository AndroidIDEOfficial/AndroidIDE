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

import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

public class EmptyReader implements Spans.Reader {

    private final List<Span> spans;
    private boolean moved;

    public EmptyReader() {
        spans = new ArrayList<>(1);
        spans.add(Span.obtain(0, EditorColorScheme.TEXT_NORMAL));
    }


    @Override
    public void moveToLine(int line) {

    }

    @Override
    public Span getSpanAt(int index) {
        return spans.get(index);
    }

    @Override
    public int getSpanCount() {
        return 1;
    }

    @Override
    public List<Span> getSpansOnLine(int line) {
        return spans;
    }
}
