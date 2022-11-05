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
package com.itsaky.androidide.inflater.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.ChecksSdkIntAtLeast;

import com.itsaky.androidide.inflater.internal.utils.ParseUtilsKt;

import org.xmlpull.v1.XmlPullParser;

/**
 * Base class for drawable parsers.
 *
 * @author Akash Yadav
 */
public abstract class IDrawableParser {

  public static final int ANY_DEPTH = -1;

  protected final XmlPullParser parser;
  protected int minDepth;

  protected IDrawableParser(XmlPullParser parser, int minDepth) {
    this.parser = parser;
    this.minDepth = minDepth;
  }

  /**
   * Parse the drawable using the already provided parser and data.
   *
   * @return The parsed {@link Drawable} or <code>null</code> if the parse was unsuccessful.
   * @throws Exception If any fatal error occurred while parsing the drawable.
   */
  public Drawable parse(final Context context) throws Exception {
    var index = attrIndex("visible");
    var visible = true;
    if (index != -1) {
      visible = parseBoolean(value(index));
    }

    var autoMirrored = false;
    index = attrIndex("autoMirrored");
    if (index != -1) {
      autoMirrored = parseBoolean(value(index));
    }

    var level = 0;
    index = attrIndex("level");
    if (index != -1) {
      level = parseInteger(value(index), 0);
    }

    final var drawable = parseDrawable(context);

    if (drawable != null) {
      drawable.setVisible(visible, false);
      drawable.setAutoMirrored(autoMirrored);
      drawable.setLevel(level);
    }

    return drawable;
  }
  
  protected int parseInteger(final String value, final int def) {
    return ParseUtilsKt.parseInteger(value, def);
  }
  
  protected boolean parseBoolean(final String value) {
    return ParseUtilsKt.parseBoolean(value);
  }
  
  protected Drawable parseDrawable(final Context context, final String value) {
    return ParseUtilsKt.parseDrawable(context, value);
  }
  
  protected int parseGravity(String value) {
    return ParseUtilsKt.parseGravity(value);
  }
  
  protected int parseDimension(Context context, String value) {
    return (int) ParseUtilsKt.parseDimension(context, value, 0);
  }
  
  protected int parseColor(Context context, String value) {
    return ParseUtilsKt.parseColor(context, value);
  }
  
  /**
   * Actual implementation of the parse logic.
   *
   * @return The parsed drawable. Maybe <code>null</code>.
   * @throws Exception If any fatal error occurs while parsing the drawable.
   */
  protected abstract Drawable parseDrawable(final Context context) throws Exception;

  /**
   * Find the index of the attribute with the given name.
   *
   * @param name The name of the attribute to look for.
   * @return The index of the attribute or <code>-1</code>.
   */
  protected int attrIndex(final String name) {
    for (int i = 0; i < this.parser.getAttributeCount(); i++) {
      if (this.parser.getAttributeName(i).equals(name)) {
        return i;
      }
    }

    return -1;
  }

  /**
   * Get the value of the attribute at the given index.
   *
   * @param index The index of the attribute.
   * @return The value of the attribute.
   */
  protected String value(int index) {
    return parser.getAttributeValue(index);
  }

  /**
   * Set the minimum depth that this parser should keep parsing. Subclasses are expected to parse
   * child tags with depths greater than this specified value.
   *
   * @param minDepth The minimum depth of this parser.
   */
  public void setMinDepth(int minDepth) {
    this.minDepth = minDepth;
  }

  /**
   * Checks if the drawable parser is allowed to parse at the current depth. This must be checked if
   * the drawable parser keeps looking for {@link XmlPullParser#START_TAG} or {@link
   * XmlPullParser#END_TAG}.
   *
   * <p>The {@link LayerListParser} can contain tags which are expected to be parsed by other
   * parsers. So, if the nesting parser does not check if this method if <code>true</code>, it might
   * consume all the events and the {@link LayerListParser} will have invalid data to parse. In some
   * cases, the nested parser might consume all the events until {@link XmlPullParser#END_DOCUMENT}.
   *
   * @return <code>true</code> if the parser can keep parsing, <code>false</code> otherwise.
   */
  protected boolean canParse() {
    return this.parser.getDepth() >= minDepth;
  }

  @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
  protected boolean isApi28() {
    return Build.VERSION.SDK_INT >= 28;
  }

  @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
  protected boolean isApi29() {
    return Build.VERSION.SDK_INT >= 29;
  }

  @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
  protected boolean isApi30() {
    return Build.VERSION.SDK_INT >= 30;
  }
}
