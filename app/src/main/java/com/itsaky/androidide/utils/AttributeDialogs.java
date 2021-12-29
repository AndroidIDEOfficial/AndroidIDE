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

import static com.itsaky.androidide.utils.DialogUtils.newMaterialDialogBuilder;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.colorpicker.ColorPickerView;
import com.itsaky.androidide.databinding.LayoutDimensionAttrEditorBinding;
import com.itsaky.androidide.databinding.LayoutTextAttrEditorBinding;
import com.itsaky.toaster.Toaster;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Helper class for creating dialogs for editing view attributes.
 * <br>
 * TODO Should we merge this into {@link DialogUtils}?
 * TODO An attribute can accept multiple types of values (dimension, enum, reference, etc)
 *     Can we somehow combine the editors according to attribute format?
 * @author Akash Yadav
 */
public class AttributeDialogs {
    
    private static final String DIMENSION_MATCH = "match_parent";
    private static final String DIMENSION_WRAP = "wrap_content";
    
    private static final Logger LOG = Logger.instance ("AttributeDialogs");
    
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
    
    @NonNull
    public static AlertDialog booleanEditor (OnClickListener listener) {
        final var items = new String [] {"true", "false"};
        final var builder = newMaterialDialogBuilder (mContext);
        builder.setTitle (mContext.getString(R.string.msg_choose_new_val));
        builder.setItems (items, (dialog, which) -> {
            dialog.dismiss ();
            final var val = items [which];
            if (listener != null) {
                listener.onClick (dialog, which, val);
            }
        });
        return builder.create ();
    }
    
    /**
     * Create a new dialog for editing an attribute with format STRING.
     * @param listener The listener that will be invoked when the user clicks the
     *                 positive button.
     * @return The newly created dialog instance.
     */
    @NonNull
    public static AlertDialog stringEditor (OnClickListener listener) {
        final var binding = LayoutTextAttrEditorBinding.inflate (LayoutInflater.from (mContext));
        final var builder = newMaterialDialogBuilder (mContext);
        builder.setTitle (mContext.getString(R.string.msg_new_str_value));
        builder.setView (binding.getRoot ());
        builder.setPositiveButton (android.R.string.ok, (dialog, which) -> {
            final var newValue = binding.valueField.getEditText ().getText ().toString ().trim ();
            if (newValue.length () <= 0) {
                StudioApp.getInstance ().toast (mContext.getString (R.string.msg_invalid_attr_value), Toaster.Type.ERROR);
                return;
            }
            if (listener != null) {
                listener.onClick (dialog, which, newValue);
            }
        });
        builder.setNegativeButton (android.R.string.cancel, (dialog, which) -> dialog.dismiss ());
        return builder.create ();
    }
    
    /**
     * Create a new dialog for editing an attribute which accepts dimension values.
     * @param value The current value of the attribute. Used for determining which item must be selected by default.
     * @param listener A listener that will be invoked when the user presses the positive button of the dialog.
     *                 This listener is invoked only when the user passes a valid dimension value.
     * @return The newly created dialog instance. Note that this dialog is not shown by default.
     */
    @NonNull
    public static AlertDialog dimensionEditor (CharSequence value, OnClickListener listener) {
        final var binding = LayoutDimensionAttrEditorBinding.inflate(LayoutInflater.from (mContext));
        binding.choices.setOnCheckedChangeListener ((group, checkedId) -> binding.otherValue.setVisibility (binding.other.isChecked () ? View.VISIBLE : View.GONE));
        
        if (DIMENSION_MATCH.contentEquals (value)) {
            binding.choices.check (R.id.match);
        } else if (DIMENSION_WRAP.contentEquals (value)) {
            binding.choices.check (R.id.wrap);
        } else {
            binding.choices.check (R.id.other);
        }
        
        final var builder = newMaterialDialogBuilder (mContext);
        builder.setView (binding.getRoot ());
        builder.setPositiveButton (android.R.string.ok, ((dialog, which) -> {
            var val = "";
            if (binding.choices.getCheckedRadioButtonId () == R.id.match) {
                val = DIMENSION_MATCH;
            } else if (binding.choices.getCheckedRadioButtonId () == R.id.wrap) {
                val = DIMENSION_WRAP;
            } else {
                if (binding.otherValue.getEditText () != null) {
                    val = binding.otherValue.getEditText ().getText ().toString ().trim ();
                } else {
                    val = "";
                }
            }
            
            if (val.length () <= 0) {
                StudioApp.getInstance ().toast (mContext.getString(R.string.msg_invalid_attr_value), Toaster.Type.ERROR);
                return;
            }
    
            dialog.dismiss ();
            
            if (listener != null) {
                listener.onClick (dialog, which, val);
            }
        }));
        builder.setNegativeButton (android.R.string.cancel, (dialog, which) -> dialog.dismiss ());
        return builder.create ();
    }
    
    /**
     * @see #enumEditor(CharSequence[], int, OnClickListener)
     */
    public static AlertDialog enumEditor (CharSequence[] values, CharSequence value, OnClickListener listener) {
        return enumEditor (values, findIndexOf (values, value), listener);
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
     * @param listener The click listener that will be invoked when an item is selected from the list.
     * @return The newly created dialog instance.
     */
    public static AlertDialog enumEditor (CharSequence[] values, int selected, OnClickListener listener) {
        final var builder = newMaterialDialogBuilder (mContext);
        builder.setSingleChoiceItems (values, selected, (dialog, which) -> {
            dialog.dismiss ();
            final var val = values [which].toString ();
            if (listener != null) {
                listener.onClick (dialog, which, val);
            }
        });
        return builder.show ();
    }
    
    /**
     * Creates a new attribute editor dialog to edit flag attributes.
     * @param values The possible values.
     * @param value The current value of the attribute.
     * @param listener The listener that will be invoked when the user presses the positive button.
     * @return The newly created dialog instance.
     */
    @NonNull
    public static AlertDialog flagEditor (@NonNull CharSequence[] values, @NonNull CharSequence value, OnClickListener listener) {
        final var checked = new boolean [values.length];
        final var vals = value.toString ().split (Pattern.quote ("|"));
        for (var i=0;i<values.length;i++) {
            for (var val : vals) {
                if (val.contentEquals (values[i])) {
                    checked [i] = true;
                }
            }
        }
        
        return flagEditor (values, checked, listener);
    }
    
    /**
     * Creates a new attribute editor dialog to edit flag attributes.
     * @param values The possible values.
     * @param selected The values that must be selected by default.
     * @param listener The listener that will be invoked when the user presses the positive button.
     * @return The newly created dialog instance.
     */
    @NonNull
    public static AlertDialog flagEditor (@NonNull CharSequence[] values, boolean[] selected, OnClickListener listener) {
        final var checked = new ArrayList<CharSequence> ();
        for (var i=0;i<values.length;i++) {
            if (selected[i]) {
                checked.add (values[i]);
            }
        }
        final var builder = newMaterialDialogBuilder (mContext);
        builder.setMultiChoiceItems (values, selected, (dialog, which, isChecked) -> {
            if (isChecked) {
                checked.add (values[which]);
            } else {
                checked.remove (values[which]);
            }
        });
        builder.setPositiveButton (android.R.string.ok, (dialog, which) -> {
            if (checked.size () <= 0) {
                StudioApp.getInstance ().toast (mContext.getString(R.string.msg_select_one_flag), Toaster.Type.ERROR);
                return;
            }
            
            final var val = TextUtils.join ("|", checked);
    
            if (listener != null) {
                listener.onClick (dialog, which, val);
            }
        });
        builder.setNegativeButton (android.R.string.cancel, (dialog, which) -> dialog.dismiss ());
        return builder.create ();
    }
    
    @NonNull
    public static AlertDialog colorPicker (final ColorPickerView.OnPickListener listener) {
        final var view = new ColorPickerView (mContext);
        final var builder = newMaterialDialogBuilder (mContext);
        builder.setView (view);
        
        final var dialog = builder.create ();
        dialog.getWindow ().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    
        view.setOnPickListener ((color, hexCode) -> {
            dialog.dismiss ();
            if (listener != null) {
                listener.onPick (color, hexCode);
            }
        });
        
        return dialog;
    }
    
    public interface OnClickListener {
        void onClick (DialogInterface dialog, int which, String newValue);
    }
}