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

package com.itsaky.androidide.editor.api;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;

import io.github.rosemoe.sora.text.CharPosition;
import java.io.File;

/**
 * Interface for other modules to access the editor.
 *
 * @author Akash Yadav
 */
public interface IEditor {

  String KEY_FILE = "ide.editor.file";

  /**
   * Get the file that this editor is currently editing.
   *
   * @return The file instance.
   */
  @Nullable
  File getFile();

  /**
   * Whether the content of this editor differs from the content of the file on disk.
   *
   * @return <code>true</code> if the content has been modified, <code>false</code> otherwise.
   */
  boolean isModified();

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
  default void setSelection(@NonNull Range range) {
    setSelection(range.getStart(), range.getEnd());
  }

  void setSelection(@NonNull Position start, @NonNull Position end);

  /**
   * Set selection around the given position.
   * @param position The position to set selection around.
   */
  default void setSelectionAround(CharPosition position) {
    setSelectionAround(position.getLine(), position.getColumn());
  }

  /**
   * Set selection around the given position.
   * @param position The position to set selection around.
   */
  default void setSelectionAround(Position position) {
    setSelectionAround(position.getLine(), position.getColumn());
  }

  /**
   * Set selection around the given position.
   * @param line The line index.
   * @param column The column index.
   */
  void setSelectionAround(int line, int column);
  
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
  void validateRange(@NonNull Range range);
  
  /**
   * Checks if the given range is valid for this editor's text.
   *
   * @param range The range to check.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  default boolean isValidRange(Range range) {
    return isValidRange(range, false);
  }
  
  /**
   * Checks if the given range is valid for this editor's text.
   *
   * @param range The range to check.
   * @param allowColumnEqual Whether to allow <code>column</code> to be equal to column count of *
   *     <code>line</code>.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  boolean isValidRange(Range range, boolean allowColumnEqual);
  
  /**
   * Checks if the given position is valid for this editor's text.
   *
   * @param position The position to check.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  default boolean isValidPosition(Position position) {
    return isValidPosition(position, false);
  }
  
  /**
   * Checks if the given position is valid for this editor's text.
   *
   * @param position The position to check.
   * @param allowColumnEqual Whether to allow <code>column</code> to be equal to column count of *
   *     <code>line</code>.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  boolean isValidPosition(Position position, boolean allowColumnEqual);
  
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
   * @see #isValidColumn(int, int, boolean)
   */
  default boolean isValidColumn(int line, int column) {
    return isValidColumn(line, column, false);
  }
  
  /**
   * Checks if the given column is valid for this editor's text.
   *
   * @param line The line of the column to check.
   * @param column The column to check.
   * @param allowColumnEqual Whether to allow <code>column</code> to be equal to column count of
   *     <code>line</code>.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  boolean isValidColumn(int line, int column, boolean allowColumnEqual);
  
  /**
   * Append the given text at the end of the editor's content.
   *
   * @param text The text to append.
   * @return The line at which the text was appended.
   */
  int append(CharSequence text);
  
  /**
   * Replaces the editor's existing content with the given content.
   *
   * @param newContent The new content to set to the editor.
   */
  void replaceContent(CharSequence newContent);
  
  /** Set the selection of the editor's cursor to the last line of the it's content. */
  void goToEnd();
}