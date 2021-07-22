package com.itsaky.androidide.language.java;

import android.text.TextUtils;
import androidx.core.util.Pair;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.java.JavaLanguageAnalyzer;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.itsaky.androidide.utils.Either;
import com.itsaky.androidide.utils.EitherOfResult;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.struct.CompletionItem;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.ArrayList;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class JavaAutoComplete implements AutoCompleteProvider {

	private CodeEditor editor;
	private JavaLanguageAnalyzer analyzer;

	public JavaAutoComplete(CodeEditor editor, JavaLanguageAnalyzer analyzer) {
		this.editor = editor;
		this.analyzer = analyzer;
	}

	@Override
	public List<Either<SuggestItem, CompletionItem>> getAutoCompleteItems(String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int line) {
		Pair<ArrayList<SuggestItem>, List<Diagnostic<? extends JavaFileObject>>> pair = StudioApp.getInstance().getCompletionProvider().getSuggestions(editor);
		final ArrayList<SuggestItem> suggestions = pair.first;
		final List<Diagnostic<? extends JavaFileObject>> diags = pair.second;
		if(suggestions != null) {
			List<Either<SuggestItem, CompletionItem>> result = new ArrayList<>();
			for(SuggestItem item : suggestions) {
				result.add(new EitherOfResult(item, null));
			}
			return result;
		}
        return new ArrayList<Either<SuggestItem, CompletionItem>>();
	}
}
