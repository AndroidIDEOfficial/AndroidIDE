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
package com.itsaky.androidide.utils;

import androidx.annotation.Nullable;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.models.prefs.EditorPreferencesKt;
import com.itsaky.androidide.views.SymbolInputView.Symbol;

import java.io.File;

public class Symbols {

  public static Symbol[] forFile(@Nullable File file) {
    if (file == null) {
      return new Symbol[0];
    }

    if (file.isFile()) {
      if (file.getName().endsWith(".java")
          || file.getName().endsWith(".gradle")
          || file.getName().endsWith(".kt")) return javaSymbols();

      if (file.getName().endsWith(".xml")) return xmlSymbols();
    }

    return new Symbol[0];
  }

  public static Symbol[] javaSymbols() {
    return new Symbol[] {
      new Symbol("↹", "\t"),
      new Symbol("{", "{}"),
      new Symbol("}"),
      new Symbol("(", "()"),
      new Symbol(")"),
      new Symbol(";"),
      new Symbol("="),
      new Symbol("\"", "\"\""),
      new Symbol("|"),
      new Symbol("&"),
      new Symbol("!"),
      new Symbol("[", "[]"),
      new Symbol("]"),
      new Symbol("<", "<>"),
      new Symbol(">"),
      new Symbol("+"),
      new Symbol("-"),
      new Symbol("/"),
      new Symbol("*"),
      new Symbol("?"),
      new Symbol(":"),
      new Symbol("_")
    };
  }

  public static Symbol[] xmlSymbols() {
    return new Symbol[] {
      new Symbol("↹", "\t"),
      new Symbol("<", "<>"),
      new Symbol(">"),
      new Symbol("/"),
      new Symbol("="),
      new Symbol("\"", "\"\""),
      new Symbol(":"),
      new Symbol("@"),
      new Symbol("+"),
      new Symbol("(", "()"),
      new Symbol(")"),
      new Symbol(";"),
      new Symbol(","),
      new Symbol("."),
      new Symbol("?"),
      new Symbol("|"),
      new Symbol("\\"),
      new Symbol("&"),
      new Symbol("[", "[]"),
      new Symbol("]"),
      new Symbol("{", "{}"),
      new Symbol("}"),
      new Symbol("_"),
      new Symbol("-")
    };
  }

  public static String createTabSpaces() {
    int size = EditorPreferencesKt.getTabSize();
    StringBuilder tab = new StringBuilder();
    for (int i = 1; i <= size; i++) {
      tab.append(" ");
    }
    return tab.toString();
  }
}
