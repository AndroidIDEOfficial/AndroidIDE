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

import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.models.SignatureHelp;
import com.itsaky.lsp.models.SignatureInformation;

import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.widget.base.EditorPopupWindow;

/**
 * An {@link EditorPopupWindow} used to show signature help.
 *
 * @author Akash Yadav
 */
public class SignatureHelpWindow extends BaseEditorWindow {

  private static final ILogger LOG = ILogger.newInstance("SignatureHelpWindow");

  /**
   * Create a signature help popup window for editor
   *
   * @param editor The editor.
   */
  public SignatureHelpWindow(@NonNull IDEEditor editor) {
    super(editor);

    editor.subscribeEvent(
        SelectionChangeEvent.class,
        (event, unsubscribe) -> {
          if (isShowing()) {
            dismiss();
          }
        });
  }

  public void setupAndDisplay(SignatureHelp signature) {
    if (signature == null || signature.getSignatures().isEmpty()) {
      if (isShowing()) {
        dismiss();
      }

      return;
    }

    final var signatureText = createSignatureText(signature);

    if (signatureText == null) {
      return;
    }

    this.text.setText(signatureText);
    displayWindow();
  }

  @Nullable
  private CharSequence createSignatureText(@NonNull SignatureHelp signature) {
    final var signatures = signature.getSignatures();
    final var activeSignature = signature.getActiveSignature();
    final var activeParameter = signature.getActiveParameter();
    final SpannableStringBuilder sb = new SpannableStringBuilder();

    if (activeSignature < 0 || activeParameter < 0) {
      LOG.debug("activeSignature:", activeSignature, "activeParameter:", activeParameter);
      return null;
    }

    var count = signatures.size();
    if (activeSignature >= count) {
      LOG.debug("Active signature is invalid", "Size is " + count);
      return null;
    }

    // remove all with non-applicable signatures
    signatures.removeIf(
        info -> {
          final var remove = activeParameter >= info.getParameters().size();
          if (remove) {
            LOG.debug(
                "Removing",
                info,
                "params=" + info.getParameters().size(),
                "active=" + activeParameter);
          }
          return remove;
        });

    count = signatures.size();
    for (var i = 0; i < count; i++) {
      final var info = signatures.get(i);
      formatSignature(info, activeParameter, i == activeSignature, sb);
      if (i != count - 1) {
        sb.append('\n');
      }
    }

    return sb;
  }

  /**
   * Formats (highlights) a method signature
   *
   * @param signature Signature information
   * @param paramIndex Currently active parameter index
   * @param isCurrentSignature <code>true</code> if the given signature is the active signature.
   * @param result The builder to append spanned text to.
   */
  private void formatSignature(
      @NonNull SignatureInformation signature,
      int paramIndex,
      boolean isCurrentSignature,
      SpannableStringBuilder result) {

    final var currentIndex = Math.max(0, result.length() - 1);
    String name = signature.getLabel();
    name = name.substring(0, name.indexOf("("));

    result.append(
        name, new ForegroundColorSpan(0xffffffff), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    result.append(
        "(", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

    var params = signature.getParameters();
    for (int i = 0; i < params.size(); i++) {
      int color = i == paramIndex ? 0xffff6060 : 0xffffffff;
      final var info = params.get(i);
      if (i == params.size() - 1) {
        result.append(
            info.getLabel() + "",
            new ForegroundColorSpan(color),
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
      } else {
        result.append(
            info.getLabel() + "",
            new ForegroundColorSpan(color),
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        result.append(
            ",",
            new ForegroundColorSpan(0xff4fc3f7),
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        result.append(" ");
      }
    }
    result.append(
        ")", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

    if (isCurrentSignature) {
      // set background span to little light color than the text's background
      result.setSpan(
          new BackgroundColorSpan(0xff373737),
          currentIndex,
          result.length(),
          SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
  }
}
