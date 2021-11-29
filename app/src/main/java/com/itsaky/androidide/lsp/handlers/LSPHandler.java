/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/


package com.itsaky.androidide.lsp.handlers;

import java.util.Optional;
import org.eclipse.lsp4j.InitializeResult;

/**
 * A LSPHandler handles the launch, initialization and shutdown of a Language server
 */
public interface LSPHandler {
    
    /**
     * Called to start the language server
     *
     * @param onStarted A {@link Runnable} that must be called once the server is started
     */
    void start(Runnable onStarted);
    
    /**
     * Initialize the Language server with the root path provided
     *
     * @param rootPath Path of currently opened project
     * @return An {@link Optional} of {@link InitializeResult}.
     *         May be {@link Optional#empty} if the initialization was not successful.
     */
    Optional<InitializeResult> init(String rootPath);
     
    /**
     * Called to send initialized notification to server.
     */
    void initialized();
    
    /**
     * Called to notify that the server must shutdown now.
     */
    void shutdown();
}
