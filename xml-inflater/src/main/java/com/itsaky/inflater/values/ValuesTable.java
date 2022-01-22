/*
 * This file is part of AndroidIDE.
 * 
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 * 
 */
package com.itsaky.inflater.values;

import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.inflater.values.models.BooleanValue;
import com.itsaky.inflater.values.models.ColorValue;
import com.itsaky.inflater.values.models.DimensionValue;
import com.itsaky.inflater.values.models.IntegerValue;
import com.itsaky.inflater.values.models.StringValue;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Reads and manages values (colors, strings, integers, etc.) for a single module.
 *
 * @author Akash Yadav
 */
public class ValuesTable {
    
    // TODO Parse arrays, styles and styleables
    private final Map<String, IResourceValue> strings;
    private final Map<String, IResourceValue> colors;
    private final Map<String, IResourceValue> integers;
    private final Map<String, IResourceValue> dimens;
    private final Map<String, IResourceValue> booleans;
    
    private ValuesTable () {
        this.strings = new HashMap<> ();
        this.colors = new HashMap<> ();
        this.integers = new HashMap<> ();
        this.dimens = new HashMap<> ();
        this.booleans = new HashMap<> ();
    }
    
    @NonNull
    @Override
    public String toString () {
        return "ValuesTable{" +
                "strings=" + strings +
                ", colors=" + colors +
                ", integers=" + integers +
                ", dimens=" + dimens +
                ", booleans=" + booleans +
                '}';
    }
    
    /**
     * Create a new table by reading the files from the given values directory.
     * @param valuesDirectory The "res/values" directory.
     * @return The values table or {@code null} if the directory is invalid or there are no files in this directory.
     * @throws XmlPullParserException Thrown by {@link XmlPullParserFactory}.
     * @throws IOException Thrown by {@link XmlPullParser}.
     */
    public static ValuesTable forDirectory (File valuesDirectory) throws Exception {
        if (valuesDirectory == null) {
            LOG.error ("Cannot create values table for directory: ", null);
            return null;
        }
        
        if (!valuesDirectory.exists ()) {
            LOG.error ("Cannot create values table for directory as it does not exists. Directory:", valuesDirectory);
            return null;
        }
    
        final var files = valuesDirectory.listFiles (ValuesTable::isValid);
        if (files == null) {
            LOG.error ("No files found in the given values directory");
            return null;
        }
        
        final var start = System.currentTimeMillis ();
        final var values = new ValuesTable ();
        
        for (var file : files) {
            final var factory = XmlPullParserFactory.newInstance ();
            final var parser = factory.newPullParser ();
            parser.setInput (new FileInputStream (file), null);
            
            var event = parser.getEventType ();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    final var tag = parser.getName ();
                    if ("resources".equals (tag)) {
                        event = parser.next ();
                        continue;
                    }
                    
                    readTag (tag, parser, values);
                }
                event = parser.next ();
            }
        }
        
        final var duration = System.currentTimeMillis () - start;
        LOG.info (String.format (Locale.getDefault (), "Took %d ms to read %s files in directory: %s", duration, files.length, valuesDirectory.toString ()));
        
        return values;
    }
    
    private static void readTag (String tag, XmlPullParser parser, ValuesTable values) throws Exception {
        
        if ("style".equals (tag)
        || "declare-styleable".equals (tag)) {
            // These tags are not supported yet
            // Skip parsing until we reach next tag with same depth as of this tag
            skip (parser);
            return;
        }
        
        // TODO Should we check if these tags are specified within <resources>?
        final var name = getName (parser);
        if (name == null ) {
            LOG.error (String.format ("A <%s> resource was found with no 'name' attribute", tag));
            return;
        }
        
        if ("item".equals (tag)) {
            tag = parser.getAttributeValue (null, "type");
            if (tag == null) {
                LOG.error ("<item> resource value found but no 'type' was specified.");
                return;
            }
        }
        
        // Value must be read after all other required values have been read
        final var value = parser.nextText ();
        IResourceValue resourceValue;
        
        switch (tag) {
            case "string" :
                resourceValue = new StringValue (name, value);
                values.strings.put (name, resourceValue);
                break;
            case "color" :
                resourceValue = new ColorValue (name, value);
                values.colors.put (name, resourceValue);
                break;
            case "bool" :
                resourceValue = new BooleanValue (name, value);
                values.booleans.put (name, resourceValue);
                break;
            case "dimen":
                resourceValue = new DimensionValue (name, value);
                values.dimens.put (name, resourceValue);
                break;
            case "integer" :
                resourceValue = new IntegerValue (name, value);
                values.integers.put (name, resourceValue);
                break;
            default:
                LOG.warn ("Unknown or unsupported resource value tag:", tag);
                break;
        }
    }
    
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    
    private static String getName (XmlPullParser parser) {
        return parser.getAttributeValue (null, "name");
    }
    
    private static boolean isValid (File file) {
        return file != null && file.isFile () && FileUtils.isUtf8 (file);
    }
    
    private static final Logger LOG = Logger.instance ("ValuesTable");
}
