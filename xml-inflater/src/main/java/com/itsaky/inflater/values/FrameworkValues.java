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

package com.itsaky.inflater.values;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides framework resource values.
 *
 * @author Akash Yadav
 */
public class FrameworkValues {

  @NonNull
  public static List<String> listDimens() {
    return listFields(android.R.dimen.class);
  }

  @NonNull
  public static List<String> listBools() {
    return listFields(android.R.bool.class);
  }

  @NonNull
  public static List<String> listStrings() {
    return listFields(android.R.string.class);
  }

  @NonNull
  public static List<String> listColors() {
    return listFields(android.R.color.class);
  }

  @NonNull
  private static List<String> listFields(final Class<?> klass) {
    Objects.requireNonNull(klass);

    final var list = new ArrayList<String>();
    final var fields = klass.getDeclaredFields();
    for (var field : fields) {
      final var mods = field.getModifiers();
      if (!isPublic(mods) || !isStatic(mods)) {
        continue;
      }

      list.add(field.getName());
    }

    return list;
  }

  @NonNull
  public static List<String> listAllResources() {
    final var list = new ArrayList<String>();
    for (var klass : android.R.class.getDeclaredClasses()) {
      list.addAll(
          listFields(klass).stream()
              .map(("@android:" + klass.getSimpleName() + "/")::concat)
              .collect(Collectors.toList()));
    }
    return list;
  }
}
