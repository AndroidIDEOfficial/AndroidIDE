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

package com.itsaky.androidide.adapters.viewholders;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.LayoutFiletreeItemBinding;
import com.itsaky.androidide.utils.Environment;
import com.unnamed.b.atv.model.TreeNode;

import java.io.File;

public class FileTreeViewHolder extends TreeNode.BaseNodeViewHolder<File> {

  private Context context;
  private ImageView chevron;
  private LayoutFiletreeItemBinding binding;

  public FileTreeViewHolder(Context context) {
    super(context);
    this.context = context;
  }

  @Override
  public View createNodeView(TreeNode node, File file) {
    binding = LayoutFiletreeItemBinding.inflate(LayoutInflater.from(context));
    int dp15 =
        context instanceof StudioActivity
            ? ((StudioActivity) context).dpToPx(15)
            : (int)
                (TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources().getDisplayMetrics()));
    int icon = 0;
    if (file.isDirectory()) icon = R.drawable.ic_folder;
    else if (file.getName().endsWith(".java")) icon = R.drawable.ic_language_java;
    else if (file.getName().endsWith(".kt")) icon = R.drawable.ic_language_kotlin;
    else if (file.getName().endsWith(".xml")) icon = R.drawable.ic_language_xml;
    else if (file.getName().endsWith(".gradle")) icon = R.drawable.ic_language_gradle;
    else if (file.getName().endsWith(".json")) icon = R.drawable.ic_language_json;
    else icon = R.drawable.ic_file_unknown;

    final boolean isGradle =
        file.getAbsolutePath().equals(Environment.GRADLE_USER_HOME.getAbsolutePath());
    chevron = binding.getRoot().findViewById(R.id.filetree_chevron);
    binding.filetreeName.setText(isGradle ? "GRADLE_HOME" : file.getName());
    binding.filetreeIcon.setImageResource(icon);
    binding
        .getRoot()
        .setPadding(
            binding.getRoot().getPaddingLeft() + (dp15 * (node.getLevel() - 1)),
            binding.getRoot().getPaddingTop(),
            binding.getRoot().getPaddingRight(),
            binding.getRoot().getPaddingBottom());
    chevron.setVisibility(file.isFile() ? View.INVISIBLE : View.VISIBLE);
    chevron.setImageResource(
        node.isExpanded() ? R.drawable.ic_chevron_down : R.drawable.ic_chevron_right);
    chevron
        .getDrawable()
        .setColorFilter(
            ContextCompat.getColor(context, R.color.secondaryLightColor), PorterDuff.Mode.SRC_ATOP);
    binding
        .filetreeIcon
        .getDrawable()
        .setColorFilter(
            ContextCompat.getColor(context, R.color.secondaryLightColor), PorterDuff.Mode.SRC_ATOP);

    return binding.getRoot();
  }

  public void setLoading() {
    chevron
        .getDrawable()
        .setColorFilter(
            ContextCompat.getColor(context, R.color.secondaryLightColor), PorterDuff.Mode.SRC_ATOP);
  }

  public void updateChevron(boolean isExpanded) {
    chevron.setImageResource(isExpanded ? R.drawable.ic_chevron_down : R.drawable.ic_chevron_right);
    chevron
        .getDrawable()
        .setColorFilter(
            ContextCompat.getColor(context, R.color.secondaryLightColor), PorterDuff.Mode.SRC_ATOP);
  }
}
