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

package com.itsaky.androidide.benchmark.lsp.java

import android.content.Context
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lsp.java.indexing.JavaJarModelBuilder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * @author Akash Yadav
 */
@RunWith(AndroidJUnit4::class)
class JavaIndexingWorkerBenchmark {

  @get:Rule
  val benchmarkRule = BenchmarkRule()

  val androidJar: File by lazy {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val file = File.createTempFile("ajar", null, context.cacheDir)
    context.assets.open("android.jar").use { asset ->
      assertThat(asset).isNotNull()
      file.outputStream().buffered().use { out ->
        asset.copyTo(out)
        out.flush()
      }
    }
    file
  }

  @Test
  fun benchmarkJavaIndexingWorker() {
    val worker = JavaJarModelBuilder(androidJar)
    benchmarkRule.measureRepeated {
      worker.buildTypes()
    }
  }
}