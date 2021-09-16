package com.itsaky.apiinfo.models;

import java.util.List;
import java.util.ArrayList;

/**
 * Holds information about a method
 */
public class MethodInfo extends Info {
    
    /**
     * In case of a method, the {@link com.itsaky.apiinfo.models.Info#name} contains signature of the method
     * This field contains the actual simple name
     */
    public String simpleName;
    
    /**
     * Parameter types of this method
     */
    public final List<String> parameters = new ArrayList<>();
}
