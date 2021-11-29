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
package com.itsaky.androidide.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Set;

/**
 * Attribute that can be applied to a {@link IView}
 *
 * @author Akash Yadav
 */
public interface IAttribute {
    
    /**
     * The android namespace
     */
    public static final String NS_ANDROID = "android";
    
    @NonNull
    String getNamespace ();
    
    @NonNull
    String getAttributeName ();
    
    @Nullable
    String getValue ();
    
    @NonNull
    Set<IFixedValue> getFixedValues ();
}
