/************************************************************************************
 * This file is part of AndroidIDE.
 * 
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/
package com.itsaky.androidide.language.java;

import androidx.annotation.NonNull;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.models.CompletionResult;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.jetbrains.annotations.Contract;

import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.text.TextAnalyzeResult;

public class JavaAutoComplete implements AutoCompleteProvider {
    
    private CompletableFuture<CompletionResult> future;

	@Override
	public List<CompletionItem> getAutoCompleteItems(CharSequence content, String fileUri, String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int index, int line, int column) throws Exception {
		if (this.future != null && !this.future.isDone ()) {
			try {
				this.future.cancel (true);
			} catch (java.util.concurrent.CancellationException e) {
				// Do not pollute logs with cancellation exceptions
				return new ArrayList<> ();
			}
		}
		
		this.future = CompletableFuture.supplyAsync (() -> {
			final var server = StudioApp.getInstance ().getJavaLanguageServer ();
			final var completer = server.getCompletionProvider ();
			final var params = new com.itsaky.lsp.models.CompletionParams (new com.itsaky.lsp.models.Position (line, column), Paths.get (URI.create (fileUri)));
			return completer.complete (params);
		});
		
		final var result = future.get ();
		final var list = new ArrayList<CompletionItem> ();
		for (final var item : result.getItems ()) {
			final var i = new CompletionItem ();
			i.setLabel (item.getLabel ());
			i.setDetail (item.getDetail ());
			i.setKind (CompletionItemKind.Method);
			i.setSortText (item.getSortText ());
			i.setInsertText (item.getInsertText ());
			list.add (i);
		}
		
		return finalizeResults (list);
	}
    
    @NonNull
	@Contract("_ -> param1")
	private List<CompletionItem> finalizeResults(@NonNull List<CompletionItem> items) {
        items.sort (RESULT_SORTER);
        return items;
    }
    
    private static final Comparator<CompletionItem> RESULT_SORTER = (p1, p2) -> {
		String s1 = p1.getSortText () == null ? p1.getLabel () : p1.getSortText ();
		String s2 = p2.getSortText () == null ? p2.getLabel () : p2.getSortText ();
		return s1.compareTo (s2);
	};
    
    private static final Logger LOG = Logger.instance("JavaAutoComplete");
}
