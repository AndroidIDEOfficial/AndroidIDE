package com.itsaky.androidide.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.ThreadUtils
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
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.tasks.executeAsyncProvideError
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashSuccess
import com.itsaky.androidide.viewmodel.MainViewModel
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.ProgressMonitor
import java.io.File

class MainFragment : BaseFragment() {

  private val viewModel by viewModels<MainViewModel>(
    ownerProducer = { requireActivity() })
  private var binding: FragmentMainBinding? = null

  private val log = ILogger.newInstance("MainFragment")

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?
  ): View {
    binding = FragmentMainBinding.inflate(inflater, container, false)
    return binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val createProject = MainScreenAction(string.create_project,
      R.drawable.ic_add) { showCreateProject() }
    val openProject = MainScreenAction(string.msg_open_existing_project,
      R.drawable.ic_folder) { pickDirectory() }
    val cloneGitRepository = MainScreenAction(string.git_clone_repo,
      R.drawable.ic_git) { cloneGitRepo() }
    val openTerminal =
      MainScreenAction(string.title_terminal, R.drawable.ic_terminal) {
        startActivity(Intent(requireActivity(), TerminalActivity::class.java))
      }
    val preferences = MainScreenAction(string.msg_preferences,
      R.drawable.ic_settings) { gotoPreferences() }
    val sponsor = MainScreenAction(string.btn_donate, R.drawable.ic_donate) {
      BaseApplication.getBaseInstance().openSponsors()
    }
    val docs = MainScreenAction(string.btn_docs, R.drawable.ic_docs) {
      BaseApplication.getBaseInstance().openDocs()
    }

    binding!!.actions.adapter = MainActionsListAdapter(
      listOf(createProject, openProject, cloneGitRepository, openTerminal,
        preferences, docs, sponsor))
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
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
    var git: Git? = null
    val future = executeAsyncProvideError({
      return@executeAsyncProvideError Git.cloneRepository()
        .setURI(url)
        .setDirectory(targetDir)
        .setProgressMonitor(progress)
        .call()
        .also { git = it }
    }, { _, _ -> })

    builder.setPositiveButton(android.R.string.cancel) { iface, _ ->
      iface.dismiss()
      progress.cancel()
      git?.close()
      future.cancel(true)
    }

    val dialog = builder.show()

    future.whenComplete { result, error ->
      ThreadUtils.runOnUiThread {
        dialog?.dismiss()
        result?.close()
        if (result == null || error != null) {
          if (!future.isCancelled) {
            showCloneError(error)
          }
        } else flashSuccess(string.git_clone_success)
      }
    }
  }

  private fun showCloneError(error: Throwable?) {
    if (error == null) {
      flashError(string.git_clone_failed)
      return
    }

    val builder = DialogUtils.newMaterialDialogBuilder(requireContext())
    builder.setTitle(string.git_clone_failed)
    builder.setMessage(error.localizedMessage)
    builder.setPositiveButton(android.R.string.ok, null)
    builder.show()
  }

  private fun gotoPreferences() {
    startActivity(Intent(requireActivity(), PreferencesActivity::class.java))
  }

  // TODO(itsaky) : Improve this implementation
  class GitCloneProgressMonitor(val progress: LinearProgressIndicator,
                                val message: TextView
  ) : ProgressMonitor {

    private var cancelled = false

    fun cancel() {
      cancelled = true
    }

    override fun start(totalTasks: Int) {
      ThreadUtils.runOnUiThread { progress.max = totalTasks }
    }

    override fun beginTask(title: String?, totalWork: Int) {
      ThreadUtils.runOnUiThread { message.text = title }
    }

    override fun update(completed: Int) {
      ThreadUtils.runOnUiThread { progress.progress = completed }
    }

    override fun endTask() {}

    override fun isCancelled(): Boolean {
      return cancelled || Thread.currentThread().isInterrupted
    }
  }
}
