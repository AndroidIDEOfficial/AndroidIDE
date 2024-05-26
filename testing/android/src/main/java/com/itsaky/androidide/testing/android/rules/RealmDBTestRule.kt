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

package com.itsaky.androidide.testing.android.rules

import com.itsaky.androidide.db.IRealmProvider
import com.itsaky.androidide.testing.android.util.NoOpStatement
import io.realm.Realm
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Rule for Realm DB tests.
 *
 * @author Akash Yadav
 */
class RealmDBTestRule(
  val baseModule: Any? = null,
  vararg val additionalModules: Array<Any>,
) : AbstractAndroidTestRule() {

  inline fun withDb(dbName: String, deleteDbAfterTest: Boolean = true, action: Realm.() -> Unit) {
    val name = dbName.replace(IRealmProvider.PATH_SEPARATOR, '-')
    val realm = IRealmProvider.instance().get("/indexing/java/$name") {
      baseModule?.let { baseModule ->
        modules(baseModule, *additionalModules)
      }
    }

    try {
      realm.action()
    } finally {
      if (deleteDbAfterTest) {
        realm.configuration.realmDirectory.deleteRecursively()
      }
    }
  }

  override fun apply(base: Statement?, description: Description?): Statement {
    Realm.init(context.applicationContext)
    RealmLog.setLevel(LogLevel.ALL)
    base?.evaluate()
    return NoOpStatement()
  }
}