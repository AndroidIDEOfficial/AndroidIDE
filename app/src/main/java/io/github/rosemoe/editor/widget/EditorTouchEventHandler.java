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

import android.content.res.Resources;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;

import io.github.rosemoe.editor.util.IntPair;

/**
 * Handles touch events of editor
 *
 * @author Rose
 */
@SuppressWarnings("CanBeFinal")
final class EditorTouchEventHandler implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {

    private final static int HIDE_DELAY = 3000;
    private final static int SELECTION_HANDLE_RESIZE_DELAY = 10;
    private final static int HIDE_DELAY_HANDLE = 5000;
    private static final long INTERACTION_END_DELAY = 100;
    private static final String TAG = "EditorTouchEventHandler";
    private final CodeEditor mEditor;
    private final OverScroller mScroller;
    protected boolean topOrBottom; //true for bottom
    protected boolean leftOrRight; //true for right
    boolean isScaling = false;
    float maxSize, minSize;
    private long mLastScroll = 0;
    private long mLastSetSelection = 0;
    private long mLastTouchedSelectionHandle = 0;
    private long mLastInteraction = 0;
    private boolean mHoldingScrollbarVertical = false;
    private boolean mHoldingScrollbarHorizontal = false;
    private boolean mHoldingInsertHandle = false;
    private float downY = 0;
    private float downX = 0;
    private SelectionHandle insert = null, left = null, right = null;
    private int mSelHandleType = -1;
    private int mTouchedHandleType = -1;

    private final static int LEFT_EDGE = 1;
    private final static int RIGHT_EDGE = 1 << 1;
    private final static int TOP_EDGE = 1 << 2;
    private final static int BOTTOM_EDGE = 1 << 3;
    private float edgeFieldSize;
    private int mEdgeFlags;
    private MotionEvent mThumb;

    /**
     * Create a event handler for the given editor
     *
     * @param editor Host editor
     */
    public EditorTouchEventHandler(CodeEditor editor) {
        mEditor = editor;
        mScroller = new OverScroller(editor.getContext());
        maxSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 32, Resources.getSystem().getDisplayMetrics());
        minSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * Whether this character is a part of word
     *
     * @param ch Character to check
     * @return Whether a part of word
     */
    private static boolean isIdentifierPart(char ch) {
        return Character.isJavaIdentifierPart(ch);
    }

    /**
     * Checks whether the provided character is a whitespace
     *
     * @param c the char to check
     * @return Whether the provided character is a whitespace
     */
    private boolean isWhitespace(char c) {
        return (c == '\t' || c == ' ' || c == '\f' || c == '\n' || c == '\r');
    }

    /**
     * Handles the selected text click event
     *
     * @param e      the MotionEvent
     * @param line   line number index
     * @param column column index in line
     */
    private void handleSelectedTextClick(MotionEvent e, int line, int column) {
		boolean isShowing1 = mEditor.getTextActionPresenter() instanceof EditorTextActionWindow && ((EditorTextActionWindow) mEditor.getTextActionPresenter()).isShowing();
        boolean isShowing2 = mEditor.getTextActionPresenter() instanceof TextActionPopupWindow && ((TextActionPopupWindow) mEditor.getTextActionPresenter()).isShowing();
        char text = mEditor.getText().charAt(line, column);
        if (isWhitespace(text) || isShowing1 || isShowing2)
            mEditor.setSelection(line, column);
        else mEditor.getTextActionPresenter().onSelectedTextClicked(e);
    }

    /**
     * Whether we should draw scroll bars
     *
     * @return whether draw scroll bars
     */
    public boolean shouldDrawScrollBar() {
        return System.currentTimeMillis() - mLastScroll < HIDE_DELAY || mHoldingScrollbarVertical || mHoldingScrollbarHorizontal;
    }

    /**
     * Hide the insert handle at once
     */
    public void hideInsertHandle() {
        if (!shouldDrawInsertHandle()) {
            return;
        }
        mLastSetSelection = 0;
        mEditor.invalidate();
    }

    /**
     * Whether the vertical scroll bar is touched
     *
     * @return Whether touched
     */
    public boolean holdVerticalScrollBar() {
        return mHoldingScrollbarVertical;
    }

    /**
     * Whether the horizontal scroll bar is touched
     *
     * @return Whether touched
     */
    public boolean holdHorizontalScrollBar() {
        return mHoldingScrollbarHorizontal;
    }

    /**
     * Whether insert handle is touched
     *
     * @return Whether touched
     */
    public boolean holdInsertHandle() {
        return mHoldingInsertHandle;
    }

    /**
     * Whether the editor should draw insert handler
     *
     * @return Whether to draw
     */
    public boolean shouldDrawInsertHandle() {
        return (System.currentTimeMillis() - mLastSetSelection < HIDE_DELAY || mHoldingInsertHandle) && checkActionWindow();
    }

    /**
     * Check whether the text action window is shown
     */
    private boolean checkActionWindow() {
        CodeEditor.EditorTextActionPresenter presenter = mEditor.mTextActionPresenter;
        if (presenter instanceof EditorTextActionWindow) {
            return !((EditorTextActionWindow) presenter).isShowing();
        }
        return true;
    }

    /**
     * Notify the editor later to hide scroll bars
     */
    public void notifyScrolled() {
        mLastScroll = System.currentTimeMillis();
        class ScrollNotifier implements Runnable {

            @Override
            public void run() {
                if (System.currentTimeMillis() - mLastScroll >= HIDE_DELAY_HANDLE) {
                    mEditor.invalidate();
                }
            }

        }
        mEditor.postDelayed(new ScrollNotifier(), HIDE_DELAY_HANDLE);
    }

    /**
     * Notify the editor later to hide insert handle
     */
    public void notifyLater() {
        mLastSetSelection = System.currentTimeMillis();
        class InvalidateNotifier implements Runnable {

            @Override
            public void run() {
                if (System.currentTimeMillis() - mLastSetSelection >= HIDE_DELAY) {
                    mEditor.invalidate();
                }
            }

        }
        mEditor.postDelayed(new InvalidateNotifier(), HIDE_DELAY);
    }

    /**
     * Notify the editor later to resize touched selection handle to normal size
     */
    public void notifyTouchedSelectionHandlerLater() {
        mLastTouchedSelectionHandle = System.currentTimeMillis();
        class InvalidateNotifier implements Runnable {

            @Override
            public void run() {
                if (System.currentTimeMillis() - mLastTouchedSelectionHandle >= SELECTION_HANDLE_RESIZE_DELAY) {
                    mEditor.invalidate();
                    mEditor.onEndTextSelect();
                }
            }
        }
        mEditor.postDelayed(new InvalidateNotifier(), SELECTION_HANDLE_RESIZE_DELAY);
    }

    private int preciousX = 0;
    private int preciousY = 0;

    public void notifyGestureInteractionEnd(int type) {
        mLastInteraction = System.currentTimeMillis();
        class InvalidateNotifier implements Runnable {
            @Override
            public void run() {
                if (type == TextComposeBasePopup.SCROLL) {
                    int x = mScroller.getCurrX();
                    int y = mScroller.getCurrY();
                    if (x - preciousX == 0 && y - preciousY == 0) {
                        mEditor.invalidate();
                        mEditor.onEndGestureInteraction();
                        preciousX = 0;
                        preciousY = 0;
                        return;
                    }
                    preciousX = x;
                    preciousY = y;
                    mEditor.postDelayed(this, INTERACTION_END_DELAY);
                } else if (System.currentTimeMillis() - mLastInteraction >= INTERACTION_END_DELAY) {
                    mEditor.invalidate();
                    mEditor.onEndGestureInteraction();
                }
            }

        }
        mEditor.postDelayed(new InvalidateNotifier(), INTERACTION_END_DELAY);
    }


    /**
     * Called by editor
     * Whether this class is handling motions by user
     *
     * @return Whether handling
     */
    protected boolean handlingMotions() {
        return holdHorizontalScrollBar() || holdVerticalScrollBar() || holdInsertHandle() || mSelHandleType != -1;
    }

    /**
     * Get scroller for editor
     *
     * @return Scroller using
     */
    protected OverScroller getScroller() {
        return mScroller;
    }

    /**
     * Reset scroll state
     */
    protected void reset() {
        mScroller.startScroll(0, 0, 0, 0, 0);
    }

    /**
     * Handle events apart from detectors
     *
     * @param e The event editor received
     * @return Whether this touch event is handled by this class
     */
    public boolean onTouchEvent(MotionEvent e) {
        if (edgeFieldSize == 0) {
            edgeFieldSize = mEditor.getDpUnit() * 25;
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHoldingScrollbarVertical = mHoldingScrollbarHorizontal = false;
                RectF rect = mEditor.getVerticalScrollBarRect();
                if (rect.contains(e.getX(), e.getY())) {
                    mHoldingScrollbarVertical = true;
                    downY = e.getY();
                    mEditor.hideAutoCompleteWindow(); mEditor.hideDiagnosticWindow();
                }
                rect = mEditor.getHorizontalScrollBarRect();
                if (rect.contains(e.getX(), e.getY())) {
                    mHoldingScrollbarHorizontal = true;
                    downX = e.getX();
                    mEditor.hideAutoCompleteWindow(); mEditor.hideDiagnosticWindow();
                }
                if (mHoldingScrollbarVertical && mHoldingScrollbarHorizontal) {
                    mHoldingScrollbarHorizontal = false;
                }
                if (mHoldingScrollbarVertical || mHoldingScrollbarHorizontal) {
                    mEditor.invalidate();
                }
                if (shouldDrawInsertHandle() && mEditor.getInsertHandleRect().contains(e.getX(), e.getY())) {
                    mHoldingInsertHandle = true;
                    downY = e.getY();
                    downX = e.getX();

                    insert = new SelectionHandle(SelectionHandle.BOTH);
                }
                boolean left = mEditor.getLeftHandleRect().contains(e.getX(), e.getY());
                boolean right = mEditor.getRightHandleRect().contains(e.getX(), e.getY());
                if (left || right) {
                    if (left) {
                        mSelHandleType = SelectionHandle.LEFT;
                        mTouchedHandleType = SelectionHandle.LEFT;
                    } else {
                        mSelHandleType = SelectionHandle.RIGHT;
                        mTouchedHandleType = SelectionHandle.RIGHT;
                    }
                    downY = e.getY();
                    downX = e.getX();

                    this.left = new SelectionHandle(SelectionHandle.LEFT);
                    this.right = new SelectionHandle(SelectionHandle.RIGHT);

                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mHoldingScrollbarVertical) {
                    float movedDis = e.getY() - downY;
                    downY = e.getY();
                    float all = mEditor.mLayout.getLayoutHeight() + mEditor.getHeight() / 2f;
                    float dy = movedDis / mEditor.getHeight() * all;
                    scrollBy(0, dy);
                    return true;
                }
                if (mHoldingScrollbarHorizontal) {
                    float movedDis = e.getX() - downX;
                    downX = e.getX();
                    float all = mEditor.getScrollMaxX() + mEditor.getWidth();
                    float dx = movedDis / mEditor.getWidth() * all;
                    scrollBy(dx, 0);
                    return true;
                }
                return handleSelectionChange(e);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mHoldingScrollbarVertical) {
                    mHoldingScrollbarVertical = false;
                    mEditor.invalidate();
                    mLastScroll = System.currentTimeMillis();
                    notifyScrolled();
                }
                if (mHoldingScrollbarHorizontal) {
                    mHoldingScrollbarHorizontal = false;
                    mEditor.invalidate();
                    mLastScroll = System.currentTimeMillis();
                    notifyScrolled();
                }
                if (mHoldingInsertHandle) {
                    mHoldingInsertHandle = false;
                    mEditor.invalidate();
                    notifyLater();
                }
                mSelHandleType = -1;

                // check touch event is related to text selection or not
                if (mTouchedHandleType > -1) {
                    mTouchedHandleType = -1;
                    notifyTouchedSelectionHandlerLater();
                }
                stopEdgeScroll();
                break;
        }
        return false;
    }

    private boolean handleSelectionChange(MotionEvent e) {
        if (mHoldingInsertHandle) {
            insert.applyPosition(e);
            scrollIfThumbReachesEdge(e);
            return true;
        }
        switch (mSelHandleType) {
            case SelectionHandle.LEFT:
                this.left.applyPosition(e);
                scrollIfThumbReachesEdge(e);
                return true;
            case SelectionHandle.RIGHT:
                this.right.applyPosition(e);
                scrollIfThumbReachesEdge(e);
                return true;
        }
        return false;
    }

    private void handleSelectionChange2(MotionEvent e) {
        if (mHoldingInsertHandle) {
            insert.applyPosition(e);
        } else {
            switch (mSelHandleType) {
                case SelectionHandle.LEFT:
                    this.left.applyPosition(e);
                    break;
                case SelectionHandle.RIGHT:
                    this.right.applyPosition(e);
                    break;
            }
        }
    }

    private int computeEdgeFlags(float x, float y) {
        int flags = 0;
        if (x < edgeFieldSize) {
            flags |= LEFT_EDGE;
        }
        if (y < edgeFieldSize) {
            flags |= TOP_EDGE;
        }
        if (x > mEditor.getWidth() - edgeFieldSize) {
            flags |= RIGHT_EDGE;
        }
        if (y > mEditor.getHeight() - edgeFieldSize) {
            flags |= BOTTOM_EDGE;
        }
        return flags;
    }

    private void scrollIfThumbReachesEdge(MotionEvent e) {
        int flag = computeEdgeFlags(e.getX(), e.getY());
        int initialDelta = (int) (8 * mEditor.getDpUnit());
        if (flag != 0 && mEdgeFlags == 0) {
            mEdgeFlags = flag;
            mThumb = MotionEvent.obtain(e);
            mEditor.post(new EdgeScrollRunnable(initialDelta));
        } else if (flag == 0) {
            stopEdgeScroll();
        } else {
            mEdgeFlags = flag;
            mThumb = MotionEvent.obtain(e);
        }
    }

    private boolean isSameSign(int a, int b) {
        return (a < 0 && b < 0) || (a > 0 && b > 0);
    }

    private void stopEdgeScroll() {
        mEdgeFlags = 0;
    }

    protected void scrollBy(float distanceX, float distanceY) {
        if (mEditor.getTextActionPresenter() != null) {
            if (mEditor.getTextActionPresenter() instanceof TextActionPopupWindow) {
                mEditor.getTextActionPresenter().onUpdate(TextActionPopupWindow.SCROLL);
            } else {
                mEditor.getTextActionPresenter().onUpdate();
            }
        }
        mEditor.hideAutoCompleteWindow();
        mEditor.hideDiagnosticWindow();
        int endX = mScroller.getCurrX() + (int) distanceX;
        int endY = mScroller.getCurrY() + (int) distanceY;
        endX = Math.max(endX, 0);
        endY = Math.max(endY, 0);
        endY = Math.min(endY, mEditor.getScrollMaxY());
        endX = Math.min(endX, mEditor.getScrollMaxX());
        mScroller.startScroll(mScroller.getCurrX(),
                mScroller.getCurrY(),
                endX - mScroller.getCurrX(),
                endY - mScroller.getCurrY(), 0);
        mEditor.invalidate();
    }

    protected int getTouchedHandleType() {
        return mTouchedHandleType;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mEditor.showSoftInput();
        mScroller.forceFinished(true);
        long res = mEditor.getPointPositionOnScreen(e.getX(), e.getY());
        int line = IntPair.getFirst(res);
        int column = IntPair.getSecond(res);
        if (mEditor.getCursor().isSelected() && mEditor.getCursor().isInSelectedRegion(line, column) && !mEditor.isOverMaxY(e.getY())) {
            handleSelectedTextClick(e, line, column);
        } else {
            notifyLater();
            int oldLine = mEditor.getCursor().getLeftLine();
            int oldColumn = mEditor.getCursor().getLeftColumn();
            if (line == oldLine && column == oldColumn) {
                if (mEditor.mTextActionPresenter instanceof EditorTextActionWindow) {
                    EditorTextActionWindow window = (EditorTextActionWindow) mEditor.mTextActionPresenter;
                    if (window.isShowing()) {
                        window.hide();
                    } else {
                        window.onBeginTextSelect();
                        window.onSelectedTextClicked(e);
                    }
                }
            } else {
                mEditor.setSelection(line, column);
                mEditor.hideAutoCompleteWindow();
                mEditor.hideDiagnosticWindow();
            }
        }
        mEditor.performClick();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (mEditor.mTextActionPresenter instanceof TextActionPopupWindow) {
            handleLongPressForModifiedTextAction(e);
            return;
        }
        if (mEditor.getCursor().isSelected() || e.getPointerCount() != 1) {
            return;
        }
        long res = mEditor.getPointPositionOnScreen(e.getX(), e.getY());
        int line = IntPair.getFirst(res);
        int column = IntPair.getSecond(res);
        //Find word edges
        int startLine = line, endLine = line;
        int startColumn = column;
        while (startColumn > 0 && isIdentifierPart(mEditor.getText().charAt(line, startColumn - 1))) {
            startColumn--;
        }
        int maxColumn = mEditor.getText().getColumnCount(line);
        int endColumn = column;
        while (endColumn < maxColumn && isIdentifierPart(mEditor.getText().charAt(line, endColumn))) {
            endColumn++;
        }
        mEditor.setSelectionRegion(startLine, startColumn, endLine, endColumn);
    }

    private void handleLongPressForModifiedTextAction(MotionEvent e) {
        if (mEditor.getCursor().isSelected() || e.getPointerCount() != 1) {
            return;
        }
        long res = mEditor.getPointPositionOnScreen(e.getX(), e.getY());
        int line = IntPair.getFirst(res);
        int column = IntPair.getSecond(res);
        //Find word edges
        int startLine = line, endLine = line;
        int startColumn = column;
        while (startColumn > 0 && isIdentifierPart(mEditor.getText().charAt(line, startColumn - 1))) {
            startColumn--;
        }
        int maxColumn = mEditor.getText().getColumnCount(line);
        int endColumn = column;
        while (endColumn < maxColumn && isIdentifierPart(mEditor.getText().charAt(line, endColumn))) {
            endColumn++;
        }
        if (startLine == endLine && startColumn == endColumn) {
            mEditor.showTextActionPopup();
        } else {
            mEditor.setSelectionRegion(startLine, startColumn, endLine, endColumn);
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if(e1 == null || e2 == null)
			return false;
        if (mEditor.getTextActionPresenter() instanceof TextActionPopupWindow) {
            mEditor.getTextActionPresenter().onUpdate(TextActionPopupWindow.SCROLL);
        } else {
            mEditor.getTextActionPresenter().onUpdate();
        }
        int endX = mScroller.getCurrX() + (int) distanceX;
        int endY = mScroller.getCurrY() + (int) distanceY;
        endX = Math.max(endX, 0);
        endY = Math.max(endY, 0);
        endY = Math.min(endY, mEditor.getScrollMaxY());
        endX = Math.min(endX, mEditor.getScrollMaxX());
        boolean notifyY = true;
        boolean notifyX = true;
        if (!mEditor.getVerticalEdgeEffect().isFinished() && !mEditor.getVerticalEdgeEffect().isRecede()) {
            endY = mScroller.getCurrY();
            float displacement = Math.max(0, Math.min(1, e2.getX() / mEditor.getWidth()));
            mEditor.getVerticalEdgeEffect().onPull((topOrBottom ? distanceY : -distanceY) / mEditor.getMeasuredHeight(), !topOrBottom ? displacement : 1 - displacement);
            notifyY = false;
        }
        if (!mEditor.getHorizontalEdgeEffect().isFinished() && !mEditor.getHorizontalEdgeEffect().isRecede()) {
            endX = mScroller.getCurrX();
            float displacement = Math.max(0, Math.min(1, e2.getY() / mEditor.getHeight()));
            mEditor.getHorizontalEdgeEffect().onPull((leftOrRight ? distanceX : -distanceX) / mEditor.getMeasuredWidth(), !leftOrRight ? 1 - displacement : displacement);
            notifyX = false;
        }
        mScroller.startScroll(mScroller.getCurrX(),
                mScroller.getCurrY(),
                endX - mScroller.getCurrX(),
                endY - mScroller.getCurrY(), 0);
        final float minOverPull = 0;
        if (notifyY && mScroller.getCurrY() + distanceY <= -minOverPull) {
            mEditor.getVerticalEdgeEffect().onPull(-distanceY / mEditor.getMeasuredHeight(), Math.max(0, Math.min(1, e2.getX() / mEditor.getWidth())));
            topOrBottom = false;
        }
        if (notifyY && mScroller.getCurrY() + distanceY >= mEditor.getScrollMaxY() + minOverPull) {
            mEditor.getVerticalEdgeEffect().onPull(distanceY / mEditor.getMeasuredHeight(), Math.max(0, Math.min(1, e2.getX() / mEditor.getWidth())));
            topOrBottom = true;
        }
        if (notifyX && mScroller.getCurrX() + distanceX <= -minOverPull) {
            mEditor.getHorizontalEdgeEffect().onPull(-distanceX / mEditor.getMeasuredWidth(), Math.max(0, Math.min(1, e2.getY() / mEditor.getHeight())));
            leftOrRight = false;
        }
        if (notifyX && mScroller.getCurrX() + distanceX >= mEditor.getScrollMaxX() + minOverPull) {
            mEditor.getHorizontalEdgeEffect().onPull(distanceX / mEditor.getMeasuredWidth(), Math.max(0, Math.min(1, e2.getY() / mEditor.getHeight())));
            leftOrRight = true;
        }
        mEditor.invalidate();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (mEditor.isDrag()) {
            return false;
        }
        // If we do not finish it here, it can produce a high speed and cause the final scroll range to be broken, even a NaN for velocity
        mScroller.forceFinished(true);
        mScroller.fling(mScroller.getCurrX(),
                mScroller.getCurrY(),
                (int) -velocityX,
                (int) -velocityY,
                0,
                mEditor.getScrollMaxX(),
                0,
                mEditor.getScrollMaxY(),
                mEditor.isOverScrollEnabled() && !mEditor.isWordwrap() ? (int) (20 * mEditor.getDpUnit()) : 0,
                mEditor.isOverScrollEnabled() ? (int) (20 * mEditor.getDpUnit()) : 0);
        mEditor.invalidate();
        float minVe = mEditor.getDpUnit() * 2000;
        if (Math.abs(velocityX) >= minVe || Math.abs(velocityY) >= minVe) {
            notifyScrolled();
            mEditor.hideAutoCompleteWindow();
            mEditor.hideDiagnosticWindow();
        }
        if (Math.abs(velocityX) >= minVe / 2f) {
            mEditor.getHorizontalEdgeEffect().finish();
        }
        if (Math.abs(velocityY) >= minVe) {
            mEditor.getVerticalEdgeEffect().finish();
        }
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (mEditor.isScalable()) {
            float newSize = mEditor.getTextSizePx() * detector.getScaleFactor();
            if (newSize < minSize || newSize > maxSize) {
                return false;
            }
            int firstVisible = mEditor.getFirstVisibleRow();
            float top = mScroller.getCurrY() - firstVisible * mEditor.getRowHeight();
            int height = mEditor.getRowHeight();
            mEditor.setTextSizePxDirect(newSize);
            mEditor.invalidate();
            float newY = firstVisible * mEditor.getRowHeight() + top * mEditor.getRowHeight() / height;
            mScroller.startScroll(mScroller.getCurrX(), (int) newY, 0, 0, 0);
            isScaling = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        mScroller.forceFinished(true);
        return mEditor.isScalable();
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        isScaling = false;
        mEditor.createLayout();
        mEditor.invalidate();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return mEditor.isEnabled();
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        onLongPress(e);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }


    /**
     * This is a helper for EventHandler to control handles
     */
    @SuppressWarnings("CanBeFinal")
    class SelectionHandle {

        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        public static final int BOTH = 2;

        public int type;

        /**
         * Create a handle
         *
         * @param type Type :left,right,both
         */
        public SelectionHandle(int type) {
            this.type = type;
        }

        /**
         * Handle the event
         *
         * @param e Event sent by EventHandler
         */
        public void applyPosition(MotionEvent e) {
            float targetX = mScroller.getCurrX() + e.getX();
            float targetY = mScroller.getCurrY() + e.getY() - mEditor.getInsertHandleRect().height() * 4 / 3;
            int line = IntPair.getFirst(mEditor.getPointPosition(0, targetY));
            if (line >= 0 && line < mEditor.getLineCount()) {
                int column = IntPair.getSecond(mEditor.getPointPosition(targetX, targetY));
                int lastLine = type == RIGHT ? mEditor.getCursor().getRightLine() : mEditor.getCursor().getLeftLine();
                int lastColumn = type == RIGHT ? mEditor.getCursor().getLeftColumn() : mEditor.getCursor().getLeftColumn();
                int anotherLine = type != RIGHT ? mEditor.getCursor().getRightLine() : mEditor.getCursor().getLeftLine();
                int anotherColumn = type != RIGHT ? mEditor.getCursor().getRightColumn() : mEditor.getCursor().getLeftColumn();

                if (line != lastLine || column != lastColumn) {
                    switch (type) {
                        case BOTH:
                            mEditor.cancelAnimation();
                            mEditor.setSelection(line, column, false);
                            break;
                        case RIGHT:
                            if (anotherLine > line || (anotherLine == line && anotherColumn > column)) {
                                //Swap type
                                EditorTouchEventHandler.this.mSelHandleType = LEFT;
                                this.type = LEFT;
                                left.type = RIGHT;
                                SelectionHandle tmp = right;
                                right = left;
                                left = tmp;
                                mEditor.setSelectionRegion(line, column, anotherLine, anotherColumn, false);
                            } else {
                                mEditor.setSelectionRegion(anotherLine, anotherColumn, line, column, false);
                            }
                            break;
                        case LEFT:
                            if (anotherLine < line || (anotherLine == line && anotherColumn < column)) {
                                //Swap type
                                EditorTouchEventHandler.this.mSelHandleType = RIGHT;
                                this.type = RIGHT;
                                right.type = LEFT;
                                SelectionHandle tmp = right;
                                right = left;
                                left = tmp;
                                mEditor.setSelectionRegion(anotherLine, anotherColumn, line, column, false);
                            } else {
                                mEditor.setSelectionRegion(line, column, anotherLine, anotherColumn, false);
                            }
                            break;
                    }
                }
            }

            if (mEditor.getTextActionPresenter() instanceof TextActionPopupWindow) {
                mEditor.getTextActionPresenter().onUpdate(TextActionPopupWindow.DRAG);
            } else {
                mEditor.getTextActionPresenter().onUpdate();
            }
        }

    }

    /**
     * Runnable for controlling auto-scrolling when thumb reaches the edges of editor
     */
    private class EdgeScrollRunnable implements Runnable {
        private final static int MAX_FACTOR = 25;
        private final static float INCREASE_FACTOR = 1.06f;

        private int initialDelta;
        private int deltaHorizontal;
        private int deltaVertical;
        private int lastDx, lastDy;
        private int factorX, factorY;

        public EdgeScrollRunnable(int initDelta) {
            initialDelta = deltaHorizontal = deltaVertical = initDelta;
        }

        @Override
        public void run() {
            int dx = (((mEdgeFlags & LEFT_EDGE) != 0) ? -deltaHorizontal : 0) + (((mEdgeFlags & RIGHT_EDGE) != 0) ? deltaHorizontal : 0);
            int dy = (((mEdgeFlags & TOP_EDGE) != 0) ? -deltaVertical : 0) + (((mEdgeFlags & BOTTOM_EDGE) != 0) ? deltaVertical : 0);
            if (dx > 0) {
                // Check whether there is content at right
                int line;
                if (mHoldingInsertHandle || mSelHandleType == SelectionHandle.LEFT) {
                    line = mEditor.getCursor().getLeftLine();
                } else {
                    line = mEditor.getCursor().getRightLine();
                }
                int column = mEditor.getText().getColumnCount(line);
                // Do not scroll too far from text region of this line
                float maxOffset = mEditor.measureTextRegionOffset() + mEditor.mLayout.getCharLayoutOffset(line, column)[1] - mEditor.getWidth() * 0.85f;
                if (mScroller.getCurrX() > maxOffset) {
                    dx = 0;
                }
            }
            scrollBy(dx, dy);

            // Speed up if we are scrolling in the direction
            if (isSameSign(dx, lastDx)) {
                if (factorX < MAX_FACTOR) {
                    factorX++;
                    deltaHorizontal *= INCREASE_FACTOR;
                }
            } else {
                // Recover initial speed because direction changed
                deltaHorizontal = initialDelta;
                factorX = 0;
            }
            if (isSameSign(dy, lastDy)) {
                if (factorY < MAX_FACTOR) {
                    factorY++;
                    deltaVertical *= INCREASE_FACTOR;
                }
            } else {
                deltaVertical = initialDelta;
                factorY = 0;
            }
            lastDx = dx;
            lastDy = dy;

            // Update selection
            handleSelectionChange2(mThumb);

            // Post for animation
            if (mEdgeFlags != 0) {
                mEditor.postDelayed(this, 10);
            }
        }
    }
}

