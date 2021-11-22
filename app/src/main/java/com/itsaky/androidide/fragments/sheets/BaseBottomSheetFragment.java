/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
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
**************************************************************************************/


package com.itsaky.androidide.fragments.sheets;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itsaky.androidide.databinding.LayoutSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.graphics.Rect;

public abstract class BaseBottomSheetFragment extends BottomSheetDialogFragment {

    protected Dialog mDialog;
    private LayoutSheetBinding binding;
    protected boolean shadowEnabled = true;
    protected boolean titleEnabled = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = super.onCreateDialog(savedInstanceState);
        mDialog.setOnShowListener(p1 -> onShow());
        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LayoutSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(binding.container);
        binding.title.setText(getTitle());
        binding.title.setOnClickListener(v -> handleTitleClick());

        if(shouldHideTitle()) {
            binding.getRoot().removeView(binding.title);
        }

        if(!shadowEnabled) {
            binding.shadow.setVisibility(View.GONE);
        }
    }

    private void handleTitleClick() {
        if(isCancelable()) {
            dismiss();
        }
    }

    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    public BaseBottomSheetFragment setTitle(int title) {
        if(binding != null && binding.title != null) {
            binding.title.setText(title);
        }
        return this;
    }

    public BaseBottomSheetFragment setTitle(String title) {
        if(binding != null && binding.title != null) {
            binding.title.setText(title);
        }
        return this;
    }

    protected boolean shouldHideTitle() {
        return !titleEnabled;
    }

    protected String getTitle() {
        return "";
    }

    public void setShowShadow(boolean enabled) {
        this.shadowEnabled = enabled;
    }

    public void setShowTitle(boolean enabled) {
        this.titleEnabled = enabled;
    }

    protected void onShow() {}
    protected abstract void bind(LinearLayout container);
}
