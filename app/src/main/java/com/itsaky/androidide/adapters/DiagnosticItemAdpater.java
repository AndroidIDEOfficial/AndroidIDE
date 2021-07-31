package com.itsaky.androidide.adapters;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutDiagnosticItemBinding;
import com.itsaky.androidide.interfaces.DiagnosticClickListener;
import com.itsaky.lsp.Diagnostic;
import com.itsaky.lsp.DiagnosticSeverity;
import java.util.List;

public class DiagnosticItemAdpater extends RecyclerView.Adapter<DiagnosticItemAdpater.VH> {
    
    private List<Diagnostic> diags;
    private DiagnosticClickListener listener;

    public DiagnosticItemAdpater(List<Diagnostic> diags, DiagnosticClickListener listener) {
        this.diags = diags;
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
        binding.icon.setColorFilter(ContextCompat.getColor(binding.icon.getContext(), diagnostic.severity == DiagnosticSeverity.Error ? R.color.diagnostic_error : R.color.diagnostic_warning), PorterDuff.Mode.SRC_ATOP);
        binding.title.setText(diagnostic.message);
        
        binding.getRoot().setOnClickListener(v -> {
            if(listener != null) {
                listener.onDiagnosticClick(diagnostic);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return diags.size();
    }
    
    private int getDiagnosticIconId(Diagnostic diagnostic) {
        if(diagnostic.severity == DiagnosticSeverity.Error)
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
