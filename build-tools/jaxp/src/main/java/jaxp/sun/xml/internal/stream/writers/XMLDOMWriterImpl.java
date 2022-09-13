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
 * Copyright (c) 2005, 2006, Oracle and/or its affiliates. All rights reserved.
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

package jaxp.sun.xml.internal.stream.writers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import jaxp.xml.XMLConstants;
import jaxp.xml.namespace.NamespaceContext;
import jaxp.xml.stream.XMLStreamException;
import jaxp.xml.stream.XMLStreamWriter;
import jaxp.xml.transform.dom.DOMResult;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This class provides support to build a DOM tree using XMLStreamWriter API's.
 * @author K.Venugopal@sun.com
 */

/*
 * TODO : -Venu
 * Internal NamespaceManagement
 * setPrefix
 * support for isRepairNamespace property.
 * Some Unsupported Methods.
 * Change StringBuffer to StringBuilder, when JDK 1.5 will be minimum requirement for SJSXP.
 */

public class XMLDOMWriterImpl implements XMLStreamWriter  {


    private Document ownerDoc = null;
    private Node currentNode = null;
    private Node node = null;
    private NamespaceSupport namespaceContext = null;
    private Method  mXmlVersion = null;
    private boolean [] needContextPop = null;
    private StringBuffer stringBuffer = null;
    private int resizeValue = 20;
    private int depth = 0;
    /**
     * Creates a new instance of XMLDOMwriterImpl
     * @param result DOMResult object @javax.xml.transform.dom.DOMResult
     */
    public XMLDOMWriterImpl(DOMResult result) {

        node = result.getNode();
        if( node.getNodeType() == Node.DOCUMENT_NODE){
            ownerDoc = (Document)node;
            currentNode = ownerDoc;
        }else{
            ownerDoc = node.getOwnerDocument();
            currentNode = node;
        }
        getDLThreeMethods();
        stringBuffer = new StringBuffer();
        needContextPop = new boolean[resizeValue];
        namespaceContext = new NamespaceSupport();
    }

    private void getDLThreeMethods(){
        try{
            mXmlVersion =  ownerDoc.getClass().getMethod("setXmlVersion",new Class[] {String.class});
        }catch(NoSuchMethodException mex){
            //log these errors at fine level.
            mXmlVersion = null;
        }catch(SecurityException se){
            //log these errors at fine level.
            mXmlVersion = null;
        }
    }


    /**
     * This method has no effect when called.
     * @throws XMLStreamException {@inheritDoc}
     */
    public void close() throws XMLStreamException {
        //no-op
    }

    /**
     * This method has no effect when called.
     * @throws XMLStreamException {@inheritDoc}
     */
    public void flush() throws XMLStreamException {
        //no-op
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    public NamespaceContext getNamespaceContext() {
        return null;
    }

    /**
     * {@inheritDoc}
     * @param namespaceURI {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     * @return {@inheritDoc}
     */
    public String getPrefix(String namespaceURI) throws XMLStreamException {
        String prefix = null;
        if(this.namespaceContext != null){
            prefix = namespaceContext.getPrefix(namespaceURI);
        }
        return prefix;
    }

    /**
     * Is not supported in this implementation.
     * @param str {@inheritDoc}
     * @throws java.lang.IllegalArgumentException {@inheritDoc}
     * @return {@inheritDoc}
     */
    public Object getProperty(String str) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    /**
     * Is not supported in this version of the implementation.
     * @param uri {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        namespaceContext.declarePrefix(XMLConstants.DEFAULT_NS_PREFIX, uri);
        if(!needContextPop[depth]){
            needContextPop[depth] = true;
        }
    }

    /**
     * {@inheritDoc}
     * @param namespaceContext {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void setNamespaceContext(NamespaceContext namespaceContext) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    /**
     * Is not supported in this version of the implementation.
     * @param prefix {@inheritDoc}
     * @param uri {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        if(prefix == null){
            throw new XMLStreamException("Prefix cannot be null");
        }
        namespaceContext.declarePrefix(prefix, uri);
        if(!needContextPop[depth]){
            needContextPop[depth] = true;
        }
    }

    /**
     * Creates a DOM Atrribute @see org.w3c.dom.Node and associates it with the current DOM element @see org.w3c.dom.Node.
     * @param localName {@inheritDoc}
     * @param value {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeAttribute(String localName, String value) throws XMLStreamException {

        if(currentNode.getNodeType() == Node.ELEMENT_NODE){
            Attr attr = ownerDoc.createAttribute(localName);
            attr.setValue(value);
            ((Element)currentNode).setAttributeNode(attr);
        }else{
            //Convert node type to String
            throw new IllegalStateException("Current DOM Node type  is "+ currentNode.getNodeType() +
                    "and does not allow attributes to be set ");
        }
    }

    /**
     * Creates a DOM Atrribute @see org.w3c.dom.Node and associates it with the current DOM element @see org.w3c.dom.Node.
     * @param namespaceURI {@inheritDoc}
     * @param localName {@inheritDoc}
     * @param value {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeAttribute(String namespaceURI,String localName,String value)throws XMLStreamException {
        if(currentNode.getNodeType() == Node.ELEMENT_NODE){
            String prefix = null;
            if(namespaceURI == null ){
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if(localName == null){
                throw new XMLStreamException("Local name cannot be null");
            }
            if(namespaceContext != null){
                prefix = namespaceContext.getPrefix(namespaceURI);
            }

            if(prefix == null){
                throw new XMLStreamException("Namespace URI "+namespaceURI +
                        "is not bound to any prefix" );
            }

            String qualifiedName = null;
            if(prefix.equals("")){
                qualifiedName = localName;
            }else{
                qualifiedName = getQName(prefix,localName);
            }
            Attr attr = ownerDoc.createAttributeNS(namespaceURI, qualifiedName);
            attr.setValue(value);
            ((Element)currentNode).setAttributeNode(attr);
        }else{
            //Convert node type to String
            throw new IllegalStateException("Current DOM Node type  is "+ currentNode.getNodeType() +
                    "and does not allow attributes to be set ");
        }
    }

    /**
     * Creates a DOM Atrribute @see org.w3c.dom.Node and associates it with the current DOM element @see org.w3c.dom.Node.
     * @param prefix {@inheritDoc}
     * @param namespaceURI {@inheritDoc}
     * @param localName {@inheritDoc}
     * @param value {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeAttribute(String prefix,String namespaceURI,String localName,String value)throws XMLStreamException {
        if(currentNode.getNodeType() == Node.ELEMENT_NODE){
            if(namespaceURI == null ){
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if(localName == null){
                throw new XMLStreamException("Local name cannot be null");
            }
            if(prefix == null){
                throw new XMLStreamException("prefix cannot be null");
            }
            String qualifiedName = null;
            if(prefix.equals("")){
                qualifiedName = localName;
            }else{

                qualifiedName = getQName(prefix,localName);
            }
            Attr attr = ownerDoc.createAttributeNS(namespaceURI, qualifiedName);
            attr.setValue(value);
            ((Element)currentNode).setAttributeNodeNS(attr);
        }else{
            //Convert node type to String
            throw new IllegalStateException("Current DOM Node type  is "+ currentNode.getNodeType() +
                    "and does not allow attributes to be set ");
        }

    }

    /**
     * Creates a CDATA object @see org.w3c.dom.CDATASection.
     * @param data {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeCData(String data) throws XMLStreamException {
        if(data == null){
            throw new XMLStreamException("CDATA cannot be null");
        }

        CDATASection cdata = ownerDoc.createCDATASection(data);
        getNode().appendChild(cdata);
    }

    /**
     * Creates a character object @see org.w3c.dom.Text and appends it to the current
     * element in the DOM tree.
     * @param charData {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeCharacters(String charData) throws XMLStreamException {
        Text text = ownerDoc.createTextNode(charData);
        currentNode.appendChild(text);
    }

    /**
     * Creates a character object @see org.w3c.dom.Text and appends it to the current
     * element in the DOM tree.
     * @param values {@inheritDoc}
     * @param param {@inheritDoc}
     * @param param2 {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeCharacters(char[] values, int param, int param2) throws XMLStreamException {

        Text text = ownerDoc.createTextNode(new String(values,param,param2));
        currentNode.appendChild(text);
    }

    /**
     * Creates a Comment object @see org.w3c.dom.Comment and appends it to the current
     * element in the DOM tree.
     * @param str {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeComment(String str) throws XMLStreamException {
        Comment comment = ownerDoc.createComment(str);
        getNode().appendChild(comment);
    }

    /**
     * This method is not supported in this implementation.
     * @param str {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeDTD(String str) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a DOM attribute and adds it to the current element in the DOM tree.
     * @param namespaceURI {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        if(currentNode.getNodeType() == Node.ELEMENT_NODE){
            String qname = XMLConstants.XMLNS_ATTRIBUTE;
            ((Element)currentNode).setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI,qname, namespaceURI);
        }else{
            //Convert node type to String
            throw new IllegalStateException("Current DOM Node type  is "+ currentNode.getNodeType() +
                    "and does not allow attributes to be set ");
        }
    }

    /**
     * creates a DOM Element and appends it to the current element in the tree.
     * @param localName {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeEmptyElement(String localName) throws XMLStreamException {
        if(ownerDoc != null){
            Element element = ownerDoc.createElement(localName);
            if(currentNode!=null){
                currentNode.appendChild(element);
            }else{
                ownerDoc.appendChild(element);
            }
        }

    }

    /**
     * creates a DOM Element and appends it to the current element in the tree.
     * @param namespaceURI {@inheritDoc}
     * @param localName {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        if(ownerDoc != null){
            String qualifiedName = null;
            String prefix = null;
            if(namespaceURI == null ){
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if(localName == null){
                throw new XMLStreamException("Local name cannot be null");
            }

            if(namespaceContext != null){
                prefix = namespaceContext.getPrefix(namespaceURI);
            }
            if(prefix == null){
                throw new XMLStreamException("Namespace URI "+namespaceURI +
                        "is not bound to any prefix" );
            }
            if("".equals(prefix)){
                qualifiedName = localName;
            }else{

                qualifiedName = getQName(prefix,localName);

            }
            Element element = ownerDoc.createElementNS(namespaceURI, qualifiedName);
            if(currentNode!=null){
                currentNode.appendChild(element);
            }else{
                ownerDoc.appendChild(element);
            }
            //currentNode = element;
        }
    }

    /**
     * creates a DOM Element and appends it to the current element in the tree.
     * @param prefix {@inheritDoc}
     * @param localName {@inheritDoc}
     * @param namespaceURI {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        if(ownerDoc != null){
            if(namespaceURI == null ){
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if(localName == null){
                throw new XMLStreamException("Local name cannot be null");
            }
            if(prefix == null){
                throw new XMLStreamException("Prefix cannot be null");
            }
            String qualifiedName = null;
            if("".equals(prefix)){
                qualifiedName = localName;
            }else{
                qualifiedName = getQName(prefix,localName);
            }
            Element el  = ownerDoc.createElementNS(namespaceURI,qualifiedName);
            if(currentNode!=null){
                currentNode.appendChild(el);
            }else{
                ownerDoc.appendChild(el);
            }

        }
    }

    /**
     * Will reset current Node pointer maintained by the implementation.
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeEndDocument() throws XMLStreamException {
        //What do you want me to do eh! :)
        currentNode = null;
        for(int i=0; i< depth;i++){
            if(needContextPop[depth]){
                needContextPop[depth] = false;
                namespaceContext.popContext();
            }
            depth--;
        }
        depth =0;
    }

    /**
     * Internal current Node pointer will point to the parent of the current Node.
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeEndElement() throws XMLStreamException {
        Node node= currentNode.getParentNode();
        if(currentNode.getNodeType() == Node.DOCUMENT_NODE){
            currentNode = null;
        }else{
            currentNode = node;
        }
        if(needContextPop[depth]){
            needContextPop[depth] = false;
            namespaceContext.popContext();
        }
        depth--;
    }

    /**
     * Is not supported in this implementation.
     * @param name {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeEntityRef(String name) throws XMLStreamException {
        EntityReference er = ownerDoc.createEntityReference(name);
        currentNode.appendChild(er);
    }

    /**
     * creates a namespace attribute and will associate it with the current element in
     * the DOM tree.
     * @param prefix {@inheritDoc}
     * @param namespaceURI {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {

        if (prefix == null) {
            throw new XMLStreamException("prefix cannot be null");
        }

        if (namespaceURI == null) {
            throw new XMLStreamException("NamespaceURI cannot be null");
        }

        String qname = null;

        if (prefix.equals("")) {
            qname = XMLConstants.XMLNS_ATTRIBUTE;
        } else {
            qname = getQName(XMLConstants.XMLNS_ATTRIBUTE,prefix);
        }

        ((Element)currentNode).setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI,qname, namespaceURI);
    }

    /**
     * is not supported in this release.
     * @param target {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeProcessingInstruction(String target) throws XMLStreamException {
        if(target == null){
            throw new XMLStreamException("Target cannot be null");
        }
        ProcessingInstruction pi = ownerDoc.createProcessingInstruction(target, "");
        currentNode.appendChild(pi);
    }

    /**
     * is not supported in this release.
     * @param target {@inheritDoc}
     * @param data {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        if(target == null){
            throw new XMLStreamException("Target cannot be null");
        }
        ProcessingInstruction pi  = ownerDoc.createProcessingInstruction(target, data);
        currentNode.appendChild(pi);
    }

    /**
     * will set version on the Document object when the DOM Node passed to this implementation
     * supports DOM Level3 API's.
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeStartDocument() throws XMLStreamException {
        try{
            if(mXmlVersion != null){
                mXmlVersion.invoke(ownerDoc, new Object[] {"1.0"});
            }
        }catch(IllegalAccessException iae){
            throw new XMLStreamException(iae);
        }catch(InvocationTargetException ite){
            throw new XMLStreamException(ite);
        }
    }

    /**
     * will set version on the Document object when the DOM Node passed to this implementation
     * supports DOM Level3 API's.
     * @param version {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeStartDocument(String version) throws XMLStreamException {
        try{
            if(mXmlVersion != null){
                mXmlVersion.invoke(ownerDoc, new Object[] {version});
            }
        }catch(IllegalAccessException iae){
            throw new XMLStreamException(iae);
        }catch(InvocationTargetException ite){
            throw new XMLStreamException(ite);
        }
    }

    /**
     * will set version on the Document object when the DOM Node passed to this implementation
     * supports DOM Level3 API's.
     * @param encoding {@inheritDoc}
     * @param version {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        try{
            if(mXmlVersion != null){
                mXmlVersion.invoke(ownerDoc, new Object[] {version});
            }
        }catch(IllegalAccessException iae){
            throw new XMLStreamException(iae);
        }catch(InvocationTargetException ite){
            throw new XMLStreamException(ite);
        }
        //TODO: What to do with encoding.-Venu
    }

    /**
     * creates a DOM Element and appends it to the current element in the tree.
     * @param localName {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeStartElement(String localName) throws XMLStreamException {
        if(ownerDoc != null){
            Element element = ownerDoc.createElement(localName);
            if(currentNode!=null){
                currentNode.appendChild(element);
            }else{
                ownerDoc.appendChild(element);
            }
            currentNode = element;
        }
        if(needContextPop[depth]){
            namespaceContext.pushContext();
        }
        incDepth();
    }

    /**
     * creates a DOM Element and appends it to the current element in the tree.
     * @param namespaceURI {@inheritDoc}
     * @param localName {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        if(ownerDoc != null){
            String qualifiedName = null;
            String prefix = null;

            if(namespaceURI == null ){
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if(localName == null){
                throw new XMLStreamException("Local name cannot be null");
            }

            if(namespaceContext != null){
                prefix = namespaceContext.getPrefix(namespaceURI);
            }
            if(prefix == null){
                throw new XMLStreamException("Namespace URI "+namespaceURI +
                        "is not bound to any prefix" );
            }
            if("".equals(prefix)){
                qualifiedName = localName;
            }else{
                qualifiedName =  getQName(prefix,localName);
            }

            Element element = ownerDoc.createElementNS(namespaceURI, qualifiedName);

            if(currentNode!=null){
                currentNode.appendChild(element);
            }else{
                ownerDoc.appendChild(element);
            }
            currentNode = element;
        }
        if(needContextPop[depth]){
            namespaceContext.pushContext();
        }
        incDepth();
    }

    /**
     * creates a DOM Element and appends it to the current element in the tree.
     * @param prefix {@inheritDoc}
     * @param localName {@inheritDoc}
     * @param namespaceURI {@inheritDoc}
     * @throws XMLStreamException {@inheritDoc}
     */
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {

        if(ownerDoc != null){
            String qname = null;
            if(namespaceURI == null ){
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if(localName == null){
                throw new XMLStreamException("Local name cannot be null");
            }
            if(prefix == null){
                throw new XMLStreamException("Prefix cannot be null");
            }

            if(prefix.equals("")){
                qname = localName;
            }else{
                qname = getQName(prefix,localName);
            }

            Element el = ownerDoc.createElementNS(namespaceURI,qname);

            if(currentNode!=null){
                currentNode.appendChild(el);
            }else{
                ownerDoc.appendChild(el);
            }
            currentNode = el;
            if(needContextPop[depth]){
                namespaceContext.pushContext();
            }
            incDepth();

        }
    }

    private String getQName(String prefix , String localName){
        stringBuffer.setLength(0);
        stringBuffer.append(prefix);
        stringBuffer.append(":");
        stringBuffer.append(localName);
        return stringBuffer.toString();
    }

    private Node getNode(){
        if(currentNode == null){
            return ownerDoc;
        } else{
            return currentNode;
        }
    }
    private void incDepth() {
        depth++;
        if (depth == needContextPop.length) {
            boolean[] array = new boolean[depth + resizeValue];
            System.arraycopy(needContextPop, 0, array, 0, depth);
            needContextPop = array;
        }
    }
}
