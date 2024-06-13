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

package com.itsaky.androidide.lsp.java.indexing

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.blankj.utilcode.util.ConvertUtils
import com.google.common.truth.Truth
import com.itsaky.androidide.testing.android.rules.RealmDBTestRule
import io.realm.RealmModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * @author Akash Yadav
 */
@RunWith(AndroidJUnit4::class)
class JavaIndexerTest {

  @Rule
  @JvmField
  val dbTestRule = RealmDBTestRule(JavaIndexingRealmModule())

  val androidJar: File by lazy {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val file = File.createTempFile("ajar", null, context.cacheDir)
    context.assets.open("android.jar").use { asset ->
      Truth.assertThat(asset).isNotNull()
      file.outputStream().buffered().use { out ->
        asset.copyTo(out)
        out.flush()
      }
    }
    file
  }

  @Test
  fun testSimpleAndroidJarIndexingDurationCheck() {
    val worker = JavaJarModelBuilder(androidJar)
    val batches = mutableMapOf<Class<*>, MutableList<RealmModel>>()

    var dbWriteDuration = 0L

    dbTestRule.withDb("android-jar-classes") {
      val totalStart = System.currentTimeMillis()
      worker.consumeTypes { type ->
        val batched = batches.computeIfAbsent(type.javaClass) { mutableListOf() }
        batched.add(type)

        if (batched.size >= 100) {
          val start = System.currentTimeMillis()
          executeTransaction {
            insertOrUpdate(batched)
          }
          dbWriteDuration += System.currentTimeMillis() - start

          batched.clear()
        }
      }

      for ((_, batched) in batches) {
        if (batched.isNotEmpty()) {
          val start = System.currentTimeMillis()
          executeTransaction {
            insertOrUpdate(batched)
          }
          dbWriteDuration += System.currentTimeMillis() - start
        }
      }

      println(
        "Took ${dbWriteDuration}ms to write android.jar (${
          ConvertUtils.byte2FitMemorySize(
            androidJar.length()
          )
        }) classes to Realm"
      )
      println("Total time: ${System.currentTimeMillis() - totalStart}ms")
    }
  }
}