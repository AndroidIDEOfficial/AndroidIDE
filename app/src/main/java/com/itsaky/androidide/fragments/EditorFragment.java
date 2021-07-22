package com.itsaky.androidide.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.itsaky.androidide.adapters.CompletionListAdapter;
import com.itsaky.androidide.databinding.FragmentEditorBinding;
import com.itsaky.androidide.fragments.preferences.EditorPreferences;
import com.itsaky.androidide.language.groovy.GroovyLanguage;
import com.itsaky.androidide.language.java.JavaLanguage;
import com.itsaky.androidide.language.xml.XMLLanguage;
import com.itsaky.androidide.language.xml.lexer.XMLLexer;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.services.compiler.model.CompilerDiagnostic;
import com.itsaky.androidide.syntax.colorschemes.SchemeWombat;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.ReadFileTask;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.PreferenceManager;
import com.itsaky.androidide.utils.TypefaceUtils;
import io.github.rosemoe.editor.interfaces.EditorEventListener;
import io.github.rosemoe.editor.langs.EmptyLanguage;
import io.github.rosemoe.editor.widget.CodeEditor;
import io.github.rosemoe.editor.widget.schemes.SchemeVS2019;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import com.elvishew.xlog.XLog;
import android.text.TextUtils;
import com.itsaky.androidide.services.compiler.JavaCompilerService;

public class EditorFragment extends BaseFragment implements EditorEventListener {
	
	public FragmentEditorBinding binding;
	private File mFile;
	private boolean isRead = false;
	private boolean isModified = false;
	private boolean isFirstCreate = false;
	
	private static AndroidProject project;
	
	private List<CompilerDiagnostic> diags;
	
	public static final String KEY_FILE_PATH = "file_path";
	public static final String KEY_PROJECT = "project";
	public static final String EXT_JAVA = ".java";
	public static final String EXT_XML = ".xml";
	public static final String EXT_HTML = ".html";
	public static final String EXT_GRADLE = ".gradle";
	public static final String EXT_GROOVY = ".groovy";
	public static final String EXT_KOTLIN = ".kt";
	public static final String EXT_JSON = ".json";

	public static EditorFragment newInstance(File file, AndroidProject project) {
		Bundle bundle = new Bundle();
		bundle.putString(KEY_FILE_PATH, file.getAbsolutePath());
		bundle.putParcelable(KEY_PROJECT, project);
		EditorFragment frag = new EditorFragment();
		frag.setArguments(bundle);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFirstCreate = true;
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
		
		binding.editorCodeEditor.setOverScrollEnabled(false);
		binding.editorCodeEditor.setTypefaceText(TypefaceUtils.jetbrainsMono());
		binding.editorCodeEditor.setTextActionMode(CodeEditor.TextActionMode.ACTION_MODE);
		binding.editorCodeEditor.setHighlightCurrentBlock(true);
		binding.editorCodeEditor.setEventListener(this);
        binding.editorCodeEditor.setAutoCompletionOnComposing(true);
        binding.editorCodeEditor.setAutoCompletionItemAdapter(new CompletionListAdapter());
		binding.editorCodeEditor.setLineColorsEnabled(true);
		
		configureEditorIfNeeded();
		
		new TaskExecutor().executeAsync(new ReadFileTask(mFile), result -> {
			binding.editorCodeEditor.setText(result);
			postRead();
		});
	}
	
	public void setDiagnostics(List<CompilerDiagnostic> diags) {
		this.diags = diags;
		XLog.i("[EditorFragment] Diagnostics for file: " + getFile().getAbsolutePath()
				+ "\ndiagnostics: " + TextUtils.join("\n", diags));
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
			binding.editorCodeEditor.setTextSize(textSize);
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

			binding.editorCodeEditor.setNonPrintablePaintingFlags(flags);
			ConstantsBridge.EDITORPREF_FLAGS_CHANGED = false;
		}
		
		if(drawHexChanged) {
			binding.editorCodeEditor.setLineColorsEnabled(prefs.getBoolean(PreferenceManager.KEY_EDITOR_DRAW_HEX, true));
			ConstantsBridge.EDITORPREF_DRAW_HEX_CHANGED = false;
		}
		
		isFirstCreate = false;
	}
	
	public boolean isModified() {
		return isModified;
	}
	
	public void undo() {
		if(binding.editorCodeEditor.canUndo())
			binding.editorCodeEditor.undo();
	}
	
	public void redo() {
		if(binding.editorCodeEditor.canRedo())
			binding.editorCodeEditor.redo();
	}

	public void save() {
		FileUtil.writeFile(mFile.getAbsolutePath(), binding.editorCodeEditor.getText().toString());
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
			binding.editorCodeEditor.setEditorLanguage(new JavaLanguage(binding.editorCodeEditor, project));
			binding.editorCodeEditor.setColorScheme(new SchemeVS2019());
		} else if (mFile.isFile() && mFile.getName().endsWith(EXT_XML)) {
			binding.editorCodeEditor.setEditorLanguage(new XMLLanguage(binding.editorCodeEditor));
			binding.editorCodeEditor.setColorScheme(new SchemeWombat());
		} else if (mFile.isFile() && mFile.getName().endsWith(EXT_GRADLE)) {
			binding.editorCodeEditor.setEditorLanguage(new GroovyLanguage());
			binding.editorCodeEditor.setColorScheme(new SchemeVS2019());
		} else {
			binding.editorCodeEditor.setEditorLanguage(new EmptyLanguage());
		}
		isRead = true;
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
					}
					else if(currentNames.size() > 0)
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
				binding.editorCodeEditor.getText().insert(line, col + 2, currentNames.get(0));
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
	public void afterDelete(CodeEditor editor, CharSequence content, int startLine, int startColumn, int endLine, int endColumn, CharSequence deletedContent) {
		isModified = true;
	}

	@Override
	public void afterInsert(CodeEditor editor, CharSequence content, int startLine, int startColumn, int endLine, int endColumn, CharSequence insertedContent) {
		isModified = true;
		boolean isOpen = false;
		try {
			isOpen = editor.getText().charAt(editor.getCursor().getLeft() - 2) == '<';
		} catch (Throwable th) {}
		if(isOpen && insertedContent.toString().equals("/")) {
			closeCurrentTag(editor.getText().toString(), endLine, endColumn);
		}
		
		JavaCompilerService compiler = getStudioActivity().getApp().getJavaCompiler();
		if(compiler != null) {
			compiler.compileAll();
		}
	}

	@Override
	public void beforeReplace(CodeEditor editor, CharSequence content) {
	}
}
