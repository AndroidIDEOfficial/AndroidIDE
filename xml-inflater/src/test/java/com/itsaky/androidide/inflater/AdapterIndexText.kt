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
import android.view.*
import android.widget.*
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.inflater.internal.AttributeAdapterIndex
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
import com.itsaky.androidide.inflater.internal.adapters.SwitchAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.TextViewAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.ToggleButtonAttrAdapter
import com.itsaky.androidide.inflater.internal.adapters.ViewAttrAdapter
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class AdapterIndexText {

  @Test
  fun `verify instances of adapters`() {
    val expected =
      mapOf(
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
        TextView::class to TextViewAttrAdapter::class,
        ToggleButton::class to ToggleButtonAttrAdapter::class,
        View::class to ViewAttrAdapter::class
      )

    val unexpected =
      listOf(
        AbsListView::class,
        AbsSeekBar::class,
        AbsSpinner::class,
        AdapterView::class,
        CompoundButton::class,
        ViewGroup::class
      )

    expected.forEach {
      AttributeAdapterIndex.getAdapter(it.key.qualifiedName).apply {
        assertThat(this).isNotNull()
        assertThat(this).isInstanceOf(it.value.java)
      }
    }

    unexpected.forEach { assertThat(AttributeAdapterIndex.getAdapter(it.qualifiedName)).isNull() }
  }
}
