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

import android.content.res.Resources;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;

import io.github.rosemoe.sora.event.ClickEvent;
import io.github.rosemoe.sora.event.DoubleClickEvent;
import io.github.rosemoe.sora.event.HandleStateChangeEvent;
import io.github.rosemoe.sora.event.LongPressEvent;
import io.github.rosemoe.sora.event.ScrollEvent;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.text.ICUUtils;
import io.github.rosemoe.sora.util.IntPair;
import io.github.rosemoe.sora.widget.component.Magnifier;
import io.github.rosemoe.sora.widget.style.SelectionHandleStyle;

/**
 * Handles touch events of editor
 *
 * @author Rose
 */
@SuppressWarnings("CanBeFinal")
public final class EditorTouchEventHandler implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {

    private final static int HIDE_DELAY = 3000;
    private final static int SELECTION_HANDLE_RESIZE_DELAY = 180;
    private final static int HIDE_DELAY_HANDLE = 5000;
    private static final long INTERACTION_END_DELAY = 250;
    private static final String TAG = "EditorTouchEventHandler";
    private final CodeEditor mEditor;
    private final OverScroller mScroller;
    boolean topOrBottom; //true for bottom
    boolean leftOrRight; //true for right
    boolean isScaling = false;
    float maxSize, minSize;
    private long mLastScroll = 0;
    private long mLastSetSelection = 0;
    private boolean mHoldingScrollbarVertical = false;
    private boolean mHoldingScrollbarHorizontal = false;
    private boolean mHoldingInsertHandle = false;
    private float downY = 0;
    private float downX = 0;
    private SelectionHandle insert = null, left = null, right = null;
    int mSelHandleType = -1;
    private int mTouchedHandleType = -1;
    Magnifier mMagnifier;

    private final static int LEFT_EDGE = 1;
    private final static int RIGHT_EDGE = 1 << 1;
    private final static int TOP_EDGE = 1 << 2;
    private final static int BOTTOM_EDGE = 1 << 3;
    private float edgeFieldSize;
    private int mEdgeFlags;
    private MotionEvent mThumb;
    float mMotionX, mMotionY;

    /**
     * Create an event handler for the given editor
     *
     * @param editor Host editor
     */
    public EditorTouchEventHandler(CodeEditor editor) {
        mEditor = editor;
        mScroller = new OverScroller(editor.getContext());
        maxSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 32, Resources.getSystem().getDisplayMetrics());
        minSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6, Resources.getSystem().getDisplayMetrics());
        mMagnifier = new Magnifier(editor);
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

    public boolean hasAnyHeldHandle() {
        return holdInsertHandle() || mSelHandleType != -1;
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
        return (System.currentTimeMillis() - mLastSetSelection < HIDE_DELAY || mHoldingInsertHandle);
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
     * Called by editor
     * Whether this class is handling motions by user
     *
     * @return Whether handling
     */
    public boolean handlingMotions() {
        return holdHorizontalScrollBar() || holdVerticalScrollBar() || holdInsertHandle() || mSelHandleType != -1;
    }

    /**
     * Get scroller for editor
     *
     * @return Scroller using
     */
    public OverScroller getScroller() {
        return mScroller;
    }

    /**
     * Reset scroll state
     */
    public void reset() {
        mScroller.startScroll(0, 0, 0, 0, 0);
    }

    public void updateMagnifier(MotionEvent e) {
        if (mEdgeFlags != 0) {
            dismissMagnifier();
            return;
        }
        if (mMagnifier.isEnabled()) {
            var height = Math.max(Math.max(mEditor.getInsertHandleDescriptor().position.height(), mEditor.getLeftHandleDescriptor().position.height()), mEditor.getRightHandleDescriptor().position.height());
            mMagnifier.show((int) e.getX(), (int) (e.getY() - height / 2 - mEditor.getRowHeight()));
        }
    }

    public void dismissMagnifier() {
        mMagnifier.dismiss();
    }

    /**
     * Handle events apart from detectors
     *
     * @param e The event editor received
     * @return Whether this touch event is handled by this class
     */
    public boolean onTouchEvent(MotionEvent e) {
        if (edgeFieldSize == 0) {
            edgeFieldSize = mEditor.getDpUnit() * 18;
        }
        mMotionY = e.getY();
        mMotionX = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mHoldingScrollbarVertical = mHoldingScrollbarHorizontal = false;
                RectF rect = mEditor.getVerticalScrollBarRect();
                if (rect.contains(e.getX(), e.getY())) {
                    mHoldingScrollbarVertical = true;
                    downY = e.getY();
                    mEditor.hideAutoCompleteWindow();
                }
                rect = mEditor.getHorizontalScrollBarRect();
                if (rect.contains(e.getX(), e.getY())) {
                    mHoldingScrollbarHorizontal = true;
                    downX = e.getX();
                    mEditor.hideAutoCompleteWindow();
                }
                if (mHoldingScrollbarVertical && mHoldingScrollbarHorizontal) {
                    mHoldingScrollbarHorizontal = false;
                }
                if (mHoldingScrollbarVertical || mHoldingScrollbarHorizontal) {
                    mEditor.invalidate();
                }
                if (shouldDrawInsertHandle() && mEditor.getInsertHandleDescriptor().position.contains(e.getX(), e.getY())) {
                    mHoldingInsertHandle = true;
                    downY = e.getY();
                    downX = e.getX();
                    updateMagnifier(e);
                    insert = new SelectionHandle(SelectionHandle.BOTH);
                    dispatchHandle(HandleStateChangeEvent.HANDLE_TYPE_INSERT, true);
                }
                boolean left = mEditor.getLeftHandleDescriptor().position.contains(e.getX(), e.getY());
                boolean right = mEditor.getRightHandleDescriptor().position.contains(e.getX(), e.getY());
                if (left || right) {
                    if (left) {
                        mSelHandleType = SelectionHandle.LEFT;
                        mTouchedHandleType = SelectionHandle.LEFT;
                    } else {
                        mSelHandleType = SelectionHandle.RIGHT;
                        mTouchedHandleType = SelectionHandle.RIGHT;
                    }
                    dispatchHandle(mSelHandleType, true);
                    downY = e.getY();
                    downX = e.getX();
                    updateMagnifier(e);

                    this.left = new SelectionHandle(SelectionHandle.LEFT);
                    this.right = new SelectionHandle(SelectionHandle.RIGHT);
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
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
                if (handleSelectionChange(e)) {
                    updateMagnifier(e);
                    if (mTouchedHandleType != -1 || holdInsertHandle()) {
                        mEditor.invalidate();
                    }
                    return true;
                } else {
                    return false;
                }
            }
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
                    dispatchHandle(HandleStateChangeEvent.HANDLE_TYPE_INSERT, false);
                }
                if (mSelHandleType != -1) {
                    dispatchHandle(mSelHandleType, false);
                    mSelHandleType = -1;
                }
                mEditor.invalidate();
                // check touch event is related to text selection or not
                if (mTouchedHandleType > -1) {
                    mTouchedHandleType = -1;
                }
                stopEdgeScroll();
                dismissMagnifier();
                break;
        }
        return false;
    }

    private void dispatchHandle(int type, boolean held) {
        mEditor.dispatchEvent(new HandleStateChangeEvent(mEditor, type, held));
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

    public void scrollIfThumbReachesEdge(MotionEvent e) {
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

    public void stopEdgeScroll() {
        mEdgeFlags = 0;
    }

    public void scrollBy(float distanceX, float distanceY) {
        scrollBy(distanceX, distanceY, false);
    }

    public void scrollBy(float distanceX, float distanceY, boolean smooth) {
        mEditor.hideAutoCompleteWindow();
        int endX = mScroller.getCurrX() + (int) distanceX;
        int endY = mScroller.getCurrY() + (int) distanceY;
        endX = Math.max(endX, 0);
        endY = Math.max(endY, 0);
        endY = Math.min(endY, mEditor.getScrollMaxY());
        endX = Math.min(endX, mEditor.getScrollMaxX());
        mEditor.dispatchEvent(new ScrollEvent(mEditor, mScroller.getCurrX(),
                mScroller.getCurrY(), endX, endY, ScrollEvent.CAUSE_USER_DRAG));
        if (smooth) {
            mScroller.startScroll(mScroller.getCurrX(),
                    mScroller.getCurrY(),
                    endX - mScroller.getCurrX(),
                    endY - mScroller.getCurrY());
        } else {
            mScroller.startScroll(mScroller.getCurrX(),
                    mScroller.getCurrY(),
                    endX - mScroller.getCurrX(),
                    endY - mScroller.getCurrY(), 0);
        }
        mEditor.invalidate();
    }

    public int getTouchedHandleType() {
        return mTouchedHandleType;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mEditor.showSoftInput();
        mScroller.forceFinished(true);
        long res = mEditor.getPointPositionOnScreen(e.getX(), e.getY());
        int line = IntPair.getFirst(res);
        int column = IntPair.getSecond(res);
        mEditor.performClick();
        if (mEditor.dispatchEvent(new ClickEvent(mEditor, mEditor.getText().getIndexer().getCharPosition(line, column), e))) {
            return true;
        }
        notifyLater();
        mEditor.setSelection(line, column, SelectionChangeEvent.CAUSE_TAP);
        mEditor.hideAutoCompleteWindow();
        return true;
    }

    private void selectWord(int line, int column) {
        // Find word edges
        int startLine = line, endLine = line;
        var lineObj = mEditor.getText().getLine(line);
        long edges = ICUUtils.getWordEdges(lineObj, column);
        int startColumn = IntPair.getFirst(edges);
        int endColumn = IntPair.getSecond(edges);
        if (startColumn == endColumn) {
            if (startColumn > 0) {
                startColumn--;
            } else if (endColumn < lineObj.length()) {
                endColumn++;
            } else {
                if (line > 0) {
                    int lastColumn = mEditor.getText().getColumnCount(line - 1);
                    startLine = line - 1;
                    startColumn = lastColumn;
                } else if (line < mEditor.getLineCount() - 1) {
                    endLine = line + 1;
                    endColumn = 0;
                }
            }
        }
        mEditor.setSelectionRegion(startLine, startColumn, endLine, endColumn, SelectionChangeEvent.CAUSE_LONG_PRESS);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (mEditor.getCursor().isSelected() || e.getPointerCount() != 1) {
            return;
        }
        long res = mEditor.getPointPositionOnScreen(e.getX(), e.getY());
        int line = IntPair.getFirst(res);
        int column = IntPair.getSecond(res);
        if (mEditor.dispatchEvent(new LongPressEvent(mEditor, mEditor.getText().getIndexer().getCharPosition(line, column), e))) {
            return;
        }
        selectWord(line, column);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
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
            float distance = (topOrBottom ? distanceY : -distanceY) / mEditor.getMeasuredHeight();
            if (distance < -0.005) {
                mEditor.getVerticalEdgeEffect().finish();
            } else {
                mEditor.getVerticalEdgeEffect().onPull(distance, !topOrBottom ? displacement : 1 - displacement);
            }
            notifyY = false;
        }
        if (!mEditor.getHorizontalEdgeEffect().isFinished() && !mEditor.getHorizontalEdgeEffect().isRecede()) {
            endX = mScroller.getCurrX();
            float displacement = Math.max(0, Math.min(1, e2.getY() / mEditor.getHeight()));
            float distance = (leftOrRight ? distanceX : -distanceX) / mEditor.getMeasuredWidth();
            if (distance < -0.005) {
                mEditor.getHorizontalEdgeEffect().finish();
            } else {
                mEditor.getHorizontalEdgeEffect().onPull(distance, !leftOrRight ? 1 - displacement : displacement);
            }
            notifyX = false;
        }
        mScroller.startScroll(mScroller.getCurrX(),
                mScroller.getCurrY(),
                endX - mScroller.getCurrX(),
                endY - mScroller.getCurrY(), 0);
        mEditor.updateCompletionWindowPosition(false);
        final float minOverPull = 2f;
        if (notifyY && mScroller.getCurrY() + distanceY < -minOverPull) {
            mEditor.getVerticalEdgeEffect().onPull(-distanceY / mEditor.getMeasuredHeight(), Math.max(0, Math.min(1, e2.getX() / mEditor.getWidth())));
            topOrBottom = false;
        }
        if (notifyY && mScroller.getCurrY() + distanceY > mEditor.getScrollMaxY() + minOverPull) {
            mEditor.getVerticalEdgeEffect().onPull(distanceY / mEditor.getMeasuredHeight(), Math.max(0, Math.min(1, e2.getX() / mEditor.getWidth())));
            topOrBottom = true;
        }
        if (notifyX && mScroller.getCurrX() + distanceX < -minOverPull) {
            mEditor.getHorizontalEdgeEffect().onPull(-distanceX / mEditor.getMeasuredWidth(), Math.max(0, Math.min(1, e2.getY() / mEditor.getHeight())));
            leftOrRight = false;
        }
        if (notifyX && mScroller.getCurrX() + distanceX > mEditor.getScrollMaxX() + minOverPull) {
            mEditor.getHorizontalEdgeEffect().onPull(distanceX / mEditor.getMeasuredWidth(), Math.max(0, Math.min(1, e2.getY() / mEditor.getHeight())));
            leftOrRight = true;
        }
        mEditor.invalidate();
        mEditor.dispatchEvent(new ScrollEvent(mEditor, mScroller.getCurrX(),
                mScroller.getCurrY(), endX, endY, ScrollEvent.CAUSE_USER_DRAG));
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!mEditor.getProps().scrollFling) {
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
                mEditor.getProps().overScrollEnabled && !mEditor.isWordwrap() ? (int) (20 * mEditor.getDpUnit()) : 0,
                mEditor.getProps().overScrollEnabled ? (int) (20 * mEditor.getDpUnit()) : 0);
        mEditor.invalidate();
        float minVe = mEditor.getDpUnit() * 2000;
        if (Math.abs(velocityX) >= minVe || Math.abs(velocityY) >= minVe) {
            notifyScrolled();
            mEditor.hideAutoCompleteWindow();
        }
        if (Math.abs(velocityX) >= minVe / 2f) {
            mEditor.getHorizontalEdgeEffect().finish();
        }
        if (Math.abs(velocityY) >= minVe) {
            mEditor.getVerticalEdgeEffect().finish();
        }
        mEditor.dispatchEvent(new ScrollEvent(mEditor, mScroller.getCurrX(),
                mScroller.getCurrY(), mScroller.getFinalX(), mScroller.getFinalY(), ScrollEvent.CAUSE_USER_FLING));
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (mEditor.isScalable()) {
            float newSize = mEditor.getTextSizePx() * detector.getScaleFactor();
            if (newSize < minSize || newSize > maxSize) {
                return false;
            }
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();
            int originHeight = mEditor.getRowHeight();
            mEditor.setTextSizePxDirect(newSize);
            float heightFactor = mEditor.getRowHeight() * 1f / originHeight;
            float afterScrollY = (mScroller.getCurrY() + focusY) * heightFactor - focusY;
            float afterScrollX = (mScroller.getCurrX() + focusX) * detector.getScaleFactor() - focusX;
            afterScrollX = Math.max(0, Math.min(afterScrollX, mEditor.getScrollMaxX()));
            afterScrollY = Math.max(0, Math.min(afterScrollY, mEditor.getScrollMaxY()));
            mEditor.dispatchEvent(new ScrollEvent(mEditor, mScroller.getCurrX(),
                    mScroller.getCurrY(), (int)afterScrollX, (int)afterScrollY, ScrollEvent.CAUSE_SCALE_TEXT));
            mScroller.startScroll((int) afterScrollX, (int) afterScrollY, 0, 0, 0);
            isScaling = true;
            mEditor.invalidate();
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
        mEditor.updateTimestamp();
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
        if (mEditor.getCursor().isSelected() || e.getPointerCount() != 1) {
            return true;
        }
        long res = mEditor.getPointPositionOnScreen(e.getX(), e.getY());
        int line = IntPair.getFirst(res);
        int column = IntPair.getSecond(res);
        if (mEditor.dispatchEvent(new DoubleClickEvent(mEditor, mEditor.getText().getIndexer().getCharPosition(line, column), e))) {
            return true;
        }
        selectWord(line, column);
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

        public static final int LEFT = HandleStateChangeEvent.HANDLE_TYPE_LEFT;
        public static final int RIGHT = HandleStateChangeEvent.HANDLE_TYPE_RIGHT;
        public static final int BOTH = HandleStateChangeEvent.HANDLE_TYPE_INSERT;

        public int type;

        /**
         * Create a handle
         *
         * @param type Type :left,right,both
         */
        public SelectionHandle(int type) {
            this.type = type;
        }

        private boolean checkNoIntersection(SelectionHandleStyle.HandleDescriptor one, SelectionHandleStyle.HandleDescriptor another) {
            return !RectF.intersects(one.position, another.position);
        }

        /**
         * Handle the event
         *
         * @param e Event sent by EventHandler
         */
        public void applyPosition(MotionEvent e) {
            SelectionHandleStyle.HandleDescriptor descriptor = null;
            switch (type) {
                case LEFT:
                    descriptor = mEditor.getLeftHandleDescriptor();
                    break;
                case RIGHT:
                    descriptor = mEditor.getRightHandleDescriptor();
                    break;
                default:
                    descriptor = mEditor.getInsertHandleDescriptor();
            }
            var anotherDesc = type == LEFT ? mEditor.getRightHandleDescriptor() : mEditor.getLeftHandleDescriptor();
            float targetX = mScroller.getCurrX() + e.getX() + (descriptor.alignment != SelectionHandleStyle.ALIGN_CENTER ? descriptor.position.width() : 0) * (descriptor.alignment == SelectionHandleStyle.ALIGN_LEFT ? 1 : -1);
            float targetY = mScroller.getCurrY() + e.getY() - descriptor.position.height();
            int line = IntPair.getFirst(mEditor.getPointPosition(0, targetY));
            if (line >= 0 && line < mEditor.getLineCount()) {
                int column = IntPair.getSecond(mEditor.getPointPosition(targetX, targetY));
                int lastLine = type == RIGHT ? mEditor.getCursor().getRightLine() : mEditor.getCursor().getLeftLine();
                int lastColumn = type == RIGHT ? mEditor.getCursor().getRightColumn() : mEditor.getCursor().getLeftColumn();
                int anotherLine = type != RIGHT ? mEditor.getCursor().getRightLine() : mEditor.getCursor().getLeftLine();
                int anotherColumn = type != RIGHT ? mEditor.getCursor().getRightColumn() : mEditor.getCursor().getLeftColumn();

                if ((line != lastLine || column != lastColumn) && (type == BOTH || (line != anotherLine || column != anotherColumn))) {
                    switch (type) {
                        case BOTH:
                            mEditor.cancelAnimation();
                            mEditor.setSelection(line, column, false, SelectionChangeEvent.CAUSE_SELECTION_HANDLE);
                            break;
                        case RIGHT:
                            if (anotherLine > line || (anotherLine == line && anotherColumn > column)) {
                                //Swap type
                                if (checkNoIntersection(descriptor, anotherDesc)) {
                                    dispatchHandle(mSelHandleType, false);
                                    EditorTouchEventHandler.this.mSelHandleType = LEFT;
                                    dispatchHandle(mSelHandleType, true);
                                    this.type = LEFT;
                                    left.type = RIGHT;
                                    SelectionHandle tmp = right;
                                    right = left;
                                    left = tmp;
                                    mEditor.setSelectionRegion(line, column, anotherLine, anotherColumn, false, SelectionChangeEvent.CAUSE_SELECTION_HANDLE);
                                }
                            } else {
                                mEditor.setSelectionRegion(anotherLine, anotherColumn, line, column, false, SelectionChangeEvent.CAUSE_SELECTION_HANDLE);
                            }
                            break;
                        case LEFT:
                            if (anotherLine < line || (anotherLine == line && anotherColumn < column)) {
                                //Swap type
                                if (checkNoIntersection(descriptor, anotherDesc)) {
                                    dispatchHandle(mSelHandleType, false);
                                    EditorTouchEventHandler.this.mSelHandleType = RIGHT;
                                    dispatchHandle(mSelHandleType, true);
                                    this.type = RIGHT;
                                    right.type = LEFT;
                                    SelectionHandle tmp = right;
                                    right = left;
                                    left = tmp;
                                    mEditor.setSelectionRegion(anotherLine, anotherColumn, line, column, false, SelectionChangeEvent.CAUSE_SELECTION_HANDLE);
                                }
                            } else {
                                mEditor.setSelectionRegion(line, column, anotherLine, anotherColumn, false, SelectionChangeEvent.CAUSE_SELECTION_HANDLE);
                            }
                            break;
                    }
                }
            }
        }

    }

    /**
     * Runnable for controlling auto-scrolling when thumb reaches the edges of editor
     */
    private class EdgeScrollRunnable implements Runnable {
        private final static int MAX_FACTOR = 32;
        private final static float INCREASE_FACTOR = 1.06f;

        private final int initialDelta;
        private int deltaHorizontal;
        private int deltaVertical;
        private int lastDx, lastDy;
        private int factorX, factorY;
        private long postTimes;

        public EdgeScrollRunnable(int initDelta) {
            initialDelta = deltaHorizontal = deltaVertical = initDelta;
            postTimes = 0;
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
            if (mMagnifier.isShowing()) {
                mMagnifier.dismiss();
            }

            // Speed up if we are scrolling in the direction
            if (isSameSign(dx, lastDx)) {
                if (factorX < MAX_FACTOR && (postTimes & 1) == 0) {
                    factorX++;
                    deltaHorizontal *= INCREASE_FACTOR;
                }
            } else {
                // Recover initial speed because direction changed
                deltaHorizontal = initialDelta;
                factorX = 0;
            }
            if (isSameSign(dy, lastDy)) {
                if (factorY < MAX_FACTOR && (postTimes & 1) == 0) {
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

            postTimes++;
            // Post for animation
            if (mEdgeFlags != 0) {
                mEditor.postDelayed(this, 10);
            }
        }
    }
}

