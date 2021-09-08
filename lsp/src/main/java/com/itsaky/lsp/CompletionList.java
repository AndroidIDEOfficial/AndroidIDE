package com.itsaky.lsp;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class CompletionList {
    
    @SerializedName("isIncomplete")
    public boolean isIncomplete;
    
    @SerializedName("items")
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
