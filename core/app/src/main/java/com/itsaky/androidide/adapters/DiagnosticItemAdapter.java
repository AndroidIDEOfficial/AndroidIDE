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

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.itsaky.androidide.databinding.LayoutDiagnosticItemBinding;
import com.itsaky.androidide.interfaces.DiagnosticClickListener;
import com.itsaky.androidide.lsp.models.DiagnosticItem;
import com.itsaky.androidide.lsp.models.DiagnosticSeverity;
import com.itsaky.androidide.resources.R;
import java.io.File;
import java.util.List;
import java.util.Objects;

public class DiagnosticItemAdapter extends RecyclerView.Adapter<DiagnosticItemAdapter.VH> {

  private final List<DiagnosticItem> diagnostics;
  private final File file;
  private final DiagnosticClickListener listener;

  public DiagnosticItemAdapter(
      List<DiagnosticItem> diagnostics, File file, DiagnosticClickListener listener) {
    this.diagnostics = Objects.requireNonNull(diagnostics);
    this.file = file;
    this.listener = listener;
  }

  @NonNull
  @Override
  public DiagnosticItemAdapter.VH onCreateViewHolder(@NonNull ViewGroup p1, int p2) {
    return new VH(
        LayoutDiagnosticItemBinding.inflate(LayoutInflater.from(p1.getContext()), p1, false));
  }

  @Override
  public void onBindViewHolder(DiagnosticItemAdapter.VH p1, int p2) {
    final DiagnosticItem diagnostic = diagnostics.get(p2);
    final LayoutDiagnosticItemBinding binding = p1.binding;

    binding.icon.setImageResource(getDiagnosticIconId(diagnostic));
    binding.icon.setColorFilter(
        ContextCompat.getColor(
            binding.icon.getContext(),
            diagnostic.getSeverity() == DiagnosticSeverity.ERROR
                ? R.color.diagnostic_error
                : R.color.diagnostic_warning),
        PorterDuff.Mode.SRC_ATOP);
    binding.title.setText(diagnostic.getMessage());

    binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (listener != null) {
                listener.onDiagnosticClick(file, diagnostic);
              }
            });
  }

  @Override
  public int getItemCount() {
    return diagnostics.size();
  }

  private int getDiagnosticIconId(DiagnosticItem diagnostic) {
    if (diagnostic.getSeverity() == DiagnosticSeverity.ERROR)
      return R.drawable.ic_compilation_error;
    return R.drawable.ic_info;
  }

  public static class VH extends RecyclerView.ViewHolder {
    private LayoutDiagnosticItemBinding binding;

    public VH(LayoutDiagnosticItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
