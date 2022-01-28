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

package com.itsaky.lsp.xml;

import androidx.annotation.NonNull;

import com.itsaky.lsp.api.ICodeActionProvider;
import com.itsaky.lsp.api.ICodeAnalyzer;
import com.itsaky.lsp.api.ICompletionProvider;
import com.itsaky.lsp.api.IDefinitionProvider;
import com.itsaky.lsp.api.IDocumentHandler;
import com.itsaky.lsp.api.ILanguageClient;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.api.IReferenceProvider;
import com.itsaky.lsp.api.ISelectionProvider;
import com.itsaky.lsp.api.IServerSettings;
import com.itsaky.lsp.api.ISignatureHelpProvider;
import com.itsaky.lsp.models.DocumentChangeEvent;
import com.itsaky.lsp.models.DocumentCloseEvent;
import com.itsaky.lsp.models.DocumentOpenEvent;
import com.itsaky.lsp.models.DocumentSaveEvent;
import com.itsaky.lsp.models.InitializeParams;
import com.itsaky.lsp.models.InitializeResult;
import com.itsaky.lsp.xml.models.DefaultXMLServerSettings;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Language server implementation for XML files.
 * @author Akash Yadav
 */
public class XMLLanguageServer implements ILanguageServer, IDocumentHandler {
    
    private ILanguageClient client;
    private IServerSettings settings;
    private boolean initialized = false;
    
    public XMLLanguageServer () {
    
    }
    
    @NonNull
    public IServerSettings getSettings () {
        if (settings == null) {
            settings = new DefaultXMLServerSettings ();
        }
        return settings;
    }
    
    @Override
    public InitializeResult initialize (InitializeParams params) throws AlreadyInitializedException {
        if (initialized) {
            throw new AlreadyInitializedException ();
        }
        
        XMLFileStore.setWorkspaceRoots (params.getWorkspaceRoots ());
        
        final var result = new InitializeResult ();
        result.setCompletionsAvailable (true);
        result.setCodeAnalysisAvailable (true);
        result.setSignatureHelpAvailable (false);
        result.setReferencesAvailable (false);
        result.setDefinitionsAvailable (false);
        
        initialized = true;

        return result;
    }
    
    @Override
    public void shutdown () {
        XMLFileStore.shutdown ();
    }
    
    @Override
    public void connectClient (ILanguageClient client) {
        this.client = client;
    }
    
    @Nullable
    @Override
    public ILanguageClient getClient () {
        return this.client;
    }
    
    @Override
    public void applySettings (IServerSettings settings) {
        this.settings = settings;
    }
    
    @Override
    public void configurationChanged (Object newConfiguration) {
    
    }
    
    @Override
    public ICompletionProvider getCompletionProvider () {
        return null;
    }
    
    @Override
    public ICodeActionProvider getCodeActionProvider () {
        return null;
    }
    
    @Override
    public IReferenceProvider getReferenceProvider () {
        return null;
    }
    
    @Override
    public IDefinitionProvider getDefinitionProvider () {
        return null;
    }
    
    @Override
    public ISelectionProvider getSelectionProvider () {
        return null;
    }
    
    @Override
    public ISignatureHelpProvider getSignatureHelpProvider () {
        return null;
    }
    
    @Override
    public IDocumentHandler getDocumentHandler () {
        return null;
    }
    
    @Override
    public ICodeAnalyzer getCodeAnalyzer () {
        return null;
    }
    
    @Override
    public void onFileOpened (DocumentOpenEvent event) {
        XMLFileStore.open (event);
    }
    
    @Override
    public void onContentChange (DocumentChangeEvent event) {
        XMLFileStore.change (event);
    }
    
    @Override
    public void onFileSaved (DocumentSaveEvent event) {
        XMLFileStore.save (event);
    }
    
    @Override
    public void onFileClosed (DocumentCloseEvent event) {
        XMLFileStore.close (event);
    }
}