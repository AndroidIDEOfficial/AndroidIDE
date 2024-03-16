/*
 * This file is part of AndroidIDE.
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
 */

package com.itsaky.androidide.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.itsaky.androidide.databinding.LayoutDiagnosticGroupBinding;
import com.itsaky.androidide.interfaces.DiagnosticClickListener;
import com.itsaky.androidide.models.DiagnosticGroup;
import java.util.List;
import java.util.Objects;

public class DiagnosticsAdapter extends RecyclerView.Adapter<DiagnosticsAdapter.VH> {

  private final List<DiagnosticGroup> diagnostics;
  private final DiagnosticClickListener listener;

  public DiagnosticsAdapter(List<DiagnosticGroup> diagnostics, DiagnosticClickListener listener) {
    this.diagnostics = Objects.requireNonNull(diagnostics);
    this.listener = listener;
  }

  @NonNull
  @Override
  public DiagnosticsAdapter.VH onCreateViewHolder(ViewGroup p1, int p2) {
    return new VH(
        LayoutDiagnosticGroupBinding.inflate(LayoutInflater.from(p1.getContext()), p1, false));
  }

  @Override
  public void onBindViewHolder(DiagnosticsAdapter.VH p1, int p2) {
    final DiagnosticGroup group = diagnostics.get(p2);
    final LayoutDiagnosticGroupBinding binding = p1.binding;

    binding.info.icon.setImageResource(group.icon);
    binding.info.title.setText(group.text);
    binding.diagnostics.setLayoutManager(new LinearLayoutManager(binding.diagnostics.getContext()));
    binding.diagnostics.setAdapter(
        new DiagnosticItemAdapter(group.diagnostics, group.file, listener));

    binding
        .info
        .getRoot()
        .setOnClickListener(
            v -> {
              if (listener != null) {
                listener.onGroupClick(group);
              }
            });
  }

  @Override
  public int getItemCount() {
    return diagnostics.size();
  }

  public class VH extends RecyclerView.ViewHolder {
    private LayoutDiagnosticGroupBinding binding;

    public VH(LayoutDiagnosticGroupBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
