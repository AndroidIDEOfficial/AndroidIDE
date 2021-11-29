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
package com.itsaky.androidide.ui.view.adapters;

import android.view.View;
import android.widget.Button;
import com.itsaky.androidide.ui.inflater.IResourceFinder;
import com.itsaky.androidide.ui.view.IAttribute;

public class ButtonAttrAdapter extends TextViewAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof Button;
    }

    @Override
    public boolean apply(IAttribute attribute, View view, IResourceFinder resFinder) {
        if (super.apply(attribute, view, resFinder)) {
            return true;
        }
        
        boolean handled = false;
        
        // Button does not have special attributes 
        // All attributes are inherited from TextView
        
        return handled;
    }
}
