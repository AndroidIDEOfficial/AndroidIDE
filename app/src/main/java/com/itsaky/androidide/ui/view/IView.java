package com.itsaky.androidide.ui.view;

import android.view.View;

/**
 * Represents a view in the UI Designer
 *
 * @author Akash Yadav
 */
public interface IView {
    
    /**
     * Get this view as android {@link View}. This will be used to display this view in the designer
     *
     * @return {@link View} of this object. Maybe {@code null}
     */
    View asView();
    
    /**
     * Add this attribute to this view
     *
     * @param attr The Attribute to add
     */
    void addAttribute (IAttribute attr);
    
    /**
     * Remove this attribute
     *
     * @param attr The Attribute to remove
     */
    void removeAttribute (IAttribute attr);
    
    /**
     * Remove attribute at index
     *
     * @param index Index of the attribute to remove
     */
    void removeAttributeAt (int index);
}
