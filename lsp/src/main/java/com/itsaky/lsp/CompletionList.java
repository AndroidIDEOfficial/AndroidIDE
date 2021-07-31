package com.itsaky.lsp;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CompletionList {
    public boolean isIncomplete;
    public List<CompletionItem> items;

    public CompletionList() {
        this.items = new ArrayList<>();
    }

    public CompletionList(boolean isIncomplete, List<CompletionItem> items) {
        this.isIncomplete = isIncomplete;
        this.items = items;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this, CompletionList.class);
    }
}
