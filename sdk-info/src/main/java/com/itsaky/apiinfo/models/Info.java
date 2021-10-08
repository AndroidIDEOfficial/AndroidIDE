package com.itsaky.apiinfo.models;

/**
 * Base class for information about a class, method, field
 */
public abstract class Info {
    
    /**
     * Name of this class, method or field
     */
    public String name = "";
    
    /**
     * API level in which this info was deprecated
     */
    public int deprecated = -1;
    
    /**
     * API level in which this was added
     */
    public int since = -1;
    
    /**
     * API level in which this was removed
     */
    public int removed = -1;
}
