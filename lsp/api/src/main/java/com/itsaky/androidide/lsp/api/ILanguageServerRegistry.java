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

/**
 * A language server registry which keeps track of registered language servers.
 *
 * @author Akash Yadav
 */
public abstract class ILanguageServerRegistry {

  private static ILanguageServerRegistry sRegistry = null;

  public static ILanguageServerRegistry getDefault() {
    if (sRegistry == null) {
      sRegistry = new DefaultLanguageServerRegistry();
    }

    return sRegistry;
  }

  /**
   * Register the language server.
   *
   * @param server The server to register.
   */
  public abstract void register(@NonNull ILanguageServer server);

  /** Connects client to all the registered {@link ILanguageServer}s. */
  public abstract void connectClient(@NonNull ILanguageClient client);

  /**
   * Unregister the given server. If any server is registered with the given server ID, a shutdown
   * request will be sent to that server.
   *
   * @param serverId The ID of the server to unregister.
   */
  public abstract void unregister(@NonNull String serverId);

  /** Calls {@link #unregister(String)} for all the registered language servers. */
  public abstract void destroy();

  /**
   * Get the {@link ILanguageServer} registered with the given server ID.
   *
   * @param serverId The ID of the language server.
   * @return The {@link ILanguageServer} instance. Or <code>null</code> if no server is registered
   *     with the provided ID.
   */
  @Nullable
  public abstract ILanguageServer getServer(@NonNull String serverId);
}
