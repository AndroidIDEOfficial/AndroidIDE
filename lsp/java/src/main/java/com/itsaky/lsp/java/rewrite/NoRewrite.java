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

import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.lsp.models.TextEdit;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Helper class that simply returns the given code action items.
 *
 * @author Akash Yadav
 */
public class NoRewrite extends Rewrite {

    private final List<CodeActionItem> actions;

    public NoRewrite(List<CodeActionItem> actions) {
        this.actions = actions;
    }

    @Override
    public Map<Path, TextEdit[]> rewrite(CompilerProvider compiler) {
        return CANCELLED;
    }

    @Override
    public List<CodeActionItem> asCodeActions(CompilerProvider compiler, String title) {
        return actions;
    }
}
