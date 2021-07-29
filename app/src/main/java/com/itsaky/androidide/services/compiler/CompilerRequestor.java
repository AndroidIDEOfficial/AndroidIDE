package com.itsaky.androidide.services.compiler;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompilerImpl;
import org.eclipse.jdt.internal.compiler.tool.EclipseFileManager;
import org.eclipse.jdt.internal.compiler.tool.EclipseFileObject;

public class CompilerRequestor implements ICompilerRequestor {

    private DiagnosticListener<? super JavaFileObject> listener;

    public CompilerRequestor(DiagnosticListener<? super JavaFileObject> listener) {
        this.listener = listener;
    }

    @Override
    public void acceptResult(CompilationResult p1) {
        if (p1 != null && p1.hasProblems()) {
            for (CategorizedProblem problem : p1.getAllProblems()) {
                report(problem);
            }
        }
    }

    private void report(final CategorizedProblem problem) {
        this.listener.report(new Diagnostic<JavaFileObject>() {
                @Override
                public String getCode() {
                    return null;
                }
                @Override
                public long getColumnNumber() {
                    if (problem instanceof DefaultProblem) {
                        return ((DefaultProblem) problem).column;
                    }
                    return Diagnostic.NOPOS;
                }
                @Override
                public long getEndPosition() {
                    if (problem instanceof DefaultProblem) {
                        return ((DefaultProblem) problem).getSourceEnd();
                    }
                    return Diagnostic.NOPOS;
                }
                @Override
                public Kind getKind() {
                    if (problem.isError()) {
                        return Diagnostic.Kind.ERROR;
                    }
                    if (problem.isWarning()) {
                        return Diagnostic.Kind.WARNING;
                    } else if (problem instanceof DefaultProblem && ((DefaultProblem) problem).isInfo()) {
                        return Diagnostic.Kind.NOTE;
                    }
                    return Diagnostic.Kind.OTHER;
                }
                @Override
                public long getLineNumber() {
                    if (problem instanceof DefaultProblem) {
                        return ((DefaultProblem) problem).getSourceLineNumber();
                    }
                    return Diagnostic.NOPOS;
                }
                @Override
                public String getMessage(Locale locale) {
                    return problem.getMessage();
                }
                @Override
                public long getPosition() {
                    if (problem instanceof DefaultProblem) {
                        return ((DefaultProblem) problem).getSourceStart();
                    }
                    return Diagnostic.NOPOS;
                }
                @Override
                public JavaFileObject getSource() {
                    if (problem instanceof DefaultProblem) {
                        File f = new File(new String(problem.getOriginatingFileName()));
                        if (f.exists()) {
                            return new EclipseFileObject(null, f.toURI(), JavaFileObject.Kind.SOURCE, null);
                        }
                        return null;
                    }
                    return null;
                }
                @Override
                public long getStartPosition() {
                    return getPosition();
                }
            });
    }
}
