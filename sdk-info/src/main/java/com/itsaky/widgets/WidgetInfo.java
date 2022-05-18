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
package com.itsaky.widgets;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.widgets.models.Widget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class WidgetInfo {

  private final SortedMap<String, Widget> widgets;
  private final SortedMap<String, String[]> params;

  public WidgetInfo(@NonNull final Resources resources) throws IOException {
    this.widgets = new TreeMap<>();
    this.params = new TreeMap<>();
    readWidgets(resources);
  }

  private void readWidgets(@NonNull final Resources resources) throws IOException {
    final InputStream in = resources.openRawResource(com.itsaky.sdkinfo.R.raw.widgets);
    if (in == null) {
      return;
    }

    final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String line;
    while ((line = reader.readLine()) != null) {
      final var split = line.split("\\s");
      final var name = split[0];
      if (name == null || name.trim().isEmpty()) {
        continue;
      }

      final var code = name.charAt(0);

      final var entryName = name.substring(1);
      final var isLayout = code == 'L'; // L -> Layout, W -> Widget, P -> LayoutParam
      final var superclasses = new String[split.length - 1];

      System.arraycopy(split, 1, superclasses, 0, superclasses.length);

      if (code == 'P') {
        final var className = entryName.substring(0, entryName.lastIndexOf("."));
        this.params.put(className, superclasses);
      } else {
        final var widget = new Widget(entryName, isLayout);
        widget.superclasses = superclasses;
        widgets.put(entryName, widget);
      }
    }
  }

  public Widget getWidget(String name) {
    return this.widgets.getOrDefault(name, null);
  }

  @Nullable
  public Widget getWidgetBySimpleName(@NonNull final String name) {
    for (final var entry : this.widgets.entrySet()) {
      final var key = entry.getKey();
      final var simple = key.substring(key.lastIndexOf(".") + 1);
      if (simple.equals(name)) {
        return entry.getValue();
      }
    }
    return null;
  }

  public Collection<Widget> getWidgets() {
    return this.widgets.values();
  }

  public String[] getLayoutParamSuperClasses(final String className) {
    return this.params.get(className);
  }
}
