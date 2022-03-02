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

package com.itsaky.androidide.ui.edit;

import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IView;

/**
 * An attribute edit handler handles add/update
 * of an attribute for a view.
 *
 * @author Akash Yadav
 */
public interface IAttrEditHandler {
    
    /**
     * Check if the given format is supported by this handler.
     *
     * @param format The formats to check.
     * @return <code>true</code> if the given formats contain a supported format, <code>false</code> otherwise.
     */
    boolean checkFormat (int format);
    
    /**
     * Handle the attribute addition/update.
     * The handler is responsible for making sure that
     * the attribute is reflected on the view. Also,
     * for attribute addition, the handler should check
     * for duplicate entries.
     *
     * @param view      The view to which the attribute must be added/updated.
     * @param attribute The attribute to add/update.
     */
    void handle (IView view, IAttribute attribute);
}
