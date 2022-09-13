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
 * Copyright 2004,2005 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.util;

/**
 * <p>A structure that represents an error code, characterized by
 * a domain and a message key.</p>
 *
 * @author Naela Nissar, IBM
 *
 */
final class XMLErrorCode {

    //
    // Data
    //

    /** error domain **/
    private String fDomain;

    /** message key **/
    private String fKey;

    /**
     * <p>Constructs an XMLErrorCode with the given domain and key.</p>
     *
     * @param domain The error domain.
     * @param key The key of the error message.
     */
    public XMLErrorCode(String domain, String key) {
        fDomain = domain;
        fKey = key;
    }

    /**
     * <p>Convenience method to set the values of an XMLErrorCode.</p>
     *
     * @param domain The error domain.
     * @param key The key of the error message.
     */
    public void setValues(String domain, String key) {
        fDomain = domain;
        fKey = key;
    }

    /**
     * <p>Indicates whether some other object is equal to this XMLErrorCode.</p>
     *
     * @param obj the object with which to compare.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof XMLErrorCode))
            return false;
        XMLErrorCode err = (XMLErrorCode) obj;
        return (fDomain.equals(err.fDomain) && fKey.equals(err.fKey));
    }

    /**
     * <p>Returns a hash code value for this XMLErrorCode.</p>
     *
     * @return a hash code value for this XMLErrorCode.
     */
    public int hashCode() {
        return fDomain.hashCode() + fKey.hashCode();
    }
}
