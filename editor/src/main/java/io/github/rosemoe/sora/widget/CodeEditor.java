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

import static io.github.rosemoe.sora.util.Numbers.stringSize;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.RenderNode;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MutableInt;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.OverScroller;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.rosemoe.sora.R;
import io.github.rosemoe.sora.annotations.Experimental;
import io.github.rosemoe.sora.annotations.UnsupportedUserUsage;
import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.event.EditorKeyEvent;
import io.github.rosemoe.sora.event.Event;
import io.github.rosemoe.sora.event.EventManager;
import io.github.rosemoe.sora.event.EventReceiver;
import io.github.rosemoe.sora.event.ScrollEvent;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.graphics.BufferedDrawPoints;
import io.github.rosemoe.sora.graphics.GraphicTextRow;
import io.github.rosemoe.sora.graphics.Paint;
import io.github.rosemoe.sora.lang.EmptyLanguage;
import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.lang.analysis.StyleReceiver;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandleResult;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler;
import io.github.rosemoe.sora.lang.styling.CodeBlock;
import io.github.rosemoe.sora.lang.styling.EmptyReader;
import io.github.rosemoe.sora.lang.styling.ExternalRenderer;
import io.github.rosemoe.sora.lang.styling.Span;
import io.github.rosemoe.sora.lang.styling.Spans;
import io.github.rosemoe.sora.lang.styling.Styles;
import io.github.rosemoe.sora.lang.styling.TextStyle;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.text.ContentLine;
import io.github.rosemoe.sora.text.ContentListener;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.text.Cursor;
import io.github.rosemoe.sora.text.FormatThread;
import io.github.rosemoe.sora.text.LineRemoveListener;
import io.github.rosemoe.sora.text.TextLayoutHelper;
import io.github.rosemoe.sora.text.TextUtils;
import io.github.rosemoe.sora.util.IntPair;
import io.github.rosemoe.sora.util.LongArrayList;
import io.github.rosemoe.sora.util.Numbers;
import io.github.rosemoe.sora.util.TemporaryCharBuffer;
import io.github.rosemoe.sora.util.TemporaryFloatBuffer;
import io.github.rosemoe.sora.util.ThemeUtils;
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion;
import io.github.rosemoe.sora.widget.component.EditorBuiltinComponent;
import io.github.rosemoe.sora.widget.component.EditorCompletionAdapter;
import io.github.rosemoe.sora.widget.component.EditorTextActionWindow;
import io.github.rosemoe.sora.widget.component.Magnifier;
import io.github.rosemoe.sora.widget.layout.Layout;
import io.github.rosemoe.sora.widget.layout.LineBreakLayout;
import io.github.rosemoe.sora.widget.layout.Row;
import io.github.rosemoe.sora.widget.layout.RowIterator;
import io.github.rosemoe.sora.widget.layout.WordwrapLayout;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;
import io.github.rosemoe.sora.widget.style.SelectionHandleStyle;
import io.github.rosemoe.sora.widget.style.builtin.HandleStyleSideDrop;

/**
 * CodeEditor is an editor that can highlight text regions by doing basic syntax analyzing
 * This project in GitHub: https://github.com/Rosemoe/sora-editor
 * <p>
 * Note:
 * Row and line are different in this editor
 * When we say 'row', it means a line displayed on screen. It can be a part of a line in the text object.
 * When we say 'line', it means a real line in the original text.
 *
 * @author Rosemoe
 */
@SuppressWarnings("unused")
public class CodeEditor extends View implements ContentListener, StyleReceiver, FormatThread.FormatResultReceiver, LineRemoveListener {

    /**
     * Digits for line number measuring
     */
    private final static String NUMBER_DIGITS = "0 1 2 3 4 5 6 7 8 9";

    /**
     * The default size when creating the editor object. Unit is sp.
     */
    public static final int DEFAULT_TEXT_SIZE = 18;

    /**
     * The default cursor blinking period
     */
    public static final int DEFAULT_CURSOR_BLINK_PERIOD = 500;

    /**
     * Draw whitespace characters before line content start
     * <strong>Whitespace here only means space and tab</strong>
     *
     * @see #setNonPrintablePaintingFlags(int)
     */
    public static final int FLAG_DRAW_WHITESPACE_LEADING = 1;

    /**
     * Draw whitespace characters inside line content
     * <strong>Whitespace here only means space and tab</strong>
     *
     * @see #setNonPrintablePaintingFlags(int)
     */
    public static final int FLAG_DRAW_WHITESPACE_INNER = 1 << 1;

    /**
     * Draw whitespace characters after line content end
     * <strong>Whitespace here only means space and tab</strong>
     *
     * @see #setNonPrintablePaintingFlags(int)
     */
    public static final int FLAG_DRAW_WHITESPACE_TRAILING = 1 << 2;

    /**
     * Draw whitespace characters even if it is a line full of whitespaces
     * To apply this, you must enable {@link #FLAG_DRAW_WHITESPACE_LEADING}
     * <strong>Whitespace here only means space and tab</strong>
     *
     * @see #setNonPrintablePaintingFlags(int)
     */
    public static final int FLAG_DRAW_WHITESPACE_FOR_EMPTY_LINE = 1 << 3;

    /**
     * Draw newline signals in text
     *
     * @see #setNonPrintablePaintingFlags(int)
     */
    public static final int FLAG_DRAW_LINE_SEPARATOR = 1 << 4;

    /**
     * Draw the tab character the same as space.
     * If not set, tab will be display to be a line.
     *
     * @see #setNonPrintablePaintingFlags(int)
     */
    public static final int FLAG_DRAW_TAB_SAME_AS_SPACE = 1 << 5;

    /**
     * Draw the whitespaces in selected text
     *
     * @see #setNonPrintablePaintingFlags(int)
     */
    public static final int FLAG_DRAW_WHITESPACE_IN_SELECTION = 1 << 6;

    /**
     * Adjust the completion window's position scheme according to the device's screen size.
     */
    public static final int WINDOW_POS_MODE_AUTO = 0;

    /**
     * Completion window always follow the cursor
     */
    public static final int WINDOW_POS_MODE_FOLLOW_CURSOR_ALWAYS = 1;

    /**
     * Completion window always stay at the bottom of view and occupies the
     * horizontal viewport
     */
    public static final int WINDOW_POS_MODE_FULL_WIDTH_ALWAYS = 2;

    /**
     * Text size scale of small graph
     */
    private static final float SCALE_MINI_GRAPH = 0.9f;

    /*
     * Internal state identifiers of action mode
     */
    static final int ACTION_MODE_NONE = 0;
    static final int ACTION_MODE_SEARCH_TEXT = 1;
    static final int ACTION_MODE_SELECT_TEXT = 2;
    private static final String LOG_TAG = "CodeEditor";
    protected SymbolPairMatch mLanguageSymbolPairs;
    Layout mLayout;
    int mStartedActionMode;
    private int mTabWidth;
    private int mCursorPosition;
    private int mDownX = 0;
    private int mInputType;
    private int mNonPrintableOptions;
    private int mCachedLineNumberWidth;
    private int mCompletionPosMode;
    private long mTimestamp;
    private long mAvailableFloatArrayRegion;
    private float mDpUnit;
    private float mDividerWidth;
    private float mDividerMargin;
    private float mInsertSelWidth;
    private float mBlockLineWidth;
    private float mVerticalScrollFactor;
    private float mLineInfoTextSize;
    private boolean mWait;
    private boolean mScalable;
    private boolean mEditable;
    private boolean mWordwrap;
    private boolean mUndoEnabled;
    private boolean mDisplayLnPanel;
    private boolean mLineNumberEnabled;
    private boolean mBlockLineEnabled;
    private boolean mForceHorizontalScrollable;
    private boolean mHighlightCurrentBlock;
    private boolean mHighlightCurrentLine;
    private boolean mVerticalScrollBarEnabled;
    private boolean mHorizontalScrollBarEnabled;
    private boolean mCursorAnimation;
    private boolean mPinLineNumber;
    private boolean mFirstLineNumberAlwaysVisible;
    private boolean mLigatureEnabled;
    private boolean mLastCursorState;
    private RectF mRect;
    private SelectionHandleStyle.HandleDescriptor mLeftHandle;
    private SelectionHandleStyle.HandleDescriptor mRightHandle;
    private SelectionHandleStyle.HandleDescriptor mInsertHandle;
    private RectF mVerticalScrollBar;
    private RectF mHorizontalScrollBar;
    private Path mPath;
    private ClipboardManager mClipboardManager;
    private InputMethodManager mInputMethodManager;
    private Cursor mCursor;
    private Content mText;
    private Paint mPaint;
    private Paint mPaintOther;
    private Paint mPaintGraph;
    private ContentLine mBuffer;
    private Matrix mMatrix;
    private Rect mViewRect;
    private EditorColorScheme mColors;
    private String mLnTip = "Line:";
    private Language mLanguage;
    private long mLastMakeVisible = 0;
    private EditorAutoCompletion mCompletionWindow;
    private EditorTouchEventHandler mEventHandler;
    private Paint.Align mLineNumberAlign;
    private GestureDetector mBasicDetector;
    protected EditorTextActionWindow mTextActionWindow;
    private ScaleGestureDetector mScaleDetector;
    EditorInputConnection mConnection;
    private CursorAnchorInfo.Builder mAnchorInfoBuilder;
    private MaterialEdgeEffect mVerticalEdgeGlow;
    private MaterialEdgeEffect mHorizontalGlow;
    private ExtractedTextRequest mExtracting;
    private FormatThread mFormatThread;
    private EditorSearcher mSearcher;
    private EventManager mEventManager;
    private CursorAnimator mCursorAnimator;
    private Paint.FontMetricsInt mTextMetrics;
    private Paint.FontMetricsInt mLineNumberMetrics;
    private Paint.FontMetricsInt mGraphMetrics;
    private SelectionHandleStyle mHandleStyle;
    private CursorBlink mCursorBlink;
    protected List<Span> defSpans = new ArrayList<>(2);
    private final LongArrayList mPostDrawLineNumbers = new LongArrayList();
    private final LongArrayList mPostDrawCurrentLines = new LongArrayList();
    private CharPosition mSelectionAnchor;
    private BufferedDrawPoints mDrawPoints;
    private HwAcceleratedRenderer mRenderer;
    private DirectAccessProps mProps;
    private Bundle mExtraArguments;
    private Styles mStyles;
    final KeyMetaStates mKeyMetaStates = new KeyMetaStates(this);

    public CodeEditor(Context context) {
        this(context, null);
    }

    public CodeEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CodeEditor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Get builtin component so that you can enable/disable them or do some other actions.
     *
     * @see io.github.rosemoe.sora.widget.component
     */
    @SuppressWarnings("unchecked")
    public <T extends EditorBuiltinComponent> T getComponent(Class<T> clazz) {
        if (clazz == EditorAutoCompletion.class) {
            return (T) mCompletionWindow;
        } else if (clazz == Magnifier.class) {
            return (T) mEventHandler.mMagnifier;
        } else if (clazz == EditorTextActionWindow.class) {
            return (T) mTextActionWindow;
        } else {
            throw new IllegalArgumentException("Unknown component type");
        }
    }

    public KeyMetaStates getKeyMetaStates() {
        return mKeyMetaStates;
    }

    /**
     * Checks whether this region has visible region on screen
     *
     * @param begin The start line of code block
     * @param end   The end line of code block
     * @param first The first visible line on screen
     * @param last  The last visible line on screen
     * @return Whether this block can be seen
     */
    private static boolean hasVisibleRegion(int begin, int end, int first, int last) {
        return (end > first && begin < last);
    }

    /**
     * Cancel the next animation for {@link CodeEditor#ensurePositionVisible(int, int)}
     */
    protected void cancelAnimation() {
        mLastMakeVisible = System.currentTimeMillis();
    }

    /**
     * Get the width of line number and divider line
     *
     * @return The width
     */
    public float measureTextRegionOffset() {
        return isLineNumberEnabled() ? measureLineNumber() + mDividerMargin * 2 + mDividerWidth : mDpUnit * 5;
    }

    /**
     * Get the rect of left selection handle painted on view
     *
     * @return Descriptor of left handle
     */
    public SelectionHandleStyle.HandleDescriptor getLeftHandleDescriptor() {
        return mLeftHandle;
    }

    /**
     * Get the rect of right selection handle painted on view
     *
     * @return Descriptor of right handle
     */
    public SelectionHandleStyle.HandleDescriptor getRightHandleDescriptor() {
        return mRightHandle;
    }

    /**
     * Get the character's x offset on view
     *
     * @param line   The line position of character
     * @param column The column position of character
     * @return The x offset on screen
     */
    public float getOffset(int line, int column) {
        return mLayout.getCharLayoutOffset(line, column)[1] + measureTextRegionOffset() - getOffsetX();
    }

    /**
     * Prepare editor
     *
     * Initialize variants
     */
    private void initialize(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Log.i(LOG_TAG, COPYRIGHT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var configuration = ViewConfiguration.get(getContext());
            mVerticalScrollFactor = configuration.getScaledVerticalScrollFactor();
        } else {
            try {
                var a = getContext().obtainStyledAttributes(new int[] {android.R.attr.listPreferredItemHeight});
                mVerticalScrollFactor = a.getFloat(0, 32);
                a.recycle();
            } catch (Exception e) {
                Log.e(LOG_TAG, "Failed to get scroll factor", e);
                mVerticalScrollFactor = 32;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mRenderer = new HwAcceleratedRenderer(this);
        }
        mProps = new DirectAccessProps();
        mEventManager = new EventManager();
        mDpUnit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, Resources.getSystem().getDisplayMetrics()) / 10F;
        mDividerWidth = 2 * mDpUnit;
        mInsertSelWidth = mDividerWidth / 2;
        mDividerMargin = mDpUnit * 6;
        mDrawPoints = new BufferedDrawPoints();
        mPaint = new Paint();
        mPaintOther = new Paint();
        mPaintGraph = new Paint();
        mMatrix = new Matrix();
        mPath = new Path();
        mHandleStyle = new HandleStyleSideDrop(getContext());
        mSearcher = new EditorSearcher(this);
        mCursorAnimator = new CursorAnimator(this);
        setCursorBlinkPeriod(DEFAULT_CURSOR_BLINK_PERIOD);
        mAnchorInfoBuilder = new CursorAnchorInfo.Builder();
        mPaint.setAntiAlias(true);
        mPaintOther.setAntiAlias(true);
        mPaintGraph.setAntiAlias(true);
        mPaintOther.setTypeface(Typeface.MONOSPACE);
        mStartedActionMode = ACTION_MODE_NONE;
        setTextSize(DEFAULT_TEXT_SIZE);
        setLineInfoTextSize(mPaint.getTextSize());
        mColors = new EditorColorScheme(this);
        mEventHandler = new EditorTouchEventHandler(this);
        mBasicDetector = new GestureDetector(getContext(), mEventHandler);
        mBasicDetector.setOnDoubleTapListener(mEventHandler);
        mScaleDetector = new ScaleGestureDetector(getContext(), mEventHandler);
        mViewRect = new Rect(0, 0, 0, 0);
        mRect = new RectF();
        mInsertHandle = new SelectionHandleStyle.HandleDescriptor();
        mLeftHandle = new SelectionHandleStyle.HandleDescriptor();
        mRightHandle = new SelectionHandleStyle.HandleDescriptor();
        mVerticalScrollBar = new RectF();
        mHorizontalScrollBar = new RectF();
        mLineNumberAlign = Paint.Align.RIGHT;
        mWait = false;
        mBlockLineEnabled = true;
        mBlockLineWidth = 1.5f;
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mClipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        setUndoEnabled(true);
        mCursorPosition = -1;
        setScalable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        mConnection = new EditorInputConnection(this);
        mCompletionWindow = new EditorAutoCompletion(this);
        mVerticalEdgeGlow = new MaterialEdgeEffect();
        mHorizontalGlow = new MaterialEdgeEffect();
        mTextActionWindow = new EditorTextActionWindow(this);
        setEditorLanguage(null);
        setText(null);
        setTabWidth(4);
        setHighlightCurrentLine(true);
        setVerticalScrollBarEnabled(true);
        setHighlightCurrentBlock(true);
        setDisplayLnPanel(true);
        setHorizontalScrollBarEnabled(true);
        setFirstLineNumberAlwaysVisible(true);
        setCursorAnimationEnabled(true);
        setEditable(true);
        setLineNumberEnabled(true);
        setHardwareAcceleratedDrawAllowed(true);
        setInterceptParentHorizontalScrollIfNeeded(false);
        setTypefaceText(Typeface.DEFAULT);
        setCompletionWndPositionMode(WINDOW_POS_MODE_AUTO);
        mPaintOther.setStrokeWidth(getDpUnit() * 1.8f);
        mPaintOther.setStrokeCap(Paint.Cap.ROUND);
        // Issue #41 View being highlighted when focused on Android 11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setDefaultFocusHighlightEnabled(false);
        }
        if (getContext() instanceof ContextThemeWrapper) {
            setEdgeEffectColor(ThemeUtils.getColorPrimary((ContextThemeWrapper) getContext()));
        }
    }

    /**
     * Invalidate the whole hardware-accelerated renderer
     */
    private void invalidateHwRenderer() {
        if (mRenderer != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mRenderer.invalidate();
        }
    }

    /**
     * Invalidate the region in hardware-accelerated renderer
     */
    private void invalidateChanged(int startLine, int endLine) {
        if (mRenderer != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && mCursor != null) {
            if (mRenderer.invalidateInRegion(startLine, endLine)) {
                invalidate();
            }
        }
    }

    /**
     * Invalidate the cursor region in hardware-accelerated renderer
     */
    private void invalidateInCursor() {
        invalidateChanged(mCursor.getLeftLine(), mCursor.getRightLine());
    }

    /**
     * Set how should we control the position&size of completion window
     *
     * @see #WINDOW_POS_MODE_AUTO
     * @see #WINDOW_POS_MODE_FOLLOW_CURSOR_ALWAYS
     * @see #WINDOW_POS_MODE_FULL_WIDTH_ALWAYS
     */
    public void setCompletionWndPositionMode(int mode) {
        mCompletionPosMode = mode;
        updateCompletionWindowPosition();
    }

    /**
     * @see #setCompletionWndPositionMode(int)
     */
    public int getCompletionWndPositionMode() {
        return mCompletionPosMode;
    }

    /**
     * Get {@code DirectAccessProps} object of the editor.
     *
     * You can update some features in editor with the instance without disturb to call methods.
     */
    public DirectAccessProps getProps() {
        return mProps;
    }

    /**
     * Set whether line number region will scroll together with code region
     *
     * @see CodeEditor#isLineNumberPinned()
     */
    public void setPinLineNumber(boolean pinLineNumber) {
        mPinLineNumber = pinLineNumber;
        if (isLineNumberEnabled()) {
            invalidate();
        }
    }

    /**
     * @see CodeEditor#setPinLineNumber(boolean)
     */
    public boolean isLineNumberPinned() {
        return mPinLineNumber;
    }

    /**
     * Show first line number in screen in word wrap mode
     *
     * @see CodeEditor#isFirstLineNumberAlwaysVisible()
     */
    public void setFirstLineNumberAlwaysVisible(boolean enabled) {
        mFirstLineNumberAlwaysVisible = enabled;
        if (isWordwrap()) {
            invalidate();
        }
    }

    /**
     * @see CodeEditor#setFirstLineNumberAlwaysVisible(boolean)
     */
    public boolean isFirstLineNumberAlwaysVisible() {
        return mFirstLineNumberAlwaysVisible;
    }

    /**
     * Inserts the given text in the editor.
     * <p>
     * This method allows you to insert texts externally to the content of editor.
     * The content of {@param text} is not checked to be exactly characters of symbols.
     *
     * @throws IllegalArgumentException If the {@param selectionRegion} is invalid
     * @param text Text to insert, usually a text of symbols
     * @param selectionOffset New selection position relative to the start of text to insert.
     *                        Ranging from 0 to text.length()
     */
    public void insertText(String text, int selectionOffset) {
        if (selectionOffset < 0 || selectionOffset > text.length()) {
            throw new IllegalArgumentException("selectionOffset is invalid");
        }
        var cur = getText().getCursor();
        if (cur.isSelected()) {
            deleteText();
            notifyIMEExternalCursorChange();
        }        mText.insert(cur.getRightLine(), cur.getRightColumn(), text);
        notifyIMEExternalCursorChange();
        if (selectionOffset != text.length()) {
            var pos = mText.getIndexer().getCharPosition(cur.getRight() - (text.length() - selectionOffset));
            setSelection(pos.line, pos.column);
        }
    }

    /**
     * Set adapter for auto-completion window
     * Will take effect next time the window updates
     *
     * @param adapter New adapter, maybe null
     */
    public void setAutoCompletionItemAdapter(@Nullable EditorCompletionAdapter adapter) {
        mCompletionWindow.setAdapter(adapter);
    }

    /**
     * Set cursor blinking period
     * If zero or negative period is passed, the cursor will always be shown.
     *
     * @param period The period time of cursor blinking
     */
    public void setCursorBlinkPeriod(int period) {
        if (mCursorBlink == null) {
            mCursorBlink = new CursorBlink(this, period);
        } else {
            int before = mCursorBlink.period;
            mCursorBlink.setPeriod(period);
            if (before <= 0 && mCursorBlink.valid && isAttachedToWindow()) {
                post(mCursorBlink);
            }
        }
    }

    /**
     * Enable/disable ligature of all types(except 'rlig').
     * Generally you should disable them unless enabling this will have no effect on text measuring.
     * <p>
     * Disabled by default. If you want to enable ligature of a specified type, use
     * {@link CodeEditor#setFontFeatureSettings(String)}
     * <p>
     * For enabling JetBrainsMono font's ligature, Use like this:
     * <pre class="pretty-print">
     * CodeEditor editor = ...;
     * editor.setFontFeatureSettings(enabled ? null : "'liga' 0,'hlig' 0,'dlig' 0,'clig' 0");
     * </pre>
     */
    public void setLigatureEnabled(boolean enabled) {
        this.mLigatureEnabled = enabled;
        setFontFeatureSettings(enabled ? null : "'liga' 0,'calt' 0,'hlig' 0,'dlig' 0,'clig' 0");
    }

    /**
     * @see CodeEditor#setLigatureEnabled(boolean)
     */
    public boolean isLigatureEnabled() {
        return mLigatureEnabled;
    }

    /**
     * Set font feature settings for all paints used by editor
     *
     * @see Paint#setFontFeatureSettings(String)
     */
    public void setFontFeatureSettings(String features) {
        mPaint.setFontFeatureSettingsWrapped(features);
        mPaintOther.setFontFeatureSettings(features);
        mPaintGraph.setFontFeatureSettings(features);
        updateTimestamp();
    }

    /**
     * Set the style of selection handler.
     * @see SelectionHandleStyle
     * @see io.github.rosemoe.sora.widget.style.builtin.HandleStyleDrop
     * @see HandleStyleSideDrop
     */
    public void setSelectionHandleStyle(@NonNull SelectionHandleStyle style) {
        mHandleStyle = Objects.requireNonNull(style);
        invalidate();
    }

    /**
     * Returns whether highlight current code block
     *
     * @return This module enabled / disabled
     * @see CodeEditor#setHighlightCurrentBlock(boolean)
     */
    public boolean isHighlightCurrentBlock() {
        return mHighlightCurrentBlock;
    }

    /**
     * Whether the editor should use a different color to draw
     * the current code block line and this code block's start line and end line's
     * background.
     *
     * @param highlightCurrentBlock Enabled / Disabled this module
     */
    public void setHighlightCurrentBlock(boolean highlightCurrentBlock) {
        this.mHighlightCurrentBlock = highlightCurrentBlock;
        if (!mHighlightCurrentBlock) {
            mCursorPosition = -1;
        } else {
            mCursorPosition = findCursorBlock();
        }
        invalidate();
    }

    /**
     * Specify whether the editor should use a different color to draw
     * the background of current line
     */
    public void setHighlightCurrentLine(boolean highlightCurrentLine) {
        mHighlightCurrentLine = highlightCurrentLine;
        invalidate();
    }

    /**
     * @see CodeEditor#setHighlightCurrentLine(boolean)
     */
    public boolean isHighlightCurrentLine() {
        return mHighlightCurrentLine;
    }

    /**
     * Get the editor's language.
     *
     * @return EditorLanguage
     */
    @NonNull
    public Language getEditorLanguage() {
        return mLanguage;
    }

    /**
     * Set the editor's language.
     * A language is a tool for auto-completion,highlight and auto indent analysis.
     *
     * @param lang New EditorLanguage for editor
     */
    public void setEditorLanguage(@Nullable Language lang) {
        if (lang == null) {
            lang = new EmptyLanguage();
        }

        // Destroy old one
        var old = mLanguage;
        if (old != null) {
            old.getAnalyzeManager().setReceiver(null);
            old.getAnalyzeManager().destroy();
            old.destroy();
        }

        this.mLanguage = lang;
        this.mStyles = null;

        if (mCompletionWindow != null) {
            mCompletionWindow.hide();
        }
        // Setup new one
        var mgr = lang.getAnalyzeManager();
        mgr.setReceiver(this);
        if (mText != null) {
            mgr.reset(new ContentReference(mText), mExtraArguments);
        }

        // Symbol pairs
        if (mLanguageSymbolPairs != null) {
            mLanguageSymbolPairs.setParent(null);
        }
        mLanguageSymbolPairs = mLanguage.getSymbolPairs();
        if (mLanguageSymbolPairs == null) {
            Log.w(LOG_TAG, "Language(" + mLanguage.toString() + ") returned null for symbol pairs. It is a mistake.");
            mLanguageSymbolPairs = new SymbolPairMatch();
        }
        mLanguageSymbolPairs.setParent(mProps.overrideSymbolPairs);

        invalidateHwRenderer();
        invalidate();
    }

    /**
     * Getter
     *
     * @return The width in dp unit
     * @see CodeEditor#setBlockLineWidth(float)
     */
    public float getBlockLineWidth() {
        return mBlockLineWidth;
    }

    /**
     * Set the width of code block line
     *
     * @param dp Width in dp unit
     */
    public void setBlockLineWidth(float dp) {
        mBlockLineWidth = dp;
        invalidate();
    }

    /**
     * @see #setWordwrap(boolean)
     */
    public boolean isWordwrap() {
        return mWordwrap;
    }

    /**
     * Set whether text in editor should be wrapped to fit its size
     *
     * @param wordwrap Whether to wrap words
     */
    public void setWordwrap(boolean wordwrap) {
        if (mWordwrap != wordwrap) {
            mWordwrap = wordwrap;
            createLayout();
            if (!wordwrap) {
                invalidateHwRenderer();
            }
            invalidate();
        }
    }

    public void setCursorAnimationEnabled(boolean enabled) {
        if (!enabled) {
            mCursorAnimator.cancel();
        }
        mCursorAnimation = enabled;
    }

    public boolean isCursorAnimationEnabled() {
        return mCursorAnimation;
    }

    /**
     * Whether display vertical scroll bar when scrolling
     *
     * @param enabled Enabled / disabled
     */
    public void setScrollBarEnabled(boolean enabled) {
        mVerticalScrollBarEnabled = mHorizontalScrollBarEnabled = enabled;
        invalidate();
    }

    /**
     * @return Enabled / disabled
     * @see CodeEditor#setDisplayLnPanel(boolean)
     */
    public boolean isDisplayLnPanel() {
        return mDisplayLnPanel;
    }

    /**
     * Whether display the line number panel beside vertical scroll bar
     * when the scroll bar is touched by user
     *
     * @param displayLnPanel Enabled / disabled
     */
    public void setDisplayLnPanel(boolean displayLnPanel) {
        this.mDisplayLnPanel = displayLnPanel;
        invalidate();
    }

    /**
     * @return The prefix
     * @see CodeEditor#setLnTip(String)
     */
    public String getLnTip() {
        return mLnTip;
    }

    /**
     * Set the tip text before line number for the line number panel
     *
     * @param prefix The prefix for text
     */
    public void setLnTip(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        mLnTip = prefix;
        invalidate();
    }

    @Override
    public boolean isHorizontalScrollBarEnabled() {
        return mHorizontalScrollBarEnabled;
    }

    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        mHorizontalScrollBarEnabled = horizontalScrollBarEnabled;
    }

    @Override
    public boolean isVerticalScrollBarEnabled() {
        return mVerticalScrollBarEnabled;
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        mVerticalScrollBarEnabled = verticalScrollBarEnabled;
    }

    /**
     * Get the rect of vertical scroll bar on view
     *
     * @return Rect of scroll bar
     */
    protected RectF getVerticalScrollBarRect() {
        return mVerticalScrollBar;
    }

    /**
     * Get the rect of horizontal scroll bar on view
     *
     * @return Rect of scroll bar
     */
    protected RectF getHorizontalScrollBarRect() {
        return mHorizontalScrollBar;
    }

    /**
     * Get the rect of insert cursor handle on view
     *
     * @return Rect of insert handle
     */
    public SelectionHandleStyle.HandleDescriptor getInsertHandleDescriptor() {
        return mInsertHandle;
    }

    /**
     * Get text size in pixel unit
     *
     * @return Text size in pixel unit
     * @see CodeEditor#setTextSize(float)
     * @see CodeEditor#setTextSizePx(float)
     */
    @Px
    public float getTextSizePx() {
        return mPaint.getTextSize();
    }

    /**
     * Set text size in pixel unit
     *
     * @param size Text size in pixel unit
     */
    public void setTextSizePx(@Px float size) {
        setTextSizePxDirect(size);
        createLayout();
        invalidate();
    }

    /**
     * Set text size directly without creating layout or invalidating view
     *
     * @param size Text size in pixel unit
     */
    protected void setTextSizePxDirect(float size) {
        mPaint.setTextSizeWrapped(size);
        mPaintOther.setTextSize(size);
        mPaintGraph.setTextSize(size * SCALE_MINI_GRAPH);
        mTextMetrics = mPaint.getFontMetricsInt();
        mLineNumberMetrics = mPaintOther.getFontMetricsInt();
        mGraphMetrics = mPaintGraph.getFontMetricsInt();
        invalidateHwRenderer();
        updateTimestamp();
    }

    /**
     * Paint the view on given Canvas
     *
     * @param canvas Canvas you want to draw
     */
    public void drawView(Canvas canvas) {
        if (mFormatThread != null) {
            String text = "Formatting your code...";
            float centerY = getHeight() / 2f;
            drawColor(canvas, mColors.getColor(EditorColorScheme.LINE_NUMBER_PANEL), mRect);
            float baseline = centerY - getRowHeight() / 2f + getRowBaseline(0);
            float centerX = getWidth() / 2f;
            mPaint.setColor(mColors.getColor(EditorColorScheme.LINE_NUMBER_PANEL_TEXT));
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, centerX, baseline, mPaint);
            mPaint.setTextAlign(Paint.Align.LEFT);
            return;
        }
        getCursor().updateCache(getFirstVisibleLine());

        EditorColorScheme color = mColors;
        drawColor(canvas, color.getColor(EditorColorScheme.WHOLE_BACKGROUND), mViewRect);

        float lineNumberWidth = measureLineNumber();
        float offsetX = -getOffsetX() + measureTextRegionOffset();
        float textOffset = offsetX;

        if (isWordwrap()) {
            if (mCachedLineNumberWidth == 0) {
                mCachedLineNumberWidth = (int) lineNumberWidth;
            } else if (mCachedLineNumberWidth != (int) lineNumberWidth && !mEventHandler.isScaling) {
                mCachedLineNumberWidth = (int) lineNumberWidth;
                createLayout();
            }
        } else {
            mCachedLineNumberWidth = 0;
        }

        buildMeasureCacheForLines(getFirstVisibleLine(), getLastVisibleLine(), mTimestamp);

        if (mCursor.isSelected()) {
            mInsertHandle.setEmpty();
        }
        if (!mCursor.isSelected()) {
            mLeftHandle.setEmpty();
            mRightHandle.setEmpty();
        }

        boolean lineNumberNotPinned = isLineNumberEnabled() && (isWordwrap() || !isLineNumberPinned());

        LongArrayList postDrawLineNumbers = mPostDrawLineNumbers;
        postDrawLineNumbers.clear();
        LongArrayList postDrawCurrentLines = mPostDrawCurrentLines;
        postDrawCurrentLines.clear();
        List<DrawCursorTask> postDrawCursor = new ArrayList<>(3);
        MutableInt firstLn = isFirstLineNumberAlwaysVisible() && isWordwrap() ? new MutableInt(-1) : null;

        drawRows(canvas, textOffset, postDrawLineNumbers, postDrawCursor, postDrawCurrentLines, firstLn);

        offsetX = -getOffsetX();

        if (lineNumberNotPinned) {
            drawLineNumberBackground(canvas, offsetX, lineNumberWidth + mDividerMargin, color.getColor(EditorColorScheme.LINE_NUMBER_BACKGROUND));
            int lineNumberColor = mColors.getColor(EditorColorScheme.LINE_NUMBER);
            int currentLineBgColor = mColors.getColor(EditorColorScheme.CURRENT_LINE);
            if (mCursorAnimator.isRunning()) {
                mRect.bottom = (float) mCursorAnimator.animatorBgBottom.getAnimatedValue() - getOffsetY();
                mRect.top = mRect.bottom - (float) mCursorAnimator.animatorBackground.getAnimatedValue();
                mRect.left = 0;
                mRect.right = (int) (textOffset - mDividerMargin);
                drawColor(canvas, currentLineBgColor, mRect);
            }
            for (int i = 0; i < postDrawCurrentLines.size(); i++) {
                drawRowBackground(canvas, currentLineBgColor, (int) postDrawCurrentLines.get(i), (int) (textOffset - mDividerMargin));
            }
            drawDivider(canvas, offsetX + lineNumberWidth + mDividerMargin, color.getColor(EditorColorScheme.LINE_DIVIDER));
            if (firstLn != null && firstLn.value != -1) {
                int bottom = getRowBottom(0);
                float y;
                if (postDrawLineNumbers.size() == 0 || getRowTop(IntPair.getSecond(postDrawLineNumbers.get(0))) - getOffsetY() > bottom) {
                    // Free to draw at first line
                    y = (getRowBottom(0) + getRowTop(0)) / 2f - (mLineNumberMetrics.descent - mLineNumberMetrics.ascent) / 2f - mLineNumberMetrics.ascent;
                } else {
                    int row = IntPair.getSecond(postDrawLineNumbers.get(0));
                    y = (getRowBottom(row - 1) + getRowTop(row - 1)) / 2f - (mLineNumberMetrics.descent - mLineNumberMetrics.ascent) / 2f - mLineNumberMetrics.ascent - getOffsetY();
                }
                mPaintOther.setTextAlign(mLineNumberAlign);
                mPaintOther.setColor(lineNumberColor);
                switch (mLineNumberAlign) {
                    case LEFT:
                        canvas.drawText(Integer.toString(firstLn.value + 1), offsetX, y, mPaintOther);
                        break;
                    case RIGHT:
                        canvas.drawText(Integer.toString(firstLn.value + 1), offsetX + lineNumberWidth, y, mPaintOther);
                        break;
                    case CENTER:
                        canvas.drawText(Integer.toString(firstLn.value + 1), offsetX + (lineNumberWidth + mDividerMargin) / 2f, y, mPaintOther);
                }
            }
            for (int i = 0; i < postDrawLineNumbers.size(); i++) {
                long packed = postDrawLineNumbers.get(i);
                drawLineNumber(canvas, IntPair.getFirst(packed), IntPair.getSecond(packed), offsetX, lineNumberWidth, lineNumberColor);
            }
        }

        if (!isWordwrap() && isBlockLineEnabled()) {
            drawBlockLines(canvas, textOffset);
        }

        if (!mCursorAnimator.isRunning()) {
            for (var action : postDrawCursor) {
                action.execute(canvas);
            }
        } else {
            drawSelectionOnAnimation(canvas);
        }

        if (isLineNumberEnabled() && !lineNumberNotPinned) {
            drawLineNumberBackground(canvas, 0, lineNumberWidth + mDividerMargin, color.getColor(EditorColorScheme.LINE_NUMBER_BACKGROUND));
            int lineNumberColor = mColors.getColor(EditorColorScheme.LINE_NUMBER);
            int currentLineBgColor = mColors.getColor(EditorColorScheme.CURRENT_LINE);
            if (mCursorAnimator.isRunning()) {
                mRect.bottom = (float) mCursorAnimator.animatorBgBottom.getAnimatedValue() - getOffsetY();
                mRect.top = mRect.bottom - (float) mCursorAnimator.animatorBackground.getAnimatedValue();
                mRect.left = 0;
                mRect.right = (int) (textOffset - mDividerMargin);
                drawColor(canvas, currentLineBgColor, mRect);
            }
            for (int i = 0; i < postDrawCurrentLines.size(); i++) {
                drawRowBackground(canvas, currentLineBgColor, (int) postDrawCurrentLines.get(i), (int) (textOffset + getOffsetX() - mDividerMargin));
            }
            drawDivider(canvas, lineNumberWidth + mDividerMargin, color.getColor(EditorColorScheme.LINE_DIVIDER));
            for (int i = 0; i < postDrawLineNumbers.size(); i++) {
                long packed = postDrawLineNumbers.get(i);
                drawLineNumber(canvas, IntPair.getFirst(packed), IntPair.getSecond(packed), 0, lineNumberWidth, lineNumberColor);
            }
        }

        drawScrollBars(canvas);
        drawEdgeEffect(canvas);

        rememberDisplayedLines();
    }

    /**
     * Update displayed lines after drawing
     */
    private void rememberDisplayedLines() {
        mAvailableFloatArrayRegion = IntPair.pack(getFirstVisibleLine(), getLastVisibleLine());
    }

    /**
     * Obtain a float array from previously displayed lines, or either create a new one
     * if no float array matches the requirement.
     */
    private float[] obtainFloatArray(int desiredSize) {
        var start = IntPair.getFirst(mAvailableFloatArrayRegion);
        var end = IntPair.getSecond(mAvailableFloatArrayRegion);
        var firstVis = getFirstVisibleLine();
        var lastVis = getLastVisibleRow();
        start = Math.max(0, start - 5);
        end = Math.min(end + 5, getLineCount());
        for (int i = start; i < end; i++) {
            // Find line that is not displaying currently
            if (i < firstVis || i > lastVis) {
                var line = mText.getLine(i);
                if (line.widthCache != null && line.widthCache.length >= desiredSize) {
                    line.timestamp = 0;
                    var res = line.widthCache;
                    line.widthCache = null;
                    return res;
                }
            }
            // Skip the region because we can't obtain arrays from here
            if (i >= firstVis && i <= lastVis) {
                i = lastVis;
            }
        }
        //Log.d(LOG_TAG, "Allocate float[], size = " + desiredSize);
        return new float[desiredSize];
    }

    /**
     * Build measure cache for the given lines, if the timestamp indicates that it is outdated.
     */
    private void buildMeasureCacheForLines(int startLine, int endLine, long timestamp) {
        var text = mText;
        while (startLine <= endLine && startLine < text.getLineCount()) {
            ContentLine line = text.getLine(startLine);
            // Do not create cache for long lines
            if (line.length() <= 256) {
                if (line.timestamp < timestamp) {
                    var gtr = GraphicTextRow.obtain();
                    if (line.widthCache == null) {
                        line.widthCache = obtainFloatArray(Math.max(line.length(), 128));
                    }
                    gtr.set(line, 0, line.length(), getTabWidth(), getSpansForLine(startLine), mPaint);
                    gtr.buildMeasureCache();
                    GraphicTextRow.recycle(gtr);
                    line.timestamp = timestamp;
                }
            } else {
                line.widthCache = null;
            }
            startLine++;
        }
    }

    /**
     * Clear flag in flags
     * The flag must be power of two
     *
     * @param flags Flags to filter
     * @param flag  The flag to clear
     * @return Cleared flags
     */
    private int clearFlag(int flags, int flag) {
        return (flags & flag) != 0 ? flags ^ flag : flags;
    }

    /**
     * Whether non-printable is to be drawn
     */
    protected boolean shouldInitializeNonPrintable() {
        return clearFlag(clearFlag(mNonPrintableOptions, FLAG_DRAW_WHITESPACE_FOR_EMPTY_LINE), FLAG_DRAW_TAB_SAME_AS_SPACE) != 0;
    }

    private boolean mHardwareAccAllowed;

    /**
     * Set whether allow the editor to use RenderNode to draw its text.
     * Enabling this can cause more memory usage, but the editor can display text
     * much quick.
     * However, only when hardware accelerate is enabled on this view can the switch
     * make a difference.
     */
    @Experimental
    public void setHardwareAcceleratedDrawAllowed(boolean acceleratedDraw) {
        mHardwareAccAllowed = acceleratedDraw;
        if (acceleratedDraw && !isWordwrap()) {
            invalidateHwRenderer();
        }
    }

    /**
     * @see #setHardwareAcceleratedDrawAllowed(boolean)
     */
    @Experimental
    public boolean isHardwareAcceleratedDrawAllowed() {
        return mHardwareAccAllowed;
    }

    @RequiresApi(29)
    protected void updateLineDisplayList(RenderNode renderNode, int line, Spans.Reader spans) {
        final float waveLength = getDpUnit() * 18;
        final float amplitude = getDpUnit() * 4;
        prepareLine(line);
        int columnCount = getText().getColumnCount(line);
        float widthLine = measureText(mBuffer, 0, columnCount, line) + getDpUnit() * 20;
        renderNode.setPosition(0, 0, (int) widthLine, getRowHeight() + (int) amplitude);
        Canvas canvas = renderNode.beginRecording();
        if (spans == null) {
            spans = new EmptyReader();
        }
        int spanOffset = 0;
        float paintingOffset = 0;
        int row = 0;
        float phi = 0f;
        Span span = spans.getSpanAt(spanOffset);
        // Draw by spans
        long lastStyle = 0;
        while (columnCount > span.column) {
            int spanEnd = spanOffset + 1 >= spans.getSpanCount() ? columnCount : spans.getSpanAt(spanOffset + 1).column;
            spanEnd = Math.min(columnCount, spanEnd);
            int paintStart = span.column;
            int paintEnd = Math.min(columnCount, spanEnd);
            float width = measureText(mBuffer, paintStart, paintEnd - paintStart, line);
            ExternalRenderer renderer = span.renderer;

            // Invoke external renderer preDraw
            if (renderer != null && renderer.requirePreDraw()) {
                int saveCount = canvas.save();
                canvas.translate(paintingOffset, getRowTop(row));
                canvas.clipRect(0f, 0f, width, getRowHeight());
                try {
                    renderer.draw(canvas, mPaint, mColors, true);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error while invoking external renderer", e);
                }
                canvas.restoreToCount(saveCount);
            }

            // Apply font style
            long styleBits = span.getStyleBits();
            if (span.getStyleBits() != lastStyle) {
                mPaint.setFakeBoldText(TextStyle.isBold(styleBits));
                if (TextStyle.isItalics(styleBits)) {
                    mPaint.setTextSkewX(-0.2f);
                } else {
                    mPaint.setTextSkewX(0);
                }
                lastStyle = styleBits;
            }

            int backgroundColorId = span.getBackgroundColorId();
            if (backgroundColorId != 0) {
                drawRowRegionBackground(canvas, paintingOffset, row, 0, columnCount, paintStart, paintEnd, mColors.getColor(backgroundColorId), line);
            }

            // Draw text
            drawRegionText(canvas, paintingOffset, getRowBaseline(row), line, paintStart, paintEnd, span.column, spanEnd, columnCount, mColors.getColor(span.getForegroundColorId()));

            // Draw strikethrough
            if ((span.problemFlags & Span.FLAG_DEPRECATED) != 0 || TextStyle.isStrikeThrough(span.style)) {
                mPaintOther.setColor(Color.BLACK);
                canvas.drawLine(paintingOffset, getRowTop(row) + getRowHeight() / 2f, paintingOffset + width, getRowTop(row) + getRowHeight() / 2f, mPaintOther);
            }

            // Draw underline
            if (span.underlineColor != 0) {
                mRect.bottom = getRowBottom(row) - mDpUnit * 1;
                mRect.top = mRect.bottom - getRowHeight() * 0.08f;
                mRect.left = paintingOffset;
                mRect.right = paintingOffset + width;
                drawColor(canvas, span.underlineColor, mRect);
            }

            // Draw issue curly underline
            if (span.problemFlags > 0 && Integer.highestOneBit(span.problemFlags) != Span.FLAG_DEPRECATED) {
                int color = 0;
                switch (Integer.highestOneBit(span.problemFlags)) {
                    case Span.FLAG_ERROR:
                        color = mColors.getColor(EditorColorScheme.PROBLEM_ERROR);
                        break;
                    case Span.FLAG_WARNING:
                        color = mColors.getColor(EditorColorScheme.PROBLEM_WARNING);
                        break;
                    case Span.FLAG_TYPO:
                        color = mColors.getColor(EditorColorScheme.PROBLEM_TYPO);
                        break;
                }
                if (color != 0 && span.column >= 0 && spanEnd - span.column >= 0) {
                    // Start and end X offset
                    float startOffset = measureText(mBuffer, 0, span.column, line);
                    float lineWidth = measureText(mBuffer, Math.max(0, span.column), spanEnd - span.column, line) + phi;
                    float centerY = getRowBottom(row);
                    // Clip region due not to draw outside the horizontal region
                    canvas.save();
                    canvas.clipRect(startOffset, 0, startOffset + lineWidth, canvas.getHeight());
                    canvas.translate(startOffset - phi, centerY);
                    // Draw waves
                    mPath.reset();
                    mPath.moveTo(0, 0);
                    int waveCount = (int) Math.ceil(lineWidth / waveLength);
                    for (int i = 0; i < waveCount; i++) {
                        mPath.quadTo(waveLength * i + waveLength / 4, amplitude, waveLength * i + waveLength / 2, 0);
                        mPath.quadTo(waveLength * i + waveLength * 3 / 4, -amplitude, waveLength * i + waveLength, 0);
                    }
                    phi = waveLength - (waveCount * waveLength - lineWidth);
                    // Draw path
                    mPaint.setStrokeWidth(getDpUnit() * 1.8f);
                    mPaintOther.setStyle(Paint.Style.STROKE);
                    mPaintOther.setColor(color);
                    canvas.drawPath(mPath, mPaintOther);
                    canvas.restore();
                }
                mPaintOther.setStyle(Paint.Style.FILL);
            } else {
                phi = 0f;
            }

            // Invoke external renderer postDraw
            if (renderer != null && renderer.requirePostDraw()) {
                int saveCount = canvas.save();
                canvas.translate(paintingOffset, getRowTop(row));
                canvas.clipRect(0f, 0f, width, getRowHeight());
                try {
                    renderer.draw(canvas, mPaint, mColors, false);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error while invoking external renderer", e);
                }
                canvas.restoreToCount(saveCount);
            }

            paintingOffset += width;

            if (paintEnd == columnCount) {
                break;
            }
            spanOffset++;
            if (spanOffset < spans.getSpanCount()) {
                span = spans.getSpanAt(spanOffset);
            } else {
                spanOffset--;
            }
        }
        renderNode.endRecording();
        mPaint.setTextSkewX(0);
        mPaint.setFakeBoldText(false);
    }

    /**
     * Draw rows with a {@link RowIterator}
     *
     * @param canvas              Canvas to draw
     * @param offset              Offset of text region start
     * @param postDrawLineNumbers Line numbers to be drawn later
     * @param postDrawCursor      Cursors to be drawn later
     */
    protected void drawRows(Canvas canvas, float offset, LongArrayList postDrawLineNumbers, List<DrawCursorTask> postDrawCursor, LongArrayList postDrawCurrentLines, MutableInt requiredFirstLn) {
        int firstVis = getFirstVisibleRow();
        final float waveLength = getDpUnit() * 18;
        final float amplitude = getDpUnit() * 4;
        RowIterator rowIterator = mLayout.obtainRowIterator(firstVis);
        List<Span> temporaryEmptySpans = null;
        Spans spans = mStyles == null ? null : mStyles.spans;
        List<Integer> matchedPositions = new ArrayList<>();
        int currentLine = mCursor.isSelected() ? -1 : mCursor.getLeftLine();
        int currentLineBgColor = mColors.getColor(EditorColorScheme.CURRENT_LINE);
        int lastPreparedLine = -1;
        int spanOffset = 0;
        int leadingWhitespaceEnd = 0;
        int trailingWhitespaceStart = 0;
        float circleRadius = 0f;
        if (shouldInitializeNonPrintable()) {
            float spaceWidth = mPaint.getSpaceWidth();
            float maxD = Math.min(getRowHeight(), spaceWidth);
            maxD *= 0.25f;
            circleRadius = maxD / 2;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !isWordwrap() && canvas.isHardwareAccelerated() && isHardwareAcceleratedDrawAllowed()) {
            mRenderer.keepCurrentInDisplay(firstVis, getLastVisibleRow());
        }
        float offset2 = getOffsetX() - measureTextRegionOffset();
        float offset3 = offset2 - mDpUnit * 15;

        // Step 1 - Draw background of rows
        for (int row = firstVis; row <= getLastVisibleRow() && rowIterator.hasNext(); row++) {
            Row rowInf = rowIterator.next();
            int line = rowInf.lineIndex;
            int columnCount = mText.getColumnCount(line);
            if (lastPreparedLine != line) {
                computeMatchedPositions(line, matchedPositions);
                prepareLine(line);
                lastPreparedLine = line;
            }
            // Get visible region on the line
            float[] charPos = findFirstVisibleChar(offset3, rowInf.startColumn, rowInf.endColumn, mBuffer, line);
            int firstVisibleChar = (int) charPos[0];
            float paintingOffset = charPos[1] - offset2;
            int lastVisibleChar = (int) findFirstVisibleChar(offset2 + getWidth(), firstVisibleChar + 1, rowInf.endColumn, rowInf.startColumn, mBuffer, line)[0];

            // Draw current line background
            if (line == currentLine && !mCursorAnimator.isRunning()) {
                drawRowBackground(canvas, currentLineBgColor, row);
                postDrawCurrentLines.add(row);
            }

            // Draw matched text background
            if (!matchedPositions.isEmpty()) {
                for (int position : matchedPositions) {
                    drawRowRegionBackground(canvas, paintingOffset, row, firstVisibleChar, lastVisibleChar, position, position + mSearcher.mSearchText.length(), mColors.getColor(EditorColorScheme.MATCHED_TEXT_BACKGROUND), line);
                }
            }

            // Draw selected text background
            if (mCursor.isSelected() && line >= mCursor.getLeftLine() && line <= mCursor.getRightLine()) {
                int selectionStart = 0;
                int selectionEnd = columnCount;
                if (line == mCursor.getLeftLine()) {
                    selectionStart = mCursor.getLeftColumn();
                }
                if (line == mCursor.getRightLine()) {
                    selectionEnd = mCursor.getRightColumn();
                }
                if (mText.getColumnCount(line) == 0 && line != mCursor.getRightLine()) {
                    mRect.top = getRowTop(row) - getOffsetY();
                    mRect.bottom = getRowBottom(row) - getOffsetY();
                    mRect.left = paintingOffset;
                    mRect.right = mRect.left + mPaint.getSpaceWidth() * 2;
                    mPaint.setColor(mColors.getColor(EditorColorScheme.SELECTED_TEXT_BACKGROUND));
                    canvas.drawRoundRect(mRect, getRowHeight() * 0.13f, getRowHeight() * 0.13f, mPaint);
                } else {
                    drawRowRegionBackground(canvas, paintingOffset, row, firstVisibleChar, lastVisibleChar, selectionStart, selectionEnd, mColors.getColor(EditorColorScheme.SELECTED_TEXT_BACKGROUND), line);
                }
            }
        }
        rowIterator.reset();

        // Draw current line background on animation
        if (mCursorAnimator.isRunning()) {
            mRect.bottom = (float) mCursorAnimator.animatorBgBottom.getAnimatedValue() - getOffsetY();
            mRect.top = mRect.bottom - (float) mCursorAnimator.animatorBackground.getAnimatedValue();
            mRect.left = 0;
            mRect.right = mViewRect.right;
            drawColor(canvas, currentLineBgColor, mRect);
        }

        // Step 2 - Draw text and text decorations
        long lastStyle = 0;
        for (int row = firstVis; row <= getLastVisibleRow() && rowIterator.hasNext(); row++) {
            Row rowInf = rowIterator.next();
            int line = rowInf.lineIndex;
            ContentLine contentLine = mText.getLine(line);
            int columnCount = contentLine.length();
            if (row == firstVis && requiredFirstLn != null) {
                requiredFirstLn.value = line;
            } else if (rowInf.isLeadingRow) {
                postDrawLineNumbers.add(IntPair.pack(line, row));
            }

            // Prepare data
            if (lastPreparedLine != line) {
                lastPreparedLine = line;
                prepareLine(line);
                spanOffset = 0;
                if (shouldInitializeNonPrintable()) {
                    long positions = findLeadingAndTrailingWhitespacePos(line);
                    leadingWhitespaceEnd = IntPair.getFirst(positions);
                    trailingWhitespaceStart = IntPair.getSecond(positions);
                }
            }

            // Get visible region on the line
            float[] charPos = findFirstVisibleChar(offset3, rowInf.startColumn, rowInf.endColumn, mBuffer, line);
            int firstVisibleChar = (int) charPos[0];
            float paintingOffset = charPos[1] - offset2;
            int lastVisibleChar = (int) findFirstVisibleChar(offset2 + getWidth(), firstVisibleChar + 1, rowInf.endColumn, rowInf.startColumn, mBuffer, line)[0];

            float backupOffset = paintingOffset;

            // Draw text here
            if (!mHardwareAccAllowed || !canvas.isHardwareAccelerated() || isWordwrap() || Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || rowInf.endColumn - rowInf.startColumn > 256 /* Save memory */) {
                // Draw without hardware acceleration
                // Get spans
                var reader = spans == null ? new EmptyReader() : spans.read();
                try {
                    reader.moveToLine(line);
                }catch (Exception e) {
                    reader = new EmptyReader();
                }
                // Seek for first span
                float phi = 0f;
                while (spanOffset + 1 < reader.getSpanCount()) {
                    if (reader.getSpanAt(spanOffset + 1).column <= firstVisibleChar) {
                        // Update phi
                        Span span = reader.getSpanAt(spanOffset);
                        if (span.problemFlags > 0 && Integer.highestOneBit(span.problemFlags) != Span.FLAG_DEPRECATED) {
                            float lineWidth;
                            int spanEnd = Math.min(rowInf.endColumn, reader.getSpanAt(spanOffset + 1).column);
                            if (isWordwrap()) {
                                lineWidth = measureText(mBuffer, Math.max(firstVisibleChar, span.column), spanEnd - Math.max(firstVisibleChar, span.column), line) + phi;
                            } else {
                                lineWidth = measureText(mBuffer, span.column, spanEnd - span.column, line) + phi;
                            }
                            int waveCount = (int) Math.ceil(lineWidth / waveLength);
                            phi = waveLength - (waveCount * waveLength - lineWidth);
                        } else {
                            phi = 0f;
                        }
                        spanOffset++;
                    } else {
                        break;
                    }
                }
                Span span = reader.getSpanAt(spanOffset);
                // Draw by spans
                while (lastVisibleChar > span.column) {
                    int spanEnd = spanOffset + 1 >= reader.getSpanCount() ? columnCount : reader.getSpanAt(spanOffset + 1).column;
                    spanEnd = Math.min(columnCount, spanEnd);
                    int paintStart = Math.max(firstVisibleChar, span.column);
                    if (paintStart >= columnCount) {
                        break;
                    }
                    int paintEnd = Math.min(lastVisibleChar, spanEnd);
                    if (paintStart > paintEnd) {
                        break;
                    }
                    float width = measureText(mBuffer, paintStart, paintEnd - paintStart, line);
                    ExternalRenderer renderer = span.renderer;

                    // Invoke external renderer preDraw
                    if (renderer != null && renderer.requirePreDraw()) {
                        int saveCount = canvas.save();
                        canvas.translate(paintingOffset, getRowTop(row) - getOffsetY());
                        canvas.clipRect(0f, 0f, width, getRowHeight());
                        try {
                            renderer.draw(canvas, mPaint, mColors, true);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "Error while invoking external renderer", e);
                        }
                        canvas.restoreToCount(saveCount);
                    }

                    // Apply font style
                    long styleBits = span.getStyleBits();
                    if (span.getStyleBits() != lastStyle) {
                        mPaint.setFakeBoldText(TextStyle.isBold(styleBits));
                        if (TextStyle.isItalics(styleBits)) {
                            mPaint.setTextSkewX(-0.2f);
                        } else {
                            mPaint.setTextSkewX(0);
                        }
                        lastStyle = styleBits;
                    }

                    int backgroundColorId = span.getBackgroundColorId();
                    if (backgroundColorId != 0) {
                        drawRowRegionBackground(canvas, paintingOffset, row, firstVisibleChar, lastVisibleChar, paintStart, paintEnd, mColors.getColor(backgroundColorId), line);
                    }

                    // Draw text
                    drawRegionText(canvas, paintingOffset, getRowBaseline(row) - getOffsetY(), line, paintStart, paintEnd, span.column, spanEnd, columnCount, mColors.getColor(span.getForegroundColorId()));

                    // Draw strikethrough
                    if ((span.problemFlags & Span.FLAG_DEPRECATED) != 0 || TextStyle.isStrikeThrough(styleBits)) {
                        mPaintOther.setColor(Color.BLACK);
                        canvas.drawLine(paintingOffset, getRowTop(row) + getRowHeight() / 2f - getOffsetY(), paintingOffset + width, getRowTop(row) + getRowHeight() / 2f - getOffsetY(), mPaintOther);
                    }

                    // Draw underline
                    if (span.underlineColor != 0) {
                        mRect.bottom = getRowBottom(row) - getOffsetY() - mDpUnit * 1;
                        mRect.top = mRect.bottom - getRowHeight() * 0.08f;
                        mRect.left = paintingOffset;
                        mRect.right = paintingOffset + width;
                        drawColor(canvas, span.underlineColor, mRect);
                    }

                    // Draw issue curly underline
                    if (span.problemFlags > 0 && Integer.highestOneBit(span.problemFlags) != Span.FLAG_DEPRECATED) {
                        int color = 0;
                        switch (Integer.highestOneBit(span.problemFlags)) {
                            case Span.FLAG_ERROR:
                                color = mColors.getColor(EditorColorScheme.PROBLEM_ERROR);
                                break;
                            case Span.FLAG_WARNING:
                                color = mColors.getColor(EditorColorScheme.PROBLEM_WARNING);
                                break;
                            case Span.FLAG_TYPO:
                                color = mColors.getColor(EditorColorScheme.PROBLEM_TYPO);
                                break;
                        }
                        if (color != 0 && span.column >= 0 && spanEnd - span.column >= 0) {
                            // Start and end X offset
                            float startOffset;
                            float lineWidth;
                            if (isWordwrap()) {
                                startOffset = measureTextRegionOffset() + measureText(mBuffer, firstVisibleChar, Math.max(0, span.column - firstVisibleChar), line) - getOffsetX();
                                lineWidth = measureText(mBuffer, Math.max(firstVisibleChar, span.column), spanEnd - Math.max(firstVisibleChar, span.column), line) + phi;
                            } else {
                                startOffset = measureTextRegionOffset() + measureText(mBuffer, 0, span.column, line) - getOffsetX();
                                lineWidth = measureText(mBuffer, span.column, spanEnd - span.column, line) + phi;
                            }
                            float centerY = getRowBottom(row) - getOffsetY();
                            // Clip region due not to draw outside the horizontal region
                            canvas.save();
                            canvas.clipRect(startOffset, 0, startOffset + lineWidth, canvas.getHeight());
                            canvas.translate(startOffset - phi, centerY);
                            // Draw waves
                            mPath.reset();
                            mPath.moveTo(0, 0);
                            int waveCount = (int) Math.ceil(lineWidth / waveLength);
                            for (int i = 0; i < waveCount; i++) {
                                mPath.quadTo(waveLength * i + waveLength / 4, amplitude, waveLength * i + waveLength / 2, 0);
                                mPath.quadTo(waveLength * i + waveLength * 3 / 4, -amplitude, waveLength * i + waveLength, 0);
                            }
                            phi = waveLength - (waveCount * waveLength - lineWidth);
                            // Draw path
                            mPaint.setStrokeWidth(getDpUnit() * 1.8f);
                            mPaintOther.setStyle(Paint.Style.STROKE);
                            mPaintOther.setColor(color);
                            canvas.drawPath(mPath, mPaintOther);
                            canvas.restore();
                        }
                        mPaintOther.setStyle(Paint.Style.FILL);
                    } else {
                        phi = 0f;
                    }

                    // Invoke external renderer postDraw
                    if (renderer != null && renderer.requirePostDraw()) {
                        int saveCount = canvas.save();
                        canvas.translate(paintingOffset, getRowTop(row) - getOffsetY());
                        canvas.clipRect(0f, 0f, width, getRowHeight());
                        try {
                            renderer.draw(canvas, mPaint, mColors, false);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "Error while invoking external renderer", e);
                        }
                        canvas.restoreToCount(saveCount);
                    }

                    paintingOffset += width;

                    if (paintEnd == lastVisibleChar) {
                        break;
                    }
                    spanOffset++;
                    if (spanOffset < reader.getSpanCount()) {
                        span = reader.getSpanAt(spanOffset);
                    } else {
                        spanOffset--;
                    }
                }
                try {
                    reader.moveToLine(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                paintingOffset = offset + mRenderer.drawLineHardwareAccelerated(canvas, line, offset) - mDpUnit * 20;
                lastVisibleChar = columnCount;
            }

            // Draw hard wrap
            if (lastVisibleChar == columnCount && (mNonPrintableOptions & FLAG_DRAW_LINE_SEPARATOR) != 0) {
                drawMiniGraph(canvas, paintingOffset, row, "↵");
            }

            // Recover the offset
            paintingOffset = backupOffset;

            // Draw non-printable characters
            if (circleRadius != 0f && (leadingWhitespaceEnd != columnCount || (mNonPrintableOptions & FLAG_DRAW_WHITESPACE_FOR_EMPTY_LINE) != 0)) {
                if ((mNonPrintableOptions & FLAG_DRAW_WHITESPACE_LEADING) != 0) {
                    drawWhitespaces(canvas, paintingOffset, line, row, firstVisibleChar, lastVisibleChar, 0, leadingWhitespaceEnd, circleRadius);
                }
                if ((mNonPrintableOptions & FLAG_DRAW_WHITESPACE_INNER) != 0) {
                    drawWhitespaces(canvas, paintingOffset, line, row, firstVisibleChar, lastVisibleChar, leadingWhitespaceEnd, trailingWhitespaceStart, circleRadius);
                }
                if ((mNonPrintableOptions & FLAG_DRAW_WHITESPACE_TRAILING) != 0) {
                    drawWhitespaces(canvas, paintingOffset, line, row, firstVisibleChar, lastVisibleChar, trailingWhitespaceStart, columnCount, circleRadius);
                }
                if ((mNonPrintableOptions & FLAG_DRAW_WHITESPACE_IN_SELECTION) != 0 && mCursor.isSelected() && line >= mCursor.getLeftLine() && line <= mCursor.getRightLine()) {
                    int selectionStart = 0;
                    int selectionEnd = columnCount;
                    if (line == mCursor.getLeftLine()) {
                        selectionStart = mCursor.getLeftColumn();
                    }
                    if (line == mCursor.getRightLine()) {
                        selectionEnd = mCursor.getRightColumn();
                    }
                    if ((mNonPrintableOptions & 0b1110) == 0) {
                        drawWhitespaces(canvas, paintingOffset, line, row, firstVisibleChar, lastVisibleChar, selectionStart, selectionEnd, circleRadius);
                    } else {
                        if ((mNonPrintableOptions & FLAG_DRAW_WHITESPACE_LEADING) == 0) {
                            drawWhitespaces(canvas, paintingOffset, line, row, firstVisibleChar, lastVisibleChar, selectionStart, Math.min(leadingWhitespaceEnd, selectionEnd), circleRadius);
                        }
                        if ((mNonPrintableOptions & FLAG_DRAW_WHITESPACE_INNER) == 0) {
                            drawWhitespaces(canvas, paintingOffset, line, row, firstVisibleChar, lastVisibleChar, Math.max(leadingWhitespaceEnd, selectionStart), Math.min(trailingWhitespaceStart, selectionEnd), circleRadius);
                        }
                        if ((mNonPrintableOptions & FLAG_DRAW_WHITESPACE_TRAILING) == 0) {
                            drawWhitespaces(canvas, paintingOffset, line, row, firstVisibleChar, lastVisibleChar, Math.max(trailingWhitespaceStart, selectionStart), selectionEnd, circleRadius);
                        }
                    }
                }
            }

            // Draw composing text underline
            if (line == mConnection.mComposingLine) {
                int composingStart = mConnection.mComposingStart;
                int composingEnd = mConnection.mComposingEnd;
                int paintStart = Math.min(Math.max(composingStart, firstVisibleChar), lastVisibleChar);
                int paintEnd = Math.min(Math.max(composingEnd, firstVisibleChar), lastVisibleChar);
                if (paintStart != paintEnd) {
                    mRect.top = getRowBottom(row) - getOffsetY();
                    mRect.bottom = mRect.top + getRowHeight() * 0.06f;
                    mRect.left = paintingOffset + measureText(mBuffer, firstVisibleChar, paintStart - firstVisibleChar, line);
                    mRect.right = mRect.left + measureText(mBuffer, paintStart, paintEnd - paintStart, line);
                    drawColor(canvas, mColors.getColor(EditorColorScheme.UNDERLINE), mRect);
                }
            }

            // Draw cursors
            if (mCursor.isSelected()) {
                if (mCursor.getLeftLine() == line && isInside(mCursor.getLeftColumn(), firstVisibleChar, lastVisibleChar, line)) {
                    float centerX = paintingOffset + measureText(mBuffer, firstVisibleChar, mCursor.getLeftColumn() - firstVisibleChar, line);
                    postDrawCursor.add(new DrawCursorTask(centerX, getRowBottom(row) - getOffsetY(), SelectionHandleStyle.HANDLE_TYPE_LEFT, mLeftHandle));
                }
                if (mCursor.getRightLine() == line && isInside(mCursor.getRightColumn(), firstVisibleChar, lastVisibleChar, line)) {
                    float centerX = paintingOffset + measureText(mBuffer, firstVisibleChar, mCursor.getRightColumn() - firstVisibleChar, line);
                    postDrawCursor.add(new DrawCursorTask(centerX, getRowBottom(row) - getOffsetY(), SelectionHandleStyle.HANDLE_TYPE_RIGHT, mRightHandle));
                }
            } else if (mCursor.getLeftLine() == line && isInside(mCursor.getLeftColumn(), firstVisibleChar, lastVisibleChar, line)) {
                float centerX = paintingOffset + measureText(mBuffer, firstVisibleChar, mCursor.getLeftColumn() - firstVisibleChar, line);
                postDrawCursor.add(new DrawCursorTask(centerX, getRowBottom(row) - getOffsetY(), mEventHandler.shouldDrawInsertHandle() ? SelectionHandleStyle.HANDLE_TYPE_INSERT : SelectionHandleStyle.HANDLE_TYPE_UNDEFINED, mInsertHandle));
            }
        }

        mPaint.setFakeBoldText(false);
        mPaint.setTextSkewX(0);
        mPaintOther.setStrokeWidth(circleRadius * 2);
        mDrawPoints.commitPoints(canvas, mPaintOther);
    }

    /**
     * Draw small characters as graph
     */
    protected void drawMiniGraph(Canvas canvas, float offset, int row, String graph) {
        // Draw
        mPaintGraph.setColor(mColors.getColor(EditorColorScheme.NON_PRINTABLE_CHAR));
        float baseline = getRowBottom(row) - getOffsetY() - mGraphMetrics.descent;
        canvas.drawText(graph, 0, graph.length(), offset, baseline, mPaintGraph);
    }

    /**
     * Draw non-printable characters
     */
    protected void drawWhitespaces(Canvas canvas, float offset, int line, int row, int rowStart, int rowEnd, int min, int max, float circleRadius) {
        int paintStart = Math.max(rowStart, Math.min(rowEnd, min));
        int paintEnd = Math.max(rowStart, Math.min(rowEnd, max));
        mPaintOther.setColor(mColors.getColor(EditorColorScheme.NON_PRINTABLE_CHAR));

        if (paintStart < paintEnd) {
            float spaceWidth = mPaint.getSpaceWidth();
            float rowCenter = (getRowTop(row) + getRowBottom(row)) / 2f - getOffsetY();
            offset += measureText(mBuffer, rowStart, paintStart - rowStart, line);
            var chars = mBuffer.value;
            var lastPos = paintStart;
            while (paintStart < paintEnd) {
                char ch = chars[paintStart];
                int paintCount = 0;
                boolean paintLine = false;
                if (ch == ' ' || ch == '\t') {
                    offset += measureText(mBuffer, lastPos, paintStart - lastPos, line);
                }
                if (ch == ' ') {
                    paintCount = 1;
                } else if (ch == '\t') {
                    if ((getNonPrintablePaintingFlags() & FLAG_DRAW_TAB_SAME_AS_SPACE) != 0) {
                        paintCount = getTabWidth();
                    } else {
                        paintLine = true;
                    }
                }
                for (int i = 0; i < paintCount; i++) {
                    float charStartOffset = offset + spaceWidth * i;
                    float charEndOffset = charStartOffset + spaceWidth;
                    float centerOffset = (charStartOffset + charEndOffset) / 2f;
                    mDrawPoints.drawPoint(centerOffset, rowCenter);
                }
                if (paintLine) {
                    var charWidth = getTabWidth() * spaceWidth;
                    float delta = charWidth * 0.05f;
                    canvas.drawLine(offset + delta, rowCenter, offset + charWidth - delta, rowCenter, mPaintOther);
                }

                if (ch == ' ' || ch == '\t') {
                    offset += (ch == ' ' ? spaceWidth : spaceWidth * getTabWidth());
                    lastPos = paintStart + 1;
                }
                paintStart++;
            }
        }
    }

    /**
     * As the name is, we find where leading spaces end and trailing spaces start
     * Before calling this, the line should be prepared
     *
     * @param line The line to search
     */
    protected long findLeadingAndTrailingWhitespacePos(int line) {
        var buffer = mText.getLine(line).value;
        int column = mText.getColumnCount(line);
        int leading = 0;
        int trailing = column;
        while (leading < column && isWhitespace(buffer[leading])) {
            leading++;
        }
        // Only them this action is needed
        if (leading != column && (mNonPrintableOptions & (FLAG_DRAW_WHITESPACE_INNER | FLAG_DRAW_WHITESPACE_TRAILING)) != 0) {
            while (trailing > 0 && isWhitespace(buffer[trailing - 1])) {
                trailing--;
            }
        }
        return IntPair.pack(leading, trailing);
    }

    /**
     * A quick method to predicate whitespace character
     */
    private boolean isWhitespace(char ch) {
        return ch == '\t' || ch == ' ';
    }

    /**
     * Get matched text regions on the given line
     *
     * @param line      Target line
     * @param positions Outputs start positions
     */
    protected void computeMatchedPositions(int line, List<Integer> positions) {
        positions.clear();
        CharSequence pattern = mSearcher.mSearchText;
        if (pattern == null || pattern.length() == 0) {
            return;
        }
        ContentLine seq = mText.getLine(line);
        int index = 0;
        while (index != -1) {
            index = seq.indexOf(pattern, index);
            if (index != -1) {
                positions.add(index);
                index += pattern.length();
            }
        }
    }

    /**
     * Is inside the region
     *
     * @param index Index to test
     * @param start Start of region
     * @param end   End of region
     * @param line  Checking line
     * @return true if cursor should be drawn in this row
     */
    private boolean isInside(int index, int start, int end, int line) {
        // Due not to draw duplicate cursors for a single one
        if (index == end && mText.getLine(line).length() != end) {
            return false;
        }
        return index >= start && index <= end;
    }

    /**
     * Draw background of a text region
     *
     * @param canvas         Canvas to draw
     * @param paintingOffset Paint offset x on canvas
     * @param row            The row index
     * @param firstVis       First visible character
     * @param lastVis        Last visible character
     * @param highlightStart Region start
     * @param highlightEnd   Region end
     * @param color          Color of background
     */
    protected void drawRowRegionBackground(Canvas canvas, float paintingOffset, int row, int firstVis, int lastVis, int highlightStart, int highlightEnd, int color, int line) {
        int paintStart = Math.min(Math.max(firstVis, highlightStart), lastVis);
        int paintEnd = Math.min(Math.max(firstVis, highlightEnd), lastVis);
        if (paintStart != paintEnd) {
            mRect.top = getRowTop(row) - getOffsetY();
            mRect.bottom = getRowBottom(row) - getOffsetY();
            mRect.left = paintingOffset + measureText(mBuffer, firstVis, paintStart - firstVis, line);
            mRect.right = mRect.left + measureText(mBuffer, paintStart, paintEnd - paintStart, line);
            mPaint.setColor(color);
            canvas.drawRoundRect(mRect, getRowHeight() * 0.13f, getRowHeight() * 0.13f, mPaint);
        }
    }

    /**
     * Draw text region with highlighting selected text
     *
     * @param canvas      Canvas to draw
     * @param offsetX     Start paint offset x on canvas
     * @param baseline    Baseline on canvas
     * @param line        Drawing line index
     * @param startIndex  Start index to paint
     * @param endIndex    Index of end character to paint
     * @param columnCount Column count of line
     * @param color       Color of normal text in this region
     */
    protected void drawRegionText(Canvas canvas, float offsetX, float baseline, int line, int startIndex, int endIndex, int contextStart, int contextEnd, int columnCount, int color) {
        boolean hasSelectionOnLine = mCursor.isSelected() && line >= mCursor.getLeftLine() && line <= mCursor.getRightLine();
        int selectionStart = 0;
        int selectionEnd = columnCount;
        int contextCount = contextEnd - contextStart;
        if (line == mCursor.getLeftLine()) {
            selectionStart = mCursor.getLeftColumn();
        }
        if (line == mCursor.getRightLine()) {
            selectionEnd = mCursor.getRightColumn();
        }
        mPaint.setColor(color);
        if (hasSelectionOnLine && mColors.getColor(EditorColorScheme.TEXT_SELECTED) != 0) {
            if (endIndex <= selectionStart || startIndex >= selectionEnd) {
                drawText(canvas, mBuffer, startIndex, endIndex - startIndex, contextStart, contextCount, offsetX, baseline, line);
            } else {
                if (startIndex <= selectionStart) {
                    if (endIndex >= selectionEnd) {
                        //Three regions
                        //startIndex - selectionStart
                        drawText(canvas, mBuffer, startIndex, selectionStart - startIndex, contextStart, contextCount, offsetX, baseline, line);
                        float deltaX = measureText(mBuffer, startIndex, selectionStart - startIndex, line);
                        //selectionStart - selectionEnd
                        mPaint.setColor(mColors.getColor(EditorColorScheme.TEXT_SELECTED));
                        drawText(canvas, mBuffer, selectionStart, selectionEnd - selectionStart, contextStart, contextCount, offsetX + deltaX, baseline, line);
                        deltaX += measureText(mBuffer, selectionStart, selectionEnd - selectionStart, line);
                        //selectionEnd - endIndex
                        mPaint.setColor(color);
                        drawText(canvas, mBuffer, selectionEnd, endIndex - selectionEnd, contextStart, contextCount, offsetX + deltaX, baseline, line);
                    } else {
                        //Two regions
                        //startIndex - selectionStart
                        drawText(canvas, mBuffer, startIndex, selectionStart - startIndex, contextStart, contextCount, offsetX, baseline, line);
                        //selectionStart - endIndex
                        mPaint.setColor(mColors.getColor(EditorColorScheme.TEXT_SELECTED));
                        drawText(canvas, mBuffer, selectionStart, endIndex - selectionStart, contextStart, contextCount, offsetX + measureText(mBuffer, startIndex, selectionStart - startIndex, line), baseline, line);
                    }
                } else {
                    //selectionEnd > startIndex > selectionStart
                    if (endIndex > selectionEnd) {
                        //Two regions
                        //selectionEnd - endIndex
                        drawText(canvas, mBuffer, selectionEnd, endIndex - selectionEnd, contextStart, contextCount, offsetX + measureText(mBuffer, startIndex, selectionEnd - startIndex, line), baseline, line);
                        //startIndex - selectionEnd
                        mPaint.setColor(mColors.getColor(EditorColorScheme.TEXT_SELECTED));
                        drawText(canvas, mBuffer, startIndex, selectionEnd - startIndex, contextStart, contextCount, offsetX, baseline, line);
                    } else {
                        //One region
                        mPaint.setColor(mColors.getColor(EditorColorScheme.TEXT_SELECTED));
                        drawText(canvas, mBuffer, startIndex, endIndex - startIndex, contextStart, contextCount, offsetX, baseline, line);
                    }
                }
            }
        } else {
            drawText(canvas, mBuffer, startIndex, endIndex - startIndex, contextStart, contextCount, offsetX, baseline, line);
        }
    }

    /**
     * Draw effect of edges
     *
     * @param canvas The canvas to draw
     */
    protected void drawEdgeEffect(Canvas canvas) {
        boolean postDraw = false;
        if (!mVerticalEdgeGlow.isFinished()) {
            boolean bottom = mEventHandler.topOrBottom;
            if (bottom) {
                canvas.save();
                canvas.translate(-getMeasuredWidth(), getMeasuredHeight());
                canvas.rotate(180, getMeasuredWidth(), 0);
            }
            postDraw = mVerticalEdgeGlow.draw(canvas);
            if (bottom) {
                canvas.restore();
            }
        }
        if (isWordwrap()) {
            mHorizontalGlow.finish();
        }
        if (!mHorizontalGlow.isFinished()) {
            canvas.save();
            boolean right = mEventHandler.leftOrRight;
            if (right) {
                canvas.rotate(90);
                canvas.translate(0, -getMeasuredWidth());
            } else {
                canvas.translate(0, getMeasuredHeight());
                canvas.rotate(-90);
            }
            postDraw = mHorizontalGlow.draw(canvas) || postDraw;
            canvas.restore();
        }
        OverScroller scroller = getScroller();
        if (scroller.isOverScrolled()) {
            if (mVerticalEdgeGlow.isFinished() && (scroller.getCurrY() < 0 || scroller.getCurrY() > getScrollMaxY())) {
                mEventHandler.topOrBottom = scroller.getCurrY() >= getScrollMaxY();
                mVerticalEdgeGlow.onAbsorb((int) scroller.getCurrVelocity());
                postDraw = true;
            }
            if (mHorizontalGlow.isFinished() && (scroller.getCurrX() < 0 || scroller.getCurrX() > getScrollMaxX())) {
                mEventHandler.leftOrRight = scroller.getCurrX() >= getScrollMaxX();
                mHorizontalGlow.onAbsorb((int) scroller.getCurrVelocity());
                postDraw = true;
            }
        }
        if (postDraw) {
            postInvalidate();
        }
    }

    /**
     * Draw code block lines on screen
     *
     * @param canvas  The canvas to draw
     * @param offsetX The start x offset for text
     */
    protected void drawBlockLines(Canvas canvas, float offsetX) {
        List<CodeBlock> blocks = mStyles == null ? null : mStyles.blocks;
        if (blocks == null || blocks.isEmpty()) {
            return;
        }
        int first = getFirstVisibleRow();
        int last = getLastVisibleRow();
        boolean mark = false;
        int invalidCount = 0;
        int maxCount = Integer.MAX_VALUE;
        if (mStyles != null) {
            maxCount = mStyles.getSuppressSwitch();
        }
        int mm = binarySearchEndBlock(first, blocks);
        int cursorIdx = mCursorPosition;
        for (int curr = mm; curr < blocks.size(); curr++) {
            CodeBlock block = blocks.get(curr);
            if (hasVisibleRegion(block.startLine, block.endLine, first, last)) {
                try {
                    var lineContent = mText.getLine(block.endLine);
                    float offset1 = measureText(lineContent, 0, Math.min(block.endColumn, lineContent.length()), block.endLine);
                    lineContent = mText.getLine(block.startLine);
                    float offset2 = measureText(lineContent, 0, Math.min(block.startColumn, lineContent.length()), block.startLine);
                    float offset = Math.min(offset1, offset2);
                    float centerX = offset + offsetX;
                    mRect.top = Math.max(0, getRowBottom(block.startLine) - getOffsetY());
                    mRect.bottom = Math.min(getHeight(), (block.toBottomOfEndLine ? getRowBottom(block.endLine) : getRowTop(block.endLine)) - getOffsetY());
                    mRect.left = centerX - mDpUnit * mBlockLineWidth / 2;
                    mRect.right = centerX + mDpUnit * mBlockLineWidth / 2;
                    drawColor(canvas, mColors.getColor(curr == cursorIdx ? EditorColorScheme.BLOCK_LINE_CURRENT : EditorColorScheme.BLOCK_LINE), mRect);
                } catch (IndexOutOfBoundsException e) {
                    //Ignored
                    //Because the exception usually occurs when the content changed.
                }
                mark = true;
            } else if (mark) {
                if (invalidCount >= maxCount)
                    break;
                invalidCount++;
            }
        }
    }

    /**
     * Draw scroll bars and tracks
     *
     * @param canvas The canvas to draw
     */
    protected void drawScrollBars(Canvas canvas) {
        mVerticalScrollBar.setEmpty();
        mHorizontalScrollBar.setEmpty();
        if (!mEventHandler.shouldDrawScrollBar()) {
            return;
        }
        if (isVerticalScrollBarEnabled() && getScrollMaxY() > getHeight() / 2) {
            drawScrollBarTrackVertical(canvas);
            drawScrollBarVertical(canvas);
        }
        if (isHorizontalScrollBarEnabled() && !isWordwrap() && getScrollMaxX() > getWidth() * 3 / 4) {
            drawScrollBarTrackHorizontal(canvas);
            drawScrollBarHorizontal(canvas);
        }
    }

    /**
     * Draw vertical scroll bar track
     *
     * @param canvas Canvas to draw
     */
    protected void drawScrollBarTrackVertical(Canvas canvas) {
        if (mEventHandler.holdVerticalScrollBar()) {
            mRect.right = getWidth();
            mRect.left = getWidth() - mDpUnit * 10;
            mRect.top = 0;
            mRect.bottom = getHeight();
            drawColor(canvas, mColors.getColor(EditorColorScheme.SCROLL_BAR_TRACK), mRect);
        }
    }

    /**
     * Draw vertical scroll bar
     *
     * @param canvas Canvas to draw
     */
    protected void drawScrollBarVertical(Canvas canvas) {
        int page = getHeight();
        float all = mLayout.getLayoutHeight() + getHeight() / 2f;
        float length = page / all * getHeight();
        float topY;
        if (length < mDpUnit * 30) {
            length = mDpUnit * 30;
            topY = (getOffsetY() + page / 2f) / all * (getHeight() - length);
        } else {
            topY = getOffsetY() / all * getHeight();
        }
        if (mEventHandler.holdVerticalScrollBar()) {
            float centerY = topY + length / 2f;
            drawLineInfoPanel(canvas, centerY, mRect.left - mDpUnit * 5);
        }
        mRect.right = getWidth();
        mRect.left = getWidth() - mDpUnit * 10;
        mRect.top = topY;
        mRect.bottom = topY + length;
        mVerticalScrollBar.set(mRect);
        drawColor(canvas, mColors.getColor(mEventHandler.holdVerticalScrollBar() ? EditorColorScheme.SCROLL_BAR_THUMB_PRESSED : EditorColorScheme.SCROLL_BAR_THUMB), mRect);
    }

    /**
     * Draw line number panel
     *
     * @param canvas  Canvas to draw
     * @param centerY The center y on screen for the panel
     * @param rightX  The right x on screen for the panel
     */
    protected void drawLineInfoPanel(Canvas canvas, float centerY, float rightX) {
        if (!mDisplayLnPanel) {
            return;
        }
        String text = mLnTip + (1 + getFirstVisibleLine());
        float backupSize = mPaint.getTextSize();
        mPaint.setTextSize(getLineInfoTextSize());
        Paint.FontMetricsInt backupMetrics = mTextMetrics;
        mTextMetrics = mPaint.getFontMetricsInt();
        float expand = mDpUnit * 3;
        float textWidth = mPaint.measureText(text);
        mRect.top = centerY - getRowHeight() / 2f - expand;
        mRect.bottom = centerY + getRowHeight() / 2f + expand;
        mRect.right = rightX;
        mRect.left = rightX - expand * 2 - textWidth;
        drawColor(canvas, mColors.getColor(EditorColorScheme.LINE_NUMBER_PANEL), mRect);
        float baseline = centerY - getRowHeight() / 2f + getRowBaseline(0);
        float centerX = (mRect.left + mRect.right) / 2;
        mPaint.setColor(mColors.getColor(EditorColorScheme.LINE_NUMBER_PANEL_TEXT));
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, centerX, baseline, mPaint);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(backupSize);
        mTextMetrics = backupMetrics;
    }

    /**
     * Draw horizontal scroll bar track
     *
     * @param canvas Canvas to draw
     */
    protected void drawScrollBarTrackHorizontal(Canvas canvas) {
        if (mEventHandler.holdHorizontalScrollBar()) {
            mRect.top = getHeight() - mDpUnit * 10;
            mRect.bottom = getHeight();
            mRect.right = getWidth();
            mRect.left = 0;
            drawColor(canvas, mColors.getColor(EditorColorScheme.SCROLL_BAR_TRACK), mRect);
        }
    }

    /**
     * Draw horizontal scroll bar
     *
     * @param canvas Canvas to draw
     */
    protected void drawScrollBarHorizontal(Canvas canvas) {
        int page = getWidth();
        float all = getScrollMaxX();
        float length = page / (all + getWidth()) * getWidth();
        float leftX = getOffsetX() / all * (getWidth() - length);
        mRect.top = getHeight() - mDpUnit * 10;
        mRect.bottom = getHeight();
        mRect.right = leftX + length;
        mRect.left = leftX;
        mHorizontalScrollBar.set(mRect);
        drawColor(canvas, mColors.getColor(mEventHandler.holdHorizontalScrollBar() ? EditorColorScheme.SCROLL_BAR_THUMB_PRESSED : EditorColorScheme.SCROLL_BAR_THUMB), mRect);
    }

    protected void drawSelectionOnAnimation(Canvas canvas) {
        mRect.bottom = (float) mCursorAnimator.animatorY.getAnimatedValue() - getOffsetY();
        mRect.top = mRect.bottom - getRowHeight();
        float centerX = (float) mCursorAnimator.animatorX.getAnimatedValue() - getOffsetX();
        mRect.left = centerX - mInsertSelWidth / 2;
        mRect.right = centerX + mInsertSelWidth / 2;
        drawColor(canvas, mColors.getColor(EditorColorScheme.SELECTION_INSERT), mRect);
        if (mEventHandler.shouldDrawInsertHandle()) {
            mHandleStyle.draw(canvas, SelectionHandleStyle.HANDLE_TYPE_INSERT, centerX, mRect.bottom, getRowHeight(), mColors.getColor(EditorColorScheme.SELECTION_HANDLE), mInsertHandle);
        }
    }


    /**
     * Get the color of EdgeEffect
     *
     * @return The color of EdgeEffect.
     */
    public int getEdgeEffectColor() {
        return mVerticalEdgeGlow.getColor();
    }

    /**
     * Set the color of EdgeEffect
     *
     * @param color The color of EdgeEffect
     */
    public void setEdgeEffectColor(int color) {
        mVerticalEdgeGlow.setColor(color);
        mHorizontalGlow.setColor(color);
    }

    /**
     * Get EdgeEffect for vertical direction
     *
     * @return EdgeEffect
     */
    protected MaterialEdgeEffect getVerticalEdgeEffect() {
        return mVerticalEdgeGlow;
    }

    /**
     * Get EdgeEffect for horizontal direction
     *
     * @return EdgeEffect
     */
    protected MaterialEdgeEffect getHorizontalEdgeEffect() {
        return mHorizontalGlow;
    }

    /**
     * Find the smallest code block that cursor is in
     *
     * @return The smallest code block index.
     * If cursor is not in any code block,just -1.
     */
    private int findCursorBlock() {
        List<CodeBlock> blocks = mStyles == null ? null : mStyles.blocks;
        if (blocks == null || blocks.isEmpty()) {
            return -1;
        }
        return findCursorBlock(blocks);
    }

    /**
     * Find the cursor code block internal
     *
     * @param blocks Current code blocks
     * @return The smallest code block index.
     * If cursor is not in any code block,just -1.
     */
    private int findCursorBlock(List<CodeBlock> blocks) {
        int line = mCursor.getLeftLine();
        int min = binarySearchEndBlock(line, blocks);
        int max = blocks.size() - 1;
        int minDis = Integer.MAX_VALUE;
        int found = -1;
        int invalidCount = 0;
        int maxCount = Integer.MAX_VALUE;
        if (mStyles != null) {
            maxCount = mStyles.getSuppressSwitch();
        }
        for (int i = min; i <= max; i++) {
            CodeBlock block = blocks.get(i);
            if (block.endLine >= line && block.startLine <= line) {
                int dis = block.endLine - block.startLine;
                if (dis < minDis) {
                    minDis = dis;
                    found = i;
                }
            } else if (minDis != Integer.MAX_VALUE) {
                invalidCount++;
                if (invalidCount >= maxCount) {
                    break;
                }
            }
        }
        return found;
    }

    /**
     * Find the first code block that maybe seen on screen
     * Because the code blocks is sorted by its end line position
     * we can use binary search to quicken this process in order to decrease
     * the time we use on finding
     *
     * @param firstVis The first visible line
     * @param blocks   Current code blocks
     * @return The block we found. It is always a valid index(Unless there is no block)
     */
    private int binarySearchEndBlock(int firstVis, List<CodeBlock> blocks) {
        //end > firstVis
        int left = 0, right = blocks.size() - 1, mid, row;
        int max = right;
        while (left <= right) {
            mid = (left + right) / 2;
            if (mid < 0) return 0;
            if (mid > max) return max;
            row = blocks.get(mid).endLine;
            if (row > firstVis) {
                right = mid - 1;
            } else if (row < firstVis) {
                left = mid + 1;
            } else {
                left = mid;
                break;
            }
        }
        return Math.max(0, Math.min(left, max));
    }

    /**
     * Measure text width with editor's text paint
     *
     * @param text  Source string
     * @param index Start index in array
     * @param count Count of characters
     * @return The width measured
     */
    @UnsupportedUserUsage
    public float measureText(ContentLine text, int index, int count, int line) {
        var gtr = GraphicTextRow.obtain();
        gtr.set(text, 0, text.length(), mTabWidth, getSpansForLine(line), mPaint);
        var res = gtr.measureText(index, index + count);
        GraphicTextRow.recycle(gtr);
        return res;
    }

    /**
     * Get spans on the given line
     */
    public List<Span> getSpansForLine(int line) {
        var spanMap = mStyles == null ? null : mStyles.spans;
        if (defSpans.size() == 0) {
            defSpans.add(Span.obtain(0, EditorColorScheme.TEXT_NORMAL));
        }
        try {
            if (spanMap != null) {
                return spanMap.read().getSpansOnLine(line);
            } else {
                return defSpans;
            }
        } catch (Exception e) {
            return defSpans;
        }
    }

    /**
     * Draw text on the given position
     *
     * @param canvas Canvas to draw
     * @param line   Source of characters
     * @param index  The index in array
     * @param count  Count of characters
     * @param offX   Offset x for paint
     * @param offY   Offset y for paint(baseline)
     */
    @SuppressLint("NewApi")
    protected void drawText(Canvas canvas, ContentLine line, int index, int count, int contextStart, int contextCount, float offX, float offY, int lineNumber) {
        // drawTextRun() can be called directly on low API systems
        int end = index + count;
        var src = line.value;
        int st = index;
        for (int i = index; i < end; i++) {
            if (src[i] == '\t') {
                canvas.drawTextRun(src, st, i - st, contextStart, contextCount, offX, offY, false, mPaint);
                offX = offX + measureText(line, st, i - st + 1, lineNumber);
                st = i + 1;
            }
        }
        if (st < end) {
            canvas.drawTextRun(src, st, end - st, contextStart, contextCount, offX, offY, false, mPaint);
        }
    }

    @UnsupportedUserUsage
    public float[] findFirstVisibleChar(float target, int start, int end, ContentLine line, int lineNumber) {
        return findFirstVisibleChar(target, start, end, start, line, lineNumber);
    }

    /**
     * Find first visible character
     */
    @UnsupportedUserUsage
    public float[] findFirstVisibleChar(float target, int start, int end, int contextStart, ContentLine line, int lineNumber) {
        if (start >= end) {
            return new float[]{end, 0};
        }
        var gtr = GraphicTextRow.obtain();
        gtr.set(line, contextStart, end, mTabWidth, getSpansForLine(lineNumber), mPaint);
        if (line.widthCache != null && line.timestamp < mTimestamp) {
            buildMeasureCacheForLines(lineNumber, lineNumber, mTimestamp);
        }
        var res = gtr.findOffsetByAdvance(start, target);
        GraphicTextRow.recycle(gtr);
        return res;
    }

    /**
     * Switch current drawing context to the given line
     *
     * @param line Line going to draw or measure
     */
    protected void prepareLine(int line) {
        mBuffer = mText.getLine(line);
    }

    /**
     * Draw background for whole row
     */
    protected void drawRowBackground(Canvas canvas, int color, int row) {
        drawRowBackground(canvas, color, row, mViewRect.right);
    }

    protected void drawRowBackground(Canvas canvas, int color, int row, int right) {
        mRect.top = getRowTop(row) - getOffsetY();
        mRect.bottom = getRowBottom(row) - getOffsetY();
        mRect.left = 0;
        mRect.right = right;
        drawColor(canvas, color, mRect);
    }

    /**
     * Draw single line number
     */
    protected void drawLineNumber(Canvas canvas, int line, int row, float offsetX, float width, int color) {
        if (width + offsetX <= 0) {
            return;
        }
        if (mPaintOther.getTextAlign() != mLineNumberAlign) {
            mPaintOther.setTextAlign(mLineNumberAlign);
        }
        mPaintOther.setColor(color);
        // Line number center align to text center
        float y = (getRowBottom(row) + getRowTop(row)) / 2f - (mLineNumberMetrics.descent - mLineNumberMetrics.ascent) / 2f - mLineNumberMetrics.ascent - getOffsetY();

        var buffer = TemporaryCharBuffer.obtain(20);
        line++;
        int i = stringSize(line);
        Numbers.getChars(line, i, buffer);

        switch (mLineNumberAlign) {
            case LEFT:
                canvas.drawText(buffer, 0, i, offsetX, y, mPaintOther);
                break;
            case RIGHT:
                canvas.drawText(buffer, 0, i, offsetX + width, y, mPaintOther);
                break;
            case CENTER:
                canvas.drawText(buffer, 0, i, offsetX + (width + mDividerMargin) / 2f, y, mPaintOther);
        }
        TemporaryCharBuffer.recycle(buffer);
    }

    /**
     * Draw line number background
     *
     * @param canvas  Canvas to draw
     * @param offsetX Start x of line number region
     * @param width   Width of line number region
     * @param color   Color of line number background
     */
    protected void drawLineNumberBackground(Canvas canvas, float offsetX, float width, int color) {
        float right = offsetX + width;
        if (right < 0) {
            return;
        }
        float left = Math.max(0f, offsetX);
        mRect.bottom = getHeight();
        mRect.top = 0;
        int offY = getOffsetY();
        if (offY < 0) {
            mRect.bottom = mRect.bottom - offY;
            mRect.top = mRect.top - offY;
        }
        mRect.left = left;
        mRect.right = right;
        drawColor(canvas, color, mRect);
    }

    /**
     * Draw divider line
     *
     * @param canvas  Canvas to draw
     * @param offsetX End x of line number region
     * @param color   Color to draw divider
     */
    protected void drawDivider(Canvas canvas, float offsetX, int color) {
        boolean shadow = isLineNumberPinned() && !isWordwrap() && getOffsetX() > 0;
        float right = offsetX + mDividerWidth;
        if (right < 0) {
            return;
        }
        float left = Math.max(0f, offsetX);
        mRect.bottom = getHeight();
        mRect.top = 0;
        int offY = getOffsetY();
        if (offY < 0) {
            mRect.bottom = mRect.bottom - offY;
            mRect.top = mRect.top - offY;
        }
        mRect.left = left;
        mRect.right = right;
        if (shadow) {
            canvas.save();
            canvas.clipRect(mRect.left, mRect.top, getWidth(), mRect.bottom);
            mPaint.setShadowLayer(Math.min(mDpUnit * 8, getOffsetX()), 0, 0, Color.BLACK);
        }
        drawColor(canvas, color, mRect);
        if (shadow) {
            canvas.restore();
            mPaint.setShadowLayer(0, 0, 0, 0);
        }
    }

    /**
     * Get the width of line number region
     *
     * @return width of line number region
     */
    protected float measureLineNumber() {
        if (!isLineNumberEnabled()) {
            return 0f;
        }
        int count = 0;
        int lineCount = getLineCount();
        while (lineCount > 0) {
            count++;
            lineCount /= 10;
        }
        var len = NUMBER_DIGITS.length();
        var buffer = TemporaryFloatBuffer.obtain(len);
        mPaintOther.getTextWidths(NUMBER_DIGITS, buffer);
        TemporaryFloatBuffer.recycle(buffer);
        float single = 0f;
        for (int i = 0; i < len; i += 2) {
            single = Math.max(single, buffer[i]);
        }
        return single * count;
    }

    /**
     * Create layout for text
     */
    protected void createLayout() {
        if (mLayout != null) {
            mLayout.destroyLayout();
        }
        if (mWordwrap) {
            mCachedLineNumberWidth = (int) measureLineNumber();
            mLayout = new WordwrapLayout(this, mText);
        } else {
            mLayout = new LineBreakLayout(this, mText);
        }
        if (mEventHandler != null) {
            mEventHandler.scrollBy(0, 0);
        }
    }

    /**
     * Draw rect on screen
     * Will not do anything if color is zero
     *
     * @param canvas Canvas to draw
     * @param color  Color of rect
     * @param rect   Rect to draw
     */
    protected void drawColor(Canvas canvas, int color, RectF rect) {
        if (color != 0) {
            mPaint.setColor(color);
            canvas.drawRect(rect, mPaint);
        }
    }

    /**
     * Draw rect on screen
     * Will not do anything if color is zero
     *
     * @param canvas Canvas to draw
     * @param color  Color of rect
     * @param rect   Rect to draw
     */
    protected void drawColor(Canvas canvas, int color, Rect rect) {
        if (color != 0) {
            mPaint.setColor(color);
            canvas.drawRect(rect, mPaint);
        }
    }

    /**
     * Commit a tab to cursor
     */
    private void commitTab() {
        if (mConnection != null && isEditable()) {
            mConnection.commitTextInternal("\t", true);
        }
    }

    protected void updateCompletionWindowPosition() {
        updateCompletionWindowPosition(true);
    }

    /**
     * Apply new position of auto-completion window
     */
    protected void updateCompletionWindowPosition(boolean shift) {
        float panelX = updateCursorAnchor() + mDpUnit * 20;
        float[] rightLayoutOffset = mLayout.getCharLayoutOffset(mCursor.getRightLine(), mCursor.getRightColumn());
        float panelY = rightLayoutOffset[0] - getOffsetY() + getRowHeight() / 2f;
        float restY = getHeight() - panelY;
        if (restY > mDpUnit * 200) {
            restY = mDpUnit * 200;
        } else if (restY < mDpUnit * 100 && shift) {
            float offset = 0;
            while (restY < mDpUnit * 100) {
                restY += getRowHeight();
                panelY -= getRowHeight();
                offset += getRowHeight();
            }
            getScroller().startScroll(getOffsetX(), getOffsetY(), 0, (int) offset, 0);
        }
        int width;
        if ((getWidth() < 500 * mDpUnit && mCompletionPosMode == WINDOW_POS_MODE_AUTO) || mCompletionPosMode == WINDOW_POS_MODE_FULL_WIDTH_ALWAYS) {
            // center mode
            width = getWidth() * 7 / 8;
            panelX = getWidth() / 8f / 2f;
        } else {
            // follow cursor mode
            width = (int) Math.min(300 * mDpUnit, getWidth() / 2f);
        }
        int height = mCompletionWindow.getHeight();
        if (!mCompletionWindow.isShowing()) {
            height = (int) restY;
        }
        mCompletionWindow.setMaxHeight((int) restY);
        mCompletionWindow.setSize(width, height);
        mCompletionWindow.setLocation((int) panelX + getOffsetX(), (int) panelY + getOffsetY());
    }

    /**
     * Update the information of cursor
     * Such as the position of cursor on screen(For input method that can go to any position on screen like PC input method)
     *
     * @return The offset x of right cursor on view
     */
    protected float updateCursorAnchor() {
        CursorAnchorInfo.Builder builder = mAnchorInfoBuilder;
        builder.reset();
        mMatrix.set(getMatrix());
        int[] b = new int[2];
        getLocationOnScreen(b);
        mMatrix.postTranslate(b[0], b[1]);
        builder.setMatrix(mMatrix);
        builder.setSelectionRange(mCursor.getLeft(), mCursor.getRight());
        int l = mCursor.getRightLine();
        int column = mCursor.getRightColumn();
        prepareLine(l);
        boolean visible = true;
        float x = measureTextRegionOffset();
        x = x + mLayout.getCharLayoutOffset(l, column)[1];
        x = x - getOffsetX();
        if (x < 0) {
            visible = false;
            x = 0;
        }
        builder.setInsertionMarkerLocation(x, getRowTop(l) - getOffsetY(), getRowBaseline(l) - getOffsetY(), getRowBottom(l) - getOffsetY(), visible ? CursorAnchorInfo.FLAG_HAS_VISIBLE_REGION : CursorAnchorInfo.FLAG_HAS_INVISIBLE_REGION);
        mInputMethodManager.updateCursorAnchorInfo(this, builder.build());
        return x;
    }

    /**
     * Delete text before cursor or selected text (if there is)
     */
    public void deleteText() {
        var cur = mCursor;
        if (cur.isSelected()) {
            mText.delete(cur.getLeftLine(), cur.getLeftColumn(), cur.getRightLine(), cur.getRightColumn());
        } else {
            int col = cur.getLeftColumn(), len = 1;
            int line = cur.getLeftLine();
            if (mProps.deleteEmptyLineFast || (mProps.deleteMultiSpaces != 1 && col > 0 && mText.charAt(line, col - 1) == ' ')) {
                // Check whether selection is in leading spaces
                var text = mText.getLine(cur.getLeftLine()).value;
                var inLeading = true;
                for (int i = col - 1; i >= 0; i--) {
                    char ch = text[i];
                    if (ch != ' ' && ch != '\t') {
                        inLeading = false;
                        break;
                    }
                }

                if (inLeading) {
                    // Check empty line
                    var emptyLine = true;
                    var max = mText.getColumnCount(line);
                    for (int i = col; i < max; i++) {
                        char ch = text[i];
                        if (ch != ' ' && ch != '\t') {
                            emptyLine = false;
                            break;
                        }
                    }
                    if (mProps.deleteEmptyLineFast && emptyLine) {
                        if (line == 0) {
                            // Just delete whitespaces before
                            mText.delete(line, 0, line, col);
                        } else {
                            mText.delete(line - 1, mText.getColumnCount(line - 1), line, max);
                        }
                        return;
                    }

                    if (mProps.deleteMultiSpaces != 1 && col > 0 && mText.charAt(line, col - 1) == ' ') {
                        mText.delete(line, Math.max(0, col - (mProps.deleteMultiSpaces == -1 ? getTabWidth() : mProps.deleteMultiSpaces)), line, col);
                        return;
                    }
                }
            }
            // Do not put cursor inside combined characters
            if (col > 1) {
                int left = TextLayoutHelper.get().getCurPosLeft(col, mText.getLine(cur.getLeftLine()));
                len = col - left;
            }
            mText.delete(cur.getLeftLine(), cur.getLeftColumn() - len, cur.getLeftLine(), cur.getLeftColumn());
        }
    }

    /**
     * Commit text to the content from IME
     */
    public void commitText(CharSequence text) {
        commitText(text, true);
    }

    /**
     * Commit text at current state from IME
     *
     * @param text Text commit by InputConnection
     */
    public void commitText(CharSequence text, boolean applyAutoIndent) {
        var cur = mCursor;
        if (cur.isSelected()) {
            mText.replace(cur.getLeftLine(), cur.getLeftColumn(), cur.getRightLine(), cur.getRightColumn(), text);
        } else {
            if (mProps.autoIndent && text.length() != 0 && applyAutoIndent) {
                char first = text.charAt(0);
                if (first == '\n') {
                    String line = mText.getLineString(cur.getLeftLine());
                    int p = 0, count = 0;
                    while (p < cur.getLeftColumn()) {
                        if (isWhitespace(line.charAt(p))) {
                            if (line.charAt(p) == '\t') {
                                count += mTabWidth;
                            } else {
                                count++;
                            }
                            p++;
                        } else {
                            break;
                        }
                    }
                    try {
                        count += mLanguage.getIndentAdvance(new ContentReference(mText), cur.getLeftLine(), cur.getLeftColumn());
                    } catch (Exception e) {
                        Log.w(LOG_TAG, "Language object error", e);
                    }
                    StringBuilder sb = new StringBuilder(text);
                    sb.insert(1, TextUtils.createIndent(count, mTabWidth, mLanguage.useTab()));
                    text = sb;
                }
            }
            mText.insert(cur.getLeftLine(), cur.getLeftColumn(), text);
        }
    }

    /**
     * @see #setLineInfoTextSize(float)
     */
    public float getLineInfoTextSize() {
        return mLineInfoTextSize;
    }

    /**
     * Set text size for line info panel
     *
     * @param size Text size for line information, <strong>unit is SP</strong>
     */
    public void setLineInfoTextSize(float size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
        mLineInfoTextSize = size;
    }

    /**
     * Sets non-printable painting flags.
     * Specify where they should be drawn.
     * <p>
     * Flags can be mixed.
     *
     * @param flags Flags
     * @see #FLAG_DRAW_WHITESPACE_LEADING
     * @see #FLAG_DRAW_WHITESPACE_INNER
     * @see #FLAG_DRAW_WHITESPACE_TRAILING
     * @see #FLAG_DRAW_WHITESPACE_FOR_EMPTY_LINE
     * @see #FLAG_DRAW_LINE_SEPARATOR
     * @see #FLAG_DRAW_WHITESPACE_IN_SELECTION
     */
    public void setNonPrintablePaintingFlags(int flags) {
        this.mNonPrintableOptions = flags;
        invalidate();
    }

    /**
     * @see #setNonPrintablePaintingFlags(int)
     * @see #FLAG_DRAW_WHITESPACE_LEADING
     * @see #FLAG_DRAW_WHITESPACE_INNER
     * @see #FLAG_DRAW_WHITESPACE_TRAILING
     * @see #FLAG_DRAW_WHITESPACE_FOR_EMPTY_LINE
     * @see #FLAG_DRAW_LINE_SEPARATOR
     * @see #FLAG_DRAW_WHITESPACE_IN_SELECTION
     */
    public int getNonPrintablePaintingFlags() {
        return mNonPrintableOptions;
    }

    /**
     * Make the selection visible
     */
    public void ensureSelectionVisible() {
        ensurePositionVisible(getCursor().getRightLine(), getCursor().getRightColumn());
    }

    /**
     * Make the given character position visible
     *
     * @param line   Line of char
     * @param column Column of char
     */
    public void ensurePositionVisible(int line, int column) {
        float[] layoutOffset = mLayout.getCharLayoutOffset(line, column);
        // x offset is the left of character
        float xOffset = layoutOffset[1] + measureTextRegionOffset();
        // y offset is the bottom of row
        float yOffset = layoutOffset[0];

        float targetY = getOffsetY();
        float targetX = getOffsetX();

        if (yOffset - getRowHeight() < getOffsetY()) {
            //top invisible
            targetY = yOffset - getRowHeight() * 1.1f;
        }
        if (yOffset > getHeight() + getOffsetY()) {
            //bottom invisible
            targetY = yOffset - getHeight() + getRowHeight() * 0.1f;
        }
        float charWidth = column == 0 ? 0 : measureText(mText.getLine(line), column - 1, 1, line);
        if (xOffset < getOffsetX()) {
            targetX = xOffset - charWidth * 0.2f;
        }
        if (xOffset + charWidth > getOffsetX() + getWidth()) {
            targetX = xOffset + charWidth * 0.8f - getWidth();
        }

        targetX = Math.max(0, Math.min(getScrollMaxX(), targetX));
        targetY = Math.max(0, Math.min(getScrollMaxY(), targetY));

        if (targetY == getOffsetY() && targetX == getOffsetX()) {
            invalidate();
            return;
        }

        boolean animation = System.currentTimeMillis() - mLastMakeVisible >= 100;
        mLastMakeVisible = System.currentTimeMillis();
        if (animation) {
            getScroller().forceFinished(true);
            getScroller().startScroll(getOffsetX(), getOffsetY(), (int) (targetX - getOffsetX()), (int) (targetY - getOffsetY()));
            if (Math.abs(getOffsetY() - targetY) > mDpUnit * 100) {
                mEventHandler.notifyScrolled();
            }
        } else {
            getScroller().startScroll(getOffsetX(), getOffsetY(), (int) (targetX - getOffsetX()), (int) (targetY - getOffsetY()), 0);
        }

        dispatchEvent(new ScrollEvent(this, getOffsetX(),
                getOffsetY(), (int) targetX, (int)targetY, ScrollEvent.CAUSE_MAKE_POSITION_VISIBLE));

        invalidate();
    }

    /**
     * Whether there is clip
     *
     * @return whether clip in clip board
     */
    public boolean hasClip() {
        return mClipboardManager.hasPrimaryClip();
    }

    /**
     * Get 1dp = ?px
     *
     * @return 1dp in pixel
     */
    public float getDpUnit() {
        return mDpUnit;
    }

    /**
     * Get scroller from EventHandler
     * You would better not use it for your own scrolling
     *
     * @return The scroller
     */
    public OverScroller getScroller() {
        return mEventHandler.getScroller();
    }

    /**
     * Checks whether the position is over max Y position
     *
     * @param posOnScreen Y position on view
     * @return Whether over max Y
     */
    public boolean isOverMaxY(float posOnScreen) {
        return posOnScreen + getOffsetY() > mLayout.getLayoutHeight();
    }

    /**
     * Determine character position using positions in scroll coordinate
     *
     * @param xOffset Horizontal position in scroll coordinate
     * @param yOffset Vertical position in scroll coordinate
     * @return IntPair. first is line and second is column
     * @see IntPair
     */
    public long getPointPosition(float xOffset, float yOffset) {
        return mLayout.getCharPositionForLayoutOffset(xOffset - measureTextRegionOffset(), yOffset);
    }

    /**
     * Determine character position using positions on view
     *
     * @param x X on view
     * @param y Y on view
     * @return IntPair. first is line and second is column
     * @see IntPair
     */
    public long getPointPositionOnScreen(float x, float y) {
        return getPointPosition(x + getOffsetX(), y + getOffsetY());
    }

    /**
     * Get max scroll y
     *
     * @return max scroll y
     */
    public int getScrollMaxY() {
        return Math.max(0, mLayout.getLayoutHeight() - getHeight() / 2);
    }

    /**
     * Get max scroll x
     *
     * @return max scroll x
     */
    public int getScrollMaxX() {
        return (int) Math.max(0, mLayout.getLayoutWidth() + measureTextRegionOffset() - getWidth() / 2f);
    }

    /**
     * Get EditorSearcher
     *
     * @return EditorSearcher
     */
    public EditorSearcher getSearcher() {
        return mSearcher;
    }

    /**
     * Set selection around the given position
     * It will try to set selection as near as possible (Exactly the position if that position exists)
     */
    protected void setSelectionAround(int line, int column) {
        if (line < getLineCount()) {
            int columnCount = mText.getColumnCount(line);
            if (column > columnCount) {
                column = columnCount;
            }
            setSelection(line, column);
        } else {
            setSelection(getLineCount() - 1, mText.getColumnCount(getLineCount() - 1));
        }
    }

    /**
     * Format text Async
     *
     * @return Whether the format task is scheduled
     */
    public synchronized boolean formatCodeAsync() {
        if (mFormatThread != null) {
            return false;
        }
        mFormatThread = new FormatThread(mText, mLanguage, this);
        mFormatThread.start();
        return true;
    }

    /**
     * Get tab width
     *
     * @return tab width
     */
    public int getTabWidth() {
        return mTabWidth;
    }

    /**
     * Set tab width
     *
     * @param width tab width compared to space
     */
    public void setTabWidth(int width) {
        if (width < 1) {
            throw new IllegalArgumentException("width can not be under 1");
        }
        mTabWidth = width;
        invalidateHwRenderer();
        updateTimestamp();
        invalidate();
    }

    /**
     * Set max and min text size that can be used by user zooming.
     * <p>
     * Unit is px.
     */
    public void setScaleTextSizes(float minSize, float maxSize) {
        if (minSize > maxSize) {
            throw new IllegalArgumentException("min size can not be bigger than max size");
        }
        if (minSize < 2f) {
            throw new IllegalArgumentException("min size must be at least 2px");
        }
        mEventHandler.minSize = minSize;
        mEventHandler.maxSize = maxSize;
    }

    /**
     * @see CodeEditor#setInterceptParentHorizontalScrollIfNeeded(boolean)
     */
    public boolean isInterceptParentHorizontalScrollEnabled() {
        return mForceHorizontalScrollable;
    }

    /**
     * When the parent is a scrollable view group,
     * request it not to allow horizontal scrolling to be intercepted.
     * Until the code cannot scroll horizontally
     *
     * @param forceHorizontalScrollable Whether force horizontal scrolling
     */
    public void setInterceptParentHorizontalScrollIfNeeded(boolean forceHorizontalScrollable) {
        this.mForceHorizontalScrollable = forceHorizontalScrollable;
        if (!forceHorizontalScrollable) {
            ViewParent parent = getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(false);
            }
        }
    }

    /**
     * @see CodeEditor#setInputType(int)
     */
    public int getInputType() {
        return mInputType;
    }

    /**
     * Specify input type for the editor
     * <p>
     * Zero for default input type
     *
     * @see EditorInfo#inputType
     */
    public void setInputType(int inputType) {
        mInputType = inputType;
        restartInput();
    }

    /**
     * Undo last action
     */
    public void undo() {
        mText.undo();
        mCompletionWindow.hide();
    }

    /**
     * Redo last action
     */
    public void redo() {
        mText.redo();
        mCompletionWindow.hide();
    }

    /**
     * Checks whether we can undo
     *
     * @return true if we can undo
     */
    public boolean canUndo() {
        return mText.canUndo();
    }

    /**
     * Checks whether we can redo
     *
     * @return true if we can redo
     */
    public boolean canRedo() {
        return mText.canRedo();
    }

    /**
     * @return Enabled/Disabled
     * @see CodeEditor#setUndoEnabled(boolean)
     */
    public boolean isUndoEnabled() {
        return mUndoEnabled;
    }

    /**
     * Enable / disabled undo manager
     *
     * @param enabled Enable/Disable
     */
    public void setUndoEnabled(boolean enabled) {
        mUndoEnabled = enabled;
        if (mText != null) {
            mText.setUndoEnabled(enabled);
        }
    }

    /**
     * Start search action mode
     */
    public void beginSearchMode() {
        class SearchActionMode implements ActionMode.Callback {

            @Override
            public boolean onCreateActionMode(ActionMode p1, Menu p2) {
                mStartedActionMode = ACTION_MODE_SEARCH_TEXT;
                p2.add(0, 0, 0, R.string.next).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                p2.add(0, 1, 0, R.string.last).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                p2.add(0, 2, 0, R.string.replace).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                p2.add(0, 3, 0, R.string.replaceAll).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                SearchView sv = new SearchView(getContext());
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String text) {
                        getSearcher().gotoNext();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String text) {
                        getSearcher().search(text);
                        return false;
                    }

                });
                p1.setCustomView(sv);
                sv.performClick();
                sv.setQueryHint(getContext().getString(R.string.text_to_search));
                sv.setIconifiedByDefault(false);
                sv.setIconified(false);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode p1, Menu p2) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode am, MenuItem p2) {
                switch (p2.getItemId()) {
                    case 1:
                        getSearcher().gotoLast();
                        break;
                    case 0:
                        getSearcher().gotoNext();
                        break;
                    case 2:
                    case 3:
                        final boolean replaceAll = p2.getItemId() == 3;
                        final EditText et = new EditText(getContext());
                        et.setHint(R.string.replacement);
                        new AlertDialog.Builder(getContext())
                                .setTitle(replaceAll ? R.string.replaceAll : R.string.replace)
                                .setView(et)
                                .setNegativeButton(R.string.cancel, null)
                                .setPositiveButton(R.string.replace, (dialog, which) -> {
                                    if (replaceAll) {
                                        getSearcher().replaceAll(et.getText().toString());
                                    } else {
                                        getSearcher().replaceThis(et.getText().toString());
                                    }
                                    am.finish();
                                    dialog.dismiss();
                                })
                                .show();
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode p1) {
                mStartedActionMode = ACTION_MODE_NONE;
                getSearcher().stopSearch();
            }

        }
        ActionMode.Callback callback = new SearchActionMode();
        startActionMode(callback);
    }

    /**
     * Get {@link EditorTouchEventHandler} of the editor
     */
    public EditorTouchEventHandler getEventHandler() {
        return mEventHandler;
    }

    /**
     * @return Margin of divider line
     * @see CodeEditor#setDividerMargin(float)
     */
    public float getDividerMargin() {
        return mDividerMargin;
    }

    /**
     * Set divider line's left and right margin
     *
     * @param dividerMargin Margin for divider line
     */
    public void setDividerMargin(float dividerMargin) {
        if (dividerMargin < 0) {
            throw new IllegalArgumentException("margin can not be under zero");
        }
        this.mDividerMargin = dividerMargin;
        invalidate();
    }

    /**
     * @return Width of divider line
     * @see CodeEditor#setDividerWidth(float)
     */
    public float getDividerWidth() {
        return mDividerWidth;
    }

    /**
     * Set divider line's width
     *
     * @param dividerWidth Width of divider line
     */
    public void setDividerWidth(float dividerWidth) {
        if (dividerWidth < 0) {
            throw new IllegalArgumentException("width can not be under zero");
        }
        this.mDividerWidth = dividerWidth;
        invalidate();
    }

    /**
     * @return Typeface of line number
     * @see CodeEditor#setTypefaceLineNumber(Typeface)
     */
    public Typeface getTypefaceLineNumber() {
        return mPaintOther.getTypeface();
    }

    /**
     * Set line number's typeface
     *
     * @param typefaceLineNumber New typeface
     */
    public void setTypefaceLineNumber(Typeface typefaceLineNumber) {
        if (typefaceLineNumber == null) {
            typefaceLineNumber = Typeface.MONOSPACE;
        }
        mPaintOther.setTypeface(typefaceLineNumber);
        mLineNumberMetrics = mPaintOther.getFontMetricsInt();
        invalidate();
    }

    /**
     * @return Typeface of text
     * @see CodeEditor#setTypefaceText(Typeface)
     */
    public Typeface getTypefaceText() {
        return mPaint.getTypeface();
    }

    /**
     * Set text's typeface
     *
     * @param typefaceText New typeface
     */
    public void setTypefaceText(Typeface typefaceText) {
        if (typefaceText == null) {
            typefaceText = Typeface.DEFAULT;
        }
        mPaint.setTypefaceWrapped(typefaceText);
        mTextMetrics = mPaint.getFontMetricsInt();
        invalidateHwRenderer();
        updateTimestamp();
        createLayout();
        invalidate();
    }

    /**
     * @return Line number align
     * @see CodeEditor#setLineNumberAlign(Paint.Align)
     */
    public Paint.Align getLineNumberAlign() {
        return mLineNumberAlign;
    }

    /**
     * Set line number align
     *
     * @param align Align for line number
     */
    public void setLineNumberAlign(Paint.Align align) {
        if (align == null) {
            align = Paint.Align.LEFT;
        }
        mLineNumberAlign = align;
        invalidate();
    }

    /**
     * Width for insert cursor
     *
     * @param width Cursor width
     */
    public void setCursorWidth(float width) {
        if (width < 0) {
            throw new IllegalArgumentException("width can not be under zero");
        }
        mInsertSelWidth = width;
        invalidate();
    }

    /**
     * Get Cursor
     * Internal method!
     *
     * @return Cursor of text
     */
    public Cursor getCursor() {
        return mCursor;
    }

    /**
     * Get line count
     *
     * @return line count
     */
    public int getLineCount() {
        return mText.getLineCount();
    }

    /**
     * Get first visible line on screen
     *
     * @return first visible line
     */
    public int getFirstVisibleLine() {
        try {
            return mLayout.getLineNumberForRow(getFirstVisibleRow());
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    /**
     * Get first visible row on screen
     *
     * @return first visible row
     */
    public int getFirstVisibleRow() {
        return Math.max(0, getOffsetY() / getRowHeight());
    }

    /**
     * Get last visible row on screen.
     * The result is <strong>unchecked</strong>. It can be bigger than the max row count in layout
     *
     * @return last visible row
     */
    public int getLastVisibleRow() {
        return Math.max(0, Math.min(mLayout.getLayoutHeight(), getOffsetY() + getHeight()) / getRowHeight());
    }

    /**
     * Get last visible line on screen
     *
     * @return last visible line
     */
    public int getLastVisibleLine() {
        try {
            return mLayout.getLineNumberForRow(getLastVisibleRow());
        } catch (IndexOutOfBoundsException e) {
            return getLineCount() - 1;
        }
    }

    /**
     * Checks whether this row is visible on screen
     *
     * @param row Row to check
     * @return Whether visible
     */
    public boolean isRowVisible(int row) {
        return (getFirstVisibleRow() <= row && row <= getLastVisibleRow());
    }

    /**
     * Get baseline directly
     *
     * @param row Row
     * @return baseline y offset
     */
    public int getRowBaseline(int row) {
        return getRowHeight() * (row + 1) - mTextMetrics.descent;
    }

    /**
     * Get row height
     *
     * @return height of single row
     */
    public int getRowHeight() {
        return mTextMetrics.descent - mTextMetrics.ascent;
    }

    /**
     * Get row top y offset
     *
     * @param row Row
     * @return top y offset
     */
    public int getRowTop(int row) {
        return getRowHeight() * row;
    }

    /**
     * Get row bottom y offset
     *
     * @param row Row
     * @return Bottom y offset
     */
    public int getRowBottom(int row) {
        return getRowHeight() * (row + 1);
    }

    /**
     * Get scroll x
     *
     * @return scroll x
     */
    public int getOffsetX() {
        return mEventHandler.getScroller().getCurrX();
    }

    /**
     * Get scroll y
     *
     * @return scroll y
     */
    public int getOffsetY() {
        return mEventHandler.getScroller().getCurrY();
    }

    /**
     * @return Whether editable
     * @see CodeEditor#setEditable(boolean)
     */
    public boolean isEditable() {
        return mEditable;
    }

    /**
     * Set whether text can be edited
     *
     * @param editable Editable
     */
    public void setEditable(boolean editable) {
        mEditable = editable;
        if (!editable) {
            hideSoftInput();
        }
    }

    /**
     * Allow scale text size by thumb
     *
     * @param scale Whether allow
     */
    public void setScalable(boolean scale) {
        mScalable = scale;
    }

    /**
     * @return Whether allow scaling
     * @see CodeEditor#setScalable(boolean)
     */
    public boolean isScalable() {
        return mScalable;
    }

    public void setBlockLineEnabled(boolean enabled) {
        mBlockLineEnabled = enabled;
        invalidate();
    }

    public boolean isBlockLineEnabled() {
        return mBlockLineEnabled;
    }

    /**
     * Get the target cursor to move when shift is pressed
     */
    private CharPosition getSelectingTarget() {
        if (mCursor.left().equals(mSelectionAnchor)) {
            return mCursor.right();
        } else {
            return mCursor.left();
        }
    }

    /**
     * Make sure the moving selection is visible
     */
    private void ensureSelectingTargetVisible() {
        if (mCursor.left().equals(mSelectionAnchor)) {
            // Ensure right selection visible
            ensureSelectionVisible();
        } else {
            ensurePositionVisible(mCursor.getLeftLine(), mCursor.getLeftColumn());
        }
    }

    /**
     * Move the selection down
     * If the auto complete panel is shown,move the selection in panel to next
     */
    public void moveSelectionDown() {
        if (mSelectionAnchor == null) {
            if (mCompletionWindow.isShowing()) {
                mCompletionWindow.moveDown();
                return;
            }
            long pos = mCursor.getDownOf(IntPair.pack(mCursor.getLeftLine(), mCursor.getLeftColumn()));
            setSelection(IntPair.getFirst(pos), IntPair.getSecond(pos));
        } else {
            mCompletionWindow.hide();
            long pos = mCursor.getDownOf(getSelectingTarget().toIntPair());
            setSelectionRegion(mSelectionAnchor.line, mSelectionAnchor.column, IntPair.getFirst(pos), IntPair.getSecond(pos), false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move the selection up
     * If Auto complete panel is shown,move the selection in panel to last
     */
    public void moveSelectionUp() {
        if (mSelectionAnchor == null) {
            if (mCompletionWindow.isShowing()) {
                mCompletionWindow.moveUp();
                return;
            }
            long pos = mCursor.getUpOf(IntPair.pack(mCursor.getLeftLine(), mCursor.getLeftColumn()));
            setSelection(IntPair.getFirst(pos), IntPair.getSecond(pos));
        } else {
            mCompletionWindow.hide();
            long pos = mCursor.getUpOf(getSelectingTarget().toIntPair());
            setSelectionRegion(mSelectionAnchor.line, mSelectionAnchor.column, IntPair.getFirst(pos), IntPair.getSecond(pos), false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move the selection left
     */
    public void moveSelectionLeft() {
        if (mSelectionAnchor == null) {
            Cursor c = getCursor();
            int line = c.getLeftLine();
            int column = c.getLeftColumn();
            long pos = mCursor.getLeftOf(IntPair.pack(line, column));
            int lineAfter = IntPair.getFirst(pos);
            int columnAfter = IntPair.getSecond(pos);
            setSelection(lineAfter, columnAfter);
            if (line == lineAfter) {
                if (mCompletionWindow.isShowing()) {
                    if (columnAfter == 0) {
                        mCompletionWindow.hide();
                    } else {
                        mCompletionWindow.requireCompletion();
                    }
                }
            }
        } else {
            mCompletionWindow.hide();
            long pos = mCursor.getLeftOf(getSelectingTarget().toIntPair());
            setSelectionRegion(mSelectionAnchor.line, mSelectionAnchor.column, IntPair.getFirst(pos), IntPair.getSecond(pos), false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move the selection right
     */
    public void moveSelectionRight() {
        if (mSelectionAnchor == null) {
            Cursor c = getCursor();
            int line = c.getLeftLine();
            int column = c.getLeftColumn();
            int c_column = getText().getColumnCount(line);
            long pos = mCursor.getRightOf(IntPair.pack(line, column));
            int lineAfter = IntPair.getFirst(pos);
            int columnAfter = IntPair.getSecond(pos);
            setSelection(lineAfter, columnAfter);
            if (line == lineAfter && mCompletionWindow.isShowing()) {
                mCompletionWindow.requireCompletion();
            }
        } else {
            mCompletionWindow.hide();
            long pos = mCursor.getRightOf(getSelectingTarget().toIntPair());
            setSelectionRegion(mSelectionAnchor.line, mSelectionAnchor.column, IntPair.getFirst(pos), IntPair.getSecond(pos), false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move selection to end of line
     */
    public void moveSelectionEnd() {
        if (mSelectionAnchor == null) {
            int line = mCursor.getLeftLine();
            setSelection(line, getText().getColumnCount(line));
        } else {
            int line = getSelectingTarget().line;
            setSelectionRegion(mSelectionAnchor.line, mSelectionAnchor.column, line, getText().getColumnCount(line), false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move selection to start of line
     */
    public void moveSelectionHome() {
        if (mSelectionAnchor == null) {
            setSelection(mCursor.getLeftLine(), 0);
        } else {
            setSelectionRegion(mSelectionAnchor.line, mSelectionAnchor.column, getSelectingTarget().line, 0, false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move selection to given position
     *
     * @param line          The line to move
     * @param column        The column to move
     */
    public void setSelection(int line, int column) {
        setSelection(line, column, SelectionChangeEvent.CAUSE_UNKNOWN);
    }

    /**
     * Move selection to given position
     *
     * @param line   The line to move
     * @param column The column to move
     */
    public void setSelection(int line, int column, int cause) {
        setSelection(line, column, true, cause);
    }

    /**
     * Move selection to given position
     *
     * @param line          The line to move
     * @param column        The column to move
     * @param makeItVisible Make the character visible
     */
    public void setSelection(int line, int column, boolean makeItVisible) {
        setSelection(line, column, makeItVisible, SelectionChangeEvent.CAUSE_UNKNOWN);
    }

    /**
     * Move selection to given position
     *
     * @param line          The line to move
     * @param column        The column to move
     * @param makeItVisible Make the character visible
     */
    public void setSelection(int line, int column, boolean makeItVisible, int cause) {
        invalidateInCursor();
        mCursorAnimator.markStartPos();
        if (column > 0 && Character.isHighSurrogate(mText.charAt(line, column - 1))) {
            column++;
            if (column > mText.getColumnCount(line)) {
                column--;
            }
        }
        mCursor.set(line, column);
        if (mHighlightCurrentBlock) {
            mCursorPosition = findCursorBlock();
        }
        updateCursor();
        invalidateInCursor();
        if (!mEventHandler.hasAnyHeldHandle()) {
            mCursorAnimator.markEndPosAndStart();
        }
        if (makeItVisible) {
            ensurePositionVisible(line, column);
        } else {
            invalidate();
        }
        onSelectionChanged(cause);
    }

    /**
     * Select all text
     */
    public void selectAll() {
        setSelectionRegion(0, 0, getLineCount() - 1, getText().getColumnCount(getLineCount() - 1));
    }

    /**
     * Set selection region with a call to {@link CodeEditor#ensureSelectionVisible()}
     *
     * @param lineLeft    Line left
     * @param columnLeft  Column Left
     * @param lineRight   Line right
     * @param columnRight Column right
     */
    public void setSelectionRegion(int lineLeft, int columnLeft, int lineRight, int columnRight, int cause) {
        setSelectionRegion(lineLeft, columnLeft, lineRight, columnRight, true, cause);
    }

    /**
     * Set selection region with a call to {@link CodeEditor#ensureSelectionVisible()}
     *
     * @param lineLeft    Line left
     * @param columnLeft  Column Left
     * @param lineRight   Line right
     * @param columnRight Column right
     */
    public void setSelectionRegion(int lineLeft, int columnLeft, int lineRight, int columnRight) {
        setSelectionRegion(lineLeft, columnLeft, lineRight, columnRight, true, SelectionChangeEvent.CAUSE_UNKNOWN);
    }

    /**
     * Set selection region
     *
     * @param lineLeft         Line left
     * @param columnLeft       Column Left
     * @param lineRight        Line right
     * @param columnRight      Column right
     * @param makeRightVisible Whether to make right cursor visible
     */
    public void setSelectionRegion(int lineLeft, int columnLeft, int lineRight, int columnRight, boolean makeRightVisible) {
        setSelectionRegion(lineLeft, columnLeft, lineRight, columnRight, makeRightVisible, SelectionChangeEvent.CAUSE_UNKNOWN);
    }

    /**
     * Set selection region
     *
     * @param lineLeft         Line left
     * @param columnLeft       Column Left
     * @param lineRight        Line right
     * @param columnRight      Column right
     * @param makeRightVisible Whether to make right cursor visible
     */
    public void setSelectionRegion(int lineLeft, int columnLeft, int lineRight, int columnRight, boolean makeRightVisible, int cause) {
        invalidateInCursor();
        int start = getText().getCharIndex(lineLeft, columnLeft);
        int end = getText().getCharIndex(lineRight, columnRight);
        if (start == end) {
            setSelection(lineLeft, columnLeft);
            return;
        }
        if (start > end) {
            setSelectionRegion(lineRight, columnRight, lineLeft, columnLeft, makeRightVisible, cause);
            Log.w(LOG_TAG, "setSelectionRegion() error: start > end:start = " + start + " end = " + end + " lineLeft = " + lineLeft + " columnLeft = " + columnLeft + " lineRight = " + lineRight + " columnRight = " + columnRight);
            return;
        }
        mCursorAnimator.cancel();
        boolean lastState = mCursor.isSelected();
        if (columnLeft > 0) {
            int column = columnLeft - 1;
            char ch = mText.charAt(lineLeft, column);
            if (Character.isHighSurrogate(ch)) {
                columnLeft++;
                if (columnLeft > mText.getColumnCount(lineLeft)) {
                    columnLeft--;
                }
            }
        }
        if (columnRight > 0) {
            int column = columnRight - 1;
            char ch = mText.charAt(lineRight, column);
            if (Character.isHighSurrogate(ch)) {
                columnRight++;
                if (columnRight > mText.getColumnCount(lineRight)) {
                    columnRight--;
                }
            }
        }
        mCursor.setLeft(lineLeft, columnLeft);
        mCursor.setRight(lineRight, columnRight);
        invalidateInCursor();
        updateCursor();
        mCompletionWindow.hide();
        if (makeRightVisible) {
            ensurePositionVisible(lineRight, columnRight);
        } else {
            invalidate();
        }
        onSelectionChanged(cause);
    }

    /**
     * Move to next page
     */
    public void movePageDown() {
        mEventHandler.scrollBy(0, getHeight(), true);
        mCompletionWindow.hide();
    }

    /**
     * Move to previous page
     */
    public void movePageUp() {
        mEventHandler.scrollBy(0, -getHeight(), true);
        mCompletionWindow.hide();
    }

    /**
     * Paste text from clip board
     */
    public void pasteText() {
        try {
            if (!mClipboardManager.hasPrimaryClip() || mClipboardManager.getPrimaryClip() == null) {
                return;
            }
            ClipData.Item data = mClipboardManager.getPrimaryClip().getItemAt(0);
            CharSequence text = data.getText();
            if (text != null && mConnection != null) {
                mConnection.commitText(text, 0);
            }
            notifyIMEExternalCursorChange();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Copy text to clip board
     */
    public void copyText() {
        try {
            if (mCursor.isSelected()) {
                String clip = getText().subContent(mCursor.getLeftLine(),
                        mCursor.getLeftColumn(),
                        mCursor.getRightLine(),
                        mCursor.getRightColumn()).toString();
                mClipboardManager.setPrimaryClip(ClipData.newPlainText(clip, clip));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Copy text to clipboard and delete them
     */
    public void cutText() {
        copyText();
        if (mCursor.isSelected()) {
            deleteText();
            notifyIMEExternalCursorChange();
        }
    }

    /**
     * @return Text displaying, the result is read-only. You should not make changes to this object as it is used internally
     * @see CodeEditor#setText(CharSequence)
     * @see CodeEditor#setText(CharSequence, Bundle)
     */
    @NonNull
    public Content getText() {
        return mText;
    }

    /**
     * Get extra argument set by {@link CodeEditor#setText(CharSequence, Bundle)}
     */
    @NonNull
    public Bundle getExtraArguments() {
        return mExtraArguments;
    }

    /**
     * Set the text to be displayed.
     * With no extra arguments.
     *
     * @param text the new text you want to display
     */
    public void setText(@Nullable CharSequence text) {
        setText(text, null);
    }

    /**
     * Sets the text to be displayed.
     *
     * @param text the new text you want to display
     * @param extraArguments Extra arguments for the document. This {@link Bundle} object is passed
     *                       to all languages and plugins in editor.
     */
    public void setText(@Nullable CharSequence text, @Nullable Bundle extraArguments) {
        if (text == null) {
            text = "";
        }

        if (mText != null) {
            mText.removeContentListener(this);
            mText.setLineListener(null);
        }
        mExtraArguments = extraArguments == null ? new Bundle() : extraArguments;
        mText = new Content(text);
        mCursor = mText.getCursor();
        mEventHandler.reset();
        mText.addContentListener(this);
        mText.setUndoEnabled(mUndoEnabled);
        mText.setLineListener(this);

        if (mLanguage != null) {
            mLanguage.getAnalyzeManager().reset(new ContentReference(mText), extraArguments);
        }

        requestLayout();

        dispatchEvent(new ContentChangeEvent(this, ContentChangeEvent.ACTION_SET_NEW_TEXT, new CharPosition(), mText.getIndexer().getCharPosition(getLineCount() - 1, mText.getColumnCount(getLineCount() - 1)), mText));
        if (mInputMethodManager != null) {
            mInputMethodManager.restartInput(this);
        }
        createLayout();
        invalidateHwRenderer();
        invalidate();
    }

    /**
     * Set the editor's text size in sp unit. This value must be > 0
     *
     * @param textSize the editor's text size in <strong>Sp</strong> units.
     */
    public void setTextSize(float textSize) {
        Context context = getContext();
        Resources res;

        if (context == null) {
            res = Resources.getSystem();
        } else {
            res = context.getResources();
        }

        setTextSizePx(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, res.getDisplayMetrics()));
    }

    /**
     * Subscribe event of the given type.
     *
     * @see EventManager#subscribeEvent(Class, EventReceiver)
     */
    public <T extends Event> void subscribeEvent(Class<T> eventType, EventReceiver<T> receiver) {
        mEventManager.subscribeEvent(eventType, receiver);
    }

    /**
     * Dispatch the given event
     *
     * @see EventManager#dispatchEvent(Event)
     */
    public <T extends Event> boolean dispatchEvent(T event) {
        return mEventManager.dispatchEvent(event);
    }

    /**
     * Check whether line numbers are shown
     *
     * @return The state of line number displaying
     */
    public boolean isLineNumberEnabled() {
        return mLineNumberEnabled;
    }

    /**
     * Set whether we should display line numbers
     *
     * @param lineNumberEnabled The state of line number displaying
     */
    public void setLineNumberEnabled(boolean lineNumberEnabled) {
        if (lineNumberEnabled != mLineNumberEnabled && isWordwrap()) {
            createLayout();
        }
        mLineNumberEnabled = lineNumberEnabled;
        invalidate();
    }

    /**
     * Get the paint of the editor
     * You should not change text size and other attributes that are related to text measuring by the object
     *
     * @return The paint which is used by the editor now
     */
    @NonNull
    public Paint getTextPaint() {
        return mPaint;
    }

    /**
     * Get the ColorScheme object of this editor
     * You can config colors of some regions, texts and highlight text
     *
     * @return ColorScheme object using
     */
    @NonNull
    public EditorColorScheme getColorScheme() {
        return mColors;
    }

    /**
     * Set a new color scheme for editor.
     * <p>
     * It can be a subclass of {@link EditorColorScheme}.
     * The scheme object can only be applied to one editor instance.
     * Otherwise, an IllegalStateException is thrown.
     *
     * @param colors A non-null and free EditorColorScheme
     */
    public void setColorScheme(@NonNull EditorColorScheme colors) {
        colors.attachEditor(this);
        if (mColors != null) {
            mColors.detachEditor(this);
        }
        mColors = colors;
        if (mCompletionWindow != null) {
            mCompletionWindow.applyColorScheme();
        }
        invalidateHwRenderer();
        invalidate();
    }

    /**
     * Move selection to line start with scrolling
     *
     * @param line Line to jump
     */
    public void jumpToLine(int line) {
        setSelection(line, 0);
    }


    /**
     * Rerun analysis forcibly
     */
    public void rerunAnalysis() {
        if (mLanguage != null) {
            mLanguage.getAnalyzeManager().rerun();
        }
    }

    /**
     * Get analyze result.
     * <strong>Do not make changes to it or read concurrently</strong>
     */
    @Nullable
    public Styles getStyles() {
        return mStyles;
    }

    /**
     * Hide auto complete window if shown
     */
    public void hideAutoCompleteWindow() {
        if (mCompletionWindow != null) {
            mCompletionWindow.hide();
        }
    }

    /**
     * Get cursor code block index
     *
     * @return index of cursor's code block
     */
    public int getBlockIndex() {
        return mCursorPosition;
    }


    //-------------------------------------------------------------------------------
    //-------------------------IME Interaction---------------------------------------
    //-------------------------------------------------------------------------------

    /**
     * Display soft input method for self
     */
    public void showSoftInput() {
        if (isEditable() && isEnabled()) {
            if (isInTouchMode()) {
                requestFocusFromTouch();
            }
            if (!hasFocus()) {
                requestFocus();
            }
            mInputMethodManager.showSoftInput(this, 0);
        }
        invalidate();
    }

    /**
     * Hide soft input
     */
    public void hideSoftInput() {
        mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    /**
     * Send current selection position to input method
     */
    protected void updateSelection() {
        int candidatesStart = -1, candidatesEnd = -1;
        if (mConnection.mComposingLine != -1) {
            try {
                candidatesStart = mText.getCharIndex(mConnection.mComposingLine, mConnection.mComposingStart);
                candidatesEnd = mText.getCharIndex(mConnection.mComposingLine, mConnection.mComposingEnd);
            } catch (IndexOutOfBoundsException e) {
                //Ignored
            }
        }
        mInputMethodManager.updateSelection(this, mCursor.getLeft(), mCursor.getRight(), candidatesStart, candidatesEnd);
    }

    /**
     * Update request result for monitoring request
     */
    protected void updateExtractedText() {
        if (mExtracting != null) {
            //Logs.log("Send extracted text updates");
            mInputMethodManager.updateExtractedText(this, mExtracting.token, extractText(mExtracting));
        }
    }

    /**
     * Set request needed to update when editor updates selection
     */
    protected void setExtracting(@Nullable ExtractedTextRequest request) {
        mExtracting = request;
    }

    /**
     * Extract text in editor for input method
     */
    protected ExtractedText extractText(@NonNull ExtractedTextRequest request) {
        Cursor cur = getCursor();
        ExtractedText text = new ExtractedText();
        int selBegin = cur.getLeft();
        int selEnd = cur.getRight();
        int startOffset;
        if (request.hintMaxChars == 0) {
            request.hintMaxChars = mProps.maxIPCTextLength;
        }
        startOffset = 0;
        text.text = mConnection.getTextRegion(startOffset, startOffset + request.hintMaxChars, request.flags);
        text.startOffset = startOffset;
        text.selectionStart = selBegin - startOffset;
        text.selectionEnd = selEnd - startOffset;
        if (selBegin != selEnd) {
            text.flags |= ExtractedText.FLAG_SELECTING;
        }
        return text;
    }

    /**
     * Notify input method that text has been changed for external reason
     */
    public void notifyIMEExternalCursorChange() {
        updateExtractedText();
        updateSelection();
        updateCursorAnchor();
        // Restart if composing
        if (mConnection.mComposingLine != -1) {
            restartInput();
        }
    }

    /**
     * Restart the input connection.
     * Do not call this method randomly. Please refer to documentation first.
     *
     * @see InputConnection
     */
    public void restartInput() {
        if (mConnection != null)
            mConnection.invalid();
        if (mInputMethodManager != null)
            mInputMethodManager.restartInput(this);
    }

    /**
     * Send cursor position in text and on screen to input method
     */
    public void updateCursor() {
        updateCursorAnchor();
        updateExtractedText();
        if (!mText.isInBatchEdit()) {
            updateSelection();
        }
    }

    /**
     * Release any resources held by editor.
     * This will stop completion threads and destroy using {@link Language} object.
     * <p>
     * Recommend to call if the activity is to destroy.
     */
    public void release() {
        mCompletionWindow.cancelCompletion();
        if (mLanguage != null) {
            mLanguage.getAnalyzeManager().destroy();
            mLanguage.destroy();
            mLanguage = new EmptyLanguage();
        }
    }

    //-------------------------------------------------------------------------------
    //------------------------Internal Callbacks-------------------------------------
    //-------------------------------------------------------------------------------

    /**
     * Called by ColorScheme to notify invalidate
     *
     * @param type Color type changed
     */
    public void onColorUpdated(int type) {
        if (type == EditorColorScheme.AUTO_COMP_PANEL_BG || type == EditorColorScheme.AUTO_COMP_PANEL_CORNER) {
            if (mCompletionWindow != null)
                mCompletionWindow.applyColorScheme();
            return;
        }
        invalidateHwRenderer();
        invalidate();
    }

    /**
     * Called by color scheme to init colors
     */
    public void onColorFullUpdate() {
        if (mCompletionWindow != null)
            mCompletionWindow.applyColorScheme();
        invalidateHwRenderer();
        invalidate();
    }

    /**
     * Get using InputMethodManager
     */
    protected InputMethodManager getInputMethodManager() {
        return mInputMethodManager;
    }

    /**
     * Called by CodeEditorInputConnection
     */
    protected void onCloseConnection() {
        setExtracting(null);
        invalidate();
    }

    /**
     * Called when the text is edited or {@link CodeEditor#setSelection} is called
     */
    protected void onSelectionChanged(int cause) {
        dispatchEvent(new SelectionChangeEvent(this, cause));
    }

    //-------------------------------------------------------------------------------
    //-------------------------Override methods--------------------------------------
    //-------------------------------------------------------------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
        // Update magnifier
        if ((mLastCursorState != mCursorBlink.visibility || !mEventHandler.getScroller().isFinished()) && mEventHandler.mMagnifier.isShowing()) {
            mLastCursorState = mCursorBlink.visibility;
            post(mEventHandler.mMagnifier::updateDisplay);
        }
    }

    @Override
    public AccessibilityNodeInfo createAccessibilityNodeInfo() {
        AccessibilityNodeInfo node = super.createAccessibilityNodeInfo();
        node.setEditable(isEditable());
        node.setTextSelection(mCursor.getLeft(), mCursor.getRight());
        node.setScrollable(true);
        node.setInputType(InputType.TYPE_CLASS_TEXT);
        node.setMultiLine(true);
        node.setText(getText().toStringBuilder());
        node.setLongClickable(true);
        node.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_COPY);
        node.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CUT);
        node.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_PASTE);
        node.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_TEXT);
        node.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
        node.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
        return node;
    }

    @Override
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        switch (action) {
            case AccessibilityNodeInfo.ACTION_COPY:
                copyText();
                return true;
            case AccessibilityNodeInfo.ACTION_CUT:
                cutText();
                return true;
            case AccessibilityNodeInfo.ACTION_PASTE:
                pasteText();
                return true;
            case AccessibilityNodeInfo.ACTION_SET_TEXT:
                setText(arguments.getCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE));
                return true;
            case AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD:
                movePageDown();
                return true;
            case AccessibilityNodeInfo.ACTION_SCROLL_FORWARD:
                movePageUp();
                return true;
        }
        return super.performAccessibilityAction(action, arguments);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                if (mForceHorizontalScrollable) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mDownX;
                if (mForceHorizontalScrollable) {
                    if (deltaX > 0 && getScroller().getCurrX() == 0
                            || deltaX < 0 && getScroller().getCurrX() == getScrollMaxX()) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return isEnabled() && isEditable();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        if (!isEditable() || !isEnabled()) {
            return null;
        }
        outAttrs.inputType = mInputType != 0 ? mInputType : EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE;
        outAttrs.initialSelStart = getCursor() != null ? getCursor().getLeft() : 0;
        outAttrs.initialSelEnd = getCursor() != null ? getCursor().getRight() : 0;
        outAttrs.initialCapsMode = mConnection.getCursorCapsMode(0);

        // Prevent fullscreen when the screen height is too small
        // Especially in landscape mode
        if (!mProps.allowFullscreen) {
            outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN;
        }

        mConnection.reset();
        setExtracting(null);
        return mConnection;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        boolean handlingBefore = mEventHandler.handlingMotions();
        boolean res = mEventHandler.onTouchEvent(event);
        boolean handling = mEventHandler.handlingMotions();
        boolean res2 = false;
        boolean res3 = false;
        if (!handling && !handlingBefore) {
            res2 = mBasicDetector.onTouchEvent(event);
            res3 = mScaleDetector.onTouchEvent(event);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mVerticalEdgeGlow.onRelease();
            mHorizontalGlow.onRelease();
        }
        return (res3 || res2 || res);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mKeyMetaStates.onKeyDown(event);
        var e = new EditorKeyEvent(this, event);
        if (mEventManager.dispatchEvent(e)) {
            return e.result(false);
        }
        boolean isShiftPressed = mKeyMetaStates.isShiftPressed();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_MOVE_HOME:
            case KeyEvent.KEYCODE_MOVE_END:
                if (isShiftPressed && (!mCursor.isSelected())) {
                    mSelectionAnchor = mCursor.left();
                } else if (!isShiftPressed && mSelectionAnchor != null) {
                    mSelectionAnchor = null;
                }
                mKeyMetaStates.adjust();
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (mCursor.isSelected()) {
                    setSelection(mCursor.getLeftLine(), mCursor.getLeftColumn());
                    return e.result(true);
                }
                return e.result(false);
            }
            case KeyEvent.KEYCODE_DEL:
                if (isEditable()) {
                    deleteText();
                    notifyIMEExternalCursorChange();
                }
                return e.result(true);
            case KeyEvent.KEYCODE_FORWARD_DEL: {
                if (isEditable()) {
                    mConnection.deleteSurroundingText(0, 1);
                    notifyIMEExternalCursorChange();
                }
                return e.result(true);
            }
            case KeyEvent.KEYCODE_ENTER: {
                if (isEditable()) {
                    if (mCompletionWindow.isShowing()) {
                        mCompletionWindow.select();
                        return true;
                    }
                    NewlineHandler[] handlers = mLanguage.getNewlineHandlers();
                    if (handlers == null || getCursor().isSelected()) {
                        commitText("\n", true);
                    } else {
                        ContentLine line = mText.getLine(mCursor.getLeftLine());
                        int index = mCursor.getLeftColumn();
                        String beforeText = line.subSequence(0, index).toString();
                        String afterText = line.subSequence(index, line.length()).toString();
                        boolean consumed = false;
                        for (NewlineHandler handler : handlers) {
                            if (handler != null) {
                                if (handler.matchesRequirement(beforeText, afterText)) {
                                    try {
                                        NewlineHandleResult result = handler.handleNewline(beforeText, afterText, getTabWidth());
                                        if (result != null) {
                                            commitText(result.text, false);
                                            int delta = result.shiftLeft;
                                            if (delta != 0) {
                                                int newSel = Math.max(getCursor().getLeft() - delta, 0);
                                                CharPosition charPosition = getCursor().getIndexer().getCharPosition(newSel);
                                                setSelection(charPosition.line, charPosition.column);
                                            }
                                            consumed = true;
                                        } else {
                                            continue;
                                        }
                                    } catch (Exception ex) {
                                        Log.w(LOG_TAG, "Error occurred while calling Language's NewlineHandler", ex);
                                    }
                                    break;
                                }
                            }
                        }
                        if (!consumed) {
                            commitText("\n", true);
                        }
                    }
                    notifyIMEExternalCursorChange();
                }
                return e.result(true);
            }
            case KeyEvent.KEYCODE_DPAD_DOWN:
                moveSelectionDown();
                return e.result(true);
            case KeyEvent.KEYCODE_DPAD_UP:
                moveSelectionUp();
                return e.result(true);
            case KeyEvent.KEYCODE_DPAD_LEFT:
                moveSelectionLeft();
                return e.result(true);
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                moveSelectionRight();
                return e.result(true);
            case KeyEvent.KEYCODE_MOVE_END:
                moveSelectionEnd();
                return e.result(true);
            case KeyEvent.KEYCODE_MOVE_HOME:
                moveSelectionHome();
                return e.result(true);
            case KeyEvent.KEYCODE_PAGE_DOWN:
                movePageDown();
                return e.result(true);
            case KeyEvent.KEYCODE_PAGE_UP:
                movePageUp();
                return e.result(true);
            case KeyEvent.KEYCODE_TAB:
                if (isEditable()) {
                    if (mCompletionWindow.isShowing()) {
                        mCompletionWindow.select();
                    } else {
                        commitTab();
                    }
                }
                return e.result(true);
            case KeyEvent.KEYCODE_PASTE:
                if (isEditable()) {
                    pasteText();
                }
                return e.result(true);
            case KeyEvent.KEYCODE_COPY:
                copyText();
                return e.result(true);
            case KeyEvent.KEYCODE_SPACE:
                if (isEditable()) {
                    commitText(" ");
                    notifyIMEExternalCursorChange();
                }
                return e.result(true);
            default:
                if (event.isCtrlPressed() && !event.isAltPressed()) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_V:
                            if (isEditable()) {
                                pasteText();
                            }
                            return e.result(true);
                        case KeyEvent.KEYCODE_C:
                            copyText();
                            return e.result(true);
                        case KeyEvent.KEYCODE_X:
                            if (isEditable()) {
                                cutText();
                            } else {
                                copyText();
                            }
                            return e.result(true);
                        case KeyEvent.KEYCODE_A:
                            selectAll();
                            return e.result(true);
                        case KeyEvent.KEYCODE_Z:
                            if (isEditable()) {
                                undo();
                            }
                            return e.result(true);
                        case KeyEvent.KEYCODE_Y:
                            if (isEditable()) {
                                redo();
                            }
                            return e.result(true);
                    }
                } else if (!event.isCtrlPressed() && !event.isAltPressed()) {
                    if (event.isPrintingKey() && isEditable()) {
                        String text = new String(Character.toChars(event.getUnicodeChar(event.getMetaState())));
                        SymbolPairMatch.Replacement replacement = null;
                        if (text.length() == 1 && mProps.symbolPairAutoCompletion) {
                            replacement = mLanguageSymbolPairs.getCompletion(text.charAt(0));
                        }
                        if (replacement == null || replacement == SymbolPairMatch.Replacement.NO_REPLACEMENT
                                || (replacement.shouldNotDoReplace(getText()) && replacement.notHasAutoSurroundPair())) {
                            commitText(text);
                            notifyIMEExternalCursorChange();
                        } else {
                            String[] autoSurroundPair;
                            if (getCursor().isSelected() && (autoSurroundPair = replacement.getAutoSurroundPair()) != null) {
                                getText().beginBatchEdit();
                                //insert left
                                getText().insert(getCursor().getLeftLine(), getCursor().getLeftColumn(), autoSurroundPair[0]);
                                //insert right
                                getText().insert(getCursor().getRightLine(), getCursor().getRightColumn(), autoSurroundPair[1]);
                                getText().endBatchEdit();
                                //cancel selected
                                setSelection(getCursor().getLeftLine(), getCursor().getLeftColumn() + autoSurroundPair[0].length() - 1);

                                notifyIMEExternalCursorChange();
                            } else {
                                commitText(replacement.text);
                                int delta = (replacement.text.length() - replacement.selection);
                                if (delta != 0) {
                                    int newSel = Math.max(getCursor().getLeft() - delta, 0);
                                    CharPosition charPosition = getCursor().getIndexer().getCharPosition(newSel);
                                    setSelection(charPosition.line, charPosition.column);
                                    notifyIMEExternalCursorChange();
                                }
                            }

                        }
                    } else {
                        return super.onKeyDown(keyCode, event);
                    }
                    return e.result(true);
                }
        }
        return e.result(super.onKeyDown(keyCode, event));
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        mKeyMetaStates.onKeyUp(event);
        var e = new EditorKeyEvent(this, event);
        if (mEventManager.dispatchEvent(e)) {
            return e.result(false);
        }
        if (!mKeyMetaStates.isShiftPressed() && mSelectionAnchor != null && !mCursor.isSelected()) {
            mSelectionAnchor = null;
            return e.result(true);
        }
        return e.result(super.onKeyUp(keyCode, event));
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        var e = new EditorKeyEvent(this, event);
        if (mEventManager.dispatchEvent(e)) {
            return e.result(false);
        }
        return e.result(super.onKeyMultiple(keyCode, repeatCount, event));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean warn = false;
        //Fill the horizontal layout if WRAP_CONTENT mode
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
            warn = true;
        }
        //Fill the vertical layout if WRAP_CONTENT mode
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST || MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
            warn = true;
        }
        if (warn) {
            Log.i(LOG_TAG, "onMeasure():Code editor does not support wrap_content mode when measuring.It will just fill the whole space.");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_SCROLL && event.isFromSource(InputDevice.SOURCE_CLASS_POINTER)) {
            float v_scroll = -event.getAxisValue(MotionEvent.AXIS_VSCROLL);
            float h_scroll = -event.getAxisValue(MotionEvent.AXIS_HSCROLL);
            mEventHandler.onScroll(event, event, h_scroll * mVerticalScrollFactor, v_scroll * mVerticalScrollFactor);
            return true;
        }
        return super.onGenericMotionEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        mViewRect.right = w;
        mViewRect.bottom = h;
        getVerticalEdgeEffect().setSize(w, h);
        getHorizontalEdgeEffect().setSize(h, w);
        getVerticalEdgeEffect().finish();
        getHorizontalEdgeEffect().finish();
        if (isWordwrap() && w != oldWidth) {
            createLayout();
        } else {
            mEventHandler.scrollBy(getOffsetX() > getScrollMaxX() ? getScrollMaxX() - getOffsetX() : 0, getOffsetY() > getScrollMaxY() ? getScrollMaxY() - getOffsetY() : 0);
        }
        if (oldHeight > h) {
            ensureSelectionVisible();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mCursorBlink.valid = mCursorBlink.period > 0;
        if (mCursorBlink.valid) {
            post(mCursorBlink);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCursorBlink.valid = false;
        removeCallbacks(mCursorBlink);
    }

    @Override
    public void computeScroll() {
        if (mEventHandler.getScroller().computeScrollOffset()) {
            invalidate();
        }
    }

    @Override
    public void beforeReplace(Content content) {
        mWait = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mRenderer.beforeReplace(content);
        }
        mLayout.beforeReplace(content);
    }

    @Override
    public void afterInsert(Content content, int startLine, int startColumn, int endLine, int endColumn, CharSequence insertedContent) {
        updateTimestamp();
        var start = mText.getIndexer().getCharPosition(startLine, startColumn);
        var end = mText.getIndexer().getCharPosition(endLine, endColumn);
        for (int i = startLine; i <= endLine && i < getLineCount(); i++) {
            mText.getLine(i).widthCache = null;
        }

        // Update spans
        try {
            if (mStyles != null) {
                mStyles.adjustOnInsert(start, end);
            }
        } catch (Exception e) {
            Log.w(LOG_TAG, "Update failure", e);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mRenderer.afterInsert(content, startLine, startColumn, endLine, endColumn, insertedContent);
        }
        mLayout.afterInsert(content, startLine, startColumn, endLine, endColumn, insertedContent);

        // Notify input method
        updateCursor();
        mWait = false;

        // Auto completion
        if (mCompletionWindow.isEnabled() && !mText.isUndoOrRedo()) {
            if ((mConnection.mComposingLine == -1 || mProps.autoCompletionOnComposing) && endColumn != 0 && startLine == endLine) {
                mCompletionWindow.requireCompletion();
            } else {
                mCompletionWindow.hide();
            }
            updateCompletionWindowPosition(mCompletionWindow.isShowing());
        } else {
            mCompletionWindow.hide();
        }

        //Log.d(LOG_TAG, "Ins: " + startLine + " " + startColumn + ", " + endLine + " " + endColumn + ", content = " + insertedContent);
        updateCursorAnchor();

        invalidateInCursor();
        invalidateChanged(startLine, endLine);
        ensureSelectionVisible();

        mLanguage.getAnalyzeManager().insert(start, end, insertedContent);
        mEventHandler.hideInsertHandle();
        onSelectionChanged(SelectionChangeEvent.CAUSE_TEXT_MODIFICATION);
        if (!mCursor.isSelected()) {
            mCursorAnimator.markEndPosAndStart();
        }
        dispatchEvent(new ContentChangeEvent(this, ContentChangeEvent.ACTION_INSERT, start, end, insertedContent));
    }

    /**
     * Update timestamp required for measuring cache
     */
    protected void updateTimestamp() {
        mTimestamp = System.nanoTime();
    }

    @Override
    public void afterDelete(Content content, int startLine, int startColumn, int endLine, int endColumn, CharSequence deletedContent) {
        updateTimestamp();
        var start = mText.getIndexer().getCharPosition(startLine, startColumn);
        var end = start.fromThis();
        end.column = endColumn;
        end.line = endLine;
        end.index = start.index + deletedContent.length();

        for (int i = startLine; i <= startLine + 1 && i < getLineCount(); i++) {
            mText.getLine(i).widthCache = null;
        }

        try {
            if (mStyles != null) {
                mStyles.adjustOnDelete(start, end);
            }
        } catch (Exception e) {
            Log.w(LOG_TAG, "Update failure", e);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mRenderer.afterDelete(content, startLine, startColumn, endLine, endColumn, deletedContent);
        }
        mLayout.afterDelete(content, startLine, startColumn, endLine, endColumn, deletedContent);

        updateCursor();

        if (mCompletionWindow.isEnabled() && !mText.isUndoOrRedo()) {
            if (mConnection.mComposingLine == -1 && mCompletionWindow.isShowing()) {
                if (startLine != endLine || startColumn != endColumn - 1) {
                    mCompletionWindow.hide();
                } else {
                    mCompletionWindow.requireCompletion();
                    updateCompletionWindowPosition();
                }
            }
        } else {
            mCompletionWindow.hide();
        }

        //Log.d(LOG_TAG, "Del: " + startLine + " " + startColumn + ", " + endLine + " " + endColumn + ", content = " + deletedContent);

        if (!mWait) {
            updateCursorAnchor();
            invalidateInCursor();
            invalidateChanged(startLine, startLine + 1);
            ensureSelectionVisible();
            mEventHandler.hideInsertHandle();
        }
        if (!mCursor.isSelected()) {
            mCursorAnimator.markEndPosAndStart();
        }

        mLanguage.getAnalyzeManager().delete(start, end, deletedContent);
        onSelectionChanged(SelectionChangeEvent.CAUSE_TEXT_MODIFICATION);
        dispatchEvent(new ContentChangeEvent(this, ContentChangeEvent.ACTION_DELETE, start, end, deletedContent));
    }

    @Override
    public void beforeModification(Content content) {
        mCursorAnimator.markStartPos();
    }

    @Override
    public void onFormatFail(final Throwable throwable) {
        post(() -> Toast.makeText(getContext(), throwable.toString(), Toast.LENGTH_SHORT).show());
        mFormatThread = null;
    }

    @Override
    public void onRemove(Content content, ContentLine line) {
        mLayout.onRemove(content, line);
    }

    @Override
    public void onFormatSucceed(CharSequence originalText, final CharSequence newText) {
        mFormatThread = null;
        if (originalText == mText) {
            post(() -> {
                int line = mCursor.getLeftLine();
                int column = mCursor.getLeftColumn();
                mText.replace(0, 0, getLineCount() - 1, mText.getColumnCount(getLineCount() - 1), newText);
                getScroller().forceFinished(true);
                mCompletionWindow.hide();
                setSelectionAround(line, column);
            });
        }
    }

    @Override
    public void setStyles(final AnalyzeManager sourceManager, Styles styles) {
        if (sourceManager == mLanguage.getAnalyzeManager()) {
            Runnable operation = () -> {
                mStyles = styles;
                if (mHighlightCurrentBlock) {
                    mCursorPosition = findCursorBlock();
                }
                invalidateHwRenderer();
                updateTimestamp();
                invalidate();
            };
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                operation.run();
            } else {
                post(operation);
            }
        }
    }

    //-------------------------------------------------------------------------------
    //-------------------------Inner classes-----------------------------------------
    //-------------------------------------------------------------------------------

    private class DrawCursorTask {

        float x;
        float y;
        int handleType;
        SelectionHandleStyle.HandleDescriptor descriptor;

        public DrawCursorTask(float x, float y, int handleType, SelectionHandleStyle.HandleDescriptor descriptor) {
            this.x = x;
            this.y = y;
            this.handleType = handleType;
            this.descriptor = descriptor;
        }

        void execute(Canvas canvas) {
            // Follow the thumb
            if (!descriptor.position.isEmpty()) {
                if ((mEventHandler.holdInsertHandle() && handleType == SelectionHandleStyle.HANDLE_TYPE_INSERT)
                        || (mEventHandler.mSelHandleType == EditorTouchEventHandler.SelectionHandle.LEFT && handleType == SelectionHandleStyle.HANDLE_TYPE_LEFT)
                        || (mEventHandler.mSelHandleType == EditorTouchEventHandler.SelectionHandle.RIGHT && handleType == SelectionHandleStyle.HANDLE_TYPE_RIGHT)) {
                    x = mEventHandler.mMotionX + (descriptor.alignment != SelectionHandleStyle.ALIGN_CENTER ? descriptor.position.width() : 0) * (descriptor.alignment == SelectionHandleStyle.ALIGN_LEFT ? 1 : -1);
                    y = mEventHandler.mMotionY - descriptor.position.height() * 2 / 3f;
                }
            }

            if (handleType != SelectionHandleStyle.HANDLE_TYPE_UNDEFINED || mCursorBlink.visibility || mEventHandler.holdInsertHandle()) {
                mRect.top = y - getRowHeight();
                mRect.bottom = y;
                mRect.left = x - mInsertSelWidth / 2f;
                mRect.right = x + mInsertSelWidth / 2f;
                drawColor(canvas, mColors.getColor(EditorColorScheme.SELECTION_INSERT), mRect);
            }
            if (handleType != SelectionHandleStyle.HANDLE_TYPE_UNDEFINED) {
                mHandleStyle.draw(canvas, handleType, x, y, getRowHeight(), mColors.getColor(EditorColorScheme.SELECTION_HANDLE), descriptor);
            } else if (descriptor != null) {
                descriptor.setEmpty();
            }
        }

    }

    private final static String COPYRIGHT = "sora-editor\nCopyright (C) Rosemoe roses2020@qq.com\nThis project is distributed under the LGPL v2.1 license";

}
