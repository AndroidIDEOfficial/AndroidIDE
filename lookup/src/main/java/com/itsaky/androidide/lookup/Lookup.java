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

import com.itsaky.androidide.lookup.internal.DefaultLookup;

import org.jetbrains.annotations.Nullable;

/**
 * <strong>Lookup</strong> provides API to look for registered services.
 *
 * @author Akash Yadav
 */
public interface Lookup {
  
  Lookup DEFAULT = DefaultLookup.INSTANCE;
  
  /**
   * Register the service for the given class.
   *
   * @param klass The class to register the service for.
   * @param instance The implementation of teh service.
   * @param <T> The type of service.
   */
  <T> void register(Class<T> klass, T instance);

  /**
   * Unregister the service for given class.
   *
   * @param klass The class of service to unregister.
   * @param <T> The type of service.
   */
  <T> void unregister(Class<T> klass);

  /**
   * Lookup the service for the given class.
   *
   * @param klass The Class of the service to look for.
   * @param <T> The type of service.
   * @return The instance of the registered service or <code>null</code>.
   */
  @Nullable
  <T> T lookup(Class<T> klass);
}
