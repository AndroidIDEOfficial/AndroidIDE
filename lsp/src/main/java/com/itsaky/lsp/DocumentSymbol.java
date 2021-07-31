package com.itsaky.lsp;

import java.util.List;

public class DocumentSymbol {
    public String name, detail;
    public int kind;
    public boolean deprecated;
    public Range range, selectionRange;
    public List<DocumentSymbol> children;
}
