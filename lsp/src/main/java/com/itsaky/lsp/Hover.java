package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Hover {
    @SerializedName("contents")
    public List<MarkedString> contents;
    
    @SerializedName("range")
    public Range range;

    public Hover() {}

    public Hover(List<MarkedString> contents) {
        this.contents = contents;
    }

    public Hover(List<MarkedString> contents, Range range) {
        this.contents = contents;
        this.range = range;
    }
}
