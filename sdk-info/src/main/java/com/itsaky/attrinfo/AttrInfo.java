/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
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
 **************************************************************************************/
package com.itsaky.attrinfo;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.utils.Logger;
import com.itsaky.attrinfo.models.Attr;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AttrInfo {
    
    private final Map<String, Attr> attrs = new HashMap<> ();
    
    private static final Logger LOG = Logger.instance ("AttrInfo");
    
    public AttrInfo (@NonNull Context ctx) throws Exception {
        readAttributes (ctx.getResources ());
    }
    
    @NonNull
    public Map<String, Attr> getAttrs () {
        return this.attrs;
    }
    
    @Nullable
    public Attr getAttribute (String name) {
        return this.attrs.get (name);
    }
    
    private void readAttributes (@NonNull Resources resources) throws Exception {
        final InputStream in = resources.openRawResource (com.itsaky.sdkinfo.R.raw.attrs);
        
        if (in == null) {
            throw new IllegalStateException ("attrs.xml: Resource not found.");
        }
        
        Document doc = Jsoup.parse (in, null, "file://android_res/");
        Elements attributes = doc.getElementsByTag ("resources").first ().getElementsByTag ("attr");
        for (Element attribute : attributes) {
            final var attr = new Attr (attribute.attr ("name"), true);
            
            if (attr.name.contains (":")) {
                String[] split = attr.name.split (":");
                attr.namespace = split[0];
                attr.name = split[1];
            }
            
            // If there is an implicit declaration od format attribute,
            // then we parse its values and set the format bits accordingly
            if (attribute.hasAttr ("format")) {
                final var format = attribute.attr ("format");
                attr.format = Attr.formatForName (format);
                
                if (format.contains ("boolean")) {
                    attr.possibleValues.add ("true");
                    attr.possibleValues.add ("false");
                }
            }
            
            // If there is no implicit declaration of format or
            // this attribute has <enum> or <flag> children tags,
            // then format will be set here along with the possible values
            Elements enums = attribute.getElementsByTag ("enum");
            Elements flags = attribute.getElementsByTag ("flag");
            if (enums.size () > 0) {
                for (Element e : enums) {
                    attr.possibleValues.add (e.attr ("name"));
                }
                
                attr.format |= Attr.ENUM;
            }
            
            if (flags.size () > 0) {
                for (Element e : flags) {
                    attr.possibleValues.add (e.attr ("name"));
                }
                
                attr.format |= Attr.FLAG;
            }
            
            if (this.attrs.containsKey (attr.name)) {
                final var present = this.attrs.get (attr.name);
                if (present != null) {
                    if (present.hasPossibleValues ()) {
                        attr.possibleValues.addAll (present.possibleValues);
                    }
                    
                    if (attr.format == 0 && present.format != 0) {
                        attr.format = present.format;
                    }
                }
            }
            
            this.attrs.put (attr.name, attr);
        }
    }
}
