package com.itsaky.androidide.language.xml;

import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.lsp.services.IDELanguageServer;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import com.itsaky.androidide.utils.Logger;

public class XMLAutoComplete implements AutoCompleteProvider {
	
    private static final Logger LOG = Logger.instance("XMLAutoComplete");
    
	private Comparator<CompletionItem> RESULTS_SORTER = new Comparator<CompletionItem>(){

		@Override
		public int compare(CompletionItem p1, CompletionItem p2) {
            if(p1.getSortText() == null && p1.getSortText() != null) return -1;
            if(p1.getSortText() != null && p1.getSortText() == null) return 1;
            if(p1.getSortText() == null && p1.getSortText() == null) return 0;
			return p1.getSortText().compareTo(p2.getSortText());
		}
	};
    
    private CompletableFuture<Either<List<CompletionItem>, CompletionList>> lastRequest;
	
    /**
     * TODO: Change this to a language server implementation and remove the use of passing editor content
     */
	@Override
	public List<CompletionItem> getAutoCompleteItems(String fileUri, String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int index, int line, int column) {
		final IDELanguageServer server = LSPProvider.getServerForLanguage(LSPProvider.LANGUAGE_XML);
        if(server == null)
            return List.of();
         
        if(lastRequest != null && !lastRequest.isDone()) {
            lastRequest.cancel(true);
        } 
           
        final CompletionParams params = new CompletionParams();
        params.setTextDocument(new TextDocumentIdentifier(fileUri));
        params.setPosition(new Position(line, column));
        
        lastRequest = server.getTextDocumentService().completion(params);
        
        try {
            Either<List<CompletionItem>, CompletionList> result = lastRequest.get();
            if(result != null) {
                if(result.isLeft()) {
                    return sort(result.getLeft());
                } else if(result.isRight()) {
                    return sort(result.getRight().getItems());
                }
            }
        } catch (Throwable th) {
            LOG.error("XML Completion error", th);
            return List.of();
        }
        
		return List.of();
	}
	
	private List<CompletionItem> sort(List<CompletionItem> result) {
		Collections.sort(result, RESULTS_SORTER);
		return result;
	}
}
