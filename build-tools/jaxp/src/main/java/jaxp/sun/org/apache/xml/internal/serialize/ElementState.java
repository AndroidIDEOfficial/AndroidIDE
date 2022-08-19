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


package jaxp.sun.org.apache.xml.internal.serialize;


import java.util.Hashtable;


/**
 * Holds the state of the currently serialized element.
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @see BaseMarkupSerializer
 */
public class ElementState
{


    /**
     * The element's raw tag name (local or prefix:local).
     */
    public String rawName;


    /**
     * The element's local tag name.
     */
    public String localName;


    /**
     * The element's namespace URI.
     */
    public String namespaceURI;


    /**
     * True if element is space preserving.
     */
    public boolean preserveSpace;


    /**
     * True if element is empty. Turns false immediately
     * after serializing the first contents of the element.
     */
    public boolean empty;


    /**
     * True if the last serialized node was an element node.
     */
    public boolean afterElement;


    /**
     * True if the last serialized node was a comment node.
     */
    public boolean afterComment;


    /**
     * True if textual content of current element should be
     * serialized as CDATA section.
     */
    public boolean doCData;


    /**
     * True if textual content of current element should be
     * serialized as raw characters (unescaped).
     */
    public boolean unescaped;


    /**
     * True while inside CData and printing text as CData.
     */
    public boolean inCData;


    /**
     * Association between namespace URIs (keys) and prefixes (values).
     */
    public Hashtable prefixes;


}
