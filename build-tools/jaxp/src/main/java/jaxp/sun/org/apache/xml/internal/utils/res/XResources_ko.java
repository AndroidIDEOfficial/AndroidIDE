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
/*
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: XResources_ko.java,v 1.2.4.1 2005/09/15 08:16:08 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.utils.res;

//
//  LangResources_ko.properties
//

/**
 * The Korean resource bundle.
 * @xsl.usage internal
 */
public class XResources_ko extends XResourceBundle
{
  private static final Object[][] _contents = new Object[][]
  {
    { "ui_language", "ko" }, { "help_language", "ko" }, { "language", "ko" },
    { "alphabet", new CharArrayWrapper(
      new char[]{ 0x3131, 0x3134, 0x3137, 0x3139, 0x3141, 0x3142, 0x3145,
                  0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e,
                  0x314f, 0x3151, 0x3153, 0x3155, 0x3157, 0x315b, 0x315c,
                  0x3160, 0x3161, 0x3163})},
    { "tradAlphabet", new CharArrayWrapper(
      new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                  'Y', 'Z' }) },

    //language orientation
    { "orientation", "LeftToRight" },

    //language numbering
    { "numbering", "multiplicative-additive" },
    { "multiplierOrder", "follows" },

    // largest numerical value
    //{"MaxNumericalValue", new Integer(100000000)},
    //These would not be used for EN. Only used for traditional numbering
    { "numberGroups", new IntArrayWrapper(new int[]{ 1 }) },

    // chinese only ??
    { "zero", new CharArrayWrapper(new char[0]) },

    //These only used for mutiplicative-additive numbering
    { "multiplier", new LongArrayWrapper(new long[]{ 100000000, 10000, 1000,
        100, 10 }) },
    { "multiplierChar", new CharArrayWrapper(
      new char[]{  0xc5b5, 0xb9cc, 0xcc9c, 0xbc31, 0xc2ed }) },
    { "digits", new CharArrayWrapper(
      new char[]{ 0xc77c, 0xc774, 0xc0bc, 0xc0ac, 0xc624, 0xc721, 0xce60,
          0xd314, 0xad6c}) }, { "tables", new StringArrayWrapper(
              new String[]{ "digits" }) }
  };

  /**
   * Get the association list.
   *
   * @return The association list.
   */
  public Object[][] getContents()
  {
    return _contents;
  }
}
