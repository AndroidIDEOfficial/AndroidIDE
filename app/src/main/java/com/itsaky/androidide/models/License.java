package com.itsaky.androidide.models;

import com.google.gson.annotations.SerializedName;

public class License {
    @SerializedName("name")
    public String name;
    
    @SerializedName("license")
    public String license;
    
    @SerializedName("url")
    public String url;
}
