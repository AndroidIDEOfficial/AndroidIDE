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

package com.itsaky.lsp.java.utils

import com.squareup.javapoet.ImportCollectingCodeWriter
import com.squareup.javapoet.MethodSpec

/**
 * @author Akash Yadav
 */
class JavaPoetUtils {
    companion object {
        @JvmStatic
        fun print(method: MethodSpec, importsOut: MutableSet<String>, qualifiedNames: Boolean = true): String {
            val sb = StringBuilder()
            val writer = ImportCollectingCodeWriter(sb)
            
            writer.setPrintQualifiedNames(qualifiedNames)
            writer.emit(method)
            importsOut.addAll(writer.importClasses)
            
            return sb.toString()
        }
        
        @JvmStatic
        fun print(build: MethodSpec, imports: MutableSet<String>): String {
            return print(build, imports, true)
        }
    }
}