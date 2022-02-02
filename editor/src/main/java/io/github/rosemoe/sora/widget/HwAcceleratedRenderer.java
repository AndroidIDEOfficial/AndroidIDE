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
package io.github.rosemoe.sora.widget;

import android.graphics.Canvas;
import android.graphics.RenderNode;

import androidx.annotation.RequiresApi;

import java.util.concurrent.atomic.AtomicBoolean;

import io.github.rosemoe.sora.annotations.Experimental;
import io.github.rosemoe.sora.lang.styling.EmptyReader;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.text.ContentListener;
import io.github.rosemoe.sora.util.ArrayList;

/**
 * Hardware accelerated text render, which manages {@link RenderNode}
 * to speed up drawing process.
 *
 * @author Rosemoe
 */
@RequiresApi(29)
@Experimental
class HwAcceleratedRenderer implements ContentListener {

    private final CodeEditor editor;
    private final ArrayList<TextRenderNode> cache;

    public HwAcceleratedRenderer(CodeEditor editor) {
        this.editor = editor;
        cache = new ArrayList<>(64);
    }

    private boolean shouldUpdateCache() {
        return !editor.isWordwrap() && editor.isHardwareAcceleratedDrawAllowed();
    }

    public boolean invalidateInRegion(int startLine, int endLine) {
        var res = new AtomicBoolean(false);
        cache.forEach((node) -> {
            if (!node.isDirty && node.line >= startLine && node.line <= endLine) {
                node.isDirty = true;
                res.set(true);
            }
        });
        return res.get();
    }

    /**
     * Called by editor when text style changes.
     * Such as text size/typeface.
     * Also called when wordwrap state changes from true to false
     */
    public void invalidate() {
        if (shouldUpdateCache()) {
            invalidateDirectly();
        }
    }

    public void invalidateDirectly() {
        cache.forEach(node -> node.isDirty = true);
    }

    public TextRenderNode getNode(int line) {
        var size = cache.size();
        for (int i = 0; i < size; i++) {
            var node = cache.get(i);
            if (node.line == line) {
                cache.remove(i);
                cache.add(0, node);
                return node;
            }
        }
        var node = new TextRenderNode(line);
        cache.add(0, node);
        return node;
    }

    public void keepCurrentInDisplay(int start, int end) {
        var itr = cache.iterator();
        while (itr.hasNext()) {
            var node = itr.next();
            if (node.line < start || node.line > end) {
                itr.remove();
                node.renderNode.discardDisplayList();
            }
        }
    }

    public int drawLineHardwareAccelerated(Canvas canvas, int line, float offset) {
        if (!canvas.isHardwareAccelerated()) {
            throw new UnsupportedOperationException("Only hardware-accelerated canvas can be used");
        }
        var styles = editor.getStyles();
        // It's safe to use row directly because the mode is non-wordwrap
        var node = getNode(line);
        if (node.needsRecord()) {
            var spans = styles == null ? null : styles.spans;
            var reader = spans == null ? new EmptyReader() : spans.read();
            try {
                reader.moveToLine(line);
            } catch (Exception e) {
                reader = new EmptyReader();
            }
            editor.updateLineDisplayList(node.renderNode, line, reader);
            try {
                reader.moveToLine(-1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            node.isDirty = false;
        }
        canvas.save();
        canvas.translate(offset, editor.getRowTop(line) - editor.getOffsetY());
        canvas.drawRenderNode(node.renderNode);
        canvas.restore();
        return node.renderNode.getWidth();
    }

    @Override
    public void beforeReplace(Content content) {
        //Intentionally empty
    }

    @Override
    public void afterInsert(Content content, int startLine, int startColumn, int endLine, int endColumn, CharSequence insertedContent) {
        if (shouldUpdateCache()) {
            if (startLine != endLine)
                invalidateInRegion(startLine, Integer.MAX_VALUE);
        }
    }

    @Override
    public void afterDelete(Content content, int startLine, int startColumn, int endLine, int endColumn, CharSequence deletedContent) {
        if (shouldUpdateCache()) {
            int delta = endLine - startLine;
            if (delta != 0)
                invalidateInRegion(startLine, Integer.MAX_VALUE);
        }
    }

    protected static class TextRenderNode {

        /**
         * The target line of this node.
         * -1 for unavailable
         */
        public int line;
        public RenderNode renderNode;
        public boolean isDirty;

        public TextRenderNode(int line) {
            this.line = line;
            renderNode = new RenderNode("editorRenderNode");
            isDirty = true;
        }

        public boolean needsRecord() {
            return isDirty || !renderNode.hasDisplayList();
        }

    }
}
