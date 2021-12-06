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
package com.itsaky.androidide.utils;

import android.content.Context;
import android.util.TypedValue;
import com.itsaky.toaster.Toaster;

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
