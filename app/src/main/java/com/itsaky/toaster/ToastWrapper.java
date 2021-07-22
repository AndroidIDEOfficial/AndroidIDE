package com.itsaky.toaster;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import com.google.android.material.animation.ArgbEvaluatorCompat;
import android.os.Build;

public class ToastWrapper extends LinearLayout
{
	private Toaster mToaster;
	
	public ToastWrapper(Context context)
	{
		super(context);
	}
	
	public ToastWrapper(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public ToastWrapper(Context context, AttributeSet attrs, int style)
	{
		super(context, attrs, style);
	}

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		startRevealAnimation();
		startIconColorChangeAnimation();
	}

	protected void startIconColorChangeAnimation()
	{
		ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluatorCompat(), Color.WHITE, mToaster.getIconColor());
		anim.setDuration(Toaster.REVEAL_ANIM_DURATION + 300);
		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

				@Override
				public void onAnimationUpdate(ValueAnimator p1)
				{
					mToaster.getIcon().setTint((int) p1.getAnimatedValue());
				}
			});
		anim.start();
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		setBackgroundColor(Color.TRANSPARENT);
	}
	
	public void setToasterInstance(Toaster toaster)
	{
		this.mToaster = toaster;
	}
	
	protected void startRevealAnimation()
	{
		setBackgroundDrawable(mToaster.getBackroundDrawable());
		int x = (getLeft());// + getRight()) / 2;
		int y = (getTop() + getBottom()) / 2;
		int endRadius = Math.max(getHeight(), getWidth());
		Animator anim = ViewAnimationUtils.createCircularReveal(this, x, y, 0, endRadius);
		anim.setDuration(Toaster.REVEAL_ANIM_DURATION);
		anim.start();
	}
}
