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

package com.itsaky.androidide.uidesigner.utils

import android.content.Context
import android.view.SurfaceView
import android.view.TextureView
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Switch
import android.widget.ToggleButton
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.AttributeImpl
import com.itsaky.androidide.inflater.internal.LayoutFile
import com.itsaky.androidide.uidesigner.R.drawable
import com.itsaky.androidide.uidesigner.R.string
import com.itsaky.androidide.uidesigner.models.UiWidget
import com.itsaky.androidide.uidesigner.models.UiWidgetCategory

internal object Widgets {

  @JvmField val CATEGORY_WIDGETS = UiWidgetCategory(string.ui_category_widgets)
  @JvmField val CATEGORY_LAYOUTS = UiWidgetCategory(string.ui_category_layouts)

  private val internalCategories = mutableListOf<UiWidgetCategory>()

  val categories: List<UiWidgetCategory>
    get() = this.internalCategories

  init {
    internalCategories.add(getLayouts())
    internalCategories.add(getWidgets())
  }

  private fun getWidgets(): UiWidgetCategory {
    return CATEGORY_WIDGETS.apply {
      if (widgets.isNotEmpty()) {
        return@apply
      }
      widgets.apply {
        add(
          UiWidget(
            AutoCompleteTextView::class.java,
            string.widget_auto_complete_textview,
            drawable.ic_widget_auto_complete_textview
          )
        )
        add(UiWidget(Button::class.java, string.widget_button, drawable.ic_widget_button))
        add(UiWidget(CheckBox::class.java, string.widget_checkbox, drawable.ic_widget_checkbox))
        add(
          UiWidget(
            CheckedTextView::class.java,
            string.widget_checked_textview,
            drawable.ic_widget_checked_textview
          )
        )
        add(UiWidget(EditText::class.java, string.widget_edittext, drawable.ic_widget_edittext))
        add(
          UiWidget(
            ImageButton::class.java,
            string.widget_image_button,
            drawable.ic_widget_imagebutton
          )
        )
        add(UiWidget(ImageView::class.java, string.widget_image_view, drawable.ic_widget_imageview))
        add(UiWidget(ListView::class.java, string.widget_listview, drawable.ic_widget_list_view))
        add(
          UiWidget(
            ProgressBar::class.java,
            string.widget_progressbar,
            drawable.ic_widget_progress_bar
          )
        )
        add(
          UiWidget(
            RadioButton::class.java,
            string.widget_radio_button,
            drawable.ic_widget_radio_button
          )
        )
        add(UiWidget(SeekBar::class.java, string.widget_seekbar, drawable.ic_widget_seek_bar))
        add(
          UiWidget(
            SurfaceView::class.java,
            string.widget_surfaceview,
            drawable.ic_widget_surface_view
          )
        )
        add(UiWidget(Switch::class.java, string.widget_switch, drawable.ic_widget_switch))
        add(
          UiWidget(
            TextureView::class.java,
            string.widget_textureview,
            drawable.ic_widget_textureview
          )
        )
        add(
          UiWidget(
            ToggleButton::class.java,
            string.widget_togglebutton,
            drawable.ic_widget_toggle_button
          )
        )
        add(UiWidget(WebView::class.java, string.widget_webview, drawable.ic_widget_webview))
      }
    }
  }

  private fun getLayouts(): UiWidgetCategory {
    return CATEGORY_LAYOUTS.apply {
      if (widgets.isNotEmpty()) {
        return@apply
      }

      widgets.apply {
        add(
          UiWidget(
            FrameLayout::class.java,
            string.widget_frame_layout,
            drawable.ic_widget_frame_layout
          )
        )
        add(
          UiWidget(
            GridLayout::class.java,
            string.widget_grid_layout,
            drawable.ic_widget_grid_layout
          )
        )
        add(
          object :
            UiWidget(
              LinearLayout::class.java,
              string.widget_linear_layout_horz,
              drawable.ic_widget_linear_layout_horz
            ) {
            override fun createView(context: Context, parent: ViewGroup, layoutFile: LayoutFile): IView {
              return super.createView(context, parent, layoutFile).apply {
                addAttribute(AttributeImpl(name = "orientation", value = "horizontal"))
              }
            }
          }
        )
        add(
          object :
            UiWidget(
              LinearLayout::class.java,
              string.widget_linear_layout_vert,
              drawable.ic_widget_linear_layout_vert
            ) {
            override fun createView(context: Context, parent: ViewGroup, layoutFile: LayoutFile): IView {
              return super.createView(context, parent, layoutFile).apply {
                addAttribute(AttributeImpl(name = "orientation", value = "vertical"))
              }
            }
          }
        )
        add(
          UiWidget(
            RelativeLayout::class.java,
            string.widget_relative_layout,
            drawable.ic_widget_relative_layout
          )
        )
      }
    }
  }
}
