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
 * Copyright 2001, 2002,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.xni.parser;

import java.io.IOException;
import jaxp.sun.org.apache.xerces.internal.xni.XNIException;

/**
 * This interface defines a generic document scanner. This interface
 * allows a scanner to be used interchangably in existing parser
 * configurations.
 * <p>
 * If the parser configuration uses a document scanner that implements
 * this interface, components should be able to query the scanner
 * instance from the component manager using the following property
 * identifier:
 * <blockquote>
 *  "http://apache.org/xml/properties/internal/document-scanner"
 * </blockquote>
 *
 * @author Andy Clark, IBM
 *
 */
public interface XMLDocumentScanner
    extends XMLDocumentSource {

    //
    // XMLDocumentScanner methods
    //

    /**
     * Sets the input source.
     *
     * @param inputSource The input source.
     *
     * @throws IOException Thrown on i/o error.
     */
    public void setInputSource(XMLInputSource inputSource) throws IOException;

    /**
     * Scans a document.
     *
     * @param complete True if the scanner should scan the document
     *                 completely, pushing all events to the registered
     *                 document handler. A value of false indicates that
     *                 that the scanner should only scan the next portion
     *                 of the document and return. A scanner instance is
     *                 permitted to completely scan a document if it does
     *                 not support this "pull" scanning model.
     *
     * @return True if there is more to scan, false otherwise.
     */
    public boolean scanDocument(boolean complete)
        throws IOException, XNIException;

    public int next() throws XNIException, IOException;
} // interface XMLDocumentScanner
