package com.itsaky.androidide.language.java.parser.expression;

import androidx.annotation.NonNull;
import com.itsaky.androidide.language.java.parser.Expression;
import com.itsaky.androidide.language.java.parser.internal.PackageManager;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.itsaky.androidide.language.java.parser.model.PackageDescription;
import com.sun.tools.javac.tree.JCTree;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

/**
 * Complete package
 * e.g.
 * <p>
 * java.i|
 * java.util.|
 * java.util.Hash|
 */
public class CompletePackage extends JavaCompleteMatcherImpl {
    private static final String TAG = "CompletePackage";

    private static final Pattern PACKAGE
            = Pattern.compile("^\\s*(package)\\s+([_A-Za-z0-9.]+)");
    private static final Pattern IMPORT_OR_IMPORT_STATIC
            = Pattern.compile("^\\s*import(\\s+static)?\\s+([_A-Za-z0-9.]+)");

    @NonNull
    private final PackageManager mPackageManager;

    public CompletePackage(@NonNull PackageManager packageManager) {
        mPackageManager = packageManager;
    }

    @Override
    public boolean process(JCTree.JCCompilationUnit ast, CodeEditor editor, Expression expression, String statement, ArrayList<SuggestItem> result) {
        Matcher matcher = PACKAGE.matcher(statement);
        if (matcher.find()) {
            String incompletePkg = matcher.group(2);
            getSuggestion(editor, incompletePkg, result);
            return true;
        }

        matcher = IMPORT_OR_IMPORT_STATIC.matcher(statement);
        if (matcher.find()) {
            boolean isStatic = matcher.group(1) != null;
            String incompletePkg = matcher.group(2);
            getSuggestion(editor, incompletePkg, result);
        }

        return false;
    }

    @Override
    public void getSuggestion(CodeEditor editor, String expr, List<SuggestItem> suggestItems) {
        getSuggestionInternal(editor, expr, suggestItems, false);
    }

    private void getSuggestionInternal(CodeEditor editor, String expr, List<SuggestItem> suggestItems,
                                       boolean addSemicolon) {

        //package must be contains dot (.)
        if (!expr.contains(".")) {
            return;
        }

        //completed package java.lang
        int lastDotIndex = expr.lastIndexOf(".");
        String completedPart = expr.substring(0, lastDotIndex /*not contains dot*/);
        String incompletePart = expr.substring(lastDotIndex + 1);

        PackageDescription packages = mPackageManager.trace(completedPart);
        if (packages != null) {
            //members of current package
            //such as java has more member (util, io, lang)
            HashMap<String, PackageDescription> members = packages.getChild();
            for (Map.Entry<String, PackageDescription> entry : members.entrySet()) {
                PackageDescription packageDescription = entry.getValue();
                if (packageDescription.getName().toLowerCase(Locale.US).startsWith(incompletePart.toLowerCase(Locale.US))) {
                    setInfo(packageDescription, editor, incompletePart);
                    suggestItems.add(packageDescription);
                }

            }
        }
    }
}
