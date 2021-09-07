package com.itsaky.androidide.models.project;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.ArrayList;

/**
 * A model that contains data of a project currently opened in AndroidIDE
 */
public class IDEProject {
    
    /**
     * Name of this project
     */
    @SerializedName("name")
    public String name = "";
    
    /**
     * Name of this project which should be displayed to user
     */
    @SerializedName("displayName")
    public String displayName = "";
    
    /**
     * Description of this project. Maybe null.
     */
    @SerializedName("description")
    public String description = "";
    
    /**
     * Path of this project. This is NOT the directory path of this project.
     * 
     * For example, ':app or ':module:subModule'
     */
    @SerializedName("path")
    public String path = "";
    
    /**
     * Subprojects of this project
     */
    @SerializedName("modules")
    public List<IDEProject> modules = new ArrayList<>();
    
    /**
     * Tasks included in this project
     */
    @SerializedName("tasks")
    public List<IDETask> tasks = new ArrayList<>();
    
    /**
     * Path of jar file of dependencies of this project
     */
    @SerializedName("dependencies")
    public List<String> dependencies = new ArrayList<>();
}
