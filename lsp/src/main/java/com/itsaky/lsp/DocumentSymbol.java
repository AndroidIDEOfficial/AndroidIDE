package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DocumentSymbol {
    @SerializedName("name")
    public String name;
    
    @SerializedName("detail")
    public String detail;
    
    @SerializedName("kind")
    public int kind;
    
    @SerializedName("deprecated")
    public boolean deprecated;
    
    @SerializedName("range")
    public Range range;
    
    @SerializedName("selectionRange")
    public Range selectionRange;
    
    @SerializedName("children")
    public List<DocumentSymbol> children;
}
