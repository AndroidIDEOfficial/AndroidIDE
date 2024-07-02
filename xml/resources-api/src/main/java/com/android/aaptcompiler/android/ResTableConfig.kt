package com.android.aaptcompiler.android

import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_COLOR_MODE
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_DENSITY
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_GRAMMATICAL_GENDER
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_KEYBOARD
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_KEYBOARD_HIDDEN
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_LAYOUTDIR
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_LOCALE
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_MCC
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_MNC
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_NAVIGATION
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_ORIENTATION
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_SCREEN_LAYOUT
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_SCREEN_ROUND
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_SCREEN_SIZE
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_SMALLEST_SCREEN_SIZE
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_TOUCHSCREEN
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_UI_MODE
import com.android.aaptcompiler.android.AConfiguration.ACONFIGURATION_VERSION
import com.android.aaptcompiler.android.LocaleData.localeDataCompareRegions
import com.android.aaptcompiler.android.LocaleData.localeDataComputeScript
import com.android.aaptcompiler.android.LocaleData.localeDataIsCloseToUsEnglish
import com.google.common.base.Preconditions
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.math.min

/**
 * Describes a particular resource configuration.
 *
 * <p>Transliterated from: *
 * https://android.googlesource.com/platform/frameworks/base/+/android-9.0.0_r12/libs/androidfw/ResourceTypes.cpp
 * *
 * https://android.googlesource.com/platform/frameworks/base/+/android-9.0.0_r12/libs/androidfw/include/ResourceTypes.h
 * (struct ResTableConfig)
 *
 *
 */
open class ResTableConfig(
  var size: Int = 0,
  // imsi block
  var mcc: Short = 0,
  var mnc: Short = 0,
  // locale block
  val language: ByteArray = ByteArray(2),
  val country: ByteArray = ByteArray(2),
  // screenType block
  var orientation: Byte = 0,
  var touchscreen: Byte = 0,
  var density: Int = 0,
  // input block
  var keyboard: Byte = 0,
  var navigation: Byte = 0,
  var inputFlags: Byte = 0,
  var grammaticalInflection: Byte = 0,
  // padding: Byte,
  // screenSize block
  var screenWidth: Int = 0,
  var screenHeight: Int = 0,
  // version block
  var sdkVersion: Short = 0,
  var minorVersion: Short = 0,
  // screenConfig block
  var screenLayout: Byte = 0,
  var uiMode: Byte = 0,
  var smallestScreenWidthDp: Int = 0,
  // screenSizeDp block
  var screenWidthDp: Int = 0,
  var screenHeightDp: Int = 0,

  val localeScript: ByteArray = ByteArray(4),
  val localeVariant: ByteArray = ByteArray(8),
  // screenConfig2 block
  var screenLayout2: Byte = 0,
  var colorMode: Byte = 0,
  // padding: Short,

  var localeScriptWasComputed: Boolean = false,
  val localeNumberSystem: ByteArray = ByteArray(8)
): Comparable<ResTableConfig> {

  constructor(
    sizeFromDevice: Int,
    imsi: Int,
    locale: Int,
    screenType: Int,
    input: Int,
    grammaticalInflection: Int,
    screenSize: Int,
    version: Int,
    screenConfig: Int,
    screenSizeDp: Int,
    localeScript: ByteArray,
    localeVariant: ByteArray,
    screenConfig2: Int,
    localeNumberSystem: ByteArray): this(

    sizeFromDevice.deviceToHost(),
    mccFromImsi(imsi),
    mncFromImsi(imsi),
    languageFromLocale(locale),
    countryFromLocale(locale),
    orientationFromScreenType(screenType),
    touchscreenFromScreenType(screenType),
    densityFromScreenType(screenType),
    keyboardFromInput(input),
    navigationFromInput(input),
    inputFlagsFromInput(input),
    grammaticalInflection.deviceToHost().toByte(),
    screenWidthFromScreenSize(screenSize),
    screenHeightFromScreenSize(screenSize),
    sdkVersionFromVersion(version),
    minorVersionFromVersion(version),
    screenLayoutFromScreenConfig(screenConfig),
    uiModeFromScreenConfig(screenConfig),
    smallestScreenWidthDpFromScreenConfig(screenConfig),
    screenWidthDpFromScreenSizeDp(screenSizeDp),
    screenHeightDpFromScreenSizeDp(screenSizeDp),
    localeScript,
    localeVariant,
    screenLayout2FromScreenConfig2(screenConfig2),
    colorModeFromScreenConfig2(screenConfig2),
    false,
    localeNumberSystem)

  constructor(other: ResTableConfig): this(
    other.size,
    other.mcc,
    other.mnc,
    other.language.copyOf(),
    other.country.copyOf(),
    other.orientation,
    other.touchscreen,
    other.density,
    other.keyboard,
    other.navigation,
    other.inputFlags,
    other.grammaticalInflection,
    other.screenWidth,
    other.screenHeight,
    other.sdkVersion,
    other.minorVersion,
    other.screenLayout,
    other.uiMode,
    other.smallestScreenWidthDp,
    other.screenWidthDp,
    other.screenHeightDp,
    other.localeScript.copyOf(),
    other.localeVariant.copyOf(),
    other.screenLayout2,
    other.colorMode,
    other.localeScriptWasComputed,
    other.localeNumberSystem.copyOf())

  init {
    Preconditions.checkState(language.size == 2)
    Preconditions.checkState(country.size == 2)
    Preconditions.checkState(density <= 0xffff)
    Preconditions.checkState(screenWidth <= 0xffff)
    Preconditions.checkState(screenHeight <= 0xffff)
    Preconditions.checkState(smallestScreenWidthDp <= 0xffff)
    Preconditions.checkState(screenWidthDp <= 0xffff)
    Preconditions.checkState(screenHeightDp <= 0xffff)
    Preconditions.checkState(localeScript.size == 4)
    Preconditions.checkState(localeVariant.size == 8)
    Preconditions.checkState(localeNumberSystem.size == 8)
  }

  /** Returns the imsi block in Little Endian (device) format. */
  fun getImsi() = ((mcc.toInt() and 0xffff) or
    (mnc.toInt() shl 16)).hostToDevice()

  /** Returns the locale block in Little Endian (device) format. */
  fun getLocale() = (((language[0].toInt() and 0xff) shl 8) or
    (language[1].toInt() and 0xff) or
    ((country[0].toInt() and 0xff) shl 24) or
    ((country[1].toInt() and 0xff) shl 16)).hostToDevice()

  /** Returns the screenType block in Little Endian (device) format. */
  fun getScreenType() = ((orientation.toInt() and 0xff) or
    ((touchscreen.toInt() and 0xff) shl 8) or
    (density shl 16)).hostToDevice()

  /** Returns the input block in Little Endian (device) format. */
  fun getInput() = ((keyboard.toInt() and 0xff) or
    ((navigation.toInt() and 0xff) shl 8) or
    ((inputFlags.toInt() and 0xff) shl 16) or
    (0x00 shl 24)).hostToDevice() // padding.

  /** Returns the screenSize block in Little Endian (device) format. */
  fun getScreenSize() = ((screenWidth and 0xffff) or
    (screenHeight shl 16)).hostToDevice()

  /** Returns the version block in Little Endian (device) format. */
  fun getVersion() = ((sdkVersion.toInt() and 0xffff) or
    (minorVersion.toInt() shl 16)).hostToDevice()

  /** Returns the screenConfig block in Little Endian (device) format. */
  fun getScreenConfig() = ((screenLayout.toInt() and 0xff) or
    ((uiMode.toInt() and 0xff) shl 8) or
    (smallestScreenWidthDp shl 16)).hostToDevice()

  /** Returns the screenSizeDp block in Little Endian (device) format. */
  fun getScreenSizeDp() = ((screenWidthDp and 0xffff) or
    (screenHeightDp shl 16)).hostToDevice()

  /** Returns the screenConfig2 block in Little Endian (device) format */
  fun getScreenConfig2() = ((screenLayout2.toInt() and 0xff) or
    ((colorMode.toInt() and 0xff) shl 8) or
    (0x0000 shl 16)).hostToDevice() // padding

  fun layoutSize() = (screenLayout.toInt() and SCREEN_LAYOUT.SIZE_MASK).toByte()

  fun layoutLong() = (screenLayout.toInt() and SCREEN_LAYOUT.SCREENLONG_MASK).toByte()

  fun uiModeType() = (uiMode.toInt() and UI_MODE.TYPE_MASK).toByte()

  fun uiModeNight() = (uiMode.toInt() and UI_MODE.NIGHT_MASK).toByte()

  fun layoutRound() = (screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK).toByte()

  fun wideColorGamut() = (colorMode.toInt() and COLOR_MODE.WIDE_GAMUT_MASK).toByte()

  fun hdr() = (colorMode.toInt() and COLOR_MODE.HDR_MASK).toByte()

  override fun toString(): String {
    val result = StringBuilder()

    if (mcc.isTruthy()) {
      result.append("mcc$mcc-")
    }
    if (mnc.isTruthy()) {
      result.append("mnc$mnc-")
    }

    appendDirLocale(result)

    if (grammaticalInflection != GRAMMATICAL_GENDER.ANY) {
      result.append(
        when (grammaticalInflection) {
          GRAMMATICAL_GENDER.NEUTER -> "neuter"
          GRAMMATICAL_GENDER.FEMININE -> "feminine"
          GRAMMATICAL_GENDER.MASCULINE -> "masculine"
          else -> "grammaticalInflection=$grammaticalInflection"
        })
      result.append("-")
    }

    val layoutDir = (screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK).toByte()
    if (layoutDir != SCREEN_LAYOUT.DIR_ANY) {
      result.append(
        when (layoutDir) {
          SCREEN_LAYOUT.DIR_LTR -> "ldltr"
          SCREEN_LAYOUT.DIR_RTL -> "ldrtl"
          else -> "layoutDir=$layoutDir"
        })
      result.append("-")
    }

    if (smallestScreenWidthDp.isTruthy()) {
      result.append("sw${smallestScreenWidthDp}dp-")
    }

    if (screenWidthDp.isTruthy()) {
      result.append("w${screenWidthDp}dp-")
    }

    if (screenHeightDp.isTruthy()) {
      result.append("h${screenHeightDp}dp-")
    }

    val screenSizeFlag = (screenLayout.toInt() and SCREEN_LAYOUT.SIZE_MASK).toByte()
    if (screenSizeFlag != SCREEN_LAYOUT.SIZE_ANY) {
      result.append(
        when (screenSizeFlag) {
          SCREEN_LAYOUT.SIZE_SMALL -> "small"
          SCREEN_LAYOUT.SIZE_NORMAL -> "normal"
          SCREEN_LAYOUT.SIZE_LARGE -> "large"
          SCREEN_LAYOUT.SIZE_XLARGE -> "xlarge"
          else -> "screenLayoutSize=$screenSizeFlag"
        })
      result.append("-")
    }

    val screenLong = (screenLayout.toInt() and SCREEN_LAYOUT.SCREENLONG_MASK).toByte()
    if (screenLong != SCREEN_LAYOUT.SCREENLONG_ANY) {
      result.append(
        when (screenLong) {
          SCREEN_LAYOUT.SCREENLONG_YES -> "long"
          SCREEN_LAYOUT.SCREENLONG_NO -> "notlong"
          else -> "screenLayoutLong=$screenLong"
        })
      result.append("-")
    }

    val screenRound = (screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK).toByte()
    if (screenRound != SCREEN_LAYOUT2.SCREENROUND_ANY) {
      result.append(
        when (screenRound) {
          SCREEN_LAYOUT2.SCREENROUND_YES -> "round"
          SCREEN_LAYOUT2.SCREENROUND_NO -> "notround"
          else -> "screenRound=$screenRound"
        })
      result.append("-")
    }

    val wideGamut = (colorMode.toInt() and COLOR_MODE.WIDE_GAMUT_MASK).toByte()
    if (wideGamut != COLOR_MODE.WIDE_GAMUT_ANY) {
      result.append(
        when (wideGamut) {
          COLOR_MODE.WIDE_GAMUT_YES -> "widecg"
          COLOR_MODE.WIDE_GAMUT_NO -> "nowidecg"
          else -> "wideColorGamut=$wideGamut"
        })
      result.append("-")
    }

    val hdr = (colorMode.toInt() and COLOR_MODE.HDR_MASK).toByte()
    if (hdr != COLOR_MODE.HDR_ANY) {
      result.append(
        when (hdr) {
          COLOR_MODE.HDR_NO -> "lowdr"
          COLOR_MODE.HDR_YES -> "highdr"
          else -> "hdr=$hdr"
        })
      result.append("-")
    }

    if (orientation != ORIENTATION.ANY) {
      result.append(
        when (orientation) {
          ORIENTATION.PORT -> "port"
          ORIENTATION.LAND -> "land"
          ORIENTATION.SQUARE -> "square"
          else -> "orientation=$orientation"
        })
      result.append("-")
    }

    val uiModeType = (uiMode.toInt() and UI_MODE.TYPE_MASK).toByte()
    if (uiModeType != UI_MODE.TYPE_ANY) {
      result.append(
        when (uiModeType) {
          UI_MODE.TYPE_DESK -> "desk"
          UI_MODE.TYPE_CAR -> "car"
          UI_MODE.TYPE_TELEVISION -> "television"
          UI_MODE.TYPE_APPLIANCE -> "appliance"
          UI_MODE.TYPE_WATCH -> "watch"
          UI_MODE.TYPE_VR_HEADSET -> "vrheadset"
          else -> "uiModeType=$uiModeType"
        })
      result.append("-")
    }

    val nightMode = (uiMode.toInt() and UI_MODE.NIGHT_MASK).toByte()
    if (nightMode != UI_MODE.NIGHT_ANY) {
      result.append(
        when (nightMode) {
          UI_MODE.NIGHT_YES -> "night"
          UI_MODE.NIGHT_NO -> "notnight"
          else -> "uiModeNight=$nightMode"
        })
      result.append("-")
    }

    if (density != DENSITY.DEFAULT) {
      result.append(
        when (density) {
          DENSITY.LOW -> "ldpi"
          DENSITY.MEDIUM -> "mdpi"
          DENSITY.TV -> "tvdpi"
          DENSITY.HIGH -> "hdpi"
          DENSITY.XHIGH -> "xhdpi"
          DENSITY.XXHIGH -> "xxhdpi"
          DENSITY.XXXHIGH -> "xxxhdpi"
          DENSITY.NONE -> "nodpi"
          DENSITY.ANY -> "anydpi"
          else -> "${density}dpi"
        })
      result.append("-")
    }

    if (touchscreen != TOUCHSCREEN.ANY) {
      result.append(
        when (touchscreen) {
          TOUCHSCREEN.NOTOUCH -> "notouch"
          TOUCHSCREEN.FINGER -> "finger"
          TOUCHSCREEN.STYLUS -> "stylus"
          else -> "touchscreen=$touchscreen"
        })
      result.append("-")
    }

    val keysHidden = (inputFlags.toInt() and INPUT_FLAGS.KEYSHIDDEN_MASK).toByte()
    if (keysHidden.isTruthy()) {
      val keysString = when (keysHidden) {
        INPUT_FLAGS.KEYSHIDDEN_NO -> "keysexposed"
        INPUT_FLAGS.KEYSHIDDEN_YES -> "keyshidden"
        INPUT_FLAGS.KEYSHIDDEN_SOFT -> "keyssoft"
        else -> ""
      }

      if (keysString.isNotEmpty()) {
        result.append("$keysString-")
      }
    }

    if (keyboard != KEYBOARD.ANY) {
      result.append (
        when (keyboard) {
          KEYBOARD.NOKEYS -> "nokeys"
          KEYBOARD.QWERTY -> "qwerty"
          KEYBOARD.TWELVEKEY -> "12key"
          else -> "keyboard=$keyboard"
        })
      result.append("-")
    }


    val navhidden = (inputFlags.toInt() and INPUT_FLAGS.NAVHIDDEN_MASK).toByte()
    if (navhidden.isTruthy()) {
      result.append(
        when (navhidden) {
          INPUT_FLAGS.NAVHIDDEN_NO -> "navexposed"
          INPUT_FLAGS.NAVHIDDEN_YES -> "navhidden"
          else -> "inputFlagsNavHidden=$navhidden"
        })
      result.append("-")
    }

    if (navigation != NAVIGATION.ANY) {
      result.append(
        when (navigation) {
          NAVIGATION.NONAV -> "nonav"
          NAVIGATION.DPAD -> "dpad"
          NAVIGATION.TRACKBALL -> "trackball"
          NAVIGATION.WHEEL -> "wheel"
          else -> "navigation=$navigation"
        })
      result.append("-")
    }

    if (getScreenSize().isTruthy()) {
      result.append("${screenWidth}x$screenHeight-")
    }

    if (getVersion().isTruthy()) {
      result.append("v$sdkVersion")
      if (minorVersion.isTruthy()){
        result.append(".$minorVersion")
      }
      result.append("-")
    }

    // No specified values. So we are in default.
    if (result.isEmpty()) return "DEFAULT"

    // Trim off remaining '-'
    return result.substring(0, result.length - 1)
  }

  private fun appendDirLocale(result: StringBuilder) {
    if (!language[0].isTruthy()) {
      return
    }

    val scriptProvided = localeScript[0].isTruthy() && !localeScriptWasComputed
    if (!scriptProvided && !localeVariant[0].isTruthy() && !localeNumberSystem[0].isTruthy()) {
      // Legacy format.
      result.append("${unpackLanguage()}-")
      if (country[0].isTruthy()) {
        result.append("r${unpackRegion()}-")
      }
      return
    }

    // We are writing the modified BCP 47 tag.
    // It starts with 'b+' and uses '+' as a separator.
    result.append("b+${unpackLanguage()}")

    if (scriptProvided) {
      result.append("+${String(localeScript)}")
    }

    if (country[0].isTruthy()) {
      result.append("+${unpackRegion()}")
    }

    if (localeVariant[0].isTruthy()) {
      val variantTerminator = localeVariant.indexOf(0)
      val variantString = if (variantTerminator == -1) {
        String(localeVariant)
      } else {
        String(localeVariant, 0, variantTerminator)
      }
      result.append("+$variantString")
    }

    if (localeNumberSystem[0].isTruthy()) {
      val numberSystemTerminator = localeNumberSystem.indexOf(0)
      val numberSystemString = if (numberSystemTerminator == -1) {
        String(localeNumberSystem)
      } else {
        String(localeNumberSystem, 0, numberSystemTerminator)
      }
      result.append("+u+nu+$numberSystemString")
    }

    result.append("-")
  }

  fun unpackLanguage() = unpackLanguageOrRegion(language, 'a'.code.toByte())

  fun unpackRegion() = unpackLanguageOrRegion(country, '0'.code.toByte())

  fun packLanguage(value: String) {
    packLanguageOrRegion(value, 'a'.code.toByte()).copyInto(language, 0, 0, 2)
  }

  fun packRegion(value: String) {
    packLanguageOrRegion(value, '0'.code.toByte()).copyInto(country, 0, 0, 2)
  }

  /**
   * Compare two configuration, returning CONFIG_* flags set for each value that is different.
   */
  fun diff(other: ResTableConfig): Int {
    var result = 0
    if (mcc != other.mcc) {
      result = result or CONFIG_MCC
    }
    if (mnc != other.mnc) {
      result = result or CONFIG_MNC
    }
    if (orientation != other.orientation) {
      result = result or CONFIG_ORIENTATION
    }
    if (density != other.density) {
      result = result or CONFIG_DENSITY
    }
    if (touchscreen != other.touchscreen) {
      result = result or CONFIG_TOUCHSCREEN
    }
    if (inputFlags.toInt() and (INPUT_FLAGS.KEYSHIDDEN_MASK or INPUT_FLAGS.NAVHIDDEN_MASK) !=
      other.inputFlags.toInt() and (INPUT_FLAGS.KEYSHIDDEN_MASK or INPUT_FLAGS.NAVHIDDEN_MASK)) {
      result = result or CONFIG_KEYBOARD_HIDDEN
    }
    if (keyboard != other.keyboard) {
      result = result or CONFIG_KEYBOARD
    }
    if (navigation != other.navigation) {
      result = result or CONFIG_NAVIGATION
    }
    if (getScreenSize() != other.getScreenSize()) {
      result = result or CONFIG_SCREEN_SIZE
    }
    if (getVersion() != other.getVersion()) {
      result = result or CONFIG_VERSION
    }
    if (screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK !=
      other.screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK) {
      result = result or CONFIG_LAYOUTDIR
    }
    if (screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK.inv() !=
      other.screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK.inv()) {
      result = result or CONFIG_SCREEN_LAYOUT
    }
    if (screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK !=
      other.screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK) {
      result = result or CONFIG_SCREEN_ROUND
    }
    if (colorMode.toInt() and (COLOR_MODE.WIDE_GAMUT_MASK or COLOR_MODE.HDR_MASK) !=
      other.colorMode.toInt() and (COLOR_MODE.WIDE_GAMUT_MASK or COLOR_MODE.HDR_MASK)) {
      result = result or CONFIG_COLOR_MODE
    }
    if (uiMode != other.uiMode) {
      result = result or CONFIG_UI_MODE
    }
    if (smallestScreenWidthDp != other.smallestScreenWidthDp) {
      result = result or CONFIG_SMALLEST_SCREEN_SIZE
    }
    if (getScreenSizeDp() != other.getScreenSizeDp()) {
      result = result or CONFIG_SCREEN_SIZE
    }
    if (compareLocales(other) != 0) {
      result = result or CONFIG_LOCALE
    }
    if (grammaticalInflection != other.grammaticalInflection) {
      result = result or CONFIG_GRAMMATICAL_GENDER
    }
    return result
  }

  override fun compareTo(other: ResTableConfig): Int {
    val imsi = getImsi().deviceToHost()
    val oImsi = other.getImsi().deviceToHost()
    when {
      imsi > oImsi -> return 1
      oImsi > imsi -> return -1
    }

    val localeDiff = compareLocales(other)
    if (localeDiff != 0) {
      return localeDiff
    }

    when {
      grammaticalInflection > other.grammaticalInflection -> return 1
      grammaticalInflection < other.grammaticalInflection -> return -1
    }

    val screenType = getScreenType().deviceToHost()
    val oScreenType = other.getScreenType().deviceToHost()
    when {
      screenType > oScreenType -> return 1
      oScreenType > screenType -> return -1
    }

    val input = getInput().deviceToHost()
    val oInput = other.getInput().deviceToHost()
    when {
      input > oInput -> return 1
      oInput > input -> return -1
    }

    val screenSize = getScreenSize().deviceToHost()
    val oScreenSize = other.getScreenSize().deviceToHost()
    when {
      screenSize > oScreenSize -> return 1
      oScreenSize > screenSize -> return -1
    }

    val version = getVersion().deviceToHost()
    val oVersion = other.getVersion().deviceToHost()
    when {
      version > oVersion -> return 1
      oVersion > version -> return -1
    }

    when {
      screenLayout > other.screenLayout -> return 1
      other.screenLayout > screenLayout -> return -1
    }

    when {
      screenLayout2 > other.screenLayout2 -> return 1
      other.screenLayout2 > screenLayout2 -> return -1
    }

    when {
      colorMode > other.colorMode -> return 1
      other.colorMode > colorMode -> return -1
    }

    when {
      uiMode > other.uiMode -> return 1
      other.uiMode > uiMode -> return -1
    }

    when {
      smallestScreenWidthDp > other.smallestScreenWidthDp -> return 1
      other.smallestScreenWidthDp > smallestScreenWidthDp -> return -1
    }

    val screenSizeDp = getScreenSizeDp().deviceToHost()
    val oScreenSizeDp = other.getScreenSizeDp().deviceToHost()
    when {
      screenSizeDp > oScreenSizeDp -> return 1
      oScreenSizeDp > screenSizeDp -> return -1
    }

    return 0
  }

  fun compareLocales(other: ResTableConfig): Int {
    val locale = getLocale().deviceToHost()
    val oLocale = other.getLocale().deviceToHost()
    // NOTE: This is the old behaviour with respect to comparison orders. The diff value here
    // doesn't make much sense (given our bit packing scheme) but it's stable, and that's all we
    // need.
    when {
      locale > oLocale -> return 1
      oLocale > locale -> return -1
    }

    // The language & region are equal, so compare the scripts, variants and numbering systems in
    // that order. Comparison of variants and numbering systems should happen very infrequently (if
    // at all.)
    val emptyScript = ByteArray(4)
    val script = if (localeScriptWasComputed) emptyScript else localeScript
    val oScript = if (other.localeScriptWasComputed) emptyScript else other.localeScript
    val scriptDiff = compareArrays(script, oScript)
    if (scriptDiff != 0) {
      return scriptDiff
    }

    val variantDiff = compareArrays(localeVariant, other.localeVariant)
    if (variantDiff != 0) {
      return variantDiff
    }

    return compareArrays(localeNumberSystem, other.localeNumberSystem)
  }

  /**
   * Return true if 'this' is more specific than, i.e. has more specified values than 'o'.
   */
  fun isMoreSpecificThan(other: ResTableConfig): Boolean {
    // The order of the following tests defines the importance of one
    // configuration parameter over another.  Those tests first are more
    // important, trumping any values in those following them.
    if (getImsi().isTruthy() || other.getImsi().isTruthy()) {
      when {
        mcc != other.mcc -> return mcc.isTruthy()
        mnc != other.mnc -> return mnc.isTruthy()
      }
    }

    val localeDiff = isLocaleMoreSpecificThan(other)
    when {
      localeDiff < 0 -> return false
      localeDiff > 0 -> return true
    }

    if (grammaticalInflection.isTruthy() || other.grammaticalInflection.isTruthy()) {
      if (grammaticalInflection != other.grammaticalInflection) return grammaticalInflection.isTruthy()
    }

    if (screenLayout.isTruthy() || other.screenLayout.isTruthy()) {
      if (screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK !=
        other.screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK) {
        return (screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK).isTruthy()
      }
    }

    if (smallestScreenWidthDp.isTruthy() || other.smallestScreenWidthDp.isTruthy()) {
      if (smallestScreenWidthDp != other.smallestScreenWidthDp) {
        return smallestScreenWidthDp.isTruthy()
      }
    }

    if (getScreenSizeDp().isTruthy() || other.getScreenSizeDp().isTruthy()) {
      if (screenWidthDp != other.screenWidthDp) {
        return screenWidthDp.isTruthy()
      }
      if (screenHeightDp != other.screenHeightDp) {
        return screenHeightDp.isTruthy()
      }
    }

    if (screenLayout.isTruthy() || other.screenLayout.isTruthy()) {
      if (screenLayout.toInt() and SCREEN_LAYOUT.SIZE_MASK !=
        other.screenLayout.toInt() and SCREEN_LAYOUT.SIZE_MASK) {
        return (screenLayout.toInt() and SCREEN_LAYOUT.SIZE_MASK).isTruthy()
      }

      if (screenLayout.toInt() and SCREEN_LAYOUT.SCREENLONG_MASK !=
        other.screenLayout.toInt() and SCREEN_LAYOUT.SCREENLONG_MASK) {
        return (screenLayout.toInt() and SCREEN_LAYOUT.SCREENLONG_MASK).isTruthy()
      }
    }

    if (screenLayout2.isTruthy() || other.screenLayout2.isTruthy()) {
      if (screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK !=
        other.screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK) {
        return (screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK).isTruthy()
      }
    }

    if (colorMode.isTruthy() || other.colorMode.isTruthy()) {
      if (colorMode.toInt() and COLOR_MODE.HDR_MASK !=
        other.colorMode.toInt() and COLOR_MODE.HDR_MASK) {
        return (colorMode.toInt() and COLOR_MODE.HDR_MASK).isTruthy()
      }
      if (colorMode.toInt() and COLOR_MODE.WIDE_GAMUT_MASK !=
        other.colorMode.toInt() and COLOR_MODE.WIDE_GAMUT_MASK) {
        return (colorMode.toInt() and COLOR_MODE.WIDE_GAMUT_MASK).isTruthy()
      }
    }

    if (orientation != other.orientation) {
      return orientation.isTruthy()
    }

    if (uiMode.isTruthy() || other.uiMode.isTruthy()) {
      if (uiMode.toInt() and UI_MODE.TYPE_MASK !=
        other.uiMode.toInt() and UI_MODE.TYPE_MASK) {
        return (uiMode.toInt() and UI_MODE.TYPE_MASK).isTruthy()
      }
      if (uiMode.toInt() and UI_MODE.NIGHT_MASK !=
        other.uiMode.toInt() and UI_MODE.NIGHT_MASK) {
        return (uiMode.toInt() and UI_MODE.NIGHT_MASK).isTruthy()
      }
    }

    // density is never 'more specific'
    // as the default just equals 160

    if (touchscreen != other.touchscreen) {
      return touchscreen.isTruthy()
    }

    if (getInput().isTruthy() || other.getInput().isTruthy()) {
      if (inputFlags.toInt() and INPUT_FLAGS.KEYSHIDDEN_MASK !=
        other.inputFlags.toInt() and INPUT_FLAGS.KEYSHIDDEN_MASK) {
        return (inputFlags.toInt() and INPUT_FLAGS.KEYSHIDDEN_MASK).isTruthy()
      }

      if (inputFlags.toInt() and INPUT_FLAGS.NAVHIDDEN_MASK !=
        other.inputFlags.toInt() and INPUT_FLAGS.NAVHIDDEN_MASK) {
        return (inputFlags.toInt() and INPUT_FLAGS.NAVHIDDEN_MASK).isTruthy()
      }

      if (keyboard != other.keyboard) {
        return keyboard.isTruthy()
      }

      if (navigation != other.navigation) {
        return navigation.isTruthy()
      }
    }

    if (getScreenSize().isTruthy() || other.getScreenSize().isTruthy()) {
      if (screenWidth != other.screenWidth) {
        return screenWidth.isTruthy()
      }

      if (screenHeight != other.screenHeight) {
        return screenHeight.isTruthy()
      }
    }

    if (getVersion().isTruthy() || other.getVersion().isTruthy()) {
      if (sdkVersion != other.sdkVersion) {
        return sdkVersion.isTruthy()
      }

      if (minorVersion != other.minorVersion) {
        return minorVersion.isTruthy()
      }
    }

    return false
  }

  /**
   * Returns a positive integer if this config is more specific than |o| with respect to their
   * locales, a negative integer if |o| is more specific and 0 if they're equally specific.
   */
  fun isLocaleMoreSpecificThan(other: ResTableConfig): Int {
    if (getLocale().isTruthy() || other.getLocale().isTruthy()) {
      when {
        language[0] != other.language[0] -> when {
          !language[0].isTruthy() -> return -1
          !other.language[0].isTruthy()-> return 1
        }
        country[0] != other.country[0] -> when {
          !country[0].isTruthy() -> return -1
          !other.country[0].isTruthy()-> return 1
        }
      }
    }
    return getImportanceScoreOfLocale() - other.getImportanceScoreOfLocale()
  }

  /** Returns an integer representing the importance score of the configuration locale */
  fun getImportanceScoreOfLocale() =
    // There isn't a well specified "importance" order between variants and
    // scripts. We can't easily tell whether, say "en-Latn-US" is more or less
    // specific than "en-US-POSIX".
    //
    // We therefore arbitrarily decide to give priority to variants over
    // scripts since it seems more useful to do so. We will consider
    // "en-US-POSIX" to be more specific than "en-Latn-US".
    //
    // Unicode extension keywords are considered to be less important than
    // scripts and variants.
    (if (localeVariant[0] != 0.toByte()) 4 else 0) +
      (if (localeScript[0] != 0.toByte() && !localeScriptWasComputed) 2 else 0) +
      if (localeNumberSystem[0] != 0.toByte()) 1 else 0

  /**
   * Return true if 'this' is a better match than 'other' for the 'requested'
   * configuration.
   * <p> This assumes that match() has already been used to
   * remove any configurations that don't match the requested configuration
   * at all; if they are not first filtered, non-matching results can be
   * considered better than matching ones.
   * <p> The general rule per attribute: if the request cares about an attribute
   * (it normally does), if the two ('this' and 'other') are equal it's a tie.  If
   * they are not equal then one must be generic because only generic and
   * '==requested' will pass the match() call.  So if this is not generic,
   * it wins.  If this IS generic, 'other' wins (return false).
   */
  fun isBetterThan(other: ResTableConfig, requested: ResTableConfig?): Boolean {
    requested ?: return isMoreSpecificThan(other)

    if (getImsi().isTruthy() || other.getImsi().isTruthy()) {
      if ((mcc != other.mcc) && requested.mcc.isTruthy()) {
        return mcc.isTruthy()
      }
      if ((mnc != other.mnc) && requested.mnc.isTruthy()) {
        return mnc.isTruthy()
      }
    }

    if (isLocaleBetterThan(other, requested)) {
      return true
    }

    if (grammaticalInflection != other.grammaticalInflection && requested.grammaticalInflection.isTruthy()) {
      return grammaticalInflection.isTruthy()
    }

    if (screenLayout.isTruthy() || other.screenLayout.isTruthy()) {
      val myLayoutDir = screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK
      val otherLayoutDir = other.screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK
      if (myLayoutDir != otherLayoutDir &&
        (requested.screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK).isTruthy()) {
        return myLayoutDir > otherLayoutDir
      }
    }

    if (smallestScreenWidthDp.isTruthy() || other.smallestScreenWidthDp.isTruthy()) {
      // The configuration closest to the actual size is best. We assume that larger configs have
      // already been filtered out at this point.  That means we just want the largest one.
      if (smallestScreenWidthDp != other.smallestScreenWidthDp) {
        return smallestScreenWidthDp > other.smallestScreenWidthDp
      }
    }

    if (getScreenSizeDp().isTruthy() || other.getScreenSizeDp().isTruthy()) {
      // "Better" is based on the sum of the difference between both width and height from the
      // requested dimensions.  We are assuming the invalid configs (with larger dimens) have
      // already been filtered.  Note that if a particular dimension is unspecified, we will end up
      // with a large value (the difference between 0 and the requested dimension), which is good
      // since we will prefer a config that has specified a dimension value.
      var myDelta = 0
      var otherDelta = 0
      if (requested.screenWidthDp.isTruthy()) {
        myDelta += requested.screenWidthDp - screenWidthDp
        otherDelta += requested.screenWidthDp - other.screenWidthDp
      }
      if (requested.screenHeightDp.isTruthy()) {
        myDelta += requested.screenHeightDp - screenHeightDp
        otherDelta += requested.screenHeightDp - other.screenHeightDp
      }
      if (myDelta != otherDelta) {
        return myDelta < otherDelta
      }
    }

    if (screenLayout.isTruthy() || other.screenLayout.isTruthy()) {
      val mySL = screenLayout.toInt() and SCREEN_LAYOUT.SIZE_MASK
      val otherSL = other.screenLayout.toInt() and SCREEN_LAYOUT.SIZE_MASK
      if (mySL != otherSL &&
        (requested.screenLayout.toInt() and SCREEN_LAYOUT.SIZE_MASK).isTruthy()) {
        val fixedMySL: Int
        val fixedOtherSL: Int
        if ((requested.screenLayout.toInt() and SCREEN_LAYOUT.SIZE_MASK) >
          SCREEN_LAYOUT.SIZE_NORMAL) {

          fixedMySL = if (mySL == 0) SCREEN_LAYOUT.SIZE_NORMAL.toInt() else mySL
          fixedOtherSL = if (otherSL == 0) SCREEN_LAYOUT.SIZE_NORMAL.toInt() else otherSL
        } else {
          fixedMySL = mySL
          fixedOtherSL = otherSL
        }

        // For screen size, the best match is the one that is closest to the requested screen size,
        // but not over (the not over part is dealt with in match() below).
        return if (fixedMySL == fixedOtherSL) {
          // If the two are the same, but 'other' is actually undefined, then 'this' is really a
          // better match.
          mySL != 0
        } else {
          fixedMySL > fixedOtherSL
        }
      }

      val myLong = screenLayout.toInt() and SCREEN_LAYOUT.SCREENLONG_MASK
      val otherLong = other.screenLayout.toInt() and SCREEN_LAYOUT.SCREENLONG_MASK
      if (myLong != otherLong &&
        (requested.screenLayout.toInt() and SCREEN_LAYOUT.SCREENLONG_MASK).isTruthy()) {
        return myLong.isTruthy()
      }
    }

    if (screenLayout2.isTruthy() || other.screenLayout2.isTruthy()) {
      val myRound = screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK
      val otherRound = other.screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK
      if (myRound != otherRound &&
        (requested.screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK).isTruthy()) {
        return myRound.isTruthy()
      }
    }

    if (colorMode.isTruthy() || other.colorMode.isTruthy()) {
      val myGamut = colorMode.toInt() and COLOR_MODE.WIDE_GAMUT_MASK
      val oGamut = other.colorMode.toInt() and COLOR_MODE.WIDE_GAMUT_MASK
      if (myGamut != oGamut &&
        (requested.colorMode.toInt() and COLOR_MODE.WIDE_GAMUT_MASK).isTruthy()) {
        return myGamut.isTruthy()
      }

      val myHdr = colorMode.toInt() and COLOR_MODE.HDR_MASK
      val oHdr = other.colorMode.toInt() and COLOR_MODE.HDR_MASK
      if (myHdr != oHdr && (requested.colorMode.toInt() and COLOR_MODE.HDR_MASK).isTruthy()) {
        return myHdr.isTruthy()
      }
    }

    if (orientation != other.orientation && requested.orientation.isTruthy()) {
      return orientation.isTruthy()
    }

    if (uiMode.isTruthy() || other.uiMode.isTruthy()) {
      val myType = uiMode.toInt() and UI_MODE.TYPE_MASK
      val otherType = other.uiMode.toInt() and UI_MODE.TYPE_MASK
      if (myType != otherType && (requested.uiMode.toInt() and UI_MODE.TYPE_MASK).isTruthy()) {
        return myType.isTruthy()
      }

      val myNight = uiMode.toInt() and UI_MODE.NIGHT_MASK
      val otherNight = other.uiMode.toInt() and UI_MODE.NIGHT_MASK
      if (myNight != otherNight && (requested.uiMode.toInt() and UI_MODE.NIGHT_MASK).isTruthy()) {
        return myNight.isTruthy()
      }
    }

    if (getScreenType().isTruthy() || other.getScreenType().isTruthy()) {
      if (density != other.density) {
        // Use the system default density (DENSITY_MEDIUM, 160dpi) if none specified.
        val myDensity = if (density.isTruthy()) density else DENSITY.MEDIUM
        val otherDensity = if (other.density.isTruthy()) other.density else DENSITY.MEDIUM

        // We always prefer DENSITY_ANY over scaling a density bucket.
        when {
          myDensity == DENSITY.ANY -> return true
          otherDensity == DENSITY.ANY -> return false
        }

        val requestedDensity = when(requested.density) {
          0,
          DENSITY.ANY -> DENSITY.MEDIUM
          else -> requested.density
        }

        // DENSITY_ANY is now dealt with. We should look to pick a density bucket and potentially
        // scale it. Any density is potentially useful because the system will scale it.  Scaling
        // down is generally better than scaling up.
        val high = max(myDensity, otherDensity)
        val low = min(myDensity, otherDensity)
        val iAmBigger = high == myDensity

        return when {
          // requested value higher than both low and high, give high
          requestedDensity >= high -> iAmBigger
          // requested value lower than both low and high, give low
          requestedDensity <= low -> !iAmBigger
          // saying that scaling down is 2x better than scaling up
          (2*low - requestedDensity) * high > requestedDensity * requestedDensity ->
            !iAmBigger
          else ->
            iAmBigger
        }
      }

      if (touchscreen != other.touchscreen && requested.touchscreen.isTruthy()) {
        return touchscreen.isTruthy()
      }
    }

    if (getInput().isTruthy() || other.getInput().isTruthy()) {
      val keysHidden = inputFlags.toInt() and INPUT_FLAGS.KEYSHIDDEN_MASK
      val otherKeysHidden = other.inputFlags.toInt() and INPUT_FLAGS.KEYSHIDDEN_MASK
      if (keysHidden != otherKeysHidden) {
        val requestedKeysHidden = requested.inputFlags.toInt() and INPUT_FLAGS.KEYSHIDDEN_MASK
        if (requestedKeysHidden.isTruthy()) {
          when {
            !keysHidden.isTruthy() -> return false
            !otherKeysHidden.isTruthy() -> return true
            // For compatibility, we count KEYSHIDDEN_NO as being the same as KEYSHIDDEN_SOFT.  Here
            // we disambiguate these by making an exact match more specific.
            requestedKeysHidden == keysHidden -> return true
            requestedKeysHidden == otherKeysHidden -> return false
          }
        }
      }

      val navHidden = inputFlags.toInt() and INPUT_FLAGS.NAVHIDDEN_MASK
      val otherNavHidden = other.inputFlags.toInt() and INPUT_FLAGS.NAVHIDDEN_MASK
      if (navHidden != otherNavHidden &&
        (requested.inputFlags.toInt() and INPUT_FLAGS.NAVHIDDEN_MASK).isTruthy()) {

        return navHidden.isTruthy()
      }

      if (keyboard != other.keyboard && requested.keyboard.isTruthy()) {
        return keyboard.isTruthy()
      }

      if (navigation != other.navigation && requested.navigation.isTruthy()) {
        return navigation.isTruthy()
      }
    }

    if (getScreenSize().isTruthy() || other.getScreenSize().isTruthy()) {
      // "Better" is based on the sum of the difference between both width and height from the
      // requested dimensions.  We are assuming the invalid configs (with smaller sizes) have
      // already been filtered.  Note that if a particular dimension is unspecified, we will end up
      // with a large value (the difference between 0 and the requested dimension), which is
      // good since we will prefer a config that has specified a size value.
      var myDelta = 0
      var otherDelta = 0
      if (requested.screenWidth.isTruthy()) {
        myDelta += requested.screenWidth - screenWidth
        otherDelta += requested.screenWidth - other.screenWidth
      }
      if (requested.screenHeight.isTruthy()) {
        myDelta += requested.screenHeight - screenHeight
        otherDelta += requested.screenHeight - other.screenHeight
      }
      if (myDelta != otherDelta) {
        return myDelta < otherDelta
      }
    }

    if (getVersion().isTruthy() || other.getVersion().isTruthy()) {
      if (sdkVersion != other.sdkVersion && requested.sdkVersion.isTruthy()) {
        return sdkVersion > other.sdkVersion
      }

      if (minorVersion != other.minorVersion && requested.minorVersion.isTruthy()) {
        return minorVersion.isTruthy()
      }
    }

    return false
  }

  /**
   * Return true if 'this' is a better locale match than 'o' for the 'requested' configuration.
   * Similar to isBetterThan(), this assumes that match() has already been used to remove any
   * configurations that don't match the requested configuration at all.
   */
  fun isLocaleBetterThan(other: ResTableConfig, requested: ResTableConfig): Boolean {
    if (requested.getLocale() == 0) {
      // The request doesn't have a locale, so no resource is better than the other.
      return false
    }
    if (getLocale() == 0 && other.getLocale() == 0) {
      // The locale part of both resources is empty, so none is better than the other.
      return false
    }
    // Non-matching locales have been filtered out, so both resources match the requested locale.
    //
    // Because of the locale-related checks in match() and the checks, we know that:
    // 1) The resource languages are either empty or match the request; and
    // 2) If the request's script is known, the resource scripts are either unknown or match the
    // request.
    if (!langsAreEquivalent(language, other.language)) {
      // The languages of the two resources are not equivalent. If we are here, we can only assume
      // that the two resources matched the request because one doesn't have a language and the
      // other has a matching language.
      //
      // We consider the one that has the language specified a better match.
      //
      // The exception is that we consider no-language resources a better match for US English and
      // similar locales than locales that are a descendant of International English (en-001), since
      // no-language resources are where the US English resource have traditionally lived for most
      // apps.
      if (requested.language contentEquals ENGLISH) {
        when {
          requested.country contentEquals UNITED_STATES -> {
            // For US English itself, we consider a no-locale resource a better match than if the
            // resource has a country other than US specified.
            return if (language[0].isTruthy()) {
              country[0] == 0.toByte() || country contentEquals UNITED_STATES
            } else {
              !(other.country[0] == 0.toByte() || other.country contentEquals UNITED_STATES)
            }
          }
          localeDataIsCloseToUsEnglish(requested.country) -> {
            return if (language[0].isTruthy()) {
              localeDataIsCloseToUsEnglish(country)
            } else {
              !localeDataIsCloseToUsEnglish(other.country)
            }
          }
        }
      }
      return language[0].isTruthy()
    }

    // If we are here, both the resources have an equivalent non-empty language to the request.
    //
    // Because the languages are equivalent, computeScript() always returns a non-empty script for
    // languages it knows about, and we have passed the script checks in match(), the scripts are
    // either all unknown or are all the same. So we can't gain anything by checking the scripts. We
    // need to check the region and variant.

    // See if any of the regions is better than the other.
    val regionComparision = localeDataCompareRegions(
      country, other.country, requested.language, String(requested.localeScript), requested.country)
    if (regionComparision != 0) {
      return regionComparision > 0
    }

    // If regions are the same. Try the variant.
    val localeVariantMatches = localeVariant contentEquals requested.localeVariant
    val otherVariantMatches = other.localeVariant contentEquals requested.localeVariant
    if (localeVariantMatches != otherVariantMatches) {
      return localeVariantMatches
    }

    // The variants are the same, try numbering system.
    val localeNumberingMatches = localeNumberSystem contentEquals requested.localeNumberSystem
    val otherNumberingMatches = other.localeNumberSystem contentEquals requested.localeNumberSystem
    if (localeNumberingMatches != otherNumberingMatches) {
      return localeNumberingMatches
    }

    // Finally, the languages, although equivalent, may still be different (like for Tagalog and
    // Filipino). Identical is better than just equivalent.
    return language contentEquals requested.language &&
      !(other.language contentEquals requested.language)
  }


  /**
   * Return true if 'this' can be considered a match for the parameters in 'settings'.
   *
   * @note This is asymetric.  A default piece of data will match every request but a request for
   * the default should not match odd specifics (ie, request with no mcc should not match a
   * particular mcc's data) settings is the requested settings
   */
  fun match(settings: ResTableConfig): Boolean {
    if (mcc.isTruthy() && mcc != settings.mcc) {
      return false
    }
    if (mnc.isTruthy() && mnc != settings.mnc) {
      return false
    }
    if (getLocale().isTruthy()) {
      // Don't consider country and variants when deciding matches. (Theoretically, the variant can
      // also affect the script. For example, "ar-alalc97" probably implies the Latin script, but
      // since CLDR doesn't support getting likely scripts for that, we'll assume the variant
      // doesn't change the script.)
      //
      // If two configs differ only in their country and variant, they can be weeded out in the
      // isMoreSpecificThan test.
      if (!langsAreEquivalent(language, settings.language)) {
        return false
      }

      // For backward compatibility and supporting private-use locales, we fall back to old behavior
      // if we couldn't determine the script for either of the desired locale or the provided
      // locale. But if we could determine the scripts, they should be the same for the locales to
      // match.
      var countriesMustMatch = false
      val computedScript = ByteArray(4)
      var script: ByteArray? = null
      if (!settings.localeScript[0].isTruthy()) {
        countriesMustMatch = true
      } else {
        if (!localeScript[0].isTruthy() && !localeScriptWasComputed) {
          // script was not provided or computed, so we try to compute it.
          localeDataComputeScript(computedScript, language, country)
          if (!computedScript[0].isTruthy()) { // failed to compute the script
            countriesMustMatch = true
          } else {
            script = computedScript
          }
        } else {
          // script was provided so just use it
          script = localeScript
        }
      }

      if (countriesMustMatch) {
        if (country[0].isTruthy() && !(country contentEquals settings.country)) {
          return false
        }
      } else {
        if (script?.contentEquals(localeScript) != true) {
          return false
        }
      }
    }

    if (grammaticalInflection.isTruthy() && grammaticalInflection != settings.grammaticalInflection) {
      return false
    }

    if (getScreenConfig().isTruthy()) {
      val layoutDir = screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK
      val setLayoutDir = settings.screenLayout.toInt() and SCREEN_LAYOUT.DIR_MASK
      if (layoutDir != 0 && layoutDir != setLayoutDir) {
        return false
      }

      val screenSize = screenLayout.toInt() and SCREEN_LAYOUT.SIZE_MASK
      val setScreenSize = settings.screenLayout.toInt() and SCREEN_LAYOUT.SIZE_MASK
      // Any screen sizes for larger screens than the setting do not match.
      if (screenSize.isTruthy() && screenSize > setScreenSize) {
        return false
      }

      val screenLong = screenLayout.toInt() and SCREEN_LAYOUT.SCREENLONG_MASK
      val setScreenLong = settings.screenLayout.toInt() and SCREEN_LAYOUT.SCREENLONG_MASK
      if (screenLong.isTruthy() && screenLong != setScreenLong) {
        return false
      }

      val uiModeType = uiMode.toInt() and UI_MODE.TYPE_MASK
      val setUiModeType = settings.uiMode.toInt() and UI_MODE.TYPE_MASK
      if (uiModeType.isTruthy() && uiModeType != setUiModeType) {
        return false
      }

      val uiModeNight = uiMode.toInt() and UI_MODE.NIGHT_MASK
      val setUiModeNight = settings.uiMode.toInt() and UI_MODE.NIGHT_MASK
      if (uiModeNight.isTruthy() && uiModeNight != setUiModeNight) {
        return false
      }

      if (smallestScreenWidthDp.isTruthy() &&
        smallestScreenWidthDp > settings.smallestScreenWidthDp) {
        return false
      }
    }

    if (getScreenConfig2().isTruthy()) {
      val screenRound = screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK
      val setScreenRound = settings.screenLayout2.toInt() and SCREEN_LAYOUT2.SCREENROUND_MASK
      if (screenRound.isTruthy() && screenRound != setScreenRound) {
        return false
      }

      val hdr = colorMode.toInt() and COLOR_MODE.HDR_MASK
      val setHdr = settings.colorMode.toInt() and COLOR_MODE.HDR_MASK
      if (hdr.isTruthy() && hdr != setHdr) {
        return false
      }

      val wideGamut = colorMode.toInt() and COLOR_MODE.WIDE_GAMUT_MASK
      val setWideGamut = settings.colorMode.toInt() and COLOR_MODE.WIDE_GAMUT_MASK
      if (wideGamut.isTruthy() && wideGamut != setWideGamut) {
        return false
      }
    }

    if (getScreenSizeDp().isTruthy()) {
      if (screenWidthDp.isTruthy() && screenWidthDp > settings.screenWidthDp) {
        return false
      }
      if (screenHeightDp.isTruthy() && screenHeightDp > settings.screenHeightDp) {
        return false
      }
    }

    if (getScreenType().isTruthy()) {
      if (orientation.isTruthy() && orientation != settings.orientation) {
        return false
      }
      // density always matches - we can scale it.  See isBetterThan
      if (touchscreen.isTruthy() && touchscreen != settings.touchscreen) {
        return false
      }
    }

    if (getInput().isTruthy()) {
      val keysHidden = inputFlags.toInt() and INPUT_FLAGS.KEYSHIDDEN_MASK
      val setKeysHidden = settings.inputFlags.toInt() and INPUT_FLAGS.KEYSHIDDEN_MASK
      if (keysHidden.isTruthy() && keysHidden != setKeysHidden) {
        // For compatibility, we count a request for KEYSHIDDEN_NO as also matching the more recent
        // KEYSHIDDEN_SOFT.  Basically KEYSHIDDEN_NO means there is some kind of keyboard available.
        if (keysHidden != INPUT_FLAGS.KEYSHIDDEN_NO.toInt() ||
          setKeysHidden != INPUT_FLAGS.KEYSHIDDEN_SOFT.toInt()) {
          return false
        }
      }

      val navHidden = inputFlags.toInt() and INPUT_FLAGS.NAVHIDDEN_MASK
      val setNavHidden = settings.inputFlags.toInt() and INPUT_FLAGS.NAVHIDDEN_MASK
      if (navHidden.isTruthy() && navHidden != setNavHidden) {
        return false
      }

      if (keyboard.isTruthy() && keyboard != settings.keyboard) {
        return false
      }

      if (navigation.isTruthy() && navigation != settings.navigation) {
        return false
      }
    }

    if (getScreenSize().isTruthy()) {
      if (screenWidth.isTruthy() && screenWidth > settings.screenWidth) {
        return false
      }
      if (screenHeight.isTruthy() && screenHeight > settings.screenHeight) {
        return false
      }
    }
    if (getVersion().isTruthy()) {
      if (sdkVersion.isTruthy() && sdkVersion > settings.sdkVersion) {
        return false
      }
      if (minorVersion.isTruthy() && minorVersion != settings.minorVersion) {
        return false
      }
    }

    return true
  }

  /**
   * Get the string representation of the locale component of this Config.
   *
   * Example: en-US, en-Latn-US, en-POSIX.
   *
   * @param canonicalize if set, Tagalog (tl) locales get converted to Filipino (fil).
   * @return the string representation of the locale
   */
  fun getBcp47Locale(canonicalize: Boolean = false): String {
    // This represents the "any" locale value, which has traditionally been
    // represented by the empty string.
    if (language[0] == 0.toByte() && country[0] == 0.toByte()) {
      return ""
    }

    val localeString = StringBuilder()
    if (language[0] != 0.toByte()) {
      if (canonicalize && language contentEquals TAGALOG) {
        // Replace Tagalog with Filipino if we are canonicalizing.
        localeString.append("fil")
      } else {
        localeString.append(unpackLanguage())
      }
      localeString.append('-')
    }

    if (localeScript[0] != 0.toByte() && !localeScriptWasComputed) {
      var localeScriptEnd = localeScript.indexOf(0.toByte())
      if (localeScriptEnd == -1) {
        localeScriptEnd = localeScript.size
      }
      localeString.append(String(localeScript, 0, localeScriptEnd))
      localeString.append('-')
    }

    if (country[0] != 0.toByte()) {
      localeString.append(unpackRegion())
      localeString.append('-')
    }

    if (localeVariant[0] != 0.toByte()) {
      var localeVariantEnd = localeScript.indexOf(0.toByte())
      if (localeVariantEnd == -1) {
        localeVariantEnd = localeVariant.size
      }
      localeString.append(String(localeVariant, 0, localeVariantEnd))
      localeString.append('-')
    }

    // Add Unicode extension only if at least one other locale component is present
    if (localeNumberSystem[0] != 0.toByte() && localeString.isNotEmpty()) {
      localeString.append(NUMBERING_SYSTEM_PREFIX)
      var localeNumberEnd = localeNumberSystem.indexOf(0.toByte())
      if (localeNumberEnd == -1) {
        localeNumberEnd = localeNumberSystem.size
      }
      localeString.append(String(localeNumberSystem, 0, localeNumberEnd))
      localeString.append('-')
    }

    if (localeString.isEmpty()) {
      return ""
    }

    // Trim off the trailing '-'.
    return localeString.substring(0, localeString.length - 1)
  }

  companion object {
    val ENGLISH = byteArrayOf('e'.code.toByte(), 'n'.code.toByte()) // packed version of "en"
    val UNITED_STATES = byteArrayOf('U'.code.toByte(), 'S'.code.toByte()) // packed version of "US"
    val FILIPINO = byteArrayOf(0xAD.toByte(), 0x05.toByte()) // packed version of "fil"
    val TAGALOG = byteArrayOf('t'.code.toByte(), 'l'.code.toByte()) // packed version of "tl"

    const val NUMBERING_SYSTEM_PREFIX = "u-nu-"

    // Flags indicating a set of config values.  These flag constants must
    // match the corresponding ones in android.content.pm.ActivityInfo and
    // attrs_manifest.xml.
    const val CONFIG_MCC = ACONFIGURATION_MCC.toInt()
    const val CONFIG_MNC = ACONFIGURATION_MNC.toInt()
    const val CONFIG_LOCALE = ACONFIGURATION_LOCALE.toInt()
    const val CONFIG_TOUCHSCREEN = ACONFIGURATION_TOUCHSCREEN.toInt()
    const val CONFIG_KEYBOARD = ACONFIGURATION_KEYBOARD.toInt()
    const val CONFIG_KEYBOARD_HIDDEN = ACONFIGURATION_KEYBOARD_HIDDEN.toInt()
    const val CONFIG_NAVIGATION = ACONFIGURATION_NAVIGATION.toInt()
    const val CONFIG_ORIENTATION = ACONFIGURATION_ORIENTATION
    const val CONFIG_DENSITY = ACONFIGURATION_DENSITY
    const val CONFIG_SCREEN_SIZE = ACONFIGURATION_SCREEN_SIZE
    const val CONFIG_SMALLEST_SCREEN_SIZE = ACONFIGURATION_SMALLEST_SCREEN_SIZE
    const val CONFIG_VERSION = ACONFIGURATION_VERSION
    const val CONFIG_SCREEN_LAYOUT = ACONFIGURATION_SCREEN_LAYOUT
    const val CONFIG_UI_MODE = ACONFIGURATION_UI_MODE
    const val CONFIG_LAYOUTDIR = ACONFIGURATION_LAYOUTDIR
    const val CONFIG_SCREEN_ROUND = ACONFIGURATION_SCREEN_ROUND
    const val CONFIG_COLOR_MODE = ACONFIGURATION_COLOR_MODE
    const val CONFIG_GRAMMATICAL_GENDER = ACONFIGURATION_GRAMMATICAL_GENDER

    const val SCREEN_CONFIG_MIN_SIZE = 32
    const val SCREEN_DP_MIN_SIZE = 36
    const val LOCALE_MIN_SIZE = 48
    const val SCREEN_CONFIG_EXTENSION_MIN_SIZE = 52
    const val NUMBER_SYSTEM_MIN_SIZE = 60

    internal fun langsAreEquivalent(lang1: ByteArray, lang2: ByteArray) =
      when {
        lang1 contentEquals lang2 -> true
        lang1 contentEquals FILIPINO -> lang2 contentEquals TAGALOG
        lang1 contentEquals TAGALOG -> lang2 contentEquals FILIPINO
        else -> false
      }

    internal fun compareArrays(left: ByteArray, right: ByteArray): Int {
      left.forEachIndexed { index, byte ->
        when {
          byte < right[index] -> return -1
          byte > right[index] -> return 1
        }
      }
      return 0
    }

    internal fun packLanguageOrRegion(input: String?, base: Byte): ByteArray {
      val result = ByteArray(2)
      when {
        input.isNullOrEmpty() -> {}
        input.length <= 2 || input[2] == '-' -> {
          result[0] = input[0].code.toByte()
          result[1] = input[1].code.toByte()
        }
        else -> {
          val first = (input[0].code.toByte() - base) and 0x7f
          val second = (input[1].code.toByte() - base) and 0x7f
          val third = (input[2].code.toByte() - base) and 0x7f

          result[0] = (0x80 or (third shl 2) or (second ushr 3)).toByte()
          result[1] = ((second shl 5) or first).toByte()
        }
      }
      return result
    }

    internal fun unpackLanguageOrRegion(input: ByteArray, base: Byte) =
      when {
        (input[0].toInt() and 0x80).isTruthy() -> {
          // The high bit is "1", which means this is a packed three letter language code.

          // The smallest 5 bits of the second char are the first alphabet.
          val firstChar = ((input[1].toInt() and 0x1f) + base).toByte()
          // The last three bits of the second char and the first two bits
          // of the first char are the second alphabet.
          val secondChar = (((input[1].toInt() and 0xe0) shr 5) +
            ((input[0].toInt() and 0x03) shl 3) + base).toByte()
          // Bits 3 to 7 (inclusive) of the first char are the third alphabet.
          val thirdChar = (((input[0].toInt() and 0x7c) shr 2) + base).toByte()

          String(byteArrayOf(firstChar, secondChar, thirdChar))
        }
        input[0].isTruthy() -> String(input)
        else -> ""
      }

    // TODO (daniellabar): Switch over to BigBuffer.BlockRef later
    internal fun createConfig(buffer: ByteBuffer): ResTableConfig {
      val startPosition = buffer.position()
      val sizeOnDevice = buffer.int
      val sizeOnHost = sizeOnDevice.deviceToHost()
      val imsi = buffer.int
      val locale = buffer.int
      val screenType = buffer.int
      val input = buffer.int
      val grammaticalInflection = buffer.int
      val screenSize = buffer.int
      val version = buffer.int

      // At this point, the configuration's size needs to be taken into account as not all
      // configurations have all values.
      val screenConfig = if (sizeOnHost >= SCREEN_CONFIG_MIN_SIZE) buffer.int else 0

      val screenSizeDp = if (sizeOnHost >= SCREEN_DP_MIN_SIZE) buffer.int else 0

      val localeScript = ByteArray(4)
      val localeVariant = ByteArray(8)
      if (sizeOnHost >= LOCALE_MIN_SIZE) {
        buffer.get(localeScript)
        buffer.get(localeVariant)
      }

      val screenConfig2 = if (sizeOnHost >= SCREEN_CONFIG_EXTENSION_MIN_SIZE) buffer.int else 0

      val numberSystem = ByteArray(8)
      if (sizeOnHost >= NUMBER_SYSTEM_MIN_SIZE) {
        buffer.get(numberSystem)
      }

      // Toss the unknown parts, but move the ByteBuffer over
      if (buffer.position() < startPosition + sizeOnHost) {
        // TODO (daniellabar) possible diagnostics for unaccounted size.
        buffer.position(startPosition + sizeOnHost)
      }

      return ResTableConfig(
        sizeOnDevice,
        imsi,
        locale,
        screenType,
        input,
        grammaticalInflection,
        screenSize,
        version,
        screenConfig,
        screenSizeDp,
        localeScript,
        localeVariant,
        screenConfig2,
        numberSystem)
    }

    // block methods here
    internal fun mccFromImsi(imsi: Int) = imsi.deviceToHost().toShort()

    internal fun mncFromImsi(imsi: Int) = (imsi.deviceToHost() ushr 16).toShort()

    internal fun languageFromLocale(locale: Int): ByteArray {
      val hostLocale = locale.deviceToHost()
      return byteArrayOf((hostLocale ushr 8).toByte(), hostLocale.toByte())
    }

    internal fun countryFromLocale(locale: Int): ByteArray {
      val hostLocale = locale.deviceToHost()
      return byteArrayOf((hostLocale ushr 24).toByte(), (hostLocale ushr 16).toByte())
    }

    internal fun orientationFromScreenType(screenType: Int) = screenType.deviceToHost().toByte()

    internal fun touchscreenFromScreenType(screenType: Int) =
      (screenType.deviceToHost() ushr 8).toByte()

    internal fun densityFromScreenType(screenType: Int) = screenType.deviceToHost() ushr 16

    internal fun keyboardFromInput(input: Int) = input.deviceToHost().toByte()

    internal fun navigationFromInput(input: Int) = (input.deviceToHost() ushr 8).toByte()

    internal fun inputFlagsFromInput(input: Int) = (input.deviceToHost() ushr 16).toByte()

    internal fun screenWidthFromScreenSize(screenSize: Int) = screenSize.deviceToHost() and 0xffff

    internal fun screenHeightFromScreenSize(screenSize: Int) = screenSize.deviceToHost() ushr 16

    internal fun sdkVersionFromVersion(version: Int) = version.deviceToHost().toShort()

    internal fun minorVersionFromVersion(version: Int) = (version.deviceToHost() ushr 16).toShort()

    internal fun screenLayoutFromScreenConfig(screenConfig: Int) =
      screenConfig.deviceToHost().toByte()

    internal fun uiModeFromScreenConfig(screenConfig: Int) =
      (screenConfig.deviceToHost() ushr 8).toByte()

    internal fun smallestScreenWidthDpFromScreenConfig(screenConfig: Int) =
      screenConfig.deviceToHost() ushr 16

    internal fun screenWidthDpFromScreenSizeDp(screenSizeDp: Int) =
      screenSizeDp.deviceToHost() and 0xffff

    internal fun screenHeightDpFromScreenSizeDp(screenSizeDp: Int) =
      (screenSizeDp.deviceToHost() ushr 16) and 0xffff

    internal fun screenLayout2FromScreenConfig2(screenConfig2: Int) =
      screenConfig2.deviceToHost().toByte()

    internal fun colorModeFromScreenConfig2(screenConfig2: Int) =
      (screenConfig2.deviceToHost() ushr 8).toByte()
  }

  // ScreenType values
  object ORIENTATION {
    const val ANY = AConfiguration.ACONFIGURATION_ORIENTATION_ANY
    const val PORT = AConfiguration.ACONFIGURATION_ORIENTATION_PORT
    const val LAND = AConfiguration.ACONFIGURATION_ORIENTATION_LAND
    @Suppress("DEPRECATION") // Legacy support
    const val SQUARE = AConfiguration.ACONFIGURATION_ORIENTATION_SQUARE
  }

  object TOUCHSCREEN {
    const val ANY = AConfiguration.ACONFIGURATION_TOUCHSCREEN_ANY
    const val NOTOUCH = AConfiguration.ACONFIGURATION_TOUCHSCREEN_NOTOUCH
    @Suppress("DEPRECATION") // Legacy support
    const val STYLUS = AConfiguration.ACONFIGURATION_TOUCHSCREEN_STYLUS
    const val FINGER = AConfiguration.ACONFIGURATION_TOUCHSCREEN_FINGER
  }

  object DENSITY {
    const val DEFAULT = AConfiguration.ACONFIGURATION_DENSITY_DEFAULT.toInt()
    const val LOW = AConfiguration.ACONFIGURATION_DENSITY_LOW.toInt()
    const val MEDIUM = AConfiguration.ACONFIGURATION_DENSITY_MEDIUM
    const val TV = AConfiguration.ACONFIGURATION_DENSITY_TV
    const val HIGH = AConfiguration.ACONFIGURATION_DENSITY_HIGH
    const val XHIGH = AConfiguration.ACONFIGURATION_DENSITY_XHIGH
    const val XXHIGH = AConfiguration.ACONFIGURATION_DENSITY_XXHIGH
    const val XXXHIGH = AConfiguration.ACONFIGURATION_DENSITY_XXXHIGH
    const val ANY = AConfiguration.ACONFIGURATION_DENSITY_ANY
    const val NONE = AConfiguration.ACONFIGURATION_DENSITY_NONE
  }

  // Input values
  object KEYBOARD {
    const val ANY = AConfiguration.ACONFIGURATION_KEYBOARD_ANY
    const val NOKEYS = AConfiguration.ACONFIGURATION_KEYBOARD_NOKEYS
    const val QWERTY = AConfiguration.ACONFIGURATION_KEYBOARD_QWERTY
    const val TWELVEKEY = AConfiguration.ACONFIGURATION_KEYBOARD_12KEY
  }

  object NAVIGATION {
    const val ANY = AConfiguration.ACONFIGURATION_NAVIGATION_ANY
    const val NONAV = AConfiguration.ACONFIGURATION_NAVIGATION_NONAV
    const val DPAD = AConfiguration.ACONFIGURATION_NAVIGATION_DPAD
    const val TRACKBALL = AConfiguration.ACONFIGURATION_NAVIGATION_TRACKBALL
    const val WHEEL = AConfiguration.ACONFIGURATION_NAVIGATION_WHEEL
  }

  object INPUT_FLAGS {
    const val KEYSHIDDEN_MASK = 0x03
    // const val KEYSHIDDEN_SHIFT = 0
    const val KEYSHIDDEN_ANY = AConfiguration.ACONFIGURATION_KEYSHIDDEN_ANY
    const val KEYSHIDDEN_NO = AConfiguration.ACONFIGURATION_KEYSHIDDEN_NO
    const val KEYSHIDDEN_YES = AConfiguration.ACONFIGURATION_KEYSHIDDEN_YES
    const val KEYSHIDDEN_SOFT = AConfiguration.ACONFIGURATION_KEYSHIDDEN_SOFT

    const val NAVHIDDEN_MASK = 0X0c
    const val NAVHIDDEN_SHIFT = 2
    const val NAVHIDDEN_ANY =
      (AConfiguration.ACONFIGURATION_NAVHIDDEN_ANY.toInt() shl NAVHIDDEN_SHIFT).toByte()
    const val NAVHIDDEN_NO =
      (AConfiguration.ACONFIGURATION_NAVHIDDEN_NO.toInt() shl NAVHIDDEN_SHIFT).toByte()
    const val NAVHIDDEN_YES =
      (AConfiguration.ACONFIGURATION_NAVHIDDEN_YES.toInt() shl NAVHIDDEN_SHIFT).toByte()
  }

  object GRAMMATICAL_GENDER {
    const val ANY = AConfiguration.ACONFIGURATION_GRAMMATICAL_GENDER_ANY
    const val NEUTER = AConfiguration.ACONFIGURATION_GRAMMATICAL_GENDER_NEUTER
    const val FEMININE = AConfiguration.ACONFIGURATION_GRAMMATICAL_GENDER_FEMININE
    const val MASCULINE = AConfiguration.ACONFIGURATION_GRAMMATICAL_GENDER_MASCULINE
  }

  // Screensize values
  object SCREENSIZE {
    const val WIDTH_ANY = 0
    const val HEIGHT_ANY = 0
  }

  // Version values
  object VERSION {
    const val SDK_ANY = 0
    const val MINORVERSION_ANY = 0
  }

  // ScreenConfig values
  object SCREEN_LAYOUT {
    const val DIR_MASK = 0xC0
    const val DIR_SHIFT = 6
    const val DIR_ANY = (AConfiguration.ACONFIGURATION_LAYOUTDIR_ANY.toInt() shl DIR_SHIFT).toByte()
    const val DIR_LTR = (AConfiguration.ACONFIGURATION_LAYOUTDIR_LTR.toInt() shl DIR_SHIFT).toByte()
    const val DIR_RTL = (AConfiguration.ACONFIGURATION_LAYOUTDIR_RTL.toInt() shl DIR_SHIFT).toByte()

    const val SCREENLONG_MASK = 0x30
    const val SCREENLONG_SHIFT = 4
    const val SCREENLONG_ANY =
      (AConfiguration.ACONFIGURATION_SCREENLONG_ANY.toInt() shl SCREENLONG_SHIFT).toByte()
    const val SCREENLONG_NO =
      (AConfiguration.ACONFIGURATION_SCREENLONG_NO.toInt() shl SCREENLONG_SHIFT).toByte()
    const val SCREENLONG_YES =
      (AConfiguration.ACONFIGURATION_SCREENLONG_YES.toInt() shl SCREENLONG_SHIFT).toByte()

    const val SIZE_MASK = 0x0f
    // const val SIZE_SHIFT = 0
    const val SIZE_ANY = AConfiguration.ACONFIGURATION_SCREENSIZE_ANY
    const val SIZE_SMALL = AConfiguration.ACONFIGURATION_SCREENSIZE_SMALL
    const val SIZE_NORMAL = AConfiguration.ACONFIGURATION_SCREENSIZE_NORMAL
    const val SIZE_LARGE = AConfiguration.ACONFIGURATION_SCREENSIZE_LARGE
    const val SIZE_XLARGE = AConfiguration.ACONFIGURATION_SCREENSIZE_XLARGE
  }

  object UI_MODE {
    const val TYPE_MASK = 0x0f
    // const val TYPE_SHIFT = 0
    const val TYPE_ANY = AConfiguration.ACONFIGURATION_UI_MODE_TYPE_ANY
    const val TYPE_NORMAL = AConfiguration.ACONFIGURATION_UI_MODE_TYPE_NORMAL
    const val TYPE_DESK = AConfiguration.ACONFIGURATION_UI_MODE_TYPE_DESK
    const val TYPE_CAR = AConfiguration.ACONFIGURATION_UI_MODE_TYPE_CAR
    const val TYPE_TELEVISION = AConfiguration.ACONFIGURATION_UI_MODE_TYPE_TELEVISION
    const val TYPE_APPLIANCE = AConfiguration.ACONFIGURATION_UI_MODE_TYPE_APPLIANCE
    const val TYPE_WATCH = AConfiguration.ACONFIGURATION_UI_MODE_TYPE_WATCH
    const val TYPE_VR_HEADSET = AConfiguration.ACONFIGURATION_UI_MODE_TYPE_VR_HEADSET

    const val NIGHT_MASK = 0x30
    const val NIGHT_SHIFT = 4
    const val NIGHT_ANY =
      (AConfiguration.ACONFIGURATION_UI_MODE_NIGHT_ANY.toInt() shl NIGHT_SHIFT).toByte()
    const val NIGHT_NO =
      (AConfiguration.ACONFIGURATION_UI_MODE_NIGHT_NO.toInt() shl NIGHT_SHIFT).toByte()
    const val NIGHT_YES =
      (AConfiguration.ACONFIGURATION_UI_MODE_NIGHT_YES.toInt() shl NIGHT_SHIFT).toByte()
  }

  // ScreenConfig2 values
  object SCREEN_LAYOUT2 {
    const val SCREENROUND_MASK = 0x03
    // const val SCREENROUND_SHIFT = 0
    const val SCREENROUND_ANY = AConfiguration.ACONFIGURATION_SCREENROUND_ANY
    const val SCREENROUND_NO = AConfiguration.ACONFIGURATION_SCREENROUND_NO
    const val SCREENROUND_YES = AConfiguration.ACONFIGURATION_SCREENROUND_YES
  }

  object COLOR_MODE {
    const val WIDE_GAMUT_MASK = 0x03
    // const val GAMUT_SHIFT = 0
    const val WIDE_GAMUT_ANY = AConfiguration.ACONFIGURATION_WIDE_COLOR_GAMUT_ANY
    const val WIDE_GAMUT_NO = AConfiguration.ACONFIGURATION_WIDE_COLOR_GAMUT_NO
    const val WIDE_GAMUT_YES = AConfiguration.ACONFIGURATION_WIDE_COLOR_GAMUT_YES

    const val HDR_MASK = 0X0c
    const val HDR_SHIFT = 2
    const val HDR_ANY = (AConfiguration.ACONFIGURATION_HDR_ANY.toInt() shl HDR_SHIFT).toByte()
    const val HDR_NO = (AConfiguration.ACONFIGURATION_HDR_NO.toInt() shl HDR_SHIFT).toByte()
    const val HDR_YES = (AConfiguration.ACONFIGURATION_HDR_YES.toInt() shl HDR_SHIFT).toByte()
  }
}
