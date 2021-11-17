package com.itsaky.androidide.ui.view.adapters;

import android.view.View;
import android.widget.Button;
import com.itsaky.androidide.ui.inflater.IResourceFinder;
import com.itsaky.androidide.ui.view.IAttribute;

public class ButtonAttrAdapter extends TextViewAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof Button;
    }

    @Override
    public boolean apply(IAttribute attribute, View view, IResourceFinder resFinder) {
        if (super.apply(attribute, view, resFinder)) {
            return true;
        }
        
        boolean handled = false;
        
        // Button does not have special attributes 
        // All attributes are inherited from TextView
        
        return handled;
    }
}
