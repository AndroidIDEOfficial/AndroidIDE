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

package com.itsaky.androidide.editor;

import androidx.annotation.NonNull;

import com.itsaky.androidide.lsp.api.ILanguageServer;
import com.itsaky.androidide.lsp.models.SignatureHelp;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;

import java.io.File;

/**
 * Interface for other modules to access the editor.
 *
 * @author Akash Yadav
 */
public interface IEditor {

  /**
   * Get the file that this editor is currently editing.
   *
   * @return The file instance.
   */
  File getFile();

  /**
   * Whether the content of this editor differs from the content of the file on disk.
   *
   * @return <code>true</code> if the content has been modified, <code>false</code> otherwise.
   */
  boolean isModified();

  /**
   * If any language server is set, requests signature help at the cursor's position. On a valid
   * response, shows the signature help in a popup window.
   */
  void signatureHelp();

  /**
   * Shows the given signature help in the editor.
   *
   * @param help The signature help data to show.
   */
  void showSignatureHelp(SignatureHelp help);

  /**
   * Set the selection of this editor to the given position.
   *
   * @param position The position to select.
   */
  void setSelection(@NonNull Position position);

  /**
   * Set selection to the given range.
   *
   * @param range The range to select.
   */
  void setSelection(@NonNull Range range);

  /**
   * Get the cursor's selection range in the form of {@link Range}.
   *
   * @return The {@link Range} of the cursor.
   */
  Range getCursorLSPRange();

  /**
   * Get the cursor's position in the form of {@link Position}.
   *
   * @return The {@link Position} of the cursor.
   */
  Position getCursorLSPPosition();

  /**
   * Validates the range if it is invalid and returns a valid range.
   *
   * @param range Th range to validate.
   * @return A new, validated range.
   */
  Range validateRange(@NonNull Range range);

  /**
   * Checks if the given range is valid for this editor's text.
   *
   * @param range The range to check.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  boolean isValidRange(Range range);

  /**
   * Checks if the given position is valid for this editor's text.
   *
   * @param position The position to check.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  boolean isValidPosition(Position position);

  /**
   * Checks if the given line is valid for this editor's text.
   *
   * @param line The line to check.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  boolean isValidLine(int line);

  /**
   * Checks if the given column is valid for this editor's text.
   *
   * @param line The line of the column to check.
   * @param column The column to check.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  boolean isValidColumn(int line, int column);

  /**
   * Set the language server that this editor will connect with. If the language client is not set,
   * it'll be set to {@link IDELanguageClientImpl}.
   *
   * @param server The server to set. Provide <code>null</code> to disable all the language server
   *     features.
   */
  void setLanguageServer(ILanguageServer server);

  /**
   * Append the given text at the end of the editor's content.
   *
   * @param text The text to append.
   * @return The line at which the text was appended.
   */
  int append(CharSequence text);

  /** Set the selection of the editor's cursor to the last line of the it's content. */
  void goToEnd();

  /**
   * If any language server is set, asks the language server to find the definition of token at the
   * cursor position.
   *
   * <p>If the server returns a valid response, and the file specified in the response is same the
   * file in this editor, the range specified in the response will be selected.
   */
  void findDefinition();

  /**
   * If any language server instance is set, finds the references to of the token at the current
   * cursor position.
   *
   * <p>If the server returns a valid response, that response is forwarded to the {@link
   * IDELanguageClientImpl}.
   */
  @SuppressWarnings("unused")
  void findReferences();

  /**
   * Requests the language server to provided a semantically larger selection than the current
   * selection. If a valid response is received, that range will be selected.
   */
  void expandSelection();

  /** Ensures that all the windows are dismissed. */
  void ensureWindowsDismissed();
}
