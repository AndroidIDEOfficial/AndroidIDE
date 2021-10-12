package com.itsaky.androidide.language.xml;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.xml.completion.XMLCompletionService;
import com.itsaky.androidide.utils.Logger;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.eclipse.lsp4j.CompletionItem;

public class XMLAutoComplete implements AutoCompleteProvider {
    
    private static final Logger LOG = Logger.instance("XMLAutoComplete");
    
	private Comparator<CompletionItem> RESULTS_SORTER = new Comparator<CompletionItem>(){

		@Override
		public int compare(CompletionItem p1, CompletionItem p2) {
            if(p1.getSortText() == null && p2.getSortText() != null) return -1;
            if(p1.getSortText() != null && p2.getSortText() == null) return 1;
            if(p1.getSortText() == null && p2.getSortText() == null) return 0;
			return p1.getSortText().compareTo(p2.getSortText());
		}
	};
    
	@Override
	public List<CompletionItem> getAutoCompleteItems(CharSequence content, String fileUri, String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int index, int line, int column) {
		final XMLCompletionService service = StudioApp.getInstance().getXmlCompletionService();
		return sort(service.complete(content, index, line, column, prefix.toLowerCase(Locale.US).trim()));
	}
	
	private List<CompletionItem> sort(List<CompletionItem> result) {
		Collections.sort(result, RESULTS_SORTER);
		return result;
	}
}
