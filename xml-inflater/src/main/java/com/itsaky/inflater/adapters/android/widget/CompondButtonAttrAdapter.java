package com.itsaky.inflater.adapters.android.widget;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;

/**
 * Attribute handler for handling attibutes to CompoundButton
 *
 * @author Akash Yadav
 */
public class CompondButtonAttrAdapter extends ButtonAttrAdapter {

  public CompondButtonAttrAdapter(
      @NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
    super(resourceFinder, displayMetrics);
  }

  @Override
  public boolean isApplicableTo(View view) {
    return view instanceof CompoundButton;
  }

  @Override
  public boolean apply(@NonNull IAttribute attribute, @NonNull View view) {
    final CompoundButton button = (CompoundButton) view;
    final Context context = button.getContext();
    final String name = attribute.getAttributeName();
    final String value = attribute.getValue();

    if (!canHandleNamespace(attribute)) {
      return false;
    }

    boolean handled = true;

    switch (name) {
      case "buttonTint":
        button.setButtonTintList(parseColorStateList(value, context));
        break;
      case "button":
        button.setButtonDrawable(parseDrawable(value, context));
        break;
      case "buttonTintMode":
        button.setButtonTintMode(parsePorterDuffMode(value));
        break;
      default:
        handled = false;
        break;
    }

    if (!handled) {
      handled = super.apply(attribute, view);
    }

    return handled;
  }
}
