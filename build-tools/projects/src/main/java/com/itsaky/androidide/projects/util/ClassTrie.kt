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

package com.itsaky.androidide.projects.util

import java.util.function.*

/**
 * Maps classes with their package name segments.
 *
 * @author Akash Yadav
 */
class ClassTrie {

  val root = Node()

  /**
   * Appends the class name entry to this trie.
   *
   * @param name The fully qualified class name.
   * @return The added node.
   */
  fun append(name: String): Node {
    val segments = segments(name)
    var node = root
    for (segment in segments) {
      node = node.createChild(segment)
    }
    node.isClass = true
    return node
  }

  /**
   * Removes the class name node with the given fully qualified name.
   *
   * @param name The fully qualified name of the package segment or class name to remove.
   */
  fun remove(name: String) {
    remove(name, null)
  }

  /**
   * Removes the nodes (package segments and class names) with the given fully qualified name. A
   * [Predicate] can be provided to check which nodes should be deleted.
   *
   * @param name The fully qualified name of the package segment or the class.
   * @param predicate The predicate which can be used to test which entries should be deleted.
   */
  fun remove(name: String, predicate: Predicate<Node>?) {
    val segments = segments(name)
    val condition = predicate ?: Predicate { it.isClass }
    var node: Node? = root
    for (segment in segments) {
      if (node == null || node.children.isEmpty()) {
        break
      }

      val next = node.children[segment]
      if (next != null && condition.test(next)) {
        node.children.remove(next.name)
      }
      node = next
    }
  }

  /**
   * Finds all class names in the given package.
   *
   * @param packageName The package name to list classes from.
   */
  fun findClassName(packageName: String): List<String> {
    val segments = segments(packageName)
    val classes = mutableListOf<String>()
    val curr = StringBuilder()
    var node: Node? = root
    for (segement in segments) {
      if (node == null || node.children.isEmpty() || !node.children.containsKey(segement)) {
        break
      }

      if (node.isClass) {
        var name = "$curr.$segement"
        if (curr.isEmpty()) {
          name = segement
        }
        classes.add(name)
      } else {
        if (curr.isNotEmpty()) {
          curr.append(".")
        }
        curr.append(segement)
      }

      node = node.children[segement]
    }

    if (node == null || node.children.isEmpty()) {
      return classes
    }
    
    node.children.values.forEach {
      addRecursively(it, curr.toString(), classes)
    }
    return classes
  }
  
  private fun addRecursively(node: Node, curr: String, classes: MutableList<String>) {
    val segement = node.name
    var packageName = curr
    if (node.isClass) {
      var name = "$packageName.$segement"
      if (packageName.isEmpty()) {
        name = segement
      }
      classes.add(name)
    } else {
      packageName = if (curr.isEmpty()) {
        segement
      } else {
        "$packageName.$segement"
      }
    }
  
    if (node.children.isEmpty()) {
      return
    }
    
    node.children.values.forEach {
      addRecursively(it, packageName, classes)
    }
  }
  
  private fun segments(name: String): List<String> {
    return if (name.contains('.')) {
      name.split(".")
    } else {
      listOf(name)
    }
  }

  /** A Node can be a package segment or a class name in the package trie. */
  class Node(val name: String) {

    internal constructor() : this("")

    val children: MutableMap<String, Node> = mutableMapOf()
    var isClass = false

    fun createChild(name: String): Node {
      return children.computeIfAbsent(name) { Node(it) }
    }
  }
}
