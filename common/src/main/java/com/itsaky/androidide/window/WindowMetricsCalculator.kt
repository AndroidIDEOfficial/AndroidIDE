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

import android.app.Activity
import android.os.Build
import android.view.Display

/**
 * An interface to calculate the [WindowMetrics] for an [Activity].
 */
internal interface WindowMetricsCalculator {

  /**
   * Computes the size and position of the area the window would occupy with
   * [MATCH_PARENT][android.view.WindowManager.LayoutParams.MATCH_PARENT] width and height
   * and any combination of flags that would allow the window to extend behind display cutouts.
   *
   * For example, [android.view.WindowManager.LayoutParams.layoutInDisplayCutoutMode] set to
   * [android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS] or the
   * [android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS] flag set.
   *
   * The value returned from this method may be different from platform API(s) used to determine
   * the size and position of the visible area a given context occupies. For example:
   *
   *  * [Display.getSize] can be used to determine the size of the visible area
   * a window occupies, but may be subtracted to exclude certain system decorations that
   * always appear on screen, notably the navigation bar.
   *  * The decor view's [android.view.View#getWidth] and [android.view.View@getHeight] can be
   *  used to determine the size of the top level view in the view hierarchy, but this size is
   * determined through a combination of [android.view.WindowManager.LayoutParams]
   * flags and may not represent the true window size. For example, a window that does not
   * indicate it can be displayed behind a display cutout will have the size of the decor
   * view offset to exclude this region unless this region overlaps with the status bar, while
   * the value returned from this method will include this region.
   *
   * The value returned from this method is guaranteed to be correct on platforms
   * [Q][Build.VERSION_CODES.Q] and above. For older platforms the value may be invalid if
   * the activity is in multi-window mode or if the navigation bar offset can not be accounted
   * for, though a best effort is made to ensure the returned value is as close as possible to
   * the true value. See [.computeWindowBoundsP] and
   * [.computeWindowBoundsN].
   *
   * Note: The value of this is based on the last windowing state reported to the client.
   *
   * @see android.view.WindowManager.getCurrentWindowMetrics
   * @see android.view.WindowMetrics.getBounds
   */
  fun computeCurrentWindowMetrics(activity: Activity): WindowMetrics

  /**
   * Computes the maximum size and position of the area the window can expect with
   * [MATCH_PARENT][android.view.WindowManager.LayoutParams.MATCH_PARENT] width and height
   * and any combination of flags that would allow the window to extend behind display cutouts.
   *
   * The value returned from this method will always match [Display.getRealSize] on
   * [Android 10][Build.VERSION_CODES.Q] and below.
   *
   * @see android.view.WindowManager.getMaximumWindowMetrics
   */
  fun computeMaximumWindowMetrics(activity: Activity): WindowMetrics
}
