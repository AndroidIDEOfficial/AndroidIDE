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

package com.itsaky.androidide.editor.schemes.internal.parser

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken.BEGIN_OBJECT
import com.google.gson.stream.JsonToken.STRING
import com.itsaky.androidide.editor.schemes.IDEColorScheme
import com.itsaky.androidide.editor.schemes.LanguageScheme
import com.itsaky.androidide.editor.schemes.StyleDef

/**
 * Parses language definitions in JSON color scheme files.
 *
 * @author Akash Yadav
 */
class LanguageParser(private var reader: JsonReader) {

  fun parseLang(scheme: IDEColorScheme): LanguageScheme {
    return scheme.run {
      doParseLang()
    }
  }

  private fun IDEColorScheme.doParseLang(): LanguageScheme {
    reader.beginObject()
    val lang = LanguageScheme()
    while (reader.hasNext()) {
      var name = reader.nextName()
      when (name) {
        "types" -> parseLangTypes(lang)
        "local.scopes" -> parseLangLocalScopes(lang)
        "local.scopes.members" -> parseLangLocalsMembersScopes(lang)
        "local.definitions" -> parseLocalLangDefs(lang)
        "local.definitions.values" -> parseLocalLangDefVals(lang)
        "local.references" -> parseLocalLangRefs(lang)
        "styles" -> {
          reader.beginObject()
          while (reader.hasNext()) {
            name = reader.nextName()
            if (reader.peek() == BEGIN_OBJECT) {
              lang.styles[name] = parseStyleDef(reader)
            } else if (reader.peek() == STRING) {
              val color = parseColorValue(reader.nextString())
              lang.styles[name] = StyleDef(fg = color)
            } else throw ParseException("A style definition must an object or a string value")
          }
          reader.endObject()
        }
        else -> throw ParseException("Unexpected key '$name' in language object")
      }
    }
    reader.endObject()

    if (lang.files.isEmpty()) {
      throw ParseException("A language must specify the file types")
    }

    return lang
  }

  private fun parseLocalLangRefs(lang: LanguageScheme) {
    addArrStrings(lang.localRefs)
  }

  private fun parseLocalLangDefVals(lang: LanguageScheme) {
    addArrStrings(lang.localDefVals)
  }

  private fun parseLocalLangDefs(lang: LanguageScheme) {
    addArrStrings(lang.localDefs)
  }

  private fun parseLangLocalScopes(lang: LanguageScheme) {
    addArrStrings(lang.localScopes)
  }
  
  private fun parseLangLocalsMembersScopes(lang: LanguageScheme) {
    addArrStrings(lang.localMembersScopes)
  }

  private fun parseLangTypes(lang: LanguageScheme) {
    addArrStrings(lang.files)
  }

  private fun addArrStrings(collection: MutableCollection<String>) {
    reader.beginArray()
    while (reader.hasNext()) {
      collection.add(reader.nextString())
    }
    reader.endArray()
  }
}
