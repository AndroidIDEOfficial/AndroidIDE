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

fun toast(msgResId: Int, type: Type) {
  toaster.setDuration(Toaster.SHORT).setText(msgResId).setType(type).show()
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

fun toastSuccess(msgResId: Int) {
  toast(msgResId, SUCCESS)
}

fun toastError(msgResId: Int) {
  toast(msgResId, ERROR)
}

fun toastInfo(msgResId: Int) {
  toast(msgResId, INFO)
}

fun toastLong(msg: String?, type: Type) {
  toaster.setDuration(Toaster.LONG).setText(msg).setType(type).show()
}

fun toastLong(msgResId: Int, type: Type) {
  toaster.setDuration(Toaster.LONG).setText(msgResId).setType(type).show()
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

fun toastLongSuccess(msgResId: Int) {
  toastLong(msgResId, SUCCESS)
}

fun toastLongError(msgResId: Int) {
  toastLong(msgResId, ERROR)
}

fun toastLongInfo(msgResId: Int) {
  toastLong(msgResId, INFO)
}
