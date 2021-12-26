package com.itsaky.layoutinflater.adapters.android.widget;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import com.itsaky.layoutinflater.IResourceFinder;
import com.itsaky.layoutinflater.IAttribute;

/**
 * Attribute handler for handling attibutes to CompoundButton
 *
 * @author Akash Yadav
 */
public class CompondButtonAttrAdapter extends ButtonAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof CompoundButton;
    }

    @Override
    public boolean apply(IAttribute attribute, View view) {
        final CompoundButton button = (CompoundButton) view;
        final Context context = button.getContext();
        final String namespace = attribute.getNamespace();
        final String name = attribute.getAttributeName();
        final String value = attribute.getValue();
        
        if (!canHandleNamespace(namespace)) {
            return false;
        }
        
        boolean handled = true;
        
        switch (name) {
            case "buttonTint" :
                // TODO Parse color state list
                break;
            case "button" :
                button.setButtonDrawable(parseDrawable(value, context));
                break;
            case "buttonTintMode" :
                button.setButtonTintMode(parsePorterDuffMode(value));
                break;
            default :
                handled = false;
                break;
        }
        
        if (!handled) {
            handled = super.apply(attribute, view);
        }

        return handled;
    }
}
