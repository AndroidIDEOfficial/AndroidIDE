/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.fragments.sheets;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class BaseBottomSheetFragment extends BottomSheetDialogFragment {

  protected Dialog mDialog;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    mDialog = super.onCreateDialog(savedInstanceState);

    var behavior = ((BottomSheetDialog) mDialog).getBehavior();
    behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    return mDialog;
  }

  public boolean isShowing() {
    return mDialog != null && mDialog.isShowing();
  }
}
