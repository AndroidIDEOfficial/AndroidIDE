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

package jaxp.sun.org.apache.xerces.internal.impl.xs.opti;

import org.w3c.dom.TypeInfo;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.w3c.dom.DOMException;


/**
 * @xerces.internal
 *
 * @author Rahul Srivastava, Sun Microsystems Inc.
 *
 */
public class DefaultElement extends NodeImpl
                            implements Element {

    // default constructor
    public DefaultElement() {
    }


    public DefaultElement(String prefix, String localpart, String rawname, String uri, short nodeType) {
        super(prefix, localpart, rawname, uri, nodeType);
    }


    //
    // org.w3c.dom.Element methods
    //

    // getter methods
    public String getTagName() {
        return null;
    }


    public String getAttribute(String name) {
        return null;
    }


    public Attr getAttributeNode(String name) {
        return null;
    }


    public NodeList getElementsByTagName(String name) {
        return null;
    }


    public String getAttributeNS(String namespaceURI, String localName) {
        return null;
    }


    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
        return null;
    }


    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return null;
    }


    public boolean hasAttribute(String name) {
        return false;
    }


    public boolean hasAttributeNS(String namespaceURI, String localName) {
        return false;
    }

    public TypeInfo getSchemaTypeInfo(){
      return null;
    }


    // setter methods
    public void setAttribute(String name, String value) throws DOMException {
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }


    public void removeAttribute(String name) throws DOMException {
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }


    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }


    public Attr setAttributeNode(Attr newAttr) throws DOMException {
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }


    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }


    public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }


    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }

    public void setIdAttributeNode(Attr at, boolean makeId) throws DOMException{
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }
    public void setIdAttribute(String name, boolean makeId) throws DOMException{
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }

    public void setIdAttributeNS(String namespaceURI, String localName,
                                    boolean makeId) throws DOMException{
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }

}
