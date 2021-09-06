package com.itsaky.androidide.models.project;
import com.google.gson.annotations.SerializedName;

/**
 * Represents an Android application project
 */
public class IDEAppModule extends IDEModule {
    
    /**
     * Library projects don't have applicationId, application projects do.
     */
     
    @SerializedName("applicationId")
    public String applicationId;
    
    public IDEAppModule () {
        isLibrary = false;
    }
}
