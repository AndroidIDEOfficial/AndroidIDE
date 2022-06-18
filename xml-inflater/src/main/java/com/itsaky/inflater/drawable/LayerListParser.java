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

import static com.itsaky.inflater.util.Preconditions.assertNotBlank;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.util.TextDrawable;

import org.jetbrains.annotations.Contract;
import org.xmlpull.v1.XmlPullParser;

/**
 * Parser for parsing &lt;layer-list&gt; drawables.
 *
 * @author Akash Yadav
 */
public class LayerListParser extends IDrawableParser {

  protected LayerListParser(
      XmlPullParser parser,
      IResourceTable resourceFinder,
      DisplayMetrics displayMetrics,
      int minDepth) {
    super(parser, resourceFinder, displayMetrics, minDepth);
  }

  @Override
  public Drawable parseDrawable() throws Exception {
    final var layer = new LayerDrawable(new Drawable[0]);
    var index = -1;
    var value = "";
    var event = parser.getEventType();
    int left = 0, top = 0, right = 0, bottom = 0;
    var parseInner = true;
    while (event != XmlPullParser.END_DOCUMENT && canParse()) {
      if (event == XmlPullParser.START_TAG) {
        final var name = parser.getName();
        if ("item".equals(name)) {
          index = attrIndex("drawable");
          Drawable drawable = null;
          if (index != -1) {
            value = value(index);
            assertNotBlank(value, "Invalid value for android:drawable attribute");
            drawable = parseDrawable(value, BaseApplication.getBaseInstance());

            // if the android:drawable is specified, we do not parse the inner elements
            // of <item>
            parseInner = false;
          }

          index = attrIndex("left");
          left = index == -1 ? 0 : parseDimension(value(index), 0);

          index = attrIndex("top");
          top = index == -1 ? 0 : parseDimension(value(index), 0);

          index = attrIndex("right");
          right = index == -1 ? 0 : parseDimension(value(index), 0);

          index = attrIndex("bottom");
          bottom = index == -1 ? 0 : parseDimension(value(index), 0);

          if (drawable == null) {
            parseInner = true;
          } else {
            addToLayer(layer, drawable, left, top, right, bottom);
          }

          if (!parseInner) {
            skipToEndTag(this.parser.getDepth());
          }
        } else if (parser.getDepth() > 2 && parseInner) {
          // depth 1 = <layer-list>
          // depth 2 = <item>
          // depth > 2 = child drawables inside <item> tag
          final var parser =
              DrawableParserFactory.parserForTag(
                  null, resourceFinder, displayMetrics, this.parser, name);
          if (parser != null) {
            parser.setMinDepth(this.parser.getDepth());
            var d = parser.parse();
            if (d == null) {
              d = unsupported();
            }

            addToLayer(layer, d, left, top, right, bottom);
          } else {
            addToLayer(layer, unsupported(), left, top, right, bottom);
          }
        }
      }
      event = parser.next();
    }

    return layer;
  }

  private void addToLayer(
      @NonNull LayerDrawable layer, Drawable drawable, int left, int top, int right, int bottom) {
    final int index = layer.addLayer(drawable);
    layer.setLayerInset(index, left, top, right, bottom);
  }

  private void skipToEndTag(int depth) throws Exception {
    for (var event = parser.getEventType();
        event != XmlPullParser.END_DOCUMENT;
        event = parser.next()) {
      if (event == XmlPullParser.END_TAG && depth == parser.getDepth()) {
        break;
      }
    }
  }

  @NonNull
  @Contract(" -> new")
  private Drawable unsupported() {
    return new TextDrawable("Unsupported drawable", displayMetrics);
  }
}
