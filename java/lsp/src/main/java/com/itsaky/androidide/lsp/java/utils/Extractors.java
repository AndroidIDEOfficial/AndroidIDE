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

package com.itsaky.androidide.lsp.java.utils;

import androidx.annotation.NonNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractors {

  private static final Pattern PACKAGE_EXTRACTOR =
      Pattern.compile("^([a-z][_a-zA-Z0-9]*\\.)*[a-z][_a-zA-Z0-9]*");
  private static final Pattern SIMPLE_EXTRACTOR = Pattern.compile("[A-Z][_a-zA-Z0-9]*$");

  @NonNull
  public static String packageName(String className) {
    Matcher matcher = PACKAGE_EXTRACTOR.matcher(className);
    if (matcher.find()) {
      return matcher.group();
    }
    return "";
  }

  @NonNull
  public static String simpleName(String className) {
    Matcher matcher = SIMPLE_EXTRACTOR.matcher(className);
    if (matcher.find()) {
      return matcher.group();
    }
    return "";
  }
}
