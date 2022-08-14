package com.itsaky.androidide.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.itsaky.androidide.databinding.WizardTemplateItemBinding;
import com.itsaky.androidide.models.ProjectTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WizardTemplateAdapter extends RecyclerView.Adapter<WizardTemplateAdapter.ViewHolder> {

  private final List<ProjectTemplate> mItems = new ArrayList<>();
  private OnItemClickListener mListener;

  public WizardTemplateAdapter() {}

  public void setOnItemClickListener(OnItemClickListener listener) {
    mListener = listener;
  }

  public void submitList(@NonNull List<ProjectTemplate> newItems) {
    DiffUtil.DiffResult diffResult =
        DiffUtil.calculateDiff(
            new DiffUtil.Callback() {
              @Override
              public int getOldListSize() {
                return mItems.size();
              }

              @Override
              public int getNewListSize() {
                return newItems.size();
              }

              @Override
              public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return Objects.equals(mItems.get(oldItemPosition), newItems.get(newItemPosition));
              }

              @Override
              public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return Objects.equals(mItems.get(oldItemPosition), newItems.get(newItemPosition));
              }
            });
    mItems.clear();
    mItems.addAll(newItems);
    diffResult.dispatchUpdatesTo(this);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    WizardTemplateItemBinding binding =
        WizardTemplateItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

    ViewHolder holder = new ViewHolder(binding);
    binding
        .getRoot()
        .setOnClickListener(
            view1 -> {
              if (mListener != null) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                  mListener.onItemClick(mItems.get(pos), pos);
                }
              }
            });
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(mItems.get(position));
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    public final ShapeableImageView icon;
    public final TextView name;

    public ViewHolder(WizardTemplateItemBinding view) {
      super(view.getRoot());

      icon = view.templateIcon;
      name = view.templateName;
    }

    private void bind(ProjectTemplate template) {
      name.setText(template.getName());
      icon.setImageResource(template.getImageId());
      icon.setShapeAppearanceModel(
          icon.getShapeAppearanceModel().toBuilder()
              .setAllCorners(CornerFamily.ROUNDED, ConvertUtils.dp2px(8))
              .build());
    }
  }

  public interface OnItemClickListener {
    void onItemClick(ProjectTemplate item, int position);
  }
}
