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
package com.itsaky.androidide.views.editor;

import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.FLAG_LINE_BREAK;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.FLAG_PASSWORD;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.FLAG_WS_EMPTY_LINE;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.FLAG_WS_INNER;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.FLAG_WS_LEADING;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.FLAG_WS_TRAILING;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.FONT_LIGATURES;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.FONT_SIZE;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.USE_ICU;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.USE_MAGNIFER;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.WORD_WRAP;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getDrawEmptyLineWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getDrawInnerWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getDrawLeadingWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getDrawLineBreak;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getDrawTrailingWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getFontLigatures;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getFontSize;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getUseIcu;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getUseMagnifier;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutCodeEditorBinding;
import com.itsaky.androidide.eventbus.events.preferences.PreferenceChangeEvent;
import com.itsaky.androidide.language.cpp.CppLanguage;
import com.itsaky.androidide.language.groovy.GroovyLanguage;
import com.itsaky.androidide.language.java.JavaLanguage;
import com.itsaky.androidide.language.kotlin.KotlinLanguage;
import com.itsaky.androidide.language.xml.XMLLanguage;
import com.itsaky.androidide.lexers.xml.XMLLexer;
import com.itsaky.androidide.lsp.api.ILanguageServer;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.models.prefs.EditorPreferencesKt;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.inflater.values.ValuesTableFactory;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.lang.EmptyLanguage;
import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.widget.component.Magnifier;

/**
 * A view that handles opened code editors.
 *
 * @author Akash Yadav
 */
@SuppressLint("ViewConstructor") // This view is always dynamically created.
public class CodeEditorView extends FrameLayout {

  private static final ILogger LOG = ILogger.newInstance("CodeEditorView");
  private final File file;
  private final LayoutCodeEditorBinding binding;
  private boolean isModified;

  public CodeEditorView(
      @NonNull Context context, @NonNull File file, final @NonNull Range selection) {
    super(context);
    this.file = file;
    this.isModified = false;

    this.binding = LayoutCodeEditorBinding.inflate(LayoutInflater.from(context));
    this.binding.editor.setTypefaceText(TypefaceUtils.jetbrainsMono());
    this.binding.editor.setHighlightCurrentBlock(true);
    this.binding.editor.getProps().autoCompletionOnComposing = true;
    this.binding.editor.setDividerWidth(SizeUtils.dp2px(1));
    this.binding.editor.setColorScheme(new SchemeAndroidIDE());
    this.binding.editor.subscribeEvent(
        ContentChangeEvent.class, ((event, unsubscribe) -> handleContentChange(event)));

    this.binding.diagnosticTextContainer.setVisibility(GONE);

    removeAllViews();
    addView(this.binding.getRoot());

    selection.validate();
    CompletableFuture.runAsync(
        () -> {
          final var contents = FileIOUtils.readFile2String(file);
          binding.editor.post(
              () -> {
                binding.editor.setText(contents, createEditorArgs());
                postRead();
                if (LSPUtils.isEqual(selection.getStart(), selection.getEnd())) {
                  getEditor()
                      .setSelection(
                          selection.getStart().getLine(), selection.getStart().getColumn());
                } else {
                  getEditor().setSelection(selection);
                }
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

  private void handleContentChange(@NonNull ContentChangeEvent event) {
    isModified = true;

    if (event.getAction() == ContentChangeEvent.ACTION_INSERT) {
      final var editor = event.getEditor();
      final var content = event.getChangedText();
      final var endLine = event.getChangeEnd().line;
      final var endColumn = event.getChangeEnd().column;
      if (file.getName().endsWith(".xml")) {
        boolean isOpen = false;
        try {
          isOpen = editor.getText().charAt(editor.getCursor().getLeft() - 2) == '<';
        } catch (Throwable th) {
          LOG.error(th);
        }

        if (isOpen && "/".contentEquals(content)) {
          closeCurrentTag(editor.getText().toString(), endLine, endColumn);
        }
      }
    }
  }

  private void closeCurrentTag(String text, int line, int col) {
    try {
      XMLLexer lexer = new XMLLexer(CharStreams.fromReader(new StringReader(text)));
      Token token;
      boolean wasSlash = false, wasOpen = false;
      ArrayList<String> currentNames = new ArrayList<>();
      while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
        final int type = token.getType();
        if (type == XMLLexer.OPEN) {
          wasOpen = true;
        } else if (type == XMLLexer.Name) {
          if (wasOpen && wasSlash && currentNames.size() > 0) {
            currentNames.remove(0);
          } else if (wasOpen) {
            currentNames.add(0, token.getText());
            wasOpen = false;
          }
        } else if (type == XMLLexer.OPEN_SLASH) {
          int l = token.getLine() - 1;
          int c = token.getCharPositionInLine();
          if (l == line && c == col) {
            break;
          } else if (currentNames.size() > 0) {
            currentNames.remove(0);
          }
        } else if (type == XMLLexer.SLASH_CLOSE || type == XMLLexer.SPECIAL_CLOSE) {
          if (currentNames.size() > 0 && token.getText().trim().endsWith("/>")) {
            currentNames.remove(0);
          }
        } else if (type == XMLLexer.SLASH) {
          wasSlash = true;
        } else {
          wasOpen = wasSlash = false;
        }
      }

      if (currentNames.size() > 0) {
        binding.editor.getText().insert(line, col + 2, currentNames.get(0));
      }
    } catch (Throwable th) {
      LOG.error("Unable to close current tag", th);
    }
  }

  public IDEEditor getEditor() {
    return binding.editor;
  }

  protected void postRead() {
    binding.editor.setEditorLanguage(createLanguage(file));
    binding.editor.setLanguageServer(createLanguageServer(file));
    // File must be set only after setting the language server
    // This will make sure that textDocument/didOpen is sent
    binding.editor.setFile(getFile());

    if (getContext() instanceof Activity) {
      ((Activity) getContext()).invalidateOptionsMenu();
    }
  }

  private ILanguageServer createLanguageServer(File file) {
    if (file.isFile()) {
      String ext = FileUtils.getFileExtension(file);
      switch (ext) {
        case "java":
          return StudioApp.getInstance().getJavaLanguageServer();
        case "xml":
          return StudioApp.getInstance().getXMLLanguageServer();
        default:
          return null;
      }
    }
    return null;
  }

  private Language createLanguage(File file) {
    if (file.isFile()) {
      String ext = FileUtils.getFileExtension(file);
      switch (ext) {
        case "java":
          return new JavaLanguage();
        case "xml":
          return new XMLLanguage();
        case "gradle":
          return new GroovyLanguage();
        case "kt":
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

  /**
   * For internal use only!
   *
   * <p>Marks this editor as unmodified. Used only when the activity is being destroyed.
   */
  public void markUnmodified() {
    isModified = false;
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  @SuppressWarnings("unused")
  public void onPreferenceChanged(PreferenceChangeEvent event) {
    if (binding == null) {
      return;
    }

    final var prefs = StudioApp.getInstance().getPrefManager();
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
      isModified = false;
      return true;
    } catch (IOException io) {
      LOG.error("Failed to save file", file, io);
      return false;
    }
  }

  public boolean isModified() {
    return isModified;
  }

  private void notifySaved() {
    binding.editor.dispatchDocumentSaveEvent();
    if (Objects.requireNonNull(getFile()).getName().endsWith(".xml")) {
      ValuesTableFactory.syncWithFile(getFile());
    }
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

  public void undo() {
    if (binding.editor.canUndo()) {
      binding.editor.undo();
    }
  }

  public void redo() {
    if (binding.editor.canRedo()) {
      binding.editor.redo();
    }
  }

  public void beginSearch() {
    binding.editor.beginSearchMode();
  }

  /** Mark this files as saved. Even if it not saved. */
  public void markAsSaved() {
    isModified = false;
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
