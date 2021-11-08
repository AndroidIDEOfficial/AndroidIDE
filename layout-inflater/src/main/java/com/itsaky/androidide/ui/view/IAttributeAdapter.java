package com.itsaky.androidide.ui.view;

import android.view.View;
import com.itsaky.androidide.ui.inflater.IResourceFinder;

/**
 * An adapter that handles attributes of a specific type of view
 */
public interface IAttributeAdapter {

    /**
     * Can this adapter handle attributes of the provided view?
     *
     * @param view The view to which the attributes will be applied
     * @return {@code true} if this adapter can handle attributes of the specified view
     */
    boolean isApplicableTo(View view);
    
    /**
     * Apply the provided attribute to the view.
     * <p>
     * This will be called if and only if {@link #isApplicableTo(View)} returns {@code true}.
     *
     * @param attribute The attribute to apply
     * @param view The view to apply attribute to
     * @param resFinder The resource finder that can be used to find resource values
     * @return {@code true} if the attribute was applied successfully {@code false} otherwise.
     */
    boolean apply (IAttribute attribute, View view, IResourceFinder resFinder);
}
