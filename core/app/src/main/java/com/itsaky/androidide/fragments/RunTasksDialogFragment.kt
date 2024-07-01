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

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.WindowInsetsCompat.Type.navigationBars
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.transition.TransitionManager
import com.blankj.utilcode.util.ThreadUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.transition.MaterialSharedAxis
import com.itsaky.androidide.R.string
import com.itsaky.androidide.adapters.RunTasksListAdapter
import com.itsaky.androidide.databinding.LayoutRunTaskBinding
import com.itsaky.androidide.databinding.LayoutRunTaskDialogBinding
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.models.Checkable
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.projects.GradleProject
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.tooling.api.models.GradleTask
import com.itsaky.androidide.utils.SingleTextWatcher
import com.itsaky.androidide.utils.doOnApplyWindowInsets
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashInfo
import com.itsaky.androidide.viewmodel.RunTasksViewModel
import org.slf4j.LoggerFactory

/**
 * A bottom sheet dialog fragment to show UI which allows the users to select and execute Gradle
 * tasks from the initialized project.
 *
 * @author Akash Yadav
 */
class RunTasksDialogFragment : BottomSheetDialogFragment() {

  private lateinit var binding: LayoutRunTaskDialogBinding
  private lateinit var run: LayoutRunTaskBinding
  private val viewModel: RunTasksViewModel by viewModels()

  companion object {
    private val log = LoggerFactory.getLogger(RunTasksDialogFragment::class.java)

    private const val CHILD_LOADING = 0
    private const val CHILD_TASKS = 1
    private const val CHILD_CONFIRMATION = 2
    private const val CHILD_PROJECT_NOT_INITIALIZED = 3

    // The minimum amount of time (in milliseconds) the adapter should wait after the query is
    // changed before starting any further filter request.
    // A too less value here will result in UI lags
    private const val SEARCH_DELAY = 500L
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = object : BottomSheetDialog(requireContext(), theme) {
      override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findViewById<View>(com.google.android.material.R.id.container)?.apply {
          doOnApplyWindowInsets { view, insets, _, margins ->
            insets.getInsets(statusBars() or navigationBars()).apply {
              view.updateLayoutParams<MarginLayoutParams> { updateMargins(top = margins.top + top) }
              run.tasks.apply {
                updatePadding(bottom = bottom)
                clipToPadding = false
                clipChildren = false
              }
            }
          }
        }
      }
    }
    dialog.behavior.peekHeight = (getWindowHeight() * 0.7).toInt()
    return dialog
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    this.binding = LayoutRunTaskDialogBinding.inflate(inflater, container, false)
    this.run = this.binding.run
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.observeDisplayedChild(viewLifecycleOwner) {
      val transition =
        MaterialSharedAxis(MaterialSharedAxis.X, it > this.binding.flipper.displayedChild)
      TransitionManager.beginDelayedTransition(this.binding.root, transition)
      this.binding.flipper.displayedChild = it
    }

    viewModel.observeQuery(viewLifecycleOwner) {
      val adapter = run.tasks.adapter as? RunTasksListAdapter? ?: return@observeQuery
      adapter.filter(it)
    }

    run.searchInput.editText?.addTextChangedListener(
      object : SingleTextWatcher() {
        val searchRunner = Runnable {
          viewModel.query = run.searchInput.editText?.text?.toString() ?: ""
        }
        override fun afterTextChanged(s: Editable?) {
          ThreadUtils.getMainHandler().removeCallbacks(searchRunner)
          ThreadUtils.runOnUiThreadDelayed(searchRunner, SEARCH_DELAY)
        }
      }
    )

    binding.exec.setOnClickListener {
      if (viewModel.selected.isEmpty()) {
        requireActivity().flashInfo(getString(string.msg_err_select_tasks))
        return@setOnClickListener
      }

      if (viewModel.displayedChild == CHILD_TASKS) {
        binding.confirm.msg.text =
          getString(R.string.msg_tasks_to_run, viewModel.getSelectedTaskPaths())
        viewModel.displayedChild = CHILD_CONFIRMATION
        return@setOnClickListener
      }

      if (viewModel.displayedChild == CHILD_CONFIRMATION) {
        val buildService =
          Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE)
            ?: run {
              log.error("Cannot find build service")
              return@setOnClickListener
            }

        if (!buildService.isToolingServerStarted()) {
          flashError(R.string.msg_tooling_server_unavailable)
          return@setOnClickListener
        }

        val toRun = viewModel.selected.toTypedArray()
        buildService.executeTasks(*toRun)
        dismiss()
      }
    }

    binding.confirm.cancel.setOnClickListener { viewModel.displayedChild = CHILD_TASKS }

    viewModel.displayedChild = CHILD_LOADING

    executeAsync({
      val workspace = IProjectManager.getInstance().getWorkspace()
        ?: return@executeAsync emptyList<Checkable<GradleTask>>()

      return@executeAsync workspace.getSubProjects()
        .flatMap<GradleProject, GradleTask> { it.tasks }
        .map<GradleTask, Checkable<GradleTask>> {
          Checkable<GradleTask>(false, it)
        }
    }) { tasks ->
      viewModel.tasks = tasks ?: emptyList()
      viewModel.displayedChild =
        if (viewModel.tasks.isNotEmpty()) CHILD_TASKS else CHILD_PROJECT_NOT_INITIALIZED

      val onCheckChanged = { item: Checkable<GradleTask> ->
        if (item.isChecked) {
          viewModel.select(item.data.path)
        } else {
          viewModel.deselect(item.data.path)
        }
      }

      run.tasks.adapter = RunTasksListAdapter(viewModel.tasks, onCheckChanged)
    }
  }

  private fun getWindowHeight(): Int {
    val height = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      activity?.windowManager?.currentWindowMetrics?.bounds?.height()!!
    } else {
      val displayMetrics = DisplayMetrics()
      activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
      displayMetrics.heightPixels
    }
    return height
  }
}
