/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
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
