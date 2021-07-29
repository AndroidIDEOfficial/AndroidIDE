package com.itsaky.androidide.services.compiler.model;

import java.io.File;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompilerDiagnosticImpl implements CompilerDiagnostic {
	
	protected File file;
	protected String message;
	protected long line = 0;
	protected long column = 0;
	protected long start = 0;
	protected long end = 0;
	
	protected Diagnostic.Kind kind;
    
    public static final int COLOR_ERROR = 0xfff44336;
    public static final int COLOR_WARNING = 0xffffb300;
    public static final int COLOR_NOTE = 0xffffffff;
	
	private CompilerDiagnosticImpl(){}
	
	private CompilerDiagnosticImpl(Diagnostic<? extends JavaFileObject> diagnostic) {
		this.file = new File(diagnostic.getSource().toUri());
		this.message = diagnostic.getMessage(Locale.US);
		this.line = diagnostic.getLineNumber() - 1;
		this.column = diagnostic.getColumnNumber();
		this.start = diagnostic.getStartPosition();
		this.end = diagnostic.getEndPosition() + 1;
		this.kind = diagnostic.getKind();
	}
	
	private CompilerDiagnosticImpl(CompilerDiagnosticImpl d) {
		this.file = d.file();
		this.message = d.message();
		this.line = d.line();
		this.column = d.column();
		this.start = d.start();
		this.end = d.end();
		this.kind = d.kind();
	}
	
	public static CompilerDiagnosticImpl fromDiagnostic(Diagnostic<? extends JavaFileObject> d) {
		return new CompilerDiagnosticImpl(d);
	}
	
	public static CompilerDiagnosticImpl copy(CompilerDiagnosticImpl d) {
		return new CompilerDiagnosticImpl(d);
	}
	
	@Override
	public Diagnostic.Kind kind() {
		return kind;
	}
	
	@Override
	public File file() {
		return file;
	}

	@Override
	public long line() {
		return line;
	}

	@Override
	public long column() {
		return column;
	}

	@Override
	public long start() {
		return start;
	}

	@Override
	public long end() {
		return end;
	}

	@Override
	public String message() {
		return message;
	}

    @Override
    public int color() {
        if(kind() == Diagnostic.Kind.ERROR) {
            return COLOR_ERROR;
        } else if(kind() == Diagnostic.Kind.WARNING || kind() == Diagnostic.Kind.MANDATORY_WARNING) {
            return COLOR_WARNING;
        }
        return COLOR_NOTE;
    }
	
	@Override
	public String toString() {
		return "CompilerDiagnosticImpl(file=" + file() + ", line=" + line() + ", column=" + column() + ", start=" + start() + ", end=" + end() + ", message=" + message() + ", kind=" + kind().toString() + ")";
	}

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CompilerDiagnosticImpl) {
            CompilerDiagnosticImpl that = (CompilerDiagnosticImpl) obj;
            return this.file().equals(that.file()) &&
                this.line() == that.line() &&
                this.column() == that.column() &&
                this.start() == that.start() &&
                this.end() == that.end() &&
                this.message().equals(that.message()) &&
                this.kind() == that.kind();
        }
        return false;
    }
}
