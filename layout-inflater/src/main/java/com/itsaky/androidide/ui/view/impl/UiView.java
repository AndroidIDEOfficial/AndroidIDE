package com.itsaky.androidide.ui.view.impl;

import android.view.View;
import com.itsaky.androidide.ui.view.IViewGroup;

public class UiView extends BaseView {
    
    public UiView(String qualifiedName, View view) {
        this(qualifiedName, view, false);
    }

    public UiView(String qualifiedName, View view, boolean isPlaceholder) {
        super(qualifiedName, view, isPlaceholder);
    }
}
