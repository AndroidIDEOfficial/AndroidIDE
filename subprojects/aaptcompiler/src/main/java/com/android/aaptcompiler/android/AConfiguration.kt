/*
 * Copyright (C) 2019 The Android Open Source Project
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

package com.android.aaptcompiler.android

object AConfiguration {
  /** Orientation: not specified.  */
  const val ACONFIGURATION_ORIENTATION_ANY: Byte = 0x0000
  /**
   * Orientation: const value corresponding to the
   * [port](@dacRoot/guide/topics/resources/providing-resources.html#OrientationQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_ORIENTATION_PORT : Byte = 0x0001
  /**
   * Orientation: const value corresponding to the
   * [land](@dacRoot/guide/topics/resources/providing-resources.html#OrientationQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_ORIENTATION_LAND : Byte = 0x0002

  @Deprecated("Not currently supported or used. ")
  const val ACONFIGURATION_ORIENTATION_SQUARE : Byte = 0x0003
  /** Touchscreen: not specified.  */
  const val ACONFIGURATION_TOUCHSCREEN_ANY : Byte = 0x0000
  /**
   * Touchscreen: const value corresponding to the
   * [notouch](@dacRoot/guide/topics/resources/providing-resources.html#TouchscreenQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_TOUCHSCREEN_NOTOUCH : Byte = 0x0001

  @Deprecated("Not currently supported or used. ")
  const val ACONFIGURATION_TOUCHSCREEN_STYLUS : Byte = 0x0002
  /**
   * Touchscreen: const value corresponding to the
   * [finger](@dacRoot/guide/topics/resources/providing-resources.html#TouchscreenQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_TOUCHSCREEN_FINGER : Byte = 0x0003
  /** Density: default density.  */
  const val ACONFIGURATION_DENSITY_DEFAULT : Byte = 0
  /**
   * Density: const value corresponding to the
   * [ldpi](@dacRoot/guide/topics/resources/providing-resources.html#DensityQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_DENSITY_LOW : Byte = 120
  /**
   * Density: const value corresponding to the
   * [mdpi](@dacRoot/guide/topics/resources/providing-resources.html#DensityQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_DENSITY_MEDIUM : Int = 160
  /**
   * Density: const value corresponding to the
   * [tvdpi](@dacRoot/guide/topics/resources/providing-resources.html#DensityQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_DENSITY_TV : Int = 213
  /**
   * Density: const value corresponding to the
   * [hdpi](@dacRoot/guide/topics/resources/providing-resources.html#DensityQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_DENSITY_HIGH : Int = 240
  /**
   * Density: const value corresponding to the
   * [xhdpi](@dacRoot/guide/topics/resources/providing-resources.html#DensityQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_DENSITY_XHIGH : Int = 320
  /**
   * Density: const value corresponding to the
   * [xxhdpi](@dacRoot/guide/topics/resources/providing-resources.html#DensityQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_DENSITY_XXHIGH : Int = 480
  /**
   * Density: const value corresponding to the
   * [xxxhdpi](@dacRoot/guide/topics/resources/providing-resources.html#DensityQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_DENSITY_XXXHIGH : Int = 640
  /** Density: any density.  */
  const val ACONFIGURATION_DENSITY_ANY : Int = 0xfffe
  /** Density: no density specified.  */
  const val ACONFIGURATION_DENSITY_NONE : Int = 0xffff
  /** Keyboard: not specified.  */
  const val ACONFIGURATION_KEYBOARD_ANY : Byte = 0x0000
  /**
   * Keyboard: const value corresponding to the
   * [nokeys](@dacRoot/guide/topics/resources/providing-resources.html#ImeQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_KEYBOARD_NOKEYS : Byte = 0x0001
  /**
   * Keyboard: const value corresponding to the
   * [qwerty](@dacRoot/guide/topics/resources/providing-resources.html#ImeQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_KEYBOARD_QWERTY : Byte = 0x0002
  /**
   * Keyboard: const value corresponding to the
   * [12key](@dacRoot/guide/topics/resources/providing-resources.html#ImeQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_KEYBOARD_12KEY : Byte = 0x0003
  /** Navigation: not specified.  */
  const val ACONFIGURATION_NAVIGATION_ANY : Byte = 0x0000
  /**
   * Navigation: const value corresponding to the
   * [nonav](@@dacRoot/guide/topics/resources/providing-resources.html#NavigationQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_NAVIGATION_NONAV : Byte = 0x0001
  /**
   * Navigation: const value corresponding to the
   * [dpad](@dacRoot/guide/topics/resources/providing-resources.html#NavigationQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_NAVIGATION_DPAD : Byte = 0x0002
  /**
   * Navigation: const value corresponding to the
   * [trackball](@dacRoot/guide/topics/resources/providing-resources.html#NavigationQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_NAVIGATION_TRACKBALL : Byte = 0x0003
  /**
   * Navigation: const value corresponding to the
   * [wheel](@dacRoot/guide/topics/resources/providing-resources.html#NavigationQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_NAVIGATION_WHEEL : Byte = 0x0004
  /** Keyboard availability: not specified.  */
  const val ACONFIGURATION_KEYSHIDDEN_ANY : Byte = 0x0000
  /**
   * Keyboard availability: const value corresponding to the
   * [keysexposed](@dacRoot/guide/topics/resources/providing-resources.html#KeyboardAvailQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_KEYSHIDDEN_NO : Byte = 0x0001
  /**
   * Keyboard availability: const value corresponding to the
   * [keyshidden](@dacRoot/guide/topics/resources/providing-resources.html#KeyboardAvailQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_KEYSHIDDEN_YES : Byte = 0x0002
  /**
   * Keyboard availability: const value corresponding to the
   * [keyssoft](@dacRoot/guide/topics/resources/providing-resources.html#KeyboardAvailQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_KEYSHIDDEN_SOFT : Byte = 0x0003
  /** Navigation availability: not specified.  */
  const val ACONFIGURATION_NAVHIDDEN_ANY : Byte = 0x0000
  /**
   * Navigation availability: const value corresponding to the
   * [navexposed](@dacRoot/guide/topics/resources/providing-resources.html#NavAvailQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_NAVHIDDEN_NO : Byte = 0x0001
  /**
   * Navigation availability: const value corresponding to the
   * [navhidden](@dacRoot/guide/topics/resources/providing-resources.html#NavAvailQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_NAVHIDDEN_YES : Byte = 0x0002
  /** Grammatical gender: not specified.  */
  const val ACONFIGURATION_GRAMMATICAL_GENDER_ANY : Byte = 0x00
  /**
   * Grammatical gender: const value corresponding to the
   * [neuter](@dacRoot/guide/topics/resources/providing-resources.html#GrammaticalInflectionQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_GRAMMATICAL_GENDER_NEUTER : Byte = 0x01
  /**
   * Grammatical gender: const value corresponding to the
   * [feminine](@dacRoot/guide/topics/resources/providing-resources.html#GrammaticalInflectionQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_GRAMMATICAL_GENDER_FEMININE : Byte = 0x02
  /**
   * Grammatical gender: const value corresponding to the
   * [masculine](@dacRoot/guide/topics/resources/providing-resources.html#GrammaticalInflectionQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_GRAMMATICAL_GENDER_MASCULINE : Byte = 0x03
  /** Screen size: not specified.  */
  const val ACONFIGURATION_SCREENSIZE_ANY : Byte = 0x00
  /**
   * Screen size: const value indicating the screen is at least
   * approximately 320x426 dp units, corresponding to the
   * [small](@dacRoot/guide/topics/resources/providing-resources.html#ScreenSizeQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_SCREENSIZE_SMALL : Byte = 0x01
  /**
   * Screen size: const value indicating the screen is at least
   * approximately 320x470 dp units, corresponding to the
   * [normal](@dacRoot/guide/topics/resources/providing-resources.html#ScreenSizeQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_SCREENSIZE_NORMAL : Byte = 0x02
  /**
   * Screen size: const value indicating the screen is at least
   * approximately 480x640 dp units, corresponding to the
   * [large](@dacRoot/guide/topics/resources/providing-resources.html#ScreenSizeQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_SCREENSIZE_LARGE : Byte = 0x03
  /**
   * Screen size: const value indicating the screen is at least
   * approximately 720x960 dp units, corresponding to the
   * [xlarge](@dacRoot/guide/topics/resources/providing-resources.html#ScreenSizeQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_SCREENSIZE_XLARGE : Byte = 0x04
  /** Screen layout: not specified.  */
  const val ACONFIGURATION_SCREENLONG_ANY : Byte = 0x00
  /**
   * Screen layout: const value that corresponds to the
   * [notlong](@dacRoot/guide/topics/resources/providing-resources.html#ScreenAspectQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_SCREENLONG_NO : Byte = 0x1
  /**
   * Screen layout: const value that corresponds to the
   * [long](@dacRoot/guide/topics/resources/providing-resources.html#ScreenAspectQualifier)
   * resource qualifier.
   */
  const val ACONFIGURATION_SCREENLONG_YES : Byte = 0x2
  const val ACONFIGURATION_SCREENROUND_ANY : Byte = 0x00
  const val ACONFIGURATION_SCREENROUND_NO : Byte = 0x1
  const val ACONFIGURATION_SCREENROUND_YES : Byte = 0x2

  /** Wide color gamut: not specified.  */
  const val ACONFIGURATION_WIDE_COLOR_GAMUT_ANY : Byte = 0x00
  /**
   * Wide color gamut: const value that corresponds to
   * [no
   * nowidecg](@dacRoot/guide/topics/resources/providing-resources.html#WideColorGamutQualifier)
   * resource qualifier specified.
   */
  const val ACONFIGURATION_WIDE_COLOR_GAMUT_NO : Byte = 0x1
  /**
   * Wide color gamut: const value that corresponds to
   * [
   * widecg](@dacRoot/guide/topics/resources/providing-resources.html#WideColorGamutQualifier)
   * resource qualifier specified.
   */
  const val ACONFIGURATION_WIDE_COLOR_GAMUT_YES : Byte = 0x2

  /** HDR: not specified.  */
  const val ACONFIGURATION_HDR_ANY : Byte = 0x00
  /**
   * HDR: const value that corresponds to
   * [
   * lowdr](@dacRoot/guide/topics/resources/providing-resources.html#HDRQualifier) resource
   * qualifier specified.
   */
  const val ACONFIGURATION_HDR_NO : Byte = 0x1
  /**
   * HDR: const value that corresponds to
   * [
   * highdr](@dacRoot/guide/topics/resources/providing-resources.html#HDRQualifier) resource
   * qualifier specified.
   */
  const val ACONFIGURATION_HDR_YES : Byte = 0x2

  /** UI mode: not specified.  */
  const val ACONFIGURATION_UI_MODE_TYPE_ANY : Byte = 0x00
  /**
   * UI mode: const value that corresponds to
   * [no
   * UI mode type](@dacRoot/guide/topics/resources/providing-resources.html#UiModeQualifier)
   * resource qualifier specified.
   */
  const val ACONFIGURATION_UI_MODE_TYPE_NORMAL : Byte = 0x01
  /**
   * UI mode: const value that corresponds to
   * [desk](@dacRoot/guide/topics/resources/providing-resources.html#UiModeQualifier) resource
   * qualifier specified.
   */
  const val ACONFIGURATION_UI_MODE_TYPE_DESK : Byte = 0x02
  /**
   * UI mode: const value that corresponds to
   * [car](@dacRoot/guide/topics/resources/providing-resources.html#UiModeQualifier) resource
   * qualifier specified.
   */
  const val ACONFIGURATION_UI_MODE_TYPE_CAR : Byte = 0x03
  /**
   * UI mode: const value that corresponds to
   * [television](@dacRoot/guide/topics/resources/providing-resources.html#UiModeQualifier) resource
   * qualifier specified.
   */
  const val ACONFIGURATION_UI_MODE_TYPE_TELEVISION : Byte = 0x04
  /**
   * UI mode: const value that corresponds to
   * [appliance](@dacRoot/guide/topics/resources/providing-resources.html#UiModeQualifier) resource
   * qualifier specified.
   */
  const val ACONFIGURATION_UI_MODE_TYPE_APPLIANCE : Byte = 0x05
  /**
   * UI mode: const value that corresponds to
   * [watch](@dacRoot/guide/topics/resources/providing-resources.html#UiModeQualifier) resource
   * qualifier specified.
   */
  const val ACONFIGURATION_UI_MODE_TYPE_WATCH : Byte = 0x06
  /**
   * UI mode: const value that corresponds to
   * [vr](@dacRoot/guide/topics/resources/providing-resources.html#UiModeQualifier) resource
   * qualifier specified.
   */
  const val ACONFIGURATION_UI_MODE_TYPE_VR_HEADSET : Byte = 0x07
  /** UI night mode: not specified. */
  const val ACONFIGURATION_UI_MODE_NIGHT_ANY : Byte = 0x00
  /**
   * UI night mode: const value that corresponds to
   * [notnight](@dacRoot/guide/topics/resources/providing-resources.html#NightQualifier) resource
   * qualifier specified.
   */
  const val ACONFIGURATION_UI_MODE_NIGHT_NO : Byte = 0x1
  /**
   * UI night mode: const value that corresponds to
   * [night](@dacRoot/guide/topics/resources/providing-resources.html#NightQualifier) resource
   * qualifier specified.
   */
  const val ACONFIGURATION_UI_MODE_NIGHT_YES : Byte = 0x2
  /** Screen width DPI: not specified.  */
  const val ACONFIGURATION_SCREEN_WIDTH_DP_ANY : Byte = 0x0000
  /** Screen height DPI: not specified.  */
  const val ACONFIGURATION_SCREEN_HEIGHT_DP_ANY : Byte = 0x0000
  /** Smallest screen width DPI: not specified. */
  const val ACONFIGURATION_SMALLEST_SCREEN_WIDTH_DP_ANY : Byte = 0x0000
  /** Layout direction: not specified.  */
  const val ACONFIGURATION_LAYOUTDIR_ANY : Byte = 0x00
  /**
   * Layout direction: const value that corresponds to
   * [ldltr](@dacRoot/guide/topics/resources/providing-resources.html#LayoutDirectionQualifier)
   * resource qualifier specified.
   */
  const val ACONFIGURATION_LAYOUTDIR_LTR : Byte = 0x01
  /**
   * Layout direction: const value that corresponds to
   * [ldrtl](@dacRoot/guide/topics/resources/providing-resources.html#LayoutDirectionQualifier)
   * resource qualifier specified.
   */
  const val ACONFIGURATION_LAYOUTDIR_RTL : Byte = 0x02
  /**
   * Bit mask for
   * [mcc](@dacRoot/guide/topics/resources/providing-resources.html#MccQualifier)
   * configuration.
   */
  const val ACONFIGURATION_MCC : Byte = 0x0001
  /**
   * Bit mask for
   * [mnc](@dacRoot/guide/topics/resources/providing-resources.html#MccQualifier)
   * configuration.
   */
  const val ACONFIGURATION_MNC : Byte = 0x0002
  /**
   * Bit mask for
   * [locale]({@docRoot}guide/topics/resources/providing-resources.html#LocaleQualifier)
   * configuration.
   */
  const val ACONFIGURATION_LOCALE : Byte = 0x0004
  /**
   * Bit mask for
   * [touchscreen](@dacRoot/guide/topics/resources/providing-resources.html#TouchscreenQualifier)
   * configuration.
   */
  const val ACONFIGURATION_TOUCHSCREEN : Byte = 0x0008
  /**
   * Bit mask for
   * [keyboard](@dacRoot/guide/topics/resources/providing-resources.html#ImeQualifier)
   * configuration.
   */
  const val ACONFIGURATION_KEYBOARD : Byte = 0x0010
  /**
   * Bit mask for
   * [keyboardHidden](@dacRoot/guide/topics/resources/providing-resources.html#KeyboardAvailQualifier)
   * configuration.
   */
  const val ACONFIGURATION_KEYBOARD_HIDDEN : Byte = 0x0020
  /**
   * Bit mask for
   * [navigation](@dacRoot/guide/topics/resources/providing-resources.html#NavigationQualifier)
   * configuration.
   */
  const val ACONFIGURATION_NAVIGATION : Byte = 0x0040
  /**
   * Bit mask for
   * [orientation](@dacRoot/guide/topics/resources/providing-resources.html#OrientationQualifier)
   * configuration.
   */
  const val ACONFIGURATION_ORIENTATION : Int = 0x0080
  /**
   * Bit mask for
   * [density](@dacRoot/guide/topics/resources/providing-resources.html#DensityQualifier)
   * configuration.
   */
  const val ACONFIGURATION_DENSITY : Int = 0x0100
  /**
   * Bit mask for
   * [screen size](@dacRoot/guide/topics/resources/providing-resources.html#ScreenSizeQualifier)
   * configuration.
   */
  const val ACONFIGURATION_SCREEN_SIZE : Int = 0x0200
  /**
   * Bit mask for
   * [platform version](@dacRoot/guide/topics/resources/providing-resources.html#VersionQualifier)
   * configuration.
   */
  const val ACONFIGURATION_VERSION : Int = 0x0400
  /**
   * Bit mask for screen layout configuration.
   */
  const val ACONFIGURATION_SCREEN_LAYOUT : Int = 0x0800
  /**
   * Bit mask for
   * [ui mode](@dacRoot/guide/topics/resources/providing-resources.html#UiModeQualifier)
   * configuration.
   */
  const val ACONFIGURATION_UI_MODE : Int = 0x1000
  /**
   * Bit mask for
   * [smallest screen width](@dacRoot/guide/topics/resources/providing-resources.html#SmallestScreenWidthQualifier)
   * configuration.
   */
  const val ACONFIGURATION_SMALLEST_SCREEN_SIZE : Int = 0x2000
  /**
   * Bit mask for
   * [layout direction](@dacRoot/guide/topics/resources/providing-resources.html#LayoutDirectionQualifier)
   * configuration.
   */
  const val ACONFIGURATION_LAYOUTDIR : Int = 0x4000
  const val ACONFIGURATION_SCREEN_ROUND : Int = 0x8000
  /**
   * Bit mask for
   * [wide color gamut](@dacRoot/guide/topics/resources/providing-resources.html#WideColorGamutQualifier)
   * and [HDR](@dacRoot/guide/topics/resources/providing-resources.html#HDRQualifier) configurations.
   */
  const val ACONFIGURATION_COLOR_MODE : Int = 0x10000
  /**
   * Constant used to to represent MNC (Mobile Network Code) zero.
   * 0 cannot be used, since it is used to represent an undefined MNC.
   */
  const val ACONFIGURATION_MNC_ZERO : Int = 0xffff
  /**
   * Bit mask for
   * [grammatical gender](@dacRoot/guide/topics/resources/providing-resources.html#GrammaticalInflectionQualifier)
   * configuration.
   */
  const val ACONFIGURATION_GRAMMATICAL_GENDER : Int = 0x20000
}

