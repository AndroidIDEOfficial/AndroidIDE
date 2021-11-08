package com.itsaky.androidide.ui.view;

/**
 * A fixed value which represents an enum of a flag
 *
 * @author Akash Yadav
 */
public interface IFixedValue {
    
    /**
     * Is this value a flag? An attribute which accepts a flag
     * may be combined with other flags.
     *
     * @return {@code true} if this value is a flag. {@code false} otherwise.
     */
    boolean isFlag ();
    
    /**
     * The name of this enum/flag as represented in an XML file
     *
     * @return The name of this flag
     */
    String getName();
    
    /**
     * Get the value of this enum/flag. This value is fixed and should not change.
     *
     * @return Value of this enum/flag.
     */
    int getValue();
}
