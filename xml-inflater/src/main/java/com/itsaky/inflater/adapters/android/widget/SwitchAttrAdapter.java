/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.inflater.adapters.android.widget;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;

/**
 * Attribute adapter for {@link Switch}.
 *
 * @author Akash Yadav
 */
public class SwitchAttrAdapter extends CompondButtonAttrAdapter {

  public SwitchAttrAdapter(@NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
    super(resourceFinder, displayMetrics);
  }

  @Override
  public boolean isApplicableTo(View view) {
    return view instanceof Switch;
  }

  @SuppressLint("UseSwitchCompatOrMaterialCode")
  @Override
  public boolean apply(@NonNull IAttribute attribute, @NonNull View view) {
    final var button = (Switch) view;
    final var name = attribute.getAttributeName();
    final var value = attribute.getValue();

    if (!canHandleNamespace(attribute)) {
      return false;
    }

    boolean handled = true;
    switch (name) {
      case "showText":
        button.setShowText(parseBoolean(value));
        break;
      case "splitTrack":
        button.setSplitTrack(parseBoolean(value));
        break;
      case "switchMinWidth":
        button.setSwitchMinWidth(parseDimension(value, 0));
        break;
      case "switchPadding":
        button.setSwitchPadding(parseDimension(value, 0));
        break;
      case "textOff":
        button.setTextOff(parseString(value));
        break;
      case "textOn":
        button.setTextOn(parseString(value));
        break;
      case "textStyle":
        button.setSwitchTypeface(Typeface.DEFAULT, parseTextStyle(value));
        break;
      case "thumb":
        button.setThumbDrawable(parseDrawable(value, button.getContext()));
        break;
      case "thumbTextPadding":
        button.setThumbTextPadding(parseDimension(value, 0));
        break;
      case "thumbTint":
        button.setThumbTintList(parseColorStateList(value, button.getContext()));
        break;
      case "thumbTintMode":
        button.setThumbTintMode(parsePorterDuffMode(value));
        break;
      case "track":
        button.setTrackDrawable(parseDrawable(value, button.getContext()));
        break;
      case "trackTint":
        button.setTrackTintList(parseColorStateList(value, button.getContext()));
        break;
      case "trackTintMode":
        button.setTrackTintMode(parsePorterDuffMode(value));
        break;
      case "typeface":
        button.setSwitchTypeface(parseTypeface(value));
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
