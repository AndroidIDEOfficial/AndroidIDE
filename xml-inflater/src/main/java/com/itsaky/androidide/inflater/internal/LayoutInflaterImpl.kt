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

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import com.android.aapt.Resources.SourcePosition
import com.android.aapt.Resources.XmlElement
import com.android.aapt.Resources.XmlNode.NodeCase.ELEMENT
import com.android.aaptcompiler.AaptResourceType.ID
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
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.InflateException
import com.itsaky.androidide.inflater.InflationFinishEvent
import com.itsaky.androidide.inflater.InflationStartEvent
import com.itsaky.androidide.inflater.internal.utils.IDTable
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.xml.widgets.Widget
import com.itsaky.androidide.xml.widgets.WidgetTable
import java.io.File
import java.lang.reflect.Method

/**
 * Default implementation of [ILayoutInflater].
 *
 * @author Akash Yadav
 */
open class LayoutInflaterImpl : ILayoutInflater() {

  override var inflationEventListener: IInflateEventsListener? = null
  private val log = ILogger.newInstance("LayoutInflaterImpl")
  private var _primaryInflatingFile: File? = null
  private var _currentLayoutFile: LayoutFile? = null

  protected val primaryInflatingFile: File
    get() = this._primaryInflatingFile!!
  
  protected val currentLayoutFile: LayoutFile
    get() = _currentLayoutFile!!

  override fun inflate(file: File, parent: ViewGroup): IView? {
    this._primaryInflatingFile = file
    IDTable.newRound()
    inflationEventListener?.onEvent(InflationStartEvent())
    return doInflate(file, parent).apply {
      inflationEventListener?.onEvent(InflationFinishEvent(this))
      _primaryInflatingFile = null
    }
  }

  protected open fun doInflate(file: File, parent: ViewGroup): IView? {
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
    
    return doInflate(processor, parent, module)
  }

  protected open fun doInflate(
    processor: XmlProcessor,
    parent: ViewGroup,
    module: AndroidModule,
  ): IView? {
    // TODO(itsaky) : Add test for multiple view as root layout
    //  The inflater should fail in such cases
    val (file, node) =
      processor.xmlResources.find { it.file == processor.primaryFile }
        ?: throw InflateException("Unable to find primary XML resource from XmlProcessor")
    
    this._currentLayoutFile = LayoutFile(this.primaryInflatingFile, file.name.entry!!)
    
    if (node.nodeCase != ELEMENT) {
      throw InflateException(
        "Found ${node.nodeCase} but $ELEMENT was expected at ${node.source.lineCol()}"
      )
    }
    
    // Store all IDs
    file.exportedSymbols.filter { it.name.type == ID }.forEach {
      IDTable.set(currentLayoutFile.resName, it.name.entry!!, View.generateViewId())
    }
    
    val element = node.element
    val view = onCreateView(element, wrap(parent), module)
    
    this._currentLayoutFile = null
    
    return view
  }

  protected open fun onCreateView(
    element: XmlElement,
    parent: IViewGroup,
    module: AndroidModule,
    widgets: WidgetTable =
      module.getWidgetTable()
        ?: throw IllegalStateException("No widget table found for module $module"),
  ): IView {

    val parentView = parent.view as ViewGroup

    if ("include" == element.name) {
      return onCreateIncludedView(element, parentView, module, widgets)
    }

    if ("merge" == element.name) {
      return onCreateMergedView(element, parentView, module, widgets)
    }

    val widget =
      if (element.name.contains('.')) widgets.getWidget(element.name)
      else widgets.findWidgetWithSimpleName(element.name)

    // TODO(itsaky): Handle views from libraries
    val view =
      if (widget == null) {
        onCreateUnsupportedView("View with name '${element.name}' not found", parentView)
      } else {
        onCreatePlatformView(widget, parentView, module, widgets)
      }

    val adapter =
      AttributeAdapterIndex.getAdapter(view.name)
        ?: throw InflateException("No attribute adapter found for view ${view.name}")

    adapter.applyBasic(view)

    view.view.layoutParams = generateLayoutParams(parentView)
    parent.addChild(view)

    if (element.childCount > 0 && view is IViewGroup) {
      for (child in element.childList) {
        if (child.nodeCase != ELEMENT) {
          throw InflateException("Unexpected node at ${child.source.lineCol()}")
        }
        val childView =
          onCreateView(element = child.element, parent = view, module = module, widgets = widgets)
        view.addChild(childView)
      }
    }

    return view
  }

  private fun onCreateMergedView(
    element: XmlElement,
    parent: ViewGroup,
    module: AndroidModule,
    widgets: WidgetTable,
  ): IView {
    TODO("Not yet implemented")
  }

  private fun onCreateIncludedView(
    element: XmlElement,
    parent: ViewGroup,
    module: AndroidModule,
    widgets: WidgetTable,
  ): IView {
    TODO("Not yet implemented")
  }

  protected open fun onCreatePlatformView(
    widget: Widget,
    parent: ViewGroup,
    module: AndroidModule,
    widgets: WidgetTable,
  ): IView {
    return try {
      val v = createViewInstance(widget, parent)
      return ViewImpl(currentLayoutFile, widget.qualifiedName, v)
    } catch (err: Throwable) {
      onCreateUnsupportedView("Unable to create view for widget ${widget.qualifiedName}", parent)
    }
  }

  protected open fun generateLayoutParams(parent: ViewGroup): LayoutParams {
    return try {
      var clazz: Class<in ViewGroup> = parent.javaClass
      var method: Method?
      while (true) {
        try {
          method = clazz.getDeclaredMethod("generateDefaultLayoutParams")
          break
        } catch (e: Throwable) {
          /* ignored */
        }

        clazz = clazz.superclass
      }
      if (method != null) {
        method.isAccessible = true
        return method.invoke(parent) as LayoutParams
      }
      log.error("Unable to create default params for view parent:", parent)
      LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    } catch (th: Throwable) {
      throw InflateException("Unable to create layout params for parent: $parent", th)
    }
  }

  private fun createViewInstance(widget: Widget, parent: ViewGroup): View {
    val klass = javaClass.classLoader!!.loadClass(widget.qualifiedName)
    val constructor = klass.getConstructor(Context::class.java)
    return constructor.newInstance(parent.context) as View
  }

  private fun onCreateUnsupportedView(message: String, parent: ViewGroup): IView {
    return ErrorView(currentLayoutFile, parent.context, message)
  }

  private fun wrap(parent: ViewGroup): IViewGroup {
    return ViewGroupImpl(currentLayoutFile, parent.javaClass.name, parent)
  }

  private fun SourcePosition.lineCol(): String {
    return "line $lineNumber and column $columnNumber"
  }
}
