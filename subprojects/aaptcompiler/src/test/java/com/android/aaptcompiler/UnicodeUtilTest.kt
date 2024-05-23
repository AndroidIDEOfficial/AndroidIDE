package com.android.aaptcompiler

import com.google.common.truth.Truth
import org.junit.Test

class UnicodeUtilTest {

  @Test
  fun testIsXidStart() {
    val validInput = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZˮø"
    for (i in 0.until(validInput.codePointCount(0, validInput.length))) {
      Truth.assertThat(isXidStart(validInput.codePointAt(i))).isTrue()
    }

    val invalidInput = "$;\'/<>+=-.{}[]()\\|?@#%^&*!~`\",1234567890_"
    for (i in 0.until(invalidInput.codePointCount(0, invalidInput.length))) {
      Truth.assertThat(isXidStart(invalidInput.codePointAt(i))).isFalse()
    }
  }

  @Test
  fun testIsXidContinue() {
    val validInput = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_ˮø"
    for (i in 0.until(validInput.codePointCount(0, validInput.length))) {
      Truth.assertThat(isXidContinue(validInput.codePointAt(i))).isTrue()
    }

    val invalidInput = "$;\'/<>+=-.{}[]()\\|?@#%^&*!~`\","
    for (i in 0.until(invalidInput.codePointCount(0, invalidInput.length))) {
      Truth.assertThat(isXidContinue(invalidInput.codePointAt(i))).isFalse()
    }
  }

  @Test
  fun testIsJavaIdentifier() {
    Truth.assertThat(isJavaIdentifier("FøøBar_12")).isTrue()
    Truth.assertThat(isJavaIdentifier("Føø\$Bar")).isTrue()
    Truth.assertThat(isJavaIdentifier("_FøøBar")).isTrue()
    Truth.assertThat(isJavaIdentifier("\$Føø\$Bar")).isTrue()

    Truth.assertThat(isJavaIdentifier("12FøøBar")).isFalse()
    Truth.assertThat(isJavaIdentifier(".Hello")).isFalse()
  }

  @Test
  fun testIsValidResourceEntryName() {
    Truth.assertThat(isJavaIdentifier("FøøBar")).isTrue()
    Truth.assertThat(isValidResourceEntryName("FøøBar_12")).isTrue()
    Truth.assertThat(isValidResourceEntryName("Føø.Bar")).isTrue()
    Truth.assertThat(isValidResourceEntryName("Føø-Bar")).isTrue()
    Truth.assertThat(isValidResourceEntryName("_FøøBar")).isTrue()

    Truth.assertThat(isValidResourceEntryName("12FøøBar")).isFalse()
    Truth.assertThat(isValidResourceEntryName("Føø\$Bar")).isFalse()
    Truth.assertThat(isValidResourceEntryName("Føø/Bar")).isFalse()
    Truth.assertThat(isValidResourceEntryName("Føø:Bar")).isFalse()
    Truth.assertThat(isValidResourceEntryName("Føø;Bar")).isFalse()
    Truth.assertThat(isValidResourceEntryName("0_resource_name_obfuscated")).isFalse()
  }
}