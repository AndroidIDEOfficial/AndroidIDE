package com.itsaky.androidide.language.buildout;

import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.langs.EmptyLanguage;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import io.github.rosemoe.editor.widget.EditorColorScheme;
import java.util.ArrayList;
import java.util.List;

public class BuildOutputLanguage extends EmptyLanguage {
    
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
                    id = EditorColorScheme.BUILD_OUT_ERROR;
                else if(successMap != null && successMap.contains(line))
                    id = EditorColorScheme.BUILD_OUT_SUCCESS;
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
