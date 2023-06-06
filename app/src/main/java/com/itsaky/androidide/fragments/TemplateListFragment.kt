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
import com.itsaky.androidide.viewmodel.MainViewModel
import kotlin.math.ceil

/**
 * A fragment to show the list of available templates.
 *
 * @author Akash Yadav
 */
class TemplateListFragment : FragmentWithBinding<FragmentTemplateListBinding>(
  R.layout.fragment_template_list, FragmentTemplateListBinding::bind
) {

  private var adapter: TemplateListAdapter? = null
  private var layoutManager: FlexboxLayoutManager? = null

  private val viewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Show only project templates
    val templates = ITemplateProvider.getInstance(true)
      .getTemplates()
      .filterIsInstance<ProjectTemplate>()

    layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
    layoutManager!!.justifyContent = JustifyContent.SPACE_EVENLY

    adapter = TemplateListAdapter(templates) { template, _ ->
      viewModel.template.value = template
      viewModel.setScreen(MainViewModel.SCREEN_TEMPLATE_DETAILS)
    }

    binding.list.layoutManager = layoutManager
    binding.list.adapter = adapter

    // This makes sure that the items are evenly distributed in the list
    // and the last row is always aligned to the start
    binding.list.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
      override fun onGlobalLayout() {
        binding.list.viewTreeObserver.removeOnGlobalLayoutListener(this)

        val adapter = this@TemplateListFragment.adapter ?: return
        val layoutManager = this@TemplateListFragment.layoutManager ?: return

        val columns = layoutManager.flexLinesInternal.firstOrNull()?.itemCount ?: 0
        if (columns == 0) {
          return
        }

        val itemCount = adapter.itemCount
        val rows = ceil(itemCount.toFloat() / columns.toFloat()).toInt()
        if (itemCount % columns == 0) {
          return
        }

        val diff = rows * columns - itemCount
        if (diff <= 0) {
          return
        }

        adapter.fillDiff(diff)
      }
    })

    binding.exitButton.setOnClickListener {
      viewModel.setScreen(MainViewModel.SCREEN_MAIN)
    }
  }
}