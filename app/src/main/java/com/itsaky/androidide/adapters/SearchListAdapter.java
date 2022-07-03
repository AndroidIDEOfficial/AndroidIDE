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

import android.graphics.PorterDuff;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.databinding.LayoutSearchResultGroupBinding;
import com.itsaky.androidide.databinding.LayoutSearchResultItemBinding;
import com.itsaky.androidide.models.SearchResult;
import com.itsaky.androidide.syntax.highlighters.JavaHighlighter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.VH> {

  private final Map<File, List<SearchResult>> results;
  private final List<File> keys;

  private final OnFileClickListener fileListener;
  private final OnMatchClickListener matchListener;

  public SearchListAdapter(
      Map<File, List<SearchResult>> results,
      OnFileClickListener fileListener,
      OnMatchClickListener matchListener) {
    if (results == null) results = new HashMap<>();
    this.results = results;
    this.keys = new ArrayList<File>(results.keySet());
    this.fileListener = fileListener;
    this.matchListener = matchListener;
  }

  @Override
  public SearchListAdapter.VH onCreateViewHolder(ViewGroup p1, int p2) {
    return new VH(LayoutSearchResultGroupBinding.inflate(LayoutInflater.from(p1.getContext())));
  }

  @Override
  public void onBindViewHolder(SearchListAdapter.VH p1, int p2) {
    final LayoutSearchResultGroupBinding binding = p1.binding;
    final File file = keys.get(p2);
    final List<SearchResult> matches = results.get(file);
    final int color =
        ContextCompat.getColor(
            binding.icon.getContext(), com.itsaky.androidide.R.color.secondaryColor);
    binding.title.setText(file.getName());
    binding.icon.setImageResource(com.itsaky.androidide.R.drawable.ic_language_java);
    binding.icon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    binding.items.setLayoutManager(new LinearLayoutManager(binding.items.getContext()));
    binding.items.setAdapter(new ChildAdapter(matches));

    binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (fileListener != null) fileListener.onClick(file);
            });
  }

  @Override
  public int getItemCount() {
    return results.size();
  }

  public class ChildAdapter extends RecyclerView.Adapter<ChildVH> {

    private final List<SearchResult> matches;

    public ChildAdapter(List<SearchResult> matches) {
      if (matches == null) matches = new ArrayList<>();
      this.matches = matches;
    }

    @Override
    public SearchListAdapter.ChildVH onCreateViewHolder(ViewGroup p1, int p2) {
      return new ChildVH(
          LayoutSearchResultItemBinding.inflate(LayoutInflater.from(p1.getContext())));
    }

    @Override
    public void onBindViewHolder(SearchListAdapter.ChildVH p1, int p2) {
      final SearchResult match = matches.get(p2);
      final LayoutSearchResultItemBinding binding = p1.binding;
      new Thread(
              () -> {
                try {
                  final SpannableStringBuilder sb =
                      new JavaHighlighter().highlight(match.line, match.match);
                  ThreadUtils.runOnUiThread(() -> binding.text.setText(sb));
                } catch (Exception e) {
                  ThreadUtils.runOnUiThread(() -> binding.text.setText(match.match));
                }
              })
          .start();
      binding
          .getRoot()
          .setOnClickListener(
              v -> {
                if (matchListener != null) {
                  matchListener.onClick(match);
                }
              });
    }

    @Override
    public int getItemCount() {
      return matches.size();
    }
  }

  public class VH extends RecyclerView.ViewHolder {
    private LayoutSearchResultGroupBinding binding;

    public VH(LayoutSearchResultGroupBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  public class ChildVH extends RecyclerView.ViewHolder {
    private LayoutSearchResultItemBinding binding;

    public ChildVH(LayoutSearchResultItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  public static interface OnFileClickListener {
    void onClick(File file);
  }

  public static interface OnMatchClickListener {
    void onClick(SearchResult match);
  }
}
