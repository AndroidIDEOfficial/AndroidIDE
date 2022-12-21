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
 * Copyright 2003-2005 The Apache Software Foundation.
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
package jaxp.sun.org.apache.xerces.internal.xinclude;

import java.io.IOException;

import jaxp.sun.org.apache.xerces.internal.util.XML11Char;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;

/**
 * This class is used for reading resources requested in &lt;include&gt; elements in
 * XML 1.1 entities, when the parse attribute of the &lt;include&gt; element is "text".
 * Using this class will open the location, detect the encoding, and discard the
 * byte order mark, if applicable.
 *
 * @author Michael Glavassevich, IBM
 *
 *
 * @see XIncludeHandler
 */
public class XInclude11TextReader
    extends XIncludeTextReader {

    /**
     * Construct the XIncludeReader using the XMLInputSource and XIncludeHandler.
     *
     * @param source The XMLInputSource to use.
     * @param handler The XIncludeHandler to use.
     * @param bufferSize The size of this text reader's buffer.
     */
    public XInclude11TextReader(XMLInputSource source, XIncludeHandler handler, int bufferSize)
        throws IOException {
        super(source, handler, bufferSize);
    }

    /**
     * Returns true if the specified character is a valid XML character
     * as per the rules of XML 1.1.
     *
     * @param ch The character to check.
     */
    protected boolean isValid(int ch) {
        return XML11Char.isXML11Valid(ch);
    }
}
