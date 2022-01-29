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

package com.itsaky.inflater;

import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.itsaky.inflater.impl.UiNamespace;

import org.jetbrains.annotations.Contract;

/**
 * Represents a namespace.
 * @author Akash Yadav
 */
public interface INamespace extends Parcelable {
    
    /**
     * Used to represent the namespace declarator (xmlns) (don't know what else to call it).
     */
    INamespace DECLARATOR = new UiNamespace ("xmlns", "<namespace declarator>");
    
    /**
     * The android namespace.
     */
    INamespace ANDROID = new UiNamespace ("android", "http://schemas.android.com/apk/res/android");
    
    /**
     * Get the prefix (name) of this namespace.
     * @return The name of the namespace.
     */
    String getName ();
    
    /**
     * Get the uri of this namespace.
     * @return The uri of this namespace.
     */
    String getUri();
    
    @NonNull
    @Contract(value = "_ -> new", pure = true)
    static INamespace invalid (String name) {
        return new UiNamespace (name, "");
    }
}
