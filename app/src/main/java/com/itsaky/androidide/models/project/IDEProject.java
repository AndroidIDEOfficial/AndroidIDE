package com.itsaky.androidide.models.project;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

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
     * Path of the directory containing this project
     */
    @SerializedName("projectDir")
    public String projectDir;
    
    /**
     * File path of the application's icon
     * We get this manually by parsing AndroidManifest.xml
     */
    public String iconPath;
    
    /**
     * Subprojects of this project
     */
    @SerializedName("modules")
    public List<IDEModule> modules = new ArrayList<>();
    
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
    
    /**
     * Get the module by its path
     *
     * @param path Path of the module
     * @return An Optional containing the IDEModule or Optional.empty()
     */
    public Optional<IDEModule> getModuleByPath(String path) {
        if(modules != null && modules.size() > 0) {
            for(IDEModule module : modules) {
                if(module == null) continue;
                if(module.path.trim().equals(path.trim()))
                    return Optional.of(module);
            }
        }
        return Optional.empty();
    }
    
    
    /**
     * Stores information about a SDK. Like,
     * • Codename
     * • API string
     * • API level
     */
    public static class SDK {

        @SerializedName("codename")
        public String codename;


        @SerializedName("apiString")
        public String apiString;

        @SerializedName("apiLevel")
        public int apiLevel;
    }
}
