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

package com.itsaky.lsp.xml.providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.lexers.xml.XMLLexer;
import com.itsaky.androidide.utils.CharSequenceReader;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.attrinfo.models.Attr;
import com.itsaky.lsp.api.AbstractServiceProvider;
import com.itsaky.lsp.api.ICompletionProvider;
import com.itsaky.lsp.api.IServerSettings;
import com.itsaky.lsp.models.CompletionData;
import com.itsaky.lsp.models.CompletionItem;
import com.itsaky.lsp.models.CompletionItemKind;
import com.itsaky.lsp.models.CompletionParams;
import com.itsaky.lsp.models.CompletionResult;
import com.itsaky.lsp.models.InsertTextFormat;
import com.itsaky.lsp.xml.R;
import com.itsaky.sdk.SDKInfo;
import com.itsaky.widgets.models.Widget;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.Contract;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Completion provider for the XML Language
 *
 * @author Akash Yadav
 */
public class CompletionProvider extends AbstractServiceProvider implements ICompletionProvider {

    private static final Logger LOG = Logger.instance("XMLCompletionProvider");
    private static final String APP_NS = "http://schemas.android.com/apk/res/auto";
    private static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    private static final String TOOLS_NS = "http://schemas.android.com/tools";

    private static final String INITIAL_TAG_ATTRIBUTES =
            "android:layout_width=\"wrap_content\"\n" + "android:layout_height=\"wrap_content\"";

    private final SDKInfo sdkInfo;

    public CompletionProvider(SDKInfo sdkInfo, IServerSettings settings) {
        Objects.requireNonNull(sdkInfo);
        Objects.requireNonNull(settings);

        this.sdkInfo = sdkInfo;
    }

    @Override
    public boolean canComplete(Path file) {
        return ICompletionProvider.super.canComplete(file)
                && file.toFile().getName().endsWith(".xml");
    }

    @NonNull
    @Override
    public CompletionResult complete(@NonNull CompletionParams params) {
        final var index = params.getPosition().requireIndex();
        final var contents = params.requireContents();
        final var prefix = params.requirePrefix();
        try {
            return complete(contents, prefix, getFileType(params.getFile()), index);
        } catch (Exception e) {
            LOG.error("Unable to provide XML completions");
            return EMPTY;
        }
    }

    private String getFileType(Path path) {
        final var file = path.toFile();
        if (file.getName().equals("AndroidManifest.xml")) {
            return "manifest";
        }

        final var resPattern = Pattern.compile(".*/src/.*/res");
        //noinspection ConstantConditions
        if (!resPattern.matcher(file.getParentFile().getParentFile().getAbsolutePath()).matches()) {
            return null;
        }

        final var parent = file.getParentFile().getName();

        if (parent.startsWith("drawable")
                || parent.startsWith("mipmap")
                || parent.startsWith("color")) {
            return "drawable";
        } else if (parent.startsWith("values")) {
            return "values";
        } else if (parent.startsWith("layout")) {
            return "layout";
        }

        return null;
    }

    @NonNull
    private CompletionResult complete(
            final CharSequence contents,
            @NonNull final String prefix,
            final String fileType,
            final int index) {
        if (fileType == null) {
            return EMPTY;
        }

        final var result = new CompletionResult();
        final var attrs = sdkInfo.getAttrInfo();
        // final var parser = XmlUtils.newParser (contents);

        if (prefix.startsWith("<") || prefix.startsWith("</")) {
            if ("layout".equals(fileType)) {
                var newPrefix = prefix.substring(1);
                var slash = false;
                if (newPrefix.startsWith("/")) {
                    slash = true;
                    newPrefix = newPrefix.substring(1);
                }

                addLayoutXmlTags(result, newPrefix, slash);
            }
        } else {
            final IsInValueScanner scanner = new IsInValueScanner(index);
            final String name = scanner.scan(contents);
            if (name != null) {
                final String attrName =
                        name.contains(":") ? name.substring(name.indexOf(":") + 1) : name;
                if (attrs.getAttributes().containsKey(attrName)) {
                    Attr attr = attrs.getAttributes().get(attrName);
                    if (attr != null && attr.hasPossibleValues()) {
                        Set<String> values = attr.possibleValues;
                        for (String value : values) {
                            if (value.toLowerCase(Locale.US).startsWith(prefix)) {
                                result.getItems().add(valueAsCompletion(value));
                            }
                        }
                    }
                }
            } else {
                for (Map.Entry<String, Attr> entry : attrs.getAttributes().entrySet()) {
                    Attr attr = entry.getValue();
                    if (attr.name.toLowerCase(Locale.US).startsWith(prefix)) {
                        result.getItems().add(attrAsCompletion(attr));
                    }
                }

                // Shortcuts for automatically declaring namespaces
                // These completions are proposed if you type 'androidNs', 'appNs' or 'toolsNs'
                // Idea is shamelessly copied from Android Studio 😂
                if ("android".startsWith(prefix)) {
                    result.getItems().add(createNamespaceCompletion("android", ANDROID_NS));
                }

                if ("app".startsWith(prefix)) {
                    result.getItems().add(createNamespaceCompletion("app", APP_NS));
                }

                if ("tools".startsWith(prefix)) {
                    result.getItems().add(createNamespaceCompletion("tools", TOOLS_NS));
                }
            }
        }

        return result;
    }

    private void addLayoutXmlTags(CompletionResult result, String prefix, boolean slash) {
        prefix = prefix.toLowerCase(Locale.ROOT);
        for (var widget : this.sdkInfo.getWidgetInfo().getWidgets()) {
            var name = widget.simpleName.toLowerCase(Locale.ROOT);
            if (name.startsWith(prefix)) {
                result.getItems().add(widgetNameAsCompletion(widget, slash));
            }
        }
    }

    @NonNull
    private CompletionItem valueAsCompletion(String value) {
        CompletionItem item = new CompletionItem();
        item.setLabel(value);
        item.setDetail("Attribute value");
        item.setInsertText(value);
        item.setInsertTextFormat(InsertTextFormat.PLAIN_TEXT);
        item.setSortText("0" + value);
        item.setKind(CompletionItemKind.VALUE);
        return item;
    }

    @NonNull
    private CompletionItem attrAsCompletion(@NonNull Attr attr) {
        CompletionItem item = new CompletionItem();
        item.setLabel(attr.name);
        item.setDetail("Attribute");
        item.setInsertText(createAttributeInsertText(attr));
        item.setInsertTextFormat(InsertTextFormat.PLAIN_TEXT);
        item.setSortText("1" + attr.name);
        item.setKind(CompletionItemKind.SNIPPET);
        return item;
    }

    @NonNull
    private String createAttributeInsertText(@NonNull Attr attr) {
        StringBuilder xml = new StringBuilder();
        xml.append(attr.namespace.getName());
        xml.append(":");
        xml.append(attr.name);
        xml.append("=");
        xml.append("\"");

        if (attr.namespace.getName().equals("android") && attr.name.equals("id")) {
            xml.append("@+id/");
        }

        xml.append("$0");
        xml.append("\"");

        return xml.toString();
    }

    @NonNull
    private CompletionItem widgetNameAsCompletion(@NonNull Widget view, boolean slash) {
        CompletionItem item = new CompletionItem();
        item.setLabel(view.simpleName);
        item.setDetail(view.name);
        item.setInsertText(createTagInsertText(view, slash));
        item.setInsertTextFormat(InsertTextFormat.PLAIN_TEXT);
        item.setSortText("2" + view.simpleName);
        item.setKind(CompletionItemKind.CLASS);

        // Required to show API information in completion list
        final var data = new CompletionData();
        data.setClassName(view.name);

        item.setData(data);

        return item;
    }

    @NonNull
    private String createTagInsertText(@NonNull Widget view, boolean closing) {
        StringBuilder sb = new StringBuilder();

        // Don't append leading '<' and trailing '>'
        // They may have been already inserted by editor upon typing '<'

        sb.append(view.simpleName);

        if (!closing) {
            sb.append("\n");
            sb.append(INITIAL_TAG_ATTRIBUTES);
            sb.append("\n$0/");
        } else {
            sb.append(">$0");
        }

        return sb.toString();
    }

    @NonNull
    @Contract(pure = true)
    private static CompletionItem createNamespaceCompletion(String name, String value) {
        final var item = new CompletionItem();
        item.setLabel(name + "Ns");
        item.setDetail(
                BaseApplication.getBaseInstance().getString(R.string.msg_add_namespace_decl, name));
        item.setInsertText(String.format("xmlns:%1$s=\"%2$s\"", name, value));
        item.setKind(CompletionItemKind.SNIPPET);
        item.setSortText(
                "1000" + item.getLabel()); // This item is expected to be at the last of the
        // completion list
        return item;
    }

    private static class IsInValueScanner {

        private final int cursorIndex;

        public IsInValueScanner(int cursorIndex) {
            this.cursorIndex = cursorIndex;
        }

        private boolean containsCursor(@NonNull Token token) {
            int start = token.getStartIndex();
            int end = token.getStopIndex();
            return start <= cursorIndex && cursorIndex <= end;
        }

        @Nullable
        public String scan(CharSequence xmlContent) {
            try {
                XMLLexer lexer =
                        new XMLLexer(CharStreams.fromReader(new CharSequenceReader(xmlContent)));
                Token token;
                String attrName = null;
                while ((token = lexer.nextToken()) != null && token.getType() != XMLLexer.EOF) {
                    if (token.getType() == XMLLexer.STRING && containsCursor(token)) {
                        String text = token.getText();
                        if (text.startsWith("\"")) return attrName;
                    } else if (token.getType() == XMLLexer.Name) {
                        attrName = token.getText();
                    }
                }
            } catch (Throwable e) {
                LOG.error("An error occurred on checking if cursor is in string", e);
            }
            return null;
        }
    }
}
