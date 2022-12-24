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

package com.itsaky.androidide.editor.schemes

import com.itsaky.androidide.preferences.internal.colorScheme
import com.itsaky.androidide.tasks.executeAsyncProvideError
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.io.FileFilter
import java.util.Properties
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

/** @author Akash Yadav */
object IDEColorSchemeProvider {

  private val log = ILogger.newInstance("IDEColorSchemeProvider")
  private val schemesDir = File(Environment.ANDROIDIDE_UI, "editor/schemes")

  private val schemes = ConcurrentHashMap<String, IDEColorScheme>()

  private const val SCHEME_NAME = "scheme.name"
  private const val SCHEME_IS_DARK = "scheme.isDark"
  private const val SCHEME_LANGS = "scheme.langs"
  private const val SCHEME_FILE = "scheme.file"

  val currentScheme: IDEColorScheme? by lazy {
    this.schemes[colorScheme]?.let {
      it.load()
      it
    }
  }

  @JvmStatic
  fun init() {
    val schemeDirs =
      schemesDir.listFiles(FileFilter { it.isDirectory && File(it, "scheme.prop").exists() })
        ?: run {
          log.error("No color schemes found")
          return
        }

    for (schemeDir in schemeDirs) {
      val prop = File(schemeDir, "scheme.prop")
      val props =
        try {
          Properties().apply { load(prop.reader()) }
        } catch (err: Exception) {
          log.error("Failed to read properties for scheme '${schemeDir.name}'")
          continue
        }

      val name = props.getProperty(SCHEME_NAME, "Unknown")
      val isDark = props.getProperty(SCHEME_IS_DARK, "false").toBoolean()
      val langs = props.getProperty(SCHEME_LANGS, "").split(',').map { it.trim() }
      val file =
        props.getProperty(SCHEME_FILE)
          ?: run {
            log.error(
              "Scheme '${schemeDir.name}' does not specify 'scheme.file' in scheme.prop file"
            )
            ""
          }

      if (file.isBlank()) {
        continue
      }
      if (langs.isEmpty()) {
        log.error("Scheme '${schemeDir.name}' does not specify any languages")
        continue
      }

      val scheme = IDEColorScheme(File(schemeDir, file))
      scheme.name = name
      scheme.isDarkScheme = isDark
      scheme.langs = langs.toTypedArray()
      schemes[schemeDir.name] = scheme
    }
    
    currentScheme!!
  }

  fun readScheme(schemeConsumer: Consumer<IDEColorScheme>) {
    readScheme(schemeConsumer::accept)
  }

  fun readScheme(consume: (IDEColorScheme) -> Unit) {
    executeAsyncProvideError({ this.currentScheme }) { scheme, error ->
      if (scheme == null || error != null) {
        log.error("Failed to read color scheme", error)
        return@executeAsyncProvideError
      }
      consume(scheme)
    }
  }
}
