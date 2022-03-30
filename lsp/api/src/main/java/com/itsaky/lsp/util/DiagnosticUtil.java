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

import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.Position;
import java.util.List;

/**
 * Utility class for working with diagnostic items.
 *
 * @author Akash Yadav
 */
public class DiagnosticUtil {

    public static DiagnosticItem binarySearchDiagnostic(
            List<DiagnosticItem> diagnostics, Position position) {
        return binarySearchDiagnostic(diagnostics, position.getLine(), position.getColumn());
    }

    public static DiagnosticItem binarySearchDiagnostic(
            List<DiagnosticItem> diagnostics, int line, int column) {
        if (diagnostics.isEmpty()) {
            return null;
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
                return d;
            }
        }

        return null;
    }
}
