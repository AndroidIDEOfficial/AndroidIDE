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

import io.github.rosemoe.editor.struct.BlockLine;
import io.github.rosemoe.editor.struct.HexColor;
import io.github.rosemoe.editor.struct.NavigationItem;
import io.github.rosemoe.editor.struct.Span;
import io.github.rosemoe.editor.widget.EditorColorScheme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.lsp4j.Range;

/**
 * The result of analysis
 */
public class TextAnalyzeResult {

    protected final List<BlockLine> mBlocks;
    protected final List<List<Span>> mSpanMap;
	protected final HashMap<HexColor, Integer> mHexColor;
    public Object mExtra;
    protected List<NavigationItem> mLabels;
    protected Span mLast;
    protected int mSuppressSwitch = Integer.MAX_VALUE;
    
    private final Map<Integer, List<Range>> stringMap = new HashMap<>();

    /**
     * Create a new result
     */
    public TextAnalyzeResult() {
        mLast = null;
        mSpanMap = new ArrayList<>(2048);
        mBlocks = new ArrayList<>(1024);
		mHexColor = new HashMap<>();
    }
    
    public Map<Integer, List<Range>> getStringMap() {
        return stringMap;
    }
    
    public void addStringRange(int line, Range range) {
        if(stringMap.containsKey(line)) {
            List<Range> ranges = stringMap.get(line);
            ranges.add(range);
            stringMap.put(line, ranges);
        } else {
            List<Range> ranges = new ArrayList<>();
            ranges.add(range);
            stringMap.put(line, ranges);
        }
    }
	
	public void addHexColor(HexColor index, int color) {
		mHexColor.put(index, color);
	}
	
	public HashMap<HexColor, Integer> getHexColors() {
		return mHexColor;
	}
	
	public TextAnalyzeResult setHexColors(HashMap<HexColor, Integer> colors) {
		this.mHexColor.clear();
		this.mHexColor.putAll(colors);
		return this;
	}

    /**
     * Add a new span if required (colorId is different from last)
     *
     * @param spanLine Line
     * @param column   Column
     * @param colorId  Type
     */
    public void addIfNeeded(int spanLine, int column, int colorId) {
        if (mLast != null && mLast.colorId == colorId) {
            return;
        }
        add(spanLine, Span.obtain(column, colorId));
    }

    /**
     * Add a span directly
     * Note: the line should always >= the line of span last committed
     * if two spans are on the same line, you must add them in order by their column
     *
     * @param spanLine The line position of span
     * @param span     The span
     */
    public void add(int spanLine, Span span) {
        int mapLine = mSpanMap.size() - 1;
        if (spanLine == mapLine) {
            mSpanMap.get(spanLine).add(span);
        } else if (spanLine > mapLine) {
            Span extendedSpan = mLast;
            if (extendedSpan == null) {
                extendedSpan = Span.obtain(0, EditorColorScheme.TEXT_NORMAL);
            }
            while (mapLine < spanLine) {
                List<Span> lineSpans = new ArrayList<>();
                lineSpans.add(extendedSpan.copy().setColumn(0));
                mSpanMap.add(lineSpans);
                mapLine++;
            }
            List<Span> lineSpans = mSpanMap.get(spanLine);
            if (span.column == 0) {
                lineSpans.clear();
            }
            lineSpans.add(span);
        } else {
            throw new IllegalStateException("Invalid position");
        }
        mLast = span;
    }

    /**
     * This method must be called when whole text is analyzed
     *
     * @param line The line is the line last of text
     */
    public void determine(int line) {
        int mapLine = mSpanMap.size() - 1;
        Span extendedSpan = mLast;
        if (mLast == null) {
            extendedSpan = Span.obtain(0, EditorColorScheme.TEXT_NORMAL);
        }
        while (mapLine < line) {
            List<Span> lineSpans = new ArrayList<>();
            lineSpans.add(extendedSpan.copy().setColumn(0));
            mSpanMap.add(lineSpans);
            mapLine++;
        }
    }

    /**
     * Get a new BlockLine object
     * <strong>It fields maybe not initialized with zero</strong>
     *
     * @return An idle BlockLine
     */
    public BlockLine obtainNewBlock() {
        return ObjectAllocator.obtainBlockLine();
    }

    /**
     * Add a new code block info
     *
     * @param block Info of code block
     */
    public void addBlockLine(BlockLine block) {
        mBlocks.add(block);
    }

    /**
     * Get list of code blocks
     *
     * @return code blocks
     */
    public List<BlockLine> getBlocks() {
        return mBlocks;
    }

    /**
     * Ensure the list not empty
     */
    public void addNormalIfNull() {
        if (mSpanMap.isEmpty()) {
            List<Span> spanList = new ArrayList<>();
            spanList.add(Span.obtain(0, EditorColorScheme.TEXT_NORMAL));
            mSpanMap.add(spanList);
        }
    }

    /**
     * Get code navigation list
     *
     * @return Current navigation list
     */
    public List<NavigationItem> getNavigation() {
        return mLabels;
    }

    /**
     * Set code navigation list
     *
     * @param navigation New navigation list
     */
    public void setNavigation(List<NavigationItem> navigation) {
        mLabels = navigation;
    }

    /**
     * Returns suppress switch
     *
     * @return suppress switch
     * @see TextAnalyzeResult#setSuppressSwitch(int)
     */
    public int getSuppressSwitch() {
        return mSuppressSwitch;
    }

    /**
     * Set suppress switch for editor
     * What is 'suppress switch' ?:
     * Suppress switch is a switch size for code block line drawing
     * and for the process to find out which code block the cursor is in.
     * Because the code blocks are not saved by the order of both start line and
     * end line,we are unable to know exactly when we should stop the process.
     * So without a suppress switch,it will cost a large of time to search code
     * blocks.So I added this switch.
     * A suppress switch is the code block count in the first layer code block
     * (as well as its sub code blocks).
     * If you are unsure,do not set it.
     * The default value if Integer.MAX_VALUE
     *
     * @param suppressSwitch Suppress switch
     */
    public void setSuppressSwitch(int suppressSwitch) {
        mSuppressSwitch = suppressSwitch;
    }

    /**
     * Get span map
     */
    public List<List<Span>> getSpanMap() {
        return mSpanMap;
    }
}
