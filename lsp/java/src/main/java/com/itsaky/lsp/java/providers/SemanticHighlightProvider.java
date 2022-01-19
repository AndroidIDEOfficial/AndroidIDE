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
import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.visitors.SemanticHighlighter;
import com.itsaky.lsp.models.HighlightToken;
import com.sun.source.tree.CompilationUnitTree;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Provides semantic highlight ranges.
 *
 * @author Akash Yadav
 */
public class SemanticHighlightProvider {
    
    private static final Comparator<HighlightToken> SORT_BY_START = Comparator.comparing (HighlightToken::getRange);
    
    @NonNull
    public static List<HighlightToken> highlight (@NonNull CompileTask task, Path file) {
        final List<HighlightToken> result = new ArrayList<> ();
        CompilationUnitTree root = null;
        for (CompilationUnitTree tree : task.roots) {
            final Path path = Paths.get (tree.getSourceFile ().toUri ());
            if (path.equals (file)) {
                root = tree;
                break;
            }
        }
        
        if (root == null) {
            LOG.warn ("Cannot provide semantic highlights. Cannot find compilation unit for the given file.");
            return result;
        }
        
        final SemanticHighlighter highlighter = new SemanticHighlighter (task);
        highlighter.scan (root, result);
        result.sort (SORT_BY_START);
        
        LOG.debug ("Java semantic highlights:", result);
        
        return result;
    }
    
    private static final Logger LOG = Logger.instance ("JavaSemanticHighlightProvider");
}
