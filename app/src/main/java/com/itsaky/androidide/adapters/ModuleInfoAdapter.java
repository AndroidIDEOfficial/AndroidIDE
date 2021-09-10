package com.itsaky.androidide.adapters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.itsaky.androidide.databinding.LayoutModuleInfoItemBinding;
import com.itsaky.androidide.models.project.IDEModule;
import java.util.ArrayList;
import java.util.List;

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
    public void onBindViewHolder(ModuleInfoAdapter.VH p1, int p2) {
        final IDEModule module = modules.get(p2);
        final LayoutModuleInfoItemBinding binding = p1.binding;
        final Context ctx = binding.getRoot().getContext();
        binding.moduleName.setText(module.displayName.substring(0, 1).toUpperCase() + module.displayName.substring(1));
        
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
