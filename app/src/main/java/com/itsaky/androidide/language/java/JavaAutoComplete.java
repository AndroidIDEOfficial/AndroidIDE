package com.itsaky.androidide.language.java;

import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.services.IDELanguageServer;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import java.util.ArrayList;
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

public class JavaAutoComplete implements AutoCompleteProvider {
    
    private CompletableFuture<Either<List<CompletionItem>, CompletionList>> future;

	@Override
	public List<CompletionItem> getAutoCompleteItems(String fileUri, String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int index, int line, int column) throws Exception {
        IDELanguageServer languageServer = LSPProvider.getServerForLanguage(LSPProvider.LANGUAGE_JAVA);
        if(languageServer != null && fileUri != null) {
            
            if(future != null && !future.isDone()) future.cancel(true);
            
            CompletionParams params = new CompletionParams();
            params.setPosition(new Position(line, column));
            params.setTextDocument(new TextDocumentIdentifier(fileUri));
            future = languageServer.getTextDocumentService().completion(params);
            
            if(future.isCancelled()) return new ArrayList<CompletionItem>();
            
            try {
                Either<List<CompletionItem>, CompletionList> either = future.get();
                if(either.isLeft())
                    return sort(either.getLeft());
                
                if(either.isRight())
                    return sort(either.getRight().getItems());
                
            } catch (Throwable th) {
                // Ignore this exception. An empty list will be sent after this
                // This exeption is thrown probably because the request was cancelled or interrupted
            }
        }
        return new ArrayList<CompletionItem>();
	}
    
    private List<CompletionItem> sort(List<CompletionItem> items) {
        Collections.sort(items, RESULT_SORTER);
        return items;
    }
    
    private static final Comparator<CompletionItem> RESULT_SORTER = new Comparator<CompletionItem>(){
        
        @Override
        public int compare(CompletionItem p1, CompletionItem p2) {
            String s1 = p1.getSortText() == null ? p1.getLabel() : p1.getSortText();
            String s2 = p2.getSortText() == null ? p2.getLabel() : p2.getSortText();
            return s1.compareTo(s2);
        }
    };
    
    private static final Logger LOG = Logger.instance("JavaAutoComplete");
}
