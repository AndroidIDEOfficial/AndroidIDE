/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/

package com.itsaky.androidide.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itsaky.androidide.R;
import com.itsaky.androidide.utils.ResourceUtilsKt;

public class EmptyView extends RelativeLayout {

  public EmptyView(Context context) {
    this(context, null);
  }

  public EmptyView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
  }

  public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    init();
  }

  private void init() {

    removeAllViews();

    TextView text = new TextView(getContext());
    text.setText(com.itsaky.androidide.resources.R.string.msg_empty_view);
    text.setTextColor(ResourceUtilsKt.resolveAttr(getContext(), R.attr.colorSecondaryVariant));
    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

    LayoutParams params = new LayoutParams(-2, -2);
    params.addRule(CENTER_IN_PARENT);

    addView(text, params);
  }
}
