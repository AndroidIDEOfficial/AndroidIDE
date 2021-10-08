package com.itsaky.androidide.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.androidide.adapters.CompletionListAdapter;
import com.itsaky.androidide.databinding.FragmentEditorBinding;
import com.itsaky.androidide.fragments.preferences.EditorPreferences;
import com.itsaky.androidide.language.groovy.GroovyLanguage;
import com.itsaky.androidide.language.java.JavaLanguage;
import com.itsaky.androidide.language.xml.XMLLanguage;
import com.itsaky.androidide.language.xml.lexer.XMLLexer;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.ReadFileTask;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.TypefaceUtils;
import io.github.rosemoe.editor.interfaces.EditorEventListener;
import io.github.rosemoe.editor.langs.EmptyLanguage;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

public class EditorFragment extends BaseFragment implements EditorEventListener {
	
	public FragmentEditorBinding mBinding;
	private File mFile;
	private boolean isRead = false;
	private boolean isModified = false;
	private boolean isFirstCreate = false;
	
    private ModificationStateListener mModificationStateListener;
    private FileOpenListener mOpenListener;
	private static AndroidProject mProject;
    
	public static final String KEY_FILE_PATH = "file_path";
	public static final String KEY_PROJECT = "project";
    public static final String KEY_LINE_START = "line_start";
    public static final String KEY_LINE_END = "line_end";
    public static final String KEY_COLUMN_START = "col_start";
    public static final String KEY_COLUMN_END = "col_end";
    
	public static final String EXT_JAVA = ".java";
	public static final String EXT_XML = ".xml";
	public static final String EXT_HTML = ".html";
	public static final String EXT_GRADLE = ".gradle";
	public static final String EXT_GROOVY = ".groovy";
	public static final String EXT_KOTLIN = ".kt";
	public static final String EXT_JSON = ".json";
    
    public EditorFragment setModificationStateListener(ModificationStateListener listener) {
        this.mModificationStateListener = listener;
        return this;
    }
    
    public String getTabTitle() {
        if(getFile() != null) {
            String title = getFile().getName();
            if(isModified())
                title += "*";
                
            return title;
        }
        return "";
    }
    
	public static EditorFragment newInstance(File file, AndroidProject project, org.eclipse.lsp4j.Range selection) {
		Bundle bundle = new Bundle();
		bundle.putString(KEY_FILE_PATH, file.getAbsolutePath());
        bundle.putInt(KEY_LINE_START, selection.getStart().getLine());
        bundle.putInt(KEY_LINE_END, selection.getEnd().getLine());
        bundle.putInt(KEY_COLUMN_START, selection.getStart().getCharacter());
        bundle.putInt(KEY_COLUMN_END, selection.getEnd().getCharacter());
		bundle.putParcelable(KEY_PROJECT, project);
		EditorFragment frag = new EditorFragment();
		frag.setArguments(bundle);
		return frag;
	}
    
    public EditorFragment setFileOpenListener(FileOpenListener openListener) {
        this.mOpenListener = openListener;
        return this;
    }
    
    public CodeEditor getEditor() {
        return mBinding.editor;
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFirstCreate = true;
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        save();
        super.onSaveInstanceState(outState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = FragmentEditorBinding.inflate(inflater, container, false);
		return mBinding.getRoot();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (getActivity() == null || getArguments() == null || !getArguments().containsKey(KEY_FILE_PATH) || !getArguments().containsKey(KEY_PROJECT)) return;
		mFile = new File(getArguments().getString(KEY_FILE_PATH));
		mProject = getArguments().getParcelable(KEY_PROJECT);
        
		mBinding.editor.setOverScrollEnabled(false);
		mBinding.editor.setTypefaceText(TypefaceUtils.jetbrainsMono());
		mBinding.editor.setTextActionMode(CodeEditor.TextActionMode.ACTION_MODE);
		mBinding.editor.setHighlightCurrentBlock(true);
		mBinding.editor.setEventListener(this);
        mBinding.editor.setAutoCompletionOnComposing(true);
        mBinding.editor.setAutoCompletionItemAdapter(new CompletionListAdapter());
		mBinding.editor.setLineColorsEnabled(true);
		mBinding.editor.setDividerWidth(SizeUtils.dp2px(1));
        
		configureEditorIfNeeded();
		
        final org.eclipse.lsp4j.Range range = fromArgs(getArguments());
		new TaskExecutor().executeAsync(new ReadFileTask(mFile), result -> {
			mBinding.editor.setText(result);
			postRead();
            mBinding.editor.post(() -> {
                if(LSPUtils.isEqual(range.getStart(), range.getEnd())) {
                    getEditor().setSelection(range.getStart().getLine(), range.getStart().getCharacter());
                } else {
                    getEditor().setSelectionRegion(range.getStart().getLine(), range.getStart().getCharacter(), range.getEnd().getLine(), range.getEnd().getCharacter());
                }
            });
		});
	}

    private org.eclipse.lsp4j.Range fromArgs(Bundle args) {
        if(!(args.containsKey(KEY_LINE_START)
         && args.containsKey(KEY_COLUMN_START)
         && args.containsKey(KEY_LINE_END)
         && args.containsKey(KEY_COLUMN_END)))
            return LSPUtils.Range_ofZero;
            
        return new org.eclipse.lsp4j.Range(
            new org.eclipse.lsp4j.Position(
                args.getInt(KEY_LINE_START),
                args.getInt(KEY_COLUMN_START)),
            new org.eclipse.lsp4j.Position(
                args.getInt(KEY_LINE_END),
                args.getInt(KEY_COLUMN_END)
                ));
    }
    
	public void setDiagnostics(List<Diagnostic> diags) {
		if(mBinding.editor != null && diags != null) {
            Map<Range, Diagnostic> map = new HashMap<>();
            for(int i=0;i<diags.size();i++) {
                final Diagnostic d = diags.get(i);
                if(d == null) continue;
                map.put(d.getRange(), d);
            }
            mBinding.editor.setDiagnostics(map);
        }
	}
	
	private void configureEditorIfNeeded() {
		boolean sizeChanged = isFirstCreate || ConstantsBridge.EDITORPREF_SIZE_CHANGED;
		boolean flagsChanged = isFirstCreate || ConstantsBridge.EDITORPREF_FLAGS_CHANGED;
		boolean drawHexChanged = isFirstCreate || ConstantsBridge.EDITORPREF_DRAW_HEX_CHANGED;
		final PreferenceManager prefs = getStudioActivity().getApp().getPrefManager();
		if(sizeChanged) {
			float textSize = prefs.getFloat(EditorPreferences.KEY_EDITOR_FONT_SIZE);
			if(textSize < 6 || textSize > 32)
				textSize = 14;
			mBinding.editor.setTextSize(textSize);
			ConstantsBridge.EDITORPREF_SIZE_CHANGED = false;
		}
		
		if(flagsChanged) {
			int flags = 0;
			if(prefs.getBoolean(PreferenceManager.KEY_EDITORFLAG_WS_LEADING, true))
				flags |= CodeEditor.FLAG_DRAW_WHITESPACE_LEADING;
			if(prefs.getBoolean(PreferenceManager.KEY_EDITORFLAG_WS_TRAILING, false))
				flags |= CodeEditor.FLAG_DRAW_WHITESPACE_TRAILING;
			if(prefs.getBoolean(PreferenceManager.KEY_EDITORFLAG_WS_INNER, true))
				flags |= CodeEditor.FLAG_DRAW_WHITESPACE_INNER;
			if(prefs.getBoolean(PreferenceManager.KEY_EDITORFLAG_WS_EMPTY_LINE, true))
				flags |= CodeEditor.FLAG_DRAW_WHITESPACE_FOR_EMPTY_LINE;
			if(prefs.getBoolean(PreferenceManager.KEY_EDITORFLAG_LINE_BREAK, true))
				flags |= CodeEditor.FLAG_DRAW_LINE_SEPARATOR;

			mBinding.editor.setNonPrintablePaintingFlags(flags);
			ConstantsBridge.EDITORPREF_FLAGS_CHANGED = false;
		}
		
		if(drawHexChanged) {
			mBinding.editor.setLineColorsEnabled(prefs.getBoolean(PreferenceManager.KEY_EDITOR_DRAW_HEX, true));
			ConstantsBridge.EDITORPREF_DRAW_HEX_CHANGED = false;
		}
		
		isFirstCreate = false;
	}
    
    public String getText() {
        return mBinding.editor.getText().toString();
    }
	
	public boolean isModified() {
		return isModified;
	}
	
	public void undo() {
		if(mBinding.editor.canUndo())
			mBinding.editor.undo();
	}
	
	public void redo() {
		if(mBinding.editor.canRedo())
			mBinding.editor.redo();
	}
    
    public void findDefinition() {
        mBinding.editor.findDefinition();
    }
    
    public void findReferences() {
        mBinding.editor.findReferences();
    }
    
    public void commentLine() {
        mBinding.editor.commentLine();
    }
    
    public void uncommentLine() {
        mBinding.editor.uncommentLine();
    }
    
    public void beginSearch() {
        mBinding.editor.beginSearchMode();
    }

	public void save() {
        if(mFile == null || mBinding == null || mBinding.editor == null || mBinding.editor.getText() == null) return;
        final String text = mBinding.editor.getText().toString();
        final boolean wrote = FileIOUtils.writeFileFromString(mFile, text);
		notifySaved(wrote, text);
		isModified = false;
	}
    
	public File getFile() {
		return mFile;
	}

	@Override
	public void onResume() {
		super.onResume();
		configureEditorIfNeeded();
	}
	
	private void postRead() {
		if (mFile.isFile() && mFile.getName().endsWith(EXT_JAVA)) {
			mBinding.editor.setEditorLanguage(new JavaLanguage(getFile()));
		} else if (mFile.isFile() && mFile.getName().endsWith(EXT_XML)) {
			mBinding.editor.setEditorLanguage(new XMLLanguage(getFile()));
		} else if (mFile.isFile() && mFile.getName().endsWith(EXT_GRADLE)) {
			mBinding.editor.setEditorLanguage(new GroovyLanguage(getFile()));
		} else {
			mBinding.editor.setEditorLanguage(new EmptyLanguage());
		}
        
        // File must be set only after setting the language
        // This will make sure that textDocument/didOpen is sent
        mBinding.editor.setFile(getFile());
        
        mBinding.editor.setColorScheme(new SchemeAndroidIDE());
		isRead = true;
        if(mOpenListener != null)
            mOpenListener.onOpenSuccessful(getFile(), getText());
	}
	
	private void closeCurrentTag(String text, int line, int col) {
        try {
            XMLLexer lexer = new XMLLexer(CharStreams.fromReader(new StringReader(text)));
            Token token;
            boolean wasSlash = false, wasOpen = false;
            ArrayList<String> currentNames = new ArrayList<>();
            while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
                final int type = token.getType();
                if(type == XMLLexer.OPEN) {
                    wasOpen = true;
                } else if(type == XMLLexer.Name) {
                    if(wasOpen && wasSlash && currentNames.size() > 0) {
                        currentNames.remove(0);
                    } else if(wasOpen){
                        currentNames.add(0, token.getText());
                        wasOpen = false;
                    }
                } else if(type == XMLLexer.OPEN_SLASH) {
                    int l = token.getLine() - 1;
                    int c = token.getCharPositionInLine();
                    if(l == line && c == col) {
                        break;
                    } else if(currentNames.size() > 0)
                        currentNames.remove(0);
                } else if(type == XMLLexer.SLASH_CLOSE
                          || type == XMLLexer.SPECIAL_CLOSE) {
                    if(currentNames.size() > 0 && token.getText().trim().endsWith("/>"))
                        currentNames.remove(0);
                } else if(type == XMLLexer.SLASH) {
                    wasSlash = true;
                } else wasOpen = wasSlash = false;
            }
            if(currentNames.size() > 0) {
                mBinding.editor.getText().insert(line, col + 2, currentNames.get(0));
            }
        } catch (Throwable th) {}
	}
	
	@Override
	public boolean onRequestFormat(CodeEditor editor, boolean async) {
		return false;
	}

	@Override
	public boolean onFormatFail(CodeEditor editor, Throwable cause) {
		return false;
	}

	@Override
	public void onFormatSucceed(CodeEditor editor) {
	}

	@Override
	public void onNewTextSet(CodeEditor editor) {
	}

    @Override
    public void onSetSelection(int startLine, int startCol, int endLine, int endCol) {
        
    }

	@Override
	public void afterDelete(CodeEditor editor, CharSequence content, int startLine, int startColumn, int endLine, int endColumn, CharSequence deletedContent) {
		isModified = true;
        notifyModified();
	}
    
	@Override
	public void afterInsert(CodeEditor editor, CharSequence content, int startLine, int startColumn, int endLine, int endColumn, CharSequence insertedContent) {
		isModified = true;
        
		if(getFile() != null && getFile().getName().endsWith(EXT_XML)) {
            boolean isOpen = false;
            try {
                isOpen = editor.getText().charAt(editor.getCursor().getLeft() - 2) == '<';
            } catch (Throwable th) {}
            if(isOpen && insertedContent.toString().equals("/")) {
                closeCurrentTag(editor.getText().toString(), endLine, endColumn);
            }
        }
        
        notifyModified();
	}

	@Override
	public void beforeReplace(CodeEditor editor, CharSequence content) {
	}
    
    private void notifySaved(boolean wrote, String text) {
        mBinding.editor.didSave();
        
        if(mModificationStateListener != null)
            mModificationStateListener.onSaved(this);
    }
    
    private void notifyModified() {
        if(mModificationStateListener != null)
            mModificationStateListener.onModified(this);
    }
    
    public static interface FileOpenListener {
        void onOpenSuccessful(File file, String text);
    }
    
    public static interface ModificationStateListener {
        void onModified(EditorFragment editor);
        void onSaved(EditorFragment editor);
    }
}
