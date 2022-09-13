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
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jaxp.sun.xml.internal.stream;

import jaxp.xml.stream.XMLEventReader;
import jaxp.xml.stream.XMLStreamReader;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;

/**
 *
 * @author  Neeraj
 *
 * This class wraps XMLInputSource and is also capable of telling wether application
 * returned XMLStreamReader or not when XMLResolver.resolveEnity
 * was called.
 */
public class StaxXMLInputSource {

    XMLStreamReader fStreamReader ;
    XMLEventReader fEventReader ;
    XMLInputSource fInputSource ;

    //indicate if the source is resolved by a resolver
    boolean fHasResolver = false;

    /** Creates a new instance of StaxXMLInputSource */
    public StaxXMLInputSource(XMLStreamReader streamReader) {
        fStreamReader = streamReader ;
    }

    /** Creates a new instance of StaxXMLInputSource */
    public StaxXMLInputSource(XMLEventReader eventReader) {
        fEventReader = eventReader ;
    }

    public StaxXMLInputSource(XMLInputSource inputSource){
        fInputSource = inputSource ;

    }

    public StaxXMLInputSource(XMLInputSource inputSource, boolean hasResolver){
        fInputSource = inputSource ;
        fHasResolver = hasResolver;
    }

    public XMLStreamReader getXMLStreamReader(){
        return fStreamReader ;
    }

    public XMLEventReader getXMLEventReader(){
        return fEventReader ;
    }

    public XMLInputSource getXMLInputSource(){
        return fInputSource ;
    }

    public boolean hasXMLStreamOrXMLEventReader(){
        return (fStreamReader == null) && (fEventReader == null) ? false : true ;
    }

    public boolean hasResolver() {
        return fHasResolver;
    }
}
