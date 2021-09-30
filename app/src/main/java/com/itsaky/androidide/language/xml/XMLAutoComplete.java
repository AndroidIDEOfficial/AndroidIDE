package com.itsaky.androidide.language.xml;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.xml.completion.XMLCompletionService;
import com.itsaky.androidide.models.CompletionListItem;
import com.itsaky.androidide.models.SuggestItem;
import com.itsaky.androidide.utils.EitherOfResult;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public class XMLAutoComplete implements AutoCompleteProvider {
	
	private Comparator<CompletionItem> RESULTS_SORTER = new Comparator<CompletionItem>(){

		@Override
		public int compare(CompletionItem p1, CompletionItem p2) {
			return p1.getSortText().compareTo(p2.getSortText());
		}
	};
	
    /**
     * TODO: Change this to a language server implementation and remove the use of passing editor content
     */
	@Override
	public List<CompletionItem> getAutoCompleteItems(String fileUri, String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int index, int line, int column) {
		final XMLCompletionService service = StudioApp.getInstance().getXmlCompletionService();
		return sort(service.complete(index, line, column, prefix.toLowerCase().trim()));
	}
	
	private List<CompletionItem> sort(List<CompletionItem> result) {
		Collections.sort(result, RESULTS_SORTER);
		return result;
	}
}
