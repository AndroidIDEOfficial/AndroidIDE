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

package com.itsaky.androidide.lsp.util;

import androidx.annotation.NonNull;
import com.itsaky.androidide.lsp.models.DiagnosticItem;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for working with diagnostic items.
 *
 * @author Akash Yadav
 */
public class DiagnosticUtil {

  /**
   * Binary search all the diagnostic items in the given range.
   *
   * @param diagnostics The diagnostics to search items from.
   * @param range The range to look for diagnostics in.
   * @return The list of found diagnostics.
   */
  public static List<DiagnosticItem> findDiagnosticsInRange(
      List<DiagnosticItem> diagnostics, Range range) {
    if (diagnostics == null || range == null || diagnostics.isEmpty()) {
      return Collections.emptyList();
    }

    final var left = findLeftBound(diagnostics, range.getStart());
    final var right = findRightBound(diagnostics, range.getEnd());

    if (left < 0 || right >= diagnostics.size()) {
      return Collections.emptyList();
    }

    return diagnostics.subList(left, right);
  }

  /**
   * Find the leftmost diagnostic element containing the given position.
   *
   * @param diagnostics The list of diagnostics.
   * @param position The position to search for.
   * @return The index of the diagnostic item.
   */
  private static int findLeftBound(List<DiagnosticItem> diagnostics, @NonNull Position position) {
    final var index =
        binarySearchDiagnosticPosition(diagnostics, position.getLine(), position.getColumn());
    if (index == -1) {
      return -1;
    }

    if (index == 0) {
      return index;
    }

    var left = index - 1;
    var i = left;

    do {
      left = i;
      i = binarySearchDiagnosticPositionInRange(diagnostics, position, 0, left);
    } while (i != -1);

    return left;
  }

  /**
   * Binary search the position of the diagnostic item within the given list bounds containing the
   * given position.
   *
   * @param diagnostics The list of diagnostics.
   * @param position The position that will be used to check against the diagnostic range.
   * @param start The start index of the item in the diagnostics list.
   * @param end The end index of the item in the diagnostic list.
   * @return The index of the found diagnostic.
   */
  private static int binarySearchDiagnosticPositionInRange(
      List<DiagnosticItem> diagnostics, Position position, int start, int end) {
    var left = start;
    var right = end;
    while (left <= right) {
      final var mid = (left + right) / 2;
      final var d = diagnostics.get(mid);
      final var r = d.getRange();
      final var c = r.containsForBinarySearch(position);
      if (c < 0) {
        left = mid - 1;
      } else if (c > 1) {
        right = mid + 1;
      } else {
        return mid;
      }
    }

    return -1;
  }

  /**
   * Binary search the diagnostic item which contains the given line and column.
   *
   * @param diagnostics The list of diagnostics.
   * @param line The line to search for.
   * @param column The column to search for.
   * @return The index of the found diagnostic item.
   */
  public static int binarySearchDiagnosticPosition(
      List<DiagnosticItem> diagnostics, int line, int column) {
    if (diagnostics.isEmpty()) {
      return -1;
    }

    final var pos = new Position(line, column);
    int left = 0;
    int right = diagnostics.size() - 1;
    int mid;
    while (left <= right) {
      mid = (left + right) / 2;
      var d = diagnostics.get(mid);
      var r = d.getRange();
      var c = r.containsForBinarySearch(pos);
      if (c < 0) {
        right = mid - 1;
      } else if (c > 0) {
        left = mid + 1;
      } else {
        return mid;
      }
    }

    return -1;
  }

  /**
   * Find the rightmost diagnostic element containing the given position.
   *
   * @param diagnostics The list of diagnostics.
   * @param position The position to search for.
   * @return The index of the diagnostic item.
   */
  private static int findRightBound(List<DiagnosticItem> diagnostics, @NonNull Position position) {
    final var index =
        binarySearchDiagnosticPosition(diagnostics, position.getLine(), position.getColumn());
    if (index == -1) {
      return -1;
    }

    if (index == diagnostics.size() - 1) {
      return index;
    }

    var right = index + 1;
    var i = right;

    do {
      right = i;
      i =
          binarySearchDiagnosticPositionInRange(
              diagnostics, position, right, diagnostics.size() - 1);
    } while (i != -1);

    return right;
  }

  /**
   * Binary search the diagnostic item which contains the given line and column.
   *
   * @param diagnostics The list of diagnostics.
   * @param position The position to search for.
   * @return The diagnostic item.
   */
  public static DiagnosticItem binarySearchDiagnostic(
      List<DiagnosticItem> diagnostics, Position position) {
    return binarySearchDiagnostic(diagnostics, position.getLine(), position.getColumn());
  }

  /**
   * Binary search the diagnostic item which contains the given line and column.
   *
   * @param diagnostics The list of diagnostics.
   * @param line The line to search for.
   * @param column The column to search for.
   * @return The diagnostic item.
   */
  public static DiagnosticItem binarySearchDiagnostic(
      List<DiagnosticItem> diagnostics, int line, int column) {
    if (diagnostics == null) {
      return null;
    }
    final var index = binarySearchDiagnosticPosition(diagnostics, line, column);
    if (index == -1) {
      return null;
    }

    return diagnostics.get(index);
  }
}
