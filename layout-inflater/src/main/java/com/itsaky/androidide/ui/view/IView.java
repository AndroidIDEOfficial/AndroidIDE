/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/
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
