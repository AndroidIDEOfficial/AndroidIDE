package com.itsaky.androidide.models;

public final class SheetOption {
	public int id;
	public int iconRes;
	public int titleRes;
	public Object extra;

	public SheetOption(int id, int iconRes, int titleRes) {
		this.id = id;
		this.iconRes = iconRes;
		this.titleRes = titleRes;
	}
	
	public SheetOption(int id, int iconRes, int titleRes, Object extra) {
		this.id = id;
		this.iconRes = iconRes;
		this.titleRes = titleRes;
		this.extra = extra;
	}
	
	public SheetOption setExtra(Object extra) {
		this.extra = extra;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof SheetOption) {
			SheetOption that = (SheetOption) obj;
			return this.iconRes == that.iconRes
				&& this.titleRes == that.titleRes;
		}
		return false;
	}
}
