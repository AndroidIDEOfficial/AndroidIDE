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
 * Copyright 2005 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.jaxp.validation;

import jaxp.xml.transform.stream.StreamSource;

import jaxp.sun.org.apache.xerces.internal.xni.XNIException;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <p>Static utility methods for the Validation API implementation.</p>
 *
 * @author Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
final class Util {

    /**
     * Creates a proper {@link XMLInputSource} from a {@link StreamSource}.
     *
     * @return always return non-null valid object.
     */
    public static final XMLInputSource toXMLInputSource( StreamSource in ) {
        if( in.getReader()!=null )
            return new XMLInputSource(
            in.getPublicId(), in.getSystemId(), in.getSystemId(),
            in.getReader(), null );
        if( in.getInputStream()!=null )
            return new XMLInputSource(
            in.getPublicId(), in.getSystemId(), in.getSystemId(),
            in.getInputStream(), null );

        return new XMLInputSource(
        in.getPublicId(), in.getSystemId(), in.getSystemId() );
    }

    /**
     * Reconstructs {@link SAXException} from XNIException.
     */
    public static SAXException toSAXException(XNIException e) {
        if(e instanceof XMLParseException)
            return toSAXParseException((XMLParseException)e);
        if( e.getException() instanceof SAXException )
            return (SAXException)e.getException();
        return new SAXException(e.getMessage(),e.getException());
    }

    public static SAXParseException toSAXParseException( XMLParseException e ) {
        if( e.getException() instanceof SAXParseException )
            return (SAXParseException)e.getException();
        return new SAXParseException( e.getMessage(),
        e.getPublicId(), e.getExpandedSystemId(),
        e.getLineNumber(), e.getColumnNumber(),
        e.getException() );
    }

} // Util
