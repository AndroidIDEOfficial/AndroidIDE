package com.itsaky.androidide.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itsaky.androidide.EditorActivity
import com.itsaky.androidide.PreferencesActivity
import com.itsaky.androidide.TerminalActivity
import com.itsaky.androidide.adapters.MainActionsListAdapter
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.databinding.FragmentMainBinding
import com.itsaky.androidide.fragments.WizardFragment.OnProjectCreatedListener
import com.itsaky.androidide.models.MainScreenAction
import com.itsaky.androidide.projects.ProjectManager.projectPath
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.resources.R.string
import java.io.File
import com.itsaky.androidide.tasks.git.CloneGitTask

class MainFragment : BaseFragment(), OnProjectCreatedListener {
  private var binding: FragmentMainBinding? = null

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
    val createProject =
      MainScreenAction(string.create_project, R.drawable.ic_add) { showCreateProject() }
    val openProject =
      MainScreenAction(string.msg_open_existing_project, R.drawable.ic_folder) { pickDirectory() }
    val cloneGitRepository =
      MainScreenAction(string.clone_git_repository, R.drawable.ic_git) { cloneGitRepo() }
    val openTerminal =
      MainScreenAction(string.btn_terminal, R.drawable.ic_terminal) {
        startActivity(Intent(requireActivity(), TerminalActivity::class.java))
      }
    val preferences =
      MainScreenAction(R.string.msg_preferences, R.drawable.ic_settings) { gotoPreferences() }
    val sponsor =
      MainScreenAction(string.btn_sponsor, R.drawable.ic_sponsor) {
        BaseApplication.getBaseInstance().openSponsors()
      }
    val docs =
      MainScreenAction(string.btn_docs, R.drawable.ic_docs) {
        BaseApplication.getBaseInstance().openDocs()
      }

    binding!!.actions.adapter =
      MainActionsListAdapter(
        listOf(createProject, openProject, cloneGitRepository, openTerminal, preferences, docs, sponsor)
      )
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }

  private fun pickDirectory() {
    pickDirectory(this::openProject)
  }

  private fun showCreateProject() {
    val wizardFragment = WizardFragment()
    wizardFragment.setOnProjectCreatedListener(this)
    parentFragmentManager
      .beginTransaction()
      .add(com.itsaky.androidide.R.id.container, wizardFragment, WizardFragment.TAG)
      .addToBackStack(null)
      .commit()
  }

  override fun openProject(root: File) {
    projectPath = root.absolutePath
    startActivity(Intent(requireActivity(), EditorActivity::class.java))
  }

  private fun cloneGitRepo() {
    val cloneGitTask = CloneGitTask(requireActivity())
    cloneGitTask.cloneRepo()
  }

  private fun gotoPreferences() {
    startActivity(Intent(requireActivity(), PreferencesActivity::class.java))
  }

  companion object {
    const val TAG = "MainFragmentTag"
  }
}
