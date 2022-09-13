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

import jaxp.xml.transform.dom.DOMResult;

import jaxp.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import jaxp.sun.org.apache.xerces.internal.xni.XNIException;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * <p>An extension to XMLDocumentHandler for building DOM structures.</p>
 *
 * @author Michael Glavassevich, IBM
 */
interface DOMDocumentHandler extends XMLDocumentHandler {

    /**
     * <p>Sets the <code>DOMResult</code> object which
     * receives the constructed DOM nodes.</p>
     *
     * @param result the object which receives the constructed DOM nodes
     */
    public void setDOMResult(DOMResult result);

    /**
     * A document type declaration.
     *
     * @param node a DocumentType node
     *
     * @exception XNIException Thrown by handler to signal an error.
     */
    public void doctypeDecl(DocumentType node) throws XNIException;

    public void characters(Text node) throws XNIException;

    public void cdata(CDATASection node) throws XNIException;

    /**
     * A comment.
     *
     * @param node a Comment node
     *
     * @exception XNIException Thrown by application to signal an error.
     */
    public void comment(Comment node) throws XNIException;

    /**
     * A processing instruction. Processing instructions consist of a
     * target name and, optionally, text data. The data is only meaningful
     * to the application.
     * <p>
     * Typically, a processing instruction's data will contain a series
     * of pseudo-attributes. These pseudo-attributes follow the form of
     * element attributes but are <strong>not</strong> parsed or presented
     * to the application as anything other than text. The application is
     * responsible for parsing the data.
     *
     * @param node a ProcessingInstruction node
     *
     * @exception XNIException Thrown by handler to signal an error.
     */
    public void processingInstruction(ProcessingInstruction node) throws XNIException;

    public void setIgnoringCharacters(boolean ignore);
}
