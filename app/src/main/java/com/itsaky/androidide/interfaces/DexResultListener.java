package com.itsaky.androidide.interfaces;

import androidx.core.util.Pair;
import java.util.List;

/**
 * A callback to listen for updates while dexing JAR files
 * 
 * @deprecated This is replaced by the ClassFileReader. There is no need to dex the JAR files in order to load them.
 */

@Deprecated
public interface DexResultListener {
	public void onDex(String name);
	public void onDexSuccess(List<String> dexes);
	public void onDexFailed(String message);
}
