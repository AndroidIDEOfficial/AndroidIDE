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

@file:Suppress("DEPRECATION")

package com.itsaky.androidide.inflater

import android.gesture.GestureOverlayView
import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.AbsSeekBar
import android.widget.AbsSpinner
import android.widget.AbsoluteLayout
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.CompoundButton
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
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.ToggleButton
import com.itsaky.androidide.inflater.internal.adapters.AbsoluteLayoutAdapter
import com.itsaky.androidide.inflater.internal.adapters.AutoCompleteTextViewAdapter
import com.itsaky.androidide.inflater.internal.adapters.ButtonAdapter
import com.itsaky.androidide.inflater.internal.adapters.CheckBoxAdapter
import com.itsaky.androidide.inflater.internal.adapters.CheckedTextViewAdapter
import com.itsaky.androidide.inflater.internal.adapters.EditTextAdapter
import com.itsaky.androidide.inflater.internal.adapters.FrameLayoutAdapter
import com.itsaky.androidide.inflater.internal.adapters.GestureOverlayViewAdapter
import com.itsaky.androidide.inflater.internal.adapters.GridLayoutAdapter
import com.itsaky.androidide.inflater.internal.adapters.ImageButtonAdapter
import com.itsaky.androidide.inflater.internal.adapters.ImageViewAdapter
import com.itsaky.androidide.inflater.internal.adapters.LinearLayoutAdapter
import com.itsaky.androidide.inflater.internal.adapters.ListViewAdapter
import com.itsaky.androidide.inflater.internal.adapters.ProgressBarAdapter
import com.itsaky.androidide.inflater.internal.adapters.RadioButtonAdapter
import com.itsaky.androidide.inflater.internal.adapters.RelativeLayoutAdapter
import com.itsaky.androidide.inflater.internal.adapters.SeekBarAdapter
import com.itsaky.androidide.inflater.internal.adapters.SpinnerAdapter
import com.itsaky.androidide.inflater.internal.adapters.SurfaceViewAdapter
import com.itsaky.androidide.inflater.internal.adapters.SwitchAdapter
import com.itsaky.androidide.inflater.internal.adapters.TextViewAdapter
import com.itsaky.androidide.inflater.internal.adapters.TextureViewAdapter
import com.itsaky.androidide.inflater.internal.adapters.ToggleButtonAdapter
import com.itsaky.androidide.inflater.internal.adapters.ViewAdapter
import com.itsaky.androidide.inflater.internal.adapters.WebViewAdapter

internal fun viewDeclTemplate(name: String) : String =
  """
      <$name
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/template_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
    """.trimIndent()

internal val viewToAdapter =
  mapOf(
    AbsoluteLayout::class to AbsoluteLayoutAdapter::class,
    AutoCompleteTextView::class to AutoCompleteTextViewAdapter::class,
    Button::class to ButtonAdapter::class,
    CheckBox::class to CheckBoxAdapter::class,
    CheckedTextView::class to CheckedTextViewAdapter::class,
    EditText::class to EditTextAdapter::class,
    FrameLayout::class to FrameLayoutAdapter::class,
    GestureOverlayView::class to GestureOverlayViewAdapter::class,
    GridLayout::class to GridLayoutAdapter::class,
    ImageButton::class to ImageButtonAdapter::class,
    ImageView::class to ImageViewAdapter::class,
    LinearLayout::class to LinearLayoutAdapter::class,
    ListView::class to ListViewAdapter::class,
    ProgressBar::class to ProgressBarAdapter::class,
    RadioButton::class to RadioButtonAdapter::class,
    RelativeLayout::class to RelativeLayoutAdapter::class,
    SeekBar::class to SeekBarAdapter::class,
    Spinner::class to SpinnerAdapter::class,
    Switch::class to SwitchAdapter::class,
    SurfaceView::class to SurfaceViewAdapter::class,
    TextView::class to TextViewAdapter::class,
    TextureView::class to TextureViewAdapter::class,
    ToggleButton::class to ToggleButtonAdapter::class,
    View::class to ViewAdapter::class,
    WebView::class to WebViewAdapter::class
  )

internal val abstractViews =
  listOf(
    AbsListView::class,
    AbsSeekBar::class,
    AbsSpinner::class,
    AdapterView::class,
    CompoundButton::class,
    ViewGroup::class
  )