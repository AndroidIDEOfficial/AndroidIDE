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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.InflateException;

import org.jetbrains.annotations.Contract;
import org.xmlpull.v1.XmlPullParser;

/**
 * Parser for parsing &lt;bitmap&gt; drawables.
 *
 * @author Akash Yadav
 */
public class BitmapDrawableParser extends IDrawableParser {

  protected BitmapDrawableParser(
      XmlPullParser parser,
      IResourceTable resourceFinder,
      DisplayMetrics displayMetrics,
      int minDepth) {
    super(parser, resourceFinder, displayMetrics, minDepth);
  }

  @Override
  public Drawable parseDrawable() throws Exception {
    var index = attrIndex("src");
    if (index == -1) {
      throw new InflateException("Invalid <bitmap> drawable. No android:src specified!");
    }

    var val = value(index);
    var dr = parseDrawable(val, BaseApplication.getBaseInstance());
    if (dr == null) {
      throw new InflateException("Cannot parse drawable for android:src = " + val);
    }

    final var bitmap =
        new BitmapDrawable(BaseApplication.getBaseInstance().getResources(), toBitmap(dr));

    index = attrIndex("antialias");
    if (index != -1) {
      bitmap.setAntiAlias(parseBoolean(value(index)));
    }

    index = attrIndex("dither");
    if (index != -1) {
      bitmap.setDither(parseBoolean(value(index)));
    }

    index = attrIndex("filter");
    if (index != -1) {
      bitmap.setFilterBitmap(parseBoolean(value(index)));
    }

    index = attrIndex("gravity");
    if (index != -1) {
      bitmap.setGravity(parseGravity(value(index)));
    }

    index = attrIndex("mipMap");
    if (index != -1) {
      bitmap.setMipMap(parseBoolean(value(index)));
    }

    index = attrIndex("tileMode");
    if (index != -1) {
      var mode = parseTileMode(value(index));
      bitmap.setTileModeXY(mode, mode);
    }

    index = attrIndex("tileModeX");
    if (index != -1) {
      var mode = parseTileMode(value(index));
      bitmap.setTileModeX(mode);
    }

    index = attrIndex("tileModeY");
    if (index != -1) {
      var mode = parseTileMode(value(index));
      bitmap.setTileModeY(mode);
    }

    return bitmap;
  }

  @Nullable
  @Contract(pure = true)
  private Shader.TileMode parseTileMode(@NonNull String value) {
    switch (value) {
      case "clamp":
        return Shader.TileMode.CLAMP;
      case "mirror":
        return Shader.TileMode.MIRROR;
      case "repeat":
        return Shader.TileMode.REPEAT;
      case "disabled":
      default:
        return null; // null = disabled
    }
  }

  private Bitmap toBitmap(@NonNull Drawable dr) {
    if (dr instanceof BitmapDrawable) {
      var bit = (BitmapDrawable) dr;
      if (bit.getBitmap() != null) {
        return bit.getBitmap();
      }
    }

    Bitmap bit;
    if (dr.getIntrinsicWidth() > 0 && dr.getIntrinsicHeight() > 0) {
      bit =
          Bitmap.createBitmap(
              dr.getIntrinsicWidth(), dr.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    } else {
      bit = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }

    final var canvas = new Canvas(bit);
    dr.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    dr.draw(canvas);
    return bit;
  }
}
