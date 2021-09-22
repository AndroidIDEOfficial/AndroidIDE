package com.itsaky.androidide.handlers;

import com.itsaky.androidide.EditorActivity;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.project.IDEProject;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.handlers.jls.JLSHandler;

/**
 * A handler is an implementation that handles a specific feature, data or anything else in AndroidIDE
 */
public abstract class IDEHandler {
    
    protected static final Logger LOG = Logger.instance();
    
    private Provider provider;
    
    /**
     * A convinient method to throw exception when trying to access a disabled handler
     * >> Should only be used by subclasses <<
     */
    protected void throwDisabled() {
        throw new NotEnabledExeption();
    }
    
    protected void throwNPE () {
        throw new NullPointerException();
    }
    
    public void start(Provider provider) {
        this.provider = provider;
    }
    
    public void stop() {
        
    }
    
    /**
     * An interface to communicate between a handler and its client
     */
    public static interface Provider {
        
        /**
         * Called by handler to get a reference to {@link EditorActivity}
         *
         * @throws NullPointerException is this is required
         */
        EditorActivity provideEditorActivity();
        
        /**
         * Called by handler to get a reference of the current {@link AndroidProject}
         *
         * @throws NullPointerException is this is required
         */
        AndroidProject provideAndroidProject();
        
        /**
         * Called by handler to get a reference of the current {@link IDEProject}
         *
         * @throws NullPointerException is this is required
         */
        IDEProject provideIDEProject();
        
        /**
         * Called by handler to get a reference of the current {@link IDEProject}
         *
         * @throws NullPointerException is this is required
         */
        JLSHandler provideJLSHandler();
    }
    
    public static class NotEnabledExeption extends RuntimeException {

        @Override
        public String getMessage() {
            return "This hanadler is not enabled!";
        }
    }
}
