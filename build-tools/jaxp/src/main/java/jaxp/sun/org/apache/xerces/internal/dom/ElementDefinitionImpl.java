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
 * Copyright 1999-2002,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.dom;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * NON-DOM CLASS: Describe one of the Elements (and its associated
 * Attributes) defined in this Document Type.
 * <p>
 * I've included this in Level 1 purely as an anchor point for default
 * attributes. In Level 2 it should enable the ChildRule support.
 *
 * @xerces.internal
 *
 */
public class ElementDefinitionImpl
    extends ParentNode {

    //
    // Constants
    //

    /** Serialization version. */
    static final long serialVersionUID = -8373890672670022714L;

    //
    // Data
    //

    /** Element definition name. */
    protected String name;

    /** Default attributes. */
    protected NamedNodeMapImpl attributes;

    //
    // Constructors
    //

    /** Factory constructor. */
    public ElementDefinitionImpl(CoreDocumentImpl ownerDocument, String name) {
        super(ownerDocument);
        this.name = name;
        attributes = new NamedNodeMapImpl(ownerDocument);
    }

    //
    // Node methods
    //

    /**
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return ELEMENT_DEFINITION_NODE;
    }

    /**
     * Returns the element definition name
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return name;
    }

    /**
     * Replicate this object.
     */
    public Node cloneNode(boolean deep) {

        ElementDefinitionImpl newnode =
            (ElementDefinitionImpl) super.cloneNode(deep);
        // NamedNodeMap must be explicitly replicated to avoid sharing
        newnode.attributes = attributes.cloneMap(newnode);
        return newnode;

    } // cloneNode(boolean):Node

    /**
     * Query the attributes defined on this Element.
     * <p>
     * In the base implementation this Map simply contains Attribute objects
     * representing the defaults. In a more serious implementation, it would
     * contain AttributeDefinitionImpl objects for all declared Attributes,
     * indicating which are Default, DefaultFixed, Implicit and/or Required.
     *
     * @return org.w3c.dom.NamedNodeMap containing org.w3c.dom.Attribute
     */
    public NamedNodeMap getAttributes() {

        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return attributes;

    } // getAttributes():NamedNodeMap

} // class ElementDefinitionImpl
