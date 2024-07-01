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

package com.itsaky.androidide.lsp.testing

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.eventbus.events.EventReceiver
import com.itsaky.androidide.eventbus.events.editor.ChangeType.DELETE
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentCloseEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentOpenEvent
import com.itsaky.androidide.eventbus.events.file.FileDeletionEvent
import com.itsaky.androidide.eventbus.events.file.FileRenameEvent
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.api.ILanguageServerRegistry
import com.itsaky.androidide.managers.PreferenceManager
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.preferences.internal.EditorPreferences
import com.itsaky.androidide.preferences.internal.prefManager
import com.itsaky.androidide.projects.FileManager
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.testing.tooling.ToolingApiTestLauncher
import com.itsaky.androidide.testing.tooling.models.ToolingApiTestLauncherParams
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.FileProvider
import io.github.rosemoe.sora.text.Content
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path

/**
 * Runs tests for a language server.
 *
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_VALUE_STRING)
abstract class LSPTest(
  private val sourceFileExt: String,
) {

  protected lateinit var toolingServer: IToolingApiServer
  protected lateinit var toolingProject: IProject
  var cursor: Int = -1
  private val cursorText = "@@cursor@@"
  var file: Path? = null
  var contents: StringBuilder? = null

  companion object {

    @JvmStatic
    protected val log: Logger = LoggerFactory.getLogger(LSPTest::class.java)

    @JvmStatic
    protected var isInitialized: Boolean = false
  }

  @Before
  open fun initProjectIfNeeded() {
    if (isInitialized) {
      return
    }

    mockkStatic(::prefManager)
    every { prefManager } returns PreferenceManager(RuntimeEnvironment.getApplication())

    mockkStatic(EditorPreferences::tabSize)
    every { EditorPreferences.tabSize } returns 4

    val params = ToolingApiTestLauncherParams()
    ToolingApiTestLauncher.launchServer(params) {

      assertThat(result?.isSuccessful).isTrue()

      this@LSPTest.toolingProject = project
      this@LSPTest.toolingServer = server

      Lookup.getDefault().update(BuildService.KEY_PROJECT_PROXY, project)

      Environment.ANDROID_JAR = FileProvider.resources().resolve("android.jar").toFile()
      Environment.JAVA_HOME = File(System.getProperty("java.home")!!)
      registerServer()

      val projectManager = IProjectManager.getInstance()
      if (projectManager is EventReceiver) {
        projectManager.register()
      } else {
        throw IllegalStateException("Expected IProjectManager instance to be an EventReceiver")
      }

      projectManager.openProject(params.projectDir.toFile())
      runBlocking { projectManager.setupProject(project) }

      // We need to manually setup the language server with the project here
      // ProjectManager.notifyProjectUpdate()
      ILanguageServerRegistry.getDefault()
        .getServer(getServerId())!!
        .setupWorkspace(projectManager.getWorkspace()!!)

      isInitialized = true
    }
  }

  protected abstract fun registerServer()
  protected abstract fun getServerId(): String
  abstract fun test()

  fun requireCursor(): Int {
    this.cursor = contents!!.indexOf(cursorText)
    assertThat(cursor).isGreaterThan(-1)
    return cursor
  }

  fun deleteCursorText() {
    contents!!.delete(this.cursor, this.cursor + cursorText.length)
    assertThat(contents!!.indexOf(cursorText)).isEqualTo(-1)

    // As the content has been changed, we have to
    // Update the content in language server
    dispatchEvent(
      DocumentChangeEvent(
        file!!,
        contents.toString(),
        contents.toString(),
        1,
        DELETE,
        0,
        Range.NONE
      )
    )
  }

  @JvmOverloads
  fun cursorPosition(deleteCursorText: Boolean = true): Position {
    requireCursor()

    if (deleteCursorText) {
      deleteCursorText()
    }

    val pos = Content(contents!!).indexer.getCharPosition(cursor)
    return Position(pos.line, pos.column, pos.index)
  }

  open fun openFile(fileName: String) {
    file = FileProvider.sourceFile(fileName, sourceFileExt).normalize()
    contents = FileProvider.contents(file!!)

    dispatchEvent(DocumentOpenEvent(file!!, contents.toString(), 0))
  }

  open fun dispatchEvent(event: Any) {
    when (event) {
      is DocumentOpenEvent -> FileManager.onDocumentOpen(event)
      is DocumentChangeEvent -> FileManager.onDocumentContentChange(event)
      is DocumentCloseEvent -> FileManager.onDocumentClose(event)
      is FileRenameEvent -> FileManager.onFileRenamed(event)
      is FileDeletionEvent -> FileManager.onFileDeleted(event)
    }
    EventBus.getDefault().post(event)
  }

  open fun createActionData(vararg values: Any): ActionData {
    val data = ActionData()

    data.put(Context::class.java, RuntimeEnvironment.getApplication())
    for (value in values) {
      if (value is Path) {
        data.put(Path::class.java, value)
      } else {
        data.put(value::javaClass.get(), value)
      }
    }

    return data
  }
}
