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

package jaxp.sun.org.apache.xerces.internal.util;


import jaxp.xml.stream.Location;

import jaxp.sun.org.apache.xerces.internal.xni.XMLLocator;

/**
 * <p>A light wrapper around a StAX location. This is useful
 * when bridging between StAX and XNI components.</p>
 *
 * @author Michael Glavassevich, IBM
 *
 * @version $Id: StAXLocationWrapper.java,v 1.2 2010-10-26 23:01:13 joehw Exp $
 */
public final class StAXLocationWrapper implements XMLLocator {

    private Location fLocation = null;

    public StAXLocationWrapper() {}

    public void setLocation(Location location) {
        fLocation = location;
    }

    public Location getLocation() {
        return fLocation;
    }

    /*
     * XMLLocator methods
     */

    public String getPublicId() {
        if (fLocation != null) {
            return fLocation.getPublicId();
        }
        return null;
    }

    public String getLiteralSystemId() {
        if (fLocation != null) {
            return fLocation.getSystemId();
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
        if (fLocation != null) {
            return fLocation.getLineNumber();
        }
        return -1;
    }

    public int getColumnNumber() {
        if (fLocation != null) {
            return fLocation.getColumnNumber();
        }
        return -1;
    }

    public int getCharacterOffset() {
        if (fLocation != null) {
            return fLocation.getCharacterOffset();
        }
        return -1;
    }

    public String getEncoding() {
        return null;
    }

    public String getXMLVersion() {
        return null;
    }

} // StAXLocationWrapper
