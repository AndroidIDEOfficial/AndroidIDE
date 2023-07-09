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
 */

package com.itsaky.androidide.fragments.sheets;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import com.itsaky.androidide.databinding.LayoutProgressSheetBinding;

public class ProgressSheet extends BaseBottomSheetFragment {

  private LayoutProgressSheetBinding binding;
  private String message = "";
  private String subMessage = "";
  private boolean subMessageEnabled = false;

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    binding.message.setText(message);

    final var params = (ConstraintLayout.LayoutParams) binding.message.getLayoutParams();
    if (subMessageEnabled) {
      binding.subMessage.setText(subMessage);
      binding.subMessage.setVisibility(View.VISIBLE);
      params.bottomToBottom = View.NO_ID;
    } else {
      binding.subMessage.setVisibility(View.GONE);
      params.bottomToBottom = LayoutParams.PARENT_ID;
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState
  ) {
    binding = LayoutProgressSheetBinding.inflate(LayoutInflater.from(getContext()));
    return binding.getRoot();
  }

  public void setSubMessageEnabled(boolean enabled) {
    this.subMessageEnabled = enabled;
  }

  public void setSubMessage(String msg) {
    this.subMessage = msg;
    if (isShowing()) {
      binding.subMessage.setText(msg);
    }
  }

  public ProgressSheet setMessage(String message) {
    this.message = message;
    if (isShowing()) {
      binding.message.setText(message);
    }

    return this;
  }

  public ProgressSheet setProgressDrawable(Drawable drawable) {
    if (isShowing()) {
      binding.progress.setIndeterminateDrawable(drawable);
    }
    return this;
  }

  @Override
  public void dismiss() {
    if (isShowing()) {
      super.dismiss();
    }
  }
}
