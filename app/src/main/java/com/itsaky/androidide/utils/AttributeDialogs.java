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

package com.itsaky.androidide.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static com.itsaky.androidide.utils.DialogUtils.newMaterialDialogBuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutDimensionEditorBinding;

/**
 * Helper class for creating dialogs for editing view attributes.
 *
 * TODO Should we merge this into {@link DialogUtils}
 * @author Akash Yadav
 */
public class AttributeDialogs {
    
    private static final String DIMENSION_MATCH = "match_parent";
    private static final String DIMENSION_WRAP = "wrap_content";
    
    /**
     * Context that will be used for creating dialogs.
     * This must be initialized by calling {@link #init(Context)}
     * and the reference must be released by calling {@link #release()}
     */
    private static Context mContext;
    
    /**
     * Initialize this class with the provided context.
     * @param ctx The context that will be used for creating dialogs.
     * @see #release()
     */
    public static void init (Context ctx) {
        mContext = ctx;
    }
    
    /**
     * Release the context instance stored in this class.
     * 
     * @see #init(Context)
     */
    public static void release () {
        mContext = null;
    }
    
    public static AlertDialog dimensionEditor (CharSequence[] values, CharSequence value, DialogInterface.OnClickListener onSubmit) {
        final var binding = LayoutDimensionEditorBinding.inflate(LayoutInflater.from (mContext));
        binding.choices.setOnCheckedChangeListener ((group, checkedId) -> {
            binding.otherValue.setVisibility (binding.other.isChecked () ? View.VISIBLE : View.GONE);
        });
        
        if (DIMENSION_MATCH.equals (value)) {
            binding.choices.check (R.id.match);
        } else if (DIMENSION_WRAP.equals (value)) {
            binding.choices.check (R.id.wrap);
        } else {
            binding.choices.check (R.id.other);
        }
        
        final var builder = newMaterialDialogBuilder (mContext);
        builder.setView (binding.getRoot ());
        builder.setPositiveButton (android.R.string.ok, ((dialog, which) -> {
            dialog.dismiss ();
            if (onSubmit != null) {
                onSubmit.onClick (dialog, which);
            }
        }));
        builder.setNegativeButton (android.R.string.cancel, (dialog, which) -> dialog.dismiss ());
        return builder.show ();
    }
    
    /**
     * @see #enumSelector(CharSequence[], int, DialogInterface.OnClickListener)
     */
    public static AlertDialog enumSelector (CharSequence[] values, CharSequence value, DialogInterface.OnClickListener onSelected) {
        return enumSelector (values, findIndexOf (values, value), onSelected);
    }
    
    private static int findIndexOf (@NonNull CharSequence[] values, CharSequence value) {
        for (int i=0;i<values.length;i++) {
            if (value.equals (values[i])) {
                return i;
            }
        }
        
        throw new IndexOutOfBoundsException ("Cannot find index for value: " + value);
    }
    
    /**
     * Create a new single choice dialog for selecting enum values.
     * @param values The value entries in the enum declaration.
     * @param selected The item that will be selected when the dialog is shown.
     * @param onSelected The click listener that will be invoked when an item is selected from the list.
     * @return The newly created dialog instance.
     */
    public static AlertDialog enumSelector (CharSequence[] values, int selected, DialogInterface.OnClickListener onSelected) {
        final var builder = newMaterialDialogBuilder (mContext);
        builder.setSingleChoiceItems (values, selected, (dialog, which) -> {
            dialog.dismiss ();
            if (onSelected != null) {
                onSelected.onClick (dialog, which);
            }
        });
        return builder.show ();
    }
    
    
}
