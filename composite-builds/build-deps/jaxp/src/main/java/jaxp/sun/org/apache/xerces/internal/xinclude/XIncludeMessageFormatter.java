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
 * Copyright 2003-2005 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.xinclude;

import jaxp.sun.org.apache.xerces.internal.util.MessageFormatter;
import jaxp.sun.org.apache.xerces.internal.utils.SecuritySupport;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

// TODO: fix error messages in XIncludeMessages.properties
/**
 * XIncludeMessageFormatter provides error messages for the XInclude 1.0 Candidate Recommendation
 *
 * @author Peter McCracken, IBM
 *
 * @version $Id: XIncludeMessageFormatter.java,v 1.7 2010-11-01 04:40:18 joehw Exp $
 */
public class XIncludeMessageFormatter implements MessageFormatter {

    public static final String XINCLUDE_DOMAIN = "http://www.w3.org/TR/xinclude";

     // private objects to cache the locale and resource bundle
    private Locale fLocale = null;
    private ResourceBundle fResourceBundle = null;

    /**
     * Formats a message with the specified arguments using the given
     * locale information.
     *
     * @param locale    The locale of the message.
     * @param key       The message key.
     * @param arguments The message replacement text arguments. The order
     *                  of the arguments must match that of the placeholders
     *                  in the actual message.
     *
     * @return Returns the formatted message.
     *
     * @throws MissingResourceException Thrown if the message with the
     *                                  specified key cannot be found.
     */
     public String formatMessage(Locale locale, String key, Object[] arguments)
        throws MissingResourceException {

        if (fResourceBundle == null || locale != fLocale) {
            if (locale != null) {
                fResourceBundle = jaxp.sun.org.apache.xerces.internal.utils.SecuritySupport.getResourceBundle("jaxp.sun.org.apache.xerces.internal.impl.msg.XIncludeMessages", locale);
                // memorize the most-recent locale
                fLocale = locale;
            }
            if (fResourceBundle == null)
                fResourceBundle = SecuritySupport.getResourceBundle("jaxp.sun.org.apache.xerces.internal.impl.msg.XIncludeMessages");
        }

        String msg = fResourceBundle.getString(key);
        if (arguments != null) {
            try {
                msg = java.text.MessageFormat.format(msg, arguments);
            } catch (Exception e) {
                msg = fResourceBundle.getString("FormatFailed");
                msg += " " + fResourceBundle.getString(key);
            }
        }

        if (msg == null) {
            msg = fResourceBundle.getString("BadMessageKey");
            throw new MissingResourceException(msg, "jaxp.sun.org.apache.xerces.internal.impl.msg.XIncludeMessages", key);
        }

        return msg;
    }
}
