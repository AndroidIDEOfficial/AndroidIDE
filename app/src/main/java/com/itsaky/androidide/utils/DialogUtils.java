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

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;

import org.jetbrains.annotations.Contract;

/**
 * Utility class for creating dialogs.
 *
 * @author Akash Yadav
 */
public class DialogUtils {
    
    /**
     * Creates a new MaterialAlertDialogBuilder with the app's default style.
     * @param context The context for the dialog builder.
     * @return The new MaterialAlertDialogBuilder instance.
     */
    @NonNull
    @Contract("_ -> new")
    public static MaterialAlertDialogBuilder newMaterialDialogBuilder (Context context) {
        return new MaterialAlertDialogBuilder (context, R.style.AppTheme_MaterialAlertDialog);
    }
    
    /**
     * Create a new alert dialog with two buttons: <span>Yes</span> and <span>No</span>.
     * This method simply calls {@link #newYesNoDialog(Context, String, String, DialogInterface.OnClickListener, DialogInterface.OnClickListener)}
     * with default values for title and message.
     *
     * @param context The context for the dialog.
     * @param positiveClickListener A listener that will be invoked on the <span>Yes</span> button click.
     * @param negativeClickListener A listener that will be invoked on the <span>No</span> button click.
     * @return The newly created dialog.
     */
    @NonNull
    public static MaterialAlertDialogBuilder newYesNoDialog (Context context,
                                                             DialogInterface.OnClickListener positiveClickListener,
                                                             DialogInterface.OnClickListener negativeClickListener) {
        return newYesNoDialog (context,
                context.getString(R.string.msg_yesno_def_title),
                context.getString(R.string.msg_yesno_def_message),
                positiveClickListener,
                negativeClickListener);
    }
    
    /**
     * Create a new alert dialog with two buttons: <span>Yes</span> and <span>No</span>.
     *
     * @param context The context for the dialog.
     * @param title The title of the dialog.
     * @param message The message of the dialog.
     * @param positiveClickListener A listener that will be invoked on the <span>Yes</span> button click.
     * @param negativeClickListener A listener that will be invoked on the <span>No</span> button click.
     * @return The newly created dialog instance.
     */
    @NonNull
    public static MaterialAlertDialogBuilder newYesNoDialog (Context context,
                                                             String title,
                                                             String message,
                                                             DialogInterface.OnClickListener positiveClickListener,
                                                             DialogInterface.OnClickListener negativeClickListener) {
        final var builder = DialogUtils.newMaterialDialogBuilder (context);
        builder.setTitle (title);
        builder.setMessage (message);
        builder.setPositiveButton (R.string.yes, positiveClickListener);
        builder.setNegativeButton (R.string.no, negativeClickListener);
        
        return builder;
    }
}
