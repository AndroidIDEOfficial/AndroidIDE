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
 * Copyright 2000-2002,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.jaxp;

import jaxp.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import java.util.Locale;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 */

class DefaultValidationErrorHandler extends DefaultHandler {
    static private int ERROR_COUNT_LIMIT = 10;
    private int errorCount = 0;
    private Locale locale = Locale.getDefault();

    public DefaultValidationErrorHandler(Locale locale) {
        this.locale = locale;
    }

    // XXX Fix message i18n
    public void error(SAXParseException e) throws SAXException {
        if (errorCount >= ERROR_COUNT_LIMIT) {
            // Ignore all errors after reaching the limit
            return;
        } else if (errorCount == 0) {
            // Print a warning before the first error
            System.err.println(SAXMessageFormatter.formatMessage(locale,
                        "errorHandlerNotSet", new Object [] {errorCount}));
        }

        String systemId = e.getSystemId();
        if (systemId == null) {
            systemId = "null";
        }
        String message = "Error: URI=" + systemId +
            " Line=" + e.getLineNumber() +
            ": " + e.getMessage();
        System.err.println(message);
        errorCount++;
    }
}
