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

package com.itsaky.androidide.lsp.snippets

import com.google.gson.JsonParseException
import com.google.gson.stream.JsonReader
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.tasks.executeAsyncProvideError
import com.itsaky.androidide.utils.VMUtils
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

/**
 * Parser for parsing snippets from assets.
 *
 * @author Akash Yadav
 */
object SnippetParser {

  private val log = LoggerFactory.getLogger(SnippetParser::class.java)

  fun <S : ISnippetScope> parse(
    lang: String,
    scopes: Array<S>,
    snippetFactory: (String, String, List<String>) -> ISnippet = { prefix, desc, body ->
      DefaultSnippet(prefix, desc, body.toTypedArray())
    }
  ): Map<S, List<ISnippet>> {

    // not supported for tests as assets cannot be accessed
    if (VMUtils.isJvm()) {
      return emptyMap()
    }

    return ConcurrentHashMap<S, List<ISnippet>>().apply {
      for (scope in scopes) {
        this[scope] =
          mutableListOf<ISnippet>().apply {
            readSnippets(lang, scope.filename, snippetFactory, this)
          }
      }
    }
  }

  private fun readSnippets(
    lang: String,
    type: String,
    snippetFactory: (String, String, List<String>) -> ISnippet,
    snippets: MutableList<ISnippet>
  ) {
    executeAsyncProvideError({
      val content =
        try {
          BaseApplication.getBaseInstance()
            .assets
            .open(assetsPath(lang, type))
            .reader()
        } catch (e: IOException) {
          // snippet file probably does not exist
          return@executeAsyncProvideError
        }

      JsonReader(content).use {
        it.beginObject()
        while (it.hasNext()) {
          val prefix = it.nextName()
          readSnippet(prefix, it, snippetFactory, snippets)
        }
        it.endObject()
      }
    }) { result, err ->
      if (result == null || err != null) {
        log.error("Failed to load '{}' snippets", type, err)
      }
    }
  }

  fun assetsPath(lang: String, type: String) =
    "data/editor/${lang}/snippets.${type}.json"

  private fun readSnippet(
    prefix: String,
    reader: JsonReader,
    snippetFactory: (String, String, List<String>) -> ISnippet,
    snippets: MutableList<ISnippet>
  ) {
    reader.beginObject()
    var desc: String? = null
    val body = mutableListOf<String>()
    while (reader.hasNext()) {
      val n = reader.nextName()
      if (n != "desc" && n != "body") {
        throw JsonParseException("'desc' or 'body' was expected, but found '${n}'")
      }

      if (n == "desc") {
        desc = reader.nextString()
        continue
      }

      if (n == "body") {
        reader.beginArray()
        while (reader.hasNext()) {
          body.add(reader.nextString())
        }
        reader.endArray()
      }
    }

    checkNotNull(desc) { "DefaultSnippet description not defined for '${prefix}'" }
    check(body.isNotEmpty()) { "DefaultSnippet body not defined for '${prefix}'" }

    snippets.add(snippetFactory(prefix, desc, body))

    reader.endObject()
  }
}
