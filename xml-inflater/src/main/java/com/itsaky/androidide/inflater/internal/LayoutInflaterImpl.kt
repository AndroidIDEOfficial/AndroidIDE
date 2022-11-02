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

package com.itsaky.androidide.inflater.internal

import android.view.InflateException
import android.view.ViewGroup
import com.android.aapt.Resources.SourcePosition
import com.android.aapt.Resources.XmlElement
import com.android.aapt.Resources.XmlNode.NodeCase
import com.android.aapt.Resources.XmlNode.NodeCase.ELEMENT
import com.android.aaptcompiler.AaptResourceType.LAYOUT
import com.android.aaptcompiler.BlameLogger
import com.android.aaptcompiler.ResourceFile
import com.android.aaptcompiler.ResourceName
import com.android.aaptcompiler.XmlProcessor
import com.android.aaptcompiler.extractPathData
import com.itsaky.androidide.aapt.logging.IDELogger
import com.itsaky.androidide.inflater.IInflateEventsListener
import com.itsaky.androidide.inflater.ILayoutInflater
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.InflationFinishEvent
import com.itsaky.androidide.inflater.InflationStartEvent
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.projects.api.AndroidModule
import java.io.File

/**
 * Default implementation of [ILayoutInflater].
 *
 * @author Akash Yadav
 */
internal class LayoutInflaterImpl : ILayoutInflater() {

  override var inflationEventListener: IInflateEventsListener? = null

  override fun inflate(file: File, parent: ViewGroup): IView? {
    inflationEventListener?.onEvent(InflationStartEvent())
    return doInflate(file, parent).apply {
      inflationEventListener?.onEvent(InflationFinishEvent(this))
    }
  }

  private fun doInflate(file: File, parent: ViewGroup): IView? {
    val pathData = extractPathData(file)
    if (pathData.type != LAYOUT) {
      throw InflateException("File is not a layout file.")
    }

    val module =
      ProjectManager.findModuleForFile(file) as? AndroidModule
        ?: throw InflateException(
          "Cannot find module for given file or the module is not an Android module"
        )
    val resFile =
      ResourceFile(
        ResourceName(module.packageName, pathData.type!!, pathData.name),
        pathData.config,
        pathData.source,
        ResourceFile.Type.ProtoXml
      )

    val processor = XmlProcessor(pathData.source, BlameLogger(IDELogger))
    processor.process(resFile, file.inputStream())

    return doInflate(processor, parent)
  }

  private fun doInflate(processor: XmlProcessor, parent: ViewGroup): IView? {
    // TODO(itsaky) : Add test for multiple view as root layout
    //  The inflater should fail in such cases
    val (file, node) =
      processor.xmlResources.find { it.file == processor.primaryFile }
        ?: throw InflateException("Unable to find primary XML resource from XmlProcessor")
    
    if (node.nodeCase != ELEMENT) {
      throw InflateException("Found ${node.nodeCase} but $ELEMENT was expected at ${node.source.lineCol()}")
    }
    
    val element = node.element
    val view = onCreateView(element)
    TODO("Not yet implemented")
  }
  
  private fun onCreateView(element: XmlElement): IView {
    TODO("Not yet implemented")
  }
  
  private fun SourcePosition.lineCol() : String {
    return "line $lineNumber and column $columnNumber"
  }
}
