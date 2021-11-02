package com.itsaky.androidide.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Set;

/**
 * Attribute that can be applied to a {@link IView}
 *
 * @author Akash Yadav
 */
public interface IAttribute {
    
    /**
     * Set namespace of this attribute
     */
    void setNamespace (String namespace);
    
    /**
     * @see setNamespace(String)
     */
    @NonNull
    String getNamespace ();
    
    /**
     * Set name of this attribute
     */
    void setAtrributeName (String name);
    
    /**
     * @see setAtrributeName(String)
     */
    @NonNull
    String getAttributeName ();
    
    /**
     * Set current value of this attribute
     */
    void setValue (String name);
    
    /**
     * @see setValue(String)
     */
    @Nullable
    String getValue ();
    
    /**
     * Set applicable values of this attribute
     * Empty by default
     */
    void setApplicableValues(Set<String> name);
    
    /**
     * @see setAtrributeName(String)
     */
    @NonNull
    String getApplicableValues ();
    
    /**
     * Get the value type of this attribute
     */
    @NonNull
    AttrType getType ();
}
