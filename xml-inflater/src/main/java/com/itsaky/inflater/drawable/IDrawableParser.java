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

package com.itsaky.inflater.drawable;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;

import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.util.CommonParseUtils;

import org.xmlpull.v1.XmlPullParser;

/**
 * Base class for drawable parsers.
 *
 * @author Akash Yadav
 */
public abstract class IDrawableParser extends CommonParseUtils {
    
    protected final XmlPullParser parser;
    
    protected IDrawableParser (XmlPullParser parser, IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
        super(resourceFinder, displayMetrics);
        this.parser = parser;
    }
    
    public abstract Drawable parse () throws Exception;
    
    protected int attrIndex (final String name) {
        for (int i = 0; i < this.parser.getAttributeCount (); i++) {
            if (this.parser.getAttributeName (i).equals (name)) {
                return i;
            }
        }
        
        return -1;
    }
    
    protected String value (int index) {
        return parser.getAttributeValue (index);
    }
    
    protected boolean isApi26() {
        return Build.VERSION.SDK_INT >= 26;
    }
    
    protected boolean isApi28() {
        return Build.VERSION.SDK_INT >= 28;
    }
    
    protected boolean isApi29() {
        return Build.VERSION.SDK_INT >= 29;
    }
    
    protected boolean isApi30() {
        return Build.VERSION.SDK_INT >= 30;
    }
}
