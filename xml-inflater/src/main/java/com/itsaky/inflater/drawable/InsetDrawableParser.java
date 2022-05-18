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
import android.graphics.drawable.InsetDrawable;
import android.util.DisplayMetrics;

import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.InflateException;
import com.itsaky.inflater.util.Preconditions;

import org.xmlpull.v1.XmlPullParser;

/**
 * Parser for &lt;inset&gt; drawables.
 *
 * @author Akash Yadav
 */
public class InsetDrawableParser extends IDrawableParser {

  protected InsetDrawableParser(
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
      throw new InflateException("No drawable specified for <inset> drawable");
    }

    var value = value(index);
    Preconditions.assertNotBlank(value, "Invalid value specified for android:drawable");

    final var drawable = parseDrawable(value, BaseApplication.getBaseInstance());
    if (drawable == null) {
      throw new InflateException(
          "Unable to parse inset drawable. Failed to parse android:drawable");
    }

    index = attrIndex("insetLeft");
    final var left = index == -1 ? 0 : parseDimension(value(index), 0);

    index = attrIndex("insetTop");
    final var top = index == -1 ? 0 : parseDimension(value(index), 0);

    index = attrIndex("insetRight");
    final var right = index == -1 ? 0 : parseDimension(value(index), 0);

    index = attrIndex("insetBottom");
    final var bottom = index == -1 ? 0 : parseDimension(value(index), 0);

    return new InsetDrawable(drawable, left, top, right, bottom);
  }
}
