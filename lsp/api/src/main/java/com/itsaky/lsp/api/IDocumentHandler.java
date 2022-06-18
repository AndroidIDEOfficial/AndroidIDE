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

package com.itsaky.lsp.api;

import androidx.annotation.NonNull;

import com.itsaky.lsp.models.DocumentChangeEvent;
import com.itsaky.lsp.models.DocumentCloseEvent;
import com.itsaky.lsp.models.DocumentOpenEvent;
import com.itsaky.lsp.models.DocumentSaveEvent;

import java.nio.file.Path;

/**
 * Handles documents of a language server implementations.
 *
 * @author Akash Yadav
 */
public interface IDocumentHandler {

  /**
   * Check if this document handler can accept the given file or not.
   *
   * @param file The file to check.
   * @return <code>true</code> if the document handler can accept this file, <code>false</code>
   *     otherwise.
   */
  boolean accepts(Path file);

  /**
   * Notification sent to this handler to notify the file open event. This event tells the document
   * handler that the client is now handling the contents of this file.
   *
   * @param event The document open event data.
   */
  void onFileOpened(DocumentOpenEvent event);

  /**
   * Notify the language server that file's content has been changed. Handler implementations should
   * update the contents and last modified timestamp of this file.
   *
   * @param event The document change event data.
   */
  void onContentChange(DocumentChangeEvent event);

  /**
   * Notify the document handler that the given file's contents were saved. Document handlers can
   * optionally run a lint check and provide diagnostics for the file.
   *
   * @param event The document save event data.
   */
  void onFileSaved(DocumentSaveEvent event);

  /**
   * Notify the document handler that the given file was closed and the client is no longer handling
   * this file. Document handlers should remove any caches or data stored about this file and
   * optionally clear the diagnostics of this file.
   *
   * @param event The document close event data.
   */
  void onFileClosed(DocumentCloseEvent event);

  /**
   * Notify the document handler that the user has selected the given file for editing. Usually, the
   * given file is visible to user.
   *
   * @param path The file that was selected.
   */
  void onFileSelected(@NonNull Path path);
}
