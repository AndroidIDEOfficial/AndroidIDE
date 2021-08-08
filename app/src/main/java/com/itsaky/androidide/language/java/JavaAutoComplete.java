package com.itsaky.androidide.language.java;

import androidx.core.util.Pair;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.java.JavaLanguageAnalyzer;
import com.itsaky.androidide.language.java.server.JavaLanguageServer;
import com.itsaky.androidide.models.CompletionItemWrapper;
import com.itsaky.androidide.models.SuggestItem;
import com.itsaky.androidide.utils.Either;
import com.itsaky.lsp.CompletionList;
import com.itsaky.lsp.Position;
import com.itsaky.lsp.TextDocumentIdentifier;
import com.itsaky.lsp.TextDocumentPositionParams;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.struct.CompletionItem;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class JavaAutoComplete implements AutoCompleteProvider {
    
	private JavaLanguageAnalyzer analyzer;
    private int lastId = -1;
    
	public JavaAutoComplete(JavaLanguageAnalyzer analyzer) {
		this.analyzer = analyzer;
	}

	@Override
	public List<Either<SuggestItem, CompletionItem>> getAutoCompleteItems(CodeEditor editor, String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int line) {
        final JavaLanguageServer server = StudioApp.getInstance().getJavaLanguageServer();
        if(server != null) {
            if(lastId != -1) server.cancelRequest(lastId);
            TextDocumentIdentifier id = new TextDocumentIdentifier();
            id.uri = editor.getFile().toURI();
            TextDocumentPositionParams p = new TextDocumentPositionParams();
            p.position = new Position(line, editor.getCursor().getLeftColumn());
            p.textDocument = id;
            Pair<Integer, CompletionList> response = server.completion(p);
            lastId = response.first;
            CompletionList list = response.second;
            if(list != null && list.items != null) {
                List<Either<SuggestItem, CompletionItem>> result = new ArrayList<>();
                for(int i=0;i<list.items.size();i++) {
                    com.itsaky.lsp.CompletionItem item = list.items.get(i);
                    if(item == null) continue;
                    Either<SuggestItem, CompletionItem> either = Either.forLeft(new CompletionItemWrapper(item, prefix));
                    result.add(either);
                }
                Collections.sort(result, RESULT_SORTER);
                return result;
            }
        }
        return new ArrayList<Either<SuggestItem, CompletionItem>>();
	}
    
    private static final Comparator<Either<SuggestItem, CompletionItem>> RESULT_SORTER = new Comparator<Either<SuggestItem, CompletionItem>>(){

        @Override
        public int compare(Either<SuggestItem, CompletionItem> p1, Either<SuggestItem, CompletionItem> p2) {
            return p1.getLeft().getSortText().compareTo(p2.getLeft().getSortText());
        }
    };
}
