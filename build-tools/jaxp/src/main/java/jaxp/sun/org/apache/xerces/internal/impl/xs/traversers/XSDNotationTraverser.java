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

package jaxp.sun.org.apache.xerces.internal.impl.xs.traversers;

import jaxp.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import jaxp.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import jaxp.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
import jaxp.sun.org.apache.xerces.internal.impl.xs.XSNotationDecl;
import jaxp.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import jaxp.sun.org.apache.xerces.internal.util.DOMUtil;
import jaxp.sun.org.apache.xerces.internal.xs.XSObjectList;
import org.w3c.dom.Element;

/**
 * The notation declaration schema component traverser.
 *
 * <notation
 *   id = ID
 *   name = NCName
 *   public = anyURI
 *   system = anyURI
 *   {any attributes with non-schema namespace . . .}>
 *   Content: (annotation?)
 * </notation>
 *
 * @xerces.internal
 *
 * @author Rahul Srivastava, Sun Microsystems Inc.
 * @author Elena Litani, IBM
 * @version $Id: XSDNotationTraverser.java,v 1.7 2010-11-01 04:40:02 joehw Exp $
 */
class  XSDNotationTraverser extends XSDAbstractTraverser {

    XSDNotationTraverser (XSDHandler handler,
            XSAttributeChecker gAttrCheck) {
        super(handler, gAttrCheck);
    }

    XSNotationDecl traverse(Element elmNode,
                            XSDocumentInfo schemaDoc,
                            SchemaGrammar grammar) {

        // General Attribute Checking for elmNode
        Object[] attrValues = fAttrChecker.checkAttributes(elmNode, true, schemaDoc);
        //get attributes
        String  nameAttr   = (String) attrValues[XSAttributeChecker.ATTIDX_NAME];

        String  publicAttr = (String) attrValues[XSAttributeChecker.ATTIDX_PUBLIC];
        String  systemAttr = (String) attrValues[XSAttributeChecker.ATTIDX_SYSTEM];
        if (nameAttr == null) {
            reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_NOTATION, SchemaSymbols.ATT_NAME}, elmNode);
            fAttrChecker.returnAttrArray(attrValues, schemaDoc);
            return null;
        }

        if (systemAttr == null && publicAttr == null) {
            reportSchemaError("PublicSystemOnNotation", null, elmNode);
            publicAttr = "missing";
        }

        XSNotationDecl notation = new XSNotationDecl();
        notation.fName = nameAttr;
        notation.fTargetNamespace = schemaDoc.fTargetNamespace;
        notation.fPublicId = publicAttr;
        notation.fSystemId = systemAttr;

        //check content
        Element content = DOMUtil.getFirstChildElement(elmNode);
        XSAnnotationImpl annotation = null;

        if (content != null && DOMUtil.getLocalName(content).equals(SchemaSymbols.ELT_ANNOTATION)) {
            annotation = traverseAnnotationDecl(content, attrValues, false, schemaDoc);
            content = DOMUtil.getNextSiblingElement(content);
        }
        else {
            String text = DOMUtil.getSyntheticAnnotation(elmNode);
            if (text != null) {
                annotation = traverseSyntheticAnnotation(elmNode, text, attrValues, false, schemaDoc);
            }
        }
        XSObjectList annotations;
        if (annotation != null) {
            annotations = new XSObjectListImpl();
            ((XSObjectListImpl) annotations).addXSObject(annotation);
        } else {
            annotations = XSObjectListImpl.EMPTY_LIST;
        }
        notation.fAnnotations = annotations;
        if (content!=null){
            Object[] args = new Object [] {SchemaSymbols.ELT_NOTATION, "(annotation?)", DOMUtil.getLocalName(content)};
            reportSchemaError("s4s-elt-must-match.1", args, content);

        }
        if (grammar.getGlobalNotationDecl(notation.fName) == null) {
            grammar.addGlobalNotationDecl(notation);
        }

        // also add it to extended map
        final String loc = fSchemaHandler.schemaDocument2SystemId(schemaDoc);
        final XSNotationDecl notation2 = grammar.getGlobalNotationDecl(notation.fName, loc);
        if (notation2 == null) {
            grammar.addGlobalNotationDecl(notation, loc);
        }

        // handle duplicates
        if (fSchemaHandler.fTolerateDuplicates) {
            if (notation2 != null) {
                notation = notation2;
            }
            fSchemaHandler.addGlobalNotationDecl(notation);
        }
        fAttrChecker.returnAttrArray(attrValues, schemaDoc);

        return notation;
    }
}
