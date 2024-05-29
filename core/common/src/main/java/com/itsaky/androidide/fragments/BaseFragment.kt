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

package com.itsaky.androidide.fragments

import android.content.Intent
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.provider.DocumentsContractCompat
import androidx.core.provider.DocumentsContractCompat.buildDocumentUriUsingTree
import androidx.core.provider.DocumentsContractCompat.getTreeDocumentId
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.itsaky.androidide.common.R
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.utils.flashError
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import java.io.File

open class BaseFragment @JvmOverloads constructor(contentLayoutId: Int = 0) :
  Fragment(contentLayoutId) {

  private var callback: OnDirectoryPickedCallback? = null
  private val allowedAuthorities =
    setOf(ANDROID_DOCS_AUTHORITY, ANDROIDIDE_DOCS_AUTHORITY)

  protected val viewLifecycleScope = CoroutineScope(Dispatchers.Default + CoroutineName(javaClass.simpleName))

  companion object {
    const val ANDROID_DOCS_AUTHORITY = "com.android.externalstorage.documents"
    const val ANDROIDIDE_DOCS_AUTHORITY = "com.itsaky.androidide.documents"
  }

  override fun onDestroyView() {
    super.onDestroyView()
    viewLifecycleScope.cancel("${javaClass.simpleName} is being destroyed")
  }

  private val startForResult =
    registerForActivityResult(StartActivityForResult()) {
      val context = requireContext()
      val uri = it?.data?.data ?: return@registerForActivityResult
      val pickedDir = DocumentFile.fromTreeUri(context, uri)

      if (pickedDir == null) {
        flashError(string.err_invalid_data_by_intent)
        return@registerForActivityResult
      }

      if (!pickedDir.exists()) {
        flashError(getString(string.msg_picked_isnt_dir))
        return@registerForActivityResult
      }

      val docUri = buildDocumentUriUsingTree(uri, getTreeDocumentId(uri)!!)!!
      val docId = DocumentsContractCompat.getDocumentId(docUri)!!
      val authority = docUri.authority

      if (!allowedAuthorities.contains(authority)) {
        flashError(getString(string.err_authority_not_allowed, authority))
        return@registerForActivityResult
      }

      val dir =
        if (authority == ANDROIDIDE_DOCS_AUTHORITY) {
          File(docId)
        } else {
          val split = docId.split(':')
          if ("primary" != split[0]) {
            flashError(getString(string.msg_select_from_primary_storage))
            return@registerForActivityResult
          }

          File(Environment.getExternalStorageDirectory(), split[1])
        }

      if (!dir.exists() || !dir.isDirectory) {
        flashError(getString(string.err_invalid_data_by_intent))
        return@registerForActivityResult
      }

      if (callback != null) {
        callback!!.onDirectoryPicked(dir)
      }
    }

  protected fun pickDirectory(dirCallback: OnDirectoryPickedCallback?) {
    this.callback = dirCallback
    try {
      this.startForResult.launch(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
    } catch (e: Exception) {
      requireActivity().flashError(getString(R.string.msg_dir_picker_failed, e.message))
    }
  }

  fun interface OnDirectoryPickedCallback {
    fun onDirectoryPicked(file: File)
  }
}
