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

import com.android.aaptcompiler.BlameLogger.Source
import com.android.utils.ILogger
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BlameLoggerTest {

    @Test
    fun rewriteSingleSource() {
        val mockLogger = MockLogger()
        val blameLogger = getMockBlameLogger(mockLogger)

        val result = blameLogger.getOriginalSource(BlameLogger.Source("foo/bar.xml", 3, 5))
        assertThat(result.sourcePath).isEqualTo("foo/bar.xml.rewritten")
        assertThat(result.line).isEqualTo(4)
        assertThat(result.column).isEqualTo(7)
    }

    @Test
    fun testsLoggedMessageRewritten() {
        val mockLogger = MockLogger()
        val blameLogger = getMockBlameLogger(mockLogger)

        blameLogger.error("Failed to read file", BlameLogger.Source("foo/bar.xml", 3, 5))
        assertThat(mockLogger.errors).hasSize(1)
        val loggedError = mockLogger.errors.single()
        assertThat(loggedError.first).contains("bar.xml.rewritten:4:7: Failed to read file")
        assertThat(loggedError.second).isNull()
    }

    class MockLogger: ILogger {
        val warnings: MutableList<String> = mutableListOf()
        val infos: MutableList<String> = mutableListOf()
        val errors: MutableList<Pair<String, Throwable?>> = mutableListOf()
        val verboses: MutableList<String> = mutableListOf()

        override fun warning(msgFormat: String, vararg args: Any?) {
            warnings.add(String.format(msgFormat, args))
        }

        override fun info(msgFormat: String, vararg args: Any?) {
            infos.add(String.format(msgFormat, args))
        }

        override fun error(t: Throwable?, msgFormat: String?, vararg args: Any?) {
            errors.add(Pair(String.format(msgFormat!!, args), t))
        }

        override fun verbose(msgFormat: String, vararg args: Any?) {
            verboses.add(String.format(msgFormat, args))
        }
    }
}

fun getMockBlameLogger(mockLogger: BlameLoggerTest.MockLogger) =
    BlameLogger(
        mockLogger
    ) {
        Source(it.sourcePath + ".rewritten", it.line + 1, it.column + 2)
    }
