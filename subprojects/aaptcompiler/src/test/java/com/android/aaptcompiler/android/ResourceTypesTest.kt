package com.android.aaptcompiler.android

import com.google.common.truth.Truth
import org.junit.Test

class ResourceTypesTest {

  private fun testStringToIntCase(input: String, expectedValue: Int, expectedHex: Boolean = false) {
    val result = stringToInt(input)
    val expectedOutput =
      ResValue(
        if(expectedHex) ResValue.DataType.INT_HEX else ResValue.DataType.INT_DEC,
        expectedValue)
    Truth.assertThat(result).isEqualTo(expectedOutput)
  }

  private fun testStringToIntFailure(input: String) {
    val result = stringToInt(input)
    Truth.assertThat(result).isNull()
  }

  @Test
  fun testStringToInt() {
    testStringToIntFailure("")
    testStringToIntFailure("    ")
    testStringToIntFailure("\t\n")

    testStringToIntFailure("abcd")
    testStringToIntFailure("10abcd")
    testStringToIntFailure("42 42")
    testStringToIntFailure("- 42")
    testStringToIntFailure("-")

    testStringToIntFailure("0x")
    testStringToIntFailure("0xnope")
    testStringToIntFailure("0X42")
    testStringToIntFailure("0x42 0x42")
    testStringToIntFailure("-0x0")
    testStringToIntFailure("-0x42")
    testStringToIntFailure("- 0x42")

    // Note that u" 42" would pass. This preserves the old behavior, but it may not be desired.
    testStringToIntFailure("42 ")
    testStringToIntFailure("0x42 ")

    // Decimal cases.
    testStringToIntCase("0", 0)
    testStringToIntCase("-0", 0)
    testStringToIntCase("42", 42)
    testStringToIntCase(" 42", 42)
    testStringToIntCase("-42", -42)
    testStringToIntCase("\n  -42", -42)
    testStringToIntCase("042", 42)
    testStringToIntCase("-042", -42)

    // Hex cases.
    testStringToIntCase("0x0", 0x0, true)
    testStringToIntCase("0x42", 0x42, true)
    testStringToIntCase("\t0x42", 0x42, true)

    // Just Before overflow cases
    testStringToIntCase("2147483647", Int.MAX_VALUE)
    testStringToIntCase("-2147483648", Int.MIN_VALUE)
    testStringToIntCase("0xffffffff", -1, true)

    // Overflow cases:
    testStringToIntFailure("2147483648")
    testStringToIntFailure("-2147483649")
    testStringToIntFailure("0x1ffffffff")
  }

  private fun testStringToFloatCaseFloat(input: String, expectedValue: Float) {
    val result = stringToFloat(input)
    val expectedOutput = ResValue(ResValue.DataType.FLOAT, expectedValue.toRawBits())

    Truth.assertThat(result).isEqualTo(expectedOutput)
  }

  private fun testStringToFloatCaseFixed(
    input: String,
    expectedMantissa: Int,
    expectedUnitType: Int,
    expectedFormat: Int,
    isFraction: Boolean = false) {

    val result = stringToFloat(input)
    val expectedValue = (expectedUnitType shl ResValue.ComplexFormat.UNIT_SHIFT) or
      (expectedFormat shl ResValue.ComplexFormat.RADIX_SHIFT) or
      (expectedMantissa shl ResValue.ComplexFormat.MANTISSA_SHIFT)
    val expectedOutput = ResValue(
      if(isFraction) ResValue.DataType.FRACTION else ResValue.DataType.DIMENSION,
      expectedValue)

    Truth.assertThat(result).isEqualTo(expectedOutput)
  }

  private fun testStringToFloatFailure(input: String) {
    Truth.assertThat(stringToFloat(input)).isNull()
  }

  @Test
  fun testStringToFloat() {
    testStringToFloatFailure("")
    testStringToFloatFailure("    ")
    testStringToFloatFailure("\t\n")

    testStringToFloatFailure("abcd")
    testStringToFloatFailure("10abcd")
    testStringToFloatFailure("42 42")
    testStringToFloatFailure("- 42")
    testStringToFloatFailure("-")
    testStringToFloatFailure(".")
    testStringToFloatFailure("+")
    testStringToFloatFailure("+.")
    testStringToFloatFailure("e3")

    testStringToFloatFailure("0x")
    testStringToFloatFailure("0xnope")
    testStringToFloatFailure("0x42 0x42")
    testStringToFloatFailure("- 0x42")
    testStringToFloatFailure("-0xp2")

    // failures of
    testStringToFloatFailure("px")
    testStringToFloatFailure("dip")
    // 'd' could be interpreted as part of the suffix or hex, so it fails.
    testStringToFloatFailure("0x02dp")
    testStringToFloatFailure("0x10 px")
    testStringToFloatFailure("10 px")

    // Decimal Floating Point Cases:
    testStringToFloatCaseFloat("0", 0f)
    testStringToFloatCaseFloat(".0", 0f)
    testStringToFloatCaseFloat("0.", 0f)
    testStringToFloatCaseFloat("-0", -0f)
    testStringToFloatCaseFloat("+0", 0f)
    testStringToFloatCaseFloat("42", 42f)
    testStringToFloatCaseFloat("+42", 42f)
    testStringToFloatCaseFloat("-42", -42f)
    testStringToFloatCaseFloat("314159e-5", 3.14159f)
    testStringToFloatCaseFloat("1e2", 100f)
    testStringToFloatCaseFloat(".42e+2", 42f)

    // Hex Floating Point Cases:
    testStringToFloatCaseFloat("0x0", 0f)
    testStringToFloatCaseFloat("+0xa", 10f)
    testStringToFloatCaseFloat("-0x02a", -42f)
    testStringToFloatCaseFloat("-0x7.8", -7.5f)
    testStringToFloatCaseFloat("0xfp-2", 3.75f)
    testStringToFloatCaseFloat("0x21p2", 132f)
    // capital letters work fine here
    testStringToFloatCaseFloat("0X1.FBP+3", 15.84375f)
    // mix and match is fine too.
    testStringToFloatCaseFloat("0xa.fP2", 43.75f)

    // Fixed Point Cases:
    testStringToFloatCaseFixed(
      "0px",
      0,
      ResValue.ComplexFormat.UNIT_PX,
      ResValue.ComplexFormat.RADIX_23p0)
    testStringToFloatCaseFixed(
      "10dp",
      10,
      ResValue.ComplexFormat.UNIT_DIP,
      ResValue.ComplexFormat.RADIX_23p0)
    testStringToFloatCaseFixed(
      "10dip",
      10,
      ResValue.ComplexFormat.UNIT_DIP,
      ResValue.ComplexFormat.RADIX_23p0)
    testStringToFloatCaseFixed(
      ".5in",
      // mantissa = 0x0.8 << 23
      0x400000,
      ResValue.ComplexFormat.UNIT_IN,
      ResValue.ComplexFormat.RADIX_0p23)
    testStringToFloatCaseFixed(
      "0x10sp",
      16,
      ResValue.ComplexFormat.UNIT_SP,
      ResValue.ComplexFormat.RADIX_23p0)
    testStringToFloatCaseFixed(
      // Specifying the exponent "p" should allow dp to be used as a unit.
      "0x10p0dp",
      16,
      ResValue.ComplexFormat.UNIT_DIP,
      ResValue.ComplexFormat.RADIX_23p0)
    testStringToFloatCaseFixed(
      "100%",
      1,
      ResValue.ComplexFormat.UNIT_FRACTION,
      ResValue.ComplexFormat.RADIX_23p0,
      true)
    // Test some 8p15 fixed point cases.
    testStringToFloatCaseFixed(
      "520%p",
      // mantissa = 5.2 << 15. Note: 1/5 = .0011 repeating
      0x29999,
      ResValue.ComplexFormat.UNIT_FRACTION_PARENT,
      ResValue.ComplexFormat.RADIX_8p15,
      true)
    testStringToFloatCaseFixed(
      "3375%",
      0x10e000,
      ResValue.ComplexFormat.UNIT_FRACTION,
      ResValue.ComplexFormat.RADIX_8p15,
      true)
    // Test some 16p7 fixed point cases
    testStringToFloatCaseFixed(
      "256.5px",
      // 256.5 << 7
      0x8040,
      ResValue.ComplexFormat.UNIT_PX,
      ResValue.ComplexFormat.RADIX_16p7)
    testStringToFloatCaseFixed(
      "-500.125pt",
      // 24 bit 2's complement of 0xfa10
      0xff05f0,
      ResValue.ComplexFormat.UNIT_PT,
      ResValue.ComplexFormat.RADIX_16p7)
    // Min value checks
    testStringToFloatCaseFixed(
      // 1 * 2^(-23)
      "0x1p-23mm",
      1,
      ResValue.ComplexFormat.UNIT_MM,
      ResValue.ComplexFormat.RADIX_0p23)
    // test rounding of fixed point
    testStringToFloatCaseFixed(
      // 1 * 2^(-24) should round up to 1 * 2^(-23)
      "0x1p-24mm",
      1,
      ResValue.ComplexFormat.UNIT_MM,
      ResValue.ComplexFormat.RADIX_0p23)
    testStringToFloatCaseFixed(
      // -1 * 2^(-24)
      "-0x1p-24in",
      -1 and ResValue.ComplexFormat.MANTISSA_MASK,
      ResValue.ComplexFormat.UNIT_IN,
      ResValue.ComplexFormat.RADIX_0p23)
    testStringToFloatCaseFixed(
      "0.2mm",
      0x19999a,
      ResValue.ComplexFormat.UNIT_MM,
      ResValue.ComplexFormat.RADIX_0p23)
    // Test overflow of fixed points.
    testStringToFloatCaseFixed(
      "0x7fffffmm",
      0x7fffff,
      ResValue.ComplexFormat.UNIT_MM,
      ResValue.ComplexFormat.RADIX_23p0)
    // Positive overflow bleeds into negative values
    testStringToFloatCaseFixed(
      "0x1p23pt",
      0x800000,
      ResValue.ComplexFormat.UNIT_PT,
      ResValue.ComplexFormat.RADIX_23p0)
    // Test underflow of fixed points.
    testStringToFloatCaseFixed(
      "-0x800000mm",
      0x800000,
      ResValue.ComplexFormat.UNIT_MM,
      ResValue.ComplexFormat.RADIX_23p0)
    // Negative underflow bleeds into positive values.
    testStringToFloatCaseFixed(
      "-0x800001pt",
      0x7fffff,
      ResValue.ComplexFormat.UNIT_PT,
      ResValue.ComplexFormat.RADIX_23p0)
  }
}
