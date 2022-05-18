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

package com.itsaky.androidide.adapters;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutAttrEditorSheetItemBinding;
import com.itsaky.androidide.models.XMLAttribute;

import java.util.List;

public class XMLAttributeListAdapter extends RecyclerView.Adapter<XMLAttributeListAdapter.VH> {

  private final List<XMLAttribute> attributes;
  private final OnClickListener clickListener;

  public XMLAttributeListAdapter(List<XMLAttribute> attributes, OnClickListener clickListener) {
    this.attributes = attributes;
    this.clickListener = clickListener;
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new VH(
        LayoutAttrEditorSheetItemBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    final var binding = holder.binding;
    final var attr = attributes.get(position);
    final var sb = new SpannableStringBuilder();
    sb.append(attr.getAttributeName());
    if (attr.isApplied()) {
      sb.append(" = ");
      sb.append(attr.getValue(), new StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

      final var length = attr.getValue().length();
      sb.setSpan(
          new ForegroundColorSpan(
              ContextCompat.getColor(binding.text.getContext(), R.color.primaryTextColor)),
          sb.length() - length,
          sb.length() - 1,
          SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    binding.text.setText(sb);
    binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (this.clickListener != null) {
                this.clickListener.onClick(binding, attr);
              }
            });
  }

  @Override
  public int getItemCount() {
    return attributes.size();
  }

  public interface OnClickListener {
    void onClick(LayoutAttrEditorSheetItemBinding binding, XMLAttribute attribute);
  }

  static class VH extends RecyclerView.ViewHolder {
    LayoutAttrEditorSheetItemBinding binding;

    VH(@NonNull LayoutAttrEditorSheetItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
