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

package com.itsaky.androidide.utils;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

import androidx.annotation.NonNull;
import com.blankj.utilcode.util.ResourceUtils;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.text.StringsKt;

public class ProjectWriter {

  private static final String XML_TEMPLATE_PATH = "templates/xml";
  private static final String SOURCE_PATH_REGEX = "/.*/src/.*?/(java|kotlin)";

  @NonNull
  public static String createMenu() {
    return ResourceUtils.readAssets2String(XML_TEMPLATE_PATH + "/menu.xml");
  }

  @NonNull
  public static String createDrawable() {
    return ResourceUtils.readAssets2String(XML_TEMPLATE_PATH + "/drawable.xml");
  }

  @NonNull
  public static String createLayout() {
    return ResourceUtils.readAssets2String(XML_TEMPLATE_PATH + "/layout.xml");
  }

  @NonNull
  public static String createLayoutName(String name) {
    final var nameWithoutExtension = StringsKt.substringBeforeLast(name, '.', name);
    var baseName = nameWithoutExtension;
    if (baseName.endsWith("Activity")) {
      baseName = StringsKt.substringBeforeLast(baseName, "Activity", baseName);
      baseName = "activity" + baseName;
    } else if (baseName.endsWith("Fragment")) {
      baseName = StringsKt.substringBeforeLast(baseName, "Fragment", baseName);
      baseName = "fragment" + baseName;
    } else {
      baseName = "layout" + baseName;
    }

    final var sb = new StringBuilder();
    var hasUpper = false;
    for (int i = 0; i < baseName.length(); i++) {
      final char c = baseName.charAt(i);
      if (isUpperCase(c)) {
        hasUpper = true;
        sb.append("_");
        sb.append(toLowerCase(c));
        continue;
      }

      sb.append(c);
    }

    if (!hasUpper) {
      sb.delete(0, sb.length());
      sb.append("layout_");
      sb.append(nameWithoutExtension);
    }

    sb.append(".xml");

    return sb.toString();
  }

  public static String getPackageName(File parentPath) {
    Matcher pkgMatcher = Pattern.compile(SOURCE_PATH_REGEX).matcher(parentPath.getAbsolutePath());
    if (pkgMatcher.find()) {
      int end = pkgMatcher.end();
      if (end <= 0) return "";
      String name = parentPath.getAbsolutePath().substring(pkgMatcher.end());
      if (name.startsWith(File.separator)) name = name.substring(1);
      return name.replace(File.separator, ".");
    }
    return null;
  }

  public static String createJavaClass(String packageName, String className) {
    return ClassBuilder.createClass(packageName, className);
  }

  public static String createJavaInterface(String packageName, String className) {
    return ClassBuilder.createInterface(packageName, className);
  }

  public static String createJavaEnum(String packageName, String className) {
    return ClassBuilder.createEnum(packageName, className);
  }

  public static String createActivity(String packageName, String className) {
    return ClassBuilder.createActivity(packageName, className);
  }
}
