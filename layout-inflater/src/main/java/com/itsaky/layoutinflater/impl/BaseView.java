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
package com.itsaky.layoutinflater.impl;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.utils.Logger;
import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IAttributeAdapter;
import com.itsaky.layoutinflater.IResourceFinder;
import com.itsaky.layoutinflater.IView;
import com.itsaky.layoutinflater.IViewGroup;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public abstract class BaseView implements IView {

    protected final Set<IAttribute> attributes = new TreeSet<IAttribute>(IAttribute.COMPARATOR);
    protected final Set<IAttributeAdapter> attrAdapters = new HashSet<>();

    protected final String qualifiedName;
    protected final View view;
    protected IViewGroup parent;
    protected Object stored;
    private boolean isPlaceholder;

    protected final Logger LOG = Logger.instance(getClass().getSimpleName());

    public BaseView(String qualifiedName, View view) {
        this(qualifiedName, view, false);
    }

    public BaseView(String qualifiedName, View view, boolean isPlaceholder) {
        this.qualifiedName = qualifiedName;
        this.view = view;
        this.isPlaceholder = isPlaceholder;
    }

    @Override
    public void setParent (IViewGroup parent) {
        this.parent = parent;
    }

    public void setPlaceholder (boolean placeholder) {
        this.isPlaceholder = placeholder;
    }

    @Override
    public boolean isPlaceholder() {
        return isPlaceholder;
    }

    @Override
    public View asView() {
        return view;
    }

    @Override
    public IViewGroup getParent() {
        return parent;
    }

    @Override
    public boolean removeFromParent() {
        if (getParent() != null) {
            getParent().removeView(this);
            return true;
        }

        LOG.info("View does not have a parent. Cannot remove.");
        return false;
    }

    @Override
    public void addAttribute(IAttribute attr, IResourceFinder resFinder) {
        if (attr == null || this.attributes.contains(attr)) {
            return;
        }

        this.attributes.add(attr);

        for (IAttributeAdapter adapter : attrAdapters) {
            if (adapter.isApplicableTo(asView())
                && adapter.apply(attr, asView(), resFinder)) {
                    break;
            }
        }
    }

    @Override
    public void removeAttribute(IAttribute attr) {
        this.attributes.remove(attr);
    }

    @Override
    public void removeAttributeAt(int index) {
        this.attributes.remove(index);
    }

    @Override
    public Set<IAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public IAttribute[] getAttrArray() {
        return getAttributes().toArray(new IAttribute[0]);
    }

    @Override
    public void registerAttributeAdapter(IAttributeAdapter adapter) {
        if (adapter == null) {
            return;
        }

        this.attrAdapters.add(adapter);
    }

    protected Set<IAttributeAdapter> getAttributeAdapters () {
        return attrAdapters;
    }

    @Override
    public void setExtraData(Object data) {
        this.stored = data;
    }

    @Nullable
    @Override
    public Object getExtraData() {
        return this.stored;
    }

    @Override
    public String getXmlTag() {
        return Objects.requireNonNull(asView()).getClass().getSimpleName();
    }

    /**
     * Generate the XML layout code for this view.
     * <p>
     *     NOTE: To avoid writing same logic in multiple files,
     *     this implementation tries to handle logic for both
     *     {@link IView} and {@link IViewGroup}.
     *
     *     For example, if this view is an {@link IViewGroup}, then
     *     the layout code for its children will also be printed.
     * </p>
     *
     * @param indentCount The number of tabs to indent. To define custom tab size,
     *                    see {@link IView#DEFAULT_INDENTATION_LENGTH}.
     * @return The generated XML code. Never null.
     */
    @NonNull
    @Override
    public String generateCode(int indentCount) {
        final var sb = new StringBuilder();
        sb.append("<");
        sb.append(getXmlTag());

        if (!attributes.isEmpty()) {
            for (var attr : attributes) {
                newLine(sb, indentCount + 1); // Attributes must be indented by one tab (4 spaces by default)
                attr.getNamespace();
                if (attr.getNamespace().trim().length() > 0) {
                    sb.append(attr.getNamespace());
                    sb.append(":");
                }
                sb.append(attr.getAttributeName());
                sb.append("=");
                sb.append("\"");
                sb.append(attr.getValue());
                sb.append("\"");
            }
        }

        var hasChildren = false;
        if (this instanceof IViewGroup) {
            final var group = (IViewGroup) this;
            hasChildren = group.getChildCount() > 0;

            if (hasChildren) {
                sb.append(">");
                newLine(sb, indentCount + 1);
                for (var child : group.getChildren()) {
                    newLine(sb, indentCount + 1);
                    sb.append(child.generateCode(indentCount + 1));
                }
            }
        }

        if (hasChildren) {
            // Closing '>' is already printed, no need to print it here.
            // Leave one line and append the closing tag.
            newLine(sb, indentCount);
            newLine(sb, indentCount - 1);
            sb.append("</");
            sb.append(getXmlTag());
            sb.append(">");
        } else {
            sb.append("/>");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseView baseView = (BaseView) o;
        return isPlaceholder() == baseView.isPlaceholder()
                && Objects.equals(attributes, baseView.attributes)
                && Objects.equals(attrAdapters, baseView.attrAdapters)
                && Objects.equals(qualifiedName, baseView.qualifiedName)
                && Objects.equals(view, baseView.view)
                && Objects.equals(getParent(), baseView.getParent())
                && Objects.equals(stored, baseView.stored);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes, attrAdapters, qualifiedName, view, getParent(), stored, isPlaceholder());
    }

    private void newLine (@NonNull StringBuilder stringBuilder, int indentCount) {
        stringBuilder.append("\n");
        indent(stringBuilder, indentCount);
    }

    private void indent (StringBuilder sb, int count) {
        for (var i = 0; i < DEFAULT_INDENTATION_LENGTH * count; i++) {
            sb.append(" ");
        }
    }
}
