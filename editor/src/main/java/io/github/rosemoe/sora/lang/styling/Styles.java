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
import java.util.Collections;
import java.util.List;

import io.github.rosemoe.sora.data.ObjectAllocator;
import io.github.rosemoe.sora.text.CharPosition;

/**
 * This class stores styles of text and other decorations in editor related to code.
 * <p>
 * Note that this does not save any information related to languages. No extra space is provided
 * for communication between analyzers and auto-completion. You should manage it by yourself.
 */
public class Styles {

    public Spans spans;

    public List<CodeBlock> blocks;

    public int suppressSwitch = Integer.MAX_VALUE;

    public Styles() {
        this(null);
    }

    public Styles(Spans spans) {
        this(spans, true);
    }

    public Styles(Spans spans, boolean initCodeBlocks) {
        this.spans = spans;
        if (initCodeBlocks) {
            blocks = new ArrayList<>(128);
        }
    }

    /**
     * Get analyzed spans
     */
    public Spans getSpans() {
        return spans;
    }

    /**
     * Get a new BlockLine object
     *
     * @return An idle BlockLine
     */
    public CodeBlock obtainNewBlock() {
        return ObjectAllocator.obtainBlockLine();
    }

    /**
     * Add a new code block info
     *
     * @param block Info of code block
     */
    public void addCodeBlock(CodeBlock block) {
        blocks.add(block);
    }

    /**
     * Returns suppress switch
     *
     * @return suppress switch
     * @see Styles#setSuppressSwitch(int)
     */
    public int getSuppressSwitch() {
        return suppressSwitch;
    }

    /**
     * Set suppress switch for editor
     * What is 'suppress switch' ?:
     * <p>
     * Suppress switch is a switch size for code block line drawing
     * and for the process to find out which code block the cursor is in.
     * Because the code blocks are not saved by the order of both start line and
     * end line,we are unable to know exactly when we should stop the process.
     * So without a suppressing switch,it will cost a large of time to search code
     * blocks.
     * <p>
     * A suppressing switch is the code block count in the first layer code block
     * (as well as its sub code blocks).
     * If you are unsure,do not set it.
     * <p>
     * The default value is Integer.MAX_VALUE
     */
    public void setSuppressSwitch(int suppressSwitch) {
        this.suppressSwitch = suppressSwitch;
    }

    /**
     * Adjust styles on insert.
     */
    public void adjustOnInsert(CharPosition start, CharPosition end) {
        spans.adjustOnInsert(start, end);
        if (blocks != null)
            BlocksUpdater.update(blocks, start.line, end.line - start.line);
    }

    /**
     * Adjust styles on delete.
     */
    public void adjustOnDelete(CharPosition start, CharPosition end) {
        spans.adjustOnDelete(start, end);
        if (blocks != null)
            BlocksUpdater.update(blocks, start.line, start.line - end.line);
    }

    /**
     * Do some extra work before finally send the result to editor.
     */
    public void finishBuilding() {
        int pre = -1;
        var sort = false;
        for (int i = 0; i < blocks.size() - 1; i++) {
            var cur = blocks.get(i + 1).endLine;
            if (pre > cur) {
                sort = true;
                break;
            }
            pre = cur;
        }
        if (sort) {
            Collections.sort(blocks, CodeBlock.COMPARATOR_END);
        }
    }

}
