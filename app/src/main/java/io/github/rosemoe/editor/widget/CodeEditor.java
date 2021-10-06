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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.MutableInt;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.OverScroller;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutDialogTextInputBinding;
import com.itsaky.androidide.lsp.AbstractLanguageClient;
import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.utils.Symbols;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.lsp.SemanticHighlight;
import com.itsaky.lsp.services.IDELanguageServer;
import com.itsaky.toaster.Toaster;
import io.github.rosemoe.editor.interfaces.EditorEventListener;
import io.github.rosemoe.editor.interfaces.EditorLanguage;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.langs.AbstractEditorLanguage;
import io.github.rosemoe.editor.langs.EmptyLanguage;
import io.github.rosemoe.editor.struct.BlockLine;
import io.github.rosemoe.editor.struct.HexColor;
import io.github.rosemoe.editor.struct.Span;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.ContentLine;
import io.github.rosemoe.editor.text.ContentListener;
import io.github.rosemoe.editor.text.Cursor;
import io.github.rosemoe.editor.text.FontCache;
import io.github.rosemoe.editor.text.FormatThread;
import io.github.rosemoe.editor.text.LineRemoveListener;
import io.github.rosemoe.editor.text.SpanMapUpdater;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import io.github.rosemoe.editor.util.IntPair;
import io.github.rosemoe.editor.util.LongArrayList;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.CodeActionOptions;
import org.eclipse.lsp4j.DefinitionOptions;
import org.eclipse.lsp4j.DefinitionParams;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.ParameterInformation;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ReferenceContext;
import org.eclipse.lsp4j.ReferenceOptions;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.ShowDocumentParams;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SignatureHelpContext;
import org.eclipse.lsp4j.SignatureHelpOptions;
import org.eclipse.lsp4j.SignatureHelpParams;
import org.eclipse.lsp4j.SignatureHelpTriggerKind;
import org.eclipse.lsp4j.SignatureInformation;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.VersionedTextDocumentIdentifier;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.CompletionOptions;

/**
 * CodeEditor is a editor that can highlight text regions by doing basic syntax analyzing
 * This project in GitHub: https://github.com/Rosemoe/CodeEditor
 * <p>
 * Note:
 * Row and line are different in this editor
 * When we say 'row', it means a line displayed on screen. It can be a part of a line in the text object.
 * When we say 'line', it means a real line in the original text.
 *
 * @author Rosemoe
 */
public class CodeEditor extends View implements ContentListener, TextAnalyzer.Callback, FormatThread.FormatResultReceiver, LineRemoveListener {

    /**
     * The default size when creating the editor object. Unit is sp.
     */
    public static final int DEFAULT_TEXT_SIZE = 20;

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
    private int mInputType;
    private int mNonPrintableOptions;
    private int mCachedLineNumberWidth;
	private int mErrorColor;
	private int mWarningColor;
    private int mFileVersion = 0;
    private float mDpUnit;
    private float mDividerWidth;
    private float mDividerMargin;
    private float mInsertSelWidth;
    private float mBlockLineWidth;
    private float mLineInfoTextSize;
    private boolean mWait;
    private boolean mDrag;
    private boolean mScalable;
    private boolean mEditable;
    private boolean mCharPaint;
    private boolean mAutoIndentEnabled;
    private boolean mWordwrap;
    private boolean mUndoEnabled;
    private boolean mDisplayLnPanel;
    private boolean mOverScrollEnabled;
    private boolean mLineNumberEnabled;
    private boolean mBlockLineEnabled;
    private boolean mAutoCompletionEnabled;
    private boolean mFindReferencesEnabled;
    private boolean mGotoDefinitionEnabled;
    private boolean mCodeActionsEnabled;
    private boolean mSignatureHelpEnabled;
    private boolean mCompletionOnComposing;
    private boolean mHighlightSelectedText;
    private boolean mHighlightCurrentBlock;
    private boolean mHighlightCurrentLine;
    private boolean mSymbolCompletionEnabled;
    private boolean mVerticalScrollBarEnabled;
    private boolean mHorizontalScrollBarEnabled;
	private boolean mLineColorEnabled;
    private boolean mPinLineNumber;
    private boolean mFirstLineNumberAlwaysVisible;
    private RectF mRect;
	private RectF mRect2;
    private RectF mLeftHandle;
    private RectF mRightHandle;
    private RectF mInsertHandle;
    private RectF mVerticalScrollBar;
    private RectF mHorizontalScrollBar;
    private ClipboardManager mClipboardManager;
    private InputMethodManager mInputMethodManager;
    private Cursor mCursor;
    private Content mText;
    private TextAnalyzer mSpanner;
    private Paint mPaint;
    private Paint mPaintOther;
    private Paint mPaintGraph;
    private char[] mBuffer;
    private char[] mBuffer2;
    private Matrix mMatrix;
    private Rect mViewRect;
    private EditorColorScheme mColors;
    private String mLnTip = "Line:";
    private EditorLanguage mLanguage;
    private IDELanguageServer mLanguageServer;
    private long mLastMakeVisible = 0;
    private EditorAutoCompleteWindow mCompletionWindow;
    private DiagnosticWindow mDiagnosticWindow;
    private EditorTouchEventHandler mEventHandler;
    private Paint.Align mLineNumberAlign;
    private GestureDetector mBasicDetector;
    protected EditorTextActionPresenter mTextActionPresenter;
    private ScaleGestureDetector mScaleDetector;
    private CursorAnchorInfo.Builder mAnchorInfoBuilder;
    private MaterialEdgeEffect mVerticalEdgeGlow;
    private MaterialEdgeEffect mHorizontalGlow;
    private ExtractedTextRequest mExtracting;
    private FormatThread mFormatThread;
    private EditorSearcher mSearcher;
    private EditorEventListener mListener;
    private FontCache mFontCache;
    private Paint.FontMetricsInt mTextMetrics;
    private Paint.FontMetricsInt mLineNumberMetrics;
    private Paint.FontMetricsInt mGraphMetrics;
    private CursorBlink mCursorBlink;
    private SymbolPairMatch mOverrideSymbolPairs;
    private LongArrayList mPostDrawLineNumbers = new LongArrayList();
    private CharPosition mLockedSelection;
    EditorInputConnection mConnection;
    KeyMetaStates mKeyMetaStates = new KeyMetaStates(this);
	
    private List<String> mSignatureHelpTriggerChars = new ArrayList<>();
    
	private Map<HexColor, Integer> mLineColors;
    private Map<Range, Diagnostic> mDiagnostics;
    private Map<Integer, List<Range>> stringMap;
    
    private File currentFile;
    
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
        initialize();
    }

	public void setLineColorsEnabled(boolean mLineColorEnabled) {
		this.mLineColorEnabled = mLineColorEnabled;
	}

	public boolean isLineColorsEnabled() {
		return mLineColorEnabled;
	}
    
    public File getFile() {
        return currentFile;
    }
    
    /**
     * Set the current file whose contents this editor is editing <br>
     * This method must be called only once in this editors lifetime <br>
     * This will also make sure to send 'textDocument/didOpen' to any provided language server.
     *
     * @param file The file that was opened
     */
    public void setFile(File file) {
        this.currentFile = file;
        
        if(mLanguage != null && mLanguage instanceof AbstractEditorLanguage) {
            ((AbstractEditorLanguage) mLanguage).setFile(file);
        }
        
        if(mLanguageServer != null) {
            TextDocumentItem item = new TextDocumentItem();
            item.setLanguageId(getLanguageIdForFile());
            item.setText(getText().toString());
            item.setUri(file.toURI().toString());
            item.setVersion(mFileVersion = 0);
            mLanguageServer.getTextDocumentService().didOpen(new DidOpenTextDocumentParams(item));
        }
    }
    
    private String getLanguageIdForFile() {
        final String name = getFile().getName();
        
        if(name.endsWith(".java")) {
            return "java";
        }
        
        if(name.endsWith(".xml")) {
            return "xml";
        }
        
        if(name.endsWith(".kt")) {
            return "kotlin";
        }
        
        return "unknown";
    }
    
    /**
     * Show first line number in screen in word wrap mode
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
     * Whether this region has visible region on screen
     *
     * @param begin The begin line of code block
     * @param end   The end line of code block
     * @param first The first visible line on screen
     * @param last  The last visible line on screen
     * @return Whether this block can be seen
     */
    private static boolean hasVisibleRegion(int begin, int end, int first, int last) {
        return (end > first && begin < last);
    }

    /**
     * Check whether the given character is a start sign for emoji
     *
     * @param ch Character to check
     * @return Whether this is leading a emoji
     */
    private static boolean isEmoji(char ch) {
        return ch == 0xd83c || ch == 0xd83d;
    }

    /**
     * Set event listener
     *
     * @see EditorEventListener
     */
    public void setEventListener(@Nullable EditorEventListener eventListener) {
        this.mListener = eventListener;
    }

    /**
     * Cancel the next animation for {@link CodeEditor#ensurePositionVisible(int, int)}
     */
    protected void cancelAnimation() {
        mLastMakeVisible = System.currentTimeMillis();
    }

    /**
     * Hide completion window later
     */
    protected void postHideCompletionWindow() {
        // Avoid meaningless calls
        if (!mCompletionWindow.isShowing()) {
            return;
        }
        // We do this because if you hide it at once, the editor seems to flash with unknown reason
        postDelayed(() -> mCompletionWindow.hide(), 50);
    }

    /**
     * Get using EditorAutoCompleteWindow
     */
    @NonNull
    protected EditorAutoCompleteWindow getAutoCompleteWindow() {
        return mCompletionWindow;
    }

    /**
     * Get EditorTextActionPresenter instance of this editor
     *
     * @return EditorTextActionPresenter
     */
    @NonNull
    protected EditorTextActionPresenter getTextActionPresenter() {
        return mTextActionPresenter;
    }

    /**
     * Get the width of line number and divider line
     *
     * @return The width
     */
    protected float measureTextRegionOffset() {
        return isLineNumberEnabled() ? measureLineNumber() + mDividerMargin * 2 + mDividerWidth: mDpUnit * 5;
    }

    /**
     * Get the rect of left selection handle painted on view
     *
     * @return Rect of left handle
     */
    protected RectF getLeftHandleRect() {
        return mLeftHandle;
    }

    /**
     * Get the rect of right selection handle painted on view
     *
     * @return Rect of right handle
     */
    protected RectF getRightHandleRect() {
        return mRightHandle;
    }

    /**
     * Get the character's x offset on view
     *
     * @param line   The line position of character
     * @param column The column position of character
     * @return The x offset on screen
     */
    protected float getOffset(int line, int column) {
        prepareLine(line);
        return measureText(mBuffer, 0, column) + measureTextRegionOffset() - getOffsetX();
    }

    /**
     * Prepare editor
     * Initialize variants
     */
    private void initialize() {
        mFontCache = new FontCache();
        mPaint = new Paint();
        mPaintOther = new Paint();
        mPaintGraph = new Paint();
        mMatrix = new Matrix();
        mSearcher = new EditorSearcher(this);
        setCursorBlinkPeriod(DEFAULT_CURSOR_BLINK_PERIOD);
        mAnchorInfoBuilder = new CursorAnchorInfo.Builder();
        mPaint.setAntiAlias(true);
        mPaintOther.setAntiAlias(true);
        mPaintGraph.setAntiAlias(true);
        mPaintOther.setTypeface(Typeface.MONOSPACE);
        mBuffer = new char[256];
        mBuffer2 = new char[16];
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
		mRect2 = new RectF();
        mInsertHandle = new RectF();
        mLeftHandle = new RectF();
        mRightHandle = new RectF();
        mVerticalScrollBar = new RectF();
        mHorizontalScrollBar = new RectF();
        mDividerMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, Resources.getSystem().getDisplayMetrics());
        mDividerWidth = mDividerMargin;
        mInsertSelWidth = mDividerWidth / 2;
        mDpUnit = mDividerWidth / 2;
        mDividerMargin = mDpUnit * 5;
        mLineNumberAlign = Paint.Align.LEFT;
        mDrag = false;
        mWait = false;
        mBlockLineEnabled = true;
        mBlockLineWidth = mDpUnit / 1.5f;
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mClipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        setUndoEnabled(true);
        mCursorPosition = -1;
        setScalable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        mConnection = new EditorInputConnection(this);
        mCompletionWindow = new EditorAutoCompleteWindow(this);
        mDiagnosticWindow = new DiagnosticWindow(this);
        mVerticalEdgeGlow = new MaterialEdgeEffect();
        mHorizontalGlow = new MaterialEdgeEffect();
        mOverrideSymbolPairs = new SymbolPairMatch();
        setEditorLanguage(null);
        setText(null);
        setTextActionMode(TextActionMode.POPUP_WINDOW_2);
        setTabWidth(4);
        setHighlightCurrentLine(true);
        setAutoIndentEnabled(true);
        setAutoCompletionEnabled(true);
        setVerticalScrollBarEnabled(true);
        setHighlightCurrentBlock(true);
        setHighlightSelectedText(true);
        setDisplayLnPanel(true);
        setOverScrollEnabled(true);
        setHorizontalScrollBarEnabled(true);
        setSymbolCompletionEnabled(true);
        setEditable(true);
        setLineNumberEnabled(true);
		setLineColorsEnabled(true);
        setAutoCompletionOnComposing(true);
        setTypefaceText(TypefaceUtils.jetbrainsMono());
        setTypefaceLineNumber(TypefaceUtils.jetbrainsMono());
        setPinLineNumber(true);
        setLineNumberAlign(Paint.Align.RIGHT);
        setColorScheme(new SchemeAndroidIDE());
        // Issue #41 View being highlighted when focused on Android 11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setDefaultFocusHighlightEnabled(false);
        }
		
		mLineColors = new HashMap<HexColor, Integer>();
		mErrorColor = Color.parseColor("#FF5252");
		mWarningColor = Color.parseColor("#FFD740");
		mRect2.set(-1, -1, -1, -1);
        
        mDiagnostics = new HashMap<>();
    }
    
    /**
     * Set whether line number region will scroll together with code region
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
    
    public Diagnostic findDiagnosticInCursorRange() {
        if(mDiagnostics == null)
            return null;
        int leftLine = getCursor().getLeftLine();
        int leftCol = getCursor().getLeftColumn();
        int rightLine = getCursor().getRightLine();
        int rightCol = getCursor().getRightColumn();
        final org.eclipse.lsp4j. Range range = new org.eclipse.lsp4j. Range(new Position(leftLine, leftCol), new Position(rightLine, rightCol));
        final Set<Range> keys = mDiagnostics.keySet();
        Range found = null;
        for(Range r : keys) {
            if(r.getStart().getLine() <= range.getStart().getLine()
               && r.getStart().getCharacter() <= range.getStart().getCharacter()
               && r.getEnd().getLine() >= range.getEnd().getLine()
               && r.getEnd().getCharacter() >= range.getEnd().getCharacter()) {
                found = r;
                break;
            }
        }
        
        if(found != null) {
            return mDiagnostics.get(found);
        }
        
        return null;
    }
    
    public List<Diagnostic> findDiagnosticsContainingLine(int line) {
        final List<Diagnostic> diags = new ArrayList<>();
        if(mDiagnostics == null || mDiagnostics.size() <= 0) return diags;
        
        final Set<Range> keys = mDiagnostics.keySet();
        
        for(Range r : keys) {
            if(r == null) continue;
            if(r.getStart().getLine() <= line && line <= r.getEnd().getLine())
                diags.add(mDiagnostics.get(r));
        }
        
        return diags;
    }
    
    public void showOrHideDiagnosticWindow() {
        if(mDiagnostics == null) return;
        boolean hide = true;
        Diagnostic d = findDiagnosticInCursorRange();
        if(d != null) {
            hide = false;
            mDiagnosticWindow.setDiagnostic(d);
            if(!mDiagnosticWindow.isShowing())
                mDiagnosticWindow.show();
            else updateDiagnosticWindowPosition();
        }
        if(hide && mDiagnosticWindow.isShowing()) {
            mDiagnosticWindow.hide();
        }
    }
    
    public void setDiagnostics(Map<Range, Diagnostic> diags) {
        if(diags == null)
            diags = new HashMap<>();
        this.mDiagnostics = diags;
    }
    
    public void setStringMap(Map<Integer, List<Range>> map) {
        this.stringMap = map;
    }
	
	public CodeEditor setLineColors(HashMap<HexColor, Integer> map) {
		this.mLineColors = map;
		return this;
	}
	
	public CodeEditor putLineColor(HexColor index, int color) {
		createMapIfNull();
		mLineColors.put(index, color);
		return this;
	}
    
    public void notifySpansChanged() {
        if(mSpanner != null) {
            mSpanner.analyze(getText());
        }
    }

    /**
     * Define symbol pairs for any language,
     * Override language settings.
     * <p>
     * Use {@link SymbolPairMatch.Replacement#NO_REPLACEMENT} to force no completion for a character
     */
    @NonNull
    public SymbolPairMatch getOverrideSymbolPairs() {
        return mOverrideSymbolPairs;
    }

    /**
     * @see CodeEditor#setSymbolCompletionEnabled(boolean)
     */
    public boolean isSymbolCompletionEnabled() {
        return mSymbolCompletionEnabled;
    }

    /**
     * Control whether auto-completes for symbol pairs
     */
    public void setSymbolCompletionEnabled(boolean symbolCompletionEnabled) {
        mSymbolCompletionEnabled = symbolCompletionEnabled;
    }

    /**
     * Create a channel to insert symbols
     */
    public SymbolChannel createNewSymbolChannel() {
        return new SymbolChannel(this);
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
     * Whether the editor should use a different color to draw
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
     * Set the editor's language.
     * A language is a for auto completion,highlight and auto indent analysis.
     *
     * @param lang New EditorLanguage for editor
     */
    public void setEditorLanguage(@Nullable EditorLanguage lang) {
        if (lang == null) {
            lang = new EmptyLanguage();
        }
        this.mLanguage = lang;
        this.mLanguageServer = mLanguage.getLanguageServer();
        
        // Update spanner
        if (mSpanner != null) {
            mSpanner.shutdown();
            mSpanner.setCallback(null);
        }
        mSpanner = new TextAnalyzer(mLanguage);
        mSpanner.setCallback(this);
        if (mText != null) {
            mSpanner.analyze(mText);
        }
        if (mCompletionWindow != null) {
            mCompletionWindow.hide();
            mCompletionWindow.setProvider(lang.getAutoCompleteProvider());
        }
        
        hideDiagnosticWindow();

        // Symbol pairs
        if (mLanguageSymbolPairs != null) {
            mLanguageSymbolPairs.setParent(null);
        }
        mLanguageSymbolPairs = mLanguage.getSymbolPairs();
        if (mLanguageSymbolPairs == null) {
            mLanguageSymbolPairs = new SymbolPairMatch();
        }
        mLanguageSymbolPairs.setParent(mOverrideSymbolPairs);

        // Cursor
        if (mCursor != null) {
            mCursor.setLanguage(mLanguage);
        }
        
        setupLanguageServerCapabilities();
        
        invalidate();
    }
    
    public void setSemanticHighlights(SemanticHighlight highlights) {
        mLanguage.getAnalyzer().setSemanticHighlights(highlights);
        
        notifySpansChanged();
    }
    
    public EditorLanguage getEditorLanguage() {
        return mLanguage;
    }
    
    /**
     * Get the {@link org.eclipse.lsp4j.services.LanguageServer current language server} which is set to this editor
     * May return null;
     *
     * @return Current language server or {@code null}.
     */
    public IDELanguageServer getLanguageServer () {
        return mLanguageServer;
    }
    
    /**
     * Sets up this editor according to the capabilities of the current language server
     */
    private void setupLanguageServerCapabilities() {
        if(mLanguage == null || mLanguageServer == null) return;
        
        ServerCapabilities c = LSPProvider.getServerCapabilitiesForLanguage(mLanguage.getLanguageCode());
        if(c == null) {
            mCodeActionsEnabled = mGotoDefinitionEnabled = mFindReferencesEnabled = false;
            return;
        }
        
        CompletionOptions completion =  c.getCompletionProvider();
        setAutoCompletionEnabled(completion != null);
        
        Either<Boolean, CodeActionOptions> codeActionOptions = c.getCodeActionProvider();
        if(codeActionOptions == null) {
            setCodeActionsEnabled(false);
        } else {
            if(codeActionOptions.isLeft()) {
                setCodeActionsEnabled(codeActionOptions.getLeft());
            } else {
                CodeActionOptions options = codeActionOptions.getRight();
                setCodeActionsEnabled(options != null);
            }
        }
        
        Either<Boolean, DefinitionOptions> definitionOptions = c.getDefinitionProvider();
        if(definitionOptions == null) {
            setGotoDefinitionEnabled(false);
        } else {
            if(definitionOptions.isLeft()) {
                setGotoDefinitionEnabled(definitionOptions.getLeft());
            } else {
                DefinitionOptions options = definitionOptions.getRight();
                setGotoDefinitionEnabled(options != null);
            }
        }
        
        Either<Boolean, ReferenceOptions> referenceOptions = c.getReferencesProvider();
        if(referenceOptions == null) {
            setFindReferencesEnabled(false);
        } else {
            if(definitionOptions.isLeft()) {
                setFindReferencesEnabled(referenceOptions.getLeft());
            } else {
                ReferenceOptions options = referenceOptions.getRight();
                setFindReferencesEnabled(options != null);
            }
        }
        
        SignatureHelpOptions signatureOptions = c.getSignatureHelpProvider();
        if(signatureOptions == null) {
            setSignatureHelpEnabled(false);
        } else {
            setSignatureHelpEnabled(true);
            mSignatureHelpTriggerChars = new ArrayList<String>(signatureOptions.getTriggerCharacters());
        }
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
            invalidate();
        }
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
     * Set how will the editor present text actions
     */
    public void setTextActionMode(TextActionMode mode) {
        if (mCursor.isSelected()) {
            setSelection(mCursor.getLeftLine(), mCursor.getLeftColumn());
        }
        if (mode == TextActionMode.ACTION_MODE) {
            mTextActionPresenter = new EditorTextActionModeStarter(this);
        } else if (mode == TextActionMode.POPUP_WINDOW_2) {
            mTextActionPresenter = new TextActionPopupWindow(this);
        } else {
            mTextActionPresenter = new EditorTextActionWindow(this);
        }
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

    /**
     * @return Enabled / disabled
     * @see CodeEditor#setAutoIndentEnabled(boolean)
     */
    public boolean isAutoIndentEnabled() {
        return mAutoIndentEnabled;
    }

    /**
     * Set whether auto indent should be executed when user enters
     * a NEWLINE
     *
     * @param enabled Enabled / disabled
     */
    public void setAutoIndentEnabled(boolean enabled) {
        mAutoIndentEnabled = enabled;
        mCursor.setAutoIndent(enabled);
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
    protected RectF getInsertHandleRect() {
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
    void setTextSizePxDirect(float size) {
        mPaint.setTextSize(size);
        mPaintOther.setTextSize(size);
        mPaintGraph.setTextSize(size * SCALE_MINI_GRAPH);
        mTextMetrics = mPaint.getFontMetricsInt();
        mLineNumberMetrics = mPaintOther.getFontMetricsInt();
        mGraphMetrics = mPaintGraph.getFontMetricsInt();
        mFontCache.clearCache();
    }

    private long startClock;

    private void record() {
        startClock = System.nanoTime();
    }

    private void print() {
        double time = (System.nanoTime() - startClock) / 1e6;
        if (time > 3.0) {
        }
    }
	
	private void createMapIfNull() {
		if(this.mLineColors == null) {
			this.mLineColors = new HashMap<HexColor, Integer>();
		}
	}

    /**
     * Paint the view on given Canvas
     *
     * @param canvas Canvas you want to draw
     */
    private void drawView(Canvas canvas) {
        //record();
        //counter = 0;
        mSpanner.notifyRecycle();
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

        if (!mCursor.isSelected()) {
            mInsertHandle.setEmpty();
        }
        if (!mTextActionPresenter.shouldShowCursor()) {
            mLeftHandle.setEmpty();
            mRightHandle.setEmpty();
        }
        
        boolean lineNumberNotPinned = isLineNumberEnabled() && (isWordwrap() || !isLineNumberPinned());
        
        LongArrayList postDrawLineNumbers = mPostDrawLineNumbers;
        postDrawLineNumbers.clear();
        LongArrayList postDrawCurrentLines = new LongArrayList();
        List<CursorPaintAction> postDrawCursor = new ArrayList<>();
		MutableInt firstLn = isFirstLineNumberAlwaysVisible() && isWordwrap() ? new MutableInt(-1) : null;
        
        drawRows(canvas, textOffset, postDrawLineNumbers, postDrawCursor, postDrawCurrentLines, firstLn);
        
        offsetX = -getOffsetX();
		
        if (isLineNumberEnabled() && lineNumberNotPinned) {
            drawLineNumberBackground(canvas, offsetX, lineNumberWidth + mDividerMargin, color.getColor(EditorColorScheme.LINE_NUMBER_BACKGROUND));
            drawDivider(canvas, offsetX + lineNumberWidth + mDividerMargin, color.getColor(EditorColorScheme.LINE_DIVIDER));
            int lineNumberColor = mColors.getColor(EditorColorScheme.LINE_NUMBER);
            int currentLineBgColor = mColors.getColor(EditorColorScheme.CURRENT_LINE);
            for (int i = 0;i < postDrawCurrentLines.size(); i++) {
                drawRowBackground(canvas, currentLineBgColor, (int)postDrawCurrentLines.get(i), (int)(textOffset - mDividerMargin));
            }
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

        for (CursorPaintAction action : postDrawCursor) {
            action.exec(canvas, this);
        }
        
        if (!lineNumberNotPinned) {
            drawLineNumberBackground(canvas, 0, lineNumberWidth + mDividerMargin, color.getColor(EditorColorScheme.LINE_NUMBER_BACKGROUND));
            drawDivider(canvas,  lineNumberWidth + mDividerMargin, color.getColor(EditorColorScheme.LINE_DIVIDER));
            int lineNumberColor = mColors.getColor(EditorColorScheme.LINE_NUMBER);
            int currentLineBgColor = mColors.getColor(EditorColorScheme.CURRENT_LINE);
            for (int i = 0;i < postDrawCurrentLines.size(); i++) {
                drawRowBackground(canvas, currentLineBgColor, (int)postDrawCurrentLines.get(i), (int)(textOffset - mDividerMargin));
            }
            for (int i = 0; i < postDrawLineNumbers.size(); i++) {
                long packed = postDrawLineNumbers.get(i);
                drawLineNumber(canvas, IntPair.getFirst(packed), IntPair.getSecond(packed), 0, lineNumberWidth, lineNumberColor);
            }
        }

        drawScrollBars(canvas);
        drawEdgeEffect(canvas);
        //print();
        //Log.d(LOG_TAG, "drawText() calls count = " + counter);
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
    private boolean shouldInitializeNonPrintable() {
        return clearFlag(mNonPrintableOptions, FLAG_DRAW_WHITESPACE_FOR_EMPTY_LINE) != 0;
    }

    /**
     * Draw rows with a {@link RowIterator}
     *
     * @param canvas              Canvas to draw
     * @param offset              Offset of text region start
     * @param postDrawLineNumbers Line numbers to be drawn later
     * @param postDrawCursor      Cursors to be drawn later
     */
    private void drawRows(Canvas canvas, float offset, LongArrayList postDrawLineNumbers, List<CursorPaintAction> postDrawCursor, LongArrayList postDrawCurrentLines, MutableInt requiredFirstLn) {
        RowIterator rowIterator = mLayout.obtainRowIterator(getFirstVisibleRow());
        List<Span> temporaryEmptySpans = null;
        List<List<Span>> spanMap = mSpanner.getResult().getSpanMap();
        List<Integer> matchedPositions = new ArrayList<>();
        int currentLine = mCursor.isSelected() ? -1 : mCursor.getLeftLine();
        int currentLineBgColor = mColors.getColor(EditorColorScheme.CURRENT_LINE);
        int lastPreparedLine = -1;
        int spanOffset = 0;
        int leadingWhitespaceEnd = 0;
        int trailingWhitespaceStart = 0;
        float circleRadius = 0f;
        if (shouldInitializeNonPrintable()) {
            float spaceWidth = mFontCache.measureChar(' ', mPaint);
            float maxD = Math.min(getRowHeight(), spaceWidth);
            maxD *= 0.18f;
            circleRadius = maxD / 2;
        }

        for (int row = getFirstVisibleRow(); row <= getLastVisibleRow() && rowIterator.hasNext(); row++) {
            Row rowInf = rowIterator.next();
            int line = rowInf.lineIndex;
            ContentLine contentLine = mText.getLine(line);
            int columnCount = contentLine.length();
            if (row == getFirstVisibleRow() && requiredFirstLn != null) {
                requiredFirstLn.value = line;
            }else if (rowInf.isLeadingRow) {
                postDrawLineNumbers.add(IntPair.pack(line, row));
            }

            // Prepare data
            if (lastPreparedLine != line) {
                lastPreparedLine = line;
                prepareLine(line);
                computeMatchedPositions(line, matchedPositions);
                spanOffset = 0;
                if (shouldInitializeNonPrintable()) {
                    long positions = findLeadingAndTrailingWhitespacePos(line);
                    leadingWhitespaceEnd = IntPair.getFirst(positions);
                    trailingWhitespaceStart = IntPair.getSecond(positions);
                }
            }
			
            // Get visible region on line
            float[] charPos = findFirstVisibleChar(offset, rowInf.startColumn, rowInf.endColumn, mBuffer);
            int firstVisibleChar = (int) charPos[0];
            int lastVisibleChar = firstVisibleChar;
            float paintingOffset = charPos[1];
            float temporaryOffset = paintingOffset;
            while (temporaryOffset < getWidth() && lastVisibleChar < columnCount) {
                char ch = mBuffer[lastVisibleChar];
                if (isEmoji(ch) && lastVisibleChar + 1 < columnCount) {
                    temporaryOffset += mFontCache.measureText(mBuffer, lastVisibleChar, lastVisibleChar + 2, mPaint);
                    lastVisibleChar++;
                } else {
                    temporaryOffset += mFontCache.measureChar(mBuffer[lastVisibleChar], mPaint);
                }
                lastVisibleChar++;
            }
            lastVisibleChar = Math.min(lastVisibleChar, rowInf.endColumn);

            // Draw matched text background
            if (!matchedPositions.isEmpty()) {
                for (int position : matchedPositions) {
                    drawRowRegionBackground(canvas, paintingOffset, row, firstVisibleChar, lastVisibleChar, position, position + mSearcher.mSearchText.length(), mColors.getColor(EditorColorScheme.MATCHED_TEXT_BACKGROUND));
                }
            }

            float backupOffset = paintingOffset;

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
                drawRowRegionBackground(canvas, paintingOffset, row, firstVisibleChar, lastVisibleChar, selectionStart, selectionEnd, mColors.getColor(EditorColorScheme.SELECTED_TEXT_BACKGROUND));
            }

            // Draw current line background
            if (line == currentLine) {
                drawRowBackground(canvas, currentLineBgColor, row);
                postDrawCurrentLines.add(row);
            }

            // Draw text here
            {
                // Get spans
                List<Span> spans = null;
                if (line < spanMap.size() && line >= 0) {
                    spans = spanMap.get(line);
                }
                if (spans == null || spans.size() == 0) {
                    if (temporaryEmptySpans == null) {
                        temporaryEmptySpans = new LinkedList<>();
                        temporaryEmptySpans.add(Span.obtain(0, EditorColorScheme.TEXT_NORMAL));
                    }
                    spans = temporaryEmptySpans;
                }
                // Seek for first span
                while (spanOffset + 1 < spans.size()) {
                    if (spans.get(spanOffset + 1).column <= firstVisibleChar) {
                        spanOffset++;
                    } else {
                        break;
                    }
                }
                Span span = spans.get(spanOffset);
                // Draw by spans
                while (lastVisibleChar > span.column) {
                    int spanEnd = spanOffset + 1 >= spans.size() ? columnCount : spans.get(spanOffset + 1).column;
                    int paintStart = Math.max(firstVisibleChar, span.column);
                    int paintEnd = Math.min(lastVisibleChar, spanEnd);
                    float width = measureText(mBuffer, paintStart, paintEnd - paintStart);

                    // Draw text
                    drawRegionText(canvas, paintingOffset, getRowBaseline(row) - getOffsetY(), line, paintStart, paintEnd, columnCount, mColors.getColor(span.colorId));

                    // Draw underline
                    if (span.underlineColor != 0) {
                        mRect.bottom = getRowBottom(line) - getOffsetY() - mDpUnit * 1;
                        mRect.top = mRect.bottom - getRowHeight() * 0.1f;
                        mRect.left = paintingOffset;
                        mRect.right = paintingOffset + width;
                        drawColor(canvas, span.underlineColor, mRect);
                    }

                    paintingOffset += width;

                    if (paintEnd == lastVisibleChar) {
                        break;
                    }
                    spanOffset++;
                    if (spanOffset < spans.size()) {
                        span = spans.get(spanOffset);
                    } else {
                        spanOffset--;
                    }
                }
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
                    drawWhitespaces(canvas, paintingOffset, row, firstVisibleChar, lastVisibleChar, 0, leadingWhitespaceEnd, circleRadius);
                }
                if ((mNonPrintableOptions & FLAG_DRAW_WHITESPACE_INNER) != 0) {
                    drawWhitespaces(canvas, paintingOffset, row, firstVisibleChar, lastVisibleChar, leadingWhitespaceEnd, trailingWhitespaceStart, circleRadius);
                }
                if ((mNonPrintableOptions & FLAG_DRAW_WHITESPACE_TRAILING) != 0) {
                    drawWhitespaces(canvas, paintingOffset, row, firstVisibleChar, lastVisibleChar, trailingWhitespaceStart, columnCount, circleRadius);
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
                    mRect.left = paintingOffset + measureText(mBuffer, firstVisibleChar, paintStart - firstVisibleChar);
                    mRect.right = mRect.left + measureText(mBuffer, paintStart, paintEnd - paintStart);
                    drawColor(canvas, mColors.getColor(EditorColorScheme.UNDERLINE), mRect);
                }
            }
			
			// Draw hex colors
			if(isLineColorsEnabled() && mLineColors != null && mLineColors.size() > 0) {
				for(HexColor c : mLineColors.keySet()) {
					if(c != null && c.isSameLine(line) && mLineColors.get(c) != null) {
						final int paintStart = Math.max(firstVisibleChar, c.start.column);
						final int paintEnd = Math.min(lastVisibleChar, c.end.column);
						final float width = measureText(mBuffer, paintStart, paintEnd - paintStart);
						final Integer i = mLineColors.get(c);
						if(i == null) continue;
						final int col = i.intValue();
						mRect.bottom = getRowBottom(line) - getOffsetY() - mDpUnit * 1;
                        mRect.top = mRect.bottom - getRowHeight() * 0.2f;
                        mRect.left = paintingOffset + measureText(mBuffer, firstVisibleChar, paintStart - firstVisibleChar);
                        mRect.right = mRect.left + width;
                        drawColor(canvas, col, mRect);
					}
				}
			}
            
            // Draw diagnostics
            final List<Diagnostic> diags = findDiagnosticsContainingLine(line);
            if(diags.size() > 0) {
                for(int i=0;i<diags.size();i++) {
                    Diagnostic d = diags.get(i);
                    if(d == null) continue;
                    int startCol = d.getRange().getStart().getCharacter();
                    int endCol = d.getRange().getEnd().getCharacter();
                    if(line > d.getRange().getStart().getCharacter()) startCol = 0;
                    if(line > d.getRange().getEnd().getCharacter()) endCol = 0;
                    final int paintStart = Math.max(firstVisibleChar, startCol);
                    final int paintEnd = Math.min(lastVisibleChar, endCol);
                    final float width = measureText(mBuffer, paintStart, paintEnd - paintStart);
                    final RectF r = new RectF();
                    r.bottom = getRowBottom(line) - getOffsetY() - mDpUnit * 1;
                    r.top = r.bottom - getRowHeight() * 0.2f;
                    r.left = paintingOffset + measureText(mBuffer, firstVisibleChar, paintStart - firstVisibleChar);
                    r.right = r.left + width;
                    mPaint.setColor(diagnosticColor(d.getSeverity()));
                    drawZigzagUnderline(r, canvas, mPaint);
                }
            }
            
            // Draw cursors
            if (mCursor.isSelected()) {
                if (mTextActionPresenter.shouldShowCursor()) {
                    if (mCursor.getLeftLine() == line && isInside(mCursor.getLeftColumn(), firstVisibleChar, lastVisibleChar, line)) {
                        float centerX = paintingOffset + measureText(mBuffer, firstVisibleChar, mCursor.getLeftColumn() - firstVisibleChar);
                        postDrawCursor.add(new CursorPaintAction(row, centerX, mLeftHandle, false, EditorTouchEventHandler.SelectionHandle.LEFT));
                    }
                    if (mCursor.getRightLine() == line && isInside(mCursor.getRightColumn(), firstVisibleChar, lastVisibleChar, line)) {
                        float centerX = paintingOffset + measureText(mBuffer, firstVisibleChar, mCursor.getRightColumn() - firstVisibleChar);
                        postDrawCursor.add(new CursorPaintAction(row, centerX, mRightHandle, false, EditorTouchEventHandler.SelectionHandle.RIGHT));
                    }
                }
            } else if (mCursor.getLeftLine() == line && isInside(mCursor.getLeftColumn(), firstVisibleChar, lastVisibleChar, line)) {
                float centerX = paintingOffset + measureText(mBuffer, firstVisibleChar, mCursor.getLeftColumn() - firstVisibleChar);
                postDrawCursor.add(new CursorPaintAction(row, centerX, mEventHandler.shouldDrawInsertHandle() ? mInsertHandle : null, true));
            }

        }
    }
    
    private void drawRowBackground(Canvas canvas, int color, int row, int right) {
        mRect.top = getRowTop(row) - getOffsetY();
        mRect.bottom = getRowBottom(row) - getOffsetY();
        mRect.left = 0;
        mRect.right = right;
        drawColor(canvas, color, mRect);
    }
    
    private void drawZigzagUnderline(RectF boundaries, Canvas canvas, Paint paint) {
        Path path = new Path();
        path.moveTo(boundaries.right, boundaries.bottom);
        
        float left = boundaries.left;
        float right = boundaries.right;
        float y = boundaries.bottom;
        float h = boundaries.height();
        float seed = 2 * h;
        float width = right - left;
        int count  = (int) (width / seed);
        float diff = width - seed * count;
        float sideDiff = diff / 2;
        float halfSeed = seed / 2;
        float innerHeight = y - h;
        for (int i=count;i>=1;i--) {
            float startSeed = i * seed + sideDiff + left;
            float endSeed = startSeed - seed;
            if (i == 1) {
                endSeed -= sideDiff;
            }
            path.lineTo(startSeed - halfSeed, innerHeight);
            path.lineTo(endSeed, y);
        }
        
        canvas.drawPath(path, paint);
    }
	
	/**
     * Draw code block lines on screen
     *
     * @param canvas  The canvas to draw
     * @param offsetX The start x offset for text
     */
    private void drawBlockLines(Canvas canvas, float offsetX) {
        List<BlockLine> blocks = mSpanner == null ? null : mSpanner.getResult().getBlocks();
        if (blocks == null || blocks.isEmpty()) {
            return;
        }
        int first = getFirstVisibleRow();
        int last = getLastVisibleRow();
        boolean mark = false;
        int invalidCount = 0;
        int maxCount = Integer.MAX_VALUE;
        if (mSpanner != null) {
            TextAnalyzeResult colors = mSpanner.getResult();
            if (colors != null) {
                maxCount = colors.getSuppressSwitch();
            }
        }
        int mm = binarySearchEndBlock(first, blocks);
        int cursorIdx = mCursorPosition;
        for (int curr = mm; curr < blocks.size(); curr++) {
            BlockLine block = blocks.get(curr);
            if (hasVisibleRegion(block.startLine, block.endLine, first, last)) {
                try {
                    CharSequence lineContent = mText.getLine(block.endLine);
                    float offset1 = measureText(lineContent, 0, block.endColumn);
                    lineContent = mText.getLine(block.startLine);
                    float offset2 = measureText(lineContent, 0, block.startColumn);
                    float offset = Math.min(offset1, offset2);
                    float centerX = offset + offsetX;
                    mRect.top = Math.max(0, getRowBottom(block.startLine) - getOffsetY());
                    mRect.bottom = Math.min(getHeight(), getRowTop(block.endLine) - getOffsetY());
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
    
    private int diagnosticColor(DiagnosticSeverity severity) {
        int id = EditorColorScheme.DIAGNOSTIC_HINT;
        switch(severity) {
            case Error : id = EditorColorScheme.DIAGNOSTIC_ERROR; break;
            case Warning : id = EditorColorScheme.DIAGNOSTIC_WARNING; break;
            case Information : id = EditorColorScheme.DIAGNOSTIC_INFO; break;
            default : id = EditorColorScheme.DIAGNOSTIC_ERROR; break;
        }
        return mColors.getColor(id);
    }

    protected void showTextActionPopup() {
        if (mTextActionPresenter instanceof TextActionPopupWindow) {
            TextActionPopupWindow window = (TextActionPopupWindow) mTextActionPresenter;
            if (window.isShowing()) {
                //window.hide(TextActionPopupWindow.DISMISS);
            } else {
                window.onBeginTextSelect();
                window.onTextSelectionEnd();
            }
        }
    }

    /**
     * Draw small characters as graph
     */
    private void drawMiniGraph(Canvas canvas, float offset, int row, String graph) {
        // Draw
        mPaintGraph.setColor(mColors.getColor(EditorColorScheme.NON_PRINTABLE_CHAR));
        float baseline = getRowBottom(row) - getOffsetY() - mGraphMetrics.descent;
        canvas.drawText(graph, 0, graph.length(), offset, baseline, mPaintGraph);
    }

    /**
     * Draw non-printable characters
     */
    private void drawWhitespaces(Canvas canvas, float offset, int row, int rowStart, int rowEnd, int min, int max, float circleRadius) {
        int paintStart = Math.max(rowStart, Math.min(rowEnd, min));
        int paintEnd = Math.max(rowStart, Math.min(rowEnd, max));
        mPaintOther.setColor(mColors.getColor(EditorColorScheme.NON_PRINTABLE_CHAR));

        if (paintStart < paintEnd) {
            float spaceWidth = mFontCache.measureChar(' ', mPaint);
            float rowCenter = (getRowTop(row) + getRowBottom(row)) / 2f - getOffsetY();
            offset += measureText(mBuffer, rowStart, paintStart - rowStart);
            while (paintStart < paintEnd) {
                char ch = mBuffer[paintStart];
                float charWidth = measureText(mBuffer, paintStart, 1);
                int paintCount = 0;
                if (ch == ' ') {
                    paintCount = 1;
                } else if (ch == '\t') {
                    paintCount = getTabWidth();
                }
                for (int i = 0; i < paintCount; i++) {
                    float charStartOffset = offset + spaceWidth * i;
                    float charEndOffset = charStartOffset + spaceWidth;
                    float centerOffset = (charStartOffset + charEndOffset) / 2f;
                    canvas.drawCircle(centerOffset, rowCenter, circleRadius, mPaintOther);
                }
                offset += charWidth;
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
    private long findLeadingAndTrailingWhitespacePos(int line) {
        int column = mText.getColumnCount(line);
        int leading = 0;
        int trailing = column;
        while (leading < column && isWhitespace(mBuffer[leading])) {
            leading++;
        }
        // Only them this action is needed
        if (leading != column && (mNonPrintableOptions & (FLAG_DRAW_WHITESPACE_INNER | FLAG_DRAW_WHITESPACE_TRAILING)) != 0) {
            while (trailing > 0 && isWhitespace(mBuffer[trailing - 1])) {
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
     * Get matched text regions on line
     *
     * @param line      Target line
     * @param positions Outputs start positions
     */
    private void computeMatchedPositions(int line, List<Integer> positions) {
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
     * @return Whether cursor should be drawn in this row
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
    private void drawRowRegionBackground(Canvas canvas, float paintingOffset, int row, int firstVis, int lastVis, int highlightStart, int highlightEnd, int color) {
        int paintStart = Math.min(Math.max(firstVis, highlightStart), lastVis);
        int paintEnd = Math.min(Math.max(firstVis, highlightEnd), lastVis);
        if (paintStart != paintEnd) {
            mRect.top = getRowTop(row) - getOffsetY();
            mRect.bottom = getRowBottom(row) - getOffsetY();
            mRect.left = paintingOffset + measureText(mBuffer, firstVis, paintStart - firstVis);
            mRect.right = mRect.left + measureText(mBuffer, paintStart, paintEnd - paintStart);
            drawColor(canvas, color, mRect);
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
     * @param endIndex    End index to paint
     * @param columnCount Column count of line
     * @param color       Color of normal text in this region
     */
    private void drawRegionText(Canvas canvas, float offsetX, float baseline, int line, int startIndex, int endIndex, int columnCount, int color) {
        boolean hasSelectionOnLine = mCursor.isSelected() && line >= mCursor.getLeftLine() && line <= mCursor.getRightLine();
        int selectionStart = 0;
        int selectionEnd = columnCount;
        if (line == mCursor.getLeftLine()) {
            selectionStart = mCursor.getLeftColumn();
        }
        if (line == mCursor.getRightLine()) {
            selectionEnd = mCursor.getRightColumn();
        }
        mPaint.setColor(color);
        if (hasSelectionOnLine) {
            if (endIndex <= selectionStart || startIndex >= selectionEnd) {
                drawText(canvas, mBuffer, startIndex, endIndex - startIndex, offsetX, baseline);
            } else {
                if (startIndex <= selectionStart) {
                    if (endIndex >= selectionEnd) {
                        //Three regions
                        //startIndex - selectionStart
                        drawText(canvas, mBuffer, startIndex, selectionStart - startIndex, offsetX, baseline);
                        float deltaX = measureText(mBuffer, startIndex, selectionStart - startIndex);
                        //selectionStart - selectionEnd
                        mPaint.setColor(mColors.getColor(EditorColorScheme.TEXT_SELECTED));
                        drawText(canvas, mBuffer, selectionStart, selectionEnd - selectionStart, offsetX + deltaX, baseline);
                        deltaX += measureText(mBuffer, selectionStart, selectionEnd - selectionStart);
                        //selectionEnd - endIndex
                        mPaint.setColor(color);
                        drawText(canvas, mBuffer, selectionEnd, endIndex - selectionEnd, offsetX + deltaX, baseline);
                    } else {
                        //Two regions
                        //startIndex - selectionStart
                        drawText(canvas, mBuffer, startIndex, selectionStart - startIndex, offsetX, baseline);
                        //selectionStart - endIndex
                        mPaint.setColor(mColors.getColor(EditorColorScheme.TEXT_SELECTED));
                        drawText(canvas, mBuffer, selectionStart, endIndex - selectionStart, offsetX + measureText(mBuffer, startIndex, selectionStart - startIndex), baseline);
                    }
                } else {
                    //selectionEnd > startIndex > selectionStart
                    if (endIndex > selectionEnd) {
                        //Two regions
                        //selectionEnd - endIndex
                        drawText(canvas, mBuffer, selectionEnd, endIndex - selectionEnd, offsetX + measureText(mBuffer, startIndex, selectionEnd - startIndex), baseline);
                        //startIndex - selectionEnd
                        mPaint.setColor(mColors.getColor(EditorColorScheme.TEXT_SELECTED));
                        drawText(canvas, mBuffer, startIndex, selectionEnd - startIndex, offsetX, baseline);
                    } else {
                        //One region
                        mPaint.setColor(mColors.getColor(EditorColorScheme.TEXT_SELECTED));
                        drawText(canvas, mBuffer, startIndex, endIndex - startIndex, offsetX, baseline);
                    }
                }
            }
        } else {
            drawText(canvas, mBuffer, startIndex, endIndex - startIndex, offsetX, baseline);
        }
    }

    /**
     * Draw effect of edges
     *
     * @param canvas The canvas to draw
     */
    private void drawEdgeEffect(Canvas canvas) {
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
            if (mVerticalEdgeGlow.isFinished() && (scroller.getCurrY() < 0 || scroller.getCurrY() >= getScrollMaxY())) {
                mEventHandler.topOrBottom = scroller.getCurrY() >= getScrollMaxY();
                mVerticalEdgeGlow.onAbsorb((int) scroller.getCurrVelocity());
                postDraw = true;
            }
            if (mHorizontalGlow.isFinished() && (scroller.getCurrX() < 0 || scroller.getCurrX() >= getScrollMaxX())) {
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
     * Draw a handle.
     * The handle can be insert handle,left handle or right handle
     *
     * @param canvas     The Canvas to draw handle
     * @param row        The row you want to attach handle to its bottom (Usually the selection line)
     * @param centerX    Center x offset of handle
     * @param resultRect The rect of handle this method drew
     * @param handleType The selection handle type (LEFT, RIGHT,BOTH or -1)
     */
    private void drawHandle(Canvas canvas, int row, float centerX, RectF resultRect, int handleType) {
        float radius = mDpUnit * 12;

        if (handleType > -1 && handleType == mEventHandler.getTouchedHandleType()) {
            radius = mDpUnit * 16;
        }

        //Log.d("drawHandle", "drawHandle: " + mEventHandler.getTouchedHandleType());
        float top = getRowBottom(row) - getOffsetY();
        float bottom = top + radius * 2;
        float left = centerX - radius;
        float right = centerX + radius;
        if (right < 0 || left > getWidth() || bottom < 0 || top > getHeight()) {
            resultRect.setEmpty();
            return;
        }
        resultRect.left = left;
        resultRect.right = right;
        resultRect.top = top;
        resultRect.bottom = bottom;
        mPaint.setColor(mColors.getColor(EditorColorScheme.SELECTION_HANDLE));
        canvas.drawCircle(centerX, (top + bottom) / 2, radius, mPaint);
    }

    /**
     * Draw scroll bars and tracks
     *
     * @param canvas The canvas to draw
     */
    private void drawScrollBars(Canvas canvas) {
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
    private void drawScrollBarTrackVertical(Canvas canvas) {
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
    private void drawScrollBarVertical(Canvas canvas) {
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
    private void drawLineInfoPanel(Canvas canvas, float centerY, float rightX) {
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
    private void drawScrollBarTrackHorizontal(Canvas canvas) {
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
    private void drawScrollBarHorizontal(Canvas canvas) {
        int page = getWidth();
        float all = getScrollMaxX() + getWidth();
        float length = page / all * getWidth();
        float leftX = getOffsetX() / all * getWidth();
        mRect.top = getHeight() - mDpUnit * 10;
        mRect.bottom = getHeight();
        mRect.right = leftX + length;
        mRect.left = leftX;
        mHorizontalScrollBar.set(mRect);
        drawColor(canvas, mColors.getColor(mEventHandler.holdHorizontalScrollBar() ? EditorColorScheme.SCROLL_BAR_THUMB_PRESSED : EditorColorScheme.SCROLL_BAR_THUMB), mRect);
    }

    /**
     * Draw cursor
     */
    private void drawCursor(Canvas canvas, float centerX, int row, RectF handle, boolean insert) {
        if (!insert || mCursorBlink == null || mCursorBlink.visibility) {
            mRect.top = getRowTop(row) - getOffsetY();
            mRect.bottom = getRowBottom(row) - getOffsetY();
            mRect.left = centerX - mInsertSelWidth / 2f;
            mRect.right = centerX + mInsertSelWidth / 2f;
            drawColor(canvas, mColors.getColor(EditorColorScheme.SELECTION_INSERT), mRect);
        }
        if (handle != null) {
            drawHandle(canvas, row, centerX, handle, -1);
        }
    }

    /**
     * Draw cursor
     */
    private void drawCursor(Canvas canvas, float centerX, int row, RectF handle, boolean insert, int handleType) {
        if (!insert || mCursorBlink == null || mCursorBlink.visibility) {
            mRect.top = getRowTop(row) - getOffsetY();
            mRect.bottom = getRowBottom(row) - getOffsetY();
            mRect.left = centerX - mInsertSelWidth / 2f;
            mRect.right = centerX + mInsertSelWidth / 2f;
            drawColor(canvas, mColors.getColor(EditorColorScheme.SELECTION_INSERT), mRect);
        }
        if (handle != null) {
            drawHandle(canvas, row, centerX, handle, handleType);
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
        List<BlockLine> blocks = mSpanner == null ? null : mSpanner.getResult().getBlocks();
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
    private int findCursorBlock(List<BlockLine> blocks) {
        int line = mCursor.getLeftLine();
        int min = binarySearchEndBlock(line, blocks);
        int max = blocks.size() - 1;
        int minDis = Integer.MAX_VALUE;
        int found = -1;
        int invalidCount = 0;
        int maxCount = Integer.MAX_VALUE;
        if (mSpanner != null) {
            TextAnalyzeResult result = mSpanner.getResult();
            if (result != null) {
                maxCount = result.getSuppressSwitch();
            }
        }
        for (int i = min; i <= max; i++) {
            BlockLine block = blocks.get(i);
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
     * @return The block we found.Always a valid index(Unless there is no block)
     */
    private int binarySearchEndBlock(int firstVis, List<BlockLine> blocks) {
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
     * @param src   Source characters array
     * @param index Start index in array
     * @param count Count of characters
     * @return The width measured
     */
    private float measureText(char[] src, int index, int count) {
        int tabCount = 0;
        for (int i = 0; i < count; i++) {
            if (src[index + i] == '\t') {
                tabCount++;
            }
        }
        float extraWidth = mFontCache.measureChar(' ', mPaint) * getTabWidth() - mFontCache.measureChar('\t', mPaint);
        return mFontCache.measureText(src, index, index + count, mPaint) + tabCount * extraWidth;
    }

    /**
     * Measure text width with editor's text paint
     *
     * @param text  Source string
     * @param index Start index in array
     * @param count Count of characters
     * @return The width measured
     */
    private float measureText(CharSequence text, int index, int count) {
        int tabCount = 0;
        for (int i = 0; i < count; i++) {
            if (text.charAt(index + i) == '\t') {
                tabCount++;
            }
        }
        float extraWidth = mFontCache.measureChar(' ', mPaint) * getTabWidth() - mFontCache.measureChar('\t', mPaint);
        return mFontCache.measureText(text, index, index + count, mPaint) + tabCount * extraWidth;
    }

    //private int counter;

    /**
     * Draw text on the given position
     *
     * @param canvas Canvas to draw
     * @param src    Source of characters
     * @param index  The index in array
     * @param count  Count of characters
     * @param offX   Offset x for paint
     * @param offY   Offset y for paint(baseline)
     */
    private void drawText(Canvas canvas, char[] src, int index, int count, float offX, float offY) {
        //counter++;
        int end = index + count;
        int st = index;
        if (mCharPaint) {
            for (int i = index; i < end; i++) {
                if (src[i] == '\t') {
                    canvas.drawText(src, st, i - st, offX, offY, mPaint);
                    offX = offX + measureText(src, st, i - st + 1);
                    st = i + 1;
                } else if (src[i] == '/') {
                    canvas.drawText(src, st, i - st + 1, offX, offY, mPaint);
                    offX = offX + measureText(src, st, i - st + 1);
                    st = i + 1;
                }
            }
        } else {
            for (int i = index; i < end; i++) {
                if (src[i] == '\t') {
                    canvas.drawText(src, st, i - st, offX, offY, mPaint);
                    offX = offX + measureText(src, st, i - st + 1);
                    st = i + 1;
                }
            }
        }
        if (st < end) {
            canvas.drawText(src, st, end - st, offX, offY, mPaint);
        }
    }

    /**
     * Find first visible character
     */
    private float[] findFirstVisibleChar(float initialPosition, int left, int right, char[] chars) {
        float width = 0f;
        float target = mFontCache.measureChar(' ', mPaint) * getTabWidth() * 1.1f;
        while (left < right && initialPosition + width < -target) {
            float single = mFontCache.measureChar(chars[left], mPaint);
            if (chars[left] == '\t') {
                single = mFontCache.measureChar(' ', mPaint) * getTabWidth();
            }
            width += single;
            left++;
        }
        return new float[]{left, initialPosition + width};
    }

    /**
     * Read out characters to mChars for the given line
     *
     * @param line Line going to draw or measure
     */
    private void prepareLine(int line) {
        int length = mText.getColumnCount(line);
        if (length >= mBuffer.length) {
            mBuffer = new char[length + 100];
        }
        mText.getLineChars(line, mBuffer);
    }

    /**
     * Draw background for row
     */
    private void drawRowBackground(Canvas canvas, int color, int row) {
        mRect.top = getRowTop(row) - getOffsetY();
        mRect.bottom = getRowBottom(row) - getOffsetY();
        mRect.left = 0;
        mRect.right = mViewRect.right;
        drawColor(canvas, color, mRect);
    }

    /**
     * Draw single line number
     */
    private void drawLineNumber(Canvas canvas, int line, int row, float offsetX, float width, int color) {
        if (width + offsetX <= 0) {
            return;
        }
        if (mPaintOther.getTextAlign() != mLineNumberAlign) {
            mPaintOther.setTextAlign(mLineNumberAlign);
        }
		
		mPaintOther.setColor(color);
		
        // Line number center align to text center
        float y = (getRowBottom(row) + getRowTop(row)) / 2f - (mLineNumberMetrics.descent - mLineNumberMetrics.ascent) / 2f - mLineNumberMetrics.ascent - getOffsetY();

        // Avoid Integer#toString() calls
        char[] text = mBuffer2;
        int count = 0;
        int copy = line + 1;
        while (copy > 0) {
            int digit = copy % 10;
            text[count++] = (char) ('0' + digit);
            copy /= 10;
        }
        for (int i = 0, j = count - 1; i < j; i++, j--) {
            char tmp = text[i];
            text[i] = text[j];
            text[j] = tmp;
        }
		
        switch (mLineNumberAlign) {
            case LEFT:
                canvas.drawText(text, 0, count, offsetX, y, mPaintOther);
                break;
            case RIGHT:
                canvas.drawText(text, 0, count, offsetX + width, y, mPaintOther);
                break;
            case CENTER:
                canvas.drawText(text, 0, count, offsetX + (width + mDividerMargin) / 2f, y, mPaintOther);
        }
    }

    /**
     * Draw line number background
     *
     * @param canvas  Canvas to draw
     * @param offsetX Start x of line number region
     * @param width   Width of line number region
     * @param color   Color of line number background
     */
    private void drawLineNumberBackground(Canvas canvas, float offsetX, float width, int color) {
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
    private void drawDivider(Canvas canvas, float offsetX, int color) {
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
        drawColor(canvas, color, mRect);
    }

    /**
     * Get the width of line number region
     *
     * @return width of line number region
     */
    private float measureLineNumber() {
        if (!isLineNumberEnabled()) {
            return 0f;
        }
        int count = 0;
        int lineCount = getLineCount();
        while (lineCount > 0) {
            count++;
            lineCount /= 10;
        }
        final String[] charSet = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        float single = 0f;
        for (String ch : charSet) {
            single = Math.max(single, mPaintOther.measureText(ch));
        }
        return single * count;
    }

    /**
     * Create layout for text
     */
    void createLayout() {
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
     * Will not do any thing if color is zero
     *
     * @param canvas Canvas to draw
     * @param color  Color of rect
     * @param rect   Rect to draw
     */
    private void drawColor(Canvas canvas, int color, RectF rect) {
        if (color != 0) {
            mPaint.setColor(color);
            canvas.drawRect(rect, mPaint);
        }
    }

    /**
     * Draw rect on screen
     * Will not do any thing if color is zero
     *
     * @param canvas Canvas to draw
     * @param color  Color of rect
     * @param rect   Rect to draw
     */
    private void drawColor(Canvas canvas, int color, Rect rect) {
        if (color != 0) {
            mPaint.setColor(color);
            canvas.drawRect(rect, mPaint);
        }
    }

    /**
     * Commit a tab to cursor
     */
    public void commitTab() {
        if (mConnection != null && isEditable()) {
            mConnection.commitTextInternal(Symbols.createTabSpaces(), true);
        }
    }


    /**
     * Whether span map is valid
     */
    private boolean isSpanMapPrepared(boolean insert, int delta) {
        List<List<Span>> map = mSpanner.getResult().getSpanMap();
        if (map != null) {
            if (insert) {
                return map.size() == getLineCount() - delta;
            } else {
                return map.size() == getLineCount() + delta;
            }
        } else {
            return false;
        }
    }

    /**
     * Exit select mode after text changed
     */
    private void exitSelectModeIfNeeded() {
        if (!mCursor.isSelected()) {
            mTextActionPresenter.onExit();
        }
    }
    
    private void updateDiagnosticWindowPosition() {
        float panelX = updateCursorAnchor() + mDpUnit * 20;
        float[] rightLayoutOffset = mLayout.getCharLayoutOffset(mCursor.getRightLine(), mCursor.getRightColumn());
        float panelY = rightLayoutOffset[0] - getOffsetY() - getRowHeight();
        float restY = getHeight() - panelY;
        if (restY > mDpUnit * 120) {
            restY = mDpUnit * 120;
        } else if (restY < mDpUnit * 100) {
            float offset = 0;
            while (restY < mDpUnit * 100) {
                restY += getRowHeight();
                panelY -= getRowHeight();
                offset += getRowHeight();
            }
            getScroller().startScroll(getOffsetX(), getOffsetY(), 0, (int) offset, 0);
        }
        mDiagnosticWindow.setExtendedX(panelX);
        mDiagnosticWindow.setExtendedY(panelY);
        if (getWidth() < 500 * mDpUnit) {
            mDiagnosticWindow.setWidth(getWidth() * 7 / 8);
            mDiagnosticWindow.setExtendedX(getWidth() / 8f / 2f);
        } else {
            int w = getWidth() / 3;
            mDiagnosticWindow.setWidth(w * 2);
        }

        mDiagnosticWindow.setHeight((int) restY);
        mDiagnosticWindow.setExtendedY(panelY - restY);
        mDiagnosticWindow.updatePosition();
    }

    /**
     * Apply new position of auto completion window
     */
    private void updateCompletionWindowPosition() {
        float panelX = updateCursorAnchor() + mDpUnit * 20;
        float[] rightLayoutOffset = mLayout.getCharLayoutOffset(mCursor.getRightLine(), mCursor.getRightColumn());
        float panelY = rightLayoutOffset[0] - getOffsetY() + getRowHeight() / 2f;
        float restY = getHeight() - panelY;
        if (restY > mDpUnit * 200) {
            restY = mDpUnit * 200;
        } else if (restY < mDpUnit * 100) {
            float offset = 0;
            while (restY < mDpUnit * 100) {
                restY += getRowHeight();
                panelY -= getRowHeight();
                offset += getRowHeight();
            }
            getScroller().startScroll(getOffsetX(), getOffsetY(), 0, (int) offset, 0);
        }
        mCompletionWindow.setExtendedX(panelX);
        mCompletionWindow.setExtendedY(panelY);
        if (getWidth() < 500 * mDpUnit) {
            //Open center mode
            mCompletionWindow.setWidth(getWidth() * 7 / 8);
            mCompletionWindow.setExtendedX(getWidth() / 8f / 2f);
        } else {
			// Follow cursor mode
			int w = getWidth() / 3;
			mCompletionWindow.setWidth(w * 2);
        }
        if (!mCompletionWindow.isShowing()) {
            mCompletionWindow.setHeight((int) restY);
        }
        mCompletionWindow.setMaxHeight((int) restY);
        mCompletionWindow.updatePosition();
    }

    /**
     * Update the information of cursor
     * Such as the position of cursor on screen(For input method that can go to any position on screen like PC input method)
     *
     * @return The offset x of right cursor on view
     */
    public float updateCursorAnchor() {
        CursorAnchorInfo.Builder builder = mAnchorInfoBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.reset();
            mMatrix.set(getMatrix());
            int[] b = new int[2];
            getLocationOnScreen(b);
            mMatrix.postTranslate(b[0], b[1]);
            builder.setMatrix(mMatrix);
            builder.setSelectionRange(mCursor.getLeft(), mCursor.getRight());
        }
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
     * @see CodeEditor#setAutoCompletionOnComposing(boolean)
     */
    public boolean isAutoCompletionOnComposing() {
        return mCompletionOnComposing;
    }

    /**
     * Specify whether show auto completion window even the input method is in composing state.
     * <p>
     * This is useful when the user uses an input method that does not support the attitude {@link EditorInfo#TYPE_TEXT_FLAG_NO_SUGGESTIONS}
     * <p>
     * Note:
     * Composing texts are marked by an underline. When this switch is set to false, the editor
     * will not provide auto completion when there is any composing text in editor.
     * <p>
     * This is enabled by default now
     */
    public void setAutoCompletionOnComposing(boolean completionOnComposing) {
        mCompletionOnComposing = completionOnComposing;
    }

    /**
     * Specify whether we should provide any auto completion
     * <p>
     * If this is set to false, the completion window will never show and auto completion item
     * provider will never be called.
     */
    public void setAutoCompletionEnabled(boolean autoCompletionEnabled) {
        mAutoCompletionEnabled = autoCompletionEnabled;
        if (!autoCompletionEnabled) {
            mCompletionWindow.hide();
        }
    }
    
    /**
     * Specify whether we should provide any code actions
     * <p>
     */
    public void setCodeActionsEnabled(boolean codeActionsEnabled) {
        mCodeActionsEnabled = codeActionsEnabled;
    }
    
    /**
     * Specify whether we should provide any 'Go to definition' support
     */
    public void setGotoDefinitionEnabled(boolean gotoDefinitionEnabled) {
        mGotoDefinitionEnabled = gotoDefinitionEnabled;
    }
    
    /**
     * Specify whether we should provide any 'Find references' support
     */
    public void setFindReferencesEnabled(boolean findReferencesEnabled) {
        mFindReferencesEnabled = findReferencesEnabled;
    }
    
    public void setSignatureHelpEnabled(boolean enabled) {
        this.mSignatureHelpEnabled = enabled;
    }

    /**
     * @see CodeEditor#setAutoCompletionEnabled(boolean)
     */
    public boolean isAutoCompletionEnabled() {
        return mAutoCompletionEnabled;
    }
    
    public boolean isCodeActionsEnabled() {
        return mCodeActionsEnabled && mLanguageServer != null;
    }
    
    public boolean isGotoDefinitionEnabled() {
        return mGotoDefinitionEnabled && mLanguageServer != null;
    }
    
    public boolean isFindReferencesEnabled() {
        return mFindReferencesEnabled && mLanguageServer != null;
    }
    
    public boolean isSignatureHelpEnabled() {
        return mSignatureHelpEnabled && mLanguageServer != null;
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
     */
    public int getNonPrintableFlags() {
        return mNonPrintableOptions;
    }
	
	public void goToEnd() {
		final int line = getText().getLineCount() - 1;
		setSelection(line, 0, true);
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
        float charWidth = column == 0 ? 0 : measureText(mText.getLine(line), column - 1, 1);
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

        boolean animation = true;
        if (System.currentTimeMillis() - mLastMakeVisible < 100) {
            animation = false;
        }
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
    protected float getDpUnit() {
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
     * Whether the position is over max Y position
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
    
    public float[] getCursorPosition() {
        return mLayout.getCharLayoutOffset(getCursor().getLeftLine(), getCursor().getLeftColumn());
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
     * @see CodeEditor#setHighlightSelectedText(boolean)
     */
    public boolean isHighlightSelectedText() {
        return mHighlightSelectedText;
    }

    /**
     * Use color {@link EditorColorScheme#TEXT_SELECTED} to draw selected text
     * instead of color specified by its span
     */
    public void setHighlightSelectedText(boolean highlightSelected) {
        mHighlightSelectedText = highlightSelected;
        invalidate();
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
        if (mFormatThread != null || (mListener != null && mListener.onRequestFormat(this, true))) {
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
     * @param w tab width compared to space
     */
    public void setTabWidth(int w) {
        if (w < 1) {
            throw new IllegalArgumentException("width can not be under 1");
        }
        mTabWidth = w;
        if (mCursor != null) {
            mCursor.setTabWidth(mTabWidth);
        }
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
    }

    /**
     * Undo last action
     */
    public void undo() {
        mText.undo();
    }

    /**
     * Redo last action
     */
    public void redo() {
        mText.redo();
    }

    /**
     * Whether can undo
     *
     * @return whether can undo
     */
    public boolean canUndo() {
        return mText.canUndo();
    }

    /**
     * Whether can redo
     *
     * @return whether can redo
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
     * @return Whether drag
     * @see CodeEditor#setDrag(boolean)
     */
    public boolean isDrag() {
        return mDrag;
    }

    /**
     * Set whether drag mode
     * drag:no fling scroll
     *
     * @param drag Whether drag
     */
    public void setDrag(boolean drag) {
        mDrag = drag;
        if (drag && !mEventHandler.getScroller().isFinished()) {
            mEventHandler.getScroller().forceFinished(true);
        }
    }

    /**
     * @see CodeEditor#setOverScrollEnabled(boolean)
     */
    public boolean isOverScrollEnabled() {
        return mOverScrollEnabled;
    }

    /**
     * Whether over scroll is permitted.
     * When over scroll is enabled, the user will be able to scroll out of displaying
     * bounds if the user scroll fast enough.
     * This is implemented by {@link OverScroller#fling(int, int, int, int, int, int, int, int, int, int)}
     */
    public void setOverScrollEnabled(boolean overScrollEnabled) {
        mOverScrollEnabled = overScrollEnabled;
    }

    /**
     * Start search action mode
     */
    public void beginSearchMode() {
        class SearchActionMode implements ActionMode.Callback {
            
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mStartedActionMode = ACTION_MODE_SEARCH_TEXT;
                if(menu instanceof MenuBuilder){
                    MenuBuilder builder = (MenuBuilder) menu;
                    builder.setOptionalIconsVisible(true);
                }
                menu.add(0, 1, 0, R.string.last)
                    .setIcon(R.drawable.ic_search_last)
                    .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                menu.add(0, 0, 0, R.string.next)
                    .setIcon(R.drawable.ic_search_next)
                    .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                menu.add(0, 2, 0, R.string.replace)
                    .setIcon(R.drawable.ic_search_replace)
                    .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
                mode.setCustomView(sv);
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
                        final LayoutDialogTextInputBinding binding = LayoutDialogTextInputBinding.inflate(LayoutInflater.from(getContext()));
                        binding.name.setHint(R.string.replacement);
                        binding.name.setStartIconDrawable(R.drawable.ic_replace);
                        binding.name.setCounterEnabled(false);
                        new MaterialAlertDialogBuilder(getContext(), R.style.AppTheme_MaterialAlertDialog)
                                .setTitle(R.string.replace)
                                .setView(binding.getRoot())
                                .setNegativeButton(R.string.cancel, null)
                                .setPositiveButton(R.string.replace, (dialog, which) -> {
                                    getSearcher().replaceThis(binding.name.getEditText().getText().toString());
                                    am.finish();
                                    dialog.dismiss();
                                })
                                .setNeutralButton(R.string.replaceAll, (dialog, which) -> {
                                    getSearcher().replaceAll(binding.name.getEditText().getText().toString());
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
        ((AppCompatActivity) getContext()).startSupportActionMode(callback);
    }

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
        mPaint.setTypeface(typefaceText);
        mFontCache.clearCache();
        if (2 * mPaint.measureText("/") != mPaint.measureText("//")) {
            mCharPaint = true;
        } else {
            mCharPaint = false;
        }
        mTextMetrics = mPaint.getFontMetricsInt();
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
        return mLayout.getLineNumberForRow(getFirstVisibleRow());
    }

    /**
     * Get first visible row on screen
     *
     * @return first visible row
     */
    public int getFirstVisibleRow() {
        return (int) Math.max(0, getOffsetY() / getRowHeight());
    }

    /**
     * Get last visible row on screen
     *
     * @return last visible row
     */
    public int getLastVisibleRow() {
        return (int) Math.max(0, (getOffsetY() + getHeight()) / getRowHeight());
    }

    /**
     * Whether this row is visible on screen
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
     * @return Whether allow to scale
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
        if (mCursor.left().equals(mLockedSelection)) {
            return mCursor.right();
        } else {
            return mCursor.left();
        }
    }

    /**
     * Make sure the moving selection is visible
     */
    private void ensureSelectingTargetVisible() {
        if (mCursor.left().equals(mLockedSelection)) {
            // Ensure right selection vivible
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
        if (mLockedSelection == null) {
            if (mCompletionWindow.isShowing()) {
                mCompletionWindow.moveDown();
                return;
            }
            long pos = mCursor.getDownOf(IntPair.pack(mCursor.getLeftLine(), mCursor.getLeftColumn()));
            setSelection(IntPair.getFirst(pos), IntPair.getSecond(pos));
        } else {
            mCompletionWindow.hide();
            hideDiagnosticWindow();
            hideDiagnosticWindow();
            long pos = mCursor.getDownOf(getSelectingTarget().toIntPair());
            setSelectionRegion(mLockedSelection.line, mLockedSelection.column, IntPair.getFirst(pos), IntPair.getSecond(pos), false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move the selection up
     * If Auto complete panel is shown,move the selection in panel to last
     */
    public void moveSelectionUp() {
        hideDiagnosticWindow();
        if (mLockedSelection == null) {
            if (mCompletionWindow.isShowing()) {
                mCompletionWindow.moveUp();
                return;
            }
            long pos = mCursor.getUpOf(IntPair.pack(mCursor.getLeftLine(), mCursor.getLeftColumn()));
            setSelection(IntPair.getFirst(pos), IntPair.getSecond(pos));
        } else {
            mCompletionWindow.hide();
            long pos = mCursor.getUpOf(getSelectingTarget().toIntPair());
            setSelectionRegion(mLockedSelection.line, mLockedSelection.column, IntPair.getFirst(pos), IntPair.getSecond(pos), false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move the selection left
     */
    public void moveSelectionLeft() {
        hideDiagnosticWindow();
        if (mLockedSelection == null) {
            Cursor c = getCursor();
            int line = c.getLeftLine();
            int column = c.getLeftColumn();
            long pos = mCursor.getLeftOf(IntPair.pack(line, column));
            int lineAfter = IntPair.getFirst(pos);
            int columnAfter = IntPair.getSecond(pos);
            setSelection(lineAfter, columnAfter);
            if (line == lineAfter) {
                int toLeft = column - columnAfter;
                if (mCompletionWindow.isShowing()) {
                    String prefix = mCompletionWindow.getPrefix();
                    if (prefix.length() > toLeft) {
                        prefix = prefix.substring(0, prefix.length() - toLeft);
                        mCompletionWindow.setPrefix(prefix);
                    } else {
                        mCompletionWindow.hide();
                    }
                }
                if (column - 1 <= 0) {
                    mCompletionWindow.hide();
                }
            }
        } else {
            mCompletionWindow.hide();
            long pos = mCursor.getLeftOf(getSelectingTarget().toIntPair());
            setSelectionRegion(mLockedSelection.line, mLockedSelection.column, IntPair.getFirst(pos), IntPair.getSecond(pos), false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move the selection right
     */
    public void moveSelectionRight() {
        hideDiagnosticWindow();
        if (mLockedSelection == null) {
            Cursor c = getCursor();
            int line = c.getLeftLine();
            int column = c.getLeftColumn();
            int c_column = getText().getColumnCount(line);
            long pos = mCursor.getRightOf(IntPair.pack(line, column));
            int lineAfter = IntPair.getFirst(pos);
            int columnAfter = IntPair.getSecond(pos);
            setSelection(lineAfter, columnAfter);
            if (line == lineAfter) {
                char ch = (columnAfter - 1 < c_column && columnAfter - 1 >= 0) ? mText.charAt(lineAfter, columnAfter - 1) : '\0';
                if (!isEmoji(ch) && mCompletionWindow.isShowing()) {
                    if (!mLanguage.isAutoCompleteChar(ch)) {
                        mCompletionWindow.hide();
                    } else {
                        String prefix = mCompletionWindow.getPrefix() + ch;
                        mCompletionWindow.setPrefix(prefix);
                    }
                }
            }
        } else {
            mCompletionWindow.hide(); hideDiagnosticWindow();
            long pos = mCursor.getRightOf(getSelectingTarget().toIntPair());
            setSelectionRegion(mLockedSelection.line, mLockedSelection.column, IntPair.getFirst(pos), IntPair.getSecond(pos), false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move selection to end of line
     */
    public void moveSelectionEnd() {
        if (mLockedSelection == null) {
            int line = mCursor.getLeftLine();
            setSelection(line, getText().getColumnCount(line));
        } else {
            int line = getSelectingTarget().line;
            setSelectionRegion(mLockedSelection.line, mLockedSelection.column, line, getText().getColumnCount(line), false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move selection to start of line
     */
    public void moveSelectionHome() {
        if (mLockedSelection == null) {
            setSelection(mCursor.getLeftLine(), 0);
        } else {
            setSelectionRegion(mLockedSelection.line, mLockedSelection.column, getSelectingTarget().line, 0, false);
            ensureSelectingTargetVisible();
        }
    }

    /**
     * Move selection to given position
     *
     * @param line   The line to move
     * @param column The column to move
     */
    public void setSelection(int line, int column) {
        setSelection(line, column, true);
    }

    /**
     * Move selection to given position
     *
     * @param line          The line to move
     * @param column        The column to move
     * @param makeItVisible Make the character visible
     */
    public void setSelection(int line, int column, boolean makeItVisible) {
        if (column > 0 && isEmoji(mText.charAt(line, column - 1))) {
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
        if (makeItVisible) {
            ensurePositionVisible(line, column);
        } else {
            invalidate();
        }
        mCursorBlink.onSelectionChanged();
        if (mTextActionPresenter != null) {
            mTextActionPresenter.onExit();
        }
        
        showOrHideDiagnosticWindow();
        
        if(mLanguage != null) {
            AbstractLanguageClient client = LSPProvider.getClientForLanguage(mLanguage.getLanguageCode());
            if(client != null) {
                client.hideSignatureHelp();
            }
        }
        
        if(mListener != null) {
            Cursor c = getCursor();
            mListener.onSetSelection(c.getLeftLine(), c.getLeftColumn(), c.getRightLine(), c.getRightColumn());
        }
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
    public void setSelectionRegion(int lineLeft, int columnLeft, int lineRight, int columnRight) {
        setSelectionRegion(lineLeft, columnLeft, lineRight, columnRight, true);
    }

    /**
     * Set selection region
     *
     * @param lineLeft         Line left
     * @param columnLeft       Column Left
     * @param lineRight        Line right
     * @param columnRight      Column right
     * @param makeRightVisible Whether make right cursor visible
     */
    public void setSelectionRegion(int lineLeft, int columnLeft, int lineRight, int columnRight, boolean makeRightVisible) {
        int start = getText().getCharIndex(lineLeft, columnLeft);
        int end = getText().getCharIndex(lineRight, columnRight);
		
        if (start > end) {
            setSelectionRegion(lineRight, columnRight, lineLeft, columnLeft, makeRightVisible);
            return;
        }
        boolean lastState = mCursor.isSelected();
        if (columnLeft > 0) {
            int column = columnLeft - 1;
            char ch = mText.charAt(lineLeft, column);
            if (isEmoji(ch)) {
                columnLeft++;
                if (columnLeft > mText.getColumnCount(lineLeft)) {
                    columnLeft--;
                }
            }
        }
        if (columnRight > 0) {
            int column = columnRight;
            char ch = mText.charAt(lineRight, column);
            if (isEmoji(ch)) {
                columnRight++;
                if (columnRight > mText.getColumnCount(lineRight)) {
                    columnRight--;
                }
            }
        }
        mCursor.setLeft(lineLeft, columnLeft);
        mCursor.setRight(lineRight, columnRight);
        updateCursor();
        mCompletionWindow.hide();
        hideDiagnosticWindow();
        if (makeRightVisible) {
            ensurePositionVisible(lineRight, columnRight);
        } else {
            invalidate();
        }
        mCursorBlink.onSelectionChanged();
		// Removed mCursor.isSelected() so we can start action mode 
        if (!lastState /* && mCursor.isSelected() */ && mStartedActionMode != ACTION_MODE_SEARCH_TEXT) {
            mTextActionPresenter.onBeginTextSelect();
        }
        mEventHandler.notifyTouchedSelectionHandlerLater();
        showOrHideDiagnosticWindow();
        
        if(mListener != null) {
            Cursor c = getCursor();
            mListener.onSetSelection(c.getLeftLine(), c.getLeftColumn(), c.getRightLine(), c.getRightColumn());
        }
    }

    /**
     * Move to next page
     */
    public void movePageDown() {
        mEventHandler.onScroll(null, null, 0, getHeight());
        mCompletionWindow.hide(); hideDiagnosticWindow();
    }

    /**
     * Move to previous page
     */
    public void movePageUp() {
        mEventHandler.onScroll(null, null, 0, -getHeight());
        mCompletionWindow.hide();
        hideDiagnosticWindow();
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
            notifyExternalCursorChange();
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
            mCursor.onDeleteKeyPressed();
            notifyExternalCursorChange();
        }
    }

    /**
     * @return Text displaying, the result is read-only. You should not make changes to this object as it is used internally
     * @see CodeEditor#setText(CharSequence)
     */
    @NonNull
    public Content getText() {
        return mText;
    }

    /**
     * Sets the text to be displayed.
     *
     * @param text the new text you want to display
     */
    public void setText(@Nullable CharSequence text) {
        if (text == null) {
            text = "";
        }

        if (mText != null) {
            mText.removeContentListener(this);
            mText.setLineListener(null);
        }
        
        // There should not be any tabs in the text
        // This is needed for the JavaLanguageServer
        String t = text.toString();
        t = t.replace("\t", Symbols.createTabSpaces());
        
        mText = new Content(this, t);
        mCursor = mText.getCursor();
        mCursor.setAutoIndent(mAutoIndentEnabled);
        mCursor.setLanguage(mLanguage);
        mEventHandler.reset();
        mText.addContentListener(this);
        mText.setUndoEnabled(mUndoEnabled);
        mText.setLineListener(this);

        if (mSpanner != null) {
            mSpanner.setCallback(null);
            mSpanner.shutdown();
        }
        mSpanner = new TextAnalyzer(mLanguage);
        mSpanner.setCallback(this);

        TextAnalyzeResult colors = mSpanner.getResult();
        colors.getSpanMap().clear();
        mSpanner.analyze(getText());

        requestLayout();

        if (mListener != null) {
            mListener.onNewTextSet(this);
        }
        
        if (mInputMethodManager != null) {
            mInputMethodManager.restartInput(this);
        }
        createLayout();
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
        mColors = colors;
        if (mCompletionWindow != null) {
            mCompletionWindow.applyColorScheme();
        }
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
     * Get analyze result.
     * <strong>Do not make changes to it or read concurrently</strong>
     */
    @NonNull
    public TextAnalyzeResult getTextAnalyzeResult() {
        return mSpanner.getResult();
    }

    /**
     * Hide auto complete window if shown
     */
    public void hideAutoCompleteWindow() {
        if (mCompletionWindow != null) {
            mCompletionWindow.hide();
        }
    }
    
    public void hideDiagnosticWindow() {
        if(mDiagnosticWindow != null) {
            mDiagnosticWindow.hide();
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
        //Logs.log("Send selection changes: candidate(start = " + candidatesStart + ", end =" + candidatesEnd + " ) cursor(left = " + mCursor.getLeft() + ", right =" + mCursor.getRight() + ")");
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
            request.hintMaxChars = EditorInputConnection.TEXT_LENGTH_LIMIT;
        }
        /*if (selEnd - selBegin > request.hintMaxChars) {
            startOffset = selBegin;
        } else {
            int redundantLength = (request.hintMaxChars - (selEnd - selBegin)) / 2;
            startOffset = selBegin - redundantLength;
        }*/
        startOffset = 0;
        //startOffset = Math.max(0, startOffset);
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
    protected void notifyExternalCursorChange() {
        //Logs.log("Call cursorChangeExternal()");
        updateExtractedText();
        updateSelection();
        updateCursorAnchor();
        // Restart if composing
        if (mConnection.mComposingLine != -1) {
            restartInput();
        }
    }

    protected void restartInput() {
        mConnection.invalid();
        mInputMethodManager.restartInput(this);
    }

    /**
     * Send cursor position in text and on screen to input method
     */
    protected void updateCursor() {
        updateCursorAnchor();
        updateExtractedText();
        if (!mText.isInBatchEdit()) {
            updateSelection();
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
    protected void onColorUpdated(int type) {
        if (type == EditorColorScheme.AUTO_COMP_PANEL_BG || type == EditorColorScheme.AUTO_COMP_PANEL_CORNER) {
            if (mCompletionWindow != null)
                mCompletionWindow.applyColorScheme();
            return;
        }
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

    //-------------------------------------------------------------------------------
    //-------------------------Override methods--------------------------------------
    //-------------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setText(arguments.getCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE));
                    return true;
                }
                return false;
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
    public boolean onCheckIsTextEditor() {
        return isEnabled() && isEditable();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        if (!isEditable() || !isEnabled()) {
            return null;
        }
        outAttrs.inputType = mInputType != 0 ? mInputType : EditorInfo.TYPE_CLASS_TEXT 
							| EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE 
							| EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS 
							| EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        
		outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI|EditorInfo.IME_FLAG_NO_FULLSCREEN;
        
        outAttrs.initialSelStart = getCursor() != null ? getCursor().getLeft() : 0;
        outAttrs.initialSelEnd = getCursor() != null ? getCursor().getRight() : 0;
        outAttrs.initialCapsMode = mConnection.getCursorCapsMode(0);
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
        boolean isShiftPressed = mKeyMetaStates.isShiftPressed();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_MOVE_HOME:
            case KeyEvent.KEYCODE_MOVE_END:
                if (isShiftPressed && (!mCursor.isSelected())) {
                    mLockedSelection = mCursor.left();
                } else if(!isShiftPressed && mLockedSelection != null) {
                    mLockedSelection = null;
                }
                mKeyMetaStates.adjust();
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{
                return mTextActionPresenter != null && mTextActionPresenter.onExit();
            }
            case KeyEvent.KEYCODE_DEL:
                if (isEditable()) {
                    mCursor.onDeleteKeyPressed();
                    notifyExternalCursorChange();
                }
                return true;
            case KeyEvent.KEYCODE_FORWARD_DEL: {
                if (isEditable()) {
                    mConnection.deleteSurroundingText(0, 1);
                    notifyExternalCursorChange();
                }
                return true;
            }
            case KeyEvent.KEYCODE_ENTER: {
                if (isEditable()) {
                    if (mCompletionWindow.isShowing()) {
                        mCompletionWindow.select();
                        return true;
                    }
                    NewlineHandler[] handlers = mLanguage.getNewlineHandlers();
                    if (handlers == null || getCursor().isSelected()) {
                        mCursor.onCommitText("\n", true);
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
                                        NewlineHandler.HandleResult result = handler.handleNewline(beforeText, afterText, getTabWidth());
                                        if (result != null) {
                                            mCursor.onCommitText(result.text, false);
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
                                    } catch (Exception e) {
                                    }
                                    break;
                                }
                            }
                        }
                        if (!consumed) {
                            mCursor.onCommitText("\n", true);
                        }
                    }
                    notifyExternalCursorChange();
                }
                return true;
            }
            case KeyEvent.KEYCODE_DPAD_DOWN:
                moveSelectionDown();
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                moveSelectionUp();
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                moveSelectionLeft();
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                moveSelectionRight();
                return true;
            case KeyEvent.KEYCODE_MOVE_END:
                moveSelectionEnd();
                return true;
            case KeyEvent.KEYCODE_MOVE_HOME:
                moveSelectionHome();
                return true;
            case KeyEvent.KEYCODE_PAGE_DOWN:
                movePageDown();
                return true;
            case KeyEvent.KEYCODE_PAGE_UP:
                movePageUp();
                return true;
            case KeyEvent.KEYCODE_TAB:
                if (isEditable()) {
                    commitTab();
                }
                return true;
            case KeyEvent.KEYCODE_PASTE:
                if (isEditable()) {
                    pasteText();
                }
                return true;
            case KeyEvent.KEYCODE_COPY:
                copyText();
                return true;
            case KeyEvent.KEYCODE_SPACE:
                if (isEditable()) {
                    getCursor().onCommitText(" ");
                    notifyExternalCursorChange();
                }
                return true;
            default:
                if (event.isCtrlPressed() && !event.isAltPressed()) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_V:
                            if (isEditable()) {
                                pasteText();
                            }
                            return true;
                        case KeyEvent.KEYCODE_C:
                            copyText();
                            return true;
                        case KeyEvent.KEYCODE_X:
                            if (isEditable()) {
                                cutText();
                            } else {
                                copyText();
                            }
                            return true;
                        case KeyEvent.KEYCODE_A:
                            selectAll();
                            return true;
                        case KeyEvent.KEYCODE_Z:
                            if (isEditable()) {
                                undo();
                            }
                            return true;
                        case KeyEvent.KEYCODE_Y:
                            if (isEditable()) {
                                redo();
                            }
                            return true;
                    }
                } else if (!event.isCtrlPressed() && !event.isAltPressed()) {
                    if (event.isPrintingKey() && isEditable()) {
                        String text = new String(Character.toChars(event.getUnicodeChar(event.getMetaState())));
                        SymbolPairMatch.Replacement replacement = null;
                        if (text.length() == 1 && isSymbolCompletionEnabled()) {
                            replacement = mLanguageSymbolPairs.getCompletion(text.charAt(0));
                        }
                        if (replacement == null || replacement == SymbolPairMatch.Replacement.NO_REPLACEMENT) {
                            getCursor().onCommitText(text);
                            notifyExternalCursorChange();
                        } else {
                            getCursor().onCommitText(replacement.text);
                            int delta = (replacement.text.length() - replacement.selection);
                            if (delta != 0) {
                                int newSel = Math.max(getCursor().getLeft() - delta, 0);
                                CharPosition charPosition = getCursor().getIndexer().getCharPosition(newSel);
                                setSelection(charPosition.line, charPosition.column);
                                notifyExternalCursorChange();
                            }
                        }
                    } else {
                        return super.onKeyDown(keyCode, event);
                    }
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        mKeyMetaStates.onKeyUp(event);
        if (!mKeyMetaStates.isShiftPressed() && mLockedSelection != null && !mCursor.isSelected()) {
            mLockedSelection = null;
        }
        return super.onKeyUp(keyCode, event);
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
        
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_SCROLL) {
            float v_scroll = -event.getAxisValue(MotionEvent.AXIS_VSCROLL);
            if (v_scroll != 0) {
                mEventHandler.onScroll(event, event, 0, v_scroll * 20);
            }
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
        super.computeScroll();
    }

    @Override
    public void beforeReplace(Content content) {
        mWait = true;
        mLayout.beforeReplace(content);
        if (mListener != null) {
            mListener.beforeReplace(this, content);
        }
    }
    
    public void commentLine() {
        if(getFile() == null) return;
        
        final Content text = getText();
        final String name = getFile().getName();
        int line = getCursor().getLeftLine();
        if(name.endsWith(".java") || name.endsWith(".gradle")){
            while(line >= getCursor().getLeftLine() && line <= getCursor().getRightLine()) {
                if(!text.getLineString(line).trim().startsWith("//"))
                    text.insert(line, 0, "//");
                line++;
            }
        } else if(name.endsWith(".xml")) {
            while(line >= getCursor().getLeftLine() && line <= getCursor().getRightLine()) {
                final String lineString = text.getLineString(line);
                if(!lineString.trim().startsWith("<!--")
                && !lineString.trim().endsWith("-->")) {
                    text.replace(line, 0, line, text.getColumnCount(line), "<!--".concat(lineString).concat("-->"));
                }
                line++;
            }
        }
    }
    
    public void uncommentLine() {
        if(getFile() == null) return;
        
        final Content text = getText();
        final String name = getFile().getName();
        int line = getCursor().getLeftLine();
        if(name.endsWith(".java") || name.endsWith(".gradle")) {
            while(line >= getCursor().getLeftLine() && line <= getCursor().getRightLine()) {
                String l = text.getLineString(line);
                if(l.trim().startsWith("//")) {
                    int i = l.indexOf("//");
                    text.delete(line, i, line, i + 2);
                }
                line++;
            }
        } else if(name.endsWith(".xml")) {
            final String commentStart = "<!--";
            final String commentEnd   = "-->";
            while(line >= getCursor().getLeftLine() && line <= getCursor().getRightLine()) {
                String l = text.getLineString(line);
                if(l.trim().startsWith(commentStart)) {
                    int i = l.indexOf(commentStart);
                    text.delete(line, i, line, i + commentStart.length());
                }
                if(l.trim().endsWith(commentEnd)) {
                    int i = l.lastIndexOf(commentEnd);
                    int count = text.getColumnCount(line);
                    text.delete(line, count - commentEnd.length(), line, count);
                }
                line++;
            }
        }
    }
    
    public void findDefinition() {
        if(!isGotoDefinitionEnabled() || getFile() == null) return;
        
        final ProgressDialog pd = ProgressDialog.show(getContext(), null, getContext().getString(R.string.msg_finding_definition));
        
        try {
            final DefinitionParams params = new DefinitionParams();
            params.setTextDocument(getDocumentIdentifier());
            params.setPosition(getCursorAsLSPPosition());
            final CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> future = mLanguageServer.getTextDocumentService().definition(params);
            future.whenComplete((e, t) -> {
                final Either<List<? extends Location>, List<? extends LocationLink>> either = e;
                final Throwable th = t;

                final AbstractLanguageClient client = LSPProvider.getClientForLanguage(mLanguage.getLanguageCode());
                if(either == null || client == null || future.isCancelled() || future.isCompletedExceptionally()) {
                    showDefinitionNotFound(pd);
                    return;
                }

                if(either.isLeft()) {
                    final List<? extends Location> locations = either.getLeft();
                    if(locations == null || locations.size() <= 0) {
                        showDefinitionNotFound(pd);
                        return;
                    }

                    ThreadUtils.runOnUiThread(() -> {
                        if(locations.size() == 1) {
                            Location location = locations.get(0);
                            showDocument(client, location.getUri(), location.getRange());
                        } else {
                            client.showLocations(locations);
                        }
                    });

                    dismissOnUiThread(pd);
                } else if(either.isRight()) {
                    final List<? extends LocationLink> locations = either.getRight();
                    if(locations == null || locations.size() <= 0) {
                        showDefinitionNotFound(pd);
                        return;
                    }

                    ThreadUtils.runOnUiThread(() -> {
                        if(locations.size() == 1) {
                            LocationLink location = locations.get(0);
                            showDocument(client, location.getTargetUri(), location.getTargetSelectionRange());
                        } else {
                            client.showLocationLinks(locations);
                        }
                    });

                    dismissOnUiThread(pd);
                }
            });
        } catch (Throwable th) {
            showDefinitionNotFound(pd);
        }
    }
    
    private void showDefinitionNotFound(final ProgressDialog pd) {
        ThreadUtils.runOnUiThread(() -> {
            toast(R.string.msg_no_definition, Toaster.Type.ERROR);
            pd.dismiss();
        });
    }
    
    public void findReferences() {
        if(!isFindReferencesEnabled() || getFile() == null) return;
        
        final ProgressDialog pd = ProgressDialog.show(getContext(), null, getContext().getString(R.string.msg_finding_references));
        
        try {
            ReferenceParams params = new ReferenceParams();
            params.setTextDocument(getDocumentIdentifier());
            params.setPosition(getCursorAsLSPPosition());
            params.setContext(new ReferenceContext(true));
            
            final CompletableFuture<List<? extends Location>> future = mLanguageServer.getTextDocumentService().references(params);
            
            future.whenComplete((l, t) -> {
                final List<? extends Location> locations = l;
                final Throwable th = t;
                
                final AbstractLanguageClient client = LSPProvider.getClientForLanguage(mLanguage.getLanguageCode());
                
                if(locations == null || client == null || future.isCancelled() || future.isCompletedExceptionally()) {
                    showReferencesNotFound(pd);
                    return;
                }
                
                if(locations.isEmpty()) {
                    showReferencesNotFound(pd);
                    return;
                } else {
                    ThreadUtils.runOnUiThread(() -> {
                        client.showLocations(locations);
                    });
                }
                
                dismissOnUiThread(pd);
            });
        } catch (Throwable th) {
            showReferencesNotFound(pd);
        }
    }
    
    private void showReferencesNotFound(final ProgressDialog pd) {
        ThreadUtils.runOnUiThread(() -> {
            toast(R.string.msg_no_references, Toaster.Type.ERROR);
            pd.dismiss();
        });
    }
    
    public void signatureHelp(String insertedContent) {
        if(mSignatureHelpEnabled
           && mLanguageServer != null
           && getFile() != null
           && mSignatureHelpTriggerChars != null
           && mSignatureHelpTriggerChars.size() > 0
           && mSignatureHelpTriggerChars.contains(insertedContent)) {

            SignatureHelpContext context = new SignatureHelpContext();
            context.setTriggerCharacter(insertedContent);
            context.setTriggerKind(SignatureHelpTriggerKind.ContentChange);

            SignatureHelpParams params = new SignatureHelpParams();
            params.setPosition(getCursorAsLSPPosition());
            params.setTextDocument(getDocumentIdentifier());
            params.setContext(context);

            CompletableFuture<SignatureHelp> future = mLanguageServer.getTextDocumentService().signatureHelp(params);
            
            future.whenComplete((h, t) -> {
                final SignatureHelp help = h;
                final Throwable th = t;
                final AbstractLanguageClient client = LSPProvider.getClientForLanguage(mLanguage.getLanguageCode());
                
                if(help == null || client == null || future.isCancelled() || future.isCompletedExceptionally()) {
                    return;
                }
                
                ThreadUtils.runOnUiThread(() -> {
                    client.showSignatureHelp(help, getFile());
                });
            });
        }
    }
    
    private void dismissOnUiThread(final Dialog dialog) {
        ThreadUtils.runOnUiThread(() -> {
            dialog.dismiss();
        });
    }
    
    /**
     * Request the client to show the specific document in an editor and select the provided range
     */
    private void showDocument(AbstractLanguageClient client, String uri, org.eclipse.lsp4j.Range range) {
        ShowDocumentParams params = new ShowDocumentParams();
        params.setExternal(false);
        params.setSelection(range);
        params.setUri(uri);
        client.showDocument(params);
    }
    
    public void didSave () {
        if(mLanguageServer != null && getFile() != null) {
            mLanguageServer.getTextDocumentService().didSave(new DidSaveTextDocumentParams(new org.eclipse.lsp4j.TextDocumentIdentifier(getFile().toURI().toString())));
        }
    }
    
    public void close() {
        if(mLanguageServer != null && getFile() != null) {
            mLanguageServer.getTextDocumentService().didClose(new DidCloseTextDocumentParams(new org.eclipse.lsp4j.TextDocumentIdentifier(getFile().toURI().toString())));
        }
    }
    
    public void codeAction() {
        
    }
    
    public Position getCursorAsLSPPosition() {
        return new Position(getCursor().getLeftLine(), getCursor().getLeftColumn());
    }
    
    public TextDocumentIdentifier getDocumentIdentifier() {
        return new TextDocumentIdentifier(getFile().toURI().toString());
    }
    
    private boolean isInString() {
        final int line = getCursor().getLeftLine();
        final int column = getCursor().getLeftColumn();
        if(stringMap == null || stringMap.isEmpty() || !stringMap.containsKey(line)) return false;
        List<Range> strings = stringMap.get(line);
        if(strings == null || strings.isEmpty()) return false;
        for(int i=0;i<strings.size();i++) {
            Range r = strings.get(i);
            if(r.getStart().getCharacter() <= column && column <= r.getEnd().getCharacter())
                return true;
        }
        return false;
    }
    
    private void toast(int msg, Toaster.Type type) {
        StudioApp.getInstance().toast(msg, type);
    }
    
    private void toast(String msg, Toaster.Type type) {
        StudioApp.getInstance().toast(msg, type);
    }

    @Override
    public void afterInsert(Content content, int startLine, int startColumn, int endLine, int endColumn, CharSequence insertedContent) {
        notifyChanged();
        
        if(mDiagnosticWindow != null)
            mDiagnosticWindow.hide();
        
        // Update spans
        if (isSpanMapPrepared(true, endLine - startLine)) {
            if (startLine == endLine) {
                SpanMapUpdater.shiftSpansOnSingleLineInsert(mSpanner.getResult().getSpanMap(), startLine, startColumn, endColumn);
            } else {
                SpanMapUpdater.shiftSpansOnMultiLineInsert(mSpanner.getResult().getSpanMap(), startLine, startColumn, endLine, endColumn);
            }
        }

        mCursorBlink.onSelectionChanged();
        mLayout.afterInsert(content, startLine, startColumn, endLine, endColumn, insertedContent);

        // Notify input method
        updateCursor();
        mWait = false;
        // Visibility & State shift
        exitSelectModeIfNeeded();

        // Auto completion
        // Trigger only when autocompletion is enabled and cursor is not in a string token
        if (isAutoCompletionEnabled() && !isInString()) {
            if ((mConnection.mComposingLine == -1 || mCompletionOnComposing) && endColumn != 0 && startLine == endLine) {
                int end = endColumn;
                while (endColumn > 0) {
                    if (mLanguage.isAutoCompleteChar(content.charAt(endLine, endColumn - 1))) {
                        endColumn--;
                    } else {
                        break;
                    }
                }
                if (end > endColumn) {
                    String line = content.getLineString(endLine);
                    String prefix = line.substring(endColumn, end);
                    mCompletionWindow.setPrefix(prefix);
                    mCompletionWindow.show();
                } else {
                    postHideCompletionWindow();
                }
            } else {
                postHideCompletionWindow();
            }
            if (mCompletionWindow.isShowing()) {
                updateCompletionWindowPosition();
            }
        } else {
            mCompletionWindow.hide();
        }

        updateCursorAnchor();
        ensureSelectionVisible();
        // Notify to update highlight
        mSpanner.analyze(mText);
        mEventHandler.hideInsertHandle();
        
        // Notify listener
        if (mListener != null) {
            mListener.afterInsert(this, mText, startLine, startColumn, endLine, endColumn, insertedContent);
        }
        
        signatureHelp(insertedContent.toString());
    }
    
    @Override
    public void afterDelete(Content content, int startLine, int startColumn, int endLine, int endColumn, CharSequence deletedContent) {
        notifyChanged();
        
        if(mDiagnosticWindow != null)
            mDiagnosticWindow.hide();
        
        if (isSpanMapPrepared(false, endLine - startLine)) {
            if (startLine == endLine) {
                SpanMapUpdater.shiftSpansOnSingleLineDelete(mSpanner.getResult().getSpanMap(), startLine, startColumn, endColumn);
            } else {
                SpanMapUpdater.shiftSpansOnMultiLineDelete(mSpanner.getResult().getSpanMap(), startLine, startColumn, endLine, endColumn);
            }
        }

        mCursorBlink.onSelectionChanged();
        mLayout.afterDelete(content, startLine, startColumn, endLine, endColumn, deletedContent);

        updateCursor();
        exitSelectModeIfNeeded();

        if (isAutoCompletionEnabled()) {
            if (mConnection.mComposingLine == -1 && mCompletionWindow.isShowing()) {
                if (startLine != endLine || startColumn != endColumn - 1) {
                    postHideCompletionWindow();
                }
                String prefix = mCompletionWindow.getPrefix();
                if (prefix == null || prefix.length() - 1 <= 0) {
                    postHideCompletionWindow();
                } else {
                    prefix = prefix.substring(0, prefix.length() - 1);
                    updateCompletionWindowPosition();
                    mCompletionWindow.setPrefix(prefix);
                }
            }
        } else {
            mCompletionWindow.hide();
        }

        if (!mWait) {
            updateCursorAnchor();
            ensureSelectionVisible();
            mSpanner.analyze(mText);
            mEventHandler.hideInsertHandle();
        }
        
        if (mListener != null) {
            mListener.afterDelete(this, mText, startLine, startColumn, endLine, endColumn, deletedContent);
        }
    }

    @Override
    public void onFormatFail(final Throwable throwable) {
        if (mListener != null && !mListener.onFormatFail(this, throwable)) {
            post(() -> Toast.makeText(getContext(), throwable.toString(), Toast.LENGTH_SHORT).show());
        }
        mFormatThread = null;
    }

    @Override
    public void onRemove(Content content, ContentLine line) {
        mLayout.onRemove(content, line);
    }

    @Override
    public void onFormatSucceed(CharSequence originalText, final CharSequence newText) {
        if (mListener != null) {
            mListener.onFormatSucceed(this);
        }
        mFormatThread = null;
        if (originalText == mText) {
            post(() -> {
                int line = mCursor.getLeftLine();
                int column = mCursor.getLeftColumn();
                mText.replace(0, 0, getLineCount() - 1, mText.getColumnCount(getLineCount() - 1), newText);
                getScroller().forceFinished(true);
                mCompletionWindow.hide();
                hideDiagnosticWindow();
                setSelectionAround(line, column);
            });
        }
    }

    @Override
    public void onAnalyzeDone(TextAnalyzer provider) {
        if (provider == mSpanner) {
			if(mSpanner.getResult().getHexColors().size() > 0) {
				setLineColors(mSpanner.getResult().getHexColors());
			}
            if(mSpanner.getResult().getStringMap().size() > 0) {
                setStringMap(mSpanner.getResult().getStringMap());
            }
            if (mHighlightCurrentBlock) {
                mCursorPosition = findCursorBlock();
            }
            postInvalidate();
        }
    }
    
    private void notifyChanged() {
        if(mLanguageServer != null) {
            DidChangeTextDocumentParams p = didChangeParams();
            if(p != null) {
                mLanguageServer.getTextDocumentService().didChange(p);
            }
        }
    }

    protected DidChangeTextDocumentParams didChangeParams() {
        if(getFile() == null) {
            return null;
        }

        VersionedTextDocumentIdentifier doc = new VersionedTextDocumentIdentifier();
        doc.setVersion(++mFileVersion);
        doc.setUri(getFile().toURI().toString());
        DidChangeTextDocumentParams p = new DidChangeTextDocumentParams();
        p.setTextDocument(doc);
        p.setContentChanges(createChangeEvents());

        return p;
    }

    private List<TextDocumentContentChangeEvent> createChangeEvents() {
        final List<TextDocumentContentChangeEvent> events = new ArrayList<>();
        TextDocumentContentChangeEvent event = new TextDocumentContentChangeEvent();
        event.setText(getText().toString());
        events.add(event);
        return events;
    }

    public void onEndTextSelect() {
        showTextActionPopup();
    }

    public void onEndGestureInteraction() {
        showTextActionPopup();
    }

    //-------------------------------------------------------------------------------
    //-------------------------Inner classes-----------------------------------------
    //-------------------------------------------------------------------------------

    /**
     * Mode for presenting text actions
     */
    public enum TextActionMode {

        /**
         * In this way, the editor shows a windows inside the editor to present actions
         */
        POPUP_WINDOW,
        /**
         * Beautified text action window by @RandunuRtx
         */
        POPUP_WINDOW_2,
        /**
         * In this way, the editor starts a {@link ActionMode} to present actions
         */
        ACTION_MODE

    }


    /**
     * Interface for various ways to present text action panel
     */
    protected interface EditorTextActionPresenter {

        /**
         * Selected text is clicked
         *
         * @param event Event
         */
        void onSelectedTextClicked(MotionEvent event);

        /**
         * Text selection, gesture interaction is over
         */
        void onTextSelectionEnd();

        /**
         * Notify that the position of panel should be updated.
         * If the presenter is displayed in editor's viewport, it should update
         * its position
         */
        void onUpdate();

        /**
         * Notify that the position of panel should be updated.
         * If the presenter is displayed in editor's viewport, it should update
         * its position
         *
         * @param updateReason {@link TextComposeBasePopup#DISMISS} {@link TextComposeBasePopup#DRAG} {@link TextComposeBasePopup#SCROLL}
         */
        void onUpdate(int updateReason);

        /**
         * Start the presenter
         */
        void onBeginTextSelect();


        /**
         * Exit the presenter
         * @return Whether action is executed. Return true if this has cause UI change such as
         * popup window hides and action mode exits
         */
        boolean onExit();

        /**
         * Called by editor to check whether it should draw handles of cursor
         */
        boolean shouldShowCursor();

    }

    /**
     * Class for saving state for cursor
     */
    static class CursorPaintAction {

        /**
         * Row position
         */
        final int row;

        /**
         * Center x offset
         */
        final float centerX;

        /**
         * Handle rectangle
         */
        final RectF outRect;

        /**
         * Draw as insert cursor
         */
        final boolean insert;

        int handleType = -1;

        CursorPaintAction(int row, float centerX, RectF outRect, boolean insert) {
            this.row = row;
            this.centerX = centerX;
            this.outRect = outRect;
            this.insert = insert;
        }

        CursorPaintAction(int row, float centerX, RectF outRect, boolean insert, int handleType) {
            this.row = row;
            this.centerX = centerX;
            this.outRect = outRect;
            this.insert = insert;
            this.handleType = handleType;
        }


        /**
         * Execute painting on the given editor and canvas
         */
        void exec(Canvas canvas, CodeEditor editor) {
            editor.drawCursor(canvas, centerX, row, outRect, insert, handleType);
        }

    }

    private static final Logger LOG = Logger.instance("CodeEditor");
    
}
