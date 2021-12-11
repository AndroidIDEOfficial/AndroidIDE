package com.itsaky.layoutinflater.adapters.android.widget;

import android.view.View;
import android.widget.RadioButton;

import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IResourceFinder;

/**
 * Attribute adapter for handling attributes related to
 * RadioButton.
 *
 * @author Akash Yadav
 */
public class RadioButtonAttrAdapter extends CompondButtonAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof RadioButton;
    }

    @Override
    public boolean apply(IAttribute attribute, View view, IResourceFinder resFinder) {
        // No special attributes for RadioButton
        return super.apply(attribute, view, resFinder);
    }
}
