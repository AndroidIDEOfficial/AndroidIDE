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

package com.itsaky.androidide.adapters;

import androidx.annotation.NonNull;

import com.itsaky.androidide.R;
import com.itsaky.attrinfo.models.Attr;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link com.itsaky.androidide.adapters.IconTextAdapter} for {@link com.itsaky.attrinfo.models.Attr}.
 *
 * @author Akash Yadav
 */
public class AttrListAdapter extends IconTextAdapter<Attr> {
    
    private final List<Attr> attrs;
    
    public AttrListAdapter (List<Attr> attrs) {
        if (attrs == null) {
            attrs = new ArrayList<> ();
        }
        
        this.attrs = attrs;
    }
    
    @NonNull
    @Override
    public Attr getItemAt (int index) {
        return attrs.get (index);
    }
    
    @Override
    public int getIconResource (int index) {
        return R.drawable.ic_xml_attribute;
    }
    
    @NonNull
    @Override
    public String getItemText (int index) {
        return getItemAt (index).name;
    }
    
    @Override
    public int getItemCount () {
        return attrs.size ();
    }
}
