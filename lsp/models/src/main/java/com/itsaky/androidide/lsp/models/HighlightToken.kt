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

package com.itsaky.androidide.lsp.models

import com.itsaky.androidide.models.Range

/**
 * Model class for a syntax highlighting token.
 * @author Akash Yadav
 */
data class HighlightToken(var range: Range, var kind: HighlightTokenKind)

enum class HighlightTokenKind {

  // Common
  TEXT_NORMAL,

  // Java and Gradle
  KEYWORD,
  COMMENT,
  OPERATOR,
  LITERAL,
  LOCAL_VARIABLE,
  RESOURCE_VARIABLE,
  PARAMETER,
  EXCEPTION_PARAMETER,
  TYPE_PARAMETER,
  FIELD,
  STATIC_FIELD,
  ENUM,
  TYPE_NAME,
  ENUM_TYPE,
  INTERFACE,
  METHOD_DECLARATION,
  METHOD_INVOCATION,
  CONSTRUCTOR,
  ANNOTATION,
  STATIC_INIT,
  INSTANCE_INIT,
  PACKAGE_NAME,

  // XML
  TAG_NAME,
  NAMESPACE,
  ATTRIBUTE_NAME,
  ATTRIBUTE_VALUE
}
