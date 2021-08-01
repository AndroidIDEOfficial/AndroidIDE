package com.itsaky.androidide.utils;

import com.itsaky.androidide.models.SuggestItem;
import io.github.rosemoe.editor.struct.CompletionItem;

public class EitherOfResult extends Either<SuggestItem, CompletionItem> {
	
	public EitherOfResult(SuggestItem left, CompletionItem right) {
		super(left, right);
	}
}
