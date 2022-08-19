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

package jaxp.sun.org.apache.xerces.internal.util;

import org.xml.sax.Locator;
import org.xml.sax.ext.Locator2;

import jaxp.sun.org.apache.xerces.internal.xni.XMLLocator;

/**
 * <p>A light wrapper around a SAX locator. This is useful
 * when bridging between SAX and XNI components.</p>
 *
 * @author Michael Glavassevich, IBM
 *
 */
public final class SAXLocatorWrapper implements XMLLocator {

    private Locator fLocator = null;
    private Locator2 fLocator2 = null;

    public SAXLocatorWrapper() {}

    public void setLocator(Locator locator) {
        fLocator = locator;
        if (locator instanceof Locator2 || locator == null) {
            fLocator2 = (Locator2) locator;
        }
    }

    public Locator getLocator() {
        return fLocator;
    }

    /*
     * XMLLocator methods
     */

    public String getPublicId() {
        if (fLocator != null) {
            return fLocator.getPublicId();
        }
        return null;
    }

    public String getLiteralSystemId() {
        if (fLocator != null) {
            return fLocator.getSystemId();
        }
        return null;
    }

    public String getBaseSystemId() {
        return null;
    }

    public String getExpandedSystemId() {
        return getLiteralSystemId();
    }

    public int getLineNumber() {
        if (fLocator != null) {
            return fLocator.getLineNumber();
        }
        return -1;
    }

    public int getColumnNumber() {
        if (fLocator != null) {
            return fLocator.getColumnNumber();
        }
        return -1;
    }

    public int getCharacterOffset() {
        return -1;
    }

    public String getEncoding() {
        if (fLocator2 != null) {
            return fLocator2.getEncoding();
        }
        return null;
    }

    public String getXMLVersion() {
        if (fLocator2 != null) {
            return fLocator2.getXMLVersion();
        }
        return null;
    }

} // SAXLocatorWrapper
