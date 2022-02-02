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

package com.itsaky.androidide.views.editor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.lsp.models.DiagnosticItem;

/**
 * Popup window used to show diagnostic messages.
 *
 * @author Akash Yadav
 */
public class DiagnosticWindow extends SimpleTextWindow {
    
    /**
     * Create a popup window for editor
     *
     * @param editor The editor
     * @see #FEATURE_SCROLL_AS_CONTENT
     * @see #FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED
     * @see #FEATURE_HIDE_WHEN_FAST_SCROLL
     */
    public DiagnosticWindow (@NonNull IDEEditor editor) {
        super (editor);
    
        final var context = editor.getContext ();
        final var dp4 = SizeUtils.dp2px (4);
        final var dp8 = dp4 * 2;
        
        this.text.setPaddingRelative (dp8, dp4, dp8, dp4);
    }
    
    /**
     * Show the given diagnostic item.
     * @param diagnostic The diagnostic item to show.
     */
    public void showDiagnostic (@Nullable DiagnosticItem diagnostic) {
        if (diagnostic == null) {
            if (isShowing ()) {
                dismiss ();
            }
            
            return;
        }
        
        final var message = diagnostic.getMessage ();
        this.text.setText (message);
        
        displayWindow ();
    }
}
