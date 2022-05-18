package com.itsaky.lsp.xml.providers;

import com.android.ide.common.xml.XmlFormatPreferences;
import com.android.ide.common.xml.XmlFormatStyle;
import com.android.ide.common.xml.XmlPrettyPrinter;
import com.android.utils.XmlUtils;

import com.itsaky.androidide.utils.ILogger;

import org.w3c.dom.Document;

public class CodeFormatProvider {

  private static final ILogger LOG = ILogger.newInstance("XmlCodeFormatProvider");

  public CodeFormatProvider() {}

  public CharSequence format(CharSequence input) {
    final long start = System.currentTimeMillis();
    try {
      Document document = XmlUtils.parseDocument(input.toString(), true);

      XmlFormatStyle style = XmlFormatStyle.get(document);
      String prettyPrinted =
          XmlPrettyPrinter.prettyPrint(
              document, XmlFormatPreferences.defaults(), style, null, false);

      LOG.info("Xml code formatted in", System.currentTimeMillis() - start + "ms");
      return prettyPrinted;

    } catch (Throwable e) {
      LOG.error("Failed to format code.", e);
      return input;
    }
  }
}
