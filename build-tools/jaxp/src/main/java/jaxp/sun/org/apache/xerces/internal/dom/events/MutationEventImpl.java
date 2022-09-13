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

package jaxp.sun.org.apache.xerces.internal.dom.events;

import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

/**
 * @xerces.internal
 *
 */

public class MutationEventImpl
extends EventImpl
implements MutationEvent
{
    Node relatedNode=null;
    String prevValue=null,newValue=null,attrName=null;
    // REVISIT: The DOM Level 2 PR has a bug: the init method should let this
    // attribute be specified. Since it doesn't we have to give write access.
    public short attrChange;

    // NON-DOM CONSTANTS: Storage efficiency, avoid risk of typos.
    public static final String DOM_SUBTREE_MODIFIED = "DOMSubtreeModified";
    public static final String DOM_NODE_INSERTED = "DOMNodeInserted";
    public static final String DOM_NODE_REMOVED = "DOMNodeRemoved";
    public static final String DOM_NODE_REMOVED_FROM_DOCUMENT = "DOMNodeRemovedFromDocument";
    public static final String DOM_NODE_INSERTED_INTO_DOCUMENT = "DOMNodeInsertedIntoDocument";
    public static final String DOM_ATTR_MODIFIED = "DOMAttrModified";
    public static final String DOM_CHARACTER_DATA_MODIFIED = "DOMCharacterDataModified";

    /** @return the name of the Attr which
        changed, for DOMAttrModified events.
        Undefined for others.
        */
    public String getAttrName()
    {
        return attrName;
    }

    /**
     *  <code>attrChange</code> indicates the type of change which triggered
     * the DOMAttrModified event. The values can be <code>MODIFICATION</code>
     * , <code>ADDITION</code>, or <code>REMOVAL</code>.
     */
    public short getAttrChange()
    {
        return attrChange;
    }

    /** @return the new string value of the Attr for DOMAttrModified events, or
        of the CharacterData node for DOMCharDataModifed events.
        Undefined for others.
        */
    public String getNewValue()
    {
        return newValue;
    }

    /** @return the previous string value of the Attr for DOMAttrModified events, or
        of the CharacterData node for DOMCharDataModifed events.
        Undefined for others.
        */
    public String getPrevValue()
    {
        return prevValue;
    }

    /** @return a Node related to this event, other than the target that the
        node was dispatched to. For DOMNodeRemoved, it is the node which
        was removed.
        No other uses are currently defined.
        */
    public Node getRelatedNode()
    {
        return relatedNode;
    }

    /** Initialize a mutation event, or overwrite the event's current
        settings with new values of the parameters.
        */
    public void initMutationEvent(String typeArg, boolean canBubbleArg,
        boolean cancelableArg, Node relatedNodeArg, String prevValueArg,
        String newValueArg, String attrNameArg, short attrChangeArg)
    {
        relatedNode=relatedNodeArg;
        prevValue=prevValueArg;
        newValue=newValueArg;
        attrName=attrNameArg;
        attrChange=attrChangeArg;
        super.initEvent(typeArg,canBubbleArg,cancelableArg);
    }

}
