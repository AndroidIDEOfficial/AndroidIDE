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

import java.nio.ByteOrder

private val littleEndian = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN

private val DEBUG = false

/**
 * Changes the byte order of the short (on device) to the byte order of the host.
 */
fun Short.deviceToHost() = if (littleEndian) this else java.lang.Short.reverseBytes(this)

/**
 * Changes the byte order of the char on device to the byte order of the host.
 */
fun Char.deviceToHost() = if (littleEndian) this else java.lang.Character.reverseBytes(this)

/**
 * Changes the byte order of the int on device to the byte order of the host.
 */
fun Int.deviceToHost() = if (littleEndian) this else java.lang.Integer.reverseBytes(this)

/**
 * Changes the byte order of the short on host to the byte order of the device.
 */
fun Short.hostToDevice() = if (littleEndian) this else java.lang.Short.reverseBytes(this)

/**
 * Changes the byte order of the char on host to the byte order of the device.
 */
fun Char.hostToDevice() = if (littleEndian) this else java.lang.Character.reverseBytes(this)

/**
 * Changes the byte order of the int on host to the byte order of the device.
 */
fun Int.hostToDevice() = if (littleEndian) this else java.lang.Integer.reverseBytes(this)

/**
 * Returns true if the int reflects a "truthy" value in C++ for use in conditionals.
 */
fun Int.isTruthy() = this != 0

/**
 * Returns true if the short reflects a "truthy" value in C++ for use in conditionals.
 */
fun Short.isTruthy() = this != 0.toShort()

/**
 * Returns true if the byte reflects a "truthy" value in C++ for use in conditionals.
 */
fun Byte.isTruthy() = this != 0.toByte()

/**
 * Returns true if the "pointer" reflects a "truthy" value in C++ for use in conditionals.
 */
fun Any?.isTruthy() = this != null
