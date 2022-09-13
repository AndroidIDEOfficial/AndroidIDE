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


import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Locale;

import jaxp.sun.org.apache.xerces.internal.util.EncodingMap;


/**
 * Provides information about encodings. Depends on the Java runtime
 * to provides writers for the different encodings, but can be used
 * to override encoding names and provide the last printable character
 * for each encoding.
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 */
public class Encodings
{


    /**
     * The last printable character for unknown encodings.
     */
    static final int DEFAULT_LAST_PRINTABLE = 0x7F;

    // last printable character for Unicode-compatible encodings
    static final int LAST_PRINTABLE_UNICODE = 0xffff;
    // unicode-compliant encodings; can express plane 0
    static final String[] UNICODE_ENCODINGS = {
        "Unicode", "UnicodeBig", "UnicodeLittle", "GB2312", "UTF8", "UTF-16",
    };
    // default (Java) encoding if none supplied:
    static final String DEFAULT_ENCODING = "UTF8";

    // note that the size of this Hashtable
    // is bounded by the number of encodings recognized by EncodingMap;
    // therefore it poses no static mutability risk.
    static Hashtable _encodings = new Hashtable();

    /**
     * @param encoding a MIME charset name, or null.
     */
    static EncodingInfo getEncodingInfo(String encoding, boolean allowJavaNames) throws UnsupportedEncodingException {
        EncodingInfo eInfo = null;
        if (encoding == null) {
            if((eInfo = (EncodingInfo)_encodings.get(DEFAULT_ENCODING)) != null)
                return eInfo;
            eInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping(DEFAULT_ENCODING), DEFAULT_ENCODING, LAST_PRINTABLE_UNICODE);
            _encodings.put(DEFAULT_ENCODING, eInfo);
            return eInfo;
        }
        // need to convert it to upper case:
        encoding = encoding.toUpperCase(Locale.ENGLISH);
        String jName = EncodingMap.getIANA2JavaMapping(encoding);
        if(jName == null) {
            // see if the encoding passed in is a Java encoding name.
            if(allowJavaNames ) {
                EncodingInfo.testJavaEncodingName(encoding);
                if((eInfo = (EncodingInfo)_encodings.get(encoding)) != null)
                    return eInfo;
                // is it known to be unicode-compliant?
                int i=0;
                for(; i<UNICODE_ENCODINGS.length; i++) {
                    if(UNICODE_ENCODINGS[i].equalsIgnoreCase(encoding)) {
                        eInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping(encoding), encoding, LAST_PRINTABLE_UNICODE);
                        break;
                    }
                }
                if(i == UNICODE_ENCODINGS.length) {
                    eInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping(encoding), encoding, DEFAULT_LAST_PRINTABLE);
                }
                _encodings.put(encoding, eInfo);
                return eInfo;
            } else {
                throw new UnsupportedEncodingException(encoding);
            }
        }
        if ((eInfo = (EncodingInfo)_encodings.get(jName)) != null)
            return eInfo;
        // have to create one...
        // is it known to be unicode-compliant?
        int i=0;
        for(; i<UNICODE_ENCODINGS.length; i++) {
            if(UNICODE_ENCODINGS[i].equalsIgnoreCase(jName)) {
                eInfo = new EncodingInfo(encoding, jName, LAST_PRINTABLE_UNICODE);
                break;
            }
        }
        if(i == UNICODE_ENCODINGS.length) {
            eInfo = new EncodingInfo(encoding, jName, DEFAULT_LAST_PRINTABLE);
        }
        _encodings.put(jName, eInfo);
        return eInfo;
    }

    static final String JIS_DANGER_CHARS
    = "\\\u007e\u007f\u00a2\u00a3\u00a5\u00ac"
    +"\u2014\u2015\u2016\u2026\u203e\u203e\u2225\u222f\u301c"
    +"\uff3c\uff5e\uffe0\uffe1\uffe2\uffe3";

}
