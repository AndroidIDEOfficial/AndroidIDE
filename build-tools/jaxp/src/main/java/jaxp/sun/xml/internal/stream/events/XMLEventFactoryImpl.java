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
 * Copyright (c) 2005, Oracle and/or its affiliates. All rights reserved.
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

package jaxp.sun.xml.internal.stream.events;

import jaxp.xml.stream.XMLEventFactory;
import jaxp.xml.stream.Location;
import jaxp.xml.namespace.NamespaceContext;
import jaxp.xml.namespace.QName;
import jaxp.xml.stream.events.Attribute;
import jaxp.xml.stream.events.Characters;
import jaxp.xml.stream.events.Comment;
import jaxp.xml.stream.events.DTD;
import jaxp.xml.stream.events.EndDocument;
import jaxp.xml.stream.events.EndElement;
import jaxp.xml.stream.events.EntityReference;
import jaxp.xml.stream.events.Namespace;
import jaxp.xml.stream.events.ProcessingInstruction;
import jaxp.xml.stream.events.StartDocument;
import jaxp.xml.stream.events.StartElement;
import jaxp.xml.stream.events.EntityDeclaration;


/**
 *
 * @author  Neeraj Bajaj, k.venugopal@sun.com
 */
public class XMLEventFactoryImpl extends XMLEventFactory {

    Location location = null;
    /** Creates a new instance of XMLEventFactory */
    public XMLEventFactoryImpl() {
    }

    public Attribute createAttribute(String localName, String value) {
        AttributeImpl attr =  new AttributeImpl(localName, value);
        if(location != null)attr.setLocation(location);
        return attr;
    }

    public Attribute createAttribute(QName name, String value) {
        return createAttribute(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), value);
    }

    public Attribute createAttribute(String prefix, String namespaceURI, String localName, String value) {
        AttributeImpl attr =  new AttributeImpl(prefix, namespaceURI, localName, value, null);
        if(location != null)attr.setLocation(location);
        return attr;
    }

    public Characters createCData(String content) {
        //stax doesn't have separate CDATA event. This is taken care by
        //CHRACTERS event setting the cdata flag to true.
        CharacterEvent charEvent =  new CharacterEvent(content, true);
        if(location != null)charEvent.setLocation(location);
        return charEvent;
    }

    public Characters createCharacters(String content) {
        CharacterEvent charEvent =  new CharacterEvent(content);
        if(location != null)charEvent.setLocation(location);
        return charEvent;
    }

    public Comment createComment(String text) {
        CommentEvent charEvent =  new CommentEvent(text);
        if(location != null)charEvent.setLocation(location);
        return charEvent;
    }

    public DTD createDTD(String dtd) {
        DTDEvent dtdEvent = new DTDEvent(dtd);
        if(location != null)dtdEvent.setLocation(location);
        return dtdEvent;
    }

    public EndDocument createEndDocument() {
        EndDocumentEvent event =new EndDocumentEvent();
        if(location != null)event.setLocation(location);
        return event;
    }

    public EndElement createEndElement(QName name, java.util.Iterator namespaces) {
        return createEndElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart());
    }

    public EndElement createEndElement(String prefix, String namespaceUri, String localName) {
        EndElementEvent event =  new EndElementEvent(prefix, namespaceUri, localName);
        if(location != null)event.setLocation(location);
        return event;
    }

    public EndElement createEndElement(String prefix, String namespaceUri, String localName, java.util.Iterator namespaces) {

        EndElementEvent event =  new EndElementEvent(prefix, namespaceUri, localName);
        if(namespaces!=null){
            while(namespaces.hasNext())
                event.addNamespace((Namespace)namespaces.next());
        }
        if(location != null)event.setLocation(location);
        return event;
    }

    public EntityReference createEntityReference(String name, EntityDeclaration entityDeclaration) {
        EntityReferenceEvent event =  new EntityReferenceEvent(name, entityDeclaration);
        if(location != null)event.setLocation(location);
        return event;
    }

    public Characters createIgnorableSpace(String content) {
        CharacterEvent event =  new CharacterEvent(content, false, true);
        if(location != null)event.setLocation(location);
        return event;
    }

    public Namespace createNamespace(String namespaceURI) {
        NamespaceImpl event =  new NamespaceImpl(namespaceURI);
        if(location != null)event.setLocation(location);
        return event;
    }

    public Namespace createNamespace(String prefix, String namespaceURI) {
        NamespaceImpl event =  new NamespaceImpl(prefix, namespaceURI);
        if(location != null)event.setLocation(location);
        return event;
    }

    public ProcessingInstruction createProcessingInstruction(String target, String data) {
        ProcessingInstructionEvent event =  new ProcessingInstructionEvent(target, data);
        if(location != null)event.setLocation(location);
        return event;
    }

    public Characters createSpace(String content) {
        CharacterEvent event =  new CharacterEvent(content);
        if(location != null)event.setLocation(location);
        return event;
    }

    public StartDocument createStartDocument() {
        StartDocumentEvent event = new StartDocumentEvent();
        if(location != null)event.setLocation(location);
        return event;
    }

    public StartDocument createStartDocument(String encoding) {
        StartDocumentEvent event =  new StartDocumentEvent(encoding);
        if(location != null)event.setLocation(location);
        return event;
    }

    public StartDocument createStartDocument(String encoding, String version) {
        StartDocumentEvent event =  new StartDocumentEvent(encoding, version);
        if(location != null)event.setLocation(location);
        return event;
    }

    public StartDocument createStartDocument(String encoding, String version, boolean standalone) {
        StartDocumentEvent event =  new StartDocumentEvent(encoding, version, standalone);
        if(location != null)event.setLocation(location);
        return event;
    }

    public StartElement createStartElement(QName name, java.util.Iterator attributes, java.util.Iterator namespaces) {
        return createStartElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), attributes, namespaces);
    }

    public StartElement createStartElement(String prefix, String namespaceUri, String localName) {
        StartElementEvent event =  new StartElementEvent(prefix, namespaceUri, localName);
        if(location != null)event.setLocation(location);
        return event;
    }

    public StartElement createStartElement(String prefix, String namespaceUri, String localName, java.util.Iterator attributes, java.util.Iterator namespaces) {
        return createStartElement(prefix, namespaceUri, localName, attributes, namespaces, null);
    }

    public StartElement createStartElement(String prefix, String namespaceUri, String localName, java.util.Iterator attributes, java.util.Iterator namespaces, NamespaceContext context) {
        StartElementEvent elem =  new StartElementEvent(prefix, namespaceUri, localName);
        elem.addAttributes(attributes);
        elem.addNamespaceAttributes(namespaces);
        elem.setNamespaceContext(context);
        if(location != null)elem.setLocation(location);
        return elem;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
