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

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * @author Akash Yadav
 */
@RunWith(AndroidJUnit4::class)
class JavaIndexerTest {

  @Test
  fun testSimpleRealmInit() {
    val context = InstrumentationRegistry.getInstrumentation().context
    Realm.init(context.applicationContext)

    val file = File.createTempFile("ajar", null, context.cacheDir)
    context.assets.open("android.jar").use { asset ->
      assertThat(asset).isNotNull()
      file.outputStream().buffered().use { out ->
        asset.copyTo(out)
        out.flush()
      }
    }

    val dbDir = context.cacheDir.resolve("realm-db")
    if (dbDir.exists()) {
      if (dbDir.isDirectory) {
        dbDir.deleteRecursively()
      } else {
        dbDir.delete()
      }
    }

    RealmLog.setLevel(LogLevel.ALL)
    val realm = Realm.getInstance(
      RealmConfiguration.Builder()
        .directory(dbDir)
        .name("android-jar-classes.realm")
        .deleteRealmIfMigrationNeeded()
        .build()
    )

    try {
      val start = System.currentTimeMillis()
      val worker = JavaIndexWorker(file)
      val batches = mutableMapOf<Class<*>, MutableList<RealmModel>>()

      worker.consumeTypes { type ->
        val batched = batches.computeIfAbsent(type.javaClass) { mutableListOf() }
        batched.add(type)

        if (batched.size >= 100) {
          realm.executeTransaction {
            realm.insertOrUpdate(batched)
          }

          batched.clear()
        }
      }

      for ((_, batched) in batches) {
        if (batched.isNotEmpty()) {
          realm.executeTransaction {
            realm.insertOrUpdate(batched)
          }
        }
      }

      println("Took ${System.currentTimeMillis() - start}ms")
    } finally {
      realm.close()
      file.delete()
    }
  }
}