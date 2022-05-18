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
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;

import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.InflateException;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

/**
 * Parser for parsing &lt;selector&gt; drawables.
 *
 * @author Akash Yadav
 */
public class StateListParser extends IDrawableParser {

  private static final ILogger LOG = ILogger.newInstance("StateListParser");

  protected StateListParser(
      XmlPullParser parser,
      IResourceTable resourceFinder,
      DisplayMetrics displayMetrics,
      int minDepth) {
    super(parser, resourceFinder, displayMetrics, minDepth);
  }

  @Override
  public Drawable parseDrawable() throws Exception {
    var states = new StateListDrawable();

    // --------------------------- NOTE -------------------------
    // Unsupported attributes :
    //  1. android:constantSize
    //  2. android:variablePadding

    var index = attrIndex("dither");
    if (index != -1) {
      states.setDither(parseBoolean(value(index)));
    }

    var event = parser.getEventType();
    while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
      if (event == XmlPullParser.START_TAG) {
        var name = parser.getName();
        if ("item".equals(name)) {
          index = attrIndex("drawable");
          if (index == -1) {
            throw new InflateException("<selector> item does not define android:drawable");
          }
          final var drawable = parseDrawable(value(index), BaseApplication.getBaseInstance());
          if (drawable == null) {
            throw new InflateException("Unable to parse drawable for android:drawable attribute");
          }

          addStates(states, drawable);
        }
      }
    }

    return states;
  }

  /**
   * Add all the defined states of the current tag in <code>parser</code> to the given state list
   * drawable.
   *
   * @param states The drawable to add states to.
   * @param drawable The drawable associated with the defined states;
   */
  private void addStates(StateListDrawable states, Drawable drawable) {
    final var stateList = new ArrayList<Integer>();
    final var count = parser.getAttributeCount();
    for (int i = 0; i < count; i++) {
      var name = parser.getAttributeName(i);
      var value = parser.getAttributeValue(i);
      if (!name.startsWith("state_")) {
        continue;
      }

      var state = reflectState(name);

      final var isEnabled = parseBoolean(value);
      if (!isEnabled) {
        // android:state_[state_name]="false"
        state = -state;
      }

      stateList.add(state);
    }

    final var arr = new int[stateList.size()];
    for (int i = 0; i < stateList.size(); i++) {
      arr[i] = stateList.get(i);
    }

    states.addState(arr, drawable);
  }

  private int reflectState(String name) {
    try {
      final var clazz = android.R.attr.class;
      final var field = clazz.getDeclaredField(name);
      return field.getInt(null);
    } catch (Throwable th) {
      LOG.error("Unable to get state ID with name:", name);
      return -1;
    }
  }
}
