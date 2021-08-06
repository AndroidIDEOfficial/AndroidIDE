package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class ParameterInformation {
    @SerializedName("label")
    public String label;
    
    @SerializedName("documentation")
    public MarkupContent documentation;
}
