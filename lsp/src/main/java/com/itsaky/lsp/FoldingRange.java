package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class FoldingRange {
    @SerializedName("startLine")
    public int startLine;
    
    @SerializedName("startCharacter")
    public int startCharacter;
    
    @SerializedName("endLine")
    public int endLine;
    
    @SerializedName("endCharacter")
    public int endCharacter;
    
    @SerializedName("kind")
    public String kind;

    public FoldingRange() {}

    public FoldingRange(int startLine, int startCharacter, int endLine, int endCharacter, String kind) {
        this.startLine = startLine;
        this.startCharacter = startCharacter;
        this.endLine = endLine;
        this.endCharacter = endCharacter;
        this.kind = kind;
    }
}
