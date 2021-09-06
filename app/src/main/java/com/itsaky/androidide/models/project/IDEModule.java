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
    int buildToolsVersion = 0;
    
    /**
     * Compile SDK version of this project
     */
    @SerializedName("compileSdkVersion")
    int compileSdkVersion = 0;
    
    /**
     * Minimum SDK version of this project
     */
    @SerializedName("minSdk")
    int minSdk = 0;
    
    /**
     * Target SDK version of this project
     */
    @SerializedName("targetSdk")
    int targetSdk = 0;
    
    /**
     * Version code of this project
     */
    @SerializedName("versionCode")
    int versionCode = 0;
    
    /**
     * Version name of this project
     */
    @SerializedName("versionName")
    String versionName = "Not defined";
    
    /**
     * Is this project an Android library project?
     */
    @SerializedName("isLibrary")
    boolean isLibrary = true;
}
