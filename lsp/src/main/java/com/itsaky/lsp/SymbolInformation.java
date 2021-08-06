package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class SymbolInformation {
    
    @SerializedName("name")
    public String name;
    
    @SerializedName("kind")
    public int kind;
    
    @SerializedName("deprecated")
    public boolean deprecated;
    
    @SerializedName("location")
    public Location location;
    
    @SerializedName("containerName")
    public String containerName;
}
