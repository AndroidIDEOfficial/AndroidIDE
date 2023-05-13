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

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itsaky.androidide.xml.utils;

import static com.android.SdkConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_WIDTH;
import static com.android.SdkConstants.PreferenceTags.PREFERENCE_CATEGORY;
import static com.android.SdkConstants.VALUE_MATCH_PARENT;
import static com.android.SdkConstants.VALUE_WRAP_CONTENT;

import com.android.SdkConstants;
import com.android.annotations.NonNull;

/**
 * Builds XML strings. Arguments are not validated or escaped. This class is designed to replace
 * hand writing XML snippets in string literals.
 */
public final class XmlBuilder {
  private enum Construct {
    NULL,
    START_TAG,
    ATTRIBUTE,
    CHARACTER_DATA,
    END_TAG
  }

  private final StringBuilder stringBuilder = new StringBuilder();

  private Construct lastAppendedConstruct = Construct.NULL;
  private int indentationLevel;

  @NonNull
  public XmlBuilder startTag(@NonNull String name) {
    if (!lastAppendedConstruct.equals(Construct.END_TAG)) {
      int length = stringBuilder.length();
      if (length > 0) {
        stringBuilder.replace(length - 1, length, ">\n");
      }
    }

    if (indentationLevel != 0) {
      stringBuilder.append('\n');
    }

    indent();

    stringBuilder.append('<').append(name).append('\n');

    indentationLevel++;
    lastAppendedConstruct = Construct.START_TAG;

    return this;
  }

  @NonNull
  public XmlBuilder androidAttribute(@NonNull String name, boolean value) {
    return androidAttribute(name, Boolean.toString(value));
  }

  @NonNull
  public XmlBuilder androidAttribute(@NonNull String name, int value) {
    return androidAttribute(name, Integer.toString(value));
  }

  @NonNull
  public XmlBuilder androidAttribute(@NonNull String name, @NonNull String value) {
    return attribute(SdkConstants.ANDROID_NS_NAME, name, value);
  }

  @NonNull
  public XmlBuilder attribute(@NonNull String name, @NonNull String value) {
    return attribute("", name, value);
  }

  @NonNull
  public XmlBuilder attribute(
    @NonNull String namespacePrefix, @NonNull String name, @NonNull String value) {
    indent();

    if (!namespacePrefix.isEmpty()) {
      stringBuilder.append(namespacePrefix).append(':');
    }

    stringBuilder.append(name).append("=\"").append(value).append("\"\n");

    lastAppendedConstruct = Construct.ATTRIBUTE;
    return this;
  }

  @NonNull
  public XmlBuilder wrapContent() {
    return withSize(VALUE_WRAP_CONTENT, VALUE_WRAP_CONTENT);
  }

  @NonNull
  public XmlBuilder matchParent() {
    return withSize(VALUE_MATCH_PARENT, VALUE_MATCH_PARENT);
  }

  @NonNull
  public XmlBuilder withSize(@NonNull String width, @NonNull String height) {
    androidAttribute(ATTR_LAYOUT_WIDTH, width);
    androidAttribute(ATTR_LAYOUT_HEIGHT, height);
    return this;
  }

  @NonNull
  public XmlBuilder characterData(@NonNull String data) {
    if (lastAppendedConstruct.equals(Construct.START_TAG)
      || lastAppendedConstruct.equals(Construct.ATTRIBUTE)) {
      int length = stringBuilder.length();
      stringBuilder.replace(length - 1, length, ">\n");
    }

    indent();

    stringBuilder.append(data).append('\n');

    lastAppendedConstruct = Construct.CHARACTER_DATA;
    return this;
  }

  @NonNull
  public XmlBuilder appendDirectly(String content) {
    stringBuilder.append(content);
    return this;
  }

  @NonNull
  public XmlBuilder endTag(@NonNull String name) {
    return endTagImpl(name, !name.endsWith("Layout") && !name.equals(PREFERENCE_CATEGORY));
  }

  @NonNull
  public XmlBuilder seperateEndTag(@NonNull String name) {
    return endTagImpl(name, false);
  }

  @NonNull
  private XmlBuilder endTagImpl(@NonNull String name, boolean useEmptyElementTag) {
    if (lastAppendedConstruct.equals(Construct.START_TAG)
      || lastAppendedConstruct.equals(Construct.ATTRIBUTE)) {
      int length = stringBuilder.length();

      if (useEmptyElementTag) {
        stringBuilder.deleteCharAt(length - 1);
      } else {
        stringBuilder.replace(length - 1, length, ">\n\n");
      }
    }

    indentationLevel--;

    if ((lastAppendedConstruct.equals(Construct.START_TAG)
      || lastAppendedConstruct.equals(Construct.ATTRIBUTE))
      && useEmptyElementTag) {
      stringBuilder.append(" />\n");
    } else {
      indent();

      stringBuilder.append("</").append(name).append(">\n");
    }

    lastAppendedConstruct = Construct.END_TAG;
    return this;
  }

  private void indent() {
    for (int i = 0; i < indentationLevel; i++) {
      stringBuilder.append("    ");
    }
  }

  @NonNull
  @Override
  public String toString() {
    return stringBuilder.toString();
  }
}

