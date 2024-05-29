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

package com.itsaky.androidide.colorpicker;

import static android.graphics.Color.alpha;
import static android.graphics.Color.argb;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.parseColor;
import static android.graphics.Color.red;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.google.android.material.slider.Slider;
import com.itsaky.androidide.common.R;
import com.itsaky.androidide.common.databinding.LayoutColorPickerBinding;

/**
 * A color picker view.
 *
 * @author Akash Yadav
 */
public class ColorPickerView extends LinearLayout {

  private final LayoutColorPickerBinding binding;
  private final int DEFAULT_COLOR;
  private int alpha;
  private int red;
  private int green;
  private int blue;
  private final Slider.OnChangeListener mValueChangeListener =
      (slider, value, fromUser) -> {

        // If it's not from user, then the values have already been updated
        if (fromUser) {
          final var id = slider.getId();
          final var intVal = (int) value;
          if (id == R.id.alpha_slider) {
            alpha = intVal;
          } else if (id == R.id.red_slider) {
            red = intVal;
          } else if (id == R.id.green_slider) {
            green = intVal;
          } else if (id == R.id.blue_slider) {
            blue = intVal;
          }

          updatePreview();
        }
      };
  private OnPickListener mPickListener;

  public ColorPickerView(Context context) {
    this(context, null);
  }

  public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
  }

  public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    this.DEFAULT_COLOR = ContextCompat.getColor(context, R.color.color_picker_default_color);

    this.binding = LayoutColorPickerBinding.inflate(LayoutInflater.from(context));

    removeAllViews();
    addView(
        binding.getRoot(),
        new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    init();
  }

  private void init() {

    setColor(DEFAULT_COLOR);

    this.binding.alphaSlider.addOnChangeListener(mValueChangeListener);
    this.binding.redSlider.addOnChangeListener(mValueChangeListener);
    this.binding.greenSlider.addOnChangeListener(mValueChangeListener);
    this.binding.blueSlider.addOnChangeListener(mValueChangeListener);

    this.binding.hexField.getEditText().addTextChangedListener(new ColorTextWatcher());

    this.binding.pick.setOnClickListener(
        v -> {
          if (mPickListener != null) {
            mPickListener.onPick(getColor(), getHexColor());
          }
        });
  }

  /**
   * Get the hex color code of the current color value.
   *
   * @return The hex color code of the current color value.
   */
  @NonNull
  public String getHexColor() {
    return "#" + Integer.toHexString(getColor());
  }

  /**
   * Set a listener that will be notified when the user picks a color.
   *
   * @param listener The listener to set.
   */
  public void setOnPickListener(OnPickListener listener) {
    this.mPickListener = listener;
  }

  /**
   * A TextWatcher that is used to listen for text updates in the hex color input field.
   */
  private class ColorTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      try {
        final var color = parseColor("#" + s);
        setColor(color);
      } catch (Exception e) {
        // May happen due to invalid color values
        // Simply ignore
      }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
  }

  /**
   * Set the color value of this picker to the given hex color code.
   *
   * @param hexCode The hex color code.
   * @see #setColor(int)
   */
  public void setColor(final String hexCode) {
    setColor(parseColor(hexCode));
  }

  /**
   * A listener that can be used to get notified when the user picks a color.
   *
   * @author Akash Yadav
   */
  public interface OnPickListener {

    /**
     * Called when the user picks a color using the 'Pick' button.
     *
     * @param color   The integer value of the color.
     * @param hexCode The hex color code of the color.
     */
    void onPick(int color, String hexCode);
  }

  /**
   * Set the color value for this color picker. this will in turn update value of the sliders and
   * the color preview.
   *
   * @param color The new color value.
   */
  public void setColor(int color) {
    this.alpha = alpha(color);
    this.red = red(color);
    this.green = green(color);
    this.blue = blue(color);

    this.binding.alphaSlider.setValue(alpha);
    this.binding.redSlider.setValue(red);
    this.binding.greenSlider.setValue(green);
    this.binding.blueSlider.setValue(blue);

    updatePreview();
  }

  /**
   * Get the int value of the color from the current values of alpha, red, green and blue color
   * values.
   *
   * @return The int color value.
   */
  public int getColor() {
    return argb(alpha, red, green, blue);
  }

  /**
   * Call this method to update the preview of the current color.
   */
  private void updatePreview() {
    this.binding.colorPreview.setBackgroundColor(getColor());
  }
}
