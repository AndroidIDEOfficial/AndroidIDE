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

package com.itsaky.lsp.util;

import androidx.annotation.NonNull;

import com.itsaky.lsp.api.IDocumentHandler;
import com.itsaky.lsp.models.DocumentChangeEvent;
import com.itsaky.lsp.models.DocumentCloseEvent;
import com.itsaky.lsp.models.DocumentOpenEvent;
import com.itsaky.lsp.models.DocumentSaveEvent;

import java.nio.file.Path;

/**
 * @author Akash Yadav
 */
public class NoDocumentHandler implements IDocumentHandler {

  @Override
  public boolean accepts(Path file) {
    return false;
  }

  @Override
  public void onFileOpened(DocumentOpenEvent event) {}

  @Override
  public void onContentChange(DocumentChangeEvent event) {}

  @Override
  public void onFileSaved(DocumentSaveEvent event) {}

  @Override
  public void onFileClosed(DocumentCloseEvent event) {}

  @Override
  public void onFileSelected(@NonNull Path path) {}
}
