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
import com.google.common.truth.Truth
import com.itsaky.androidide.db.IRealmProvider
import com.itsaky.androidide.lsp.java.indexing.models.JavaIndexingRealmModule
import com.itsaky.androidide.utils.Environment
import io.realm.Realm
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import java.io.File

/**
 * @author Akash Yadav
 */
class IndexingHelper(
  private val dbName: String
) {
  private val context: Context
    get() = ApplicationProvider.getApplicationContext()

  val androidJar: File by lazy {
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

  fun doWithRealm(deleteDbAfterAction: Boolean = true, action: Realm.() -> Unit) {
    Realm.init(context.applicationContext)
    RealmLog.setLevel(LogLevel.ALL)

    if (Environment.REALM_DB_DIR == null) {
      Environment.init(context)
    }

    val dbName = dbName.replace(IRealmProvider.PATH_SEPARATOR, '-')
    val realm = IRealmProvider.instance().get("/indexing/java/$dbName") {
      modules(JavaIndexingRealmModule())
    }
    try {
      realm.action()
    } finally {
      if (deleteDbAfterAction) {
        realm.configuration.realmDirectory.deleteRecursively()
      }
    }
  }
}