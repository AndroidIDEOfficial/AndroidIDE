package com.itsaky.androidide.fragments.sheets;

import android.view.View;
import android.widget.LinearLayout;
import com.itsaky.androidide.databinding.LayoutProjectDetailsBinding;
import com.itsaky.androidide.models.project.IDEProject;
import java.io.File;
import com.bumptech.glide.Glide;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import androidx.annotation.DrawableRes;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.graphics.PorterDuff;

public class ProjectInfoSheet extends BaseBottomSheetFragment {
    
    private LayoutProjectDetailsBinding binding;
    private IDEProject project;
    
    @Override
    protected void bind(LinearLayout container) {
        binding = LayoutProjectDetailsBinding.inflate(getLayoutInflater());
        container.addView(binding.getRoot());
        
        setShowShadow(false);
        
        boolean noProject = project == null;
        if(noProject) {
            binding.emptyView.setVisibility(View.VISIBLE);
            binding.detailsContainer.setVisibility(View.GONE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
            binding.detailsContainer.setVisibility(View.VISIBLE);
        }
        
        if(!noProject) {
            boolean iconInvisible = false;
            if(project.iconPath != null) {
                File icon = new File(project.iconPath);
                if(icon.exists()) {
                    Glide.with(getContext().getApplicationContext()).load(icon).into(binding.appIcon);
                } else iconInvisible = true;
            } else iconInvisible = true;
            
            binding.appIcon.setVisibility(iconInvisible ? View.INVISIBLE : View.VISIBLE);
            
            String name = project.displayName.trim();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            
            binding.projectName.setText(name);
            
            // Not sure if we would get line height without setting text
            binding.projectSummary.setText("Getting info");
            final int lineHeight = binding.projectSummary.getLineHeight();
            SpannableStringBuilder summaryBuilder = new SpannableStringBuilder();
            summaryBuilder.append(" ", createSpan(com.itsaky.androidide.R.drawable.ic_package, lineHeight), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            summaryBuilder.append(" ");
            summaryBuilder.append(getString(com.itsaky.androidide.R.string.msg_module_count, project.modules.size()));
            summaryBuilder.append("\n");
            summaryBuilder.append(" ", createSpan(com.itsaky.androidide.R.drawable.ic_run, lineHeight), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            summaryBuilder.append(" ");
            summaryBuilder.append(getString(com.itsaky.androidide.R.string.msg_task_count, project.tasks.size()));
            binding.projectSummary.setText(summaryBuilder);
        }
    }
    
    private ImageSpan createSpan(@DrawableRes int id, int lineHeight) {
        Drawable image = ContextCompat.getDrawable(getContext(), id);
        image.setBounds(0, 0, lineHeight, lineHeight);
        image.setColorFilter(ContextCompat.getColor(getContext(), com.itsaky.androidide.R.color.secondaryTextColor_light), PorterDuff.Mode.SRC_ATOP);
        return new ImageSpan(image);
    }

    @Override
    protected boolean shouldHideTitle() {
        return true;
    }
    
    public ProjectInfoSheet setProject(IDEProject project) {
        this.project = project;
        return this;
    }
}
