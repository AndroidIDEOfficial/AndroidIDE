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

package com.itsaky.androidide.utils

import android.content.pm.PackageInstaller

/**
 * @author Akash Yadav
 */
open class SingleSessionCallback : PackageInstaller.SessionCallback() {
  override fun onCreated(sessionId: Int) {}
  override fun onBadgingChanged(sessionId: Int) {}
  override fun onActiveChanged(sessionId: Int, active: Boolean) {}
  override fun onProgressChanged(sessionId: Int, progress: Float) {}
  override fun onFinished(sessionId: Int, success: Boolean) {}
}