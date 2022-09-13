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
 * Copyright 2002,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.util;

/**
 * All internalized xml symbols. They can be compared using "==".
 *
 * @author Sandy Gao, IBM
 */
public class XMLSymbols {

    // public constructor.
    public XMLSymbols(){}

    //==========================
    // Commonly used strings
    //==========================

    /**
     * The empty string.
     */
    public final static String EMPTY_STRING = "".intern();

    //==========================
    // Namespace prefixes/uris
    //==========================

    /**
     * The internalized "xml" prefix.
     */
    public final static String PREFIX_XML = "xml".intern();

    /**
     * The internalized "xmlns" prefix.
     */
    public final static String PREFIX_XMLNS = "xmlns".intern();

    //==========================
    // DTD symbols
    //==========================

    /** Symbol: "ANY". */
    public static final String fANYSymbol = "ANY".intern();

    /** Symbol: "CDATA". */
    public static final String fCDATASymbol = "CDATA".intern();

    /** Symbol: "ID". */
    public static final String fIDSymbol = "ID".intern();

    /** Symbol: "IDREF". */
    public static final String fIDREFSymbol = "IDREF".intern();

    /** Symbol: "IDREFS". */
    public static final String fIDREFSSymbol = "IDREFS".intern();

    /** Symbol: "ENTITY". */
    public static final String fENTITYSymbol = "ENTITY".intern();

    /** Symbol: "ENTITIES". */
    public static final String fENTITIESSymbol = "ENTITIES".intern();

    /** Symbol: "NMTOKEN". */
    public static final String fNMTOKENSymbol = "NMTOKEN".intern();

    /** Symbol: "NMTOKENS". */
    public static final String fNMTOKENSSymbol = "NMTOKENS".intern();

    /** Symbol: "NOTATION". */
    public static final String fNOTATIONSymbol = "NOTATION".intern();

    /** Symbol: "ENUMERATION". */
    public static final String fENUMERATIONSymbol = "ENUMERATION".intern();

    /** Symbol: "#IMPLIED. */
    public static final String fIMPLIEDSymbol = "#IMPLIED".intern();

    /** Symbol: "#REQUIRED". */
    public static final String fREQUIREDSymbol = "#REQUIRED".intern();

    /** Symbol: "#FIXED". */
    public static final String fFIXEDSymbol = "#FIXED".intern();


}
