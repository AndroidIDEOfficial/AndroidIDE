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
package com.itsaky.androidide.utils

import android.content.Context
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.ActionsRegistry
import com.itsaky.androidide.actions.build.CancelBuildAction
import com.itsaky.androidide.actions.build.ProjectSyncAction
import com.itsaky.androidide.actions.build.QuickRunAction
import com.itsaky.androidide.actions.build.RunActionMenu
import com.itsaky.androidide.actions.editor.CopyAction
import com.itsaky.androidide.actions.editor.CutAction
import com.itsaky.androidide.actions.editor.ExpandSelectionAction
import com.itsaky.androidide.actions.editor.PasteAction
import com.itsaky.androidide.actions.editor.SelectAllAction
import com.itsaky.androidide.actions.editor.find.GoToNextAction
import com.itsaky.androidide.actions.editor.find.GoToPreviousAction
import com.itsaky.androidide.actions.editor.find.ReplaceAction
import com.itsaky.androidide.actions.etc.DaemonStatusAction
import com.itsaky.androidide.actions.etc.FileTreeAction
import com.itsaky.androidide.actions.etc.FindActionMenu
import com.itsaky.androidide.actions.etc.PreviewLayoutAction
import com.itsaky.androidide.actions.file.FormatCodeAction
import com.itsaky.androidide.actions.file.SaveFileAction
import com.itsaky.androidide.actions.text.RedoAction
import com.itsaky.androidide.actions.text.UndoAction

/**
 * Takes care of registering actions to the actions registry for the editor activity.
 *
 * @author Akash Yadav
 */
class EditorActivityActions {
    companion object {
        @JvmStatic
        fun register(context: Context) {
            val registry = ActionsRegistry.getInstance()

            // Toolbar actions
            registry.registerAction(UndoAction(context))
            registry.registerAction(RedoAction(context))
            registry.registerAction(QuickRunAction(context))
            registry.registerAction(RunActionMenu(context))
            registry.registerAction(SaveFileAction(context))
            registry.registerAction(PreviewLayoutAction(context))
            registry.registerAction(FindActionMenu(context))
            registry.registerAction(FileTreeAction(context))
            registry.registerAction(DaemonStatusAction(context))
            registry.registerAction(CancelBuildAction(context))
            registry.registerAction(ProjectSyncAction(context))

            // editor text actions
            registry.registerAction(ExpandSelectionAction(context))
            registry.registerAction(SelectAllAction(context))
            registry.registerAction(CutAction(context))
            registry.registerAction(CopyAction(context))
            registry.registerAction(PasteAction(context))
            registry.registerAction(FormatCodeAction(context))

            // editor search actions
            registry.registerAction(GoToNextAction(context))
            registry.registerAction(GoToPreviousAction(context))
            registry.registerAction(ReplaceAction(context))
        }

        @JvmStatic
        fun clear() = ActionsRegistry.getInstance().clearActions(ActionItem.Location.EDITOR_TOOLBAR)
    }
}
