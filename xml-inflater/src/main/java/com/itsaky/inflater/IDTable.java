/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/
package com.itsaky.inflater;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds references to view IDs. The {@link #newRound()} must be called before inflating a layout
 * from the XML. This will make sure that we cleanup the existing IDs.
 *
 * @author Akash Yadav
 */
public class IDTable {

  /**
   * Map<String, Integer> containing the IDs. Entries are mapped by the name of the ID.
   *
   * <p>For example : if android:id="@+id/my_id" is provided for view, then an entry will be created
   * in this map with the key : 'my_id' and a random integer as the value.
   *
   * <p>No entries are created for views which do not have the android:id attribute.
   */
  private static final Map<String, Integer> ids = new HashMap<>();

  /** Clears the existing ID map. */
  public static void newRound() {
    ids.clear();
  }

  /**
   * Creates a new ID entry for the given name.
   *
   * @param idName The name of the ID.
   * @return The assigned ID.
   */
  public static int newId(String idName) {

    {
      // In some cases, @+id might be used as a reference
      // to another ID. So we check if an ID with the same
      // name is already present or not.
      final int id = getId(idName);
      if (id != View.NO_ID) {
        return id;
      }
    }

    final int id = View.generateViewId();
    ids.put(idName, id);
    return id;
  }

  /**
   * Get the ID mapped by the given name.
   *
   * @param name The name of the ID.
   * @return The mapped ID or {@link android.view.View#NO_ID}
   */
  public static int getId(String name) {
    final Integer id = ids.getOrDefault(name, null);
    if (id == null) {
      return View.NO_ID;
    }
    return id;
  }
}
