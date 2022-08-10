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

import android.content.Context
import android.content.Intent
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ImageUtils.ImageType.TYPE_UNKNOWN
import java.io.File

/**
 * Utilities for sharing files.
 *
 * @author Akash Yadav
 */
object IntentUtils {

  @JvmStatic
  fun openImage(context: Context, file: File) {
    imageIntent(context = context, file = file, intentAction = Intent.ACTION_VIEW)
  }
  
  @JvmStatic
  @JvmOverloads
  fun imageIntent(
    context: Context,
    file: File,
    intentAction: String = Intent.ACTION_SEND
  ) {
    val type = ImageUtils.getImageType(file)
    var typeString = type.value
    if (type == TYPE_UNKNOWN) {
      typeString = "*"
    }
    startIntent(
      context = context,
      file = file,
      mimeType = "image/$typeString",
      intentAction = intentAction
    )
  }

  @JvmStatic
  fun shareFile(context: Context, file: File, mimeType: String) {
    startIntent(context = context, file = file, mimeType = mimeType)
  }

  @JvmStatic
  @JvmOverloads
  fun startIntent(
    context: Context,
    file: File,
    mimeType: String = "*/*",
    intentAction: String = Intent.ACTION_SEND
  ) {
    val uri =
      FileProvider.getUriForFile(context, "${context.packageName}.providers.fileprovider", file)
    val intent =
      ShareCompat.IntentBuilder(context)
        .setType(mimeType)
        .setStream(uri)
        .intent
        .setAction(intentAction)
        .setDataAndType(uri, mimeType)
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    context.startActivity(Intent.createChooser(intent, null))
  }
}
