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

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.InflateException;
import com.itsaky.inflater.util.Preconditions;

import org.xmlpull.v1.XmlPullParser;

/**
 * Parser for parsing &lt;scale&gt; drawables.
 *
 * @author Akash Yadav
 */
public class ScaleDrawableParser extends IDrawableParser {

  protected ScaleDrawableParser(
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
      throw new InflateException("<scale> drawable must specify android:drawable attribute");
    }
    var v = value(index);
    Preconditions.assertNotBlank(v, "Invalid value specified for android:drawable attribute");

    final var drawable = parseDrawable(v, BaseApplication.getBaseInstance());
    if (drawable == null) {
      throw new InflateException("Unable to parse drawable: " + v);
    }

    var gravity = Gravity.LEFT;
    index = attrIndex("scaleGravity");
    if (index != -1) {
      try {
        gravity = parseGravity(value(index));
      } catch (Throwable th) {
        // ignored
      }
    }

    var scaleWidth = -1.0f;
    var scaleHeight = -1.0f; // DO_NOT_SCALE by default

    index = attrIndex("scaleWidth");
    if (index != -1) {
      try {
        scaleWidth = parseScale(value(index));
      } catch (Throwable th) {
        // ignored
      }
    }

    index = attrIndex("scaleHeight");
    if (index != -1) {
      try {
        scaleHeight = parseScale(value(index));
      } catch (Throwable th) {
        // ignored
      }
    }

    return new ScaleDrawable(drawable, gravity, scaleWidth, scaleHeight);
  }

  private float parseScale(@NonNull String value) {
    if (!value.endsWith("%")) {
      throw new InflateException("Invalid scale value:" + value);
    }

    final var factor = Float.parseFloat(value.substring(0, value.length() - 1)) / 100;
    if (factor < 0f || factor > 1f) {
      throw new InflateException("Scale factor must be between 0% and 100%");
    }

    return factor;
  }
}
