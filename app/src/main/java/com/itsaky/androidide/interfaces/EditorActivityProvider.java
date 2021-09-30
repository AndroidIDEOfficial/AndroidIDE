package com.itsaky.androidide.interfaces;

import com.itsaky.androidide.EditorActivity;

public interface EditorActivityProvider {
    
    /**
     * Provides a reference to {@link com.itsaky.androidide.EditorActivity EditorActivity}.
     * This is used by LanguageClients to get reference to EditorActivity without keeping a strong reference.
     */
    EditorActivity provide();
}
