/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
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
package com.itsaky.androidide.ui.view.impl;

import android.view.View;
import android.view.ViewGroup;
import com.itsaky.androidide.ui.view.IAttributeAdapter;
import com.itsaky.androidide.ui.view.IView;
import com.itsaky.androidide.ui.view.IViewGroup;

public abstract class BaseViewGroup extends BaseView implements IViewGroup {
    
    public BaseViewGroup(String qualifiedName, ViewGroup view) {
        this(qualifiedName, view, false);
    }

    public BaseViewGroup(String qualifiedName, ViewGroup view, boolean isPlaceholder) {
        super(qualifiedName, view, isPlaceholder);
    }
    
    @Override
    public boolean isPlaceholder() {
        return false; // We do not use view groups as placeholder
    }

    @Override
    public void addView(IView view, int index) {
        for (IAttributeAdapter adapter : getAttributeAdapters()) {
            view.registerAttributeAdapter(adapter);
        }
    }
    
    /**
     * Called when a new view has been added to this group
     *
     * @param view The view that was added
     */
    protected void onViewAdded (IView view) {}
    
    /**
     * Called when a new view has been removed from this group
     *
     * @param view The view that was removed
     */
    protected void onViewRemoved (IView view) {}
}
