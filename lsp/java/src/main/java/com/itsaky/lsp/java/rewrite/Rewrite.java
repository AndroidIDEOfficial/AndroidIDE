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

package com.itsaky.lsp.java.rewrite;

import com.itsaky.lsp.java.CompilerProvider;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.TextEdit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public interface Rewrite {
    /** Perform a rewrite across the entire codebase. */
    Map<Path, TextEdit[]> rewrite(CompilerProvider compiler);
    /** CANCELLED signals that the rewrite couldn't be completed. */
    Map<Path, TextEdit[]> CANCELLED = Collections.emptyMap ();
    
    Rewrite NOT_SUPPORTED = new RewriteNotSupported();
}