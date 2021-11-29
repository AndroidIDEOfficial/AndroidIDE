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

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.itsaky.androidide.databinding.LayoutModuleInfoItemBinding;
import com.itsaky.androidide.project.IDEModule;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ModuleInfoAdapter extends RecyclerView.Adapter<ModuleInfoAdapter.VH> {
    
    private List<IDEModule> modules;

    public ModuleInfoAdapter(List<IDEModule> modules) {
        this.modules = modules;
        if(this.modules == null)
            this.modules = new ArrayList<>();
    }
    
    @Override
    public ModuleInfoAdapter.VH onCreateViewHolder(ViewGroup p1, int p2) {
        return new VH(LayoutModuleInfoItemBinding.inflate(LayoutInflater.from(p1.getContext()), p1, false));
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(ModuleInfoAdapter.VH p1, int p2) {
        final IDEModule module = modules.get(p2);
        final LayoutModuleInfoItemBinding binding = p1.binding;
        final Context ctx = binding.getRoot().getContext();
        binding.moduleName.setText(module.displayName.substring(0, 1).toUpperCase(Locale.US) + module.displayName.substring(1));
        
        SpannableStringBuilder summaryBuilder = new SpannableStringBuilder();
        if(!module.isLibrary) {
            summaryBuilder.append(ctx.getString(com.itsaky.androidide.R.string.msg_package_name, module.applicationId));
            summaryBuilder.append("\n");
        }
        summaryBuilder.append(ctx.getString(com.itsaky.androidide.R.string.msg_min_sdk, module.minSdk.apiString));
        summaryBuilder.append("\n");
        summaryBuilder.append(ctx.getString(com.itsaky.androidide.R.string.msg_target_sdk, module.targetSdk.apiString));
        summaryBuilder.append("\n");
        summaryBuilder.append(ctx.getString(com.itsaky.androidide.R.string.msg_dependency_count, module.dependencies.size()));
        summaryBuilder.append("\n");
        summaryBuilder.append(ctx.getString(com.itsaky.androidide.R.string.msg_task_count, module.tasks.size()));
        
        binding.moduleSummary.setText(summaryBuilder);
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }
    
    public class VH extends RecyclerView.ViewHolder {
        LayoutModuleInfoItemBinding binding;

        public VH(LayoutModuleInfoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
