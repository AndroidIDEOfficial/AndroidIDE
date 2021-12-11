package com.itsaky.layoutinflater.adapters.android.widget;

import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IResourceFinder;

/**
 * Attribute adapter for handling attributes related to
 * SeekBar.
 *
 * @author Akash Yadav
 */
public class SeekBarAttrAdapter extends AbsSeekBarAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof SeekBar;
    }

    @Override
    public boolean apply(@NonNull IAttribute attribute, View view, IResourceFinder resFinder) {
        final var seek = (SeekBar) view;
        final var context = seek.getContext();
        final var namespace = attribute.getNamespace();
        final var name = attribute.getAttributeName();
        final var value = attribute.getValue();

        if (!canHandleNamespace(namespace)) {
            return false;
        }
        boolean handled = true;

        switch (name) {
            case "thumb":
                seek.setThumb(parseDrawable(value, resFinder, context));
                break;
            default:
                handled = false;
                break;
        }

        return handled;

    }
}
