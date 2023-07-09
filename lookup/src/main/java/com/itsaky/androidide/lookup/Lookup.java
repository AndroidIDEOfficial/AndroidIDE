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

package com.itsaky.androidide.lookup;

import org.jetbrains.annotations.Nullable;

/**
 * <strong>Lookup</strong> provides API to look for registered services.
 *
 * @author Akash Yadav
 */
public interface Lookup {

  /**
   * Get the default implementation of {@link Lookup} service.
   *
   * @return The default implementation.
   */
  static Lookup getDefault() {
    return LookupProvider.lookupService();
  }

  /**
   * Register the service for the given class.
   *
   * @param klass    The class to register the service for.
   * @param instance The implementation of the service.
   * @param <T>      The type of service.
   */
  <T> void register(Class<T> klass, T instance);

  /**
   * Updates the service for the given class. Registers if not already registered.
   *
   * @param klass    The class to update the service for.
   * @param instance The instance of the service.
   * @param <T>      The type of the service.
   */
  <T> void update(Class<T> klass, T instance);

  /**
   * Unregister the service for given class.
   *
   * @param klass The class of service to unregister.
   * @param <T>   The type of service.
   */
  <T> void unregister(Class<T> klass);

  /**
   * Lookup the service for the given class.
   *
   * @param klass The Class of the service to look for.
   * @param <T>   The type of service.
   * @return The instance of the registered service or <code>null</code>.
   */
  @Nullable
  <T> T lookup(Class<T> klass);

  /**
   * Register the service for the given key.
   *
   * @param key      The class to register the service for.
   * @param instance The implementation of the service.
   * @param <T>      The type of service.
   */
  <T> void register(Key<T> key, T instance);

  /**
   * Unregister the service for given key.
   *
   * @param key The key of service to unregister.
   * @param <T> The type of service.
   */
  <T> void unregister(Key<T> key);

  /**
   * Lookup the service for the given key.
   *
   * @param key The key of the service to look for.
   * @param <T> The type of service.
   * @return The instance of the registered service or <code>null</code>.
   */
  @Nullable
  <T> T lookup(Key<T> key);

  /**
   * Updates the service for the given key. Registers if not already registered.
   *
   * @param key      The key to update the service for.
   * @param instance The instance of the service.
   * @param <T>      The type of the service.
   */
  <T> void update(Key<T> key, T instance);

  /**
   * Unregister all registered services.
   */
  void unregisterAll();

  /**
   * The key that is used to register services in the table.
   *
   * @param <T> The type of the service.
   */
  class Key<T> {
    // we inherit identity equality from Object
  }
}
