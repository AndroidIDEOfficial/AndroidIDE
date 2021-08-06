package com.itsaky.lsp;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * A class which stores the syntax highlighting colors obtained from JLS
 */
public class JavaColors {
    
    @SerializedName("uri")
    public URI uri;
    
    @SerializedName("packages")
    public List<Range> packages;
    
    @SerializedName("enumTypes")
    public List<Range> enumTypes;
    
    @SerializedName("classNames")
    public List<Range> classNames;
    
    @SerializedName("annotationTypes")
    public List<Range> annotationTypes;
    
    @SerializedName("interfaces")
    public List<Range> interfaces;
    
    @SerializedName("enums")
    public List<Range> enums;
    
    @SerializedName("statics")
    public List<Range> statics;
    
    @SerializedName("fields")
    public List<Range> fields;
    
    @SerializedName("parameters")
    public List<Range> parameters;
    
    @SerializedName("locals")
    public List<Range> locals;
    
    @SerializedName("exceptionParams")
    public List<Range> exceptionParams;
    
    @SerializedName("methods")
    public List<Range> methods;
    
    @SerializedName("constructors")
    public List<Range> constructors;
    
    @SerializedName("staticInits")
    public List<Range> staticInits;
    
    @SerializedName("instanceInits")
    public List<Range> instanceInits;
    
    @SerializedName("typeParams")
    public List<Range> typeParams;
    
    @SerializedName("resourceVariables")
    public List<Range> resourceVariables;

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
