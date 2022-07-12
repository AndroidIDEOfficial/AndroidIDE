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

package com.itsaky.androidide.lsp.java.visitors

import com.itsaky.androidide.utils.ILogger
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.JCTree.JCErroneous
import com.sun.tools.javac.tree.TreeScanner

/**
 * Prints all the trees it visits. For debugging/testing purposes only.
 *
 * @author Akash Yadav
 */
class PrintingVisitor : TreeScanner() {

  private val log = ILogger.newInstance(javaClass.simpleName)

  override fun scan(tree: JCTree?) {
    log.debug(if (tree != null) tree::class.java else "NullClass", tree)
    super.scan(tree)
  }

  override fun visitErroneous(tree: JCErroneous?) {
    if (tree?.errs != null) {
      tree.errs.forEach { scan(it) }
    }
    super.visitErroneous(tree)
  }
}
