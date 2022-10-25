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

package com.itsaky.androidide.preferences

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.reflect.Method

/**
 * Helper for creating dialogs.
 *
 * @author Akash Yadav
 */
var newDialogMethod: Method? = null
var newYNDialogMethod: Method? = null
var newYNDialogWithTitleAndMessageMethod: Method? = null

/** Creates an instance of the [MaterialAlertDialogBuilder] */
fun newMaterialDialogBuilder(context: Context): MaterialAlertDialogBuilder {
  initMethodIfNeeded()
  try {
    return newDialogMethod!!.invoke(null, context) as MaterialAlertDialogBuilder
  } catch (error: Throwable) {
    throw RuntimeException(error)
  }
}

fun newYesNoDialog(
  context: Context,
  onPositiveClick: DialogInterface.OnClickListener,
  onNegativeClick: DialogInterface.OnClickListener
): MaterialAlertDialogBuilder {
  try {
    return newYNDialogMethod!!.invoke(null, context, onPositiveClick, onNegativeClick)
      as MaterialAlertDialogBuilder
  } catch (error: Throwable) {
    throw RuntimeException(error)
  }
}

fun newYesNoDialog(
  context: Context,
  title: String,
  message: String,
  onPositiveClick: DialogInterface.OnClickListener,
  onNegativeClick: DialogInterface.OnClickListener
): MaterialAlertDialogBuilder {
  try {
    return newYNDialogWithTitleAndMessageMethod!!.invoke(
      null,
      context,
      title,
      message,
      onPositiveClick,
      onNegativeClick
    ) as MaterialAlertDialogBuilder
  } catch (error: Throwable) {
    throw RuntimeException(error)
  }
}

private fun initMethodIfNeeded() {
  if (
    newDialogMethod == null ||
      newYNDialogMethod == null ||
      newYNDialogWithTitleAndMessageMethod == null
  ) {
    initMethods()
  }
  if (
    newDialogMethod == null ||
      newYNDialogMethod == null ||
      newYNDialogWithTitleAndMessageMethod == null
  ) {
    throw RuntimeException("Unable to instantiate methods")
  }
}

fun initMethods() {
  try {
    val klass = Class.forName("com.itsaky.androidide.utils.DialogUtils")
    newDialogMethod = klass.getDeclaredMethod("newMaterialDialogBuilder", Context::class.java)
    newYNDialogMethod =
      klass.getDeclaredMethod(
        "newYesNoDialog",
        Context::class.java,
        DialogInterface.OnClickListener::class.java,
        DialogInterface.OnClickListener::class.java
      )
    newYNDialogWithTitleAndMessageMethod =
      klass.getDeclaredMethod(
        "newYesNoDialog",
        Context::class.java,
        String::class.java,
        String::class.java,
        DialogInterface.OnClickListener::class.java,
        DialogInterface.OnClickListener::class.java
      )
  } catch (error: Throwable) {
    throw RuntimeException(error)
  }
}
