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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.SizeUtils
import com.itsaky.androidide.fragments.BaseFragment
import com.itsaky.androidide.inflater.IInflateEventsListener
import com.itsaky.androidide.inflater.IInflationEvent
import com.itsaky.androidide.inflater.ILayoutInflater
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.IViewGroup.SingleOnHierarchyChangeListener
import com.itsaky.androidide.inflater.InflationFinishEvent
import com.itsaky.androidide.inflater.OnInflateViewEvent
import com.itsaky.androidide.inflater.internal.LayoutFile
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.inflater.viewGroup
import com.itsaky.androidide.uidesigner.R
import com.itsaky.androidide.uidesigner.UIDesignerActivity
import com.itsaky.androidide.uidesigner.databinding.FragmentDesignerWorkspaceBinding
import com.itsaky.androidide.uidesigner.databinding.LayoutDesignerErrorBinding
import com.itsaky.androidide.uidesigner.drag.WidgetDragListener
import com.itsaky.androidide.uidesigner.models.PlaceholderView
import com.itsaky.androidide.uidesigner.models.UiView
import com.itsaky.androidide.uidesigner.models.UiViewGroup
import com.itsaky.androidide.uidesigner.utils.WidgetTouchListener
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
@SuppressLint("ClickableViewAccessibility")
class DesignerWorkspaceFragment : BaseFragment() {
  private val log = ILogger.newInstance("DesignerWorkspaceFragment")
  private var binding: FragmentDesignerWorkspaceBinding? = null
  internal val viewModel by viewModels<WorkspaceViewModel>(ownerProducer = { requireActivity() })

  private val placeholder by lazy {
    val view = View(requireContext()).apply {
      setBackgroundResource(R.drawable.bg_widget_drag_placeholder)
      layoutParams =
        ViewGroup.LayoutParams(
          SizeUtils.dp2px(PLACEHOLDER_WIDTH_DP),
          SizeUtils.dp2px(PLACEHOLDER_HEIGHT_DP)
        )
    }
    PlaceholderView(view)
  }

  private val workspaceView by lazy {
    UiViewGroup(LayoutFile(File(""), ""), LinearLayout::class.qualifiedName!!, binding!!.workspace)
  }

  companion object {
    const val DRAGGING_WIDGET = "DRAGGING_WIDGET"
    const val DRAGGING_WIDGET_MIME = "androidide/uidesigner_widget"
    private const val PLACEHOLDER_WIDTH_DP = 40f
    private const val PLACEHOLDER_HEIGHT_DP = 20f
  }

  private val inflateListener by lazy {
    object : IInflateEventsListener {
      override fun onEvent(event: IInflationEvent<*>) {
        if (event is OnInflateViewEvent) {
          setupView(event.data)
        }
        if (event is InflationFinishEvent && event.data.isNotEmpty()) {
          val file = (event.data[0] as ViewImpl).file
          workspaceView.file = file
          placeholder.file = file
        }
      }

      override fun onInterceptCreateView(view: IView): IView {
        view as ViewImpl
        if (view is IViewGroup) {
          return UiViewGroup(view.file, view.name, view.viewGroup)
        }
        return UiView(view.file, view.name, view.view)
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
        emptyList()
      }
    if (inflated.isEmpty()) {
      binding!!.workspace.addView(createErrorView("Failed to inflate view"))
    }
    binding!!.workspace.setOnDragListener(WidgetDragListener(workspaceView, this.placeholder))
  }

  override fun onDestroyView() {
    super.onDestroyView()
    this.binding = null
  }

  private fun createErrorView(message: String): View {
    return LayoutDesignerErrorBinding.inflate(layoutInflater).let {
      it.root.text = message
      it.root
    }
  }

  private fun setupView(view: IView) {
    view.view.setOnTouchListener(WidgetTouchListener(view, requireContext()))
    val fg = view.view.foreground
    view.view.foreground =
      if (fg != null) layeredForeground(requireContext(), fg) else bgDesignerView(view.view.context)

    if (view is IViewGroup) {
      setupViewGroup(view as UiViewGroup)
    }
  }

  private fun setupViewGroup(viewGroup: UiViewGroup) {
    viewGroup.view.setOnDragListener(WidgetDragListener(viewGroup, placeholder))
    viewGroup.addOnHierarchyChangeListener(
      object : SingleOnHierarchyChangeListener() {
        override fun onViewAdded(group: IViewGroup, view: IView) {
          setupView(view as UiView)
        }
      }
    )
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
