package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class FormattingOptions {
    
    @SerializedName("tabSize")
    public int tabSize;
    
    @SerializedName("insertSpaces")
    public boolean insertSpaces;

    public FormattingOptions(){}
   
    public FormattingOptions(int tabSize, boolean insertSpaces) {
        this.tabSize = tabSize;
        this.insertSpaces = insertSpaces;
    }
}
