package com.itsaky.androidide.models.project;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a Gradle task
 */
public class IDETask {
    
    /**
     * Name of this task
     */
    @SerializedName("name")
    public String name;
    
    /**
     * Description of this task
     */
    @SerializedName("description")
    public String description;
    
    /**
     * Name of the group that this task belongs to
     */
    @SerializedName("group")
    public String group;
    
    
    /**
     * Path of this task
     * 
     * For example, ':app:assembleDebug', ':app:build'
     */
    @SerializedName("path")
    public String path;
    
}
