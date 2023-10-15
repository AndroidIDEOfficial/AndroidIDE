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

package com.itsaky.androidide.treesitter.api

import com.itsaky.androidide.treesitter.TSNode
import com.itsaky.androidide.treesitter.TSPoint
import com.itsaky.androidide.treesitter.TSTree
import com.itsaky.androidide.treesitter.TSTreeCursor
import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.RecyclableObjectPool

/**
 * @author Akash Yadav
 */
class TreeSitterNode @JvmOverloads internal constructor(
  context0: Int = 0,
  context1: Int = 0,
  context2: Int = 0,
  context3: Int = 0,
  id: Long = 0,
  tree: Long = 0
) : TSNode(context0, context1, context2, context3, id, tree),
  RecyclableObjectPool.Recyclable by DefaultRecyclable(), TSSynchronized by DefaultSynchronized() {

  companion object {

    @JvmStatic
    fun obtain(
      context0: Int, context1: Int, context2: Int, context3: Int, id: Long, tree: Long
    ): TreeSitterNode {
      return obtainFromPool<TreeSitterNode>().apply {
        this.context0 = context0
        this.context1 = context1
        this.context2 = context2
        this.context3 = context3
        this.id = id
        this.tree = tree
      }
    }
  }

  override fun getTree(): TSTree {
    return withLock { super.getTree() }
  }

  override fun getChild(index: Int): TSNode {
    return withLock { super.getChild(index) }
  }

  override fun getNamedChild(index: Int): TSNode {
    return withLock { super.getNamedChild(index) }
  }

  override fun getChildByFieldName(fieldName: String?): TSNode {
    return withLock { super.getChildByFieldName(fieldName) }
  }

  override fun walk(): TSTreeCursor {
    return withLock { super.walk() }
  }

  override fun findNodeWithType(type: String?, namedOnly: Boolean): TSNode {
    return withLock { super.findNodeWithType(type, namedOnly) }
  }

  override fun findChildrenWithType(type: String?, reverseSearch: Boolean,
    namedOnly: Boolean): MutableList<TSNode> {
    return withLock { super.findChildrenWithType(type, reverseSearch, namedOnly) }
  }

  override fun findChildrenWithTypeReverse(type: String?, namedOnly: Boolean): MutableList<TSNode> {
    return withLock { super.findChildrenWithTypeReverse(type, namedOnly) }
  }

  override fun getParent(): TSNode {
    return withLock { super.getParent() }
  }

  override fun getFieldNameForChild(p0: Int): String {
    return withLock { super.getFieldNameForChild(p0) }
  }

  override fun getChildByFieldId(p0: Int): TSNode {
    return withLock { super.getChildByFieldId(p0) }
  }

  override fun getNextSibling(): TSNode {
    return withLock { super.getNextSibling() }
  }

  override fun getPreviousSibling(): TSNode {
    return withLock { super.getPreviousSibling() }
  }

  override fun getNextNamedSibling(): TSNode {
    return withLock { super.getNextNamedSibling() }
  }

  override fun getPreviousNamedSibling(): TSNode {
    return withLock { super.getPreviousNamedSibling() }
  }

  override fun getFirstChildForByte(p0: Int): TSNode {
    return withLock { super.getFirstChildForByte(p0) }
  }

  override fun getFirstNamedChildForByte(p0: Int): TSNode {
    return withLock { super.getFirstNamedChildForByte(p0) }
  }

  override fun getDescendantForByteRange(p0: Int, p1: Int): TSNode {
    return withLock { super.getDescendantForByteRange(p0, p1) }
  }

  override fun getDescendantForPointRange(p0: TSPoint?, p1: TSPoint?): TSNode {
    return withLock { super.getDescendantForPointRange(p0, p1) }
  }

  override fun getNamedDescendantForByteRange(p0: Int, p1: Int): TSNode {
    return withLock { super.getNamedDescendantForByteRange(p0, p1) }
  }

  override fun getNamedDescendantForPointRange(p0: TSPoint?, p1: TSPoint?): TSNode? {
    return withLock { super.getNamedDescendantForPointRange(p0, p1) }
  }

  override fun isEqualTo(p0: TSNode?): Boolean {
    return withLock { super.isEqualTo(p0) }
  }

  override fun getChildCount(): Int {
    return withLock { super.getChildCount() }
  }

  override fun getNamedChildCount(): Int {
    return withLock { super.getNamedChildCount() }
  }

  override fun getNodeString(): String {
    return withLock { super.getNodeString() }
  }

  override fun getStartByte(): Int {
    return withLock { super.getStartByte() }
  }

  override fun getEndByte(): Int {
    return withLock { super.getEndByte() }
  }

  override fun getStartPoint(): TSPoint {
    return withLock { super.getStartPoint() }
  }

  override fun getEndPoint(): TSPoint {
    return withLock { super.getEndPoint() }
  }

  override fun getType(): String {
    return withLock { super.getType() }
  }

  override fun getSymbol(): Int {
    return withLock { super.getSymbol() }
  }

  override fun isNull(): Boolean {
    return withLock { super.isNull() }
  }

  override fun isNamed(): Boolean {
    return withLock { super.isNamed() }
  }

  override fun isExtra(): Boolean {
    return withLock { super.isExtra() }
  }

  override fun isMissing(): Boolean {
    return withLock { super.isMissing() }
  }

  override fun hasChanges(): Boolean {
    return withLock { super.hasChanges() }
  }

  override fun hasErrors(): Boolean {
    return withLock { super.hasErrors() }
  }

  override fun isError(): Boolean {
    return withLock { super.isError() }
  }

  override fun getParseState(): Short {
    return withLock { super.getParseState() }
  }

  override fun recycle() {
    withLock {
      this.context0 = 0
      this.context1 = 0
      this.context2 = 0
      this.context3 = 0
      this.id = 0
      this.tree = 0
      this.mTree = null
      returnToPool()
    }
  }
}