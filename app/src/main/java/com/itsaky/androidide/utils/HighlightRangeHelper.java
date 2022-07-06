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

package com.itsaky.androidide.utils;

import static com.itsaky.androidide.lsp.models.HighlightTokenKind.ANNOTATION;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.CONSTRUCTOR;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.ENUM;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.ENUM_TYPE;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.EXCEPTION_PARAMETER;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.FIELD;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.INSTANCE_INIT;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.INTERFACE;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.LOCAL_VARIABLE;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.METHOD_DECLARATION;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.METHOD_INVOCATION;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.PACKAGE_NAME;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.PARAMETER;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.RESOURCE_VARIABLE;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.STATIC_FIELD;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.STATIC_INIT;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.TYPE_NAME;
import static com.itsaky.androidide.lsp.models.HighlightTokenKind.TYPE_PARAMETER;

import com.itsaky.androidide.lsp.models.HighlightToken;
import com.itsaky.androidide.lsp.models.HighlightTokenKind;
import com.itsaky.androidide.models.Position;

import java.util.List;

public class HighlightRangeHelper {

  private static final ILogger LOG = ILogger.newInstance("HighlightRangeHelper");
  private final List<HighlightToken> highlights;

  public HighlightRangeHelper(List<HighlightToken> highlights) {
    this.highlights = highlights;
  }

  public boolean isEnumType(int line, int column) {
    return isOfKind(line, column, ENUM_TYPE);
  }

  /**
   * Check if the token at the given index has the given token kind.
   *
   * @param line The line to check.
   * @param column The column to check.
   * @param kind The kind to check.
   * @return {@code true} if the token at the given position has the given kind, {@code false}
   *     otherwise.
   */
  public boolean isOfKind(int line, int column, HighlightTokenKind kind) {
    final var position = new Position(line, column);
    int left = 0;
    int right = this.highlights.size();
    int mid;
    while (left < right) {
      mid = (left + right) / 2;
      var token = this.highlights.get(mid);
      var range = token.getRange();
      var pos = range.getStart().compareTo(position);
      if (pos < 0) {
        left = mid + 1;
      } else if (pos > 0) {
        right = mid - 1;
      } else {
        return token.getKind() == kind;
      }
    }
    return false;
  }

  public boolean isAnnotationType(int line, int column) {
    return isOfKind(line, column, ANNOTATION);
  }

  public boolean isInterface(int line, int column) {
    return isOfKind(line, column, INTERFACE);
  }

  public boolean isEnum(int line, int column) {
    return isOfKind(line, column, ENUM);
  }

  public boolean isParameter(int line, int column) {
    return isOfKind(line, column, PARAMETER);
  }

  public boolean isExceptionParam(int line, int column) {
    return isOfKind(line, column, EXCEPTION_PARAMETER);
  }

  public boolean isConstructor(int line, int column) {
    return isOfKind(line, column, CONSTRUCTOR);
  }

  public boolean isStaticInit(int line, int column) {
    return isOfKind(line, column, STATIC_INIT);
  }

  public boolean isInstanceInit(int line, int column) {
    return isOfKind(line, column, INSTANCE_INIT);
  }

  public boolean isTypeParam(int line, int column) {
    return isOfKind(line, column, TYPE_PARAMETER);
  }

  public boolean isResourceVariable(int line, int column) {
    return isOfKind(line, column, RESOURCE_VARIABLE);
  }

  public boolean isPackageName(int line, int column) {
    return isOfKind(line, column, PACKAGE_NAME);
  }

  public boolean isClassName(int line, int column) {
    return isOfKind(line, column, TYPE_NAME);
  }

  public boolean isField(int line, int column) {
    return isOfKind(line, column, FIELD);
  }

  public boolean isStaticField(int line, int column) {
    return isOfKind(line, column, STATIC_FIELD);
  }

  public boolean isMethodDeclaration(int line, int column) {
    return isOfKind(line, column, METHOD_DECLARATION);
  }

  public boolean isMethodInvocation(int line, int column) {
    return isOfKind(line, column, METHOD_INVOCATION);
  }

  public boolean isLocal(int line, int column) {
    return isOfKind(line, column, LOCAL_VARIABLE);
  }
}
