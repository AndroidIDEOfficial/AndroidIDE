package com.itsaky.androidide.ui.inflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import com.itsaky.androidide.ui.resources.ResourceTable;
import com.itsaky.androidide.ui.resources.ResourceTableFactory;
import com.itsaky.androidide.ui.util.Preconditions;
import com.itsaky.androidide.ui.view.IAttribute;
import com.itsaky.androidide.ui.view.IAttributeAdapter;
import com.itsaky.androidide.ui.view.IView;
import com.itsaky.androidide.ui.view.IViewGroup;
import com.itsaky.androidide.ui.view.adapters.BaseViewAttrAdapter;
import com.itsaky.androidide.ui.view.adapters.BaseViewGroupAttrAdapter;
import com.itsaky.androidide.ui.view.adapters.ButtonAttrAdapter;
import com.itsaky.androidide.ui.view.adapters.LinearLayoutAttrAdapter;
import com.itsaky.androidide.ui.view.adapters.TextViewAttrAdapter;
import com.itsaky.androidide.ui.view.impl.BaseView;
import com.itsaky.androidide.ui.view.impl.UiAttribute;
import com.itsaky.androidide.ui.view.impl.UiView;
import com.itsaky.androidide.ui.view.impl.UiViewGroup;
import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.widgets.WidgetInfo;
import com.itsaky.widgets.models.Widget;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import static com.itsaky.androidide.ui.util.Preconditions.*;

class XMLLayoutInflater extends BaseLayoutInflater {
    
    private final Set<File> resDirs;
    private final AttrInfo attrInfo;
    private final WidgetInfo widgetInfo;
    private final ResourceTable frameworkResourceTable;
    private final IResourceFinder resFinder;
    
    private ContextProvider contextProvider;
    
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
    
    private XMLLayoutInflater () {
        throw new UnsupportedOperationException("Cannot instantiate inflater");
    }
    
    XMLLayoutInflater(LayoutInflaterConfiguration config) {
        this.resDirs = config.resDirs;
        this.attrInfo = config.attrInfo;
        this.widgetInfo = config.widgetInfo;
        this.resFinder = config.resFinder;
        this.contextProvider = config.contextProvider;
        this.frameworkResourceTable = ResourceTableFactory.newFrameworkResourceTable();
        
        Preconditions.assertAllNotNull("LayoutInflater parameters cannot be null", resDirs, attrInfo, widgetInfo, resFinder, frameworkResourceTable);
    }

    @Override
    @NonNull
    protected IView doInflate(String layout, ViewGroup parent) throws InflateException {
        try {
            
            // Notify
            preInflate();
            
            final Document doc = Jsoup.parse(layout, "", Parser.xmlParser());
            
            if (doc.childrenSize() <= 0) {
                throw new InflateException ("No views added");
            }
            
            if (doc.childrenSize() > 1) {
                throw new InflateException ("More than one root element was found. An XML layout can have only one root element.");
            }
            
            IView root = onCreateView(doc.child(0), parent);
            
            if (root == null) {
                root = onCreateErrorView(TextView.class.getName(), "No views added");
            }
            
            // Notify
            postInflate(root);
            
            return root;
        } catch (Throwable th) {
            throw new InflateException("Unable to inflate layout", th);
        }
    }

    @Override
    public void resetContextProvider(ILayoutInflater.ContextProvider provider) {
        this.contextProvider = provider;
    }
    
    protected IView onCreateView (Element tag, ViewGroup parent) throws InflateException {
        if (tag == null) {
            return null;
        }
        
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

        if (root instanceof IViewGroup) {
            addChildren (tag, (IViewGroup) root, parent);
        }
        
        postCreateView(root);

        return root;
    }

    protected void addChildren(Element tag, IViewGroup root, ViewGroup parent) {
        if (tag.childrenSize() > 0 && !root.isPlaceholder()) {
            for (Element child : tag.children()) {
                final BaseView created = (BaseView) onCreateView(child, (ViewGroup) root.asView());
                if(created != null) {
                    root.addView(created);
                    created.setParent(root);
                }
            }
        }
    }

    protected int parseFrameworkStyle (String value) throws InflateException{

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

    private String underscorize(String substring) {
        return substring.replace(".", "_");
    }

    protected void registerAttributeAdaptersTo(IView root) {
        for (IAttributeAdapter adapter : ATTR_ADAPTERS) {
            root.registerAttributeAdapter(adapter);
        }
    }

    protected void addAtrributesTo(IView view, Attributes attributes) {
        assertNotnull(attributes, "Cannot apply null attributes!");

        for (Attribute attr : attributes) {
            final String key = attr.getKey();
            final String[] split = !key.contains(":") ? new String[] {"", key} : key.split(Pattern.quote(":"), 2);
            final String namespace = split[0];
            final String name = split[1];
            final String value = attr.getValue();

            final IAttribute iAttr = asAttribute (namespace, name, value);
            view.addAttribute(iAttr, resFinder);
        }
    }

    protected IAttribute asAttribute(String namespace, String name, String value) {
        return new UiAttribute (namespace, name, value);
    }

    protected IView create (String name, ViewGroup parent, int style) throws InflateException {
        if (!name.contains(".")) {
            return createFromSimpleName (name, parent, style);
        } else {
            return createFromQualifiedName (name, parent, style);
        }
    }

    protected IView createFromInclude (Attributes attrs, ViewGroup parent) {
        throw new UnsupportedOperationException ("Inflating from <include> is not supported yet!");
    }

    protected IView createFromSimpleName(String name, ViewGroup parent, int style) throws InflateException {
        final Widget widget = widgetInfo.getWidgetBySimpleName(name);
        if (widget == null) {
            return onCreateErrorView(name, getString(com.itsaky.androidide.ui.inflater.R.string.msg_cannot_create_view, name));
        }

        return createFromQualifiedName(widget.name, parent, style);
    }

    protected IView createFromQualifiedName(String name, ViewGroup parent, int style) throws InflateException {
        assertNotBlank(name, "Invalid tag name: " + name);
        
        // TODO Try to load classes directly from .class files
        try {
            final Class<? extends View> loaded = Class.forName(name).asSubclass(View.class);
            final Constructor<? extends View> constructor = loaded.getConstructor(Context.class, AttributeSet.class, int.class);
            final View created = constructor.newInstance(contextProvider.getContext(), null, style);
            final BaseView view =
                created instanceof ViewGroup
                ? new UiViewGroup (name, (ViewGroup) created)
                : new UiView (name, created);
            return applyLayoutParams(view, parent);
        } catch (Throwable th) {
            return onCreateErrorView(name, "Cannot create view for: " + name);
        }
    }

    protected IView applyLayoutParams(IView view, ViewGroup parent) {
        final ViewGroup.LayoutParams params = generateLayoutParams(parent);
        view.asView().setLayoutParams(params);
        return view;
    }

    protected IView onCreateErrorView (String name, String msg) throws InflateException {
        
        final Context ctx = contextProvider.getContext();
        assertNotnull(ctx, "Context is null!");

        final TextView error = new TextView (ctx);
        error.setText(msg);
        error.setLayoutParams(new ViewGroup.LayoutParams (-2, -2));
        error.setTextColor(ContextCompat.getColor(ctx, android.R.color.black));
        error.setBackgroundColor(ContextCompat.getColor(ctx, android.R.color.white));

        return new UiView (name, error, true);
    }

    private String getString (@StringRes int id, Object... format) throws InflateException {
        assertNotnull(contextProvider.getContext(), "Context is null!");
        return contextProvider.getContext().getString(id, format);
    }
    
    @NonNull
    protected ViewGroup.LayoutParams generateLayoutParams (ViewGroup parent) {
        try {
            final Class<?> clazz = parent.getClass();
            final Method method = clazz.getDeclaredMethod("generateDefaultLayoutParams");
            method.setAccessible(true);
            return (ViewGroup.LayoutParams) method.invoke(parent);
        } catch (Throwable th) {
            throw new InflateException ("Unable to create layout params for parent: " + parent, th);
        }
    }
}
