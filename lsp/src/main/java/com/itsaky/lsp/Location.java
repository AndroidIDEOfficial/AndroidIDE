package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.net.URI;

public class Location {
    @SerializedName("uri")
    public URI uri;
    
    @SerializedName("range") 
    public Range range;

    public Location() {}

    public Location(URI uri, Range range) {
        this.uri = uri;
        this.range = range;
    }

    public static final Location NONE = new Location(null, Range.NONE);
}
