package io.github.rosemoe.editor.struct;

public final class ErrorOrWarning {
	
	private final int line;
	private final boolean isError;
	private final String message;

	private ErrorOrWarning(int line, boolean isError, String message) {
		this.line = line;
		this.isError = isError;
		this.message = message;
	}

	public static ErrorOrWarning forError(int line, String message) {
		return new ErrorOrWarning(line, true, message);
	}

	public static ErrorOrWarning forWarning(int line, String message) {
		return new ErrorOrWarning(line, false, message);
	}

	public int getLine() {
		return line;
	}

	public String getMessage() {
		return message;
	}

	public boolean isError() {
		return isError;
	}

	public boolean isWarning() {
		return isError == false;
	}

	@Override
	public String toString() {
		return "ErrorOrWarning(line=" + line + ", isError=" + isError + ", message=" + message + ")";
	}
}
