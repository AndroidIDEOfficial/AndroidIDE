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

package com.itsaky.androidide.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.itsaky.androidide.databinding.LayoutOptionssheetItemBinding;
import com.itsaky.androidide.models.SheetOption;
import java.util.List;

public class OptionsSheetAdapter extends RecyclerView.Adapter<OptionsSheetAdapter.VH> {

  private OnOptionsClickListener listener;
  private List<SheetOption> options;

  public OptionsSheetAdapter(List<SheetOption> options, OnOptionsClickListener listener) {
    this.options = options;
    this.listener = listener;
  }

  @Override
  public OptionsSheetAdapter.VH onCreateViewHolder(ViewGroup p1, int p2) {
    return new VH(
        LayoutOptionssheetItemBinding.inflate(LayoutInflater.from(p1.getContext()), p1, false));
  }

  @Override
  public void onBindViewHolder(OptionsSheetAdapter.VH p1, int p2) {
    final LayoutOptionssheetItemBinding binding = p1.binding;
    final SheetOption option = options.get(p2);

    binding.text.setText(option.title);
    binding.icon.setImageDrawable(option.icon);

    binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (listener != null) listener.onOptionClick(option);
            });
  }

  @Override
  public int getItemCount() {
    return options.size();
  }

  public class VH extends RecyclerView.ViewHolder {
    private LayoutOptionssheetItemBinding binding;

    public VH(LayoutOptionssheetItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  public static interface OnOptionsClickListener {
    public void onOptionClick(SheetOption option);
  }
}
