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
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jaxp.sun.org.apache.xerces.internal.impl.xs.opti;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

/**
 * @xerces.internal
 *
 * @version $Id: SchemaDOMImplementation.java,v 1.2 2010-10-26 23:01:18 joehw Exp $
 */
final class SchemaDOMImplementation implements DOMImplementation {

    private static final SchemaDOMImplementation singleton = new SchemaDOMImplementation();

    /** NON-DOM: Obtain and return the single shared object */
    public static DOMImplementation getDOMImplementation() {
        return singleton;
    }

    private SchemaDOMImplementation() {}

    public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype)
            throws DOMException {
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }

    public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId)
            throws DOMException {
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Method not supported");
    }

    public Object getFeature(String feature, String version) {
        if (singleton.hasFeature(feature, version)) {
            return singleton;
        }
        return null;
    }

    public boolean hasFeature(String feature, String version) {
        final boolean anyVersion = version == null || version.length() == 0;
        return (feature.equalsIgnoreCase("Core") || feature.equalsIgnoreCase("XML")) &&
            (anyVersion || version.equals("1.0") || version.equals("2.0") || version.equals("3.0"));
    }

}
