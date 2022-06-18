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
package com.itsaky.inflater.values;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.inflater.values.models.ArrayResource;
import com.itsaky.inflater.values.models.BooleanResource;
import com.itsaky.inflater.values.models.ColorResource;
import com.itsaky.inflater.values.models.DimensionResource;
import com.itsaky.inflater.values.models.IntegerResource;
import com.itsaky.inflater.values.models.StringResource;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Reads and manages values (colors, strings, integers, etc.) for a single module.
 *
 * @author Akash Yadav
 */
public class ValuesTable {

  private static final ILogger LOG = ILogger.newInstance("ValuesTable");
  // TODO Parse styles and styleables
  private final Map<String, IResource> arrays;
  private final Map<String, IResource> strings;
  private final Map<String, IResource> colors;
  private final Map<String, IResource> integers;
  private final Map<String, IResource> dimens;
  private final Map<String, IResource> booleans;

  private final Map<String, Map<String, IResource>> resourceMap = new HashMap<>();

  protected ValuesTable() {
    this.arrays = new HashMap<>();
    this.strings = new HashMap<>();
    this.colors = new HashMap<>();
    this.integers = new HashMap<>();
    this.dimens = new HashMap<>();
    this.booleans = new HashMap<>();

    this.resourceMap.put("array", this.arrays);
    this.resourceMap.put("string", this.strings);
    this.resourceMap.put("color", this.colors);
    this.resourceMap.put("integer", this.integers);
    this.resourceMap.put("dimen", this.dimens);
    this.resourceMap.put("bool", this.booleans);
  }

  /**
   * Create a new table by reading the files from the given values directory.
   *
   * @param valuesDirectory The "res/values" directory.
   * @return The values table or {@code null} if the directory is invalid or there are no files in
   *     this directory.
   * @throws XmlPullParserException Thrown by {@link XmlPullParserFactory}.
   * @throws IOException Thrown by {@link XmlPullParser}.
   */
  @Nullable
  public static ValuesTable forDirectory(File valuesDirectory) throws Exception {
    if (valuesDirectory == null) {
      LOG.error("Cannot create values table for directory: ", null);
      return null;
    }

    if (!valuesDirectory.exists()) {
      LOG.error(
          "Cannot create values table for directory as it does not exists. Directory:",
          valuesDirectory);
      return null;
    }

    final var files = valuesDirectory.listFiles(ValuesTable::isValid);
    if (files == null) {
      LOG.error("No files found in the given values directory");
      return null;
    }

    final var start = System.currentTimeMillis();
    final var values = new ValuesTable();

    for (var file : files) {
      final var factory = XmlPullParserFactory.newInstance();
      final var parser = factory.newPullParser();
      parser.setInput(new FileInputStream(file), null);

      var event = parser.getEventType();
      while (event != XmlPullParser.END_DOCUMENT) {
        if (event == XmlPullParser.START_TAG) {
          final var tag = parser.getName();
          if ("resources".equals(tag)) {
            event = parser.next();
            continue;
          }

          readTag(tag, parser, values);
        }
        event = parser.next();
      }
    }

    final var duration = System.currentTimeMillis() - start;
    LOG.info(
        String.format(
            Locale.getDefault(),
            "Took %d ms to read %d files in directory: %s",
            duration,
            files.length,
            valuesDirectory));

    return values;
  }

  private static void readTag(String tag, XmlPullParser parser, ValuesTable values)
      throws Exception {

    if ("style".equals(tag) || "declare-styleable".equals(tag)) {
      // These tags are not supported yet
      // Skip parsing until we reach next tag with same depth as of this tag
      skip(parser);
      return;
    }

    // TODO Should we check if these tags are specified within <resources>?
    final var name = getName(parser);
    if (name == null) {
      LOG.error(String.format("A <%s> resource was found with no 'name' attribute", tag));
      return;
    }

    if ("item".equals(tag)) {
      tag = parser.getAttributeValue(null, "type");
      if (tag == null) {
        LOG.error("<item> resource value found but no 'type' was specified.");
        return;
      }
    }

    // check for array before reading value
    // because array resources do not have text values
    if ("array".equals(tag) || tag.endsWith("-array")) {
      readArray(parser, values, name);
      return;
    }

    // Value must be read after all other required values have been read
    final var value = parser.nextText();
    IResource resourceValue;

    switch (tag) {
      case "string":
        resourceValue = new StringResource(name, value);
        values.strings.put(name, resourceValue);
        break;
      case "color":
        resourceValue = new ColorResource(name, value);
        values.colors.put(name, resourceValue);
        break;
      case "bool":
        resourceValue = new BooleanResource(name, value);
        values.booleans.put(name, resourceValue);
        break;
      case "dimen":
        resourceValue = new DimensionResource(name, value);
        values.dimens.put(name, resourceValue);
        break;
      case "integer":
        resourceValue = new IntegerResource(name, value);
        values.integers.put(name, resourceValue);
        break;
      default:
        LOG.warn("Unknown or unsupported resource value tag:", tag);
        break;
    }
  }

  private static void readArray(@NonNull XmlPullParser parser, ValuesTable values, String name)
      throws XmlPullParserException, IOException {
    final var minDepth = parser.getDepth();
    final var items = new ArrayList<String>();

    var event = parser.getEventType();
    while ((event = parser.next()) != XmlPullParser.END_DOCUMENT && parser.getDepth() >= minDepth) {
      if (event == XmlPullParser.START_TAG) {
        final var child = parser.getName();
        if ("item".equals(child)) {
          final var value = parser.nextText();
          items.add(value);
        }
      }
    }

    final var arr = new String[items.size()];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = items.get(i);
    }

    values.arrays.put(name, new ArrayResource(name, arr));
  }

  private static void skip(@NonNull XmlPullParser parser)
      throws XmlPullParserException, IOException {
    if (parser.getEventType() != XmlPullParser.START_TAG) {
      throw new IllegalStateException();
    }
    int depth = 1;
    while (depth != 0) {
      switch (parser.next()) {
        case XmlPullParser.END_TAG:
          depth--;
          break;
        case XmlPullParser.START_TAG:
          depth++;
          break;
      }
    }
  }

  private static String getName(@NonNull XmlPullParser parser) {
    return parser.getAttributeValue(null, "name");
  }

  private static boolean isValid(File file) {
    return file != null && file.isFile() && FileUtils.isUtf8(file);
  }

  @Nullable
  public Map<String, IResource> getTable(final String name) {
    Objects.requireNonNull(name);
    return this.resourceMap.get(name);
  }

  @NonNull
  public Map<String, Map<String, IResource>> getResourceMap() {
    return resourceMap;
  }

  /**
   * Finds a resource value of any type.
   *
   * @param name The name of the resource to find.
   * @return The resource value or {@code null} if there is no resource with the given name.
   */
  @SuppressWarnings("unused")
  public IResource findResource(final String name) {
    IResource value;

    value = findString(name);
    if (value != null) {
      return value;
    }

    value = findColor(name);
    if (value != null) {
      return value;
    }

    value = findInteger(name);
    if (value != null) {
      return value;
    }

    value = findDimension(name);
    if (value != null) {
      return value;
    }

    value = findArray(name);
    if (value != null) {
      return value;
    }

    value = findBoolean(name);
    //noinspection RedundantIfStatement
    if (value != null) {
      return value;
    }

    return null;
  }

  public ArrayResource findArray(final String name) {
    return (ArrayResource) arrays.getOrDefault(name, null);
  }

  /**
   * Find a string value with the given name.
   *
   * @param name The name of the string resource.
   * @return The string resource or {@code null} if there is no resource with the given name.
   */
  public StringResource findString(final String name) {
    return (StringResource) strings.getOrDefault(name, null);
  }

  /**
   * Find a color value with the given name.
   *
   * @param name The name of the color resource.
   * @return The color resource or {@code null} if there is no resource with the given name.
   */
  public ColorResource findColor(final String name) {
    return (ColorResource) colors.getOrDefault(name, null);
  }

  /**
   * Find a integer value with the given name.
   *
   * @param name The name of the integer resource.
   * @return The integer resource or {@code null} if there is no resource with the given name.
   */
  public IntegerResource findInteger(final String name) {
    return (IntegerResource) integers.getOrDefault(name, null);
  }

  /**
   * Find a dimension value with the given name.
   *
   * @param name The name of the dimension resource.
   * @return The dimension resource or {@code null} if there is no resource with the given name.
   */
  public DimensionResource findDimension(final String name) {
    return (DimensionResource) dimens.getOrDefault(name, null);
  }

  /**
   * Find a boolean value with the given name.
   *
   * @param name The name of the boolean resource.
   * @return The boolean resource or {@code null} if there is no resource with the given name.
   */
  public BooleanResource findBoolean(final String name) {
    return (BooleanResource) booleans.getOrDefault(name, null);
  }

  /**
   * Reads the contents of the given file and updates this table.
   *
   * @param file The file to sync with.
   */
  public void syncWithFile(final File file) throws Exception {
    final var start = System.currentTimeMillis();
    final var factory = XmlPullParserFactory.newInstance();
    final var parser = factory.newPullParser();
    parser.setInput(new FileInputStream(file), null);

    var event = parser.getEventType();
    while (event != XmlPullParser.END_DOCUMENT) {
      if (event == XmlPullParser.START_TAG) {
        final var tag = parser.getName();
        if ("resources".equals(tag)) {
          event = parser.next();
          continue;
        }

        ValuesTable.readTag(tag, parser, this);
      }
      event = parser.next();
    }

    LOG.debug(
        "Syncing with file", file.getName(), "took", (System.currentTimeMillis() - start), "ms");
  }

  @NonNull
  @Override
  public String toString() {
    return "ValuesTable{"
        + "strings="
        + strings
        + ", colors="
        + colors
        + ", integers="
        + integers
        + ", dimens="
        + dimens
        + ", booleans="
        + booleans
        + '}';
  }
}
