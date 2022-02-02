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

import com.itsaky.lsp.models.DiagnosticItem;

import java.nio.file.Path;
import java.util.List;

/**
 * A code analyzer analyzes the code and provides
 * diagnostics and semantic highlighting.
 *
 * @author Akash Yadav
 */
public interface IDiagnosticProvider {
    
    /**
     * Analyze the file given in the params.
     * @param params The params for code analysis.
     * @return The result of the analysis containing diagnostics and semantic highlighting.
     */
    @NonNull
    List<DiagnosticItem> analyze (Path file);
}
