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

package com.itsaky.androidide.uidesigner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import com.itsaky.androidide.fragments.BaseFragment
import com.itsaky.androidide.inflater.IInflateEventsListener
import com.itsaky.androidide.inflater.ILayoutInflater
import com.itsaky.androidide.inflater.OnInflateViewEvent
import com.itsaky.androidide.uidesigner.UIDesignerActivity
import com.itsaky.androidide.uidesigner.databinding.FragmentDesignerWorkspaceBinding
import com.itsaky.androidide.uidesigner.databinding.LayoutDesignerErrorBinding
import com.itsaky.androidide.uidesigner.utils.InflatedViewTouchListener
import com.itsaky.androidide.uidesigner.utils.bgDesignerView
import com.itsaky.androidide.uidesigner.utils.layeredForeground
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel
import com.itsaky.androidide.utils.ILogger
import java.io.File

/**
 * The fragement that previews the inflated layout.
 *
 * @author Akash Yadav
 */
class DesignerWorkspaceFragment : BaseFragment() {
  private val log = ILogger.newInstance("DesignerWorkspaceFragment")
  private var binding: FragmentDesignerWorkspaceBinding? = null
  internal val viewModel by viewModels<WorkspaceViewModel>()

  private val inflateListener by lazy {
    IInflateEventsListener { event ->
      if (event is OnInflateViewEvent) {
        event.data.view.setOnTouchListener(InflatedViewTouchListener())
        val fg = event.data.view.foreground
        event.data.view.foreground =
          if (fg != null) layeredForeground(requireContext(), fg)
          else bgDesignerView(event.data.view.context)
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    this.binding = FragmentDesignerWorkspaceBinding.inflate(inflater, container, false)
    return this.binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val inflater = ILayoutInflater.newInflater()
    inflater.inflationEventListener = this.inflateListener
    val inflated =
      try {
        inflater.inflate(viewModel.file, binding!!.workspace)
      } catch (e: Throwable) {
        log.error(e)
        binding!!.workspace.removeAllViews()
        binding!!
          .workspace
          .addView(createErrorView("${e.message}${e.cause?.message?.let { "\n$it" } ?: ""}"))
      }
    if (inflated == null) {
      binding!!.workspace.addView(createErrorView("Failed to infflate view"))
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    this.binding = null
  }

  private fun createErrorView(@StringRes message: Int): View {
    return createErrorView(getString(message))
  }

  private fun createErrorView(message: String): View {
    return LayoutDesignerErrorBinding.inflate(layoutInflater).let {
      it.root.text = message
      it.root
    }
  }

  fun setupFromBundle(bundle: Bundle?) {
    bundle?.let {
      val path = it.getString(UIDesignerActivity.EXTRA_FILE) ?: return
      val file = File(path)
      if (!file.exists()) {
        throw IllegalArgumentException("File does not exist: $file")
      }
      viewModel.file = file
    }
  }
}
