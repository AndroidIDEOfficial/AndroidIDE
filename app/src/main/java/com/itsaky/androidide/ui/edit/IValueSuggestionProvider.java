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

import androidx.annotation.NonNull;

import com.itsaky.inflater.IAttribute;

import java.util.List;

/**
 * An attribute value provider provides suggestions while editing an attribute value.
 *
 * @author Akash Yadav
 */
public interface IValueSuggestionProvider {

  /**
   * Check if the given format is supported by this provider.
   *
   * @param format The formats to check.
   * @return <code>true</code> if the given formats contain a supported format, <code>false</code>
   *     otherwise.
   */
  boolean checkFormat(int format);

  /**
   * Provide suggestions for the given attribute.
   *
   * @param attribute The attribute to provide suggestions for.
   * @param prefix The prefix (partial identifier) to filter the suggestions.
   * @return The suggestion items. Must not be <code>null</code>.
   */
  @NonNull
  List<String> suggest(IAttribute attribute, String prefix);
}
