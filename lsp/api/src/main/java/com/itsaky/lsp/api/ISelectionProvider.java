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

import com.itsaky.lsp.models.ExpandSelectionParams;
import com.itsaky.lsp.models.Range;

/**
 * A selection provider provides features such as
 * expanding the current selection.
 *
 * @author Akash Yadav
 */
public interface ISelectionProvider {
    
    /**
     * Request sent by client for expanding the current selection.
     * <p>
     *     Example: If a statement in a method body is selected,
     *     the provider returns the range of the method body as the expanded selection.
     * </p>
     *
     * @param params The params for expanding the selection.
     * @return The expanded selection range. Or same range as params if cannot be expanded.
     */
    Range expandSelection (@NonNull ExpandSelectionParams params);
}
