/************************************************************************************
 * This file is part of AndroidIDE.
 * 
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/
package com.itsaky.layoutinflater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.ui.util.Preconditions;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.layoutinflater.adapters.android.view.ViewAttrAdapter;
import com.itsaky.layoutinflater.adapters.android.view.ViewGroupAttrAdapter;
import com.itsaky.layoutinflater.impl.BaseView;
import com.itsaky.layoutinflater.impl.UiAttribute;
import com.itsaky.layoutinflater.impl.UiView;
import com.itsaky.layoutinflater.impl.UiViewGroup;
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
    private final IResourceFinder resFinder;
    
    private ContextProvider contextProvider;
    
    private static final Logger LOG = Logger.instance("XMLLayoutInflater");
    public static final String ATTR_ADAPTER_SUFFIX = "AttrAdapter";
    
    private XMLLayoutInflater () {
        throw new UnsupportedOperationException();
    }
    
    XMLLayoutInflater(@NonNull LayoutInflaterConfiguration config) {
        this.resDirs = config.resDirs;
        this.attrInfo = config.attrInfo;
        this.widgetInfo = config.widgetInfo;
        this.resFinder = config.resFinder;
        this.contextProvider = config.contextProvider;
        
        Preconditions.assertAllNotNull("LayoutInflater parameters cannot be null", resDirs, attrInfo, widgetInfo, resFinder);
    }

    @Override
    @NonNull
    protected IView doInflate(String layout, ViewGroup parent) throws InflateException {
        try {
            
            IDTable.newRound();
            
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

        if (!tag.attributes().isEmpty()) {
            addAttributesTo(root, tag.attributes());
        }

        if (root instanceof IViewGroup) {
            addChildren (tag, (IViewGroup) root, parent);
        }
        
        postCreateView(root);

        return root;
    }

    protected void addChildren(@NonNull Element tag, IViewGroup root, ViewGroup parent) {
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

    @NonNull
    private String underscorize(@NonNull String substring) {
        return substring.replace(".", "_");
    }

    protected void registerAttributeAdaptersTo(@NonNull IView root) {
        root.registerAttributeAdapter(onCreateAttributeAdapter (root.asView()));
    }
    
    protected IAttributeAdapter onCreateAttributeAdapter(View view) {
        try {
            final String adapterName = view.getClass().getName().concat(ATTR_ADAPTER_SUFFIX);
            final String name = "com.itsaky.layoutinflater.adapters.".concat(adapterName);
            final Class <? extends IAttributeAdapter> adapterClass = getClass()
                .getClassLoader()
                .loadClass(name)
                .asSubclass(IAttributeAdapter.class);
           return adapterClass.newInstance();
        } catch (Throwable th) {
            LOG.error (BaseApplication.getBaseInstance().getString(com.itsaky.layoutinflater.R.string.err_no_attr_adapter, view.getClass().getName()), th);
        }
        
        // If we cannot find a suitable adapter, fall back to using common adapters
        IAttributeAdapter adapter = null;
        if (view instanceof ViewGroup) {
            adapter = new ViewGroupAttrAdapter ();
        } else {
            adapter = new ViewAttrAdapter ();
        }
        return adapter;
    }

    protected void addAttributesTo(IView view, Attributes attributes) {
        assertNotnull(attributes, "Cannot apply null attributes!");

        for (Attribute attr : attributes) {
            final String key = attr.getKey();
            final String[] split = !key.contains(":") ? new String[] {"", key} : key.split(Pattern.quote(":"), 2);
            final String namespace = split[0];
            final String name = split[1];
            final String value = attr.getValue();

            final IAttribute iAttr = asAttribute (namespace, name, value);
            view.addAttribute(iAttr, resFinder);

            postApplyAttribute(iAttr, view);
        }
    }

    protected IAttribute asAttribute(String namespace, String name, String value) {
        return new UiAttribute (namespace, name, value);
    }

    protected IView create (@NonNull String name, ViewGroup parent, int style) throws InflateException {
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
            LOG.error("Unable to inflate view. widget == null");
            return onCreateErrorView(name, getString(com.itsaky.layoutinflater.R.string.msg_cannot_create_view, name));
        }

        return createFromQualifiedName(widget.name, parent, style);
    }

    protected IView createFromQualifiedName(String name, ViewGroup parent, int style) throws InflateException {
        assertNotBlank(name, "Invalid tag name: " + name);
        
        // TODO Try to load classes directly from .class files if possible
        try {
            final View created = createAndroidViewForName(name);
            final BaseView view =
                created instanceof ViewGroup
                ? new UiViewGroup (name, (ViewGroup) created)
                : new UiView (name, created);
            return applyLayoutParams(view, parent);
        } catch (Throwable th) {
            final var msg = getString(R.string.msg_cannot_create_view, name);
            LOG.error(msg, th);
            return onCreateErrorView(name, msg);
        }
    }

    @NonNull
    protected View createAndroidViewForName(String name) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, java.lang.reflect.InvocationTargetException {
        final Class<? extends View> loaded = Class.forName(name).asSubclass(View.class);
        final Constructor<? extends View> constructor = loaded.getConstructor(Context.class /*, AttributeSet.class, int.class*/);
        final View created = constructor.newInstance(contextProvider.getContext()/*, null, style*/);
        return created;
    }

    protected IView applyLayoutParams(@NonNull IView view, ViewGroup parent) {
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
