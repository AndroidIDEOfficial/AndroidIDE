package com.itsaky.androidide.language.buildout;

import com.itsaky.androidide.language.BaseLanguage;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import io.github.rosemoe.editor.widget.EditorColorScheme;
import java.util.ArrayList;
import java.util.List;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.widget.SymbolPairMatch;
import io.github.rosemoe.editor.langs.EmptyLanguage;
import org.eclipse.lsp4j.services.LanguageServer;

public class BuildOutputLanguage extends BaseLanguage {

    @Override
    public AutoCompleteProvider getAutoCompleteProvider() {
        return new EmptyLanguage.EmptyAutoCompleteProvider();
    }

    @Override
    public LanguageServer getLanguageServer() {
        return null;
    }

    @Override
    public String getLanguageCode() {
        return null;
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
    
    
    private final BuildAnalyzer analyzer = new BuildAnalyzer();
    
    @Override
    public CodeAnalyzer getAnalyzer() {
        return analyzer;
    }
    
    public void addError(int line) {
        analyzer.addError(line);
    }
    
    public void addSuccess(int line) {
        analyzer.addSuccess(line);
    }
    
    private class BuildAnalyzer implements CodeAnalyzer {
        
        private final List<Integer> errorMap = new ArrayList<>();
        private final List<Integer> successMap = new ArrayList<>();
        
        public void addError(int line) {
            errorMap.add(line);
        }
        
        public void addSuccess(int line) {
            successMap.add(line);
        }
        
        @Override
        public void analyze(CharSequence content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) {
            int line = 0;
            int index = 0;
            colors.addNormalIfNull();
            while(index < content.length() && delegate.shouldAnalyze()) {
                int id = EditorColorScheme.TEXT_NORMAL;
                if(errorMap != null && errorMap.contains(line))
                    id = EditorColorScheme.STDERR;
                else if(successMap != null && successMap.contains(line))
                    id = EditorColorScheme.STDOUT;
                colors.addIfNeeded(line, 0, id);
                char c = content.charAt(index);
                if(c == '\n') {
                    line++;
                }
                index++;
            }
        }
    }
}
