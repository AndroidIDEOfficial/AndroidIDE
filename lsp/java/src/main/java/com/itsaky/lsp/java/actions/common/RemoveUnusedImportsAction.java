package com.itsaky.lsp.java.actions.common;

import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.RemoveUnusedImports;

import com.itsaky.androidide.actions.ActionData;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.java.actions.BaseCodeAction;
import com.itsaky.lsp.java.R;

import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.widget.CodeEditor;

public class RemoveUnusedImportsAction extends BaseCodeAction {

    private final ILogger log = ILogger.newInstance(getClass().getSimpleName());

    @Override
    public String getId() {
        return "lsp_java_remove_unused_imports";
    }

    @Override
    public String getLabel() {
        return BaseApplication.getBaseInstance()
                .getApplicationContext()
                .getString(getTitleTextRes());
    }

    @Override
    protected int getTitleTextRes() {
        return R.string.action_remove_unused_imports;
    }

    @Override
    public void prepare(ActionData data) {
        super.prepare(data);
        if (getVisible()) {
            if (!hasRequiredData(data, CodeEditor.class)) {
                markInvisible();
                return;
            }

            setVisible(true);
            setEnabled(true);
        }
    }

    @Override
    public String execAction(ActionData data) {
        final long start = System.currentTimeMillis();
        try {
            CodeEditor editor = requireEditor(data);
            Content content = editor.getText();
            if (content != null) {
                String output = RemoveUnusedImports.removeUnusedImports(content.toString());
                log.info("Removed unused imports in", System.currentTimeMillis() - start + "ms");
                return output;
            }
        } catch (FormatterException e) {
            log.error("Failed to remove unused imports", e);
            return null;
        }
        return null;
    }

    @Override
    public boolean getRequiresUIThread() {
        return false;
    }

    @Override
    public void setLabel(String arg0) {}

    @Override
    public void postExec(ActionData data, Object result) {
        super.postExec(data, result);
        if (result instanceof String) {
            String text = (String) result;
            if (!text.isEmpty()) {
                CodeEditor editor = requireEditor(data);
                editor.setText(text);
            }
        }
    }
}

