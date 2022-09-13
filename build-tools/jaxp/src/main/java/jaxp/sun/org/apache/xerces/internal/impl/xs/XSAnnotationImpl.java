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
 * Copyright 2001-2004 The Apache Software Foundation.
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
package jaxp.sun.org.apache.xerces.internal.impl.xs;

import java.io.IOException;
import java.io.StringReader;

import jaxp.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import jaxp.sun.org.apache.xerces.internal.parsers.DOMParser;
import jaxp.sun.org.apache.xerces.internal.parsers.SAXParser;
import jaxp.sun.org.apache.xerces.internal.xs.XSAnnotation;
import jaxp.sun.org.apache.xerces.internal.xs.XSConstants;
import jaxp.sun.org.apache.xerces.internal.xs.XSNamespaceItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This is an implementation of the XSAnnotation schema component.
 *
 * @xerces.internal
 */
public class XSAnnotationImpl implements XSAnnotation {

    // Data

    // the content of the annotation node, including all children, along
    // with any non-schema attributes from its parent
    private String fData = null;

    // the grammar which owns this annotation; we get parsers
    // from here when we need them
    private SchemaGrammar fGrammar = null;

    // constructors
    public XSAnnotationImpl(String contents, SchemaGrammar grammar) {
        fData = contents;
        fGrammar = grammar;
    }

    /**
     *  Write contents of the annotation to the specified DOM object. If the
     * specified <code>target</code> object is a DOM in-scope namespace
     * declarations for <code>annotation</code> element are added as
     * attributes nodes of the serialized <code>annotation</code>, otherwise
     * the corresponding events for all in-scope namespace declaration are
     * sent via specified document handler.
     * @param target  A target pointer to the annotation target object, i.e.
     *   <code>org.w3c.dom.Document</code>,
     *   <code>org.xml.sax.ContentHandler</code>.
     * @param targetType  A target type.
     * @return If the <code>target</code> is recognized type and supported by
     *   this implementation return true, otherwise return false.
     */
    public boolean writeAnnotation(Object target,
                                   short targetType) {
        if(targetType == XSAnnotation.W3C_DOM_ELEMENT || targetType == XSAnnotation.W3C_DOM_DOCUMENT) {
            writeToDOM((Node)target, targetType);
            return true;
        } else if (targetType == SAX_CONTENTHANDLER) {
            writeToSAX((ContentHandler)target);
            return true;
        }
        return false;
    }

    /**
     * A text representation of annotation.
     */
    public String getAnnotationString() {
        return fData;
    }

    // XSObject methods

    /**
     *  The <code>type</code> of this object, i.e.
     * <code>ELEMENT_DECLARATION</code>.
     */
    public short getType() {
        return XSConstants.ANNOTATION;
    }

    /**
     * The name of type <code>NCName</code> of this declaration as defined in
     * XML Namespaces.
     */
    public String getName() {
        return null;
    }

    /**
     *  The [target namespace] of this object, or <code>null</code> if it is
     * unspecified.
     */
    public String getNamespace() {
        return null;
    }

    /**
     * A namespace schema information item corresponding to the target
     * namespace of the component, if it's globally declared; or null
     * otherwise.
     */
    public XSNamespaceItem getNamespaceItem() {
        return null;
    }

    // private methods
    private synchronized void writeToSAX(ContentHandler handler) {
        // nothing must go wrong with this parse...
        SAXParser parser = fGrammar.getSAXParser();
        StringReader aReader = new StringReader(fData);
        InputSource aSource = new InputSource(aReader);
        parser.setContentHandler(handler);
        try {
            parser.parse(aSource);
        }
        catch (SAXException e) {
            // this should never happen!
            // REVISIT:  what to do with this?; should really not
            // eat it...
        }
        catch (IOException i) {
            // ditto with above
        }
        // Release the reference to the user's ContentHandler.
        parser.setContentHandler(null);
    }

    // this creates the new Annotation element as the first child
    // of the Node
    private synchronized void writeToDOM(Node target, short type) {
        Document futureOwner = (type == XSAnnotation.W3C_DOM_ELEMENT) ?
                target.getOwnerDocument() : (Document)target;
        DOMParser parser = fGrammar.getDOMParser();
        StringReader aReader = new StringReader(fData);
        InputSource aSource = new InputSource(aReader);
        try {
            parser.parse(aSource);
        }
        catch (SAXException e) {
            // this should never happen!
            // REVISIT:  what to do with this?; should really not
            // eat it...
        }
        catch (IOException i) {
            // ditto with above
        }
        Document aDocument = parser.getDocument();
        parser.dropDocumentReferences();
        Element annotation = aDocument.getDocumentElement();
        Node newElem = null;
        if (futureOwner instanceof CoreDocumentImpl) {
            newElem = futureOwner.adoptNode(annotation);
            // adoptNode will return null when the DOM implementations are not compatible.
            if (newElem == null) {
                newElem = futureOwner.importNode(annotation, true);
            }
        }
        else {
            newElem = futureOwner.importNode(annotation, true);
        }
        target.insertBefore(newElem, target.getFirstChild());
    }

}
