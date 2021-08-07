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
