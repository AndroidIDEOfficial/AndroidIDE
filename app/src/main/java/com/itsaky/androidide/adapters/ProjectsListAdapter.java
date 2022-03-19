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

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutProjectsItemBinding;
import com.itsaky.androidide.project.AndroidProject;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.toaster.Toaster;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProjectsListAdapter extends RecyclerView.Adapter<ProjectsListAdapter.VH> {
    private ArrayList<AndroidProject> mProjects;
    private OnProjectClickListener listener;

    private final Comparator<AndroidProject> PROJECTS_SORTER =
            new Comparator<AndroidProject>() {

                @Override
                public int compare(AndroidProject p1, AndroidProject p2) {
                    return p1.getAppName().compareTo(p2.getAppName());
                }
            };

    public ProjectsListAdapter(
            ArrayList<AndroidProject> mProjects, OnProjectClickListener listener) {
        this.mProjects = mProjects;
        this.listener = listener;
        Collections.sort(this.mProjects, PROJECTS_SORTER);
    }

    @Override
    public ProjectsListAdapter.VH onCreateViewHolder(ViewGroup p1, int p2) {
        return new VH(
                LayoutProjectsItemBinding.inflate(LayoutInflater.from(p1.getContext()), p1, false));
    }

    @Override
    public void onBindViewHolder(ProjectsListAdapter.VH holder, int p2) {
        final AndroidProject project = mProjects.get(p2);
        holder.binding.projecsitemName.setText(project.getAppName());
        holder.binding.projecsitemPkgName.setText(project.getPackageName());
        holder.binding.deleteMsg.setText(
                holder.binding
                        .deleteMsg
                        .getContext()
                        .getString(R.string.msg_confirm_delete, project.getAppName()));
        if (!TextUtils.isEmpty(project.getIconPath()))
            Glide.with(holder.itemView.getContext().getApplicationContext())
                    .load(new File(project.getIconPath()))
                    .into(holder.binding.projecsitemIcon);
        else holder.binding.projecsitemIcon.setImageResource(R.drawable.ic_android);

        holder.binding.projectsitemClickableContainer.setOnClickListener(
                v -> sendClickEvent(project));
        holder.binding.projectsitemClickableContainer.setOnLongClickListener(
                v -> holder.toggleDelete());
        holder.binding.delete.setOnClickListener(v -> holder.showConfirmDelete());
        holder.binding.deleteCancel.setOnClickListener(v -> holder.hideAllDelete());
        holder.binding.deleteOk.setOnClickListener(v -> deleteProject(project, p2));
    }

    private void deleteProject(AndroidProject project, int index) {
        index = index >= mProjects.size() ? mProjects.size() - 1 : index;
        if (index < 0) return;
        mProjects.remove(index);
        notifyItemRemoved(index);

        new TaskExecutor()
                .executeAsync(
                        () -> FileUtils.delete(project.getProjectPath()),
                        __ -> {
                            StudioApp.getInstance()
                                    .toast(
                                            __
                                                    ? R.string.msg_project_deleted
                                                    : R.string.msg_project_delete_failed,
                                            Toaster.Type.SUCCESS);
                        });
    }

    private void sendClickEvent(AndroidProject project) {
        if (listener != null) listener.onProjectClick(project);
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        LayoutProjectsItemBinding binding;

        public VH(LayoutProjectsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void showConfirmDelete() {
            binding.confirmDeleteContainer.setEnabled(true);
            TransitionManager.beginDelayedTransition(
                    binding.getRoot(), new ChangeBounds().setDuration(85));
            binding.confirmDeleteContainer.setVisibility(View.VISIBLE);
        }

        public void hideAllDelete() {
            hideDelete();
            hideConfirmDelete();
        }

        public boolean toggleDelete() {
            binding.delete.setEnabled(!binding.delete.isEnabled());
            TransitionManager.beginDelayedTransition(
                    binding.getRoot(), new ChangeBounds().setDuration(85));
            binding.delete.setVisibility(binding.delete.isEnabled() ? View.VISIBLE : View.GONE);
            return true;
        }

        public void hideDelete() {
            binding.delete.setEnabled(false);
            TransitionManager.beginDelayedTransition(
                    binding.getRoot(), new ChangeBounds().setDuration(85));
            binding.delete.setVisibility(View.GONE);
        }

        public void hideConfirmDelete() {
            binding.confirmDeleteContainer.setEnabled(false);
            TransitionManager.beginDelayedTransition(
                    binding.getRoot(), new ChangeBounds().setDuration(85));
            binding.confirmDeleteContainer.setVisibility(View.GONE);
        }
    }

    public interface OnProjectClickListener {
        public void onProjectClick(AndroidProject project);
    }
}
