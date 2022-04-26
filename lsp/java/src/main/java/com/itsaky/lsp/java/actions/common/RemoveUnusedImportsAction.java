package com.itsaky.lsp.java.actions.common;

import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.RemoveUnusedImports;
import com.itsaky.androidide.actions.ActionData;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.lsp.java.actions.BaseCodeAction;
import com.itsaky.lsp.java.R;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.widget.CodeEditor;

public class RemoveUnusedImportsAction extends BaseCodeAction {

  @Override
  public String getId() {
    return "lsp_java_remove_unused_imports";
  }

  @Override
  public String getLabel() {
    return BaseApplication.getBaseInstance().getApplicationContext().getString(getTitleTextRes());
  }

  @Override
  protected int getTitleTextRes() {
    return R.string.action_remove_unused_imports;
  }

  @Override
  public Boolean execAction(ActionData data) {
    try {
      CodeEditor editor = requireEditor(data);
      Content content = editor.getText();
      if (content != null) {
        editor.setText(RemoveUnusedImports.removeUnusedImports(content.toString()));
      }
      return true;
    } catch (FormatterException fe) {
      fe.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean getRequiresUIThread() {
    return true;
  }

  @Override
  public void setLabel(String arg0) {}
}

