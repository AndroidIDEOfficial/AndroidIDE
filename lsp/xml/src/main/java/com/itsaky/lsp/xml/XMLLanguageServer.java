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
import com.itsaky.lsp.api.ICompletionProvider;
import com.itsaky.lsp.api.IDefinitionProvider;
import com.itsaky.lsp.api.IDiagnosticProvider;
import com.itsaky.lsp.api.IDocumentHandler;
import com.itsaky.lsp.api.ILanguageClient;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.api.IReferenceProvider;
import com.itsaky.lsp.api.ISelectionProvider;
import com.itsaky.lsp.api.IServerSettings;
import com.itsaky.lsp.api.ISignatureHelpProvider;
import com.itsaky.lsp.models.InitializeParams;
import com.itsaky.lsp.models.ServerCapabilities;
import com.itsaky.lsp.util.NoCodeActionsProvider;
import com.itsaky.lsp.util.NoCompletionsProvider;
import com.itsaky.lsp.util.NoDefinitionProvider;
import com.itsaky.lsp.util.NoDiagnosticProvider;
import com.itsaky.lsp.util.NoDocumentHandler;
import com.itsaky.lsp.util.NoReferenceProvider;
import com.itsaky.lsp.util.NoSelectionProvider;
import com.itsaky.lsp.util.NoSignatureHelpProvider;
import com.itsaky.lsp.xml.models.DefaultXMLServerSettings;
import com.itsaky.lsp.xml.providers.CompletionProvider;
import com.itsaky.sdk.SDKInfo;

import org.jetbrains.annotations.Nullable;

/**
 * Language server implementation for XML files.
 *
 * @author Akash Yadav
 */
public class XMLLanguageServer implements ILanguageServer {

    private SDKInfo sdkInfo;

    private ILanguageClient client;
    private IServerSettings settings;
    private boolean initialized = false;
    private boolean canProvideCompletions = false;

    private final IDocumentHandler documentHandler = new NoDocumentHandler();
    private ServerCapabilities capabilities;

    public XMLLanguageServer() {}

    public void setupSDK(@NonNull final SDKInfo info) {
        this.sdkInfo = info;
        this.canProvideCompletions = true;
    }

    @NonNull
    public IServerSettings getSettings() {
        if (settings == null) {
            settings = new DefaultXMLServerSettings();
        }
        return settings;
    }

    @Override
    public void initialize(@NonNull InitializeParams params) throws AlreadyInitializedException {
        if (initialized) {
            throw new AlreadyInitializedException();
        }

        capabilities = new ServerCapabilities();
        capabilities.setCompletionsAvailable(true);
        capabilities.setCodeAnalysisAvailable(true);
        capabilities.setSignatureHelpAvailable(false);
        capabilities.setReferencesAvailable(false);
        capabilities.setDefinitionsAvailable(false);
        capabilities.setSmartSelectionsEnabled(false);

        initialized = true;
    }

    @NonNull
    @Override
    public ServerCapabilities getCapabilities() {
        return capabilities;
    }

    @Override
    public void shutdown() {}

    @Override
    public void connectClient(ILanguageClient client) {
        this.client = client;
    }

    @Nullable
    @Override
    public ILanguageClient getClient() {
        return this.client;
    }

    @Override
    public void applySettings(IServerSettings settings) {
        this.settings = settings;
    }

    @Override
    public void configurationChanged(Object newConfiguration) {}

    @NonNull
    @Override
    public ICompletionProvider getCompletionProvider() {
        if (!getSettings().completionsEnabled() || !canProvideCompletions) {
            return new NoCompletionsProvider();
        }

        return new CompletionProvider(this.sdkInfo, this.getSettings());
    }

    @NonNull
    @Override
    public ICodeActionProvider getCodeActionProvider() {
        return new NoCodeActionsProvider();
    }

    @NonNull
    @Override
    public IReferenceProvider getReferenceProvider() {
        return new NoReferenceProvider();
    }

    @NonNull
    @Override
    public IDefinitionProvider getDefinitionProvider() {
        return new NoDefinitionProvider();
    }

    @NonNull
    @Override
    public ISelectionProvider getSelectionProvider() {
        return new NoSelectionProvider();
    }

    @NonNull
    @Override
    public ISignatureHelpProvider getSignatureHelpProvider() {
        return new NoSignatureHelpProvider();
    }

    @NonNull
    @Override
    public IDocumentHandler getDocumentHandler() {
        return this.documentHandler;
    }

    @NonNull
    @Override
    public IDiagnosticProvider getCodeAnalyzer() {
        return new NoDiagnosticProvider();
    }
}
