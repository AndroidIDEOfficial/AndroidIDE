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

package com.itsaky.androidide.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.itsaky.androidide.databinding.LayoutBannerBinding;

public class MaterialBanner extends CoordinatorLayout {

  private LayoutBannerBinding binding;
  private OnPositiveClickListener positiveListener;
  private OnNegativeClickListener negativeListener;

  private String contentText;
  private String leftButtonText;
  private String rightButtonText;
  private Drawable iconDrawable;
  private int backgroundColor;
  private int contentTextColor;
  private int buttonTextColor;

  public MaterialBanner(Context context) {
    this(context, null);
  }

  public MaterialBanner(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MaterialBanner(Context context, AttributeSet attrs, int style) {
    super(context, attrs, style);
    init();
  }

  private void init() {
    binding = LayoutBannerBinding.inflate(LayoutInflater.from(getContext()), this, true);
  }

  public void show() {
    this.binding.contentTextView.setText(contentText);
    this.binding.contentIconView.setImageDrawable(iconDrawable);
    this.binding.rightButton.setText(rightButtonText);
    this.binding.leftButton.setText(leftButtonText);
    this.binding.getRoot().setBackgroundColor(backgroundColor);
    this.binding.contentTextView.setTextColor(contentTextColor);
    this.binding.leftButton.setTextColor(buttonTextColor);
    this.binding.rightButton.setTextColor(buttonTextColor);
    this.binding.rightButton.setOnClickListener(
        v -> {
          if (positiveListener == null) dismiss();
          else {
            positiveListener.onClick(MaterialBanner.this);
            dismiss();
          }
        });
    this.binding.leftButton.setOnClickListener(
        v -> {
          if (negativeListener == null) dismiss();
          else {
            negativeListener.onClick(MaterialBanner.this);
            dismiss();
          }
        });

    final ConstraintLayout root = this.binding.getRoot();
    root.measure(-1, -2);
    final int height = root.getMeasuredHeight();
    final LayoutParams p = (LayoutParams) root.getLayoutParams();
    p.height = 0;
    root.setLayoutParams(p);
    root.setVisibility(VISIBLE);
    setVisibility(VISIBLE);

    Animation anim =
        new Animation() {
          @Override
          public boolean willChangeBounds() {
            return true;
          }

          @Override
          public void applyTransformation(float time, Transformation t) {
            p.height = time == 1f ? -2 : (int) (height * time);
            root.setLayoutParams(p);
            root.requestLayout();
          }
        };

    anim.setDuration(200);
    root.startAnimation(anim);
  }

  public void dismiss() {
    final ConstraintLayout root = this.binding.getRoot();
    final LayoutParams p = (LayoutParams) root.getLayoutParams();
    final int initialHeight = getMeasuredHeight();
    Animation animation =
        new Animation() {
          @Override
          public boolean willChangeBounds() {
            return true;
          }

          @Override
          public void applyTransformation(float interpolatedTime, Transformation t) {
            if (interpolatedTime == 1f) {
              root.setVisibility(GONE);
              setVisibility(GONE);
            } else {
              p.height = (int) (initialHeight - (initialHeight * interpolatedTime));
              root.requestLayout();
            }
          }
        };
    animation.setDuration((long) (initialHeight / getResources().getDisplayMetrics().density));
    root.startAnimation(animation);
  }

  public MaterialBanner setIcon(@DrawableRes int icon) {
    return setIcon(ContextCompat.getDrawable(getContext(), icon));
  }

  public MaterialBanner setIcon(Drawable icon) {
    this.iconDrawable = icon;
    return this;
  }

  public MaterialBanner setPositive(@StringRes int text, OnPositiveClickListener listener) {
    return setPositive(getContext().getString(text), listener);
  }

  public MaterialBanner setPositive(String text, OnPositiveClickListener listener) {
    this.rightButtonText = text;
    this.positiveListener = listener;
    return this;
  }

  public MaterialBanner setNegative(@StringRes int text, OnNegativeClickListener listener) {
    return setNegative(getContext().getString(text), listener);
  }

  public MaterialBanner setNegative(String text, OnNegativeClickListener listener) {
    this.leftButtonText = text;
    this.negativeListener = listener;
    return this;
  }

  public MaterialBanner setContentText(@StringRes int text) {
    return setContentText(getContext().getString(text));
  }

  public MaterialBanner setContentText(String contentText) {
    this.contentText = contentText;
    return this;
  }

  public MaterialBanner setBannerBackgroundColor(int backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  public MaterialBanner setContentTextColor(int contentTextColor) {
    this.contentTextColor = contentTextColor;
    return this;
  }

  public MaterialBanner setButtonTextColor(int buttonTextColor) {
    this.buttonTextColor = buttonTextColor;
    return this;
  }

  public static interface OnPositiveClickListener {
    public void onClick(MaterialBanner banner);
  }

  public static interface OnNegativeClickListener {
    public void onClick(MaterialBanner banner);
  }
}
