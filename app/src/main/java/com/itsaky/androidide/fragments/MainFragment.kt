package com.itsaky.androidide.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.itsaky.androidide.activities.MainActivity
import com.itsaky.androidide.activities.PreferencesActivity
import com.itsaky.androidide.activities.TerminalActivity
import com.itsaky.androidide.adapters.MainActionsListAdapter
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.common.databinding.LayoutDialogProgressBinding
import com.itsaky.androidide.databinding.FragmentMainBinding
import com.itsaky.androidide.models.MainScreenAction
import com.itsaky.androidide.preferences.databinding.LayoutDialogTextInputBinding
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.tasks.cancelIfActive
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashSuccess
import com.itsaky.androidide.viewmodel.MainViewModel
import java.io.File
import java.util.concurrent.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.ProgressMonitor

class MainFragment : BaseFragment() {

  private val viewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })
  private var binding: FragmentMainBinding? = null

  private val log = ILogger.newInstance("MainFragment")
  private val fragmentScope = CoroutineScope(Dispatchers.Default)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentMainBinding.inflate(inflater, container, false)
    return binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val actions =
      MainScreenAction.all().also { actions ->
        val onClick = { action: MainScreenAction, _: View ->
          when (action.id) {
            MainScreenAction.ACTION_CREATE_PROJECT -> showCreateProject()
            MainScreenAction.ACTION_OPEN_PROJECT -> pickDirectory()
            MainScreenAction.ACTION_CLONE_REPO -> cloneGitRepo()
            MainScreenAction.ACTION_OPEN_TERMINAL ->
              startActivity(Intent(requireActivity(), TerminalActivity::class.java))
            MainScreenAction.ACTION_PREFERENCES -> gotoPreferences()
            MainScreenAction.ACTION_DONATE -> BaseApplication.getBaseInstance().openDonationsPage()
            MainScreenAction.ACTION_DOCS -> BaseApplication.getBaseInstance().openDocs()
          }
        }

        actions.forEach { action -> action.onClick = onClick }
      }

    binding!!.actions.adapter = MainActionsListAdapter(actions)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null

    fragmentScope.cancelIfActive("Fragment has been destroyed")
  }

  private fun pickDirectory() {
    pickDirectory(this::openProject)
  }

  private fun showCreateProject() {
    viewModel.setScreen(MainViewModel.SCREEN_TEMPLATE_LIST)
  }

  fun openProject(root: File) {
    (requireActivity() as MainActivity).openProject(root)
  }

  private fun cloneGitRepo() {
    val builder = DialogUtils.newMaterialDialogBuilder(requireContext())
    val binding = LayoutDialogTextInputBinding.inflate(layoutInflater)
    binding.name.setHint(string.git_clone_repo_url)

    builder.setView(binding.root)
    builder.setTitle(string.git_clone_repo)
    builder.setCancelable(true)
    builder.setPositiveButton(string.git_clone) { dialog, _ ->
      dialog.dismiss()
      val url = binding.name.editText?.text?.toString()
      doClone(url)
    }
    builder.setNegativeButton(android.R.string.cancel, null)
    builder.show()
  }

  private fun doClone(repo: String?) {
    if (repo.isNullOrBlank()) {
      log.warn("Unable to clone repo. Invalid repo URL : '$repo'")
      return
    }

    var url = repo.trim()
    if (!url.endsWith(".git")) {
      url += ".git"
    }

    val builder = DialogUtils.newMaterialDialogBuilder(requireContext())
    val binding = LayoutDialogProgressBinding.inflate(layoutInflater)

    binding.message.visibility = View.VISIBLE

    builder.setTitle(string.git_clone_in_progress)
    builder.setMessage(url)
    builder.setView(binding.root)
    builder.setCancelable(false)

    val repoName = url.substringAfterLast('/').substringBeforeLast(".git")
    val targetDir = File(Environment.PROJECTS_DIR, repoName)

    val progress = GitCloneProgressMonitor(binding.progress, binding.message)

    var cloneJob: Job? = null
    var git: Git? = null

    builder.setPositiveButton(android.R.string.cancel) { dialogInterface, _ ->
      dialogInterface.dismiss()
      progress.cancel()
      git?.close()
      cloneJob?.cancel(CancellationException("Cloning canceled"))
    }

    val dialog = builder.show()

    cloneJob =
      fragmentScope
        .launch {
          try {
            git =
              Git.cloneRepository()
                .setURI(url)
                .setDirectory(targetDir)
                .setProgressMonitor(progress)
                .call()
          } catch (error: Throwable) {
            showCloneError(error)
          }
        }
        .also {
          it.invokeOnCompletion { error ->
            runOnUiThread {
              dialog?.takeIf { it.isShowing }?.dismiss()
              git?.close()
              if (git == null || error != null) {
                if (!cloneJob.isCancelled) {
                  flashError(string.git_clone_failed)
                }
              } else flashSuccess(string.git_clone_success)
            }
          }
        }
  }

  private suspend fun showCloneError(error: Throwable) {
    withContext(Dispatchers.Main) {
      DialogUtils.newMaterialDialogBuilder(requireContext())
        .setTitle(string.git_clone_failed)
        .setMessage(error.localizedMessage)
        .setPositiveButton(android.R.string.ok, null)
        .show()
    }
  }

  private fun gotoPreferences() {
    startActivity(Intent(requireActivity(), PreferencesActivity::class.java))
  }

  // TODO(itsaky) : Improve this implementation
  class GitCloneProgressMonitor(val progress: LinearProgressIndicator, val message: TextView) :
    ProgressMonitor {

    private var cancelled = false

    fun cancel() {
      cancelled = true
    }

    override fun start(totalTasks: Int) {
      runOnUiThread { progress.max = totalTasks }
    }

    override fun beginTask(title: String?, totalWork: Int) {
      runOnUiThread { message.text = title }
    }

    override fun update(completed: Int) {
      runOnUiThread { progress.progress = completed }
    }

    override fun showDuration(enabled: Boolean) {
      // no-op
    }

    override fun endTask() {}

    override fun isCancelled(): Boolean {
      return cancelled || Thread.currentThread().isInterrupted
    }
  }
}
