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

package com.itsaky.lsp.models

import java.nio.file.Path

/**
 * @author Akash Yadav
 */
data class SemanticHighlightParams(var file: Path)
class SemanticHighlight (var file: Path) {
    val packages: List<Range> = ArrayList()
    val enumTypes: List<Range> = ArrayList()
    val classNames: List<Range> = ArrayList()
    val annotationTypes: List<Range> = ArrayList()
    val interfaces: List<Range> = ArrayList()
    val enums: List<Range> = ArrayList()
    val statics: List<Range> = ArrayList()
    val fields: List<Range> = ArrayList()
    val parameters: List<Range> = ArrayList()
    val locals: List<Range> = ArrayList()
    val exceptionParams: List<Range> = ArrayList()
    val methodDeclarations: List<Range> = ArrayList()
    val methodInvocations: List<Range> = ArrayList()
    val constructors: List<Range> = ArrayList()
    val staticInits: List<Range> = ArrayList()
    val instanceInits: List<Range> = ArrayList()
    val typeParams: List<Range> = ArrayList()
    val resourceVariables: List<Range> = ArrayList()
}