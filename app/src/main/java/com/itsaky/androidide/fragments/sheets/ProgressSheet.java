/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
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

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.itsaky.androidide.databinding.LayoutProgressSheetBinding;
import com.blankj.utilcode.util.SizeUtils;
import android.widget.RelativeLayout;

public class ProgressSheet extends BaseBottomSheetFragment {
	
	private LayoutProgressSheetBinding binding;
	private Drawable drawable = null;
	private String message = "";
	private String subMessage = "";
	private boolean subMessageEnabled = false;
	private boolean welcomTextEnabled = false;
	
	@Override
	protected void bind(LinearLayout container) {
		binding = LayoutProgressSheetBinding.inflate(LayoutInflater.from(getContext()));
		container.addView(binding.getRoot());
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		binding.message.setText(message);
		if(subMessageEnabled) {
			binding.subMessage.setText(subMessage);
			binding.subMessage.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) binding.message.getLayoutParams();
			try {
				p.removeRule(RelativeLayout.CENTER_VERTICAL);
			} catch (Throwable th) {}
			binding.message.setLayoutParams(p);
		} else {
			binding.subMessage.setVisibility(View.GONE);
			RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) binding.message.getLayoutParams();
			try {
				p.addRule(RelativeLayout.CENTER_VERTICAL);
			} catch (Throwable th) {}
			binding.message.setLayoutParams(p);
		}
		
		if(!welcomTextEnabled) {
			binding.welcomeText.setVisibility(View.GONE);
		}
	}

	@Override
	protected boolean shouldHideTitle() {
		return true;
	}
	
	public ProgressSheet setWelcomeTextEnabled(boolean enabled) {
		this.welcomTextEnabled = enabled;
		return this;
	}
	
	public ProgressSheet setSubMessageEnabled(boolean enabled) {
		this.subMessageEnabled = enabled;
		return this;
	}
	
	public ProgressSheet setSubMessage(String msg) {
		this.subMessage = msg;
		if(isShowing()) {
			binding.subMessage.setText(msg);
		}
		return this;
	}
	
	public ProgressSheet setMessage(String message) {
		this.message = message;
		if(isShowing()) {
			binding.message.setText(message);
		}
		
		return this;
	}
	
	public ProgressSheet setProgressDrawable(Drawable drawable) {
		this.drawable = drawable;
		if(isShowing()) {
			binding.progress.setIndeterminateDrawable(drawable);
		}
		
		return this;
	}

	@Override
	public void dismiss() {
		if(isShowing())
			super.dismiss();
	}
}
