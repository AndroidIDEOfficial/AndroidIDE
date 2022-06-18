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
package com.itsaky.inflater;

import android.view.View;

/** An adapter that handles attributes of a specific type of view */
public interface IAttributeAdapter {

  /**
   * Set the resource finder used by this class.
   *
   * @param resourceFinder The resource finder.
   */
  void setResourceFinder(IResourceTable resourceFinder);

  /**
   * Can this adapter handle attributes of the provided view?
   *
   * @param view The view to which the attributes will be applied
   * @return {@code true} if this adapter can handle attributes of the specified view
   */
  boolean isApplicableTo(View view);

  /**
   * Apply the provided attribute to the view.
   *
   * <p>This will be called if and only if {@link #isApplicableTo(View)} returns {@code true}.
   *
   * @param attribute The attribute to apply
   * @param view The view to apply attribute to
   * @return {@code true} if the attribute was applied successfully {@code false} otherwise.
   */
  boolean apply(IAttribute attribute, View view);
}
