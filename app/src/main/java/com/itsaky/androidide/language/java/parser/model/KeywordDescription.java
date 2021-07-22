package com.itsaky.androidide.language.java.parser.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.github.rosemoe.editor.widget.CodeEditor;

public class KeywordDescription extends JavaSuggestItemImpl {
    private final String keyword;

    public KeywordDescription(String keyword) {
        this.keyword = keyword;
    }

    @Nullable
    @Override
    public String getName() {
        return keyword;
    }

    @Nullable
    @Override
    public String getDescription() {
        return null;
    }

    @Nullable
    @Override
    public String getReturnType() {
        return null;
    }

    @Override
    public char getTypeHeader() {
        return 'k';
    }

    @Override
    public void onSelectThis(@NonNull CodeEditor iEditAreaView) {
        insertImpl(iEditAreaView, keyword + " ");
    }
}
