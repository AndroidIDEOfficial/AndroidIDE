package com.itsaky.lsp;

public class FormattingOptions {
    public int tabSize;
    public boolean insertSpaces;

    public FormattingOptions(){}
   
    public FormattingOptions(int tabSize, boolean insertSpaces) {
        this.tabSize = tabSize;
        this.insertSpaces = insertSpaces;
    }
}
