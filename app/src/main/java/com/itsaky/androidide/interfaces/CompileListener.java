package com.itsaky.androidide.interfaces;

import com.itsaky.androidide.services.compiler.model.CompilerDiagnostic;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface CompileListener {
	void onCompilationResult(boolean singleFile, Map<File, List<CompilerDiagnostic>> diagnostics);
}
