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
import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryCursor
import com.itsaky.androidide.treesitter.TSQueryMatch
import com.itsaky.androidide.treesitter.TSTree
import org.slf4j.LoggerFactory

@PublishedApi
internal val log = LoggerFactory.getLogger("TsUtilsKt")

/**
 * Safely create a query cursor and execute the given [TSQuery]. The given [action] will be called for
 * every [TSQueryMatch]. If the tree, cursor, tree or the tree's node is closed or edited while the cursor is
 * querying the node, the [onClosedOrEdited] will be called.
 *
 * This method does not close the [TSQueryCursor] instance.
 */
inline fun <ResultT> TSQueryCursor.safeExecQueryCursor(
  query: TSQuery,
  tree: TSTree?,
  recycleNodeAfterUse: Boolean = true,
  crossinline matchCondition: (TSQueryMatch?) -> Boolean = { true },
  crossinline whileTrue: (TSQueryMatch?) -> Boolean = { true },
  crossinline onClosedOrEdited: () -> Unit = {},
  debugName: String = "",
  debugLogging: Boolean = false,
  crossinline action: (TSQueryMatch) -> ResultT
): ResultT? {

  if (tree == null || !tree.canAccess()) {
    if (debugLogging) {
      log.debug("$debugName: Cannot execute query, tree is null or not accessible", "tree=$tree",
        "tree.canAccess=${tree?.canAccess()}")
    }
    return null
  }

  val rootNode = tree.rootNode
  if (!rootNode.canAccess() || rootNode.hasChanges()) {
    if (debugLogging) {
      log.debug(
        "$debugName, Cannot execute query, tree's root node is not accessible or has been edited",
        "rootNode=$rootNode", "rootNode.canAccess=${rootNode.canAccess()}",
        "rootNode.hasChanges=${rootNode.canAccess() && rootNode.hasChanges()}")
    }
    return null
  }

  return safeExecQueryCursor(
    query = query,
    node = rootNode,
    recycleNodeAfterUse = recycleNodeAfterUse,
    matchCondition = {
      val result = tree.canAccess() && matchCondition(it)
      if (!result && debugLogging) {
        log.debug("$debugName: tree.canAccess=${tree.canAccess()}")
      }
      result
    },
    whileTrue = whileTrue,
    onClosedOrEdited = onClosedOrEdited,
    debugName = debugName,
    debugLogging = debugLogging,
    action = action
  )
}

/**
 * Safely create a query cursor and execute the given [TSQuery]. The given [action] will be called for
 * every [TSQueryMatch]. If the tree, cursor or the node is closed or edited while the cursor is
 * querying the node, the [onClosedOrEdited] will be called.
 *
 * This method does not close the [TSQueryCursor] instance.
 */
inline fun <ResultT> TSQueryCursor.safeExecQueryCursor(
  query: TSQuery,
  node: TSNode,
  recycleNodeAfterUse: Boolean = true,
  crossinline matchCondition: (TSQueryMatch?) -> Boolean = { true },
  crossinline whileTrue: (TSQueryMatch?) -> Boolean = { true },
  crossinline onClosedOrEdited: () -> Unit = {},
  debugName: String = "",
  debugLogging: Boolean = false,
  crossinline action: (TSQueryMatch) -> ResultT
): ResultT? {

  return doSafeExecQueryCursor(
    query = query,
    node = node,
    recycleNodeAfterUse = recycleNodeAfterUse,
    matchCondition = { match ->
      match != null && canAccess() && node.canAccess() && !node.hasChanges() && matchCondition(
        match)
    },
    whileTrue = whileTrue,
    onClosedOrEdited = onClosedOrEdited,
    debugName = debugName,
    debugLogging = debugLogging,
    action = action)
}

@PublishedApi
internal inline fun <ResultT> TSQueryCursor.doSafeExecQueryCursor(
  query: TSQuery,
  node: TSNode,
  recycleNodeAfterUse: Boolean = true,
  crossinline matchCondition: (TSQueryMatch?) -> Boolean,
  crossinline whileTrue: (TSQueryMatch?) -> Boolean,
  crossinline onClosedOrEdited: () -> Unit,
  debugName: String = "",
  debugLogging: Boolean = false,
  crossinline action: (TSQueryMatch) -> ResultT
): ResultT? {

  if (!query.canAccess()) {
    if (debugLogging) {
      log.debug("$debugName: Cannot execute query, query is not accessible")
    }
    return null
  }

  if (!node.canAccess() || node.hasChanges()) {
    if (debugLogging) {
      log.debug("$debugName: Cannot execute query, node is not accessible or has been edited",
        "node.canAccess=${node.canAccess()}",
        "node.hasChanges=${node.canAccess() && node.hasChanges()}")
    }
    return null
  }

  exec(query, node)
  var match = nextMatch()
  while (matchCondition(match) && whileTrue(match)) {

    val result = action(match)

    if (!matchCondition(match)) {
      if (debugLogging) {
        log.debug(
          "$debugName: Cannot proceed with query operation.",
          "cursor.canAccess=${canAccess()}",
          "query.canAccess=${query.canAccess()}",
          "node.canAccess=${node.canAccess()}",
          "node.hasChanges=${node.canAccess() && node.hasErrors()}"
        )
      }
      onClosedOrEdited()
      break
    }

    (match as? TreeSitterQueryMatch?)?.recycle()

    // if the action does not produce any output and simply returns Unit (void)
    // then ignore the result and continue with the capture
    if (result != Unit && result != null) {
      return result
    }

    match = nextMatch()
  }

  if (recycleNodeAfterUse && node is TreeSitterNode && !node.isRecycled) {
    node.recycle()
  }

  return null
}