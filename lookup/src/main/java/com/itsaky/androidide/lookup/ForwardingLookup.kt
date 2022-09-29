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

package com.itsaky.androidide.lookup

import com.itsaky.androidide.lookup.Lookup.Key

/**
 * For all calls to the provided lookup instance.
 *
 * @author Akash Yadav
 */
abstract class ForwardingLookup(protected val lookup: Lookup) : Lookup {
  override fun <T : Any?> register(klass: Class<T>?, instance: T) = lookup.register(klass, instance)

  override fun <T : Any?> update(klass: Class<T>?, instance: T) = lookup.update(klass, instance)

  override fun <T : Any?> unregister(klass: Class<T>?) = lookup.unregister(klass)

  override fun <T : Any?> lookup(klass: Class<T>?): T? = lookup.lookup(klass)

  override fun <T : Any?> register(key: Key<T>?, instance: T) = lookup.register(key, instance)

  override fun <T : Any?> unregister(key: Key<T>?) = lookup.unregister(key)

  override fun <T : Any?> lookup(key: Key<T>?): T? = lookup.lookup(key)

  override fun <T : Any?> update(key: Key<T>?, instance: T) = lookup.update(key, instance)
}
