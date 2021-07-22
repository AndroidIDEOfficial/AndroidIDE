package com.itsaky.androidide.utils;
import android.content.Context;
import com.itsaky.toaster.Toaster;
import android.util.TypedValue;

public class StudioUtils {
	private Context mContext;
	private Toaster mToaster;

	public StudioUtils(Context mContext) {
		this.mContext = mContext;
		mToaster = getToaster();
	}

	private Toaster getToaster() {
		return mToaster == null ? mToaster = new Toaster(mContext) : mToaster;
	}

	public void toast(String msg, Toaster.Type type) {
		getToaster().setDuration(Toaster.SHORT)
			.setText(msg)
			.setType(type)
			.show();
	}

	public void toast(int msg, Toaster.Type type) {
		getToaster().setDuration(Toaster.SHORT)
			.setText(msg)
			.setType(type)
			.show();
	}

	public void toastLong(String msg, Toaster.Type type) {
		getToaster().setDuration(Toaster.LONG)
			.setText(msg)
			.setType(type)
			.show();
	}

	public void toastLong(int msg, Toaster.Type type) {
		getToaster().setDuration(Toaster.LONG)
			.setText(msg)
			.setType(type)
			.show();
	}

	public float toDp(int px) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, mContext.getResources().getDisplayMetrics());
	}

	public float toPx(int dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, mContext.getResources().getDisplayMetrics());
	}
}
