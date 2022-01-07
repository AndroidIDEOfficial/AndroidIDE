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

import android.content.Context;
import android.graphics.NinePatch;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.itsaky.inflater.IResourceFinder;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Utility methods for creating instances of suitable drawable parsers for a file
 * or an XML code.
 *
 * @author Akash Yadav
 */
public abstract class DrawableParserFactory {
    
    /**
     * Create a new drawable parser for the given file. If the given file is not an XML Document,
     * then a no-op parser is returned which simply returns the drawable for that file. If the file
     * is an XML document, then a suitable parser is determined by looking at the root tag of the
     * drawable.
     *
     * @param context The context. This will be used for obtaining app resources.
     * @param file The file to parse.
     * @param resourceFinder The resource finder. Used for finding references to another resources.
     * @return A new {@link IDrawableParser} instance if the inputs are valid, or {@code null} if there were any
     *          issues creating the parser.
     * @throws XmlPullParserException Thrown by the {@link XmlPullParser#setInput(Reader)}.
     * @throws IOException Thrown by {@link XmlPullParser#next()}
     */
    @Nullable
    public static IDrawableParser newParser (@NonNull final Context context, @NonNull final File file, final IResourceFinder resourceFinder) throws XmlPullParserException, IOException {
        if (file.getName ().endsWith (".xml")) {
            final var code = FileIOUtils.readFile2String (file);
            return DrawableParserFactory.newXmlDrawableParser (code, resourceFinder, context.getResources ().getDisplayMetrics ());
        } else {
            final var bitmap = ImageUtils.getBitmap (file);
            if (bitmap == null) {
                return null;
            }
            
            final var chunk = bitmap.getNinePatchChunk ();
            if (NinePatch.isNinePatchChunk (chunk)) {
                return new NoParser (new NinePatchDrawable (context.getResources (), new NinePatch (bitmap, chunk)));
            }
            
            return new NoParser (new BitmapDrawable (context.getResources (), bitmap));
        }
    }
    
    /**
     * Create a new drawable parser for the given xml code.
     *
     * @param xmlDrawable The XML code. This must be valid.
     * @param resourceFinder The resource finder. Used for finding references to another resources.
     * @param displayMetrics This will be used by generated parsers to parse dimension vlaues.
     * @return A new {@link IDrawableParser} instance if the inputs are valid, or {@code null} if there were any
     *          issues creating the parser.
     * @throws XmlPullParserException Thrown by the {@link XmlPullParser#setInput(Reader)}.
     * @throws IOException Thrown by {@link XmlPullParser#next()}
     */
    @Nullable
    public static IDrawableParser newXmlDrawableParser (String xmlDrawable, IResourceFinder resourceFinder, DisplayMetrics displayMetrics) throws XmlPullParserException, IOException {
        final var factory = XmlPullParserFactory.newInstance ();
        factory.setNamespaceAware (true);
        
        final var parser = factory.newPullParser ();
        parser.setInput (new StringReader (xmlDrawable));
        
        var event = parser.getEventType ();
        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG) {
                final var name = parser.getName ();
                switch (name) {
                    case "shape" :
                        return new ShapeDrawableParser (parser, resourceFinder, displayMetrics);
                    case "vector" :
                        return new NoParser (VectorMasterDrawable.fromXML (xmlDrawable));
                    default:
                        return null;
                }
            }
            event = parser.next ();
        }
        
        // TODO Implement parsers for these root tags if possible
        //    1. <bitmap>
        //    2. <nine-patch>
        //    3. <layer-list>
        //    4. <selector>
        //    5. <level-list>
        //    6. <transition>
        //    7. <inset>
        //    8. <clip>
        //    9. <scale>
        
        // We don't support parsing of this type of drawable
        return null;
    }
    
    /**
     * Instead of parsing anything, this returns the provided drawable.
     */
    private static class NoParser extends IDrawableParser {
        
        private final Drawable parsed;
        
        protected NoParser (final Drawable parsed) {
            super (null, null, null);
            this.parsed = parsed;
        }
    
        @Override
        public Drawable parse () {
            return parsed;
        }
    }
}
