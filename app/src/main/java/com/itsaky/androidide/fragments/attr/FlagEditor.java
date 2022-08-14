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

package com.itsaky.androidide.fragments.attr;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.itsaky.androidide.utils.ILogger;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Akash Yadav
 */
public class FlagEditor extends FixedValueEditor {

  private static final ILogger LOG = ILogger.newInstance("FlagEditor");

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    this.chipGroup.setSingleSelection(false);
    this.chipGroup.setSelectionRequired(true);
    this.chipGroup.setOnCheckedChangeListener(null); // does not work for multiple selections

    if (!TextUtils.isEmpty(attribute.getValue())) {
      final var values = Arrays.asList(this.attribute.getValue().split("\\|"));
      for (int i = 0; i < this.chipGroup.getChildCount(); i++) {
        final var chip = (Chip) this.chipGroup.getChildAt(i);
        if (values.contains(chip.getText().toString().trim())) {
          chip.setChecked(true);
        }
      }
    }
  }

  @Override
  protected void onCheckChanged(@NonNull ChipGroup group, int checkedId) {
    // Does noting...
  }

  @Override
  protected Chip newChip(String title, boolean checked) {
    final var chip = super.newChip(title, checked);
    chip.setOnCheckedChangeListener(this::onCheckChangedMultiple);
    return chip;
  }

  protected void onCheckChangedMultiple(CompoundButton compoundButton, boolean b) {
    final var items = new ArrayList<String>();
    for (var id : this.chipGroup.getCheckedChipIds()) {
      final var chip = (Chip) this.chipGroup.findViewById(id);
      if (chip == null) {
        LOG.error("Unable to find chip with checked id:", id);
        continue;
      }

      final var val = chip.getText().toString().trim();
      items.add(val);
    }

    notifyValueChanged(TextUtils.join("|", items));
  }
}
