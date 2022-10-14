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

package com.itsaky.toaster

import com.itsaky.androidide.app.BaseApplication.getBaseInstance
import com.itsaky.toaster.Toaster.Type
import com.itsaky.toaster.Toaster.Type.ERROR
import com.itsaky.toaster.Toaster.Type.INFO
import com.itsaky.toaster.Toaster.Type.SUCCESS

internal lateinit var toaster: Toaster

internal fun init() {
  toaster = Toaster(getBaseInstance())
}

fun toast(msg: String?, type: Type) {
  toaster.setDuration(Toaster.SHORT).setText(msg).setType(type).show()
}

fun toast(msg: Int, type: Type) {
  toaster.setDuration(Toaster.SHORT).setText(msg).setType(type).show()
}

fun toastSuccess(msg: String?) {
  toast(msg, SUCCESS)
}

fun toastError(msg: String?) {
  toast(msg, ERROR)
}

fun toastInfo(msg: String?) {
  toast(msg, INFO)
}

fun toastSuccess(msg: Int) {
  toast(msg, SUCCESS)
}

fun toastError(msg: Int) {
  toast(msg, ERROR)
}

fun toastInfo(msg: Int) {
  toast(msg, INFO)
}

fun toastLong(msg: String?, type: Type) {
  toaster.setDuration(Toaster.LONG).setText(msg).setType(type).show()
}

fun toastLong(msg: Int, type: Type) {
  toaster.setDuration(Toaster.LONG).setText(msg).setType(type).show()
}

fun toastLongSuccess(msg: String?) {
  toastLong(msg, SUCCESS)
}

fun toastLongError(msg: String?) {
  toastLong(msg, ERROR)
}

fun toastLongInfo(msg: String?) {
  toastLong(msg, INFO)
}

fun toastLongSuccess(msg: Int) {
  toastLong(msg, SUCCESS)
}

fun toastLongError(msg: Int) {
  toastLong(msg, ERROR)
}

fun toastLongInfo(msg: Int) {
  toastLong(msg, INFO)
}
