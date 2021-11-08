package com.itsaky.androidide.ui.view.impl;

import android.view.View;
import com.itsaky.androidide.ui.inflater.IResourceFinder;
import com.itsaky.androidide.ui.view.IAttribute;
import com.itsaky.androidide.ui.view.IAttributeAdapter;
import com.itsaky.androidide.ui.view.IView;
import com.itsaky.androidide.ui.view.IViewGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseView implements IView {
    
    protected final List<IAttribute> attributes = new ArrayList<>();
    protected final Set<IAttributeAdapter> attrAdapters = new HashSet<>();
    
    protected final String qualifiedName;
    protected final View view;
    protected final IViewGroup parent;

    private boolean isPlaceholder = false;

    public BaseView(String qualifiedName, View view, IViewGroup parent) {
        this(qualifiedName, view, parent, false);
    }

    public BaseView(String qualifiedName, View view, IViewGroup parent, boolean isPlaceholder) {
        this.qualifiedName = qualifiedName;
        this.view = view;
        this.parent = parent;
        this.isPlaceholder = isPlaceholder;
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
            if (adapter.isApplicableTo(asView())) {
                if (adapter.apply(attr, asView(), resFinder)) {
                    break;
                }
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
}
