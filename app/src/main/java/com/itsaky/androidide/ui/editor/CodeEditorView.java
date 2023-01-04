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
package com.itsaky.androidide.ui.editor;

import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.FLAG_LINE_BREAK;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.FLAG_PASSWORD;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.FLAG_WS_EMPTY_LINE;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.FLAG_WS_INNER;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.FLAG_WS_LEADING;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.FLAG_WS_TRAILING;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.FONT_LIGATURES;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.FONT_SIZE;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.USE_CUSTOM_FONT;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.USE_ICU;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.USE_MAGNIFER;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.WORD_WRAP;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.getDrawEmptyLineWs;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.getDrawInnerWs;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.getDrawLeadingWs;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.getDrawLineBreak;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.getDrawTrailingWs;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.getFontLigatures;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.getFontSize;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.getUseIcu;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.getUseMagnifier;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.editor.databinding.LayoutCodeEditorBinding;
import com.itsaky.androidide.editor.language.treesitter.TreeSitterLanguage;
import com.itsaky.androidide.editor.language.cpp.CppLanguage;
import com.itsaky.androidide.editor.language.groovy.GroovyLanguage;
import com.itsaky.androidide.editor.language.java.JavaLanguage;
import com.itsaky.androidide.editor.language.kotlin.KotlinLanguage;
import com.itsaky.androidide.editor.language.xml.XMLLanguage;
import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider;
import com.itsaky.androidide.editor.ui.EditorSearchLayout;
import com.itsaky.androidide.editor.ui.IDEEditor;
import com.itsaky.androidide.eventbus.events.preferences.PreferenceChangeEvent;
import com.itsaky.androidide.lsp.IDELanguageClientImpl;
import com.itsaky.androidide.lsp.api.ILanguageServer;
import com.itsaky.androidide.lsp.api.ILanguageServerRegistry;
import com.itsaky.androidide.lsp.java.JavaLanguageServer;
import com.itsaky.androidide.lsp.xml.XMLLanguageServer;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.preferences.internal.EditorPreferencesKt;
import com.itsaky.androidide.syntax.colorschemes.DynamicColorScheme;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.TypefaceUtilsKt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import io.github.rosemoe.sora.lang.EmptyLanguage;
import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.text.LineSeparator;
import io.github.rosemoe.sora.widget.component.Magnifier;

/**
 * A view that handles opened code editors.
 *
 * @author Akash Yadav
 */
@SuppressLint("ViewConstructor") // This view is always dynamically created.
public class CodeEditorView extends LinearLayout {

  private static final ILogger LOG = ILogger.newInstance("CodeEditorView");
  private final File file;
  private final LayoutCodeEditorBinding binding;
  private final EditorSearchLayout searchLayout;

  public CodeEditorView(
      @NonNull Context context, @NonNull File file, final @NonNull Range selection) {
    super(context);
    this.file = file;

    final var inflater = LayoutInflater.from(context);
    this.binding = LayoutCodeEditorBinding.inflate(inflater);
    this.binding.editor.setHighlightCurrentBlock(true);
    this.binding.editor.getProps().autoCompletionOnComposing = true;
    this.binding.editor.setDividerWidth(SizeUtils.dp2px(1));
    this.binding.editor.setColorScheme(SchemeAndroidIDE.newInstance(context));
    this.binding.editor.setLineSeparator(LineSeparator.LF);

    this.searchLayout = new EditorSearchLayout(context, this.binding.editor);

    this.binding.diagnosticTextContainer.setVisibility(GONE);

    removeAllViews();
    setOrientation(VERTICAL);

    addView(this.binding.getRoot(), new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
    addView(
        this.searchLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

    CompletableFuture.runAsync(
        () -> {
          final var contents = FileIOUtils.readFile2String(file);
          final var editor = getEditor();
          editor.post(() -> {
            editor.setText(contents, createEditorArgs());
            postRead();
            
            selection.validate();
            editor.validateRange(selection);
            editor.setSelection(selection);
          });
        });

    configureEditorIfNeeded();
  }

  @NonNull
  private Bundle createEditorArgs() {
    var bundle = new Bundle();
    if (getFile() == null) {
      return bundle;
    }

    bundle.putString(IDEEditor.KEY_FILE, getFile().getAbsolutePath());
    return bundle;
  }

  @Nullable
  public File getFile() {
    return file;
  }

  public IDEEditor getEditor() {
    return binding.editor;
  }

  protected void postRead() {
    final var language = createLanguage(file);
    if (language instanceof TreeSitterLanguage) {
      IDEColorSchemeProvider.INSTANCE.readScheme(getContext(), scheme -> {
        applyTreeSitterLang(language, scheme);
      });
    } else {
      binding.editor.setEditorLanguage(language);
    }
    
    binding.editor.setLanguageServer(createLanguageServer(file));

    if (IDELanguageClientImpl.isInitialized()) {
      binding.editor.setLanguageClient(IDELanguageClientImpl.getInstance());
    }

    // File must be set only after setting the language server
    // This will make sure that textDocument/didOpen is sent
    binding.editor.setFile(getFile());

    if (getContext() instanceof Activity) {
      ((Activity) getContext()).invalidateOptionsMenu();
    }
  }
  
  private void applyTreeSitterLang(final Language language, SchemeAndroidIDE scheme) {
    if (scheme == null) {
      LOG.error("Failed to read current color scheme");
      scheme = SchemeAndroidIDE.newInstance(getContext());
    }
    
    if (scheme instanceof DynamicColorScheme) {
      ((DynamicColorScheme) scheme).apply(getContext());
    }
    
    binding.editor.setColorScheme(scheme);
    binding.editor.setEditorLanguage(language);
  }
  
  private ILanguageServer createLanguageServer(File file) {
    if (!file.isFile()) {
      return null;
    }

    String ext = FileUtils.getFileExtension(file);
    var serverID = "";
    switch (ext) {
      case "java":
        serverID = JavaLanguageServer.SERVER_ID;
        break;
      case "xml":
        serverID = XMLLanguageServer.SERVER_ID;
        break;
      default:
        return null;
    }

    return ILanguageServerRegistry.getDefault().getServer(serverID);
  }

  private Language createLanguage(File file) {
    if (file.isFile()) {
      String ext = FileUtils.getFileExtension(file);
      switch (ext) {
        case "java":
          return new JavaLanguage(getContext());
        case "xml":
          return new XMLLanguage(getContext());
        case "gradle":
          return new GroovyLanguage();
        case "kt":
        case "kts":
          return new KotlinLanguage();
        case "c":
        case "h":
        case "cc":
        case "cpp":
        case "cxx":
          return new CppLanguage();
        default:
          return new EmptyLanguage();
      }
    }
    return new EmptyLanguage();
  }

  private void configureEditorIfNeeded() {
    onCustomFontPrefChanged();
    onFontSizePrefChanged();
    onFontLigaturesPrefChanged();
    onPrintingFlagsPrefChanged();
    onInputTypePrefChanged();
    onWordwrapPrefChanged();
    onMagnifierPrefChanged();
    onUseIcuPrefChanged();
  }

  protected void onMagnifierPrefChanged() {
    binding.editor.getComponent(Magnifier.class).setEnabled(getUseMagnifier());
  }

  protected void onWordwrapPrefChanged() {
    var enabled = EditorPreferencesKt.getWordwrap();
    binding.editor.setWordwrap(enabled);
  }

  protected void onInputTypePrefChanged() {
    binding.editor.setInputType(IDEEditor.createInputFlags());
  }

  protected void onPrintingFlagsPrefChanged() {
    int flags = 0;
    if (getDrawLeadingWs()) {
      flags |= IDEEditor.FLAG_DRAW_WHITESPACE_LEADING;
    }

    if (getDrawTrailingWs()) {
      flags |= IDEEditor.FLAG_DRAW_WHITESPACE_TRAILING;
    }

    if (getDrawInnerWs()) {
      flags |= IDEEditor.FLAG_DRAW_WHITESPACE_INNER;
    }

    if (getDrawEmptyLineWs()) {
      flags |= IDEEditor.FLAG_DRAW_WHITESPACE_FOR_EMPTY_LINE;
    }

    if (getDrawLineBreak()) {
      flags |= IDEEditor.FLAG_DRAW_LINE_SEPARATOR;
    }

    binding.editor.setNonPrintablePaintingFlags(flags);
  }

  protected void onFontLigaturesPrefChanged() {
    var enabled = getFontLigatures();
    binding.editor.setLigatureEnabled(enabled);
  }

  protected void onFontSizePrefChanged() {
    float textSize = getFontSize();
    if (textSize < 6 || textSize > 32) {
      textSize = 14;
    }

    binding.editor.setTextSize(textSize);
  }

  private void onUseIcuPrefChanged() {
    binding.editor.getProps().useICULibToSelectWords = getUseIcu();
  }

  private void onCustomFontPrefChanged() {
    var state = EditorPreferencesKt.getUseCustomFont();
    binding.editor.setTypefaceText(TypefaceUtilsKt.customOrJBMono(state));
    binding.editor.setTypefaceLineNumber(TypefaceUtilsKt.customOrJBMono(state));
  }

  /**
   * For internal use only!
   *
   * <p>Marks this editor as unmodified. Used only when the activity is being destroyed.
   */
  public void markUnmodified() {
    binding.editor.markUnmodified();
  }

  /**
   * For internal use only!
   *
   * <p>Marks this editor as modified.
   */
  public void markModified() {
    binding.editor.markModified();
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  @SuppressWarnings("unused")
  public void onPreferenceChanged(PreferenceChangeEvent event) {
    if (binding == null) {
      return;
    }

    final var prefs = BaseApplication.getBaseInstance().getPrefManager();
    switch (event.getKey()) {
      case FONT_SIZE:
        onFontSizePrefChanged();
        break;
      case FONT_LIGATURES:
        onFontLigaturesPrefChanged();
        break;
      case FLAG_LINE_BREAK:
      case FLAG_WS_INNER:
      case FLAG_WS_EMPTY_LINE:
      case FLAG_WS_LEADING:
      case FLAG_WS_TRAILING:
        onPrintingFlagsPrefChanged();
        break;
      case FLAG_PASSWORD:
        onInputTypePrefChanged();
        break;
      case WORD_WRAP:
        onWordwrapPrefChanged();
        break;
      case USE_MAGNIFER:
        onMagnifierPrefChanged();
        break;
      case USE_ICU:
        onUseIcuPrefChanged();
        break;
      case USE_CUSTOM_FONT:
        onCustomFontPrefChanged();
        break;
    }
  }

  public boolean save() {
    final var file = getFile();
    if (file == null) {
      LOG.error("Cannot save file. File instance is null.");
      return false;
    }

    if (!isModified() && file.exists()) {
      LOG.info(file.getName());
      LOG.info("File was not modified. Skipping save operation.");
      return false;
    }

    final var text = getEditor().getText().toString();

    try {
      FileUtil.writeFile(file, text);
      notifySaved();
      return true;
    } catch (IOException io) {
      LOG.error("Failed to save file", file, io);
      return false;
    }
  }

  public boolean isModified() {
    return binding.editor.isModified();
  }

  private void notifySaved() {
    binding.editor.dispatchDocumentSaveEvent();
  }

  public LayoutCodeEditorBinding getBinding() {
    return binding;
  }

  public String getText() {
    return binding.editor.getText().toString();
  }

  public void onEditorSelected() {
    final var editor = getEditor();
    if (editor != null) {
      editor.onEditorSelected();
    }
  }

  public void beginSearch() {
    if (this.binding == null || searchLayout == null) {
      LOG.warn("Editor layout is null content=" + binding + ", searchLayout=" + searchLayout);
      return;
    }

    searchLayout.beginSearchMode();
  }

  /** Mark this files as saved. Even if it not saved. */
  public void markAsSaved() {
    notifySaved();
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this);
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    EventBus.getDefault().unregister(this);
  }
}
