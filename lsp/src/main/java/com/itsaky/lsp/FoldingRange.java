package com.itsaky.lsp;

public class FoldingRange {
    public int startLine, startCharacter, endLine, endCharacter;
    public String kind; // FoldingRangeKind

    public FoldingRange() {}

    public FoldingRange(int startLine, int startCharacter, int endLine, int endCharacter, String kind) {
        this.startLine = startLine;
        this.startCharacter = startCharacter;
        this.endLine = endLine;
        this.endCharacter = endCharacter;
        this.kind = kind;
    }
}
