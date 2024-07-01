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

package com.itsaky.androidide.lsp.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.eventbus.events.project.ProjectInitializedEvent;
import com.itsaky.androidide.projects.IWorkspace;
import com.itsaky.androidide.utils.ILogger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    if (!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this);
    }
    lock.writeLock().lock();
    try {
      final var old = mRegister.put(server.getServerId(), server);
      if (old != null) {
        mRegister.put(old.getServerId(), old);
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void connectClient(@NonNull final ILanguageClient client) {
    Objects.requireNonNull(client);
    lock.readLock().lock();
    try {
      for (final var server : mRegister.values()) {
        server.connectClient(client);
      }
    } finally {
      lock.readLock().unlock();
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
    EventBus.getDefault().unregister(this);
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

  @Subscribe(threadMode = ThreadMode.BACKGROUND)
  @SuppressWarnings("unused")
  public void onProjectInitialized(ProjectInitializedEvent event) {
    final var workspace = event.get(IWorkspace.class);
    if (workspace == null) {
      return;
    }

    ILogger.ROOT.debug("Dispatching ProjectInitializedEvent to language servers...");
    for (final var server : mRegister.values()) {
      server.setupWorkspace(workspace);
    }
  }
}
