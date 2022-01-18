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

import com.itsaky.lsp.api.ICodeAnalyzer;
import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.compiler.SynchronizedTask;
import com.itsaky.lsp.models.AnalyzeParams;
import com.itsaky.lsp.models.AnalyzeResult;

import java.nio.file.Path;

/**
 * Code analyzer for java source code.
 *
 * @author Akash Yadav
 */
public class JavaCodeAnalyzer implements ICodeAnalyzer {
    
    private final CompilerProvider compiler;
    
    public JavaCodeAnalyzer (CompilerProvider compiler) {
        this.compiler = compiler;
    }
    
    @NonNull
    @Override
    public AnalyzeResult analyze (@NonNull AnalyzeParams params) {
        final Path file = params.getFile ();
        try (final SynchronizedTask synchronizedTask = compiler.compile (file)) {
            return synchronizedTask.getWithTask (task -> {
                final AnalyzeResult result = new AnalyzeResult ();
                if (!isTaskValid (task)) {
                    return result;
                }
                
                result.setDiagnostics (DiagnosticsProvider.findDiagnostics (task, file));
                return result;
            });
        }
    }
    
    private static boolean isTaskValid (CompileTask task) {
        return task != null
                && task.task != null
                && task.roots != null
                && task.roots.size () > 0;
    }
}
