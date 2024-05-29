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

package com.itsaky.androidide.db.internal

import com.google.auto.service.AutoService
import com.itsaky.androidide.db.IRealmProvider
import com.itsaky.androidide.utils.Environment
import io.realm.Realm
import io.realm.RealmConfiguration
import org.slf4j.LoggerFactory

/**
 * Default implementation of [IRealmProvider].
 *
 * @author Akash Yadav
 */
@AutoService(IRealmProvider::class)
@Suppress("unused")
internal class RealmProviderImpl : IRealmProvider {

  companion object {
    private val log = LoggerFactory.getLogger(RealmProviderImpl::class.java)

    // must be a relative path, with no special characters except '_' '-' and '/' (path sep)
    private val dbNameRegex = Regex("^[a-zA-Z0-9_\\-/]+$")
    private const val MASTER_DB_NAME = "master"
    private const val MASTER_DB_PATH = "/$MASTER_DB_NAME"
  }

  private val masterDb: Realm by lazy {
    createDb(MASTER_DB_PATH) {
      modules(InternalDBRealmModule())
    }
  }

  override fun get(path: String, config: (RealmConfiguration.Builder.() -> Unit)?): Realm {
    require(path != MASTER_DB_PATH) {
      "Master DB is internal to AndroidIDE"
    }

    return createDb(path, config)
  }

  private fun createDb(path: String, confFunc: (RealmConfiguration.Builder.() -> Unit)?): Realm {
    val (parentPath, name) = validateDbPath(path)

    val configBuilder = RealmConfiguration.Builder()
    confFunc?.also { configure -> configBuilder.configure() }

    var dbDir = Environment.REALM_DB_DIR
    if (parentPath.isNotEmpty()) {
      dbDir = dbDir.resolve(parentPath)
    }

    val dbName = "${name}.realm"
    val config = configBuilder
      .name(dbName)
      .directory(dbDir)
      .allowQueriesOnUiThread(false)
      .allowWritesOnUiThread(false)
      .compactOnLaunch()
      .build()

    log.info("Creating DB at $dbDir/$dbName")

    if (parentPath.isEmpty() && name != MASTER_DB_NAME) {
      masterDb.executeTransactionAsync { realm ->
        realm.insertOrUpdate(DatabaseEntity(name = name, path = path, directory = dbDir.absolutePath))
      }
    }

    return Realm.getInstance(config)
  }

  private fun validateDbPath(path: String): Pair<String, String> {
    val idx = path.indexOfLast { it == IRealmProvider.PATH_SEPARATOR }
    require(
      path.matches(dbNameRegex)
        && path[0] == IRealmProvider.PATH_SEPARATOR
        && idx >= 0
        && path.indexOf("//") == -1
    ) {
      "Invalid DB path: $path"
    }

    val parentPath = if (idx > 1) {
      path.substring(1, idx)
    } else ""

    val name = path.substring(idx + 1)

    if (name == MASTER_DB_NAME) {
      require(parentPath.isEmpty()) {
        "Master DB must not have any parent path"
      }
    }

    return parentPath to name
  }
}