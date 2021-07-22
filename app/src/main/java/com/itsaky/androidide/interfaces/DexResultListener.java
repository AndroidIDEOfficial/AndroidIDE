package com.itsaky.androidide.interfaces;

import androidx.core.util.Pair;
import java.util.List;

public interface DexResultListener {
	public void onDex(String name);
	public void onDexSuccess(List<String> dexes);
	public void onDexFailed(String message);
}
