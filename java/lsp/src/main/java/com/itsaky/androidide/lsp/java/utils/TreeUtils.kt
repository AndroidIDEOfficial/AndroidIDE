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

package com.itsaky.androidide.lsp.java.utils

import openjdk.source.tree.MethodTree
import openjdk.source.tree.Tree
import openjdk.source.tree.Tree.Kind.ANNOTATION_TYPE
import openjdk.source.tree.Tree.Kind.CLASS
import openjdk.source.tree.Tree.Kind.ENUM
import openjdk.source.tree.Tree.Kind.INTERFACE
import openjdk.source.tree.Tree.Kind.METHOD

/**
 * Utility methods for Javac Trees.
 *
 * @author Akash Yadav
 */
class TreeUtils {

  companion object {
    @JvmStatic
    fun isType(tree: Tree?): Boolean {
      return isType(tree?.kind)
    }

    @JvmStatic
    fun isType(kind: Tree.Kind?): Boolean {
      kind ?: return false

      return when (kind) {
        CLASS,
        INTERFACE,
        ANNOTATION_TYPE,
        ENUM -> true
        else -> false
      }
    }

    @JvmStatic
    fun isMethod(tree: Tree?): Boolean {
      return isMethod(tree?.kind)
    }

    @JvmStatic
    fun isMethod(kind: Tree.Kind?): Boolean {
      return kind == METHOD
    }

    @JvmStatic
    fun isConstructor(tree: Tree?): Boolean {
      tree ?: return false
      return tree.kind == METHOD && (tree as MethodTree).name.contentEquals("<init>")
    }

    @JvmStatic
    fun isMethodOrConstructor(tree: Tree?): Boolean {
      return isMethod(tree) || isConstructor(tree)
    }
  }
}
