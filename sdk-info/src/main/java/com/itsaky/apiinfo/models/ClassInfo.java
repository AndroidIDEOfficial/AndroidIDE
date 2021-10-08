package com.itsaky.apiinfo.models;

import android.text.TextUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.Signature;
import android.os.Environment;
import java.io.FileNotFoundException;

/**
 * Holds information about a class
 */
public class ClassInfo extends Info {
    
    /**
     * Superclass of this class
     */
    public String superClass;
    
    /**
     * Interfaces that this class implements
     */
    public final List<String> interfaces = new ArrayList<>();
    
    /**
     * Fields of this class
     */
    public final Map<String, FieldInfo> fields = new HashMap<>();
    
    /**
     * Methods of this class
     */
    public final Map<String, MethodInfo> methods = new HashMap<>();
    
    
    /**
     * Get the field by its simple name
     *
     * @param name Name of the field to find
     */
    public FieldInfo getFieldByName(String name) {
        return fields.get(name);
    }
    
    /**
     * Get method by its name and parameter types
     *
     * @param name Name of the method to find
     * @param parameters Parameter types of the method
     */
    public MethodInfo getMethod(String name, String[] parameters) {
        
        /**
         * Convert parameter types to their binary version
         */
        final String[] parameterTypes = new String[parameters.length];
        for(int i=0;i<parameters.length;i++) {
            String paramType = parameters[i];
            String binaryParamType = Signature.createTypeSignature(paramType, true /* isResolved */);
            parameterTypes[i] = binaryParamType;
        }
        
        for(Map.Entry<String, MethodInfo> entry : methods.entrySet()) {
            MethodInfo method = entry.getValue();
            
            /**
             * Check if this method's simple name is what we are looking for
             */
            if(method != null
            && method.simpleName.trim().equals(name.trim())) {
                
                /**
                 * Get parameter types of this method
                 * Takes binary representation, returns binary representation
                 */
                String[] paramTypes = Signature.getParameterTypes(method.name);
                
                /**
                 * Join the parameters to check if they are equal
                 * This is just more efficient
                 */
                final String params1 = TextUtils.join(",", paramTypes);
                final String params2 = TextUtils.join(",", parameterTypes);
                
                
                if(params1.equals(params2)) {
                    
                    /**
                     * We found what we were looking for!
                     */
                    return method;
                }
            }
        }
        return null;
    }
}
