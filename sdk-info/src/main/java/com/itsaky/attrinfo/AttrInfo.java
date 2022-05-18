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

package com.itsaky.attrinfo;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.attrinfo.models.Attr;
import com.itsaky.attrinfo.models.Styleable;
import com.itsaky.sdkinfo.R;
import com.itsaky.xml.INamespace;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * A parser for parsing <b>attrs.xml</b>. This parser maps the attributes with the declared
 * styleables.
 *
 * @author Akash Yadav
 */
public class AttrInfo {

  /** Styles mapped by names; */
  public final Map<String, Styleable> styles = new HashMap<>();

  public final Map<String, Attr> attributes = new TreeMap<>();
  public final Styleable NO_PARENT;

  public AttrInfo(@NonNull final Resources resources) throws Exception {
    NO_PARENT = new Styleable("<unknown_parent>");

    Objects.requireNonNull(resources, "Cannot read from null resources.");

    final var in = resources.openRawResource(R.raw.attrs);
    parseFromStream(in);
  }

  @Nullable
  public Attr getAttribute(@NonNull final String name) {
    var attr = this.attributes.get(name);
    if (attr == null) {
      final var a = NO_PARENT.attributes.stream().filter(aa -> aa.name.equals(name)).findFirst();
      attr = a.orElse(null);
    }

    return attr;
  }

  @NonNull
  public Map<String, Attr> getAttributes() {
    return this.attributes;
  }

  @Nullable
  public Styleable getStyle(@NonNull final String name) {
    return this.styles.get(name);
  }

  @NonNull
  public Map<String, Styleable> getStyles() {
    return styles;
  }

  @Override
  public int hashCode() {
    return Objects.hash(NO_PARENT, styles);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AttrInfo)) {
      return false;
    }
    AttrInfo that = (AttrInfo) o;
    return Objects.equals(NO_PARENT, that.NO_PARENT) && Objects.equals(styles, that.styles);
  }

  @NonNull
  @Override
  public String toString() {
    return "AttrInfoCompat{" + "NO_PARENT=" + NO_PARENT + ", styles=" + styles + '}';
  }

  protected void parseFromStream(InputStream in) throws Exception {
    Objects.requireNonNull(in, "Cannot read from null input stream");

    Document doc = Jsoup.parse(in, null, "", Parser.xmlParser());
    final var resources = doc.getElementsByTag("resources").first();

    parseFromResources(resources);
  }

  protected void parseFromResources(Element resources) {
    Objects.requireNonNull(resources, "Cannot parse from null resource element");

    this.styles.clear();

    for (int i = 0; i < resources.childrenSize(); i++) {
      final var child = resources.child(i);
      if (child.tagName().equals("declare-styleable")) {
        this.styles.put(parseStyleable(child).name, parseStyleable(child));
      } else if (child.tagName().equals("attr")) {
        NO_PARENT.attributes.add(parseAttr(child));
      }
    }

    this.styles.put(NO_PARENT.name, NO_PARENT);
  }

  @NonNull
  protected Styleable parseStyleable(@NonNull final Element styleable) {
    checkName(styleable);

    final var name = styleable.attr("name");
    final var style = new Styleable(name);

    for (int i = 0; i < styleable.childrenSize(); i++) {
      final var attr = styleable.child(i);
      if (!attr.tagName().equals("attr")) {
        continue;
      }

      style.attributes.add(parseAttr(attr));
    }

    return style;
  }

  @NonNull
  protected Attr parseAttr(@NonNull final Element attr) {
    checkName(attr);

    final var attribute = new Attr(attr.attr("name"));
    if (attribute.name.contains(":")) {
      final var split = attribute.name.split(":", 2);
      attribute.namespace = INamespace.ANDROID;
      attribute.name = split[1];
    }

    if (attribute.name.equals("gravity")) {
      System.out.println("This is it");
    }

    if (attr.hasAttr("format")) {
      attribute.format = Attr.formatForName(attr.attr("format"));
      if (attribute.hasFormat(Attr.BOOLEAN)) {
        attribute.possibleValues.add("true");
        attribute.possibleValues.add("false");
      }
    }

    final var enums = attr.getElementsByTag("enum");
    final var flags = attr.getElementsByTag("flag");

    if (enums.size() > 0) {
      for (var enumEntry : enums) {
        checkName(enumEntry);
        attribute.possibleValues.add(enumEntry.attr("name"));
      }

      if (!attribute.hasFormat(Attr.ENUM)) {
        attribute.format |= Attr.ENUM;
      }
    }

    if (flags.size() > 0) {
      for (var flagEntry : flags) {
        checkName(flagEntry);
        attribute.possibleValues.add(flagEntry.attr("name"));
      }

      if (!attribute.hasFormat(Attr.FLAG)) {
        attribute.format |= Attr.FLAG;
      }
    }

    if (this.attributes.containsKey(attribute.name)) {
      final var present = this.attributes.get(attribute.name);
      if (present != null) {
        if (present.hasPossibleValues()) {
          attribute.possibleValues.addAll(present.possibleValues);
        }

        if (attribute.format == 0 && present.format != 0) {
          attribute.format = present.format;
        }
      }
    }

    this.attributes.put(attribute.name, attribute);

    return attribute;
  }

  private void checkName(@NonNull Element child) {
    if (!child.hasAttr("name")) {
      throw new IllegalStateException("Element does not have 'name' attribute: " + child);
    }
  }
}
