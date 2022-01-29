/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.language;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.api.ICompletionProvider;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.CompletionItem;
import com.itsaky.lsp.models.CompletionParams;
import com.itsaky.lsp.models.CompletionResult;
import com.itsaky.lsp.models.Position;

import org.jetbrains.annotations.Contract;

import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;

import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;

/**
 * Common implementation of completion provider which requests completions
 * to provided language server.
 *
 * @author Akash Yadav
 */
public class CommonCompletionProvider implements AutoCompleteProvider {
    
    private CompletableFuture<CompletionResult> future;
    
    private final ILanguageServer server;
    
    public CommonCompletionProvider (ILanguageServer server) {
        this.server = server;
    }
    
    @Override
    public List<CompletionItem> getAutoCompleteItems(Content content, String fileUri, String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int index, int line, int column) throws Exception {
        if (this.future != null && !this.future.isDone ()) {
            try {
                this.future.cancel (true);
            } catch (CancellationException e) {
                return new ArrayList<> ();
            }
        }
        
        this.future = CompletableFuture.supplyAsync (() -> {
            final var completer = server.getCompletionProvider ();
            final var path = Paths.get (URI.create (fileUri));
            
            if (!completer.canComplete (path)) {
                return ICompletionProvider.EMPTY;
            }
            
            final var params = new CompletionParams (new Position (line, column, index), path);
            params.setContent (content);
            params.setPrefix (prefix);
            return completer.complete (params);
        });
        
        return finalizeResults (future.get ().getItems ());
    }
    
    @NonNull
    @Contract("_ -> param1")
    private List<CompletionItem> finalizeResults(@NonNull List<CompletionItem> items) {
        items.sort (RESULT_SORTER);
        return items;
    }
    
    private static final Comparator<CompletionItem> RESULT_SORTER = (p1, p2) -> {
        if (p1 == null && p2 == null) {
            return 0;
        } else if (p1 == null) {
            return -1;
        } else if (p2 == null) {
            return 1;
        }
        
        String s1 = p1.getSortText () == null ? p1.getLabel () : p1.getSortText ();
        String s2 = p2.getSortText () == null ? p2.getLabel () : p2.getSortText ();
        return s1.compareTo (s2);
    };
    
    private static final Logger LOG = Logger.instance("CommonCompletionProvider");
}
