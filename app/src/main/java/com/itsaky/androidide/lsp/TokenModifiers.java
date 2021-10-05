package com.itsaky.androidide.lsp;

import org.eclipse.lsp4j.*;

public enum TokenModifiers {
    
	PUBLIC("public"),
	PRIVATE("private"),
	PROTECTED("protected"),
	NATIVE("native"),
	DEFAULT("default"),
	STRICTFP("strictp"),
	SYNCHRONIZED("synchronized"),
	TRANSIENT("transient"),
	VOLATILE("volatile"),
	ABSTRACT(SemanticTokenModifiers.Abstract),
	STATIC(SemanticTokenModifiers.Static),
	FINAL(SemanticTokenModifiers.Readonly),
	
	DEPRECATED(SemanticTokenModifiers.Deprecated);
	
	public final int bitmask = 1 << ordinal();
	public final int invertBitmask = ~bitmask;
	
	private String name;
	
	private TokenModifiers (String name) {
		this.name = name;
	}
	
	@Override
	public String toString () {
		return name;
	}
}
