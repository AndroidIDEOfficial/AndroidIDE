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
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.itsaky.androidide.utils.ILogger;

/**
 * @author Akash Yadav
 */
public class EnumEditor extends FixedValueEditor {

  private static final ILogger LOG = ILogger.newInstance("EnumEditor");

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    this.chipGroup.setSingleSelection(true);
    this.chipGroup.setSelectionRequired(true);

    final var val = this.attribute.getValue();
    if (!TextUtils.isEmpty(val)) {
      for (int i = 0; i < this.chipGroup.getChildCount(); i++) {
        final var chip = (Chip) this.chipGroup.getChildAt(i);
        if (val.equals(chip.getText().toString().trim())) {
          chip.setChecked(true);
          break;
        }
      }
    }
  }

  @Override
  protected void onCheckChanged(@NonNull ChipGroup group, int checkedId) {
    final var chip = (Chip) group.findViewById(group.getCheckedChipId());
    if (chip == null) {
      LOG.error("Unable to update enum value. Checked Chip view is null.");
      return;
    }

    final var val = chip.getText().toString().trim();
    notifyValueChanged(val);
  }
}
