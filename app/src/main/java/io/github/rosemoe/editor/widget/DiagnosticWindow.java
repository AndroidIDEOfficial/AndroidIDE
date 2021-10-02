package io.github.rosemoe.editor.widget;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import androidx.core.content.ContextCompat;
import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutDiagnosticInfoBinding;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;

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
                    ""+ (diagnostic.getRange().getStart().getLine() + 1),
                    ""+ (diagnostic.getRange().getStart().getCharacter() + 1),
                    ""+ (diagnostic.getRange().getEnd().getLine() + 1),
                    ""+ (diagnostic.getRange().getEnd().getCharacter() + 1)
                )
            );

            binding.msg.setText(diagnostic.getMessage());
            binding.msg.setTextIsSelectable(true);
        }
    }
    
    public DiagnosticWindow setDiagnostic(Diagnostic diag) {
        this.diagnostic = diag;
        return this;
    }
    
    private int getDiagnosticIconId() {
        if(diagnostic.getSeverity() == DiagnosticSeverity.Error)
            return R.drawable.ic_compilation_error;
        return R.drawable.ic_info;
    }
    
    private String getDiagnosticTypeString() {
        if(diagnostic.getSeverity() == DiagnosticSeverity.Error) {
            return editor.getContext().getString(com.itsaky.androidide.R.string.diagnostic_error);
        } else if(diagnostic.getSeverity() == DiagnosticSeverity.Warning) {
            return editor.getContext().getString(com.itsaky.androidide.R.string.diagnostic_warning);
        } else if(diagnostic.getSeverity() == DiagnosticSeverity.Hint) {
            return editor.getContext().getString(com.itsaky.androidide.R.string.diagnostic_hint);
        }
        return editor.getContext().getString(com.itsaky.androidide.R.string.diagnostic_info);
    }
    
}
