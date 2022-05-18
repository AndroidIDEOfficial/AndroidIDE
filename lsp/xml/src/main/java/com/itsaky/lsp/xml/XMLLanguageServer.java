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
import androidx.annotation.RestrictTo;

import com.itsaky.lsp.api.ICompletionProvider;
import com.itsaky.lsp.api.IDocumentHandler;
import com.itsaky.lsp.api.ILanguageClient;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.api.IServerSettings;
import com.itsaky.lsp.models.DefinitionParams;
import com.itsaky.lsp.models.DefinitionResult;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.ExpandSelectionParams;
import com.itsaky.lsp.models.InitializeParams;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.ReferenceParams;
import com.itsaky.lsp.models.ReferenceResult;
import com.itsaky.lsp.models.ServerCapabilities;
import com.itsaky.lsp.models.SignatureHelp;
import com.itsaky.lsp.models.SignatureHelpParams;
import com.itsaky.lsp.util.NoCompletionsProvider;
import com.itsaky.lsp.util.NoDocumentHandler;
import com.itsaky.lsp.xml.models.XMLServerSettings;
import com.itsaky.lsp.xml.providers.CodeFormatProvider;
import com.itsaky.lsp.xml.providers.XmlCompletionProvider;
import com.itsaky.sdk.SDKInfo;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * Language server implementation for XML files.
 *
 * @author Akash Yadav
 */
public class XMLLanguageServer implements ILanguageServer {

  private final IDocumentHandler documentHandler = new NoDocumentHandler();

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  public SDKInfo sdkInfo;

  private ILanguageClient client;
  private IServerSettings settings;
  private boolean initialized = false;
  private boolean canProvideCompletions = false;
  private ServerCapabilities capabilities;

  public XMLLanguageServer() {}

  public void setupSDK(@NonNull final SDKInfo info) {
    this.sdkInfo = info;
    this.canProvideCompletions = true;
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

  @Override
  public boolean isInitialized() {
    return initialized;
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

    return new XmlCompletionProvider(this.sdkInfo, this.getSettings());
  }

  @NonNull
  public IServerSettings getSettings() {
    if (settings == null) {
      settings = XMLServerSettings.getInstance();
    }

    return settings;
  }

  @NonNull
  @Override
  public ReferenceResult findReferences(@NonNull ReferenceParams params) {
    return new ReferenceResult(Collections.emptyList());
  }

  @NonNull
  @Override
  public DefinitionResult findDefinition(@NonNull DefinitionParams params) {
    return new DefinitionResult(Collections.emptyList());
  }

  @NonNull
  @Override
  public Range expandSelection(@NonNull ExpandSelectionParams params) {
    return params.getSelection();
  }

  @NonNull
  @Override
  public SignatureHelp signatureHelp(@NonNull SignatureHelpParams params) {
    return new SignatureHelp(Collections.emptyList(), -1, -1);
  }

  @NonNull
  @Override
  public List<DiagnosticItem> analyze(@NonNull Path file) {
    return Collections.emptyList();
  }

  @NonNull
  @Override
  public CharSequence formatCode(CharSequence input) {
    return new CodeFormatProvider().format(input);
  }

  @NonNull
  @Override
  public IDocumentHandler getDocumentHandler() {
    return this.documentHandler;
  }
}
