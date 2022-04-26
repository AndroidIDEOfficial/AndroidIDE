package com.itsaky.lsp.java.actions.common;

import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.ImportOrderer;
import com.google.googlejavaformat.java.JavaFormatterOptions;
import com.itsaky.androidide.actions.ActionData;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.lsp.java.actions.BaseCodeAction;
import com.itsaky.lsp.java.R;
import io.github.rosemoe.sora.widget.CodeEditor;

public class ImportOrdererAction extends BaseCodeAction {

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
  public Boolean execAction(ActionData data) {
    try {
      CodeEditor editor = requireEditor(data);
      String output = ImportOrderer.reorderImports(editor.getText().toString(), JavaFormatterOptions.Style.GOOGLE);
      editor.setText(output);
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

