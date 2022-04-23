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

package com.itsaky.lsp.xml.utils;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.CharSequenceReader;
import com.itsaky.androidide.utils.ILogger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * @author Akash Yadav
 */
public class XmlUtils {

    private static final XmlPullParserFactory parserFactory;
    private static final ILogger LOG = ILogger.newInstance("XmlUtils");

    static {
        try {
            parserFactory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            LOG.error("Unable to create pull parser factory");
            throw new RuntimeException(e);
        }
    }

    public static XmlPullParser newParser() throws XmlPullParserException {
        return parserFactory.newPullParser();
    }

    @NonNull
    public static XmlPullParser newParser(CharSequence contents) throws XmlPullParserException {
        final var parser = parserFactory.newPullParser();
        parser.setInput(new CharSequenceReader(contents));
        return parser;
    }
}
