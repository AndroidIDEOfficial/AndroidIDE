package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Diagnostic {
    @SerializedName("range")
    public Range range;
    
    @SerializedName("severity")
    public Integer severity;
    
    @SerializedName("code")
    public String code;
    
    @SerializedName("source")
    public String source;
    
    @SerializedName("message")
    public String message;
    
    @SerializedName("tags")
    public List<Integer> tags;
    
    @SerializedName("codeActions")
    public List<CodeAction> codeActions = new ArrayList<>();
}
