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

package com.itsaky.androidide.activities

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.itsaky.androidide.terminal.IdeTerminalSessionClient
import com.itsaky.androidide.terminal.IdesetupSession
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.flashError
import com.termux.R
import com.termux.app.TermuxActivity
import com.termux.app.terminal.TermuxTerminalSessionActivityClient
import com.termux.shared.termux.shell.command.runner.terminal.TermuxSession
import org.slf4j.LoggerFactory

/**
 * @author Akash Yadav
 */
class TerminalActivity : TermuxActivity() {

  override val navigationBarColor: Int
    get() = ContextCompat.getColor(this, android.R.color.black)
  override val statusBarColor: Int
    get() = ContextCompat.getColor(this, android.R.color.black)

  private var canAddNewSessions = true
    set(value) {
      field = value
      findViewById<View>(R.id.new_session_button)?.isEnabled = value
    }

  companion object {

    private val log = LoggerFactory.getLogger(TerminalActivity::class.java)
    private const val KEY_TERMINAL_CAN_ADD_SESSIONS = "ide.terminal.sessions.canAddSessions"

    const val EXTRA_ONBOARDING_RUN_IDESETUP = "ide.onboarding.terminal.runIdesetup"
    const val EXTRA_ONBOARDING_RUN_IDESETUP_ARGS = "ide.onboarding.terminal.runIdesetup.args"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    val controller = WindowCompat.getInsetsController(
      window, window.decorView)
    controller.isAppearanceLightNavigationBars = false
    controller.isAppearanceLightStatusBars = false
    super.onCreate(savedInstanceState)

    canAddNewSessions = savedInstanceState?.getBoolean(
      KEY_TERMINAL_CAN_ADD_SESSIONS, true) ?: true
  }

  override fun onCreateTerminalSessionClient(): TermuxTerminalSessionActivityClient {
    return IdeTerminalSessionClient(this)
  }

  override fun onSaveInstanceState(savedInstanceState: Bundle) {
    super.onSaveInstanceState(savedInstanceState)
    savedInstanceState.putBoolean(KEY_TERMINAL_CAN_ADD_SESSIONS, canAddNewSessions)
  }

  override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
    super.onServiceConnected(componentName, service)
    Environment.mkdirIfNotExits(Environment.TMP_DIR)
  }

  override fun onCreateNewSession(
    isFailsafe: Boolean,
    sessionName: String?,
    workingDirectory: String?
  ) {
    if (canAddNewSessions) {
      super.onCreateNewSession(isFailsafe, sessionName, workingDirectory)
    } else {
      flashError(R.string.msg_terminal_new_sessions_disabled)
    }
  }

  override fun setupTermuxSessionOnServiceConnected(
    intent: Intent?,
    workingDir: String?,
    sessionName: String?,
    existingSession: TermuxSession?,
    launchFailsafe: Boolean
  ) {
    if (intent != null) {
      val runIdesetup = intent.getBooleanExtra(EXTRA_ONBOARDING_RUN_IDESETUP, false)
      val runIdesetupArgs = intent.getStringArrayExtra(EXTRA_ONBOARDING_RUN_IDESETUP_ARGS)
      if (runIdesetup && !runIdesetupArgs.isNullOrEmpty()) {
        addIdesetupSession(runIdesetupArgs)
        return
      }
    }

    super.setupTermuxSessionOnServiceConnected(
      intent,
      workingDir,
      sessionName,
      existingSession,
      launchFailsafe
    )
  }

  private fun addIdesetupSession(args: Array<String>) {
    val script = IdesetupSession.createScript(this) ?: run {
      log.error("Failed to add idesetup session. Cannot create script.")
      flashError(R.string.msg_cannot_create_terminal_session)
      return
    }

    Log.d("IdeSetupConfig", "buildIdeSetupArguments: ${args.joinToString(separator = " ")}")

    val session = IdesetupSession.wrap(termuxService.createTermuxSession(
      /* executablePath = */ script.absolutePath,
      /* arguments = */ args,
      /* stdin = */ null,
      /* workingDirectory = */ Environment.HOME.absolutePath,
      /* isFailSafe = */ false,
      /* sessionName = */ "IDE setup"
    ), script)

    session ?: run {
      flashError(R.string.msg_cannot_create_terminal_session)
      return
    }

    termuxTerminalSessionClient.setCurrentSession(session.terminalSession)
  }
}