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
import com.itsaky.androidide.interfaces.JLSRequestor;
import com.itsaky.androidide.language.groovy.GroovyLanguage;
import com.itsaky.androidide.language.java.JavaLanguage;
import com.itsaky.androidide.language.java.JavaLanguageAnalyzer;
import com.itsaky.androidide.language.xml.XMLLanguage;
import com.itsaky.androidide.language.xml.lexer.XMLLexer;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.ReadFileTask;
import com.itsaky.androidide.utils.PreferenceManager;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.lsp.Diagnostic;
import com.itsaky.lsp.DidSaveTextDocumentParams;
import com.itsaky.lsp.JavaColors;
import com.itsaky.lsp.Position;
import com.itsaky.lsp.Range;
import com.itsaky.lsp.TextDocumentIdentifier;
import com.itsaky.lsp.TextDocumentPositionParams;
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

public class EditorFragment extends BaseFragment implements EditorEventListener {
	
	public FragmentEditorBinding binding;
    private CodeEditor editor;
	private File mFile;
	private boolean isRead = false;
	private boolean isModified = false;
	private boolean isFirstCreate = false;
	
    private FileOpenListener openListener;
    private JLSRequestor jlsRequestor;
	private static AndroidProject project;
    
    private JavaLanguage mJavaLanguage;
    
	public static final String KEY_FILE_PATH = "file_path";
	public static final String KEY_PROJECT = "project";
	public static final String EXT_JAVA = ".java";
	public static final String EXT_XML = ".xml";
	public static final String EXT_HTML = ".html";
	public static final String EXT_GRADLE = ".gradle";
	public static final String EXT_GROOVY = ".groovy";
	public static final String EXT_KOTLIN = ".kt";
	public static final String EXT_JSON = ".json";
    
    public EditorFragment setJLSRequestor(JLSRequestor requestor) {
        this.jlsRequestor = requestor;
        return this;
    }
    
    public void setJavaColors(JavaColors colors) {
        if(mJavaLanguage != null) {
            JavaLanguageAnalyzer analyzer = (JavaLanguageAnalyzer) mJavaLanguage.getAnalyzer();
            analyzer.setJavaColors(colors);
            editor.notifySpansChanged();
        }
    }
    
	public static EditorFragment newInstance(File file, AndroidProject project) {
		Bundle bundle = new Bundle();
		bundle.putString(KEY_FILE_PATH, file.getAbsolutePath());
		bundle.putParcelable(KEY_PROJECT, project);
		EditorFragment frag = new EditorFragment();
		frag.setArguments(bundle);
		return frag;
	}
    
    public EditorFragment setFileOpenListener(FileOpenListener openListener) {
        this.openListener = openListener;
        return this;
    }
    
    public CodeEditor getEditor() {
        return editor;
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
		binding = FragmentEditorBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (getActivity() == null || getArguments() == null || !getArguments().containsKey(KEY_FILE_PATH) || !getArguments().containsKey(KEY_PROJECT)) return;
		mFile = new File(getArguments().getString(KEY_FILE_PATH));
		project = getArguments().getParcelable(KEY_PROJECT);
        
        editor = new CodeEditor(getContext());
		editor.setOverScrollEnabled(false);
		editor.setTypefaceText(TypefaceUtils.jetbrainsMono());
		editor.setTextActionMode(CodeEditor.TextActionMode.ACTION_MODE);
		editor.setHighlightCurrentBlock(true);
		editor.setEventListener(this);
        editor.setAutoCompletionOnComposing(true);
        editor.setAutoCompletionItemAdapter(new CompletionListAdapter());
		editor.setLineColorsEnabled(true);
		editor.setDividerWidth(SizeUtils.dp2px(1));
        editor.setJLSRequestor(jlsRequestor);
        
        binding.getRoot().addView(editor, new ViewGroup.LayoutParams(-1, -1));
        
		configureEditorIfNeeded();
		
		new TaskExecutor().executeAsync(new ReadFileTask(mFile), result -> {
			editor.setText(result);
			postRead();
		});
	}
	
	public void setDiagnostics(List<Diagnostic> diags) {
		if(editor != null && diags != null) {
            Map<Range, Diagnostic> map = new HashMap<>();
            for(int i=0;i<diags.size();i++) {
                final Diagnostic d = diags.get(i);
                if(d == null) continue;
                map.put(d.range, d);
            }
            editor.setDiagnostics(map);
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
			editor.setTextSize(textSize);
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

			editor.setNonPrintablePaintingFlags(flags);
			ConstantsBridge.EDITORPREF_FLAGS_CHANGED = false;
		}
		
		if(drawHexChanged) {
			editor.setLineColorsEnabled(prefs.getBoolean(PreferenceManager.KEY_EDITOR_DRAW_HEX, true));
			ConstantsBridge.EDITORPREF_DRAW_HEX_CHANGED = false;
		}
		
		isFirstCreate = false;
	}
    
    public String getText() {
        return editor.getText().toString();
    }
	
	public boolean isModified() {
		return isModified;
	}
	
	public void undo() {
		if(editor.canUndo())
			editor.undo();
	}
	
	public void redo() {
		if(editor.canRedo())
			editor.redo();
	}

	public void save() {
        final String text = editor.getText().toString();
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
        editor.setFile(getFile());
		if (mFile.isFile() && mFile.getName().endsWith(EXT_JAVA)) {
			editor.setEditorLanguage(mJavaLanguage = new JavaLanguage(project));
		} else if (mFile.isFile() && mFile.getName().endsWith(EXT_XML)) {
			editor.setEditorLanguage(new XMLLanguage());
		} else if (mFile.isFile() && mFile.getName().endsWith(EXT_GRADLE)) {
			editor.setEditorLanguage(new GroovyLanguage());
		} else {
			editor.setEditorLanguage(new EmptyLanguage());
		}
        editor.setColorScheme(new SchemeAndroidIDE());
		isRead = true;
        if(openListener != null)
            openListener.onOpenSuccessful(getFile(), getText());
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
                editor.getText().insert(line, col + 2, currentNames.get(0));
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
		isModified = true;
	}

    @Override
    public void onSetSelection(int startLine, int startCol, int endLine, int endCol) {
        if(jlsRequestor != null) {
            jlsRequestor.hideSignature();
        }
    }

	@Override
	public void afterDelete(CodeEditor editor, CharSequence content, int startLine, int startColumn, int endLine, int endColumn, CharSequence deletedContent) {
		isModified = true;
        requestSignature();
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
        
        requestSignature();
	}

    private void requestSignature() {
        if (jlsRequestor != null) {
            TextDocumentPositionParams p = new TextDocumentPositionParams();
            p.textDocument = new TextDocumentIdentifier(getFile().toURI());
            p.position = new Position(editor.getCursor().getLeftLine(), editor.getCursor().getLeftColumn());
            jlsRequestor.signatureHelp(p, getFile());
        }
    }

	@Override
	public void beforeReplace(CodeEditor editor, CharSequence content) {
	}
    
    private void notifySaved(boolean wrote, String text) {
        if (wrote && jlsRequestor != null) {
            TextDocumentIdentifier id = new TextDocumentIdentifier();
            id.uri = mFile.toURI();
            DidSaveTextDocumentParams p = new DidSaveTextDocumentParams();
            p.text = text;
            p.textDocument = id;
            jlsRequestor.didSave(p);
        }
    }
    
    public static interface FileOpenListener {
        public void onOpenSuccessful(File file, String text);
    }
}
