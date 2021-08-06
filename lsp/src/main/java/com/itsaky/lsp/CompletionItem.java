package com.itsaky.lsp;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CompletionItem {
    @SerializedName("label")
    public String label;
    
    @SerializedName("kind")
    public int kind;
    
    @SerializedName("detail")
    public String detail;
    
    @SerializedName("documentation")
    public MarkupContent documentation;
    
    @SerializedName("deprecated")
    public Boolean deprecated;
    
    @SerializedName("preselect")
    public Boolean preselect;
    
    @SerializedName("sortText")
    public String sortText;
    
    @SerializedName("filterText")
    public String filterText;
    
    @SerializedName("insertText")
    public String insertText;
    
    @SerializedName("insertTextFormat")
    public Integer insertTextFormat;
    
    @SerializedName("textEdit")
    public TextEdit textEdit;
    
    @SerializedName("additionalTextEdits")
    public List<TextEdit> additionalTextEdits;
    
    @SerializedName("commitCharacters")
    public List<Character> commitCharacters;
    
    @SerializedName("command")
    public Command command;
    
    @SerializedName("data")
    public JsonElement data;
}
