package com.itsaky.androidide.utils;

import android.graphics.Typeface;
import com.itsaky.androidide.app.StudioApp;

public class TypefaceUtils {

	public static Typeface quicksand() {
		return Typeface.createFromAsset(StudioApp.getInstance().getAssets(), "fonts/quicksand.ttf");
	}

	public static Typeface jetbrainsMono() {
		return Typeface.createFromAsset(StudioApp.getInstance().getAssets(), "fonts/jetbrains-mono.ttf");
	}
}
