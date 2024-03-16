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
import com.itsaky.androidide.models.IconTextListItem;
import java.util.List;

/**
 * A simple implementation of {@link IconTextAdapter}.
 *
 * @author Akash Yadav
 */
public class SimpleIconTextAdapter extends IconTextAdapter<IconTextListItem> {

  private final List<? extends IconTextListItem> items;

  public SimpleIconTextAdapter(List<? extends IconTextListItem> items) {
    this.items = items;
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  @NonNull
  @Override
  public IconTextListItem getItemAt(int index) {
    return items.get(index);
  }

  @Override
  public int getIconResource(int index) {
    return getItemAt(index).getIconResource();
  }

  @NonNull
  @Override
  public String getItemText(int index) {
    return getItemAt(index).getText();
  }
}
