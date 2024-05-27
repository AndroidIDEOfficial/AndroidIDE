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

package com.itsaky.androidide.models.workspace

import com.blankj.utilcode.util.ThreadUtils
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.itsaky.androidide.models.workspace.WorkspaceSettings.Companion.EditorWorkspaceSettingsWrapper
import com.itsaky.androidide.preferences.internal.EditorPreferences
import com.itsaky.androidide.utils.flashError
import org.slf4j.LoggerFactory
import java.io.Reader

data class EditorWorkspaceSettings(
  @SerializedName(KEY_COMPLETIONS_MATCH_LOWER)
  var completionsMatchLower: Boolean = EditorPreferences.completionsMatchLower,
  @SerializedName(KEY_DRAW_LEADING_WS)
  var drawLeadingWs: Boolean = EditorPreferences.drawLeadingWs,
  @SerializedName(KEY_DRAW_TRAILING_WS)
  var drawTrailingWs: Boolean = EditorPreferences.drawTrailingWs,
  @SerializedName(KEY_DRAW_INNER_WS)
  var drawInnerWs: Boolean = EditorPreferences.drawInnerWs,
  @SerializedName(KEY_DRAW_EMPTY_LINE_WS)
  var drawEmptyLineWs: Boolean = EditorPreferences.drawEmptyLineWs,
  @SerializedName(KEY_DRAW_LINE_BREAK)
  var drawLineBreak: Boolean = EditorPreferences.drawLineBreak,
  @SerializedName(KEY_FONT_SIZE)
  var fontSize: Float = EditorPreferences.fontSize,
  @SerializedName(KEY_TAB_SIZE)
  var tabSize: Int = EditorPreferences.tabSize,
  @SerializedName(KEY_AUTO_SAVE)
  var autoSave: Boolean = EditorPreferences.autoSave,
  @SerializedName(KEY_FONT_LIGATURES)
  var fontLigatures: Boolean = EditorPreferences.fontLigatures,
  @SerializedName(KEY_VISIBLE_PASSWORD_FLAG)
  var visiblePasswordFlag: Boolean = EditorPreferences.visiblePasswordFlag,
  @SerializedName(KEY_WORDWRAP)
  var wordwrap: Boolean = EditorPreferences.wordwrap,
  @SerializedName(KEY_USE_SOFT_TAB)
  var useSoftTab: Boolean = EditorPreferences.useSoftTab,
  @SerializedName(KEY_DELETE_TABS_ON_BACKSPACE)
  var deleteTabsOnBackspace: Boolean = EditorPreferences.deleteTabsOnBackspace,
  @SerializedName(KEY_STICKY_SCROLL_ENABLED)
  var stickyScrollEnabled: Boolean = EditorPreferences.stickyScrollEnabled,
  @SerializedName(KEY_PIN_LINE_NUMBERS)
  var pinLineNumbers: Boolean = EditorPreferences.pinLineNumbers
) : WorkspaceSettings() {

  companion object {

    private val log = LoggerFactory.getLogger(EditorWorkspaceSettings::class.java)

    const val KEY_COMPLETIONS_MATCH_LOWER = "matchCompletionsInLowercase"
    const val KEY_DRAW_LEADING_WS = "drawLeadingWhitespace"
    const val KEY_DRAW_TRAILING_WS = "drawTrailingWhitespace"
    const val KEY_DRAW_INNER_WS = "drawInnerWhitespace"
    const val KEY_DRAW_EMPTY_LINE_WS = "drawEmptyLineWhitespace"
    const val KEY_DRAW_LINE_BREAK = "drawLineBreak"
    const val KEY_FONT_SIZE = "fontSize"
    const val KEY_TAB_SIZE = "tabSpace"
    const val KEY_AUTO_SAVE = "autoSave"
    const val KEY_FONT_LIGATURES = "fontLigatures"
    const val KEY_VISIBLE_PASSWORD_FLAG = "visiblePasswordFlag"
    const val KEY_WORDWRAP = "wordwrap"
    const val KEY_USE_SOFT_TAB = "useSoftTab"
    const val KEY_DELETE_TABS_ON_BACKSPACE = "deleteTabsOnBackspace"
    const val KEY_STICKY_SCROLL_ENABLED = "stickyScrollEnabled"
    const val KEY_PIN_LINE_NUMBERS = "pinLineNumbers"

    @JvmStatic
    fun parse(contentReader: Reader): EditorWorkspaceSettings? {
      return try {
        Gson().fromJson(
          contentReader,
          EditorWorkspaceSettingsWrapper::class.java
        ).editorWorkspaceSettings
      } catch (err: Exception) {
        ThreadUtils.runOnUiThread {
          flashError("$SETTINGS_FILE_NAME: ${err.message}")
        }.also {
          log.error("Error parsing workspace settings: ${err.message}", err)
        }
        null
      }
    }
  }
}