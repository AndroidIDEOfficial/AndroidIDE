package com.itsaky.lsp;

import java.util.List;

public class Hover {
    public List<MarkedString> contents;
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
