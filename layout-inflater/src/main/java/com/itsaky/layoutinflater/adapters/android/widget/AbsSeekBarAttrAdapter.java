package com.itsaky.layoutinflater.adapters.android.widget;

import android.view.View;
import android.widget.AbsSeekBar;
import androidx.annotation.NonNull;
import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IResourceFinder;

/**
 * Attribute adapter for handling attributes related to
 * AbsSeekBar.
 *
 * @author Akash Yadav
 */
public abstract class AbsSeekBarAttrAdapter extends ProgressBarAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof AbsSeekBar;
    }

    @Override
    public boolean apply(@NonNull IAttribute attribute, View view, IResourceFinder resFinder) {
        final var seek = (AbsSeekBar) view;
        final var namespace = attribute.getNamespace();
        final var name = attribute.getAttributeName();
        final var value = attribute.getValue();

        if (!canHandleNamespace(namespace)) {
            return false;
        }

        boolean handled = true;

        switch (name) {
            case "thumbTint" :
                // TODO Parse color state list
                break;
            case "thumbTintMode" :
                seek.setThumbTintMode(parsePorterDuffMode(value));
                break;
            case "tickMarkTint" :
                // TODO Parse color state list
                break;
            case "tickMarkTintMode":
                seek.setTickMarkTintMode(parsePorterDuffMode(value));
                break;
            default:
                handled = false;
                break;
        }

        if (!handled) {
            handled = super.apply(attribute, view, resFinder);
        }

        return handled;
    }
}
