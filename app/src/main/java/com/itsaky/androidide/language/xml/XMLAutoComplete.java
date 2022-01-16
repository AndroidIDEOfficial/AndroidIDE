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
package com.itsaky.androidide.language.xml;

import androidx.annotation.NonNull;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.xml.completion.XMLCompletionService;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.models.CompletionItem;

import org.jetbrains.annotations.Contract;

import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class XMLAutoComplete implements AutoCompleteProvider {
    
    private static final Logger LOG = Logger.instance ("XMLAutoComplete");
    
    private final Comparator<CompletionItem> RESULTS_SORTER = (p1, p2) -> {
		if (p1.getSortText () == null && p2.getSortText () != null) {
			return -1;
		}
		
		if (p1.getSortText () != null && p2.getSortText () == null) {
			return 1;
		}
		
		if (p1.getSortText () == null && p2.getSortText () == null) {
			return 0;
		}
		
        return p1.getSortText ().compareTo (p2.getSortText ());
    };
    
    @Override
    public List<CompletionItem> getAutoCompleteItems (Content content, String fileUri, String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int index, int line, int column) {
        final XMLCompletionService service = StudioApp.getInstance ().getXmlCompletionService ();
        return sort (service.complete (content, index, prefix.toLowerCase (Locale.US).trim ()));
    }
    
    @NonNull
    @Contract("_ -> param1")
    private List<CompletionItem> sort (@NonNull List<CompletionItem> result) {
        result.sort (RESULTS_SORTER);
        return result;
    }
}
