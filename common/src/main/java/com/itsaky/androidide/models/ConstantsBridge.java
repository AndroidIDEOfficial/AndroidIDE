/*
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
 */
package com.itsaky.androidide.models;

public class ConstantsBridge {

  public static final String VIRTUAL_KEYS =
      "["
          + "\n  ["
          + "\n    \"ESC\","
          + "\n    {"
          + "\n      \"key\": \"/\","
          + "\n      \"popup\": \"\\\\\""
          + "\n    },"
          + "\n    {"
          + "\n      \"key\": \"-\","
          + "\n      \"popup\": \"|\""
          + "\n    },"
          + "\n    \"HOME\","
          + "\n    \"UP\","
          + "\n    \"END\","
          + "\n    \"PGUP\""
          + "\n  ],"
          + "\n  ["
          + "\n    \"TAB\","
          + "\n    \"CTRL\","
          + "\n    \"ALT\","
          + "\n    \"LEFT\","
          + "\n    \"DOWN\","
          + "\n    \"RIGHT\","
          + "\n    \"PGDN\""
          + "\n  ]"
          + "\n]";
  public static boolean EDITOR_PREF_SIZE_CHANGED = false;
  public static boolean EDITOR_PREF_LIGATURES_CHANGED = false;
  public static boolean EDITOR_PREF_FLAGS_CHANGED = false;
  public static boolean EDITOR_PREF_DRAW_HEX_CHANGED = false;
  public static boolean EDITOR_PREF_VISIBLE_PASSWORD_CHANGED = false;
  public static boolean EDITOR_PREF_WORD_WRAP_CHANGED = false;
  public static boolean EDITOR_PREF_USE_MAGNIFIER_CHANGED = false;

  public static boolean CLASS_LOAD_SUCCESS = true;
  public static boolean SPLASH_TO_MAIN = false;
}
