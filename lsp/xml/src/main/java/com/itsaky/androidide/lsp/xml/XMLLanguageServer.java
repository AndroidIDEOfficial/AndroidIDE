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

package com.itsaky.androidide.lsp.xml;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent;
import com.itsaky.androidide.lsp.api.ICompletionProvider;
import com.itsaky.androidide.lsp.api.ILanguageClient;
import com.itsaky.androidide.lsp.api.ILanguageServer;
import com.itsaky.androidide.lsp.api.IServerSettings;
import com.itsaky.androidide.lsp.models.CodeFormatResult;
import com.itsaky.androidide.lsp.models.CompletionParams;
import com.itsaky.androidide.lsp.models.CompletionResult;
import com.itsaky.androidide.lsp.models.DefinitionParams;
import com.itsaky.androidide.lsp.models.DefinitionResult;
import com.itsaky.androidide.lsp.models.DiagnosticResult;
import com.itsaky.androidide.lsp.models.ExpandSelectionParams;
import com.itsaky.androidide.lsp.models.FormatCodeParams;
import com.itsaky.androidide.lsp.models.ReferenceParams;
import com.itsaky.androidide.lsp.models.ReferenceResult;
import com.itsaky.androidide.lsp.models.SignatureHelp;
import com.itsaky.androidide.lsp.models.SignatureHelpParams;
import com.itsaky.androidide.lsp.util.NoCompletionsProvider;
import com.itsaky.androidide.lsp.xml.models.XMLServerSettings;
import com.itsaky.androidide.lsp.xml.providers.AdvancedEditProvider;
import com.itsaky.androidide.lsp.xml.providers.CodeFormatProvider;
import com.itsaky.androidide.lsp.xml.providers.XmlCompletionProvider;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.progress.ICancelChecker;
import com.itsaky.androidide.projects.api.Project;

import com.itsaky.androidide.utils.DocumentUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Collections;

/**
 * Language server implementation for XML files.
 *
 * @author Akash Yadav
 */
public class XMLLanguageServer implements ILanguageServer {

  public static final String SERVER_ID = "xml";

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  private ILanguageClient client;

  private IServerSettings settings;

  public XMLLanguageServer() {
    EventBus.getDefault().register(this);
  }

  @Override
  public String getServerId() {
    return SERVER_ID;
  }

  @Override
  public void shutdown() {
    if (EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().unregister(this);
    }
  }

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
  public void setupWithProject(@NonNull final Project project) {}

  @NonNull
  @Override
  public CompletionResult complete(final CompletionParams params, ICancelChecker cancelChecker) {
    final ICompletionProvider completionProvider;
    if (!getSettings().completionsEnabled()) {
      completionProvider = new NoCompletionsProvider();
    } else {
      completionProvider = new XmlCompletionProvider(this.getSettings());
    }
    return completionProvider.complete(params);
  }

  @NonNull
  public IServerSettings getSettings() {
    if (settings == null) {
      settings = XMLServerSettings.INSTANCE;
    }

    return settings;
  }

  @NonNull
  @Override
  public ReferenceResult findReferences(@NonNull ReferenceParams params,
      ICancelChecker cancelChecker) {
    return new ReferenceResult(Collections.emptyList());
  }

  @NonNull
  @Override
  public DefinitionResult findDefinition(@NonNull DefinitionParams params,
      ICancelChecker cancelChecker) {
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
  public DiagnosticResult analyze(@NonNull Path file) {
    return DiagnosticResult.NO_UPDATE;
  }

  @NonNull
  @Override
  public CodeFormatResult formatCode(FormatCodeParams params) {
    return new CodeFormatProvider().format(params);
  }

  @Subscribe(threadMode = ThreadMode.BACKGROUND)
  public void onDocumentChange(DocumentChangeEvent event) {
    if (!DocumentUtils.isXmlFile(event.getChangedFile())) {
      return;
    }

    AdvancedEditProvider.INSTANCE.onContentChange(event);
  }
}
