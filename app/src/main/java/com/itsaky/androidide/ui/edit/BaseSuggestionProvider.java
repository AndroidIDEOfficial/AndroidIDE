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

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.values.ValuesTable;
import com.itsaky.inflater.values.ValuesTableFactory;

import java.io.File;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Base class for attribute edit handlers.
 *
 * @author Akash Yadav
 */
public abstract class BaseSuggestionProvider implements IValueSuggestionProvider {

  protected final File resDir;
  protected final File layout;

  protected BaseSuggestionProvider(File layout) {
    Objects.requireNonNull(layout);

    final var path = layout.getAbsolutePath();
    if (!Pattern.compile(".*/src/.*/res/layout/(\\w|_)+\\.xml").matcher(path).matches()) {
      throw new IllegalArgumentException("Given layout file is not valid");
    }

    this.layout = layout;
    this.resDir = Objects.requireNonNull(layout.getParentFile()).getParentFile();
    Objects.requireNonNull(this.resDir);
  }

  protected ValuesTable getValuesTable() {
    return ValuesTableFactory.getTable(this.resDir);
  }

  protected IResourceTable getResourceTable() {
    return StudioApp.getInstance().getResourceTable();
  }
}
