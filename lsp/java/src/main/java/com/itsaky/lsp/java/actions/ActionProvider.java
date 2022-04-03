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

package com.itsaky.lsp.java.actions;

import androidx.annotation.NonNull;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.Range;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Akash Yadav
 */
public interface ActionProvider {

    /**
     * Provide code actions.
     *
     * @param compiler The compiler.
     * @param file The file.
     * @param range The cursor range.
     * @param diagnostics The diagnostics within the cursor range.
     * @return The list of code actions.
     */
    @NonNull
    List<CodeActionItem> provideActions(
            @NonNull CompilerProvider compiler,
            @NonNull Path file,
            @NonNull Range range,
            @NonNull List<DiagnosticItem> diagnostics);
}
