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
 * Copyright 2001-2005 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.impl.dv.xs;

import jaxp.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import jaxp.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import jaxp.sun.org.apache.xerces.internal.util.XMLChar;
import jaxp.sun.org.apache.xerces.internal.xni.QName;
import jaxp.sun.org.apache.xerces.internal.xs.datatypes.XSQName;

/**
 * Represent the schema type "QName" and "NOTATION"
 *
 * @xerces.internal
 *
 * @author Neeraj Bajaj, Sun Microsystems, inc.
 * @author Sandy Gao, IBM
 *
 */
public class QNameDV extends TypeValidator {

    private static final String EMPTY_STRING = "".intern();

    public short getAllowedFacets() {
        return (XSSimpleTypeDecl.FACET_LENGTH | XSSimpleTypeDecl.FACET_MINLENGTH | XSSimpleTypeDecl.FACET_MAXLENGTH | XSSimpleTypeDecl.FACET_PATTERN | XSSimpleTypeDecl.FACET_ENUMERATION | XSSimpleTypeDecl.FACET_WHITESPACE);
    }

    public Object getActualValue(String content, ValidationContext context)
        throws InvalidDatatypeValueException {

        // "prefix:localpart" or "localpart"
        // get prefix and local part out of content
        String prefix, localpart;
        int colonptr = content.indexOf(":");
        if (colonptr > 0) {
            prefix = context.getSymbol(content.substring(0,colonptr));
            localpart = content.substring(colonptr+1);
        } else {
            prefix = EMPTY_STRING;
            localpart = content;
        }

        // both prefix (if any) a nd localpart must be valid NCName
        if (prefix.length() > 0 && !XMLChar.isValidNCName(prefix))
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "QName"});

        if(!XMLChar.isValidNCName(localpart))
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "QName"});

        // resove prefix to a uri, report an error if failed
        String uri = context.getURI(prefix);
        if (prefix.length() > 0 && uri == null)
            throw new InvalidDatatypeValueException("UndeclaredPrefix", new Object[]{content, prefix});

        return new XQName(prefix, context.getSymbol(localpart), context.getSymbol(content), uri);

    }

    // REVISIT: qname and notation shouldn't support length facets.
    //          now we just return the length of the rawname
    public int getDataLength(Object value) {
        return ((XQName)value).rawname.length();
    }

    /**
     * represent QName data
     */
    private static final class XQName extends QName implements XSQName {
        /** Constructs a QName with the specified values. */
        public XQName(String prefix, String localpart, String rawname, String uri) {
            setValues(prefix, localpart, rawname, uri);
        } // <init>(String,String,String,String)

        /** Returns true if the two objects are equal. */
        public boolean equals(Object object) {
            if (object instanceof QName) {
                QName qname = (QName)object;
                return uri == qname.uri && localpart == qname.localpart;
            }
            return false;
        } // equals(Object):boolean

        public synchronized String toString() {
            return rawname;
        }
        public jaxp.xml.namespace.QName getJAXPQName() {
            return new jaxp.xml.namespace.QName(uri, localpart, prefix);
        }
        public QName getXNIQName() {
            return this;
        }
    }
} // class QNameDVDV
