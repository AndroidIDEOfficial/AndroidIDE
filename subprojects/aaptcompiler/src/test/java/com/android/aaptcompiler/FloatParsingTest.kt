package com.android.aaptcompiler

import com.google.common.truth.Truth
import org.junit.Ignore
import org.junit.Test

class FloatParsingTest {

    @Test
    fun testParseDecFloatZero() {
        testParseDecSupported("0", 0.0.toFloat())
        testParseDecSupported("-0", -(0.0.toFloat()))

        testParseDecSupported("+0.0", 0.0.toFloat())
        testParseDecSupported("0.", 0.0.toFloat())
        testParseDecSupported(".0", 0.0.toFloat())
        testParseDecSupported("0000000000000.0000000000000", 0.0.toFloat())
        testParseDecSupported("-0.0", -(0.0.toFloat()))
        testParseDecSupported("0e0", 0.0.toFloat())
        testParseDecSupported("0e24", 0.0.toFloat())
        testParseDecSupported("0e-14", 0.0.toFloat())

        // Zero's with absurdly large/small exponents should still parse properly.
        testParseDecSupported("0e9001", 0.0.toFloat())
        testParseDecSupported("0e+9001", 0.0.toFloat())
        testParseDecSupported("0e-9001", 0.0.toFloat())
    }

    @Test
    fun testParseHexFloatZero() {
        testParseHexNormal("0x0", 0.0.toFloat())

        testParseHexNormal("+0x0", 0.0.toFloat())

        testParseHexNormal("0x.0", 0.0.toFloat())

        testParseHexNormal("0x0.", 0.0.toFloat())

        testParseHexNormal("0x0000000000.0000000000", 0.0.toFloat())

        testParseHexNormal("-0x0", -(0.0.toFloat()))

        testParseHexNormal("0x0p0", 0.0.toFloat())

        testParseHexNormal("0x0p12", 0.0.toFloat())

        testParseHexNormal("0x0p-14", 0.0.toFloat())

        // Zero's with absurdly large/small exponents should still work.
        testParseHexNormal("0x0p256", 0.0.toFloat())

        testParseHexNormal("0x0p-256", 0.0.toFloat())
    }

    @Test
    fun testParseDecIntegerValue() {
        // Basic Integer Parsing
        testParseDecSupported("10", 10.toFloat())
        testParseDecSupported("12345", 12345.toFloat())
        testParseDecSupported("67890", 67890.toFloat())

        // Super-Integer parsing
        // 2^32 is larger than int max
        testParseDecSupported("4294967296", 4294967296.toFloat())
        testParseDecSupported("-4294967296", (-4294967296).toFloat())

        testParseDecSupported("3.4028235e38", 3.4028235e38.toFloat())
        testParseDecSupported("-3.4028235e38", (-3.4028235e38).toFloat())

        // Anything larger should certainly fail
        testParseDecNotSupported("3.4028236e38", Float.POSITIVE_INFINITY)
        testParseDecNotSupported("-3.4028236e38", Float.NEGATIVE_INFINITY)
    }

    @Test
    fun testParseHexIntegerValue() {
        // Basic Integer Parsing
        testParseHexNormal("0x10", 16.toFloat())

        testParseHexNormal("0x1p4", 16.toFloat())

        testParseHexNormal("0x0123", 0x123.toFloat())

        testParseHexNormal("0x4567", 0x4567.toFloat())

        testParseHexNormal("0x89ab", 0x89ab.toFloat())

        testParseHexNormal("0xcdef", 0xcdef.toFloat())

        // Super-Integer parsing
        // 2^32 is larger than integer max
        testParseHexNormal("0x100000000", 4294967296.toFloat())

        testParseHexNormal("-0x100000000", (-4294967296).toFloat())

        // Max normal float value
        testParseHexNormal("0x1.fffffeP127", 3.4028235e38.toFloat())
        testParseHexNormal("-0x1.fffffeP127", (-3.4028235e38).toFloat())

        // Anything larger should fail
        testParseHexNonnormal("0x1.ffffffP127", Float.POSITIVE_INFINITY)
        testParseHexNonnormal("-0x1.ffffffP127", Float.NEGATIVE_INFINITY)

        // Rounding.
        testParseHexNormal("0x2000001", 0x2000002.toFloat())
        testParseHexNormal("-0x2000001", (-0x2000002).toFloat())
        // This rounding test will assure that the exponent is adjusted properly.
        testParseHexNormal("0x1ffffff", 0x2000000.toFloat())
        testParseHexNormal("-0x1ffffff", (-0x2000000).toFloat())
    }

    @Test
    fun testParseDecSubIntegerValue() {
        // Basic Sub-Integer parsing
        testParseDecSupported(".1", .1.toFloat())

        //testParseDecSupported("2.25", 2.25.toFloat())
        testParseDecSupported("-2.25", (-2.25).toFloat())

        // Minimum normal float value
        testParseDecSupported("1.17549435e-38", 1.17549435e-38.toFloat())
        testParseDecSupported("-1.17549435e-38", (-1.17549435e-38).toFloat())

        // Largest denormal number should fail on decimal parse,
        // i.e. negative = 0x0/0x1 exponent = 0x0 mantissa = 0x7fffff
        testParseDecNotSupported("1.1754942e-38", 1.1754942e-38.toFloat())
        testParseDecNotSupported("-1.1754942e-38", (-1.1754942e-38).toFloat())

        // Tiny numbers are interpreted as denormal by Kotlin and are rounded to 0.0.
        // We do not support those values.
        testParseDecNotSupported("1e-100", 0.00.toFloat())
        testParseDecNotSupported("-1e-100", -(0.00.toFloat()))
    }

    @Test
    fun testParseHexSubIntegerValue() {
        // Basic Sub-Integer parsing
        testParseHexNormal("0x.8", .5.toFloat())

        testParseHexNormal("0x2.4", 2.25.toFloat())

        testParseHexNormal("-0x2.4", (-2.25).toFloat())

        // Minimum normal float value
        testParseHexNormal("0x1p-126", 1.17549435e-38.toFloat())
        testParseHexNormal("-0x1p-126", (-1.17549435e-38).toFloat())

        // Edge case for denormal number rounding with less precision to minimum normal float.
        testParseHexNormal("0x1.fffffeP-127", 1.17549435e-38.toFloat())
        testParseHexNormal("-0x1.fffffeP-127", (-1.17549435e-38).toFloat())

        // Largest denormal number should fail on hex parse,
        // i.e. negative = 0x0/0x1 exponent = 0x0 mantissa = 0x7fffff
        testParseHexNonnormal("0x1.fffffdp-127", 1.1754942e-38.toFloat())
        testParseHexNonnormal("-0x1.fffffdp-127", (-1.1754942e-38).toFloat())

        // Tiny numbers are interpreted as denormal by Kotlin and are rounded to 0.0.
        // We do not support those values.
        testParseHexNonnormal("0x1p-255", 0.0.toFloat())
        testParseHexNonnormal("-0x1p-255", (-0.0).toFloat())
    }

    @Test
    fun testParseDecFailures() {
        testDecFailure(".")
        testDecFailure("-.")
        testDecFailure("+.")

        testDecFailure("+e3")
        testDecFailure("-e-2")

        testDecFailure("125e")
        testDecFailure("-125e")
        testDecFailure("+125e")

        testDecFailure("e")
        testDecFailure("-e")

        testDecFailure("1.2.3")
        testDecFailure("-1.2.3")

        testDecFailure("+-12")
        testDecFailure("--0")
        testDecFailure("++42")
    }

    @Test
    fun testParseHexFailures() {
        testHexFailure("0x.")
        testHexFailure("-0x.")

        testHexFailure("0xp")
        testHexFailure("-0xp")

        testHexFailure("0xp0")
        testHexFailure("-0xp0")

        testHexFailure("0x12p")
        testHexFailure("-0x12p")

        testHexFailure("0x1.2.1")
        testHexFailure("-0x1.2.1")

        testHexFailure("0x1.2.1p0")
        testHexFailure("-0x1.2.1p0")

        testHexFailure("--0x0")

        testHexFailure("+-0x0p0")

        testHexFailure("-+0x0p0")

        testHexFailure("++0x0p0")
    }

    /**
     * Round trip checks all supported float values.
     *
     * A round trip of a floating point value, goes as follows:
     *
     * Take a floating point number, in it's binary format.
     *
     * Call [Float.toString] getting the string representation.
     *
     * Call [parseFloat] on the string representation.
     *
     * Ensure that the initial float and it's parsed result, are the same.
     *
     * This tests goes over ~2 billion different float values, and takes ~1 hour for full
     * computation. As such this test is set to be ignored, but should be enabled if float parsing
     * is modified significantly.
     */
    @Test
    @Ignore("b/303076368")
    fun roundTrip() {
        var currentVal = 0x00800000
        val maxVal = 0x7f000000
        var err = ""
        var numErrors = 0
        while (currentVal < maxVal) {
            val float = Float.fromBits(currentVal)
            val string = float.toString()
            val parse = parseFloat(string)
            if (float != parse) {
                if (numErrors < 1000) {
                    err += "expected: $float actual: $parse \n"
                    ++numErrors
                } else if (numErrors == 1000) {
                    err += "and more...\n"
                    break
                }
            }
            ++currentVal
        }
        if (err.isNotEmpty()) {
            error(err)
        }
    }

    private fun needsModificationForKotlin(hexText: String): Boolean {
        return !hexText.lowercase().contains('p') && hexText.all {
            when (it) {
                in '0'..'9' -> true
                in 'a'..'f' -> true
                in 'A'..'F' -> true
                'x', 'X' -> true
                '.', '-', '+' -> true
                else -> false
            }
        }
    }

    private fun testParseDecSupported(text: String, expected: Float) {
        val parsedFloat = parseFloat(text)
        Truth.assertThat(parsedFloat!!.toRawBits().toString(16))
            .isEqualTo(expected.toRawBits().toString(16))
        Truth.assertThat(parsedFloat).isEqualTo(text.toFloat())
    }

    private fun testParseDecNotSupported(text: String, expected: Float) {
        val parsedFloat = parseFloat(text)
        Truth.assertThat(parsedFloat).isNull()
        Truth.assertThat(text.toFloat()).isEqualTo(expected)
    }

    private fun testDecFailure(text: String) {
        Truth.assertThat(parseFloat(text)).isNull()
        Truth.assertThat(text.toFloatOrNull()).isNull()
    }

    /**
     * Tests a parseHex value that is expected to succeed. This must have the same result as the
     * String.toFloat() method, with the possible addition of a "p0" to the text to make it compatible
     * with Kotlin's toFloat method.
     *
     * Specifically, the hex parsing functionality works for all "normal" floating point numbers.
     * (i.e.) floating point values that do not have a special value in the exponent to force
     **/
    private fun testParseHexNormal(text: String, expected: Float) {
        val parsedFloat = parseFloat(text)
        Truth.assertThat(parsedFloat).isEqualTo(expected)
        // We need to append p0 for default float parsing that doesn't already have an exponent marker.
        // As this is how Kotlin does it normally.
        val modifiedText = if (needsModificationForKotlin(text)) text + "p0" else text
        Truth.assertThat(parsedFloat).isEqualTo(modifiedText.toFloat())
    }

    /**
     * Tests a parseHex value with a subnormal (denormal) or special values. parseFloat() should fail
     * in these cases, but the text (with the possible addition of "p0") should succeed when parsing
     * normally in Kotlin's String.toFloat() method.
     */
    private fun testParseHexNonnormal(text: String, expected: Float) {
        val parsedFloat = parseFloat(text)
        Truth.assertThat(parsedFloat).isNull()

        val modifiedText = if (needsModificationForKotlin(text)) text + "p0" else text
        if (expected.isNaN()) {
            Truth.assertThat(modifiedText.toFloat()).isNaN()
        } else {
            Truth.assertThat(modifiedText.toFloat()).isEqualTo(expected)
        }
    }

    /**
     * Tests a failure to parse and ensures Kotlin has the same behavior.
     */
    private fun testHexFailure(text: String) {
        Truth.assertThat(parseFloat(text)).isNull()
        val modifiedText = if (needsModificationForKotlin(text)) text + "p0" else text
        Truth.assertThat(modifiedText.toFloatOrNull()).isNull()
    }


}
