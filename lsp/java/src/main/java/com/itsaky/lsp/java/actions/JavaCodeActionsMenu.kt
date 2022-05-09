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

package com.itsaky.lsp.java.actions

import com.itsaky.lsp.actions.CodeActionsMenu
import com.itsaky.lsp.java.actions.common.CommentAction
import com.itsaky.lsp.java.actions.common.FindReferencesAction
import com.itsaky.lsp.java.actions.common.GoToDefinitionAction
import com.itsaky.lsp.java.actions.common.UncommentAction
import com.itsaky.lsp.java.actions.common.RemoveUnusedImportsAction
import com.itsaky.lsp.java.actions.common.OrganizeImportsAction
import com.itsaky.lsp.java.actions.diagnostics.AddImportAction
import com.itsaky.lsp.java.actions.diagnostics.AddThrowsAction
import com.itsaky.lsp.java.actions.diagnostics.CreateMissingMethodAction
import com.itsaky.lsp.java.actions.diagnostics.FieldToBlockAction
import com.itsaky.lsp.java.actions.diagnostics.ImplementAbstractMethodsAction
import com.itsaky.lsp.java.actions.diagnostics.RemoveClassAction
import com.itsaky.lsp.java.actions.diagnostics.RemoveMethodAction
import com.itsaky.lsp.java.actions.diagnostics.RemoveUnusedThrowsAction
import com.itsaky.lsp.java.actions.diagnostics.SuppressUncheckedWarningAction
import com.itsaky.lsp.java.actions.diagnostics.VariableToStatementAction
import com.itsaky.lsp.java.actions.generators.GenerateConstructorAction
import com.itsaky.lsp.java.actions.generators.GenerateMissingConstructorAction
import com.itsaky.lsp.java.actions.generators.GenerateSettersAndGettersAction
import com.itsaky.lsp.java.actions.generators.GenerateToStringMethodAction
import com.itsaky.lsp.java.actions.generators.OverrideSuperclassMethodsAction

/** @author Akash Yadav */
class JavaCodeActionsMenu : CodeActionsMenu() {
    init {
        addAction(CommentAction())
        addAction(UncommentAction())
        addAction(GoToDefinitionAction())
        addAction(FindReferencesAction())
        addAction(AddImportAction())
        addAction(ImplementAbstractMethodsAction())
        addAction(VariableToStatementAction())
        addAction(FieldToBlockAction())
        addAction(RemoveClassAction())
        addAction(RemoveMethodAction())
        addAction(RemoveUnusedThrowsAction())
        addAction(CreateMissingMethodAction())
        addAction(SuppressUncheckedWarningAction())
        addAction(AddThrowsAction())
        addAction(GenerateSettersAndGettersAction())
        addAction(OverrideSuperclassMethodsAction())
        addAction(GenerateMissingConstructorAction())
        addAction(GenerateConstructorAction())
        addAction(GenerateToStringMethodAction())
        addAction(RemoveUnusedImportsAction())
        addAction(OrganizeImportsAction())
    }
}



