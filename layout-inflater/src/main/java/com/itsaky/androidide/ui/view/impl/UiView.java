package com.itsaky.androidide.ui.view.impl;

import android.view.View;
import com.itsaky.androidide.ui.view.IViewGroup;

public class UiView extends BaseView {
    
    public UiView(String qualifiedName, View view, IViewGroup parent) {
        this(qualifiedName, view, parent, false);
    }

    public UiView(String qualifiedName, View view, IViewGroup parent, boolean isPlaceholder) {
        super(qualifiedName, view, parent, isPlaceholder);
    }
}
