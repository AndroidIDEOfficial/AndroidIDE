package com.itsaky.androidide.models;

import android.os.Parcelable;
import android.os.Parcel;

public class DexPair implements Parcelable {
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel p1, int p2) {
		p1.writeString(first);
		p1.writeString(second);
	}
	
	public String first;
	public String second;
	
	private DexPair(Parcel in) {
		first = in.readString();
		second = in.readString();
	}
	
	public DexPair(String first, String second) {
		this.first = first;
		this.second = second;
	}
	
	public static final Creator<DexPair> CREATOR = new Creator<DexPair>(){

		@Override
		public DexPair createFromParcel(Parcel p1) {
			return new DexPair(p1);
		}

		@Override
		public DexPair[] newArray(int p1) {
			return new DexPair[p1];
		}
	};
}
