package com.itsaky.androidide.language.java.parser.internal;

import androidx.annotation.Nullable;

public interface IJavaDocCommentable extends SuggestItem {
	
	IJavaDocCommentable setHtmlJavaDoc(String javadoc);
	
	@Nullable
	String getJavaDoc();
}
