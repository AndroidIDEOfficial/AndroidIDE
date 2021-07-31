package io.github.rosemoe.editor.widget;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import androidx.core.content.ContextCompat;
import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutDiagnosticInfoBinding;
import com.itsaky.lsp.Diagnostic;
import com.itsaky.lsp.DiagnosticSeverity;

public class DiagnosticWindow extends EditorBasePopupWindow {
    
    private CodeEditor editor;
    private Diagnostic diagnostic;
    private LayoutDiagnosticInfoBinding binding;
    
    public DiagnosticWindow(CodeEditor editor) {
        super(editor);
        this.editor = editor;
        this.binding = LayoutDiagnosticInfoBinding.inflate(LayoutInflater.from(editor.getContext()));
        
        setContentView(this.binding.getRoot());
    }

    @Override
    public void show() {
        super.show();
        setupData();
    }
    
    public void setMaxHeight(int height) {
        
    }

    @Override
    public void updatePosition() {
        super.updatePosition();
        setupData();
    }
    
    private void setupData() {
        if(diagnostic != null) {
            binding.icon.setImageResource(getDiagnosticIconId());
            binding.icon.setColorFilter(ContextCompat.getColor(binding.icon.getContext(), R.color.primaryIconColor), PorterDuff.Mode.SRC_ATOP);
            binding.errorType.setText(getDiagnosticTypeString());
            binding.errorPosition.setText(
                editor.getContext().getString(
                    com.itsaky.androidide.R.string.diagnostic_position,
                    ""+ diagnostic.range.start.line,
                    ""+ diagnostic.range.start.character,
                    ""+ diagnostic.range.end.line,
                    ""+ diagnostic.range.end.character
                )
            );

            binding.msg.setText(diagnostic.message);
            binding.msg.setTextIsSelectable(true);
        }
    }
    
    public DiagnosticWindow setDiagnostic(Diagnostic diag) {
        this.diagnostic = diag;
        return this;
    }
    
    private int getDiagnosticIconId() {
        if(diagnostic.severity == DiagnosticSeverity.Error)
            return R.drawable.ic_compilation_error;
        return R.drawable.ic_info;
    }
    
    private String getDiagnosticTypeString() {
        if(diagnostic.severity == DiagnosticSeverity.Error) {
            return editor.getContext().getString(com.itsaky.androidide.R.string.diagnostic_error);
        } else if(diagnostic.severity == DiagnosticSeverity.Warning) {
            return editor.getContext().getString(com.itsaky.androidide.R.string.diagnostic_warning);
        }
        return editor.getContext().getString(com.itsaky.androidide.R.string.diagnostic_note);
    }
    
}
