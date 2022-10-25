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

import android.app.Activity
import android.content.Intent
import android.os.Environment
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.provider.DocumentsContractCompat
import com.itsaky.androidide.app.BaseApplication.getBaseInstance
import com.itsaky.androidide.R.string // resources.R.string
import com.itsaky.toaster.toastError
import com.itsaky.androidide.utils.Environment.ANDROIDIDE_UI
import java.io.File

val chooseFont =
  SimpleClickablePreference(
    key = "idepref_choose_customFont",
    title = string.idepref_chooseFont_title,
    summary = string.idepref_chooseFont_summary
  ) {
    val context = it.context
    if (context is ActivityResultCaller) {
      context.startForResult.launch(Intent(Intent.ACTION_OPEN_DOCUMENT))
    }
    true
  }

private val allowedAuthorities =
  setOf(
    "com.android.externalstorage.documents",
    "com.itsaky.androidide.documents",
    "com.termux.documents",
  )

private val ActivityResultCaller.startForResult : ActivityResultLauncher<Intent>
  get() = registerForActivityResult(StartActivityForResult()) {
    if (it.resultCode != Activity.RESULT_OK) return@registerForActivityResult
    val uri = it?.data?.data ?: return@registerForActivityResult
    val docId = DocumentsContractCompat.getDocumentId(uri)!!
    val authority = uri.authority

    if (!allowedAuthorities.contains(authority)) {
      toastError(getBaseInstance().getString(string.err_authority_not_allowed, authority))
      return@registerForActivityResult
    }

    val choosenFile =
      if (authority != allowedAuthorities.elementAt(0)) {
        File(docId)
      } else {
        val split = docId.split(':')
        if ("primary" != split[0]) {
          toastError(string.msg_select_from_primary_storage)
          return@registerForActivityResult
        }

        File(Environment.getExternalStorageDirectory(), split[1])
      }
    val fontFile = File(ANDROIDIDE_UI, "font.ttf")

    try {
      choosenFile.copyTo(fontFile, overwrite=true)
    } catch (e: Exception) {
      toastError(e.toString())
    }
  }
