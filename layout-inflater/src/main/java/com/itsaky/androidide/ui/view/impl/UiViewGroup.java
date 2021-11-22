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
package com.itsaky.androidide.ui.view.impl;

import android.view.ViewGroup;
import com.itsaky.androidide.ui.view.IView;
import com.itsaky.androidide.ui.view.IViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UiViewGroup extends BaseViewGroup {
    
    private final ViewGroup viewGroup;
    private final List<IView> children;
    
    public UiViewGroup(String qualifiedName, ViewGroup view) {
        this(qualifiedName, view, false);
    }
    
    public UiViewGroup(String qualifiedName, ViewGroup view, boolean isPlaceholder) {
        super(qualifiedName, view, isPlaceholder);
        
        this.viewGroup = view;
        this.children = new ArrayList<>();
    }
    
    @Override
    public void addView(IView view) {
        addView(view, getChildCount());
    }
    
    @Override
    public void addView(IView view, int index) {
        super.addView(view, index);
        this.viewGroup.addView(view.asView(), index);
        this.children.add(index, view);
        
        onViewAdded(view);
    }
    
    @Override
    public void removeView(int index) {
        
        final IView view = children.get(index);
        
        this.viewGroup.removeViewAt(index);
        this.children.remove(index);
        
        onViewRemoved(view);
    }
    
    @Override
    public void removeView(IView view) {
        this.viewGroup.removeView(view.asView());
        this.children.remove(view);
        
        onViewRemoved(view);
    }
    
    @Override
    public void removeAll() {
        this.viewGroup.removeAllViews();
        this.children.clear();
    }

    @Override
    public void forEachChild(Consumer<IView> consumer) {
        this.children.stream().forEach(consumer);
    }
    
    @Override
    public int getChildCount() {
        return this.viewGroup.getChildCount();
    }
    
    @Override
    public List<IView> getChildren() {
        return this.children;
    }
    
    @Override
    public IView getChildAt(int index) {
        return children.get(index);
    }
}
