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

import com.google.common.truth.Truth.assertThat
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

inline fun <reified T : RealmModel> Realm.assertInsertion(
  vararg models: T,
  crossinline verify: Realm.(models: RealmResults<T>) -> Unit
) {
  assertInsertion(false, *models, verify = verify)
}

inline fun <reified T : RealmModel> Realm.assertInsertion(
  allowUpdate: Boolean = false,
  vararg models: T,
  crossinline verify: Realm.(models: RealmResults<T>) -> Unit
) {
  val modelList = models.toList()
  assertThat(modelList).isNotEmpty()

  executeTransaction {
    if (allowUpdate) {
      it.insertOrUpdate(modelList)
    } else {
      it.insert(modelList)
    }
  }

  executeTransaction {
    verify(it.where(modelList[0].javaClass).findAll())
  }
}

fun Realm.assertInsertSingle(model: RealmModel) {
  assertInsertion(model) { models ->
    assertThat(models).isNotNull()
    assertThat(models).isNotEmpty()
    assertThat(models).hasSize(1)
    assertThat(models[0]).isEqualTo(model)
  }
}

fun Realm.assertInsertUnique(model: RealmModel) {
  assertInsertSingle(model)
  assertInsertion(true, model) { models ->
    assertThat(models).isNotNull()
    assertThat(models).isNotEmpty()
    assertThat(models).hasSize(1)
    assertThat(models[0]).isEqualTo(model)
  }
}


