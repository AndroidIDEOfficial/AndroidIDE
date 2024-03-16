/************************************************************************************
 * This file is part of AndroidIDE.
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
package com.itsaky.androidide.models;

import com.itsaky.androidide.lsp.models.DiagnosticItem;
import java.io.File;
import java.util.List;

public class DiagnosticGroup {
  public int icon;
  public String text;
  public File file;
  public List<DiagnosticItem> diagnostics;

  public DiagnosticGroup(int icon, File file, List<DiagnosticItem> diagnostics) {
    this.icon = icon;
    this.file = file;
    this.text = file.getName();
    this.diagnostics = diagnostics;
  }
}
