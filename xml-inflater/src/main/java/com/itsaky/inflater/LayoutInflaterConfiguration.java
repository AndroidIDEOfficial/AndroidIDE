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
package com.itsaky.inflater;

import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.widgets.WidgetInfo;

import java.io.File;
import java.util.Set;

/** Configuration for {@link ILayoutInflater} */
public class LayoutInflaterConfiguration {

  final Set<File> resDirs;
  final AttrInfo attrInfo;
  final WidgetInfo widgetInfo;
  final IResourceTable resFinder;
  final ILayoutInflater.ContextProvider contextProvider;

  public LayoutInflaterConfiguration(
      Set<File> resDirs,
      AttrInfo attrInfo,
      WidgetInfo widgetInfo,
      IResourceTable resourceProvider,
      ILayoutInflater.ContextProvider contextProvider) {
    this.resDirs = resDirs;
    this.attrInfo = attrInfo;
    this.widgetInfo = widgetInfo;
    this.resFinder = resourceProvider;
    this.contextProvider = contextProvider;
  }

  /** A class that builds a {@link LayoutInflaterConfiguration} */
  public static class Builder {

    private Set<File> resDirs;
    private AttrInfo attrInfo;
    private WidgetInfo widgetInfo;
    private IResourceTable resourceProvider;
    private ILayoutInflater.ContextProvider provider;

    public Builder setResourceDirectories(Set<File> dirs) {
      this.resDirs = dirs;
      return this;
    }

    public Builder setAttrInfo(AttrInfo info) {
      this.attrInfo = info;
      return this;
    }

    public Builder setWidgetInfo(WidgetInfo info) {
      this.widgetInfo = info;
      return this;
    }

    public Builder setResourceFinder(IResourceTable provider) {
      this.resourceProvider = provider;
      return this;
    }

    public Builder setContextProvider(ILayoutInflater.ContextProvider provider) {
      this.provider = provider;
      return this;
    }

    public LayoutInflaterConfiguration create() {
      return new LayoutInflaterConfiguration(
          resDirs, attrInfo, widgetInfo, resourceProvider, provider);
    }
  }
}
