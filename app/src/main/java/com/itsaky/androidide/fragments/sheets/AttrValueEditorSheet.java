/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itsaky.androidide.fragments.sheets;

import static com.itsaky.attrinfo.models.Attr.FORMAT_BOOLEAN;
import static com.itsaky.attrinfo.models.Attr.FORMAT_COLOR;
import static com.itsaky.attrinfo.models.Attr.FORMAT_DIMENSION;
import static com.itsaky.attrinfo.models.Attr.FORMAT_ENUM;
import static com.itsaky.attrinfo.models.Attr.FORMAT_FLAG;
import static com.itsaky.attrinfo.models.Attr.FORMAT_FLOAT;
import static com.itsaky.attrinfo.models.Attr.FORMAT_FRACTION;
import static com.itsaky.attrinfo.models.Attr.FORMAT_INTEGER;
import static com.itsaky.attrinfo.models.Attr.FORMAT_REFERENCE;
import static com.itsaky.attrinfo.models.Attr.FORMAT_STRING;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itsaky.androidide.adapters.AttrValueFormatTabAdapter;
import com.itsaky.androidide.databinding.LayoutAttrValueEditorBinding;
import com.itsaky.androidide.fragments.attr.BaseValueEditorFragment;
import com.itsaky.androidide.fragments.attr.BooleanEditor;
import com.itsaky.androidide.fragments.attr.ColorEditor;
import com.itsaky.androidide.fragments.attr.DimensionEditor;
import com.itsaky.androidide.fragments.attr.EnumEditor;
import com.itsaky.androidide.fragments.attr.FlagEditor;
import com.itsaky.androidide.fragments.attr.FloatEditor;
import com.itsaky.androidide.fragments.attr.FractionEditor;
import com.itsaky.androidide.fragments.attr.IntegerEditor;
import com.itsaky.androidide.fragments.attr.ReferenceEditor;
import com.itsaky.androidide.fragments.attr.StringEditor;
import com.itsaky.androidide.models.XMLAttribute;
import com.itsaky.attrinfo.models.Attr;
import com.itsaky.inflater.IAttribute;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

/**
 * Sheet for editing value of an attribute.
 *
 * @author Akash Yadav
 */
public class AttrValueEditorSheet extends BottomSheetDialogFragment
    implements BaseValueEditorFragment.OnValueChangeListener {

  private static final String KEY_ATTRIBUTE = "attrEditorSheet_attribute";
  private BaseValueEditorFragment.OnValueChangeListener valueChangeListener;
  private LayoutAttrValueEditorBinding binding;
  private AttrValueFormatTabAdapter adapter;
  private XMLAttribute attribute;

  @NonNull
  public static AttrValueEditorSheet newInstance(final XMLAttribute attribute) {
    Objects.requireNonNull(attribute);

    final var args = new Bundle();
    args.putParcelable(KEY_ATTRIBUTE, attribute);

    final var fragment = new AttrValueEditorSheet();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    this.valueChangeListener = (BaseValueEditorFragment.OnValueChangeListener) getParentFragment();
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    this.binding = LayoutAttrValueEditorBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (attribute == null) {
      this.attribute = requireArguments().getParcelable(KEY_ATTRIBUTE);
    }

    Objects.requireNonNull(this.attribute);

    final var namespace = this.attribute.getNamespace();
    final var formatText = Attr.createFormatText(attribute.getFormat());
    this.binding.attributeName.setText(
        String.format("%s:%s", namespace.getPrefix(), attribute.getAttributeName()));
    this.binding.attributeFormat.setText(formatText);

    setupTabs(formatText);
  }

  @SuppressLint("NotifyDataSetChanged")
  private void setupTabs(@NonNull String formatText) {
    this.binding.tabs.removeAllTabs();
    this.binding.pager.setSaveEnabled(false);

    if (this.adapter != null) {
      this.adapter.removeAll();
      this.adapter.notifyDataSetChanged();
    }

    this.adapter = new AttrValueFormatTabAdapter(getChildFragmentManager(), attribute);

    final var formats = formatText.split("\\|");
    for (var formatName : formats) {
      if (TextUtils.isEmpty(formatName) || formatName.length() < 2) {
        continue;
      }

      final var frag = getFragmentClass(formatName);
      if (frag != null) {
        this.adapter.addFragment(frag, formatName);
      }
    }

    binding.pager.setAdapter(this.adapter);
    binding.tabs.setupWithViewPager(binding.pager);
  }

  @Nullable
  @Contract(pure = true)
  private Class<? extends BaseValueEditorFragment> getFragmentClass(@NonNull String name) {
    switch (name) {
      case FORMAT_REFERENCE:
        return ReferenceEditor.class;
      case FORMAT_COLOR:
        return ColorEditor.class;
      case FORMAT_BOOLEAN:
        return BooleanEditor.class;
      case FORMAT_DIMENSION:
        return DimensionEditor.class;
      case FORMAT_FLOAT:
        return FloatEditor.class;
      case FORMAT_INTEGER:
        return IntegerEditor.class;
      case FORMAT_FRACTION:
        return FractionEditor.class;
      case FORMAT_STRING:
        return StringEditor.class;
      case FORMAT_ENUM:
        return EnumEditor.class;
      case FORMAT_FLAG:
        return FlagEditor.class;
    }

    return null;
  }

  public void setAttribute(XMLAttribute attribute) {
    Objects.requireNonNull(attribute);
    this.attribute = attribute;
  }

  @Override
  public void onValueChanged(IAttribute attribute, String newValue) {
    if (valueChangeListener != null) {
      valueChangeListener.onValueChanged(attribute, newValue);
    }
  }
}
