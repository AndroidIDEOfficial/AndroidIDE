package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class RenameResponse {
    
    @SerializedName("range")
    public Range range;
    
    @SerializedName("placeholder")
    public String placeholder;
}
