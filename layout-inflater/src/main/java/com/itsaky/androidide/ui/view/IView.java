package com.itsaky.androidide.ui.view;

import android.view.View;
import com.itsaky.androidide.ui.inflater.IResourceFinder;

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
     * @param resFinder The resource finder
     */
    void addAttribute (IAttribute attr, IResourceFinder resFinder);
    
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
    
    /**
     * Register this attribute adapter
     *
     * @param adapter The adapter to register
     */
    void registerAttributeAdapter (IAttributeAdapter adapter);
    
    /**
     * Get the parent of this view
     *
     * @return The parent of this view or {@code null} if this is the root view.
     */
    IViewGroup getParent();
    
    /**
     * Is this view a placeholder for another view?
     */
    boolean isPlaceholder ();
}
