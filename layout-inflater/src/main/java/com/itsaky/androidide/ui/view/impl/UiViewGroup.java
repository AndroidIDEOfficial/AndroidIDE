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
