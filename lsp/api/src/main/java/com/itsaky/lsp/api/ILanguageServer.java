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

import com.itsaky.lsp.models.InitializeResult;
import com.itsaky.lsp.models.InitializeParams;

/**
 * A language server provides API for providing functions related to
 * a specific file type.
 *
 * @author Akash Yadav
 */
public interface ILanguageServer {
    
    /**
     * Initialize this language server with the given params.
     * Subclasses are expected to throw {@link AlreadyInitializedException} if the
     * language server was already initialized.
     *
     * @param params The params used to initialize the language server.
     * @return The initialization result.
     * @throws AlreadyInitializedException If the language server was already initialized.
     */
    @NonNull
    InitializeResult initialize (@NonNull InitializeParams params) throws AlreadyInitializedException;
    
    /**
     * Set the client to whom notifications and events must be sent.
     * @param client The client to set.
     */
    void connectClient (@Nullable ILanguageClient client);
    
    /**
     * Apply settings to the language server. Its up to the language server how it
     * applies these settings to the language service providers.
     *
     * @param settings The new settings to use. Pass {@code null} to use default settings.
     */
    void applySettings (@Nullable IServerSettings settings);
    
    /**
     * Notify the language server that the project's configuration was changed.
     * Language servers decide what type of object they want to receive as
     * configuration.
     *
     * @param newConfiguration The new configuration object. Only a specific type of
     *                         object might be accepted by observers.
     */
    void configurationChanged (Object newConfiguration);
    
    /**
     * Get the completion provider associated with this language server.
     * Should never be null.
     *
     * @return The completion provider.
     */
    @NonNull
    ICompletionProvider getCompletionProvider ();
    
    /**
     * Get the code action provider associated with this language server.
     * Must not be null.
     *
     * @return The code action provider.
     */
    @NonNull
    ICodeActionProvider getCodeActionProvider ();
    
    /**
     * The document handler associated with this language server instance.
     *
     * @return The document handler. Must not be null.
     */
    @NonNull
    IDocumentHandler getDocumentHandler ();
    
    /**
     * Thrown to indicate that a language server received an initialize notification
     * but was already initialized.
     *
     * @author Akash Yadav
     */
    class AlreadyInitializedException extends IllegalStateException {
    }
}
