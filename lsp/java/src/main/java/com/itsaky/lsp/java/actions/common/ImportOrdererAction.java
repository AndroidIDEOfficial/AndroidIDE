package com.itsaky.lsp.java.actions.common;

import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.ImportOrderer;
import com.google.googlejavaformat.java.JavaFormatterOptions;

import com.itsaky.androidide.actions.ActionData;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.utils.ILogger;

import com.itsaky.lsp.java.R;
import com.itsaky.lsp.java.actions.BaseCodeAction;

import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.widget.CodeEditor;

public class ImportOrdererAction extends BaseCodeAction {

  private final ILogger log = ILogger.newInstance(getClass().getSimpleName());

  @Override
  public String getId() {
    return "lsp_java_import_ordrerer_action";
  }

  @Override
  public String getLabel() {
    return BaseApplication.getBaseInstance().getApplicationContext().getString(getTitleTextRes());
  }

  @Override
  protected int getTitleTextRes() {
    return R.string.action_import_ordrerer;
  }

  @Override
  public void prepare(ActionData data) {
    super.prepare(data);
    if (getVisible()) {
      if (!hasRequiredData(data)) {
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

        String output =
            ImportOrderer.reorderImports(content.toString(), JavaFormatterOptions.Style.GOOGLE);
        log.info("Reorder imports in", System.currentTimeMillis() - start + "ms");
        return output;
      }

    } catch (FormatterException e) {
      log.error("Failed to reorder imports", e);
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
    String text = (String) result;
    if (text != null) {
      CodeEditor editor = requireEditor(data);
      editor.setText(text);
    }
  }
}


