/*
 *   Copyright 2020-2021 Rosemoe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.rosemoe.editor.langs;

import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.CompletionItem;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.SemanticHighlight;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.interfaces.EditorLanguage;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import io.github.rosemoe.editor.widget.SymbolPairMatch;

/**
 * Empty language without any effect
 *
 * @author Rose
 */
public class EmptyLanguage implements EditorLanguage {
    
    @Override
    public CharSequence format(CharSequence text) {
        return text;
    }

    @Override
    public SymbolPairMatch getSymbolPairs() {
        return new SymbolPairMatch.DefaultSymbolPairs();
    }

    @Override
    public NewlineHandler[] getNewlineHandlers() {
        return new NewlineHandler[0];
    }


    @Override
    public CodeAnalyzer getAnalyzer() {
        return new EmptyCodeAnalyzer();
    }

    @Override
    public AutoCompleteProvider getAutoCompleteProvider() {
        return new EmptyAutoCompleteProvider();
    }

    @Override
    public boolean isAutoCompleteChar(char ch) {
        return false;
    }

    @Override
    public int getIndentAdvance(String content) {
        return 0;
    }

    @Override
    public boolean useTab() {
        return false;
    }

    public static class EmptyAutoCompleteProvider implements AutoCompleteProvider {

        @Override
        public List<CompletionItem> getAutoCompleteItems(Content content, String fileUri, String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int index, int line, int column) {
            return new ArrayList<> ();
        }

    }

    private static class EmptyCodeAnalyzer extends io.github.rosemoe.editor.langs.AbstractCodeAnalyzer {

        @Override
        public void analyze(ILanguageServer server, File file, Content content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) {
            colors.addNormalIfNull();
        }

        @Override
        public void setSemanticHighlights(SemanticHighlight highlights) {
        }

        @Override
        public void updateDiagnostics(Map<Integer, Map<Integer, DiagnosticItem>> diagnostics) {
        }
    }
}

