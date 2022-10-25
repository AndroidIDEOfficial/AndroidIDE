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

package com.itsaky.androidide.views

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.ViewFlipper
import androidx.annotation.GravityInt
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.adapters.DiagnosticsAdapter
import com.itsaky.androidide.adapters.EditorBottomSheetTabAdapter
import com.itsaky.androidide.adapters.SearchListAdapter
import com.itsaky.androidide.databinding.LayoutBottomActionBinding
import com.itsaky.androidide.databinding.LayoutEditorBottomSheetBinding
import com.itsaky.androidide.fragments.ShareableOutputFragment
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.tasks.TaskExecutor.CallbackWithError
import com.itsaky.androidide.tasks.TaskExecutor.executeAsync
import com.itsaky.androidide.tasks.TaskExecutor.executeAsyncProvideError
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.IntentUtils.shareFile
import com.itsaky.androidide.utils.Symbols.forFile
import com.itsaky.androidide.views.editor.CodeEditorView
import com.itsaky.toaster.Toaster.Type.ERROR
import com.itsaky.toaster.toast
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption.CREATE_NEW
import java.nio.file.StandardOpenOption.WRITE
import java.util.concurrent.Callable

/**
 * Bottom sheet shown in editor activity.
 * @author Akash Yadav
 */
class EditorBottomSheet
@JvmOverloads
constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

  private var binding: LayoutEditorBottomSheetBinding
  private var action: LayoutBottomActionBinding
  private var symbolInput: SymbolInputView

  val pagerAdapter: EditorBottomSheetTabAdapter

  val tabs: TabLayout
    get() = binding.tabs

  val pager: ViewPager2
    get() = binding.pager

  val headerContainer: ViewFlipper
    get() = binding.headerContainer

  private val log = ILogger.newInstance("EditorBottomSheet")

  companion object {
    const val CHILD_SYMBOL_INPUT = 0
    const val CHILD_HEADER = 1
    const val CHILD_ACTION = 2
  }

  private fun initialize(context: FragmentActivity) {
    val mediator =
      TabLayoutMediator(binding.tabs, binding.pager, true, true) { tab, position ->
        tab.text = pagerAdapter.getTitle(position)
      }

    mediator.attach()
    binding.pager.isUserInputEnabled = false
    binding.pager.offscreenPageLimit = pagerAdapter.itemCount - 1 // DO not remove any views

    binding.tabs.addOnTabSelectedListener(
      object : OnTabSelectedListener {
        override fun onTabSelected(tab: Tab) {
          val fragment: Fragment = pagerAdapter.getFragmentAtIndex(tab.position)
          if (fragment is ShareableOutputFragment) {
            binding.clearFab.show()
            binding.shareOutputFab.show()
          } else {
            binding.clearFab.hide()
            binding.shareOutputFab.hide()
          }
        }

        override fun onTabUnselected(tab: Tab) {}
        override fun onTabReselected(tab: Tab) {}
      }
    )

    binding.shareOutputFab.setOnClickListener {
      val fragment = pagerAdapter.getFragmentAtIndex(binding.tabs.selectedTabPosition)

      if (fragment !is ShareableOutputFragment) {
        log.error("Unknown fragment:", fragment)
        return@setOnClickListener
      }

      val filename = fragment.getFilename()
      @Suppress("DEPRECATION")
      val progress = ProgressDialog.show(context, null, context.getString(string.please_wait))
      executeAsync(fragment::getContent) {
        progress.dismiss()
        shareText(it, filename)
      }
    }

    TooltipCompat.setTooltipText(binding.clearFab, context.getString(string.title_clear_output))
    binding.clearFab.setOnClickListener {
      val fragment: Fragment = pagerAdapter.getFragmentAtIndex(binding.tabs.selectedTabPosition)
      if (fragment !is ShareableOutputFragment) {
        log.error("Unknown fragment:", fragment)
        return@setOnClickListener
      }
      (fragment as ShareableOutputFragment).clearOutput()
    }
  }

  init {
    if (context !is FragmentActivity) {
      throw IllegalArgumentException("EditorBottomSheet must be set up with a FragmentActivity")
    }

    val inflater = LayoutInflater.from(context)
    binding = LayoutEditorBottomSheetBinding.inflate(inflater)
    action = LayoutBottomActionBinding.inflate(inflater)
    symbolInput = SymbolInputView(context)
    pagerAdapter = EditorBottomSheetTabAdapter(context)

    binding.headerContainer.addView(symbolInput, CHILD_SYMBOL_INPUT, LayoutParams(-1, -2))
    binding.headerContainer.addView(action.root, CHILD_ACTION, LayoutParams(-1, -2))
    binding.pager.adapter = pagerAdapter

    removeAllViews()
    addView(binding.root)

    initialize(context)
  }

  fun showChild(index: Int) {
    binding.headerContainer.displayedChild = index
  }

  fun setActionText(text: CharSequence) {
    action.actionText.text = text
  }

  fun setActionProgress(progress: Int) {
    action.progress.setProgressCompat(progress, true)
  }

  fun appendApkLog(line: LogLine) {
    pagerAdapter.logFragment?.appendLog(line)
  }

  fun appendBuildOut(str: String?) {
    pagerAdapter.buildOutputFragment?.appendOutput(str)
  }

  fun handleDiagnosticsResultVisibility(errorVisible: Boolean) {
    runOnUiThread { pagerAdapter.diagnosticsFragment?.handleResultVisibility(errorVisible) }
  }

  fun handleSearchResultVisibility(errorVisible: Boolean) {
    runOnUiThread { pagerAdapter.searchResultFragment?.handleResultVisibility(errorVisible) }
  }

  fun setDiagnosticsAdapter(adapter: DiagnosticsAdapter) {
    runOnUiThread { pagerAdapter.diagnosticsFragment?.setAdapter(adapter) }
  }

  fun setSearchResultAdapter(adapter: SearchListAdapter) {
    runOnUiThread { pagerAdapter.searchResultFragment?.setAdapter(adapter) }
  }

  fun refreshSymbolInput(editor: CodeEditorView) {
    symbolInput.bindEditor(editor.editor)
    symbolInput.setSymbols(*forFile(editor.file))
  }

  fun onSoftInputChanged() {
    if (context !is Activity) {
      return
    }

    val activity = context as Activity
    if (KeyboardUtils.isSoftInputVisible(activity)) {
      TransitionManager.beginDelayedTransition(binding.root, Slide(Gravity.TOP))
      binding.headerContainer.displayedChild = CHILD_SYMBOL_INPUT
    } else {
      TransitionManager.beginDelayedTransition(binding.root, Slide(Gravity.BOTTOM))
      binding.headerContainer.displayedChild = CHILD_HEADER
    }
  }

  fun setStatus(text: CharSequence, @GravityInt gravity: Int) {
    runOnUiThread {
      binding.let {
        it.statusText.gravity = gravity
        it.statusText.text = text
      }
    }
  }

  private fun shareFile(file: File) {
    shareFile(context, file, "text/plain")
  }

  @Suppress("DEPRECATION")
  private fun shareText(text: String?, type: String) {
    if (text == null || TextUtils.isEmpty(text)) {
      toast(context.getString(string.msg_output_text_extraction_failed), ERROR)
      return
    }
    val pd = ProgressDialog.show(context, null, context.getString(string.please_wait), true, false)
    executeAsyncProvideError(
      Callable { writeTempFile(text, type) },
      CallbackWithError<File> { result: File?, error: Throwable? ->
        pd.dismiss()
        if (result == null || error != null) {
          log.warn("Unable to share output", error)
          return@CallbackWithError
        }
        shareFile(result)
      }
    )
  }

  private fun writeTempFile(text: String, type: String): File {
    // use a common name to avoid multiple files
    val file: Path = context.filesDir.toPath().resolve("$type.txt")
    try {
      if (Files.exists(file)) {
        Files.delete(file)
      }
      Files.write(file, text.toByteArray(StandardCharsets.UTF_8), CREATE_NEW, WRITE)
    } catch (e: IOException) {
      log.error("Unable to write output to file", e)
    }
    return file.toFile()
  }
}
