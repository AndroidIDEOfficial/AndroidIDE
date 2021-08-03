package com.itsaky.lsp;
import com.google.gson.annotations.SerializedName;
import java.net.URI;
import java.util.List;

public class JavaColors {
    
    @SerializedName("uri")
    public URI uri;
    
    @SerializedName("statics")
    public List<Range> statics;
    
    @SerializedName("fields")
    public List<Range> fields;
    
    @SerializedName("classNames")
    public List<Range> classNames;
    
    @SerializedName("packages")
    public List<Range> packages;
}
