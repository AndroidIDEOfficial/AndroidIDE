package com.itsaky.lsp;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class TextEdit {
    
    @SerializedName("range")
    public Range range;
    
    @SerializedName("newText")
    public String newText;

    public TextEdit() {}

    public TextEdit(Range range, String newText) {
        this.range = range;
        this.newText = newText;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public static final TextEdit NONE = new TextEdit(Range.NONE, "");
}
