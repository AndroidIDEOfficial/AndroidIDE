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

package com.itsaky.androidide.ui

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import androidx.annotation.GravityInt
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.transition.TransitionManager
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialSharedAxis
import com.itsaky.androidide.R
import com.itsaky.androidide.adapters.DiagnosticsAdapter
import com.itsaky.androidide.adapters.EditorBottomSheetTabAdapter
import com.itsaky.androidide.adapters.SearchListAdapter
import com.itsaky.androidide.databinding.LayoutBottomActionBinding
import com.itsaky.androidide.databinding.LayoutEditorBottomSheetBinding
import com.itsaky.androidide.fragments.output.ShareableOutputFragment
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.tasks.TaskExecutor.CallbackWithError
import com.itsaky.androidide.tasks.TaskExecutor.executeAsync
import com.itsaky.androidide.tasks.TaskExecutor.executeAsyncProvideError
import com.itsaky.androidide.ui.editor.CodeEditorView
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.IntentUtils.shareFile
import com.itsaky.androidide.utils.Symbols.forFile
import com.itsaky.androidide.utils.flashError
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

  private var action: LayoutBottomActionBinding
  private var symbolInput: SymbolInputView
  private val collapsedHeight: Float by lazy {
    val localContext = getContext() ?: return@lazy 0f
    localContext.resources.getDimension(R.dimen.editor_sheet_collapsed_height)
  }

  @JvmField
  var binding: LayoutEditorBottomSheetBinding

  val pagerAdapter: EditorBottomSheetTabAdapter

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
    binding.pager.offscreenPageLimit = pagerAdapter.itemCount - 1 // Do not remove any views

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
      val progress = android.app.ProgressDialog.show(context, null,
        context.getString(string.please_wait))
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

    binding.headerContainer.setOnClickListener {
      val sheet = BottomSheetBehavior.from(this)
      if (sheet.state != BottomSheetBehavior.STATE_EXPANDED) {
        sheet.state = BottomSheetBehavior.STATE_EXPANDED
      }
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

  fun setOffsetAnchor(view: View) {
    val listener =
      object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
          val sheet = BottomSheetBehavior.from(this@EditorBottomSheet)
          val offset = view.height + SizeUtils.dp2px(1f)
          sheet.isFitToContents = false
          sheet.halfExpandedRatio = 0.3f
          sheet.isGestureInsetBottomIgnored = false
          sheet.peekHeight = binding.headerContainer.height
          sheet.expandedOffset = offset
          view.viewTreeObserver.removeOnGlobalLayoutListener(this)

          binding.root.updatePadding(bottom = offset + SizeUtils.dp2px(16f))
        }
      }
    view.viewTreeObserver.addOnGlobalLayoutListener(listener)
  }

  fun onSlide(offset: Float) {
    if (offset >= 0.5f) {
      updateCollapsedHeight(((0.5f - offset) + 0.5f) * 2f)
    } else updateCollapsedHeight(1f)
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

  fun clearBuildOutput() {
    pagerAdapter.buildOutputFragment?.clearOutput()
  }

  fun handleDiagnosticsResultVisibility(errorVisible: Boolean) {
    runOnUiThread { pagerAdapter.diagnosticsFragment?.isEmpty = errorVisible }
  }

  fun handleSearchResultVisibility(errorVisible: Boolean) {
    runOnUiThread { pagerAdapter.searchResultFragment?.isEmpty = errorVisible }
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
      TransitionManager.beginDelayedTransition(
        binding.root,
        MaterialSharedAxis(MaterialSharedAxis.Y, false)
      )
      binding.headerContainer.displayedChild = CHILD_SYMBOL_INPUT
    } else {
      TransitionManager.beginDelayedTransition(
        binding.root,
        MaterialSharedAxis(MaterialSharedAxis.Y, false)
      )
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

  private fun updateCollapsedHeight(offset: Float) {
    binding.headerContainer.updateLayoutParams<android.view.ViewGroup.LayoutParams> {
      height = (collapsedHeight * offset).toInt()
    }
  }

  private fun shareFile(file: File) {
    shareFile(context, file, "text/plain")
  }

  @Suppress("DEPRECATION")
  private fun shareText(text: String?, type: String) {
    if (text == null || TextUtils.isEmpty(text)) {
      flashError(context.getString(string.msg_output_text_extraction_failed))
      return
    }
    val pd = android.app.ProgressDialog.show(context, null, context.getString(string.please_wait),
      true, false)
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
