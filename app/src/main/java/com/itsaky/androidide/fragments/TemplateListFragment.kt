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

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.viewModels
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.itsaky.androidide.R
import com.itsaky.androidide.adapters.TemplateListAdapter
import com.itsaky.androidide.databinding.FragmentTemplateListBinding
import com.itsaky.androidide.templates.ITemplateProvider
import com.itsaky.androidide.templates.ProjectTemplate
import com.itsaky.androidide.utils.FlexboxUtils
import com.itsaky.androidide.viewmodel.MainViewModel
import org.slf4j.LoggerFactory

/**
 * A fragment to show the list of available templates.
 *
 * @author Akash Yadav
 */
class TemplateListFragment :
  FragmentWithBinding<FragmentTemplateListBinding>(R.layout.fragment_template_list,
    FragmentTemplateListBinding::bind) {

  private var adapter: TemplateListAdapter? = null
  private var layoutManager: FlexboxLayoutManager? = null

  private lateinit var globalLayoutListener: OnGlobalLayoutListener

  private val viewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })

  companion object {

    private val log = LoggerFactory.getLogger(TemplateListFragment::class.java)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
    layoutManager!!.justifyContent = JustifyContent.SPACE_EVENLY

    binding.list.layoutManager = layoutManager

    // This makes sure that the items are evenly distributed in the list
    // and the last row is always aligned to the start
    globalLayoutListener = FlexboxUtils.createGlobalLayoutListenerToDistributeFlexboxItemsEvenly(
      { adapter }, { layoutManager }) { adapter, diff ->
      adapter.fillDiff(diff)
    }

    binding.list.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

    binding.exitButton.setOnClickListener {
      viewModel.setScreen(MainViewModel.SCREEN_MAIN)
    }

    viewModel.currentScreen.observe(viewLifecycleOwner) { current ->
      if (current == MainViewModel.SCREEN_TEMPLATE_DETAILS) {
        return@observe
      }

      reloadTemplates()
    }
  }

  override fun onDestroyView() {
    binding.list.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    super.onDestroyView()
  }

  private fun reloadTemplates() {
    _binding ?: return

    log.debug("Reloading templates...")

    // Show only project templates
    // reloading the tempaltes also makes sure that the resources are
    // released from template parameter widgets
    val templates = ITemplateProvider.getInstance(reload = true).getTemplates()
      .filterIsInstance<ProjectTemplate>()

    adapter = TemplateListAdapter(templates) { template, _ ->
      viewModel.template.value = template
      viewModel.setScreen(MainViewModel.SCREEN_TEMPLATE_DETAILS)
    }

    binding.list.adapter = adapter
  }
}
