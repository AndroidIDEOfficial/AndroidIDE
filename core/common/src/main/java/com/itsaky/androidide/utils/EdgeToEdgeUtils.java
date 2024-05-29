/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Copyright (C) 2022 The Android Open Source Project
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

package com.itsaky.androidide.utils;

import static android.graphics.Color.TRANSPARENT;
import static com.google.android.material.color.MaterialColors.isColorLight;

import android.content.Context;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.Window;
import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.ComponentActivity;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.google.android.material.color.MaterialColors;

/**
 * A util class that helps apply edge-to-edge mode to activity/dialog windows.
 * <p>
 * <b>AndroidIDE Changed</b>: Allow to choose if the status bar and navigation bar's color should
 * be changed or not.
 *
 * @see <a
 * href="https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/internal/EdgeToEdgeUtils.java">EdgeToEdgeUtils.java</a>
 */
public class EdgeToEdgeUtils {

  private static final int EDGE_TO_EDGE_BAR_ALPHA = 128;

  public static final int DEFAULT_LIGHT_SCRIM = EdgeToEdge.getDefaultLightScrim();
  public static final int DEFAULT_DARK_SCRIM = EdgeToEdge.getDefaultDarkScrim();

  public static final SystemBarStyle DEFAULT_STATUS_BAR_STYLE = SystemBarStyle.auto(TRANSPARENT,
      TRANSPARENT);
  public static final SystemBarStyle DEFAULT_NAVIGATION_BAR_STYLE = SystemBarStyle.auto(
      DEFAULT_LIGHT_SCRIM, DEFAULT_DARK_SCRIM);

  private EdgeToEdgeUtils() {
  }

  /**
   * Enable edge-to-edge mode for the provided activity, using the built-in
   * {@link EdgeToEdge#enable(androidx.activity.ComponentActivity, SystemBarStyle,
   * SystemBarStyle)}.
   *
   * @param activity           The activity to enable edge-to-edge mode for.
   * @param statusBarStyle     The status bar style to use.
   * @param navigationBarStyle The navigation bar style to use.
   */
  public static void applyEdgeToEdge(
      ComponentActivity activity,
      SystemBarStyle statusBarStyle,
      SystemBarStyle navigationBarStyle
  ) {
    EdgeToEdge.enable(activity, statusBarStyle, navigationBarStyle);
  }

  /**
   * Applies or removes edge-to-edge mode to the provided {@link Window}. When edge-to-edge mode is
   * applied, the activities, or the non-floating dialogs, that host the provided window will be
   * drawn over the system bar area by default and the system bar colors will be adjusted according
   * to the background color you provide.
   */
  public static void applyEdgeToEdgeManually(
      @NonNull Window window,
      boolean edgeToEdgeEnabled,
      @Nullable @ColorInt Integer statusBarColor,
      @Nullable @ColorInt Integer navBarColor) {
    applyEdgeToEdgeManually(window, edgeToEdgeEnabled, null, null, statusBarColor,
        navBarColor);
  }

  /**
   * Applies or removes edge-to-edge mode to the provided {@link Window}. When edge-to-edge mode is
   * applied, the activities, or the non-floating dialogs, that host the provided window will be
   * drawn over the system bar area by default and the system bar colors will be adjusted according
   * to the background color you provide.
   *
   * @param statusBarOverlapBackgroundColor     The reference background color to decide the
   *                                            text/icon colors on status bars. {@code null} to use
   *                                            the default color from
   *                                            {@code ?android:attr/colorBackground}.
   * @param navigationBarOverlapBackgroundColor The reference background color to decide the icon
   *                                            colors on navigation bars.{@code null} to use the
   *                                            default color from
   *                                            {@code ?android:attr/colorBackground}.
   */
  public static void applyEdgeToEdgeManually(
      @NonNull Window window,
      boolean edgeToEdgeEnabled,
      @Nullable @ColorInt Integer statusBarOverlapBackgroundColor,
      @Nullable @ColorInt Integer navigationBarOverlapBackgroundColor,
      @Nullable @ColorInt Integer statusBarColor,
      @Nullable @ColorInt Integer navBarColor
  ) {

    // If the overlapping background color is unknown or TRANSPARENT, use the default one.
    boolean useDefaultBackgroundColorForStatusBar =
        statusBarOverlapBackgroundColor == null || statusBarOverlapBackgroundColor == 0;
    boolean useDefaultBackgroundColorForNavigationBar =
        navigationBarOverlapBackgroundColor == null || navigationBarOverlapBackgroundColor == 0;
    if (useDefaultBackgroundColorForStatusBar || useDefaultBackgroundColorForNavigationBar) {
      int defaultBackgroundColor =
          MaterialColors.getColor(window.getContext(), android.R.attr.colorBackground, Color.BLACK);
      if (useDefaultBackgroundColorForStatusBar) {
        statusBarOverlapBackgroundColor = defaultBackgroundColor;
      }
      if (useDefaultBackgroundColorForNavigationBar) {
        navigationBarOverlapBackgroundColor = defaultBackgroundColor;
      }
    }

    WindowCompat.setDecorFitsSystemWindows(window, !edgeToEdgeEnabled);

    int color = statusBarColor != null ? statusBarColor
        : getStatusBarColor(window.getContext(), edgeToEdgeEnabled);
    window.setStatusBarColor(color);
    setLightStatusBar(
        window,
        isUsingLightSystemBar(color, isColorLight(statusBarOverlapBackgroundColor)));

    color = navBarColor != null ? navBarColor
        : getNavigationBarColor(window.getContext(), edgeToEdgeEnabled);
    window.setNavigationBarColor(color);
    setLightNavigationBar(
        window,
        isUsingLightSystemBar(
            color, isColorLight(navigationBarOverlapBackgroundColor)));
  }

  /**
   * Changes the foreground color of the status bars to light or dark so that the items on the bar
   * can be read clearly.
   *
   * @param window  Window that hosts the status bars
   * @param isLight {@code true} to make the foreground color light
   */
  public static void setLightStatusBar(@NonNull Window window, boolean isLight) {
    WindowInsetsControllerCompat insetsController =
        WindowCompat.getInsetsController(window, window.getDecorView());
    insetsController.setAppearanceLightStatusBars(isLight);
  }

  /**
   * Changes the foreground color of the navigation bars to light or dark so that the items on the
   * bar can be read clearly.
   *
   * @param window  Window that hosts the status bars
   * @param isLight {@code true} to make the foreground color light.
   */
  public static void setLightNavigationBar(@NonNull Window window, boolean isLight) {
    WindowInsetsControllerCompat insetsController =
        WindowCompat.getInsetsController(window, window.getDecorView());
    insetsController.setAppearanceLightNavigationBars(isLight);
  }

  private static int getStatusBarColor(Context context, boolean isEdgeToEdgeEnabled) {
    if (isEdgeToEdgeEnabled) {
      return TRANSPARENT;
    }
    return MaterialColors.getColor(context, android.R.attr.statusBarColor, Color.BLACK);
  }

  private static int getNavigationBarColor(Context context, boolean isEdgeToEdgeEnabled) {
    // Light navigation bars are only supported on O_MR1+. So we need to use a translucent black
    // navigation bar instead to ensure the text/icon contrast of it.
    if (isEdgeToEdgeEnabled && VERSION.SDK_INT < VERSION_CODES.O_MR1) {
      int opaqueNavBarColor =
          MaterialColors.getColor(context, android.R.attr.navigationBarColor, Color.BLACK);
      return ColorUtils.setAlphaComponent(opaqueNavBarColor, EDGE_TO_EDGE_BAR_ALPHA);
    }
    if (isEdgeToEdgeEnabled) {
      return TRANSPARENT;
    }
    return MaterialColors.getColor(context, android.R.attr.navigationBarColor, Color.BLACK);
  }

  private static boolean isUsingLightSystemBar(int systemBarColor, boolean isLightBackground) {
    return isColorLight(systemBarColor) || (systemBarColor == TRANSPARENT && isLightBackground);
  }
}
