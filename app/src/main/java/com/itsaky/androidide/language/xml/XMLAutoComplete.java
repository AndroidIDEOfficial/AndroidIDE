package com.itsaky.androidide.language.xml;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.xml.completion.XMLCompletionService;
import com.itsaky.androidide.models.CompletionListItem;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.struct.CompletionItem;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import com.itsaky.androidide.utils.Either;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import java.util.stream.Collectors;
import com.itsaky.androidide.utils.EitherOfResult;

public class XMLAutoComplete implements AutoCompleteProvider {
	
	private CodeEditor editor;
	
	private Comparator<CompletionItem> RESULTS_SORTER = new Comparator<CompletionItem>(){

		@Override
		public int compare(CompletionItem p1, CompletionItem p2) {
			return ((CompletionListItem) p1).getSortText().compareTo(((CompletionListItem) p2).getSortText());
		}
	};

	public XMLAutoComplete(CodeEditor editor) {
		this.editor = editor;
	}
	
	@Override
	public List<Either<SuggestItem, CompletionItem>> getAutoCompleteItems(String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int line) {
		// We do not store the completion service here in this class
		// This is because, if we modify the instance of the service in app,
		// it'll be updated here
		// automatically
		final XMLCompletionService service = StudioApp.getInstance().getXmlCompletionService();
		List<CompletionItem> list = new ArrayList<CompletionItem>(sort(service.complete(editor, prefix.trim())));
		List<Either<SuggestItem, CompletionItem>> result = new ArrayList<>();
		for(CompletionItem item : list) {
			result.add(new EitherOfResult(null, item));
		}
		return result;
	}
	
	private List<CompletionListItem> sort(List<CompletionListItem> result) {
		Collections.sort(result, RESULTS_SORTER);
		return result;
	}
}
