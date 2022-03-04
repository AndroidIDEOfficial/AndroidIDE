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
package com.itsaky.androidide.views.editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.androidide.adapters.CompletionListAdapter;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutCodeEditorBinding;
import com.itsaky.androidide.language.groovy.GroovyLanguage;
import com.itsaky.androidide.language.java.JavaLanguage;
import com.itsaky.androidide.language.xml.XMLLanguage;
import com.itsaky.androidide.lexers.xml.XMLLexer;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.inflater.values.ValuesTableFactory;
import com.itsaky.lsp.models.Range;

import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.lang.EmptyLanguage;
import io.github.rosemoe.sora.text.Content;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A view that handles opened code editors.
 *
 * @author Akash Yadav
 */
@SuppressLint("ViewConstructor") // This view is always dynamically created.
public class CodeEditorView extends FrameLayout {

    private final File file;
    private final LayoutCodeEditorBinding binding;

    private boolean isModified;
    private boolean isFirstCreate;

    private static final Logger LOG = Logger.instance("CodeEditorView");

    public CodeEditorView(
            @NonNull Context context, @NonNull File file, final @NonNull Range selection) {
        super(context);
        this.file = file;
        this.isModified = false;
        this.isFirstCreate = true;

        this.binding = LayoutCodeEditorBinding.inflate(LayoutInflater.from(context));
        this.binding.editor.setTypefaceText(TypefaceUtils.jetbrainsMono());
        this.binding.editor.setHighlightCurrentBlock(true);
        this.binding.editor.getProps().autoCompletionOnComposing = true;
        this.binding.editor.setAutoCompletionItemAdapter(new CompletionListAdapter());
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
                                                    selection.getStart().getLine(),
                                                    selection.getStart().getColumn());
                                } else {
                                    getEditor()
                                            .setSelectionRegion(
                                                    selection.getStart().getLine(),
                                                    selection.getStart().getColumn(),
                                                    selection.getEnd().getLine(),
                                                    selection.getEnd().getColumn());
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

    public boolean save() {

        if (getFile() == null) {
            LOG.error("Cannot save file. File instance is null.");
            return false;
        }

        final var path = getFile().toPath();
        if (!isModified() && Files.exists(path)) {
            LOG.info(getFile().getName());
            LOG.info("File was not modified. Skipping save operation.");
            return false;
        }

        final var lines = getLines(getEditor().getText());
        try {

            Files.write(getFile().toPath(), lines);
            notifySaved();
            isModified = false;

            return true;
        } catch (Throwable e) {
            LOG.error("Failed to save file", getFile(), "\n", e);
            return false;
        }
    }

    @NonNull
    private List<CharSequence> getLines(Content text) {
        final var count = text.getLineCount();
        final var result = new ArrayList<CharSequence>();

        for (int i = 0; i < count; i++) {
            result.add(text.getLine(i));
        }

        return result;
    }

    public IDEEditor getEditor() {
        return binding.editor;
    }

    public LayoutCodeEditorBinding getBinding() {
        return binding;
    }

    @Nullable
    public File getFile() {
        return file;
    }

    public String getText() {
        return binding.editor.getText().toString();
    }

    public boolean isModified() {
        return isModified;
    }

    public void onPause() {
        // unimplemented
    }

    public void onResume() {
        configureEditorIfNeeded();
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

    public void findDefinition() {
        binding.editor.findDefinition();
    }

    public void findReferences() {
        binding.editor.findReferences();
    }

    public void commentLine() {
        binding.editor.commentLine();
    }

    public void uncommentLine() {
        binding.editor.uncommentLine();
    }

    public void beginSearch() {
        binding.editor.beginSearchMode();
    }

    protected void postRead() {
        if (file.isFile() && file.getName().endsWith(".java")) {
            binding.editor.setEditorLanguage(new JavaLanguage());
            binding.editor.setLanguageServer(StudioApp.getInstance().getJavaLanguageServer());
        } else if (file.isFile() && file.getName().endsWith(".xml")) {
            binding.editor.setEditorLanguage(new XMLLanguage());
            binding.editor.setLanguageServer(StudioApp.getInstance().getXMLLanguageServer());
        } else if (file.isFile() && file.getName().endsWith(".gradle")) {
            binding.editor.setEditorLanguage(new GroovyLanguage());
        } else {
            binding.editor.setEditorLanguage(new EmptyLanguage());
        }

        // File must be set only after setting the language server
        // This will make sure that textDocument/didOpen is sent
        binding.editor.setFile(getFile());
    }

    private void configureEditorIfNeeded() {
        boolean sizeChanged = isFirstCreate || ConstantsBridge.EDITOR_PREF_SIZE_CHANGED;
        boolean ligaturesChanged = isFirstCreate || ConstantsBridge.EDITOR_PREF_LIGATURES_CHANGED;
        boolean flagsChanged = isFirstCreate || ConstantsBridge.EDITOR_PREF_FLAGS_CHANGED;
        boolean drawHexChanged = isFirstCreate || ConstantsBridge.EDITOR_PREF_DRAW_HEX_CHANGED;
        final PreferenceManager prefs = StudioApp.getInstance().getPrefManager();

        if (sizeChanged) {
            float textSize = prefs.getFloat(PreferenceManager.KEY_EDITOR_FONT_SIZE);
            if (textSize < 6 || textSize > 32) {
                textSize = 14;
            }

            binding.editor.setTextSize(textSize);
            ConstantsBridge.EDITOR_PREF_SIZE_CHANGED = false;
        }

        if (ligaturesChanged) {
            var enabled = prefs.getBoolean(PreferenceManager.KEY_EDITOR_FONT_LIGATURES, true);
            binding.editor.setLigatureEnabled(enabled);
            ConstantsBridge.EDITOR_PREF_LIGATURES_CHANGED = false;
        }

        if (flagsChanged) {
            int flags = 0;
            if (prefs.getBoolean(PreferenceManager.KEY_EDITOR_FLAG_WS_LEADING, true)) {
                flags |= IDEEditor.FLAG_DRAW_WHITESPACE_LEADING;
            }

            if (prefs.getBoolean(PreferenceManager.KEY_EDITOR_FLAG_WS_TRAILING, false)) {
                flags |= IDEEditor.FLAG_DRAW_WHITESPACE_TRAILING;
            }

            if (prefs.getBoolean(PreferenceManager.KEY_EDITOR_FLAG_WS_INNER, true)) {
                flags |= IDEEditor.FLAG_DRAW_WHITESPACE_INNER;
            }

            if (prefs.getBoolean(PreferenceManager.KEY_EDITOR_FLAG_WS_EMPTY_LINE, true)) {
                flags |= IDEEditor.FLAG_DRAW_WHITESPACE_FOR_EMPTY_LINE;
            }

            if (prefs.getBoolean(PreferenceManager.KEY_EDITOR_FLAG_LINE_BREAK, true)) {
                flags |= IDEEditor.FLAG_DRAW_LINE_SEPARATOR;
            }

            binding.editor.setNonPrintablePaintingFlags(flags);
            ConstantsBridge.EDITOR_PREF_FLAGS_CHANGED = false;
        }

        if (drawHexChanged) {
            // TODO Do something about this
            //     Maybe use spans...
            //            binding.editor.setLineColorsEnabled (prefs.getBoolean
            // (PreferenceManager.KEY_EDITOR_DRAW_HEX, true));
            ConstantsBridge.EDITOR_PREF_DRAW_HEX_CHANGED = false;
        }

        isFirstCreate = false;
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

    private void notifySaved() {
        binding.editor.didSave();

        if (Objects.requireNonNull(getFile()).getName().endsWith(".xml")) {
            ValuesTableFactory.syncWithFile(getFile());
        }
    }

    /** Mark this files as saved. Even if it not saved. */
    public void markAsSaved() {
        isModified = false;
        notifySaved();
    }
}
