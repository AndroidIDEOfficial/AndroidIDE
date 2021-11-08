package com.itsaky.androidide.ui.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import com.itsaky.androidide.ui.inflater.ILayoutInflater;
import com.itsaky.androidide.ui.inflater.IResourceFinder;
import com.itsaky.androidide.ui.inflater.InflateException;
import com.itsaky.androidide.ui.parser.ResourceTable;
import com.itsaky.androidide.ui.view.IAttribute;
import com.itsaky.androidide.ui.view.IAttributeAdapter;
import com.itsaky.androidide.ui.view.IView;
import com.itsaky.androidide.ui.view.IViewGroup;
import com.itsaky.androidide.ui.view.adapters.BaseViewAttrAdapter;
import com.itsaky.androidide.ui.view.adapters.BaseViewGroupAttrAdapter;
import com.itsaky.androidide.ui.view.adapters.ButtonAttrAdapter;
import com.itsaky.androidide.ui.view.adapters.LinearLayoutAttrAdapter;
import com.itsaky.androidide.ui.view.adapters.TextViewAttrAdapter;
import com.itsaky.androidide.ui.view.impl.UiAttribute;
import com.itsaky.androidide.ui.view.impl.UiView;
import com.itsaky.androidide.ui.view.impl.UiViewGroup;
import com.itsaky.widgets.WidgetInfo;
import com.itsaky.widgets.models.Widget;
import java.lang.reflect.Constructor;
import java.util.regex.Pattern;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import static com.itsaky.androidide.ui.util.Preconditions.*;

public class ViewCreator {
    
    private static WidgetInfo widgetInfo;
    private static ResourceTable frameworkResourceTable;
    private static IResourceFinder resFinder;
    private static ILayoutInflater.ContextProvider contextProvider;
    
    private static final IAttributeAdapter[] ATTR_ADAPTERS = {
        
        // Base adapters
        new BaseViewAttrAdapter(),
        new BaseViewGroupAttrAdapter(),
        
        // Widget adapters
        new TextViewAttrAdapter(),
        new ButtonAttrAdapter(),
        
        // ViewGroup adapters
        new LinearLayoutAttrAdapter()
    };

    public static void resetContextProvider(ILayoutInflater.ContextProvider provider) {
        Preconditions.assertNotnull(provider, "Context provider is null");
        contextProvider = provider;
    }
    
    public static void init (WidgetInfo info, ResourceTable frameworkRes, IResourceFinder finder, ILayoutInflater.ContextProvider provider) throws InflateException {
        assertNotnull(info, "Widget data is null!");
        assertNotnull(provider, "Context provider is null!");
        
        widgetInfo = info;
        frameworkResourceTable = frameworkRes;
        resFinder = finder;
        contextProvider = provider;
    }
    
    private static void checkInitialized () throws InflateException {
        if(widgetInfo == null || resFinder == null || contextProvider == null) {
            throw new InflateException ("Creator not initialized!");
        }
    }
    
    public static IView create (Element tag, IViewGroup parent) throws InflateException {
        if (tag == null) {
            return null;
        }
        
        checkInitialized();
        
        final String name = tag.tagName().trim();
        
        if (name.equals("include")) {
            return createFromInclude (tag.attributes(), parent);
        }
        
        IView root = null;
        int style = tag.hasAttr("style") ? parseFrameworkStyle (tag.attr("style")) : 0;
        if (!name.contains(".")) {
            root = createFromSimpleName (name, parent, style);
        } else {
            root = createFromQualifiedName (name, parent, style);
        }
        
        if (root == null) {
            return null;
        }
        
        registerAttributeAdaptersTo (root);
        
        if (tag.attributes() != null && !tag.attributes().isEmpty()) {
            addAtrributesTo(root, tag.attributes());
        }
        
        if (tag.childrenSize() > 0
        && root instanceof IViewGroup
        && !root.isPlaceholder()) {
            final IViewGroup thisParent = (IViewGroup) root;
            for (Element child : tag.children()) {
                final IView created = ViewCreator.create(child, thisParent);
                if(created != null) {
                    thisParent.addView(created);
                }
            }
        }
        
        return root;
    }
    
    private static int parseFrameworkStyle (String value) throws InflateException{
        
        try {
            if (value.startsWith("?android:attr/")) {
                final String name = underscorize (value.substring("?android:attr/".length()));
                return (int) android.R.attr.class.getField(name).get(null);
            } else if (value.startsWith("@android:style/")) {
                final String name = underscorize (value.substring("@android:style/".length()));
                return (int) android.R.style.class.getField(name).get(null);
            }
        } catch (Throwable th) {
            throw new InflateException (th);
        }
        
        return 0;
    }

    private static String underscorize(String substring) {
        return substring.replace(".", "_");
    }

    private static void registerAttributeAdaptersTo(IView root) {
        for (IAttributeAdapter adapter : ATTR_ADAPTERS) {
            root.registerAttributeAdapter(adapter);
        }
    }

    private static void addAtrributesTo(IView view, Attributes attributes) {
        assertNotnull(attributes, "Cannot apply null attributes!");
        
        for (Attribute attr : attributes) {
            final String key = attr.getKey();
            final String[] split = !key.contains(":") ? new String[] {"", key} : key.split(Pattern.quote(":"), 2);
            final String namespace = split[0];
            final String name = split[1];
            final String value = attr.getValue();
            
            view.addAttribute(asAttribute (namespace, name, value), resFinder);
        }
    }

    private static IAttribute asAttribute(String namespace, String name, String value) {
        return new UiAttribute (namespace, name, value);
    }
    
    public static IView create (String name, IViewGroup parent, int style) throws InflateException {
        if (!name.contains(".")) {
            return createFromSimpleName (name, parent, style);
        } else {
            return createFromQualifiedName (name, parent, style);
        }
    }
    
    public static IView createFromInclude (Attributes attrs, IViewGroup parent) {
        throw new UnsupportedOperationException ("Inflating from <include> is not supported yet!");
    }
    
    public static IView createFromSimpleName(String name, IViewGroup parent, int style) throws InflateException {
        checkInitialized();
        
        final Widget widget = widgetInfo.getWidgetBySimpleName(name);
        if (widget == null) {
            return createErrorView(name, getString(com.itsaky.androidide.ui.inflater.R.string.msg_cannot_create_view, name), parent);
        }
        
        return createFromQualifiedName(widget.name, parent, style);
    }
    
    public static IView createFromQualifiedName(String name, IViewGroup parent, int style) throws InflateException {
        checkInitialized();
        assertNotBlank(name, "Invalid tag name: " + name);
        
        // TODO Try to load classes directly from .class files
        try {
            final Class<? extends View> loaded = Class.forName(name).asSubclass(View.class);
            final Constructor<? extends View> constructor = loaded.getConstructor(Context.class, AttributeSet.class, int.class);
            final View created = constructor.newInstance(contextProvider.getContext(), null, style);
            return applyLayoutParams(created instanceof ViewGroup ? new UiViewGroup (name, (ViewGroup) created, parent) : new UiView (name, created, parent));
        } catch (Throwable th) {
            return createErrorView(name, "Cannot create view for: " + name, parent);
        }
    }

    private static IView applyLayoutParams(IView view) {
        final ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams (-2, -2);
        view.asView().setLayoutParams(params);
        return view;
    }
    
    public static IView createErrorView (String name, String msg, IViewGroup parent) throws InflateException {
        
        checkInitialized();
        
        final Context ctx = contextProvider.getContext();
        assertNotnull(ctx, "Context is null!");
        
        final TextView error = new TextView (ctx);
        error.setText(msg);
        error.setLayoutParams(new ViewGroup.LayoutParams (-2, -2));
        error.setTextColor(ContextCompat.getColor(ctx, android.R.color.black));
        error.setBackgroundColor(ContextCompat.getColor(ctx, android.R.color.white));
        
        return new UiView (name, error, parent, true);
    }
    
    private static String getString (@StringRes int id, Object... format) throws InflateException {
        assertNotnull(contextProvider.getContext(), "Context is null!");
        return contextProvider.getContext().getString(id, format);
    }
}
