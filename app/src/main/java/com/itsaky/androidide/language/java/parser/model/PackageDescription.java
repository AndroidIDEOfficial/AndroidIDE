package com.itsaky.androidide.language.java.parser.model;

import androidx.annotation.NonNull;
import android.text.Editable;
import java.util.HashMap;
import io.github.rosemoe.editor.widget.CodeEditor;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.CharPosition;

public class PackageDescription extends JavaSuggestItemImpl {
    private String mName;
    private PackageDescription mParentPkg;
    private HashMap<String, PackageDescription> mChild = new HashMap<>();

    private PackageDescription(String childName, PackageDescription parent) {
        this.mName = childName;
        this.mParentPkg = parent;
    }

    public static PackageDescription root() {
        return new PackageDescription("", null);
    }

    @Override
    public void onSelectThis(@NonNull CodeEditor editorView) {
        try {
            final int cursor = getEditor().getCursor().getLeft();
            final int length = getIncomplete().length();
            final int start = cursor - length;
            Content editable = editorView.getText();
			CharPosition s = editable.getIndexer().getCharPosition(start);
			CharPosition c = editable.getIndexer().getCharPosition(cursor);
			String text;
            text = isLeaf() ? mName.replace("$", ".") : mName + ".";
			editable.replace(s.line, s.column, c.line, c.column, text);
			CharPosition select = editable.getIndexer().getCharPosition(start + text.length());
			editorView.setSelection(select.line, select.column);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public char getTypeHeader() {
        return 'p';
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getReturnType() {
        return null;
    }


    public HashMap<String, PackageDescription> getChild() {
        return mChild;
    }

    private boolean isRoot() {
        return mName.isEmpty() || mParentPkg == null;
    }

    private boolean isLeaf() {
        return mChild.isEmpty();
    }

    public PackageDescription get(String key) {
        if (!key.contains(".")) {
            return mChild.get(key);
        } else {
            return get(key.substring(0, key.indexOf(".")))
                    .get(key.substring(key.indexOf(".") + 1));
        }
    }

    public void put(String pkg) {
        if (pkg.contains(".")) {
            String first = pkg.substring(0, pkg.indexOf("."));
            if (get(first) == null) {
                put(first);
            }
            get(first).put(pkg.substring(pkg.indexOf(".") + 1));
        } else {
            this.mChild.put(pkg, new PackageDescription(pkg, this));
        }
    }

    public PackageDescription remove(String child) {
        if (child.contains(".")) {
            return this.mChild.remove(child.substring(0, child.indexOf(".")));
        } else {
            return this.mChild.remove(child);
        }
    }

    @Override
    public String toString() {
        return mName;
    }
}
