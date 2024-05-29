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

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import kotlin.io.path.nameWithoutExtension

/**
 * A [ClassTrie] implementation to store information about java source files.
 *
 * @author Akash Yadav
 */
open class SourceClassTrie(root: SourcePackageNode = SourcePackageNode()) : ClassTrie(root) {

  companion object {
    @JvmStatic
    private val pkgNameMethod by lazy {
      Class.forName("com.itsaky.androidide.projects.util.StringSearch")
        .getDeclaredMethod("packageName", Path::class.java)
    }

    @JvmStatic
    private fun packageName(file: Path): String {
      return try {
        pkgNameMethod.invoke(null, file) as String
      } catch (error: Throwable) {
        throw RuntimeException(error)
      }
    }
  }

  override fun append(name: String): Node {
    throw UnsupportedOperationException("Method not supported!")
  }

  open fun append(path: Path, sourceDir: Path): SourceNode {
    val modified = Files.getLastModifiedTime(path).toInstant()
    val packageName = packageName(path)
    val segments = segments(packageName)
    var node: SourcePackageNode = root as SourcePackageNode
    var currentPath = sourceDir
    for ((index, segment) in segments.withIndex()) {
      if (segment.isEmpty() || !segment[0].isJavaIdentifierStart()) {
        continue
      }

      currentPath = currentPath.resolve(segment)
      node = createNode(currentPath, node, segment, segments, index)
    }
    node.isClass = false

    val name = path.nameWithoutExtension
    val klass =
      SourceNode(
        file = path,
        packageName = packageName,
        modified = modified,
        parent = node,
        name = name
      )
    klass.isClass = true
    node.children[name] = klass
    return klass
  }

  open fun createNode(
    dir: Path,
    node: SourcePackageNode,
    segment: String,
    segments: List<String>,
    index: Int
  ) = node.createChild(dir, segment, segments.subList(0, index + 1).joinToString(separator = "."))

  override fun createNode(node: Node, segment: String, segments: List<String>, index: Int): Node {
    throw UnsupportedOperationException("Method not supported")
  }

  open fun allSources(): List<SourceNode> {
    return allClassNodes().filterIsInstance(SourceNode::class.java)
  }

  fun findSource(file: Path): SourceNode? {
    return findSourceInNode(file, root)
  }

  fun findDirNode(dir: Path, node: Node): SourcePackageNode? {
    var dirNode: SourcePackageNode? = null
    for (child in node.children.values) {
      if (
        child !is SourceNode &&
          child is SourcePackageNode &&
          DocumentUtils.isSameFile(child.dir, dir)
      ) {
        dirNode = child
      }

      dirNode = dirNode ?: findDirNode(dir, child)

      if (dirNode != null) {
        break
      }
    }
    return dirNode
  }

  fun getSourceFilesInDir(dir: Path): List<SourceNode> {
    val dirNode = findDirNode(dir, root) ?: return emptyList()
    return dirNode.children.values.filterIsInstance(SourceNode::class.java)
  }

  private fun findSourceInNode(file: Path, node: Node): SourceNode? {

    if (node is SourceNode && DocumentUtils.isSameFile(file, node.file)) {
      return node
    }

    for (child in node.children.values) {
      if (child is SourceNode && DocumentUtils.isSameFile(file, child.file)) {
        return child
      }

      val childResult = findSourceInNode(file, child)
      if (childResult != null) {
        return childResult
      }
    }
    return null
  }

  open class SourceNode(
    val file: Path,
    val packageName: String,
    val modified: Instant,
    parent: SourcePackageNode? = null,
    name: String
  ) : Node(name = name, qualifiedName = "$packageName.$name", parent = parent) {

    override fun createChild(name: String, qualifiedName: String): Node {
      throw UnsupportedOperationException("Method not supported!")
    }
  }

  open class SourcePackageNode(
    val dir: Path,
    parent: SourcePackageNode? = null,
    name: String,
    qualifiedName: String
  ) : Node(name, qualifiedName, parent) {

    internal constructor() : this(Paths.get(""), null, "", "")

    override fun createChild(name: String, qualifiedName: String): Node {
      throw UnsupportedOperationException("Method not suported!")
    }

    open fun createChild(dir: Path, name: String, qualifiedName: String): SourcePackageNode {
      return this.children.computeIfAbsent(name) {
        SourcePackageNode(dir, this, name, qualifiedName)
      } as SourcePackageNode
    }
  }
}
