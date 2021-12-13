/************************************************************************************
 * This file is part of AndroidIDE.
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
package com.itsaky.layoutinflater.impl;

import android.view.View;
import com.itsaky.layoutinflater.IResourceFinder;
import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IAttributeAdapter;
import com.itsaky.layoutinflater.IView;
import com.itsaky.layoutinflater.IViewGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

public abstract class BaseView implements IView {
    
    protected final Set<IAttribute> attributes = new HashSet<>();
    protected final Set<IAttributeAdapter> attrAdapters = new HashSet<>();
    
    protected final String qualifiedName;
    protected final View view;
    protected IViewGroup parent;

    private boolean isPlaceholder = false;

    public BaseView(String qualifiedName, View view) {
        this(qualifiedName, view, false);
    }

    public BaseView(String qualifiedName, View view, boolean isPlaceholder) {
        this.qualifiedName = qualifiedName;
        this.view = view;
        this.isPlaceholder = isPlaceholder;
    }
    
    public void setParent (IViewGroup parent) {
        this.parent = parent;
    }
    
    public void setPlaceholder (boolean placeholder) {
        this.isPlaceholder = placeholder;
    }
    
    @Override
    public boolean isPlaceholder() {
        return isPlaceholder;
    }
    
    @Override
    public View asView() {
        return view;
    }

    @Override
    public IViewGroup getParent() {
        return parent;
    }
    
    @Override
    public void addAttribute(IAttribute attr, IResourceFinder resFinder) {
        if (attr == null || this.attributes.contains(attr)) {
            return;
        }
        
        this.attributes.add(attr);
        
        for (IAttributeAdapter adapter : attrAdapters) {
            if (adapter.isApplicableTo(asView())
                && adapter.apply(attr, asView(), resFinder)) {
                    break;
            }
        }
    }
    
    @Override
    public void removeAttribute(IAttribute attr) {
        this.attributes.remove(attr);
    }

    @Override
    public void removeAttributeAt(int index) {
        this.attributes.remove(index);
    }

    @Override
    public void registerAttributeAdapter(IAttributeAdapter adapter) {
        if (adapter == null) {
            return;
        }
        
        this.attrAdapters.add(adapter);
    }
    
    protected Set<IAttributeAdapter> getAttributeAdapters () {
        return attrAdapters;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            attributes,
            attrAdapters,
            qualifiedName,
            view,
            parent,
            isPlaceholder
        );
    }
}
