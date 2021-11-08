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
     * The android namespace
     */
    public static final String NS_ANDROID = "android";
    
    @NonNull
    String getNamespace ();
    
    @NonNull
    String getAttributeName ();
    
    @Nullable
    String getValue ();
    
    @NonNull
    Set<IFixedValue> getFixedValues ();
}
