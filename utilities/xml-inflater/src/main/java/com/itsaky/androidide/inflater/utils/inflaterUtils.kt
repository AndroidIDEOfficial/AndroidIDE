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

package com.itsaky.androidide.inflater.utils

import com.android.aaptcompiler.AaptResourceType
import com.android.aaptcompiler.ResourceFile.Type.ProtoXml
import com.android.aaptcompiler.XmlProcessor
import com.android.aaptcompiler.extractPathData
import com.itsaky.androidide.aapt.logging.IDELogger
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.IComponentFactory
import com.itsaky.androidide.inflater.ILayoutInflater
import com.itsaky.androidide.inflater.ILayoutInflater.Companion
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.InflateException
import com.itsaky.androidide.inflater.internal.AttributeImpl
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.projects.android.AndroidModule
import java.io.File

/** Get the [ILayoutInflater] registered with [Lookup]. */
fun lookupLayoutInflater(): ILayoutInflater? {
  return Lookup.getDefault().lookup(Companion.LOOKUP_KEY)
}

/** Get the [IComponentFactory] registered with [Lookup]. */
fun lookupComponentFactory(): IComponentFactory? {
  return Lookup.getDefault().lookup(IComponentFactory.LAYOUT_INFLATER_COMPONENT_FACTORY_KEY)
}

@JvmOverloads
fun newAttribute(
  view: IView? = null,
  attribute: IAttribute,
  namespace: INamespace? = null,
  name: String? = null,
  value: String? = null
): IAttribute {
  return newAttribute(
    view = view,
    namespace = namespace ?: attribute.namespace,
    name = name ?: attribute.name,
    value = value ?: attribute.value
  )
}

@JvmOverloads
fun newAttribute(
  view: IView? = null,
  namespace: INamespace? = INamespace.ANDROID,
  name: String,
  value: String
): IAttribute {
  val componentFactory = lookupComponentFactory()
  if (componentFactory != null && view != null) {
    return componentFactory.createAttr(view, namespace, name, value)
  }
  return AttributeImpl(namespace, name, value)
}

/**
 * Processes the XML file using [XmlProcessor].
 *
 * @param file The file to process.
 * @param expectedType The expected [type][AaptResourceType] for the XML file.
 * @return The pair of [XmlProcessor] (processed) instance and the [AndroidModule] instance for the
 *   given file.
 */
fun processXmlFile(file: File, expectedType: com.android.aaptcompiler.AaptResourceType): Pair<XmlProcessor, AndroidModule> {
  val pathData = extractPathData(file)
  if (pathData.type != expectedType) {
    throw InflateException("File is not a layout file.")
  }

  if (IProjectManager.getInstance().getWorkspace() == null) {
    throw InflateException("GradleProject is not initialized!")
  }

  val module =
    IProjectManager.getInstance().getWorkspace()?.findModuleForFile(file, false) as? AndroidModule
      ?: throw InflateException("Cannot find module for given file. Is the project initialized?")
  val resFile =
    com.android.aaptcompiler.ResourceFile(
      com.android.aaptcompiler.ResourceName(module.namespace, pathData.type!!, pathData.name),
      pathData.config,
      pathData.source,
      ProtoXml
    )

  val processor = XmlProcessor(pathData.source, com.android.aaptcompiler.BlameLogger(IDELogger))
  processor.process(resFile, file.inputStream())
  return processor to module
}
