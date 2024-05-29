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

package com.itsaky.androidide.lsp.java.parser.ts

import com.itsaky.androidide.eventbus.events.file.FileDeletionEvent
import com.itsaky.androidide.eventbus.events.file.FileRenameEvent
import com.itsaky.androidide.lsp.java.parser.IJavaParser
import com.itsaky.androidide.treesitter.TSParser
import com.itsaky.androidide.treesitter.java.TSLanguageJava
import com.itsaky.androidide.utils.StopWatch
import jdkx.tools.JavaFileObject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.slf4j.LoggerFactory

/**
 * [IJavaParser] which uses tree sitter to parse source files.
 *
 * @author Akash Yadav
 */
object TSJavaParser : IJavaParser<TSParseResult> {

  private val cache = TSParseCache(15) // cache 15 results at max

  private var isClosed = false
  private val parser = TSParser.create().also { it.language = TSLanguageJava.getInstance() }
    get() {
      check(!isClosed) { "${javaClass.simpleName} instance has been closed" }
      return field
    }

  private val log = LoggerFactory.getLogger(TSJavaParser::class.java)

  init {
    EventBus.getDefault().register(this)
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  fun onFileDeleted(event: FileDeletionEvent) {
    synchronized(this.cache) { this.cache.remove(event.file.toPath().toAbsolutePath().toUri()) }
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  fun onFileRenamed(event: FileRenameEvent) {
    synchronized(this.cache) {
      val existing = this.cache.remove(event.file.toPath().toAbsolutePath().toUri())
      if (existing != null) {
        this.cache.put(event.newFile.toPath().toAbsolutePath().toUri(), existing)
      }
    }
  }

  override fun parse(file: JavaFileObject): TSParseResult {
    check(file.kind == JavaFileObject.Kind.SOURCE) { "File must a source file object" }

    synchronized(this.cache) {
      val result = this.cache[file.toUri()]
      if (result != null) {
        if (result.fileModified == file.lastModified) {
          // cache hit and cache modified == file modified
          log.info("Using cached parse tree")
          return result
        }
        // cache hit, but cache modified != file modified
        // need to reparse
      }
    }

    parser.reset()
    val watch = StopWatch("[TreeSitter] Parsing")
    val content = file.getCharContent(false).toString()
    if (parser.isParsing) {
      parser.requestCancellationAndWait()
    }
    val parseTree = parser.parseString(content)
    watch.log()

    val result = TSParseResult(file, parseTree)

    synchronized(this.cache) { this.cache.put(result.uri, result) }

    return result
  }

  override fun close() {
    synchronized(this.cache) { this.cache.evictAll() }
    parser.close()
    EventBus.getDefault().unregister(this)
    isClosed = true
  }
}
