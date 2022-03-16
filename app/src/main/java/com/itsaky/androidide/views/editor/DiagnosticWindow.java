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

import static com.itsaky.androidide.utils.Logger.instance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutDiagnosticWindowBinding;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.lsp.models.DiagnosticItem;

import java.util.Collections;

/**
 * Popup window used to show diagnostic messages.
 *
 * @author Akash Yadav
 */
public class DiagnosticWindow extends SimpleTextWindow {
    
    private static final Logger LOG = instance ("DiagnosticWindow");
    private LayoutDiagnosticWindowBinding binding;
    
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
    }
    
    @Override
    protected View onCreateContentView (@NonNull Context context) {
        this.binding = LayoutDiagnosticWindowBinding.inflate (LayoutInflater.from (context));
        this.binding.getRoot ().setBackground (createBackground ());
        return this.binding.getRoot ();
    }
    
    @Override
    protected TextView onCreateTextView (@NonNull IDEEditor editor) {
        return this.binding.text;
    }
    
    @Override
    protected View getRootView () {
        return this.binding.getRoot ();
    }
    
    /**
     * Show the given diagnostic item.
     *
     * @param diagnostic The diagnostic item to show.
     */
    public void showDiagnostic (@Nullable DiagnosticItem diagnostic) {
        if (diagnostic == null) {
            if (isShowing ()) {
                dismiss ();
            }
            
            if (this.binding != null) {
                this.binding.fix.setVisibility (View.GONE);
            }
            
            return;
        }
        
        final var message = diagnostic.getMessage ();
        this.binding.text.setText (message);
        this.binding.fix.setVisibility (View.VISIBLE);
        this.binding.fix.setEnabled (false);
        
        final var editor = (IDEEditor) getEditor ();
        final var future = editor.codeActions (Collections.singletonList (diagnostic));
        future.whenComplete ((result, throwable) -> {
            ThreadUtils.runOnUiThread (() -> {
                if (result == null || result.getActions ().isEmpty () || throwable != null) {
                    LOG.error ("No code actions were found for the given diagnostic item", throwable);
                    binding.fix.setVisibility (View.GONE);
                    return;
                }
                
                binding.fix.setVisibility (View.VISIBLE);
                binding.fix.setEnabled (true);
                binding.fix.setClickable (true);
                binding.fix.setFocusable (true);
                binding.fix.setOnClickListener (v -> {
                    final var actions = result.getActions ();
                    if (actions.size () == 1) {
                        editor.performCodeAction (actions.get (0));
                    } else {
                        final var builder = DialogUtils.newMaterialDialogBuilder (editor.getContext ());
                        builder.setItems (actions.stream ().map (CodeActionItem::getTitle).toArray (String[]::new), (dialog, which) -> {
                            editor.performCodeAction (actions.get (which));
                        });
                        builder.setPositiveButton (R.string.cancel, (dialog, which) -> dialog.dismiss ());
                        builder.show ();
                    }
                });
            });
        });
        
        
        displayWindow ();
    }
}
