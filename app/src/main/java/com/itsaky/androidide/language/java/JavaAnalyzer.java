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
package com.itsaky.androidide.language.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.AnalyzeParams;
import com.itsaky.lsp.models.AnalyzeResult;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import io.github.rosemoe.editor.langs.AbstractCodeAnalyzer;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;

// TODO request language server for semantic highlights instead of waiting for it to send

/**
 * Code analyzer for the Java Language
 *
 * @author Akash Yadav
 */
public class JavaAnalyzer extends AbstractCodeAnalyzer {
    
    private final Map<Integer, List<Range>> stringMap = new HashMap<> ();
    private List<DiagnosticItem> diagnostics = new ArrayList<> ();
    private CompletableFuture<AnalyzeResult> future;
    
    @Override
    public void analyze (@Nullable ILanguageServer languageServer,
                         @Nullable File file,
                         @NonNull Content content,
                         @NonNull TextAnalyzeResult colors,
                         @NonNull TextAnalyzer.AnalyzeThread.Delegate delegate
    ) throws Exception {
        colors.addNormalIfNull ();
        
        if (languageServer == null || file == null) {
            return;
        }
        
        if (future != null && !future.isDone ()) {
            future.cancel (true);
        }
        
        future = CompletableFuture.supplyAsync (() -> languageServer
                .getCodeAnalyzer ()
                .analyze (new AnalyzeParams (file.toPath ()))
        );
        
        final var result = future.get ();
        this.diagnostics = result.getDiagnostics ();
    }
    
    @Override
    public DiagnosticItem findDiagnosticContaining (int line, int column) {
        final var position = new Position (line, column);
        
        // binary search the diagnostic item at the given position
        // compare by search position
        int left = 0; int right = diagnostics.size () - 1; int mid;
        while (left < right) {
            mid = (left + right) / 2;
            final var diagnostic = diagnostics.get (mid);
            final var range = diagnostic.getRange ();
            final var res = range.containsForBinarySearch (position);
            if (res < 0) {
                left = mid + 1;
            } else if (res > 0) {
                right = mid - 1;
            } else {
                return diagnostic;
            }
        }
        return super.findDiagnosticContaining (line, column);
    }
    
    @Override
    public List<DiagnosticItem> findDiagnosticsContainingLine (int line) {
        return diagnostics.stream ()
                .filter (diagnostic -> diagnostic.getRange ().containsLine (line))
                .collect (Collectors.toList ());
    }
    
    private static final Logger LOG = Logger.instance ("JavaAnalyzer");
}
