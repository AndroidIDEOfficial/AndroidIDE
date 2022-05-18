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

package com.itsaky.inflater.drawable;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.InflateException;
import com.itsaky.inflater.util.Preconditions;

import org.xmlpull.v1.XmlPullParser;

/**
 * Parser for parsing &lt;clip&gt; drawables;
 *
 * @author Akash Yadav
 */
public class ClipDrawableParser extends IDrawableParser {

  protected ClipDrawableParser(
      XmlPullParser parser,
      IResourceTable resourceFinder,
      DisplayMetrics displayMetrics,
      int minDepth) {
    super(parser, resourceFinder, displayMetrics, minDepth);
  }

  @Override
  public Drawable parseDrawable() throws Exception {
    var index = attrIndex("drawable");
    if (index == -1) {
      throw new InflateException("<clip> drawable must specify android:drawable attribute");
    }

    var val = value(index);
    Preconditions.assertNotBlank(val, "Invalid value specified to android:drawable attribute");

    final var drawable = parseDrawable(val, BaseApplication.getBaseInstance());
    if (drawable == null) {
      throw new InflateException("Unable to parse drawable for value: " + val);
    }

    var orientation = ClipDrawable.HORIZONTAL;
    index = attrIndex("clipOrientation");
    if (index != -1) {
      orientation = parseClipOrientation(value(index));
    }

    var gravity = Gravity.LEFT;
    index = attrIndex("gravity");
    if (index != -1) {
      gravity = parseGravity(value(index));
    }

    return new ClipDrawable(drawable, gravity, orientation);
  }

  protected int parseClipOrientation(@NonNull final String value) {
    switch (value) {
      case "vertical":
        return ClipDrawable.VERTICAL;
      default:
      case "horizontal":
        return ClipDrawable.HORIZONTAL;
    }
  }
}
