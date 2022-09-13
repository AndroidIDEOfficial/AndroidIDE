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
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
// PublicId.java - Information about public identifiers

/*
 * Copyright 2001-2004 The Apache Software Foundation or its licensors,
 * as applicable.
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

package jaxp.sun.org.apache.xml.internal.resolver.helpers;

/**
 * Static methods for dealing with public identifiers.
 *
 * <p>This class defines a set of static methods that can be called
 * to handle public identifiers.</p>
 *
 * @author Norman Walsh
 * <a href="mailto:Norman.Walsh@Sun.COM">Norman.Walsh@Sun.COM</a>
 *
 */
public abstract class PublicId {
  protected PublicId() { }

  /**
   * Normalize a public identifier.
   *
   * <p>Public identifiers must be normalized according to the following
   * rules before comparisons between them can be made:</p>
   *
   * <ul>
   * <li>Whitespace characters are normalized to spaces (e.g., line feeds,
   * tabs, etc. become spaces).</li>
   * <li>Leading and trailing whitespace is removed.</li>
   * <li>Multiple internal whitespaces are normalized to a single
   * space.</li>
   * </ul>
   *
   * <p>This method is declared static so that other classes
   * can use it directly.</p>
   *
   * @param publicId The unnormalized public identifier.
   *
   * @return The normalized identifier.
   */
  public static String normalize(String publicId) {
    String normal = publicId.replace('\t', ' ');
    normal = normal.replace('\r', ' ');
    normal = normal.replace('\n', ' ');
    normal = normal.trim();

    int pos;

    while ((pos = normal.indexOf("  ")) >= 0) {
      normal = normal.substring(0, pos) + normal.substring(pos+1);
    }

    return normal;
  }

  /**
   * Encode a public identifier as a "publicid" URN.
   *
   * <p>This method is declared static so that other classes
   * can use it directly.</p>
   *
   * @param publicId The unnormalized public identifier.
   *
   * @return The normalized identifier.
   */
  public static String encodeURN(String publicId) {
    String urn = PublicId.normalize(publicId);

    urn = PublicId.stringReplace(urn, "%", "%25");
    urn = PublicId.stringReplace(urn, ";", "%3B");
    urn = PublicId.stringReplace(urn, "'", "%27");
    urn = PublicId.stringReplace(urn, "?", "%3F");
    urn = PublicId.stringReplace(urn, "#", "%23");
    urn = PublicId.stringReplace(urn, "+", "%2B");
    urn = PublicId.stringReplace(urn, " ", "+");
    urn = PublicId.stringReplace(urn, "::", ";");
    urn = PublicId.stringReplace(urn, ":", "%3A");
    urn = PublicId.stringReplace(urn, "//", ":");
    urn = PublicId.stringReplace(urn, "/", "%2F");

    return "urn:publicid:" + urn;
  }

  /**
   * Decode a "publicid" URN into a public identifier.
   *
   * <p>This method is declared static so that other classes
   * can use it directly.</p>
   *
   * @param urn The urn:publicid: URN
   *
   * @return The normalized identifier.
   */
  public static String decodeURN(String urn) {
    String publicId = "";

    if (urn.startsWith("urn:publicid:")) {
      publicId = urn.substring(13);
    } else {
      return urn;
    }

    publicId = PublicId.stringReplace(publicId, "%2F", "/");
    publicId = PublicId.stringReplace(publicId, ":", "//");
    publicId = PublicId.stringReplace(publicId, "%3A", ":");
    publicId = PublicId.stringReplace(publicId, ";", "::");
    publicId = PublicId.stringReplace(publicId, "+", " ");
    publicId = PublicId.stringReplace(publicId, "%2B", "+");
    publicId = PublicId.stringReplace(publicId, "%23", "#");
    publicId = PublicId.stringReplace(publicId, "%3F", "?");
    publicId = PublicId.stringReplace(publicId, "%27", "'");
    publicId = PublicId.stringReplace(publicId, "%3B", ";");
    publicId = PublicId.stringReplace(publicId, "%25", "%");

    return publicId;
    }

  /**
   * Replace one string with another.
   *
   */
  private static String stringReplace(String str,
                                      String oldStr,
                                      String newStr) {

    String result = "";
    int pos = str.indexOf(oldStr);

    //    System.out.println(str + ": " + oldStr + " => " + newStr);

    while (pos >= 0) {
      //      System.out.println(str + " (" + pos + ")");
      result += str.substring(0, pos);
      result += newStr;
      str = str.substring(pos+1);

      pos = str.indexOf(oldStr);
    }

    return result + str;
  }
}
