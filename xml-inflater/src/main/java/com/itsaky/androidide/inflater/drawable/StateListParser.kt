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
package com.itsaky.androidide.inflater.drawable

import android.R.attr
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import com.itsaky.androidide.inflater.InflateException
import org.slf4j.LoggerFactory
import org.xmlpull.v1.XmlPullParser

/**
 * Parser for parsing &lt;selector&gt; drawables.
 *
 * @author Akash Yadav
 */
class StateListParser(parser: XmlPullParser?, minDepth: Int) :
  IDrawableParser(parser, minDepth) {

  companion object {

    private val log = LoggerFactory.getLogger(StateListParser::class.java)
  }

  @Throws(Exception::class)
  public override fun parseDrawable(context: Context): Drawable {
    val states = StateListDrawable()

    // --------------------------- NOTE -------------------------
    // Unsupported attributes :
    //  1. android:constantSize
    //  2. android:variablePadding
    var index = attrIndex("dither")
    if (index != -1) {
      states.setDither(parseBoolean(value(index)))
    }
    var event: Int
    while (parser!!.next().also { event = it } != XmlPullParser.END_DOCUMENT) {
      if (event == XmlPullParser.START_TAG) {
        val name = parser.name
        if ("item" == name) {
          index = attrIndex("drawable")
          if (index == -1) {
            throw InflateException("<selector> item does not define android:drawable")
          }
          val drawable = parseDrawable(context, value(index))
          addStates(states, drawable)
        }
      }
    }
    return states
  }

  /**
   * Add all the defined states of the current tag in `parser` to the given state list
   * drawable.
   *
   * @param states The drawable to add states to.
   * @param drawable The drawable associated with the defined states;
   */
  private fun addStates(states: StateListDrawable, drawable: Drawable) {
    val stateList = ArrayList<Int>()
    val count = parser!!.attributeCount
    for (i in 0 until count) {
      val name = parser.getAttributeName(i)
      val value = parser.getAttributeValue(i)
      if (!name.startsWith("state_")) {
        continue
      }
      var state = reflectState(name)
      val isEnabled = parseBoolean(value)
      if (!isEnabled) {
        // android:state_[state_name]="false"
        state = -state
      }
      stateList.add(state)
    }
    val arr = IntArray(stateList.size)
    for (i in stateList.indices) {
      arr[i] = stateList[i]
    }
    states.addState(arr, drawable)
  }

  private fun reflectState(name: String): Int {
    return try {
      val clazz = attr::class.java
      val field = clazz.getDeclaredField(name)
      field.getInt(null)
    } catch (th: Throwable) {
      log.error("Unable to get state ID with name: {}", name)
      -1
    }
  }
}