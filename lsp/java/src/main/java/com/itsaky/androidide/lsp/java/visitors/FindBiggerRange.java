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

package com.itsaky.androidide.lsp.java.visitors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;

/**
 * @author Akash Yadav
 */
public class FindBiggerRange extends TreeScanner<Range, Range> {

  private final SourcePositions positions;
  private final CompilationUnitTree root;
  private final LineMap lineMap;

  public FindBiggerRange(JavacTask task, @NonNull CompilationUnitTree root) {
    this.positions = Trees.instance(task).getSourcePositions();
    this.root = root;
    this.lineMap = root.getLineMap();
  }

  @Override
  public Range scan(Tree tree, Range range) {
    final Range smallerThanThis = super.scan(tree, range);
    if (smallerThanThis != null) {
      return smallerThanThis;
    }

    final Range treeRange = getRange(tree);
    if (treeRange != null && range.isSmallerThan(treeRange)) {
      ILogger.newInstance("FindBiggerRange")
          .debug("Selecting tree", tree, tree.getClass(), tree.getKind());
      return treeRange;
    }

    return null;
  }

  @Override
  public Range reduce(Range r1, Range r2) {
    return r1 == null ? r2 : r1;
  }

  @Override
  public Range visitMethod(MethodTree node, Range range) {

    // If this methods body is selected, then select the entire method
    final Range methodRange = getRange(node);
    final Range blockRange = getRange(node.getBody());
    if (range.equals(blockRange) && methodRange != null) {
      return methodRange;
    }

    return super.visitMethod(node, range);
  }

  @Override
  public Range visitTry(TryTree node, Range range) {

    // If this try's body or finally block is selected, then select the entire try
    final Range methodRange = getRange(node);
    final Range blockRange = getRange(node.getBlock());
    final Range finallyRange = getRange(node.getFinallyBlock());
    if ((range.equals(blockRange) || range.equals(finallyRange)) && methodRange != null) {
      return methodRange;
    }

    return super.visitTry(node, range);
  }

  @Nullable
  private Range getRange(Tree leaf) {
    final Range range = new Range();
    final Position start = new Position(0, 0);
    final Position end = new Position(0, 0);

    final long startPos = positions.getStartPosition(root, leaf);
    final long endPos = positions.getEndPosition(root, leaf);

    if (startPos == -1 || endPos == -1) {
      return null;
    }

    start.setLine((int) lineMap.getLineNumber(startPos) - 1);
    start.setColumn((int) lineMap.getColumnNumber(startPos) - 1);

    end.setLine((int) lineMap.getLineNumber(endPos) - 1);
    end.setColumn((int) lineMap.getColumnNumber(endPos) - 1);

    range.setStart(start);
    range.setEnd(end);

    return range;
  }
}
