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
package com.itsaky.androidide.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Adapter for showing the autocomplete items while editing attribute values in UI Designer.
 *
 * @author Akash Yadav
 */
public class AttrValueCompletionAdapter extends BaseAdapter implements Filterable {

  private final List<String> items;
  private final List<String> filtered;
  private final Context context;
  private final Filter mFilter =
      new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
          final var result = new FilterResults();
          if (TextUtils.isEmpty(constraint)) {
            final var list = new ArrayList<>(items);
            result.values = list;
            result.count = list.size();
            return result;
          }

          final var match = constraint.toString().trim().toLowerCase(Locale.ROOT);
          final var list = new ArrayList<String>();
          for (String item : items) {
            if (item.contains(match)) {
              list.add(item);
            }
          }

          result.values = list;
          result.count = list.size();
          return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {
          //noinspection unchecked
          List<String> items = (List<String>) results.values;
          Objects.requireNonNull(items);

          filtered.clear();
          filtered.addAll(items);

          if (items.isEmpty()) {
            notifyDataSetInvalidated();
          } else {
            notifyDataSetChanged();
          }
        }
      };

  public AttrValueCompletionAdapter(Context context, List<String> items) {
    this.context = context;
    this.items = items;
    this.filtered = new ArrayList<>(items);
  }

  @Override
  public int getCount() {
    return this.filtered.size();
  }

  @Override
  public Object getItem(int position) {
    return filtered.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView =
          ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
              .inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    ((TextView) convertView).setText(getItem(position).toString());

    return convertView;
  }

  @Override
  public Filter getFilter() {
    return mFilter;
  }
}
