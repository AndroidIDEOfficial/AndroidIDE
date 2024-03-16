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

package com.itsaky.androidide.utils

import java.util.concurrent.ConcurrentHashMap
import java.util.function.Predicate

/**
 * Maps classes with their package name segments.
 *
 * @author Akash Yadav
 */
open class ClassTrie(val root: Node = Node()) {

  /**
   * Appends the class name entry to this trie.
   *
   * @param name The fully qualified class name.
   * @return The added node.
   */
  open fun append(name: String): Node {
    val segments = segments(name)
    var node = root
    for ((index, segment) in segments.withIndex()) {
      if (segment.isEmpty() || !segment[0].isJavaIdentifierStart()) {
        continue
      }

      node = createNode(node, segment, segments, index)
    }
    node.isClass = true
    return node
  }

  /**
   * Removes the class name node with the given fully qualified name.
   *
   * @param name The fully qualified name of the package segment or class name to remove.
   */
  open fun remove(name: String) {
    remove(name, null)
  }

  /**
   * Removes the nodes (package segments and class names) with the given fully qualified name. A
   * [Predicate] can be provided to check which nodes should be deleted.
   *
   * @param name The fully qualified name of the package segment or the class.
   * @param predicate The predicate which can be used to test which entries should be deleted.
   */
  open fun remove(name: String, predicate: Predicate<Node>?) {
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

  /** Removes all entries from this trie. */
  fun clear() {
    this.root.children.clear()
  }

  /**
   * Checks if there is an entry with the given fully qualified name (package or class name).
   *
   * @param name The fully qualified package or class name.
   * @return Whether there is a an entry for the given fully qualified name.
   */
  open fun contains(name: String): Boolean {
    val segments = segments(name)
    var node: Node? = root
    for (segment in segments) {
      if (node == null) {
        return false
      }

      node = node.children[segment]
    }
    return node != null
  }

  /**
   * Finds all class nodes in the given package.
   *
   * @param packageName The package name to list class nodes from.
   */
  open fun findInPackage(packageName: String): List<Node> {
    val segments = segments(packageName)
    val classes = mutableListOf<Node>()
    var node: Node? = root
    for (segement in segments) {
      if (node == null || node.children.isEmpty() || !node.children.containsKey(segement)) {
        break
      }

      if (node.isClass) {
        classes.add(node)
      }

      node = node.children[segement]
    }

    if (node == null || node.children.isEmpty()) {
      return classes
    }

    node.children.values.forEach { addRecursively(it, classes) }
    return classes
  }

  /**
   * Finds all class names in the given package.
   *
   * @param packageName The package name to list classes from.
   */
  open fun findClassNames(packageName: String): List<String> {
    return findInPackage(packageName).map { it.qualifiedName }
  }

  /**
   * Finds node with the given qualified name. Or `null` if none was found.
   *
   * @param qualifiedName The fully qualified name of the node to find.
   */
  open fun findNode(qualifiedName: String): Node? {
    val segments = segments(qualifiedName)
    var node: Node? = root
    for (segment in segments) {
      if (node == null) {
        break
      }
      node = node.children[segment]
    }

    return node
  }

  /** Returns all class nodes available in this trie. */
  fun allClassNodes(): Set<Node> {
    return root.allClassNodes()
  }

  /** Returns all classes available in this trie. */
  fun allClassNames(): Set<String> {
    return root.allClassNames()
  }

  fun print() {
    root.children.forEach { print(it.value, 0) }
  }

  fun print(node: Node?, indent: Int) {
    if (node == null) {
      return
    }

    val prefix = if (indent == 0) "" else "| ".repeat(indent / 2)
    println(prefix + node.name)
    if (node.children.isNotEmpty()) {
      node.children.forEach { print(it.value, indent + 2) }
    }
  }

  open fun segments(name: String): List<String> {
    return if (name.contains('.')) {
      name.split(".")
    } else {
      listOf(name)
    }
  }

  protected open fun createNode(node: Node, segment: String, segments: List<String>, index: Int) =
    node.createChild(segment, segments.subList(0, index + 1).joinToString(separator = "."))

  private fun addRecursively(node: Node, classes: MutableList<Node>) {
    if (node.isClass) {
      classes.add(node)
    }

    if (node.children.isEmpty()) {
      return
    }

    node.children.values.forEach { addRecursively(it, classes) }
  }

  /** A Node can be a package segment or a class name in the package trie. */
  open class Node(val name: String, val qualifiedName: String, val parent: Node? = null) {

    internal constructor() : this("", "")

    val children: MutableMap<String, Node> = ConcurrentHashMap()
    var isClass = false

    open fun createChild(name: String, qualifiedName: String): Node {
      return children.computeIfAbsent(name) { Node(it, qualifiedName, this) }
    }

    open fun removeChild(child: Node) {
      this.children.remove(child.name)
    }

    open fun allClassNames(): Set<String> {
      return this.allClassNodes().map { it.qualifiedName }.toSet()
    }

    open fun allClassNodes(): Set<Node> {
      val all = mutableSetOf<Node>()

      if (this.isClass) {
        all.add(this)
      }

      if (children.isNotEmpty()) {
        children.values.forEach { all.addAll(it.allClassNodes()) }
      }
      return all
    }
  }
}
