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

package com.itsaky.androidide.terminal

import com.itsaky.androidide.activities.TerminalActivity
import com.termux.app.terminal.TermuxTerminalSessionActivityClient
import com.termux.terminal.TerminalSession
import com.termux.terminal.TerminalSessionClient

/**
 * [TerminalSessionClient] delegate for AndroidIDE.
 *
 * @author Akash Yadav
 */
class IdeTerminalSessionClient(
  activity: TerminalActivity
) : TermuxTerminalSessionActivityClient(activity) {

  override fun onSessionFinished(finishedSession: TerminalSession) {
    val termuxSession = mActivity?.termuxService?.getTermuxSessionForTerminalSession(
      finishedSession)
    if (termuxSession != null && termuxSession is IdesetupSession) {
      // if the finished session was performing tools installation
      // then set the result code for the installation process
      mActivity.setResult(finishedSession.exitStatus)
    }

    super.onSessionFinished(finishedSession)
  }
}