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

package com.android.aaptcompiler

/**
 * Parses the float in a faster way than String.toFloat. Returns null on failure.
 *
 * Handles both decimal floats (ex. 3.14e0) and Hexadecimal floats (0x3.d2fp0).
 * Does not handle denormal values (values that exist between 1.1754942e-38 and 0 which require
 * special formatting.
 *
 * Also does not handle special values (positive/negative infinity, NAN, etc.)
 * In other words, if this method returns null, it does not necessarily imply that the string is not
 * a valid float value.
 *
 * Some float values are considered to be valid with [parseFloat] which are not considered valid
 * with [String.toFloat]. Specifically, hexadecimal floating point values do not require a exponent
 * marker. That is:
 *
 * 0x1.2p0 is valid for both [parseFloat] and [String.toFloat]
 *
 * 0x1.2 is valid for [parseFloat] but not [String.toFloat]
 *
 * @param text the text to be translated into a float value.
 * @return The float representation of text, or null if the float could not be parsed.
 */
fun parseFloat(text: String): Float? {
  if (text.isEmpty()) return null

  val lowerText = text.lowercase()
  if (lowerText.startsWith("0x") || lowerText.startsWith("0x", 1)) {
    return parseFloatHex(lowerText)
  }
  return parseFloatDec(lowerText)
}

/**
 *  Gets the significand equivalent to the value 1 x 10^exponent. Used in decimal float parsing.
 *
 *  This selects the correct index from either the [positiveSignificands] or [negativeSignificands].
 *  If the value is out of range 0L is returned.
 *
 *  The power of 10 is represented as:
 *  [getValue] * 2^[getShift]
 *  where the significand is fixed point with 59 bits of precision.
 *  @see getShift
 *
 *  @param exponent the power of 10 that the significand represents.
 *  @return the correct significand or 0L if the exponent is out of bounds.
 */
private fun getValue(exponent: Int) =
  when {
    exponent > positiveSignificands.size -> 0L
    exponent >= 0 -> positiveSignificands[exponent]
    exponent >= -negativeSignificands.size -> negativeSignificands[-exponent - 1]
    else -> 0L
  }

/**
 * Gets the binary exponent associated with the value 1 x 10^exponent. Used in decimal float
 * parsing.
 *
 * This selects the correct index from either the [positiveShifts] or [negativeShifts]. If the value
 * is out of range 0 is returned.
 *
 * The power of 10 is represented as:
 *   [getValue] * 2^[getShift]
 * where the significand is fixed point with 59 bits of precision.
 * @see getShift
 *
 * @param exponent the power of 10, which corresponds the shift requested.
 * @return the correct binary shift or 0 if the exponent is out of bounds.
 */
private fun getShift(exponent: Int) =
  when {
    exponent > positiveSignificands.size -> 0
    exponent >= 0 -> positiveShifts[exponent]
    exponent >= -negativeSignificands.size -> negativeShifts[-exponent - 1]
    else -> 0
  }

/**
 * Returns the shift that would normalize the given value to proper significand format. Used in
 * decimal float parsing.
 *
 * This method is used to normalize a significand multiplied by a scalar, by returning how many bits
 * the value should be shifted in order to have the significant bit in the 59th position.
 *
 * @param valueToNormalize: The significand, after multiplication by a scalar, to be
 * re-normalized.
 * @return the amount the significand should be downshifted, as well as the amount the corresponding
 * binary shift should be increased. For example (all significands are in binary):
 *
 *     100.1 * 2^12
 * Normalizes to:
 *     1.001 * 2^14
 * So the getScalarShift(100.1) -> 2
 */
private fun getScalarShift(valueToNormalize: Long) =
  when {
    (valueToNormalize and SCALAR_SHIFT_4) != 0L -> 4
    (valueToNormalize and SCALAR_SHIFT_3) != 0L -> 3
    (valueToNormalize and SCALAR_SHIFT_2) != 0L -> 2
    (valueToNormalize and SCALAR_SHIFT_1) != 0L -> 1
    else -> 0
  }

/**
 * Parse the decimal text as a 32-bit floating point number.
 *
 * Mechanically this is done in a number of steps. We'll use "03.01e1" as an example.
 *
 * First, find the exponent of the leading significant figure. The leading zero is ignored and
 * therefore 3 is the leading significant figure. Although the 3 is in the one's place, the "e1"
 * means that it is actually representing the ten's place. I.e. 3.01e1 = 30.1. So the exponent is 1.
 *
 * Second, we compute the significand and shift of the leading figure.
 *
 *      1.010 * 2^3   (10^1 in binary)
 *     *    3         (leading figure in decimal)
 *     =  11.110 * 2^3   (significand in binary)
 *
 *     (normalize the significand):
 *     11.110 * 2^3
 *     = 1.1110 * 2^4 (1)
 *
 *  Third go through each decimal and add its significand (shifted relative to the significand of
 *  the leading figure) to the running total. For example:
 *
 *     0 in 3.01e1 does not contribute to the significand, so it is skipped.
 *
 *     1 in 3.01e1 represents the tenth's place, So compute the significand.
 *
 *       1.100_1100_ * 2^-4   (1/10 in binary, the _1100_ means repeating ad infinitum)
 *     *           1          (the value in the tenth's place in decimal)
 *     = 1.100_1100_ * 2^-4   (significand of the tenth's place in binary)
 *
 *     (normalization is skipped because it is already normalized)
 *
 *     (make the significand align with the shift of the leading figure in (1)):
 *     1.100_1100_ * 2^-4
 *     = .0000000_1100_ * 2^4 (2)
 *
 *     (Add to the accumulated significand)
 *       1.1110            * 2^4 (The significand so far in binary from (1))
 *     + 0.0000000_1100_   * 2^4 (The aligned tenth's significand in binary from (2))
 *     = 1.1110000_1100_   * 2^4 (The accumulated significand) (3)
 *
 *     (normalization is skipped, as the sum is already normalized.)
 *
 *  Fourth and finally, convert the accumulated significand to IEEE Floating Point format:
 *
 *       1.1110000_1100_ * 2^4 (from (3))
 *
 *     (cut the leading bit in the significand as it is assumed in the format):
 *          1.1110000_1100_
 *        -> .1110000_1100_
 *
 *     (limit to 23 bits of precision (rounded)):
 *           .1110000_1100_
 *        -> .11100001100110011001101 (significand in floating point format)
 *
 *     (add the required bias to the exponent, as specified by the format):
 *            4  (exponent of significand)
 *        + 127  (bias)
 *        = 131  (biased exponent)
 *        = 10000011 (in binary) (biased exponent in floating point format)
 *
 *     (set the sign bit if the float was negative):
 *       0  (0 means positive float)
 *
 *     (combine the bits. sign - biased exponent - significand)
 *       0  10000011  11100001100110011001101
 *
 * @param lowerCaseText: The string representing the float in lower case. The float should be of the
 * form:
 *
 *    [+/-]XXXXX[.]XXXXX[e[+/-]XXXXX]
 *
 *    Where XXXXX represents zero or more digits.
 *    1. Both the decimal (.) and the specified exponent (eXXXXX) is optional.
 *    2. At least one digit is required that is not part of the specified exponent. This digit may
 *    be before or after the decimal. I.e.
 *      "1.e12" is okay.
 *      "1." is okay
 *      "-.0" is okay
 *      ".2e-2" is okay.
 *      "3.14" is okay.
 *      "15" is okay.
 *      "e12" is not okay.
 *      "." is not okay.
 *    3. An optional sign ("+" or "-") can come before the significand and/or the exponent value.
 *    I.e.
 *      "+12" is okay
 *      "5e-2" is okay
 *      "-2.1e+0" is okay.
 *      "++2" is not okay.
 *      "4e+-4" is not okay.
 *
 * If the float is not well formed, the returned result will be null.
 *
 * If the float has a magnitude less than 1.1754942e-38 and is not zero, then null is returned
 * (These values are called *denormal* and are not supported).
 *
 * If the float has a magnitude greater than 3.4028235e38, then null is returned (This would
 * return +/- Infinity on [String.toFloat] which is not supported by this parser)
 *
 * Lastly, values such as "+Infinity", "-Infinity", "NaN", or other *special non-number* cases are
 * not supported and will return null.
 *
 * @return The float value closest to the value represented by the text (within margin of error).
 * If the text is malformed or the text represents a value that is not supported, null is returned
 * instead.
 */
fun parseFloatDec(lowerCaseText: String): Float? {
  // Save this for formatting later.
  val negative = lowerCaseText[0] == '-'

  // move past the sign if any.
  var currentIndex = if (lowerCaseText[0] == '+' || negative) 1 else 0

  // First step of parsing a float is to find the significant digit, and what power of 10 this
  // represents. To start this, we run through the significand to find the first
  // non-zero digit and its position relative to the decimal.
  // Ex.:
  // 1.3 -> 0
  // 0.12 -> -1
  // 0000111.14 -> 2
  // 142 -> 2 (decimal is implicitly at the end of the number.)
  // In auxiliary, we do 2 things during this step.
  // 1. verify that the significand is in a valid format.
  // 2. Save the position of the most significant figure for the computation step.
  var significandBegin = currentIndex
  var significandStart = false
  var validSignificand = false
  var subInteger = false
  var mostSignificantExponent = 0

  // If we hit the exponent marker 'e', we are done with the significand.
  while (currentIndex < lowerCaseText.length &&
    lowerCaseText[currentIndex] != DEC_EXPONENT_MARKER) {

    // Hitting a decimal, means we've move into sub-integer positions.
    if (lowerCaseText[currentIndex] == '.') {
      if (subInteger) {
        // hit second decimal, which is not allowed.
        return null
      } else {
        subInteger = true
        ++currentIndex
        continue
      }
    }

    if (significandStart && !subInteger) {
      ++mostSignificantExponent
    } else if (!significandStart && subInteger) {
      --mostSignificantExponent
    }

    when (lowerCaseText[currentIndex]) {
      in '1'..'9' -> {
        validSignificand = true
        if (!significandStart) {
          significandStart = true
          // Found the beginning of the significand.
          significandBegin = currentIndex
        }
      }
      '0' -> validSignificand = true
      else -> return null
    }

    ++currentIndex
  }

  val significandEnd = currentIndex

  // We never hit a digit in the significand. E.x. "+.e12"
  if (!validSignificand) {
    return null
  }

  // Now that we have what the digit represents in the significand. We need to adjust this value
  // based on what is in the specified exponent (the portion after the 'e')
  // So we need to compute the exponent and adjust the exponent of the significant figure
  // appropriately. For example:
  //
  // "299.792458e+6"
  // has a significant digit of '2' in the hundred's position (significandExponent = 2)
  //
  // The "e+6" has a specified exponent of 6 (declaredExponent = 6)
  // So the '2' actually represents the hundred-million's position  (significandExponent = 8)
  //
  // In auxiliary, we validate that the declared exponent is a valid integer.
  var declaredExponent = 0
  if (currentIndex < lowerCaseText.length) {
    // The float should not end in the exponent marker. I.e. "244e"
    if (currentIndex == lowerCaseText.length - 1) {
      return null
    }
    ++currentIndex
    val negativeExponent = lowerCaseText[currentIndex] == '-'
    if (negativeExponent || lowerCaseText[currentIndex] == '+') {
      ++currentIndex
    }

    while (currentIndex < lowerCaseText.length) {
      val currentValue = when (lowerCaseText[currentIndex]) {
        in '0'..'9' -> lowerCaseText[currentIndex] - '0'
        else -> return null
      }
      declaredExponent = declaredExponent*10 + currentValue
      ++currentIndex
    }

    if (negativeExponent) declaredExponent = -declaredExponent
  }

  // Since we've validated the format of the whole float, we can now handle the case of
  // a significand with no significant figure (all zeros).
  if (!significandStart) {
    return if (negative) -(0.0.toFloat()) else 0.0.toFloat()
  }

  mostSignificantExponent += declaredExponent

  // Now we begin step 2: computing the value of the significant figure.
  currentIndex = significandBegin

  // gather the initial value and shift from the significant figure.
  var significandValue = getValue(mostSignificantExponent)
  var baseBinaryShift = getShift(mostSignificantExponent)

  // If the significant figure is out-of-range, we fail.
  if (significandValue == 0L) {
    return null
  }

  // Multiply by the declared scalar and re-normalize the significand.
  significandValue *= lowerCaseText[currentIndex] - '0'
  val significandScalarShift = getScalarShift(significandValue)

  significandValue = significandValue ushr significandScalarShift
  baseBinaryShift += significandScalarShift

  var currentExponent = mostSignificantExponent - 1
  ++currentIndex

  // Third go through each decimal and add its significand (shifted relative to the significand of
  // the leading figure) to the running total.
  compute@ while (currentIndex < significandEnd) {
    val scalarValue = when (lowerCaseText[currentIndex]) {
      in '1'..'9'-> (lowerCaseText[currentIndex] - '0')
      '0' -> {
        ++currentIndex
        --currentExponent
        continue@compute
      }
      // We hit the decimal, which we don't need it anymore.
      else -> {
        ++currentIndex
        continue@compute
      }
    }

    var currentValue = getValue(currentExponent) *scalarValue
    var relativeDownShift = baseBinaryShift - getShift(currentExponent)

    // make sure to re-normalize the significand before shifting.
    val scalarShift = getScalarShift(currentValue)
    currentValue = currentValue ushr scalarShift
    relativeDownShift -= scalarShift

    // control, no longer care about new decimals because the precision is too small to change
    // the significand.
    if (relativeDownShift > 59) {
      break@compute
    }

    significandValue += currentValue ushr relativeDownShift

    // Handle overflow from addition,
    if ((significandValue and SCALAR_SHIFT_1) != 0L) {
      significandValue = significandValue ushr 1
      baseBinaryShift += 1
    }

    --currentExponent
    ++currentIndex

  }

  //Finally convert the Information into single precision floating point format.
  // round the significand
  significandValue += ROUND_VALUE
  // handle overflow
  if ((significandValue and SCALAR_SHIFT_1) != 0L) {
    significandValue = significandValue ushr 1
    baseBinaryShift += 1
  }

  // Handle the cases where the binary exponent is out-of-bounds.
  if (baseBinaryShift < SINGLE_PRECISION_EXP_MIN || baseBinaryShift > SINGLE_PRECISION_EXP_MAX ) {
    return null
  }

  // Trim off the front bit and shift down to floating point precision for standard format.
  val mantissa =
    (significandValue and DEC_SIGNIFICAND_LEADING_BIT.inv()).ushr(
      DEC_SIGNIFICAND_DOWN_SHIFT).toInt()

  // Bias the exponent
  val biasedExp = baseBinaryShift + SINGLE_PRECISION_BIAS
  return Float.fromBits(
    (if (negative) FLOAT_NEGATIVE_MASK else 0) or mantissa or (biasedExp shl 23))
}

/**
 * Parse the hexadecimal text as a 32 floating point number.
 *
 * Mechanically this is done in three steps. We'll use "-0x3a.1p+5"
 *
 * First, process the significand, to acquire 2 pieces of information.
 *     1. The normalized value of the significand. Unlike in decimal parsing, we can compute the
 *     precision exactly in base16. Because of this, we only need 24 bits (+1 for rounding) for the
 *     significand.
 *
 *          3   a.   1
 *     = 00111010.0001   (3a.1 in binary)
 *     = 1.110100001     (normalized significand)
 *
 *     2. the binary offset of the leading bit of the significand. Basically how much the decimal
 *     needs to be shifted left for this significant figure to be in the one's position.
 *
 *         111010.0001 << 5
 *       = 1.110100001      (1)
 *
 * Second, compute the signed specified exponent (ex. "p+5") and add it to the computed offset.
 *
 *     "p+5" => 5
 *       5  (specified exponent)
 *     + 5  (exponent of leading bit)
 *     = 10 (total shift.)       (2)
 *
 * Third and finally, convert the significand and shift to IEEE floating point format:
 *
 *     1.110100001 * 2^10     (from (1) and (2))
 *
 *     (cut the leading bit in the significand as it is assumed in the format):
 *          1.110100001
 *        -> .110100001
 *
 *     (limit to 23 bits of precision (rounded)):
 *           .110100001
 *        -> .1101000010000000000000 (significand in floating point format)
 *
 *     (add the required bias to the exponent as specified by the format):
 *           10  (exponent of significand)
 *        + 127  (bias)
 *        = 137  (biased exponent)
 *        = 10001001 (in binary) (biased exponent in floating point format)
 *
 *    (set the sign bit if the float was negative):
 *       1  (1 means negative float)
 *
 *    (combine the bits. sign - biased exponent - significand)
 *       1  10001001  1101000010000000000000
 *
 * @param lowerCaseText: The string representing the float in lower case. The float should be of the
 * form:
 *
 *     [+/-]0xYYYYY[.]YYYYY[p[+/-]XXXXX]
 *
 *     Where XXXXX represents 0 or more decimal digits, and YYYYY represents 0 or more hexadecimal
 *     digits.
 *     1. The significand must be lead by "0x".
 *     2. Both the decimal (.) and the specified exponent (pXXXXX) is optional.
 *     3. At least one digit is required that is not part of the specified exponent. This digit may
 *     be before or after the decimal. I.e.
 *       "0x.8p-4" is okay.
 *       "0x1." is okay
 *       "-0x.0" is okay
 *       "0x.ap-2" is okay.
 *       "0x3.ef" is okay.
 *       "0xf" is okay.
 *       "0xp12" is not okay.
 *       "0x." is not okay.
 *       "0x" is not okay.
 *     4. An optional sign ("+" or "-") can come before the significand and/or the exponent value.
 *     I.e.
 *       "+0xa" is okay
 *       "0x1ce-3" is okay
 *       "-0x2.8p+10" is okay.
 *       "++0xa" is not okay.
 *       "0x4p+-19" is not okay.
 *
 * If the float is not well formed, the returned result will be null.
 *
 * If the float has a magnitude less than 0x1.fffffep-127 and is not zero, then null is returned
 * (These values are called *denormal* and are not supported).
 * Note: 0x1.fffffep-127 rounds up to 1p-126, which is the minimum normal value.
 *
 * If the float has a magnitude greater than 1.fffffep127, then null is returned (This would
 * return +/- Infinity on [String.toFloat] which is not supported by this parser)
 *
 * Lastly, values such as "+Infinity", "-Infinity", "NaN", or other *special non-number* cases are
 * not supported and will return null.
 *
 * @return The float value closest to the value represented by the text (within margin of error).
 * If the text is malformed or the text represents a value that is not supported, null is returned
 * instead.
 */
fun parseFloatHex(lowerCaseText: String): Float? {
  // Save for formatting later.
  val negative = lowerCaseText[0] == '-'

  val signed = lowerCaseText[0] == '+' || negative

  // move past the sign and the '0x'
  var currentIndex = if (signed) 3 else 2

  if (lowerCaseText.substring(currentIndex - 2, currentIndex) != "0x") {
    return null
  }

  // First step of parsing the float is to normalizing the significand.
  // That is:
  // abcd -> 1.579a * 2^11
  // 1 -> 1. * 2^0
  // a -> 1.4 * 2^3
  // Technically, we compute the mantissa (everything after the leading bit of the significand).
  var currentMantissa = 0
  var currentSkew = 0
  var mantissaBits = 0
  var mantissaStart = false
  var subInteger = false
  var validMantissa = false

  // Compute the mantissa of the float.
  mantissa@ while (currentIndex < lowerCaseText.length &&
    lowerCaseText[currentIndex] != HEX_EXPONENT_MARKER) {

    val indexValue = when (lowerCaseText[currentIndex]) {
      in '0'..'9' -> {
        validMantissa = true
        (lowerCaseText[currentIndex] - '0')
      }
      in 'a'..'f' -> {
        validMantissa = true
        (lowerCaseText[currentIndex] - 'a') + 10
      }
      '.' -> {
        if (subInteger) {
          // hit 2 decimal points in the mantissa, which is not allowed.
          return null
        } else {
          // Hit first decimal point. So we've gone subinteger.
          subInteger = true
          ++currentIndex
          continue@mantissa
        }
      }
      // Invalid character.
      else -> return null
    }

    // Start the mantissa, recording how many bits are required.
    if (!mantissaStart && indexValue !=0) {
      mantissaStart = true
      currentMantissa = indexValue
      // The number of bits in the mantissa is decided by the leading bit in the hex value.
      when {
        indexValue >= 8 -> {
          currentSkew -= 1
          mantissaBits = 3
        }
        indexValue >= 4 -> {
          currentSkew -= 2
          mantissaBits = 2
        }
        indexValue >= 2 -> {
          currentSkew -= 3
          mantissaBits = 1
        }
        else -> {
          currentSkew -= 4
          mantissaBits = 0
        }
      }
    // In the case that mantissa has been started and we can acquire more precision, do so.
    } else if (mantissaStart && mantissaBits < 24) {
      currentMantissa = (currentMantissa shl 4) + indexValue
      mantissaBits += 4
    }

    // Even though we can't get more precise than 23 bits (with 1 extra bit for rounding),
    // we still need to search for the exponent marker and adjust the skew.
    // Adjust the skew of the mantissa.
    if (mantissaStart && !subInteger) {
      currentSkew += 4
    } else if (!mantissaStart && subInteger) {
      currentSkew -= 4
    }

    ++currentIndex
  }

  if (!validMantissa) {
    return null
  }

  // Part 2, Solve the for the declared exponent if the exponent marker exists.
  var declaredExponent = 0
  if (currentIndex < lowerCaseText.length) {
    // The float should not end in the exponent marker.
    if (currentIndex == lowerCaseText.length - 1) {
      return null
    }
    ++currentIndex
    val negativeExponent = lowerCaseText[currentIndex] == '-'
    if (negativeExponent || lowerCaseText[currentIndex] == '+') {
      ++currentIndex
    }

    while (currentIndex < lowerCaseText.length) {
      val currentValue = when (lowerCaseText[currentIndex]) {
        in '0'..'9' -> lowerCaseText[currentIndex] - '0'
        else -> return null
      }
      declaredExponent = declaredExponent*10 + currentValue
      ++currentIndex
    }

    if (negativeExponent) declaredExponent = -declaredExponent
  }

  // Align the mantissa
  // MantissaBits may be greater than or less than 24 bits, so we need to adjust.
  currentMantissa = if (24 - mantissaBits < 0) {
    currentMantissa ushr (mantissaBits - 24)
  } else {
    currentMantissa shl (24 - mantissaBits)
  }
  // Remove the leading 1
  currentMantissa = currentMantissa and MANTISSA_EXPONENT_ADJUST_VALUE

  // Compute the exponent, taking care of rounding for the mantissa.
  val exponent =
    when {
      (currentMantissa == MANTISSA_EXPONENT_ADJUST_VALUE) -> {
        // mantissa overflowed after rounding.
        currentMantissa = 0
        declaredExponent + currentSkew + 1
      }
      (currentMantissa == DENORMAL_EXPONENT_ADJUST_VALUE &&
              declaredExponent + currentSkew + 1 == SINGLE_PRECISION_EXP_MIN) -> {
        currentMantissa = 0
        declaredExponent + currentSkew + 1
      }
      else -> {
        currentMantissa = ((currentMantissa + 1) ushr 1)
        declaredExponent + currentSkew
      }
    }

  // Sanity checks
  // Zero and negative zero are special cases
  if (!mantissaStart) return if (negative) -(0.0.toFloat()) else 0.0.toFloat()

  // Bounds check the exponent
  if (exponent < SINGLE_PRECISION_EXP_MIN || exponent > SINGLE_PRECISION_EXP_MAX) {
    return null
  }

  // Finally, put the floating point data into standard format.
  // Bias the exponent
  val biasedExp = exponent + SINGLE_PRECISION_BIAS
  return Float.fromBits(
    (if (negative) FLOAT_NEGATIVE_MASK else 0) or currentMantissa or (biasedExp shl 23))
}
