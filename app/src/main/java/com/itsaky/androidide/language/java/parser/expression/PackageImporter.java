package com.itsaky.androidide.language.java.parser.expression;

import android.text.Editable;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.itsaky.androidide.language.java.parser.internal.JavaUtil;
import com.itsaky.androidide.language.java.parser.util.EditorUtil;
import io.github.rosemoe.editor.text.Content;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import io.github.rosemoe.editor.text.CharPosition;
import android.text.TextUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by Duy on 21-Jul-17.
 */

public class PackageImporter {
    private static final String TAG = "ImportUtil";

    /**
     * Add import statement if import does not already exist.
     */
    public static void importClass(Content editor, String className) {
        String packageName = JavaUtil.getPackageName(className);
        if (getImportedClassName(editor, className) == null
			&& !packageName.isEmpty()
			&& !packageName.equals("java.lang")
			&& !packageName.equals(EditorUtil.getCurrentPackage(editor.toString()))) {
            organizeImports(editor, "import " + className.replace("$", ".") + ";");
        }
    }

    public static String getImportedClassName(EditText editor, @Nullable String className) {
        return getImportedClassName(editor, className);
    }

    public static String getImportedClassName(CharSequence src, @Nullable String className) {
        if (className == null) return null;

        Pattern pattern = PatternFactory.makeImportClass(className);
        Matcher matcher = pattern.matcher(src);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return PatternFactory.match(src, pattern);
    }


    public static void organizeImports(Content editor, String importStr) {
        try {
			ArrayList<String> imprts = getImports(editor);
			imprts.add(importStr);
			Collections.sort(imprts);
			final Set<String> imports = new HashSet<String>();
			for(String imprt : imprts) {
				imports.add(imprt.trim());
			}
			String imprt = TextUtils.join("\n", imports).trim();

			int s = PatternFactory.firstMatch(editor, PatternFactory.IMPORT);
			int e = PatternFactory.lastMatch(editor, PatternFactory.IMPORT);

			if (s >= 0 && e >= 0 && e < editor.length()) {
				CharPosition start = editor.getIndexer().getCharPosition(s);
				CharPosition end = editor.getIndexer().getCharPosition(e);
				if (s < e) {
					editor.replace(start.line, start.column, end.line, end.column, imprt);
				} else if (s == e) {
					editor.insert(start.line, start.column, imprt);
				}
			} else {
				int p = PatternFactory.lastMatch(editor, PatternFactory.PACKAGE);
				int c = PatternFactory.firstMatch(editor, CompleteTypeDeclared.CLASS_DECLARE);

				if(p >= 0 && c >= 0 && p < c) {
					CharPosition start = editor.getIndexer().getCharPosition(p);
					CharPosition end = editor.getIndexer().getCharPosition(c);
					imprt = "\n\n" + imprt + "\n";
					if(p < c) {
						editor.replace(start.line, start.column, end.line, end.column, imprt);
					} else if(p == c) {
						editor.insert(start.line, start.column, imprt);
					} else {
						editor.insert(0, 0, imprt);
					}
				}
			}
		} catch (Throwable th) {}
    }

    public static ArrayList<String> getImports(Content editor) {
        return PatternFactory.allMatch(editor, PatternFactory.IMPORT);
    }

}
