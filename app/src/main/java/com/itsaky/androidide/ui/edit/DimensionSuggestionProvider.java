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

import com.itsaky.attrinfo.models.Attr;
import com.itsaky.inflater.IAttribute;
import com.itsaky.lsp.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles editing dimension attributes.
 *
 * @author Akash Yadav
 */
public class DimensionSuggestionProvider extends BaseSuggestionProvider {

  /** The dimension units that will be suggested. */
  private final List<String> dimensions = new ArrayList<>();

  public DimensionSuggestionProvider(File layout) {
    super(layout);

    this.dimensions.add("dp");
    this.dimensions.add("sp");
    this.dimensions.add("px");
    this.dimensions.add("pt");
    this.dimensions.add("in");
    this.dimensions.add("mm");
  }

  @Override
  public boolean checkFormat(int format) {
    return (format & Attr.DIMENSION) != 0;
  }

  @Override
  public @NonNull List<String> suggest(IAttribute attribute, @NonNull String prefix) {
    final var list = new ArrayList<String>();
    if (prefix.length() > 0 && Character.isDigit(prefix.charAt(0))) {
      // Fixed dimension values
      // e.g. 10dp

      var val = extractDimensionVal(prefix);
      for (var unit : this.dimensions) {
        final var dimension = val + unit;
        if (StringUtils.matchesFuzzy(dimension, prefix, true)) {
          list.add(dimension);
        }
      }
    }

    return list;
  }

  private int extractDimensionVal(@NonNull String prefix) {
    StringBuilder s = new StringBuilder();
    var i = 0;
    while (i < prefix.length()) {
      if (Character.isDigit(prefix.charAt(i))) {
        s.append(prefix.charAt(i));
      } else {
        break;
      }
      i++;
    }

    return Integer.parseInt(s.toString());
  }
}
