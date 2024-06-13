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

package com.itsaky.androidide.indexing

import io.realm.Realm

/**
 * Abstract implementation of [IIndex] for indexing data in the database.
 *
 * @property params The index parameters.
 * @author Akash Yadav
 */
abstract class AbstractDBIndex<T : IIndexable, C : IIndexParams>(
  protected val params: IIndexParams?
) : IIndex<T, C> {

  protected abstract val realm: Realm

  override fun index(symbol: T) {
    realm.executeTransaction {
      it.insertOrUpdate(symbol)
    }
  }

  override fun indexAsync(symbol: T) {
    realm.executeTransactionAsync {
      it.insertOrUpdate(symbol)
    }
  }

  override fun indexAll(symbols: Collection<T>) {
    realm.executeTransaction {
      it.insertOrUpdate(symbols)
    }
  }

  override fun indexAllAsync(symbols: Collection<T>) {
    realm.executeTransactionAsync { it.insertOrUpdate(symbols) }
  }

  override fun delete() {
    realm.deleteAll()
  }
}