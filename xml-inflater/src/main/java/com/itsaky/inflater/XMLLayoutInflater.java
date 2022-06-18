/*
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
 */
package com.itsaky.inflater;

import static com.itsaky.inflater.util.Preconditions.assertNotBlank;
import static com.itsaky.inflater.util.Preconditions.assertNotnull;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.blankj.utilcode.util.ReflectUtils;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.inflater.adapters.android.view.ViewAttrAdapter;
import com.itsaky.inflater.adapters.android.view.ViewGroupAttrAdapter;
import com.itsaky.inflater.impl.BaseView;
import com.itsaky.inflater.impl.ErrorUiView;
import com.itsaky.inflater.impl.IncludeLayout;
import com.itsaky.inflater.impl.UiAttribute;
import com.itsaky.inflater.impl.UiView;
import com.itsaky.inflater.impl.UiViewGroup;
import com.itsaky.inflater.util.Preconditions;
import com.itsaky.widgets.WidgetInfo;
import com.itsaky.widgets.models.Widget;
import com.itsaky.xml.INamespace;
import com.itsaky.xml.impl.UiNamespace;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

class XMLLayoutInflater extends BaseLayoutInflater {

  private final Set<File> resDirs;
  private final AttrInfo attrInfo;
  private final WidgetInfo widgetInfo;
  private final IResourceTable resFinder;

  private ContextProvider contextProvider;

  private static final ILogger LOG = ILogger.newInstance("XMLLayoutInflater");
  public static final String ATTR_ADAPTER_SUFFIX = "AttrAdapter";

  XMLLayoutInflater(@NonNull LayoutInflaterConfiguration config) {
    this.resDirs = config.resDirs;
    this.attrInfo = config.attrInfo;
    this.widgetInfo = config.widgetInfo;
    this.resFinder = config.resFinder;
    this.contextProvider = config.contextProvider;

    Preconditions.assertAllNotNull(
        "LayoutInflater parameters cannot be null", resDirs, attrInfo, widgetInfo, resFinder);
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
        throw new InflateException("No views added");
      }

      if (doc.childrenSize() > 1) {
        throw new InflateException(
            "More than one root element was found. An XML layout can have only one root"
                + " element.");
      }

      IView root = onCreateView(doc.child(0), parent, null);

      if (root == null) {
        root = onCreateErrorView(TextView.class.getName(), "No views added", parent);
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

  @NonNull
  @Override
  protected IResourceTable requireResourceTable() {
    return this.resFinder;
  }

  protected IView onCreateView(Element tag, ViewGroup parentGroup, IViewGroup parent)
      throws InflateException {
    if (tag == null) {
      return null;
    }

    final String name = tag.tagName().trim();

    if (name.equals("include")) {
      return createFromInclude(tag.attributes(), parent);
    }

    final IView root;
    int style = tag.hasAttr("style") ? parseFrameworkStyle(tag.attr("style")) : -1;
    if (!name.contains(".")) {
      root = createFromSimpleName(name, parentGroup, style);
    } else {
      root = createFromQualifiedName(name, parentGroup, style);
    }

    if (root == null) {
      return null;
    }

    registerAttributeAdaptersTo(root);

    if (!tag.attributes().isEmpty()) {
      addAttributesTo(root, tag.attributes(), parent);
    }

    if (root instanceof IViewGroup) {
      addChildren(tag, (IViewGroup) root);
    }

    postCreateView(root);

    return root;
  }

  protected void addChildren(@NonNull Element tag, IViewGroup parent) {
    if (tag.childrenSize() > 0 && !parent.isPlaceholder()) {
      for (Element child : tag.children()) {
        final IView created = onCreateView(child, (ViewGroup) parent.asView(), parent);
        if (created != null) {
          parent.addView(created);
          created.setParent(parent);
        }
      }
    }
  }

  protected int parseFrameworkStyle(String value) throws InflateException {
    try {
      final String name = underscorize(value.substring(value.indexOf('/') + 1));
      return (int) android.R.attr.class.getField(name).get(null);
    } catch (Throwable th) {
      LOG.error("Unable to parse framework style", th);
      return -1;
    }
  }

  @NonNull
  private String underscorize(@NonNull String substring) {
    return substring.replace(".", "_");
  }

  protected void registerAttributeAdaptersTo(@NonNull IView root) {
    root.registerAttributeAdapter(onCreateAttributeAdapter(root.asView()));
  }

  protected IAttributeAdapter onCreateAttributeAdapter(View view) {
    final var displayMetrics = contextProvider.getContext().getResources().getDisplayMetrics();
    try {
      final String adapterName = view.getClass().getName().concat(ATTR_ADAPTER_SUFFIX);
      final String name = "com.itsaky.inflater.adapters.".concat(adapterName);
      final Class<? extends IAttributeAdapter> adapterClass =
          getClass().getClassLoader().loadClass(name).asSubclass(IAttributeAdapter.class);
      final var constructor =
          adapterClass.getConstructor(IResourceTable.class, DisplayMetrics.class);
      final var adapter = constructor.newInstance(resFinder, displayMetrics);
      adapter.setResourceFinder(resFinder);
      return adapter;
    } catch (Throwable th) {
      LOG.error(
          BaseApplication.getBaseInstance()
              .getString(
                  com.itsaky.inflater.R.string.err_no_attr_adapter, view.getClass().getName()),
          th);
    }

    // If we cannot find a suitable adapter, fall back to using common adapters
    IAttributeAdapter adapter;
    if (view instanceof ViewGroup) {
      adapter = new ViewGroupAttrAdapter(resFinder, displayMetrics);
    } else {
      adapter = new ViewAttrAdapter(resFinder, displayMetrics);
    }
    return adapter;
  }

  protected void addAttributesTo(IView view, Attributes attributes, IViewGroup parent) {
    assertNotnull(attributes, "Cannot apply null attributes!");

    for (Attribute attr : attributes) {
      final String key = attr.getKey();
      final String[] split =
          !key.contains(":") ? new String[] {"", key} : key.split(Pattern.quote(":"), 2);
      final String namespace = split[0];
      final String name = split[1];
      final String value = attr.getValue();

      INamespace ns;
      if (namespace.equals("xmlns")) {
        ns = INamespace.DECLARATOR;
        view.registerNamespace(new UiNamespace(name, value));
      } else {
        // set the parent so that we could look for
        // declared namespaces
        view.setParent(parent);
        ns = view.findRegisteredNamespace(namespace);
      }

      if (ns == null) {
        ns = INamespace.invalid(namespace);
      }

      final IAttribute iAttr = asAttribute(ns, name, value);
      view.addAttribute(iAttr);

      postApplyAttribute(iAttr, view);
    }
  }

  protected IAttribute asAttribute(INamespace namespace, String name, String value) {
    return new UiAttribute(namespace, name, value);
  }

  protected IView create(@NonNull String name, ViewGroup parent, int style)
      throws InflateException {
    if (!name.contains(".")) {
      return createFromSimpleName(name, parent, style);
    } else {
      return createFromQualifiedName(name, parent, style);
    }
  }

  protected IView createFromInclude(Attributes attrs, IViewGroup parent) {

    if (!attrs.hasKey("layout")) {
      throw new InflateException("<include> tag doesn't have 'layout' attribute.");
    }

    final var layout = attrs.get("layout");
    Preconditions.assertNotBlank(
        layout,
        "'layout' attribute for <include> tag has invalid value." + "layout='" + layout + "'");

    if (layout.startsWith("@android:layout/")) {
      return inflateAndroidLayout(layout.substring("@android:layout/".length()), attrs, parent);
    }

    final var name = layout.substring("@layout/".length());
    final var file = resFinder.findLayout(name);
    if (file == null) {
      throw new InflateException("Layout file '" + name + "' not found");
    }

    return inflateLocalIncludeLayout(attrs, parent, name, file);
  }

  @NonNull
  private IncludeLayout inflateLocalIncludeLayout(
      Attributes attrs, IViewGroup parent, String name, File file) {

    // DesignerActivity applies drag listeners in 'onInflateView()' call
    // But, <include> layouts must not be edited here
    // So, we avoid notifying the listeners
    super.notify = false;

    final var view = this.inflate(file, (ViewGroup) parent.asView());
    Preconditions.assertNotnull(view, "Failed to inflate '" + name + "'");

    final var includeLayout = new IncludeLayout(view);

    applyLayoutParams(includeLayout, (ViewGroup) parent.asView());
    addAttributesTo(includeLayout, attrs, parent);

    // Enable notifications
    super.notify = true;

    return includeLayout;
  }

  protected IView inflateAndroidLayout(String name, Attributes attrs, @NonNull IViewGroup parent) {
    final var id = (int) ReflectUtils.reflect(android.R.layout.class).field(name).get();
    final var view = LayoutInflater.from(contextProvider.getContext()).inflate(id, null);
    final var layout = new IncludeLayout(new UiView(IncludeLayout.TAG, view, false));

    applyLayoutParams(layout, (ViewGroup) parent.asView());
    addAttributesTo(layout, attrs, parent);

    return layout;
  }

  protected IView createFromSimpleName(String name, ViewGroup parent, int style)
      throws InflateException {
    final Widget widget = widgetInfo.getWidgetBySimpleName(name);
    if (widget == null) {
      LOG.error("Unable to inflate view. widget == null");
      return onCreateErrorView(
          name, getString(com.itsaky.inflater.R.string.msg_cannot_create_view, name), parent);
    }

    return createFromQualifiedName(widget.name, parent, style);
  }

  protected IView createFromQualifiedName(String name, ViewGroup parent, int style)
      throws InflateException {
    assertNotBlank(name, "Invalid tag name: " + name);

    // TODO Try to load classes directly from .class files if possible
    try {

      final var androidView = this.widgetInfo.getWidget(name);
      if (androidView == null) {
        // If this is not an Android view, do not bother to create one
        throw new NotSupportedException();
      }

      final View created = createAndroidViewForName(name, style);
      final BaseView view =
          created instanceof ViewGroup
              ? new UiViewGroup(name, (ViewGroup) created)
              : new UiView(name, created);
      return applyLayoutParams(view, parent);
    } catch (Throwable th) {

      if (th instanceof NotSupportedException) {
        return onCreateErrorView(name, getString(R.string.msg_view_not_supported, name), parent);
      }

      final var msg = getString(R.string.msg_cannot_create_view, name);
      LOG.error(msg, th);
      return onCreateErrorView(name, msg, parent);
    }
  }

  @NonNull
  protected View createAndroidViewForName(String name, int style)
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
          InstantiationException, java.lang.reflect.InvocationTargetException {
    final Class<? extends View> loaded = Class.forName(name).asSubclass(View.class);
    final Constructor<? extends View> constructor;
    if (style == -1) {
      constructor = loaded.getConstructor(Context.class);
      return constructor.newInstance(contextProvider.getContext());
    } else {
      constructor = loaded.getConstructor(Context.class, AttributeSet.class, int.class);
      return constructor.newInstance(contextProvider.getContext(), null, style);
    }
  }

  protected IView applyLayoutParams(@NonNull IView view, ViewGroup parent) {
    final ViewGroup.LayoutParams params = generateLayoutParams(parent);
    view.asView().setLayoutParams(params);
    return view;
  }

  protected IView onCreateErrorView(String name, String msg, final ViewGroup parent)
      throws InflateException {
    final var view =
        ErrorUiView.create(
            Objects.requireNonNull(contextProvider.getContext(), "Context is null."), name, msg);
    return applyLayoutParams(view, parent);
  }

  private String getString(@StringRes int id, Object... format) throws InflateException {
    assertNotnull(contextProvider.getContext(), "Context is null!");
    return contextProvider.getContext().getString(id, format);
  }

  @NonNull
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup parent) {
    try {
      Class<?> clazz = parent.getClass();
      Method method = null;

      while (true) {
        try {
          method = clazz.getDeclaredMethod("generateDefaultLayoutParams");
          break;
        } catch (Throwable e) {
          /* ignored */
        }
        clazz = clazz.getSuperclass();

        if (clazz == null) {
          break;
        }
      }

      if (method != null) {
        method.setAccessible(true);
        return (ViewGroup.LayoutParams) method.invoke(parent);
      }

      LOG.error("Unable to create default params for view parent:", parent);
      return new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    } catch (Throwable th) {
      throw new InflateException("Unable to create layout params for parent: " + parent, th);
    }
  }
}
