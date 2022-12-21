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

package jaxp.sun.org.apache.xerces.internal.jaxp;

import java.util.HashMap;

import jaxp.sun.org.apache.xerces.internal.impl.validation.EntityState;
import jaxp.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import jaxp.sun.org.apache.xerces.internal.xni.Augmentations;
import jaxp.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
import jaxp.sun.org.apache.xerces.internal.xni.XMLLocator;
import jaxp.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import jaxp.sun.org.apache.xerces.internal.xni.XMLString;
import jaxp.sun.org.apache.xerces.internal.xni.XNIException;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLDTDFilter;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;

/**
 * <p>This filter records which unparsed entities have been
 * declared in the DTD and provides this information to a ValidationManager.
 * Events are forwarded to the registered XMLDTDHandler without modification.</p>
 *
 * @author Michael Glavassevich, IBM
 * @version $Id: UnparsedEntityHandler.java,v 1.6 2010-11-01 04:40:07 joehw Exp $
 */
final class UnparsedEntityHandler implements XMLDTDFilter, EntityState {

    /** DTD source and handler. **/
    private XMLDTDSource fDTDSource;
    private XMLDTDHandler fDTDHandler;

    /** Validation manager. */
    private final ValidationManager fValidationManager;

    /** Map for tracking unparsed entities. */
    private HashMap fUnparsedEntities = null;

    UnparsedEntityHandler(ValidationManager manager) {
        fValidationManager = manager;
    }

    /*
     * XMLDTDHandler methods
     */

    public void startDTD(XMLLocator locator, Augmentations augmentations)
            throws XNIException {
        fValidationManager.setEntityState(this);
        if (fDTDHandler != null) {
            fDTDHandler.startDTD(locator, augmentations);
        }
    }

    public void startParameterEntity(String name,
                                     XMLResourceIdentifier identifier, String encoding,
                                     Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.startParameterEntity(name, identifier, encoding, augmentations);
        }
    }

    public void textDecl(String version, String encoding,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.textDecl(version, encoding, augmentations);
        }
    }

    public void endParameterEntity(String name, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.endParameterEntity(name, augmentations);
        }
    }

    public void startExternalSubset(XMLResourceIdentifier identifier,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.startExternalSubset(identifier, augmentations);
        }
    }

    public void endExternalSubset(Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.endExternalSubset(augmentations);
        }
    }

    public void comment(XMLString text, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.comment(text, augmentations);
        }
    }

    public void processingInstruction(String target, XMLString data,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.processingInstruction(target, data, augmentations);
        }
    }

    public void elementDecl(String name, String contentModel,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.elementDecl(name, contentModel, augmentations);
        }
    }

    public void startAttlist(String elementName, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.startAttlist(elementName, augmentations);
        }
    }

    public void attributeDecl(String elementName, String attributeName,
            String type, String[] enumeration, String defaultType,
            XMLString defaultValue, XMLString nonNormalizedDefaultValue,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.attributeDecl(elementName, attributeName,
                    type, enumeration, defaultType,
                    defaultValue, nonNormalizedDefaultValue,
                    augmentations);
        }
    }

    public void endAttlist(Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.endAttlist(augmentations);
        }
    }

    public void internalEntityDecl(String name, XMLString text,
            XMLString nonNormalizedText, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.internalEntityDecl(name, text,
                    nonNormalizedText, augmentations);
        }
    }

    public void externalEntityDecl(String name,
            XMLResourceIdentifier identifier, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.externalEntityDecl(name, identifier, augmentations);
        }
    }

    public void unparsedEntityDecl(String name,
            XMLResourceIdentifier identifier, String notation,
            Augmentations augmentations) throws XNIException {
        if (fUnparsedEntities == null) {
            fUnparsedEntities = new HashMap();
        }
        fUnparsedEntities.put(name, name);
        if (fDTDHandler != null) {
            fDTDHandler.unparsedEntityDecl(name, identifier, notation, augmentations);
        }
    }

    public void notationDecl(String name, XMLResourceIdentifier identifier,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.notationDecl(name, identifier, augmentations);
        }
    }

    public void startConditional(short type, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.startConditional(type, augmentations);
        }
    }

    public void ignoredCharacters(XMLString text, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.ignoredCharacters(text, augmentations);
        }

    }

    public void endConditional(Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.endConditional(augmentations);
        }
    }

    public void endDTD(Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.endDTD(augmentations);
        }
    }

    public void setDTDSource(XMLDTDSource source) {
        fDTDSource = source;
    }

    public XMLDTDSource getDTDSource() {
        return fDTDSource;
    }

    /*
     * XMLDTDSource methods
     */

    public void setDTDHandler(XMLDTDHandler handler) {
        fDTDHandler = handler;
    }

    public XMLDTDHandler getDTDHandler() {
        return fDTDHandler;
    }

    /*
     * EntityState methods
     */

    public boolean isEntityDeclared(String name) {
        return false;
    }

    public boolean isEntityUnparsed(String name) {
        if (fUnparsedEntities != null) {
            return fUnparsedEntities.containsKey(name);
        }
        return false;
    }

    /*
     * Other methods
     */

    public void reset() {
        if (fUnparsedEntities != null && !fUnparsedEntities.isEmpty()) {
            // should only clear this if the last document contained unparsed entities
            fUnparsedEntities.clear();
        }
    }
}
