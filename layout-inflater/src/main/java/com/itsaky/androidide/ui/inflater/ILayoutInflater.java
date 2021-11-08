package com.itsaky.androidide.ui.inflater;

import android.content.Context;
import androidx.annotation.NonNull;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.ui.util.Preconditions;
import com.itsaky.androidide.ui.view.IView;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.view.ViewGroup;

/**
 * A layout inflater which inflates layout from a raw XML file
 *
 * @author Akash Yadav
 */
public abstract class ILayoutInflater {
    
    private static final Pattern XML_DECL = Pattern.compile("<\\?xml.*\\?>");
    
    /**
     * Acatual implementation of the layout inflation
     *
     * @param layout The layout to inflate
     * @return The inflated view
     * @throws InflateException when there was an error inflating the layout
     */
    @NonNull
    protected abstract IView doInflate (String layout, ViewGroup parent) throws InflateException;
    
    /**
     * Reset the {@link ContextProvider} of this inflater
     *
     * @param provider The new {@link ContextProvider}
     */
    public abstract void resetContextProvider (ContextProvider provider);
    
    /**
     * Register the inflate listener to this inflater.
     * <p>
     * NOTE: Do not register too much listeners as they may affect the inflation time.
     * </p>
     *
     * @param listener The listener to register.
     */
    public abstract void registerInflateListener (IInflateListener listener);
    
    /**
     * Unregister the inflate listener if it is registered.
     *
     * @param listener The listener to unregister
     */
    public abstract void unregisterListener (IInflateListener listener);
    
    /**
     * Inflate the layout from the given file path
     *
     * @param path The file path to inflate layout from
     * @return The inflated layout
     * @throws InflateException when there was an error inflating the layout
     */
    @NonNull
    public IView inflatePath (String path, ViewGroup parent) throws InflateException {
        Preconditions.assertNotBlank(path, "Layout file path is blank!");
        
        return inflate(new File(path), parent);
    }
    
    /**
     * Inflate the layout from the given file
     *
     * @param file The file to inflate layout from
     * @return The inflated layout
     * @throws InflateException when there was an error inflating the layout
     */
    @NonNull
    public IView inflate (File file, ViewGroup parent) throws InflateException {
        
        Preconditions.assertNotnull(file, "Cannot inflate null file");
        
        // How can we inflate a non existent file?
        if (!file.exists()) {
            throw new InflateException ("File does not exist!");
        }
        
        if (!file.canRead()) {
            throw new InflateException ("Cannot read file. Permission denied!");
        }
        
        // Cannot inflate a non UTF-8 too. This will also be true if the file is empty.
        if (!FileUtils.isUtf8(file)) {
            throw new InflateException ("File is not UTF-8 or is empty!");
        }
        
        return inflate(FileIOUtils.readFile2String(file), parent);
    }
    
    /**
     * Inflate the layout from the provided XML layout code
     *
     * @param layout The XML layout code
     * @return The inflated layout
     * @throws InflateException when there was an error inflating the layout
     */
    @NonNull
    public IView inflate (String layout, ViewGroup parent) throws InflateException {
        
        Preconditions.assertNotBlank(layout, "Layout code is blank!");
        
        // Trim the layout code
        layout = layout.trim();
        
        final Matcher matcher = XML_DECL.matcher(layout);
        if (matcher.find()) {
            layout = layout.substring(matcher.end()).trim();
        }
        
        return doInflate(layout, parent);
    }
    
    /**
     * Creates an instance of {@code ILayoutInflater}
     *
     * @param config The configuration of the LayoutInflater
     * @return A new layout inflater instance
     */
    @NonNull
    public static ILayoutInflater newInstance (LayoutInflaterConfiguration config) {
        return new XMLLayoutInflater (config);
    }
    
    /**
     * Provides a reference to {@link Context}. This will be used to create original view.
     */
    public static interface ContextProvider {
        
        /**
         * Provide the context to this inflater.
         *
         * @return The context
         */
        Context getContext ();
    }
}
