package com.itsaky.androidide.ui.inflater;

import android.widget.TextView;
import androidx.annotation.NonNull;
import com.itsaky.androidide.ui.parser.ResourceTable;
import com.itsaky.androidide.ui.parser.ResourceTableFactory;
import com.itsaky.androidide.ui.util.ViewCreator;
import com.itsaky.androidide.ui.view.IView;
import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.widgets.WidgetInfo;
import java.io.File;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

class XMLLayoutInflater extends ILayoutInflater {
    
    private final Set<File> resDirs;
    private final AttrInfo attrInfo;
    private final WidgetInfo widgetInfo;
    private final ResourceTable frameworkResourceTable;
    private final IResourceFinder resourceProvider;
    
    private XMLLayoutInflater () {
        throw new UnsupportedOperationException("Cannot instantiate inflater");
    }
    
    XMLLayoutInflater(LayoutInflaterConfiguration config) {
        this.resDirs = config.resDirs;
        this.attrInfo = config.attrInfo;
        this.widgetInfo = config.widgetInfo;
        this.resourceProvider = config.resourceProvider;
        this.frameworkResourceTable = ResourceTableFactory.newFrameworkResourceTable();
        
        if (frameworkResourceTable == null) {
            throw new InflateException ("Cannot create android framework resource table");
        }
        
        ViewCreator.init(widgetInfo, frameworkResourceTable, resourceProvider, config.contextProvider);
    }

    @Override
    @NonNull
    protected IView doInflate(String layout) throws InflateException {
        try {
            final Document doc = Jsoup.parse(layout, "", Parser.xmlParser());
            
            if (doc.childrenSize() <= 0) {
                throw new InflateException ("No views added");
            }
            
            if (doc.childrenSize() > 1) {
                throw new InflateException ("More than one root element was found. An XML layout can have only one root element.");
            }
            
            IView root = ViewCreator.create(doc.child(0), null);
            
            if (root == null) {
                root = ViewCreator.createErrorView(TextView.class.getName(), "No views added", null);
            }
            
            return root;
        } catch (Throwable th) {
            throw new InflateException("Unable to inflate layout", th);
        }
    }

    @Override
    public void resetContextProvider(ILayoutInflater.ContextProvider provider) {
        ViewCreator.resetContextProvider (provider);
    }
}
