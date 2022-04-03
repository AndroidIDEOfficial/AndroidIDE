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

package com.itsaky.lsp.java.providers;

import androidx.annotation.NonNull;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.java.actions.ActionProvider;
import com.itsaky.lsp.java.actions.CursorCodeActionProvider;
import com.itsaky.lsp.java.actions.DiagnosticsCodeActionProvider;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.models.CodeActionParams;
import com.itsaky.lsp.models.CodeActionResult;

public class CodeActionProvider {

    private static final Logger LOG = Logger.newInstance("JavaCodeActionProvider");
    private final CompilerProvider compiler;

    public CodeActionProvider(CompilerProvider compiler) {
        this.compiler = compiler;
    }

    @NonNull
    public CodeActionResult codeActions(@NonNull CodeActionParams params) {
        final ActionProvider actionProvider;
        if (params.getDiagnostics().isEmpty()) {
            actionProvider = new CursorCodeActionProvider();
        } else {
            actionProvider = new DiagnosticsCodeActionProvider();
        }

        return new CodeActionResult(
                actionProvider.provideActions(
                        compiler, params.getFile(), params.getRange(), params.getDiagnostics()));
    }
}
