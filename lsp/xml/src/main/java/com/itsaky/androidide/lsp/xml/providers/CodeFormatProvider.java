package com.itsaky.androidide.lsp.xml.providers;

import com.android.ide.common.xml.XmlFormatPreferences;
import com.android.ide.common.xml.XmlFormatStyle;
import com.android.ide.common.xml.XmlPrettyPrinter;
import com.android.utils.XmlUtils;
import com.itsaky.androidide.lsp.models.CodeFormatResult;
import com.itsaky.androidide.lsp.models.FormatCodeParams;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.StopWatch;

import org.w3c.dom.Document;

public class CodeFormatProvider {

  private static final ILogger LOG = ILogger.newInstance("XmlCodeFormatProvider");

  public CodeFormatResult format(FormatCodeParams params) {
    final CharSequence input = params.getContent();
    final var watch = new StopWatch("Formatting XML code");
    try {
      Document document = XmlUtils.parseDocument(input.toString(), true);

      XmlFormatStyle style = XmlFormatStyle.get(document);
      String prettyPrinted =
          XmlPrettyPrinter.prettyPrint(
              document, XmlFormatPreferences.defaults(), style, null, false);

      watch.log();
      return CodeFormatResult.forWholeContent(input, prettyPrinted);

    } catch (Throwable e) {
      LOG.error("Failed to format code.", e);
      return CodeFormatResult.NONE;
    }
  }
}
