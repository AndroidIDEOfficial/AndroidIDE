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
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE
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
  private const val SCHEME_VERSION = "scheme.version"
  private const val SCHEME_IS_DARK = "scheme.isDark"
  private const val SCHEME_LANGS = "scheme.langs"
  private const val SCHEME_FILE = "scheme.file"

  val currentScheme: IDEColorScheme? by lazy {
    val scheme = this.schemes[colorScheme] ?: return@lazy null
    return@lazy try {
      scheme.load()
      scheme
    } catch (err: Exception) {
      log.error("An error occurred while loading color scheme '$colorScheme'", err)
      null
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
      val version = props.getProperty(SCHEME_VERSION, "0").toInt()
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
      
      if (version <= 0) {
        log.warn("Version code of color scheme '$schemeDir' must be set to >= 1")
      }
      
      if (file.isBlank()) {
        continue
      }
      if (langs.isEmpty()) {
        log.error("Scheme '${schemeDir.name}' does not specify any languages")
        continue
      }

      val scheme = IDEColorScheme(File(schemeDir, file), schemeDir.name)
      scheme.name = name
      scheme.version = version
      scheme.isDarkScheme = isDark
      scheme.langs = langs.toTypedArray()
      schemes[schemeDir.name] = scheme
    }
  }
  
  @JvmStatic
  fun initIfNeeded() {
    if (this.schemes.isEmpty()) {
      init()
    }
  }

  fun readScheme(schemeConsumer: Consumer<SchemeAndroidIDE?>) {
    readScheme {
      schemeConsumer.accept(it)
    }
  }

  fun readScheme(consume: (SchemeAndroidIDE?) -> Unit) {
    executeAsyncProvideError({ this.currentScheme }) { scheme, error ->
      if (scheme == null || error != null) {
        log.error("Failed to read color scheme", error)
        return@executeAsyncProvideError
      }
      consume(scheme)
    }
  }

  fun list(): List<IDEColorScheme> {
    return this.schemes.values.toList()
  }
  
  fun destroy() {
    this.schemes.clear()
  }
}
