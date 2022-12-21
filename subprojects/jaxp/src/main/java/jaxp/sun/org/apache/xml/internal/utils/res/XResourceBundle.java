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
 * $Id: XResourceBundle.java,v 1.2.4.1 2005/09/15 08:16:04 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.utils.res;

import java.util.ListResourceBundle;

/**
 * The default (english) resource bundle.
 *
 * @xsl.usage internal
 */
public class XResourceBundle extends ListResourceBundle {

    /**
     * Error resource constants
     */
    public static final String ERROR_RESOURCES =
            "jaxp.sun.org.apache.xalan.internal.res.XSLTErrorResources", XSLT_RESOURCE =
            "jaxp.sun.org.apache.xml.internal.utils.res.XResourceBundle", LANG_BUNDLE_NAME =
            "jaxp.sun.org.apache.xml.internal.utils.res.XResources", MULT_ORDER =
            "multiplierOrder", MULT_PRECEDES = "precedes", MULT_FOLLOWS =
            "follows", LANG_ORIENTATION = "orientation", LANG_RIGHTTOLEFT =
            "rightToLeft", LANG_LEFTTORIGHT = "leftToRight", LANG_NUMBERING =
            "numbering", LANG_ADDITIVE = "additive", LANG_MULT_ADD =
            "multiplicative-additive", LANG_MULTIPLIER =
            "multiplier", LANG_MULTIPLIER_CHAR =
            "multiplierChar", LANG_NUMBERGROUPS = "numberGroups", LANG_NUM_TABLES =
            "tables", LANG_ALPHABET = "alphabet", LANG_TRAD_ALPHABET = "tradAlphabet";


    /**
     * Get the association list.
     *
     * @return The association list.
     */
    public Object[][] getContents() {
        return new Object[][]{
                    {"ui_language", "en"}, {"help_language", "en"}, {"language", "en"},
                    {"alphabet", new CharArrayWrapper(new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G',
                            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                            'V', 'W', 'X', 'Y', 'Z'})},
                    {"tradAlphabet", new CharArrayWrapper(new char[]{'A', 'B', 'C', 'D', 'E', 'F',
                            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                            'U', 'V', 'W', 'X', 'Y', 'Z'})},
                    //language orientation
                    {"orientation", "LeftToRight"},
                    //language numbering
                    {"numbering", "additive"},};
    }
}
