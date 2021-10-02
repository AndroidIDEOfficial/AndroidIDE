package com.itsaky.androidide.adapters;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutDiagnosticItemBinding;
import com.itsaky.androidide.interfaces.DiagnosticClickListener;
import java.io.File;
import java.util.List;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;

public class DiagnosticItemAdpater extends RecyclerView.Adapter<DiagnosticItemAdpater.VH> {
    
    private List<Diagnostic> diags;
    private final File file;
    private DiagnosticClickListener listener;

    public DiagnosticItemAdpater(List<Diagnostic> diags, File file, DiagnosticClickListener listener) {
        this.diags = diags;
        this.file = file;
        this.listener = listener;
    }
    
    @Override
    public DiagnosticItemAdpater.VH onCreateViewHolder(ViewGroup p1, int p2) {
        return new VH(LayoutDiagnosticItemBinding.inflate(LayoutInflater.from(p1.getContext()), p1, false));
    }
    
    @Override
    public void onBindViewHolder(DiagnosticItemAdpater.VH p1, int p2) {
        final Diagnostic diagnostic = diags.get(p2);
        final LayoutDiagnosticItemBinding binding = p1.binding;
        
        binding.icon.setImageResource(getDiagnosticIconId(diagnostic));
        binding.icon.setColorFilter(ContextCompat.getColor(binding.icon.getContext(), diagnostic.getSeverity() == DiagnosticSeverity.Error ? R.color.diagnostic_error : R.color.diagnostic_warning), PorterDuff.Mode.SRC_ATOP);
        binding.title.setText(diagnostic.getMessage());
        
        binding.getRoot().setOnClickListener(v -> {
            if(listener != null) {
                listener.onDiagnosticClick(file, diagnostic);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return diags.size();
    }
    
    private int getDiagnosticIconId(Diagnostic diagnostic) {
        if(diagnostic.getSeverity() == DiagnosticSeverity.Error)
            return R.drawable.ic_compilation_error;
        return R.drawable.ic_info;
    }
    
    public class VH extends RecyclerView.ViewHolder {
        private LayoutDiagnosticItemBinding binding;
        public VH(LayoutDiagnosticItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    
}
