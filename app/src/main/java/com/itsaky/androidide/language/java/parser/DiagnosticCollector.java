package com.itsaky.androidide.language.java.parser;

import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import java.util.Collections;
import java.util.ArrayList;
import javax.tools.JavaFileObject;

public class DiagnosticCollector<S> implements DiagnosticListener<S> {
	
	private List<Diagnostic<? extends S>> diagnostics = Collections.synchronizedList(new ArrayList<Diagnostic<? extends S>>());
	
	@Override
	public void report(Diagnostic<? extends S> diagnostic) {
		diagnostic.getClass();
		diagnostics.add(diagnostic);
	}
	
	public void clearDiagnostics() {
		diagnostics.clear();
	}
	
	public List<Diagnostic<? extends S>> getDiagnostics() {
		return Collections.unmodifiableList(diagnostics);
	}
}
