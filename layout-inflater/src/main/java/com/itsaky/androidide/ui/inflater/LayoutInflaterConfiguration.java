package com.itsaky.androidide.ui.inflater;

import java.io.File;
import java.util.Set;
import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.widgets.WidgetInfo;

/**
 * Configuration for {@link ILayoutInflater}
 */
public class LayoutInflaterConfiguration {
    
    final Set<File> resDirs;
    final AttrInfo attrInfo;
    final WidgetInfo widgetInfo;
    final IResourceFinder resourceProvider;
    final ILayoutInflater.ContextProvider contextProvider;

    public LayoutInflaterConfiguration(Set<File> resDirs, AttrInfo attrInfo, WidgetInfo widgetInfo, IResourceFinder resourceProvider, ILayoutInflater.ContextProvider contextProvider) {
        this.resDirs = resDirs;
        this.attrInfo = attrInfo;
        this.widgetInfo = widgetInfo;
        this.resourceProvider = resourceProvider;
        this.contextProvider = contextProvider;
    }
    
    /**
     * A class that builds a {@link LayoutInflaterConfiguration}
     */
    public static class Builder {
        
        private Set<File> resDirs;
        private AttrInfo attrInfo;
        private WidgetInfo widgetInfo;
        private IResourceFinder resourceProvider;
        private ILayoutInflater.ContextProvider provider;
        
        public Builder setResourceDirectories (Set<File> dirs) {
            this.resDirs = dirs;
            return this;
        }
        
        public Builder setAttrInfo (AttrInfo info) {
            this.attrInfo = info;
            return this;
        }
        
        public Builder setWidgetInfo (WidgetInfo info) {
            this.widgetInfo = info;
            return this;
        }
        
        public Builder setResourceProvider (IResourceFinder provider) {
            this.resourceProvider = provider;
            return this;
        }
        
        public Builder setContextProvider (ILayoutInflater.ContextProvider provider) {
            this.provider = provider;
            return this;
        }
        
        public LayoutInflaterConfiguration create() {
            return new LayoutInflaterConfiguration(resDirs, attrInfo, widgetInfo, resourceProvider, provider);
        }
    }
}
