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

package com.itsaky.androidide.resources.localization;

import androidx.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import kotlin.text.StringsKt;

/**
 * @author Akash Yadav
 */
public class LocaleProvider {

  /**
   * Locales supported by AndroidIDE.
   */
  public static final Map<String, Locale> SUPPORTED_LOCALES;
  private static final Map<Locale, String> LOCALE_KEYS;

  static {
    final var locales = new HashMap<String, Locale>();
    final var keys = new HashMap<Locale, String>();
    putLocale(locales, keys, "en", null); // English
    putLocale(locales, keys, "ar", "SA"); // Arabic
    putLocale(locales, keys, "bn", "IN"); // Bengali (India)
    putLocale(locales, keys, "de", "DE"); // German
    putLocale(locales, keys, "es", "ES"); // Spanish
    putLocale(locales, keys, "fr", "FR"); // French
    putLocale(locales, keys, "hi", "IN"); // Hindi
    putLocale(locales, keys, "in", "ID"); // Indonesian
    putLocale(locales, keys, "pt", "BR"); // Portuguese, Brazilian
    putLocale(locales, keys, "ro", "RO"); // Romanian
    putLocale(locales, keys, "ru", "RU"); // Russian
    putLocale(locales, keys, "tr", "TR"); // Turkish
    putLocale(locales, keys, "zh", "CN"); // Chinese (Simplified)

    SUPPORTED_LOCALES = Collections.unmodifiableMap(locales);
    LOCALE_KEYS = Collections.unmodifiableMap(keys);
  }

  @Nullable
  public static Locale getLocale(@Nullable String key) {
    if (key == null || StringsKt.isBlank(key)) {
      return null;
    }
    return SUPPORTED_LOCALES.get(key);
  }

  @Nullable
  public static String getKey(@Nullable Locale locale) {
    if (locale == null) {
      return null;
    }
    return LOCALE_KEYS.get(locale);
  }

  private static void putLocale(
      Map<String, Locale> locales,
      Map<Locale, String> keys,
      String lang,
      String region
  ) {
    final String key;
    if (region == null) {
      key = lang;
    } else {
      key = lang + "-r" + region;
    }

    final var locale = createLocale(lang, region);
    locales.put(key, locale);
    keys.put(locale, key);
  }

  private static Locale createLocale(String lang, String region) {
    if (lang == null || StringsKt.isBlank(lang)) {
      throw new IllegalArgumentException("Language must not be null");
    }
    if (region == null) {
      return new Locale(lang);
    }
    return new Locale(lang, region);
  }
}
