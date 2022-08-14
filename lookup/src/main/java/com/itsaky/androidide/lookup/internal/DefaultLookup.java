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

package com.itsaky.androidide.lookup.internal;

import com.itsaky.androidide.lookup.Lookup;
import com.itsaky.androidide.lookup.ServiceRegisteredException;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of {@link Lookup}.
 *
 * @author Akash Yadav
 */
public final class DefaultLookup implements Lookup {

  public static final Lookup INSTANCE = new DefaultLookup();
  private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

  private DefaultLookup() {}

  @SuppressWarnings("unchecked")
  @Override
  public <T> void register(final Class<T> klass, final T instance) {
    final T existing = (T) services.put(klass, instance);
    if (existing != null) {
      throw new ServiceRegisteredException();
    }
  }

  @Override
  public <T> void unregister(final Class<T> klass) {
    services.remove(klass);
  }

  @SuppressWarnings("unchecked")
  @Nullable
  @Override
  public <T> T lookup(final Class<T> klass) {
    return (T) services.get(klass);
  }
}
