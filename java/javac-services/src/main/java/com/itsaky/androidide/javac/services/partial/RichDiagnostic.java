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

package com.itsaky.androidide.javac.services.partial;

import java.util.Locale;
import jdkx.tools.Diagnostic;
import openjdk.tools.javac.api.DiagnosticFormatter;
import openjdk.tools.javac.util.JCDiagnostic;

/**
 * @author Akash Yadav
 */
public class RichDiagnostic implements Diagnostic {

  private final JCDiagnostic delegate;
  private final DiagnosticFormatter<JCDiagnostic> formatter;

  public RichDiagnostic(JCDiagnostic delegate, DiagnosticFormatter<JCDiagnostic> formatter) {
    this.delegate = delegate;
    this.formatter = formatter;
  }

  @Override
  public Diagnostic.Kind getKind() {
    return delegate.getKind();
  }

  @Override
  public Object getSource() {
    return delegate.getSource();
  }

  @Override
  public long getPosition() {
    return delegate.getPosition();
  }

  @Override
  public long getStartPosition() {
    return delegate.getStartPosition();
  }

  @Override
  public long getEndPosition() {
    return delegate.getEndPosition();
  }

  @Override
  public long getLineNumber() {
    return delegate.getLineNumber();
  }

  @Override
  public long getColumnNumber() {
    return delegate.getColumnNumber();
  }

  @Override
  public String getCode() {
    return delegate.getCode();
  }

  @Override
  public String getMessage(Locale locale) {
    return formatter.format(delegate, locale);
  }

  @Override
  public String toString() {
    return delegate.toString();
  }

  JCDiagnostic getDelegate() {
    return delegate;
  }

  public static Diagnostic wrap(Diagnostic d, DiagnosticFormatter<JCDiagnostic> df) {
    if (d instanceof JCDiagnostic) {
      return new RichDiagnostic((JCDiagnostic) d, df);
    } else {
      return d;
    }
  }
}
