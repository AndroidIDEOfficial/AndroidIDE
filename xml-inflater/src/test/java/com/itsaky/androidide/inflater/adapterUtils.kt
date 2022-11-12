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
import com.itsaky.androidide.inflater.internal.adapters.AbsoluteLayoutAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.AutoCompleteTextViewAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.ButtonAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.CheckBoxAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.CheckedTextViewAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.EditTextAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.FrameLayoutAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.GestureOverlayViewAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.GridLayoutAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.ImageButtonAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.ImageViewAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.LinearLayoutAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.ListViewAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.ProgressBarAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.RadioButtonAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.RelativeLayoutAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.SeekBarAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.SpinnerAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.SurfaceViewAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.SwitchAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.TextViewAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.TextureViewAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.ToggleButtonAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.ViewAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.WebViewAttrAdapter

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
    AbsoluteLayout::class to AbsoluteLayoutAttrAdapter::class,
    AutoCompleteTextView::class to AutoCompleteTextViewAttrAdapter::class,
    Button::class to ButtonAttrAdapter::class,
    CheckBox::class to CheckBoxAttrAdapter::class,
    CheckedTextView::class to CheckedTextViewAttrAdapter::class,
    EditText::class to EditTextAttrAdapter::class,
    FrameLayout::class to FrameLayoutAttrAdapter::class,
    GestureOverlayView::class to GestureOverlayViewAttrAdapter::class,
    GridLayout::class to GridLayoutAttrAdapter::class,
    ImageButton::class to ImageButtonAttrAdapter::class,
    ImageView::class to ImageViewAttrAdapter::class,
    LinearLayout::class to LinearLayoutAttrAdapter::class,
    ListView::class to ListViewAttrAdapter::class,
    ProgressBar::class to ProgressBarAttrAdapter::class,
    RadioButton::class to RadioButtonAttrAdapter::class,
    RelativeLayout::class to RelativeLayoutAttrAdapter::class,
    SeekBar::class to SeekBarAttrAdapter::class,
    Spinner::class to SpinnerAttrAdapter::class,
    Switch::class to SwitchAttrAdapter::class,
    SurfaceView::class to SurfaceViewAttrAdapter::class,
    TextView::class to TextViewAttrAdapter::class,
    TextureView::class to TextureViewAttrAdapter::class,
    ToggleButton::class to ToggleButtonAttrAdapter::class,
    View::class to ViewAttrAdapter::class,
    WebView::class to WebViewAttrAdapter::class
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