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

import android.view.View
import android.view.ViewGroup
import com.android.SdkConstants.EXT_XML
import com.android.aapt.Resources.SourcePosition
import com.android.aapt.Resources.XmlAttribute
import com.android.aapt.Resources.XmlElement
import com.android.aapt.Resources.XmlNode.NodeCase.ELEMENT
import com.android.aaptcompiler.AaptResourceType.ID
import com.android.aaptcompiler.AaptResourceType.LAYOUT
import com.android.aaptcompiler.XmlProcessor
import com.itsaky.androidide.inflater.DefaultComponentFactory
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.IComponentFactory
import com.itsaky.androidide.inflater.IComponentFactory.Companion.LAYOUT_INFLATER_COMPONENT_FACTORY_KEY
import com.itsaky.androidide.inflater.ILayoutInflater
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.InflateException
import com.itsaky.androidide.inflater.events.IInflateEventsListener
import com.itsaky.androidide.inflater.events.InflationFinishEvent
import com.itsaky.androidide.inflater.events.InflationStartEvent
import com.itsaky.androidide.inflater.events.OnApplyAttributeEvent
import com.itsaky.androidide.inflater.events.OnInflateViewEvent
import com.itsaky.androidide.inflater.internal.utils.IDTable
import com.itsaky.androidide.inflater.internal.utils.ViewFactory.createViewInstance
import com.itsaky.androidide.inflater.internal.utils.ViewFactory.generateLayoutParams
import com.itsaky.androidide.inflater.internal.utils.parseLayoutReference
import com.itsaky.androidide.inflater.utils.endParse
import com.itsaky.androidide.inflater.utils.isParsing
import com.itsaky.androidide.inflater.utils.startParse
import com.itsaky.androidide.inflater.viewAdapter
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.projects.android.AndroidModule
import com.itsaky.androidide.xml.widgets.Widget
import com.itsaky.androidide.xml.widgets.WidgetTable
import com.itsaky.androidide.xml.widgets.WidgetType
import java.io.File

/**
 * Default implementation of [ILayoutInflater].
 *
 * @author Akash Yadav
 */
open class LayoutInflaterImpl : ILayoutInflater {

  override var inflationEventListener: IInflateEventsListener? = null
  override var module: AndroidModule? = null
  private var manuallyStartedParse = false
  private var _primaryInflatingFile: File? = null
  private var _currentLayoutFile: LayoutFile? = null

  override var componentFactory: IComponentFactory = DefaultComponentFactory()
    set(value) {
      field = value
      Lookup.getDefault().update(LAYOUT_INFLATER_COMPONENT_FACTORY_KEY, value)
    }

  protected val primaryInflatingFile: File
    get() = this._primaryInflatingFile!!

  protected val currentLayoutFile: LayoutFile
    get() = this._currentLayoutFile!!

  override fun inflate(file: File, parent: ViewGroup): List<IView> {
    startInflation(file)
    return doInflate(file, parent).apply { finishInflation() }
  }

  override fun inflate(file: File, parent: IViewGroup): List<IView> {
    startInflation(file)
    return doInflate(file, parent).apply { finishInflation() }
  }

  override fun close() {
    this.module = null
    this.inflationEventListener = null
    this._primaryInflatingFile = null
    this._currentLayoutFile = null

    if (manuallyStartedParse) {
      // end the pare only if we called startParse()
      endParse()
    }
  }

  protected open fun doInflate(file: File, parent: ViewGroup): List<IView> {
    val (processor, module) = processXmlFile(file)
    return doInflate(processor, parent, module)
  }

  protected open fun doInflate(file: File, parent: IViewGroup): List<IView> {
    val (processor, module) = processXmlFile(file)
    return doInflate(processor, parent, module)
  }

  protected open fun doInflate(
    processor: XmlProcessor,
    parent: ViewGroup,
    module: AndroidModule,
  ): List<IView> {
    return doInflate(processor, module) { wrap(parent) }
  }

  protected open fun doInflate(
    processor: XmlProcessor,
    parent: IViewGroup,
    module: AndroidModule,
  ): List<IView> {
    return doInflate(processor, module) { parent }
  }

  protected open fun doInflate(
    processor: XmlProcessor,
    module: AndroidModule,
    parent: () -> IViewGroup,
  ): List<IView> {
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
    file.exportedSymbols
      .filter { it.name.type == ID }
      .forEach { IDTable.set(currentLayoutFile.resName, it.name.entry!!, View.generateViewId()) }

    val element = node.element

    return onCreateView(element, parent(), module)
  }

  protected open fun onCreateView(
    element: XmlElement,
    parent: IViewGroup,
    module: AndroidModule,
    widgets: WidgetTable =
      module.getWidgetTable()
        ?: throw IllegalStateException("No widget table found for module $module"),
  ): List<IView> {

    val parentView = parent.view as ViewGroup
    if ("merge" == element.name) {
      return onCreateMergedView(element, parent, module, widgets)
    }

    if ("include" == element.name) {
      return onCreateIncludedView(element, parent)
    }

    val widget =
      if (element.name.contains('.')) widgets.getWidget(element.name)
      else widgets.findWidgetWithSimpleName(element.name)

    // TODO(itsaky): Handle views from libraries
    val view: ViewImpl =
      (if (widget == null) {
        onCreateUnsupportedView(
          element.name,
          element.childCount > 0,
          "View with name '${element.name}' not found",
          parentView
        )
      } else {
        onCreatePlatformView(widget, parentView, module, widgets)
      })
          as ViewImpl

    addNamespaceDecls(element, view)
    applyAttributes(element, view, parent)

    if (element.childCount > 0 && view is IViewGroup) {
      for (child in element.childList) {
        if (child.nodeCase != ELEMENT) {
          throw InflateException("Unexpected node at ${child.source.lineCol()}")
        }
        onCreateView(element = child.element, parent = view, module = module, widgets = widgets)
      }
    }

    inflationEventListener?.onEvent(OnInflateViewEvent(view))

    return listOf(view)
  }

  @JvmOverloads
  protected open fun applyAttributes(
    element: XmlElement,
    view: ViewImpl,
    parent: IViewGroup,
    attachToParent: Boolean = true,
    shouldApplyAttr: (XmlAttribute) -> Boolean = { true }
  ) {
    val parentView = parent.view as ViewGroup
    val adapter =
      view.viewAdapter ?: throw InflateException("No attribute adapter found for view ${view.name}")

    view.view.layoutParams = generateLayoutParams(parentView)

    if (attachToParent) {
      parent.addChild(view)
    }

    adapter.applyBasic(view)

    if (element.attributeCount > 0) {
      for (xmlAttribute in element.attributeList) {

        val namespace =
          if (xmlAttribute.namespaceUri.isNullOrBlank()) null
          else view.findNamespaceByUri(xmlAttribute.namespaceUri)

        val attr = onCreateAttribute(view, namespace, xmlAttribute.name, xmlAttribute.value)
        view.addAttribute(attribute = attr, apply = shouldApplyAttr(xmlAttribute), update = true)
        inflationEventListener?.onEvent(OnApplyAttributeEvent(view, attr))
      }
    }
  }

  private fun onCreateMergedView(
    element: XmlElement,
    parent: IViewGroup,
    module: AndroidModule,
    widgets: WidgetTable,
  ): List<IView> {
    val views = mutableListOf<IView>()
    for (xmlNode in element.childList) {
      if (xmlNode.nodeCase == ELEMENT) {
        views.addAll(onCreateView(xmlNode.element, parent, module, widgets))
      }
    }

    addNamespaceDecls(element, parent as ViewImpl)

    return views
  }

  private fun onCreateIncludedView(element: XmlElement, parent: IViewGroup): List<IView> {
    val layout =
      element.attributeList.find { it.namespaceUri.isNullOrBlank() && it.name == "layout" }
        ?: throw InflateException("<include> tag must have 'layout' attribute")
    val file = parseLayoutReference(layout.value)
    if (file == null || !file.exists() || !file.isFile || file.extension != EXT_XML) {
      throw InflateException("Invalid layout file reference; '${layout.value}'")
    }

    val (processor, module) = processXmlFile(file)

    // we need to restore the layout file instance as well
    val inflated =
      currentLayoutFile.let { layoutFile ->
        doInflate(processor, module) { parent }.also { _currentLayoutFile = layoutFile }
      }

    if (inflated.isEmpty() || inflated.size > 1) {
      // probably a merged view or no views at all
      // no need to apply attributes
      return inflated
    }

    val includedView = inflated[0]
    val view = IncludeView(includedView as ViewImpl)
    addNamespaceDecls(element = element, view = view)

    // The inflated <include> view is already attached to parent
    // however, we want the parent to have the 'IncludeView' as the child and not the actually
    // included view
    val index = parent.indexOfChild(includedView)
    parent.removeChild(index)
    parent.addChild(index, view)

    // also, the attribute on an <include> tag must override the attributes specified on the root
    // view of the included layout file
    applyAttributes(element = element, view = view, parent = parent, attachToParent = false) {
      !(it.namespaceUri.isNullOrBlank() && it.name == "layout")
    }

    return listOf(view)
  }

  protected open fun onCreatePlatformView(
    widget: Widget,
    parent: ViewGroup,
    module: AndroidModule,
    widgets: WidgetTable,
  ): IView {
    return try {
      val v = createViewInstance(widget.qualifiedName, parent.context)
      return componentFactory.createView(currentLayoutFile, widget.qualifiedName, v)
    } catch (err: Throwable) {
      onCreateUnsupportedView(
        widget.qualifiedName,
        widget.type == WidgetType.LAYOUT,
        "Unable to create view for widget ${widget.qualifiedName}",
        parent
      )
    }
  }

  protected open fun onCreateAttribute(
    view: ViewImpl,
    namespace: INamespace?,
    name: String,
    value: String
  ): IAttribute {
    return componentFactory.createAttr(
      view = view,
      namespace = namespace,
      name = name,
      value = value
    )
  }

  protected open fun addNamespaceDecls(element: XmlElement, view: ViewImpl) {
    if (element.namespaceDeclarationCount > 0) {
      for (xmlNamespace in element.namespaceDeclarationList) {
        view.namespaces[xmlNamespace.uri] = NamespaceImpl(xmlNamespace.prefix, xmlNamespace.uri)
      }
    }
  }

  protected open fun processXmlFile(file: File): Pair<XmlProcessor, AndroidModule> {
    return com.itsaky.androidide.inflater.utils.processXmlFile(file, LAYOUT)
  }

  private fun onCreateUnsupportedView(
    name: String,
    isLayout: Boolean,
    message: String,
    parent: ViewGroup
  ): IView {
    return if (isLayout) {
      ErrorLayout(file = currentLayoutFile, name = name, context = parent.context)
    } else {
      ErrorView(file = currentLayoutFile, name = name, context = parent.context, message = message)
    }
  }

  private fun List<IView>.finishInflation() {
    inflationEventListener?.onEvent(InflationFinishEvent(this))
    _primaryInflatingFile = null
  }

  private fun startInflation(file: File) {
    this._primaryInflatingFile = file
    IDTable.newRound()
    if (!isParsing) {
      if (this.module == null) {
        startParse(file)
        this.module = com.itsaky.androidide.inflater.utils.module
      } else {
        startParse(this.module!!)
      }
      this.manuallyStartedParse = true
    } else {
      manuallyStartedParse = false
    }
    inflationEventListener?.onEvent(InflationStartEvent())
  }

  private fun wrap(parent: ViewGroup): IViewGroup {
    return componentFactory.createView(currentLayoutFile, parent.javaClass.name, parent)
        as IViewGroup
  }

  private fun SourcePosition.lineCol(): String {
    return "line $lineNumber and column $columnNumber"
  }
}
