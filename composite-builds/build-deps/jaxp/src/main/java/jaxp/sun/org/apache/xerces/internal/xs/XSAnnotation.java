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
 * Copyright 2003,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.xs;

/**
 * This interface represents the Annotation schema component.
 */
public interface XSAnnotation extends XSObject {
    // TargetType
    /**
     * The object type is <code>org.w3c.dom.Element</code>.
     */
    public static final short W3C_DOM_ELEMENT           = 1;
    /**
     * The object type is <code>org.xml.sax.ContentHandler</code>.
     */
    public static final short SAX_CONTENTHANDLER        = 2;
    /**
     * The object type is <code>org.w3c.dom.Document</code>.
     */
    public static final short W3C_DOM_DOCUMENT          = 3;

    /**
     *  Write contents of the annotation to the specified object. If the
     * specified <code>target</code> is a DOM object, in-scope namespace
     * declarations for <code>annotation</code> element are added as
     * attribute nodes of the serialized <code>annotation</code>, otherwise
     * the corresponding events for all in-scope namespace declarations are
     * sent via the specified document handler.
     * @param target  A target pointer to the annotation target object, i.e.
     *   <code>org.w3c.dom.Document</code>, <code>org.w3c.dom.Element</code>
     *   , <code>org.xml.sax.ContentHandler</code>.
     * @param targetType  A target type.
     * @return  True if the <code>target</code> is a recognized type and
     *   supported by this implementation, otherwise false.
     */
    public boolean writeAnnotation(Object target,
                                   short targetType);

    /**
     * A text representation of the annotation.
     */
    public String getAnnotationString();

}
