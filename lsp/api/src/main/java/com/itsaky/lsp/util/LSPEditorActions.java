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

package com.itsaky.lsp.util;

import static com.itsaky.androidide.utils.ILogger.newInstance;

import com.itsaky.androidide.actions.ActionItem;
import com.itsaky.androidide.actions.ActionsRegistry;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.actions.CodeActionsMenu;

/**
 * @author Akash Yadav
 */
public class LSPEditorActions {

  private static final ILogger LOG = newInstance("LSPEditorActions");

  public static void ensureActionsMenuRegistered(Class<? extends CodeActionsMenu> klass) {
    final var registry = ActionsRegistry.getInstance();
    final var action =
        registry.findAction(ActionItem.Location.EDITOR_TEXT_ACTIONS, CodeActionsMenu.ID);

    if (action == null) {
      try {
        final var constructor = klass.getDeclaredConstructor();
        final var instance = constructor.newInstance();
        registry.registerAction(instance);
      } catch (Throwable throwable) {
        LOG.error("Unable to register code actions item to editor text actions");
      }
    }
  }
}
