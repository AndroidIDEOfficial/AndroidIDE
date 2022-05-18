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

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.xml.INamespace;

import java.util.List;

/**
 * Represents a view in the UI Designer
 *
 * @author Akash Yadav
 */
public interface IView {

  /**
   * The indentation length that will be used while generating layout code. You can modify this to
   * change the number of spaces used for indentation.
   */
  int DEFAULT_INDENTATION_LENGTH = 4;

  /**
   * Get this view as android {@link View}. This will be used to display this view in the designer
   *
   * @return {@link View} of this object. Maybe {@code null}
   */
  View asView();

  /**
   * Add this attribute to this view
   *
   * @param attr The Attribute to add
   */
  void addAttribute(IAttribute attr);

  /**
   * Remove this attribute
   *
   * @param attr The Attribute to remove
   */
  void removeAttribute(IAttribute attr);

  /**
   * Remove attribute at index
   *
   * @param index Index of the attribute to remove
   */
  void removeAttributeAt(int index);

  /**
   * Get the attributes of this view.
   *
   * @return The attributes.
   */
  List<IAttribute> getAttributes();

  /**
   * Get the attributes of this view as an array.
   *
   * @return The attributes.
   * @see #getAttributes()
   */
  IAttribute[] getAttrArray();

  /**
   * Does this view contains an attribute with the given namespace and name?
   *
   * @param namespace The namespace to check.
   * @param name The name of the attribute to check.
   * @return {@code true} if this view contains the attribute, {@code false} otherwise.
   */
  boolean hasAttribute(INamespace namespace, String name);

  /**
   * Get the attribute with the given namespace and attribute name. Or {@code null} if there is no
   * attribute with the given namespace and name.s
   *
   * @param namespace The namespace of the attribute.
   * @param name The name of the attribute.
   * @return Found {@link IAttribute} or {@code null}.
   */
  @Nullable
  IAttribute getAttribute(INamespace namespace, String name);

  /**
   * Update the value of the given attribute.
   *
   * @param attribute The attribute that should be updated.
   * @return {@code true} if the attribute was successfully updated, {@code false} otherwise.
   */
  default boolean updateAttribute(@NonNull IAttribute attribute) {
    return updateAttribute(
        attribute.getNamespace(), attribute.getAttributeName(), attribute.getValue());
  }

  /**
   * Find and update the given attribute with the given value.
   *
   * @param namespace The namespace of the attribute.
   * @param name The name of the attribute.
   * @param value The new value of the attribute.
   * @return {@code true} if the attribute was successfully updated, {@code false} otherwise.
   */
  boolean updateAttribute(INamespace namespace, String name, String value);

  /**
   * Register this attribute adapter
   *
   * @param adapter The adapter to register
   */
  void registerAttributeAdapter(IAttributeAdapter adapter);

  /**
   * Register the given namespace to this view.
   *
   * @param namespace The name space to register.
   */
  void registerNamespace(INamespace namespace);

  /**
   * Find the namespace registered by the given name. Returns <code>null</code> if there is none.
   *
   * @param name The name of the namespace to look for. Must not be <code>null</code>.
   * @return The found namespace or <code>null</code>.
   */
  @Nullable
  INamespace findRegisteredNamespace(@NonNull String name);

  /**
   * Set the parent of this view. This is set by {@link IViewGroup} when {@link
   * IViewGroup#addView(IView, int)} is called.
   *
   * @param newParent The new parent of this view.
   */
  void setParent(IViewGroup newParent);

  /**
   * Get the parent of this view
   *
   * @return The parent of this view or {@code null} if this is the root view.
   */
  IViewGroup getParent();

  /**
   * Remove this view from its parent. This should take care of removing the actual {@link View}
   * from its parent.
   *
   * @return {@code true} if the view was successfully removed, {@code false} otherwise.
   */
  boolean removeFromParent();

  /**
   * Is this view a placeholder for another view?
   *
   * @return {@code true} if this view is a placeholder, {@code false} otherwise.
   */
  boolean isPlaceholder();

  /**
   * Store an object that is associated with this view.
   *
   * @param data The data to store.
   */
  void setExtraData(Object data);

  /**
   * Get the stored data.
   *
   * @return The stored data.
   * @see #setExtraData(Object)
   */
  @Nullable
  Object getExtraData();

  /**
   * Get the layout resource code for this file.
   *
   * @param indentationLength The number of spaces to indent.
   */
  String generateCode(int indentationLength);

  /**
   * @see #generateCode(int)
   */
  default String generateCode() {
    return generateCode(0);
  }

  /**
   * Get the tag name which will be used for generating XML Code for this view.
   *
   * <p>Default implementation simply returns the simple name of this view's class.
   *
   * @return The tag name of this view.
   */
  String getXmlTag();
}
