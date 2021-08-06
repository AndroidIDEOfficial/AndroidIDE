package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class ShowMessageParams {
    
    @SerializedName("type")
    public int type;
    
    @SerializedName("message")
    public String message;
}
