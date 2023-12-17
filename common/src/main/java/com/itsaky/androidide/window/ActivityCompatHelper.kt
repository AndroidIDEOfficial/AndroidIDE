/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itsaky.androidide.window

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Rect
import android.os.Build
import androidx.annotation.RequiresApi

@SuppressLint("ObsoleteSdkInt")
@RequiresApi(Build.VERSION_CODES.N)
internal object ActivityCompatHelperApi24 {

  fun isInMultiWindowMode(activity: Activity): Boolean {
    return activity.isInMultiWindowMode
  }
}

@RequiresApi(Build.VERSION_CODES.R)
internal object ActivityCompatHelperApi30 {

  fun currentWindowBounds(activity: Activity): Rect {
    return activity.windowManager.currentWindowMetrics.bounds
  }

  fun maximumWindowBounds(activity: Activity): Rect {
    return activity.windowManager.maximumWindowMetrics.bounds
  }
}