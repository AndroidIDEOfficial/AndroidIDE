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

package com.itsaky.lsp.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe implementation of {@link ILanguageServerRegistry}.
 *
 * @author Akash Yadav
 */
public class DefaultLanguageServerRegistry extends ILanguageServerRegistry {

  private final Map<String, ILanguageServer> mRegister = new HashMap<>();
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @Override
  public void register(@NonNull final ILanguageServer server) {
    lock.writeLock().lock();
    try {
      final var old = mRegister.put(server.getServerId(), server);
      if (old != null) {
        mRegister.put(old.getServerId(), old);
        throw new IllegalStateException("A language server with the same ID is already registered");
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void unregister(@NonNull final String serverId) {
    lock.writeLock().lock();
    try {
      final var registered = mRegister.remove(serverId);
      if (registered == null) {
        throw new IllegalStateException("No server found for the given server ID");
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void destroy() {
    lock.readLock().lock();
    try {
      for (var server : mRegister.values()) {
        server.shutdown();
      }
    } finally {
      lock.readLock().unlock();
    }

    lock.writeLock().lock();
    try {
      mRegister.clear();
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Nullable
  @Override
  public ILanguageServer getServer(@NonNull final String serverId) {
    lock.readLock().lock();
    try {
      return mRegister.get(serverId);
    } finally {
      lock.readLock().unlock();
    }
  }
}
