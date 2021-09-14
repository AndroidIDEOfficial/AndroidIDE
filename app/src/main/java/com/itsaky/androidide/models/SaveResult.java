package com.itsaky.androidide.models;

/**
 * Result obtained when files are saved
 */
public final class SaveResult {
    
    /**
     * Were any Gradle files saved?
     */
    public boolean gradleSaved = false;
    
    /**
     * Were any XML files saved?
     */
    public boolean xmlSaved = false;
    
    public SaveResult() {}

    public SaveResult(boolean gradleSaved, boolean xmlSaved) {
        this.gradleSaved = gradleSaved;
        this.xmlSaved = xmlSaved;
    }
}
