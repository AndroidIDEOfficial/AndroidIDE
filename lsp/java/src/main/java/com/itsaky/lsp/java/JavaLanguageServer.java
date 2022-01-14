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

package com.itsaky.lsp.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.api.ICodeActionProvider;
import com.itsaky.lsp.api.ICompletionProvider;
import com.itsaky.lsp.api.IDocumentHandler;
import com.itsaky.lsp.api.ILanguageClient;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.api.IServerSettings;
import com.itsaky.lsp.java.completion.JavaCompletionProvider;
import com.itsaky.lsp.java.models.JavaServerConfiguration;
import com.itsaky.lsp.models.DocumentChangeEvent;
import com.itsaky.lsp.models.DocumentCloseEvent;
import com.itsaky.lsp.models.DocumentOpenEvent;
import com.itsaky.lsp.models.DocumentSaveEvent;
import com.itsaky.lsp.models.InitializeParams;
import com.itsaky.lsp.models.InitializeResult;
import com.itsaky.lsp.util.DefaultServerSettings;
import com.itsaky.lsp.util.NoCodeActionsProvider;
import com.itsaky.lsp.util.NoCompletionsProvider;

import java.util.Collections;

public class JavaLanguageServer implements ILanguageServer, IDocumentHandler {
    
    private ILanguageClient client;
    private IServerSettings settings;
    private JavaCompilerService compiler;
    
    private JavaServerConfiguration configuration;
    private boolean initialized;
    private boolean createCompiler;
    
    private static final Logger LOG = Logger.instance ("JavaLanguageServer");
    
    public JavaLanguageServer () {
        initialized = false;
        createCompiler = true;
        configuration = new JavaServerConfiguration ();
        
        applySettings (getSettings ());
    }
    
    private JavaCompilerService getCompiler () {
        if (createCompiler) {
            LOG.info ("Creating new compiler instance...");
            compiler = createCompiler ();
            createCompiler = false;
        }
        
        return compiler;
    }
    
    public IServerSettings getSettings () {
        if (settings == null) {
            settings = new DefaultServerSettings ();
        }
        
        return settings;
    }
    
    @NonNull
    private JavaCompilerService createCompiler () {
        return new JavaCompilerService (
                configuration.getClassPaths (),
                Collections.emptySet ()
        );
    }
    
    @NonNull
    @Override
    public InitializeResult initialize (@NonNull InitializeParams params) throws AlreadyInitializedException {
        
        if (initialized) {
            throw new AlreadyInitializedException ();
        }
        
        FileStore.setWorkspaceRoots (params.getWorkspaceRoots ());
        
        final InitializeResult result = new InitializeResult ();
        result.setCompletionsAvailable (true);
        result.setCodeActionsAvailable (true);
        
        initialized = true;
        
        return result;
    }
    
    @Override
    public void connectClient (@Nullable ILanguageClient client) {
        this.client = client;
    }
    
    @Override
    public void applySettings (@Nullable IServerSettings settings) {
        this.settings = settings;
    }
    
    @Override
    public void configurationChanged (Object newConfiguration) {
        if (!(newConfiguration instanceof JavaServerConfiguration)) {
            LOG.error ("Invalid configuration passed to server.", newConfiguration);
            LOG.error ("Configuration change event will be ignored.");
            return;
        }
        
        this.configuration = (JavaServerConfiguration) newConfiguration;
        LOG.info ("Java language server configuration changed. New configuration:");
        LOG.info (this.configuration);
        
        // Compiler must be recreated on a configuration change
        this.createCompiler = true;
    }
    
    @NonNull
    @Override
    public ICompletionProvider getCompletionProvider () {
        if (!settings.completionsEnabled ()) {
            return new NoCompletionsProvider ();
        }
        
        return new JavaCompletionProvider (getCompiler ());
    }
    
    @NonNull
    @Override
    public ICodeActionProvider getCodeActionProvider () {
        return new NoCodeActionsProvider ();
    }
    
    @NonNull
    @Override
    public IDocumentHandler getDocumentHandler () {
        return this;
    }
    
    @Override
    public void onFileOpened (DocumentOpenEvent event) {
        FileStore.open (event);
    }
    
    @Override
    public void onContentChange (DocumentChangeEvent event) {
        FileStore.change (event);
    }
    
    @Override
    public void onFileSaved (DocumentSaveEvent event) {
        // TODO Run a lint check (or a simple compilation)
    }
    
    @Override
    public void onFileClosed (DocumentCloseEvent event) {
        FileStore.close (event);
    }
}
