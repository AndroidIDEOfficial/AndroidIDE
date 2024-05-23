package com.android.aaptcompiler

import com.android.aaptcompiler.android.ResTableConfig
import com.google.common.truth.Truth
import org.junit.Test
import java.nio.charset.StandardCharsets

class LocaleValueTest {

  fun testLanguage(input: String, expectedLang: String, expectFailure: Boolean = false) {
    val parts = input.split('-').map { it.lowercase() }
    val locale = LocaleValue()

    val count = locale.initFromParts(parts, 0)

    Truth.assertThat(count).isEqualTo(if (expectFailure) 0 else 1)
    Truth.assertThat(locale.language).isEqualTo(expectedLang)
  }

  fun testLanguageRegion(input: String, expectedLang: String, expectedRegion: String) {
    val parts = input.split('-').map { it.lowercase() }
    val locale = LocaleValue()

    val count = locale.initFromParts(parts, 0)

    Truth.assertThat(count).isEqualTo(2)
    Truth.assertThat(locale.language).isEqualTo(expectedLang)
    Truth.assertThat(locale.region).isEqualTo(expectedRegion)
  }

  @Test
  fun testParseLanguage() {
    testLanguage("en", "en")
    testLanguage("fr", "fr")
    testLanguage("land", "", true)
    testLanguage("fr-land", "fr")

    testLanguageRegion("fr-rCA", "fr","CA")
  }

  @Test
  fun testWritingLocaleToResTableConfig() {
    val languageLocaleValue = "b+be+tarask"
    val parts = languageLocaleValue.split('-').map(String::lowercase)
    val locale = LocaleValue()
    locale.initFromParts(parts, 0)

    val resTableConfig = ResTableConfig()
    locale.writeTo(resTableConfig)
    Truth.assertThat(resTableConfig.language.toString(StandardCharsets.UTF_8))
        .isEqualTo("be")
    Truth.assertThat(resTableConfig.localeVariant)
        // bytearray is utf-8 representation of 'tarask' followed by two null values
        // as it is 6 bytes in length but represented by 8 bytes.
        .isEqualTo(byteArrayOf(116, 97, 114, 97, 115, 107, 0, 0))
  }
}
