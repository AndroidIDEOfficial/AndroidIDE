/************************************************************************************
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
**************************************************************************************/
package com.itsaky.toaster;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

public class Toaster {
    
	private com.itsaky.androidide.common.databinding.LayoutToastBinding binding;

	private Toast mToast;
	private Context mContext;
	private Gravity mGravity;
	private Type mType;
	private Drawable mDrawable;
	private AnimatedVectorDrawableCompat mIcon;

	private int mIconColor = Color.DKGRAY;
	private int mDuration = 0;

	public static final int COLOR_SUCCESS = Color.parseColor("#4CAF50");
	public static final int COLOR_ERROR = Color.parseColor("#F44336");
	public static final int REVEAL_ANIM_DURATION = 500;
	public static final int SHORT = 2000;
	public static final int LONG = 3500;

	public Toaster(Context context) {
		this.mContext = context;
		this.mType = Type.INFO;

		this.mDrawable = createBackgroundDrawable();

		createView();
	}

	protected void createView() {
		binding = com.itsaky.androidide.common.databinding.LayoutToastBinding.inflate(LayoutInflater.from(mContext));
        binding.toastToastWrapper.setToasterInstance(this);
	}

	protected Drawable createBackgroundDrawable() {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setColor(ContextCompat.getColor(mContext, com.itsaky.androidide.common.R.color.color_toast_background));
		drawable.setCornerRadius(25);
		return drawable;
	}

	protected AnimatedVectorDrawableCompat createToastIconAnimation(int tintColor) {
		AnimatedVectorDrawableCompat imageDrawable = AnimatedVectorDrawableCompat.create(mContext, mType == Type.SUCCESS ? com.itsaky.androidide.common.R.drawable.ic_ok : com.itsaky.androidide.common.R.drawable.ic_error);
		imageDrawable.setTint(tintColor);
		imageDrawable.setTintMode(PorterDuff.Mode.SRC_ATOP);
		binding.toastImage.setImageDrawable(imageDrawable);
		return imageDrawable;
	}

    public void show() {
        int dp16 = toPx(16);
        mIconColor = mType == Type.SUCCESS ? COLOR_SUCCESS :
            mType == Type.ERROR ? COLOR_ERROR :
            Color.DKGRAY;
        int gravity = mGravity == Gravity.TOP_LEFT ? (android.view.Gravity.TOP | android.view.Gravity.START) :
            mGravity == Gravity.TOP_RIGHT ? (android.view.Gravity.TOP | android.view.Gravity.END) :
            mGravity == Gravity.BOTTOM_RIGHT ? (android.view.Gravity.BOTTOM | android.view.Gravity.END) :
            mGravity == Gravity.BOTTOM_LEFT ? (android.view.Gravity.BOTTOM | android.view.Gravity.START) :
            android.view.Gravity.TOP | android.view.Gravity.END;

        mIcon = createToastIconAnimation(mIconColor);

        mToast = Toast.makeText(mContext, binding.toastText.getText(), mDuration);
        mToast.setGravity(gravity, dp16, dp16);
        mToast.setDuration(mDuration);
        mToast.setView(binding.getRoot());
        mToast.show();

        binding.toastImage.setImageDrawable(mIcon);
		mIcon.start();
    }

	public Toaster setText(String text) {
		if (binding.toastText == null)
			createView();
            
        binding.toastText.setText(text);
		return this;
	}

	public Toaster setText(int textResId) {
		if (binding.toastText == null)
			createView();

		binding.toastText.setText(textResId);
		return this;
	}

	public Toaster setDuration(int mDuration) {
		this.mDuration = mDuration + REVEAL_ANIM_DURATION;
		return this;
	}

	public int getDuration() {
		return mDuration;
	}

	public Toaster setType(Type type) {
		this.mType = type;
		return this;
	}

	public Type getType() {
		return mType;
	}

	public Toaster setGravity(Gravity mGravity) {
		this.mGravity = mGravity;
		return this;
	}

	private int toPx(int dp) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics()));
	}

	public Gravity getGravity() {
		return mGravity;
	}

	public AnimatedVectorDrawableCompat getIcon() {
		return mIcon;
	}

	public Drawable getBackroundDrawable() {
		return mDrawable;
	}

	public int getIconColor() {
		return mIconColor;
	}

	public enum Type {
		ERROR,
		SUCCESS,
		INFO
        }

	public enum Gravity {
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT
        }
}
