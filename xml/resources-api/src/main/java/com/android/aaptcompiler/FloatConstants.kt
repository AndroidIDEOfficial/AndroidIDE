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
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.aaptcompiler

internal const val DEC_EXPONENT_MARKER = 'e'
internal const val HEX_EXPONENT_MARKER = 'p'
internal const val SINGLE_PRECISION_BIAS = 127
internal const val SINGLE_PRECISION_EXP_MIN = -126
internal const val SINGLE_PRECISION_EXP_MAX = 127
internal const val FLOAT_NEGATIVE_MASK = 0x80000000.toInt()
internal const val MANTISSA_EXPONENT_ADJUST_VALUE = 0xffffff
internal const val DENORMAL_EXPONENT_ADJUST_VALUE = MANTISSA_EXPONENT_ADJUST_VALUE - 1

internal const val SCALAR_SHIFT_1 = 1L shl 60
internal const val SCALAR_SHIFT_2 = 1L shl 61
internal const val SCALAR_SHIFT_3 = 1L shl 62
internal const val SCALAR_SHIFT_4 = 1L shl 63
internal const val DEC_SIGNIFICAND_LEADING_BIT = 1L shl 59
internal const val DEC_SIGNIFICAND_DOWN_SHIFT = 36
internal const val ROUND_VALUE = 1L shl 35

/**
 * The significands of all powers of ten from 10^-1 to 10^-47 in that order. This is used in decimal
 * parsing.
 *
 * The format of the significand is a fixed point where the 59th bit is the one's place, and the
 * bits [58:0] represent the basic fraction. The 59th bit is guaranteed to be set in this format
 * and is the highest order set bit.
 *
 * For example the significand of 10^-1 is 0x0ccccccccccccccc which in significand format would be:
 *
 * 1.100110011001100110011001100...
 *
 * The associated power of 2 which would multiply this significand to get the actual value is in the
 * [negativeShifts] array.
 *
 * The reason why the 59th bit was chosen to be the most significant bit is that the significand can
 * be safely multiplied by 9 (the highest digit value) without creating a overflow. This allows for
 * us to take the advantage of Long precision while still allowing computation to be quick.
 *
 * Long values are used has they have an overwhelmingly higher precision than floats guaranteeing
 * a lower margin of error.
 *
 * These values were generated.
 */
internal val negativeSignificands = arrayOf(
    0x0ccccccccccccccc,
    0x0a3d70a3d70a3d70,
    0x083126e978d4fdf3,
    0x0d1b71758e219652,
    0x0a7c5ac471b47842,
    0x08637bd05af6c69b,
    0x0d6bf94d5e57a42b,
    0x0abcc77118461cef,
    0x089705f4136b4a59,
    0x0dbe6fecebdedd5b,
    0x0afebff0bcb24aaf,
    0x08cbccc096f5088c,
    0x0e12e13424bb40e1,
    0x0b424dc35095cd80,
    0x0901d7cf73ab0acd,
    0x0e69594bec44de15,
    0x0b877aa3236a4b44,
    0x09392ee8e921d5d0,
    0x0ec1e4a7db69561a,
    0x0bce5086492111ae,
    0x0971da05074da7be,
    0x0f1c90080baf72cb,
    0x0c16d9a0095928a2,
    0x09abe14cd44753b5,
    0x0f79687aed3eec55,
    0x0c612062576589dd,
    0x09e74d1b791e07e4,
    0x0fd87b5f28300ca0,
    0x0cad2f7f5359a3b3,
    0x0a2425ff75e14fc3,
    0x081ceb32c4b43fcf,
    0x0cfb11ead453994b,
    0x0a6274bbdd0fadd6,
    0x084ec3c97da624ab,
    0x0d4ad2dbfc3d0778,
    0x0aa242499697392d,
    0x0881cea14545c757,
    0x0d9c7dced53c7225,
    0x0ae397d8aa96c1b7,
    0x08b61313bbabce2c,
    0x0df01e85f912e37a,
    0x0b267ed1940f1c61,
    0x08eb98a7a9a5b04e,
    0x0e45c10c42a2b3b0,
    0x0b6b00d69bb55c8d,
    0x09226712162ab070,
    0x0e9d71b689dde71a
)

/**
 * The array of all binary shifts of the powers of ten from 10^-1 to 10^-47 in that order. This is
 * used in decimal parsing.
 *
 * The shift represents the power of 2 associated with the significand in the [negativeSignificands]
 * array. That is to say the value of a power of ten that is negative can be represented as:
 *
 *     negativeSignificands[n] * 2^(negativeShifts[n])
 *
 * For example 10^-1 significand is 1.100_1100_ where the _1100_ is infinitely repeating, and the
 * negative shift is -4. Thus:
 *
 *     1.100_1100_ * 2^-4 = .0_0011_ = 10^-1
 *
 * These values were generated.
 */
internal val negativeShifts = arrayOf(
    -4,
    -7,
    -10,
    -14,
    -17,
    -20,
    -24,
    -27,
    -30,
    -34,
    -37,
    -40,
    -44,
    -47,
    -50,
    -54,
    -57,
    -60,
    -64,
    -67,
    -70,
    -74,
    -77,
    -80,
    -84,
    -87,
    -90,
    -94,
    -97,
    -100,
    -103,
    -107,
    -110,
    -113,
    -117,
    -120,
    -123,
    -127,
    -130,
    -133,
    -137,
    -140,
    -143,
    -147,
    -150,
    -153,
    -157
)
/**
 * The significands of all powers of ten from 10^1 to 10^38 in that order. This is used in decimal
 * parsing.
 *
 * The format of the significand is a fixed point where the 59th bit is the one's place, and the
 * bits [58:0] represent the basic fraction. The 59th bit is guaranteed to be set in this format
 * and is the highest order set bit.
 *
 * For example the significand of 10^3 is 0x0c80000000000000, which in significand format would be:
 *
 * 1.100100..00
 *
 * Note: 1100100 in binary is equal to 100 in decimal.
 *
 * The associated power of 2 which would multiply this significand to get the actual value is in the
 * [positiveShifts] array.
 *
 * The reason why the 59th bit was chosen to be the most significant bit is that the significand can
 * be safely multiplied by 9 (the highest digit value) without creating a overflow. This allows for
 * us to take the advantage of Long precision while still allowing computation to be quick.
 *
 * Long values are used as they have an overwhelmingly higher precision than floats guaranteeing
 * a lower margin of error.
 *
 * These values were generated.
 */
internal val positiveSignificands = arrayOf(
    0x0800000000000000,
    0x0a00000000000000,
    0x0c80000000000000,
    0x0fa0000000000000,
    0x09c4000000000000,
    0x0c35000000000000,
    0x0f42400000000000,
    0x0989680000000000,
    0x0bebc20000000000,
    0x0ee6b28000000000,
    0x09502f9000000000,
    0x0ba43b7400000000,
    0x0e8d4a5100000000,
    0x09184e72a0000000,
    0x0b5e620f48000000,
    0x0e35fa931a000000,
    0x08e1bc9bf0400000,
    0x0b1a2bc2ec500000,
    0x0de0b6b3a7640000,
    0x08ac7230489e8000,
    0x0ad78ebc5ac62000,
    0x0d8d726b7177a800,
    0x0878678326eac900,
    0x0a968163f0a57b40,
    0x0d3c21bcecceda10,
    0x084595161401484a,
    0x0a56fa5b99019a5c,
    0x0cecb8f27f4200f3,
    0x0813f3978f894098,
    0x0a18f07d736b90be,
    0x0c9f2c9cd04674ed,
    0x0fc6f7c404581229,
    0x09dc5ada82b70b59,
    0x0c5371912364ce30,
    0x0f684df56c3e01bc,
    0x09a130b963a6c115,
    0x0c097ce7bc90715b,
    0x0f0bdc21abb48db2,
    0x096769950b50d88f
)

/**
 * The array of all binary shifts of the powers of ten from 10^0 to 10^38 in that order. This is
 * used in decimal parsing.
 *
 * The shift represents the power of 2 associated with the significand in the [positiveSignificands]
 * array. That is to say the value of a power of ten that is positive can be represented as:
 *
 *     positiveSignificands[n] * 2^(positiveShifts[n])
 *
 * For example 10^3 significand is 1.1001 and the positive shift = 6
 *
 *     1.1001 * 10^6 = 1100100 = 64 + 32 + 4 = 100 (base ten) = 10^2
 *
 * These values were generated.
 */
internal val positiveShifts = arrayOf(
    0,
    3,
    6,
    9,
    13,
    16,
    19,
    23,
    26,
    29,
    33,
    36,
    39,
    43,
    46,
    49,
    53,
    56,
    59,
    63,
    66,
    69,
    73,
    76,
    79,
    83,
    86,
    89,
    93,
    96,
    99,
    102,
    106,
    109,
    112,
    116,
    119,
    122,
    126
)