package com.itsaky.androidide.ui.view.impl;

import android.view.View;
import android.view.ViewGroup;
import com.itsaky.androidide.ui.view.IAttributeAdapter;
import com.itsaky.androidide.ui.view.IView;
import com.itsaky.androidide.ui.view.IViewGroup;

public abstract class BaseViewGroup extends BaseView implements IViewGroup {
    
    public BaseViewGroup(String qualifiedName, ViewGroup view, IViewGroup parent) {
        this(qualifiedName, view, parent, false);
    }

    public BaseViewGroup(String qualifiedName, ViewGroup view, IViewGroup parent, boolean isPlaceholder) {
        super(qualifiedName, view, parent, isPlaceholder);
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
}
