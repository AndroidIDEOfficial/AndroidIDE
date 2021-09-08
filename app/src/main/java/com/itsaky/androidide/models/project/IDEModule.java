package com.itsaky.androidide.models.project;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a module (subproject) of a root project.
 *
 * In Android projects, a module can either be an application project,
 * or a library project. Application projects are represented by IDEAppModule
 */
public class IDEModule extends IDEProject {
    
    /**
     * Build tools version of this project
     */
    @SerializedName("buildToolsVersion")
    public String buildToolsVersion;
    
    /**
     * Compile SDK version of this project
     *
     * Representation: 'android-31', 'android-30', 'android-29', etc
     */
    @SerializedName("compileSdkVersion")
    public String compileSdkVersion;
    
    /**
     * Minimum SDK version of this project
     */
    @SerializedName("minSdk")
    public SDK minSdk;
    
    /**
     * Target SDK version of this project
     */
    @SerializedName("targetSdk")
    public SDK targetSdk;
    
    /**
     * Version code of this project
     */
    @SerializedName("versionCode")
    public int versionCode = 0;
    
    /**
     * Version name of this project
     */
    @SerializedName("versionName")
    public String versionName = "Not defined";
    
    /**
     * Is this project an Android library project?
     */
    @SerializedName("isLibrary")
    public boolean isLibrary = true;
}
