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

package com.itsaky.androidide.lsp.util;

import com.itsaky.androidide.actions.ActionItem;
import com.itsaky.androidide.actions.ActionMenu;
import com.itsaky.androidide.actions.ActionsRegistry;
import com.itsaky.androidide.actions.locations.CodeActionsMenu;
import com.itsaky.androidide.lsp.actions.IActionsMenuProvider;
import com.itsaky.androidide.utils.ILogger;

/**
 * @author Akash Yadav
 */
public class LSPEditorActions {

  public static void ensureActionsMenuRegistered(IActionsMenuProvider provider) {
    final var registry = ActionsRegistry.getInstance();
    final var action =
        registry.findAction(ActionItem.Location.EDITOR_TEXT_ACTIONS, CodeActionsMenu.ID);

    if (action == null) {
      ILogger.ROOT.error("[LSPEditorActions] Cannot find registered editor actions menu");
      return;
    }

    final var editorActions = (ActionMenu) action;
    for (final var item : provider.getActions()) {
      if (editorActions.findAction(item.getId()) != null) {
        continue;
      }
      editorActions.addAction(item);
    }
  }
}
