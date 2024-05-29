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

package com.itsaky.androidide.editor.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View.MeasureSpec
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.appcompat.view.menu.SubMenuBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.button.MaterialButton
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.ActionsRegistry
import com.itsaky.androidide.actions.ActionsRegistry.Companion.getInstance
import com.itsaky.androidide.actions.EditorActionItem
import com.itsaky.androidide.actions.FillMenuParams
import com.itsaky.androidide.editor.R.layout
import com.itsaky.androidide.editor.databinding.LayoutPopupMenuItemBinding
import com.itsaky.androidide.editor.ui.EditorActionsMenu.ActionsListAdapter.VH
import com.itsaky.androidide.lsp.api.ILanguageServerRegistry
import com.itsaky.androidide.lsp.java.JavaLanguageServer
import com.itsaky.androidide.lsp.models.DiagnosticItem
import com.itsaky.androidide.lsp.xml.XMLLanguageServer
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.resolveAttr
import io.github.rosemoe.sora.event.HandleStateChangeEvent
import io.github.rosemoe.sora.event.ScrollEvent
import io.github.rosemoe.sora.event.SelectionChangeEvent
import io.github.rosemoe.sora.event.SubscriptionReceipt
import io.github.rosemoe.sora.text.Cursor
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.EditorTouchEventHandler
import java.io.File
import kotlin.math.max
import kotlin.math.min

/**
 * PopupMenu for showing editor's text and code actions.
 *
 * @author Akash Yadav
 */
@SuppressLint("RestrictedApi")
open class EditorActionsMenu(val editor: IDEEditor) :
  AbstractPopupWindow(editor, FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED),
  ActionsRegistry.ActionExecListener,
  MenuBuilder.Callback {

  companion object {

    const val DELAY: Long = 200
  }

  private val touchHandler: EditorTouchEventHandler = editor.eventHandler
  private val receipts: MutableList<SubscriptionReceipt<*>> = mutableListOf()
  private val list = RecyclerView(editor.context)
  private var mLastScroll: Long = 0
  private var mLastPosition: Int = 0

  private val contentHeight by lazy {
    // approximated size is around 56dp
    SizeUtils.dp2px(56f)
  }

  private val menu: MenuBuilder = MenuBuilder(editor.context)
  protected open var location = ActionItem.Location.EDITOR_TEXT_ACTIONS

  open fun init() {
    subscribe()
    applyBackground()

    list.apply {
      clipChildren = true
      clipToOutline = true
      isVerticalFadingEdgeEnabled = true
      isVerticalScrollBarEnabled = true
      layoutParams =
        ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT
        )

      setFadingEdgeLength(SizeUtils.dp2px(42f))
      setPaddingRelative(paddingStart, paddingTop, SizeUtils.dp2px(16f), paddingBottom)
    }

    popup.contentView = this.list
    popup.animationStyle = R.style.PopupAnimation

    val menu = getMenu()
    if (menu is MenuBuilder) {
      menu.setCallback(this)
    }
  }

  open fun subscribe() {
    receipts.add(
      editor.subscribeEvent(SelectionChangeEvent::class.java) { event, _ ->
        this.onSelectionChanged(event)
      }
    )
    receipts.add(editor.subscribeEvent(ScrollEvent::class.java) { _, _ -> this.onScrollEvent() })
    receipts.add(
      editor.subscribeEvent(HandleStateChangeEvent::class.java) { event, _ ->
        this.onHandleStateChanged(event)
      }
    )
  }

  open fun unsubscribeEvents() {
    for (receipt in receipts) {
      receipt.unsubscribe()
    }
  }

  fun destroy() {
    if (this.receipts.isNotEmpty()) {
      unsubscribeEvents()
    }

    getInstance().unregisterActionExecListener(this)
  }

  protected open fun onSelectionChanged(event: SelectionChangeEvent) {
    if (touchHandler.hasAnyHeldHandle()) {
      return
    }
    if (event.isSelected) {
      editor.post { displayWindow(isShowing) }
      mLastPosition = -1
    } else {
      var show = false
      if (
        event.cause == SelectionChangeEvent.CAUSE_TAP &&
        event.left.index == mLastPosition &&
        !isShowing &&
        !editor.text.isInBatchEdit
      ) {
        editor.post(::displayWindow)
        show = true
      } else {
        dismiss()
      }
      mLastPosition =
        if (event.cause == SelectionChangeEvent.CAUSE_TAP && !show) {
          event.left.index
        } else {
          -1
        }
    }
  }

  protected open fun onScrollEvent() {
    val last = mLastScroll
    mLastScroll = System.currentTimeMillis()
    if (mLastScroll - last < DELAY) {
      postDisplay()
    }
  }

  protected open fun onHandleStateChanged(
    event: HandleStateChangeEvent,
  ) {
    if (event.isHeld) {
      postDisplay()
    }
  }

  protected open fun applyBackground() {
    val drawable = GradientDrawable()
    drawable.shape = GradientDrawable.RECTANGLE
    drawable.cornerRadius = SizeUtils.dp2px(28f).toFloat() // Recommeneded size is 28dp
    drawable.color = ColorStateList.valueOf(editor.context.resolveAttr(R.attr.colorSurface))
    drawable.setStroke(SizeUtils.dp2px(1f), editor.context.resolveAttr(R.attr.colorOutline))
    list.background = drawable
  }

  private fun postDisplay() {
    if (!isShowing) {
      return
    }
    dismiss()
    if (!editor.cursor.isSelected) {
      return
    }
    editor.postDelayed(
      object : Runnable {
        override fun run() {
          if (
            !touchHandler.hasAnyHeldHandle() &&
            System.currentTimeMillis() - mLastScroll > DELAY &&
            touchHandler.scroller.isFinished
          ) {
            displayWindow()
          } else {
            editor.postDelayed(this, DELAY)
          }
        }
      },
      DELAY
    )
  }

  private fun selectTop(rect: RectF): Int {
    val rowHeight = editor.rowHeight
    // when the window is being shown for the first time, the height is 0
    val height = if (this.height == 0) contentHeight else this.height
    return if (rect.top - rowHeight * 3 / 2f > height) {
      (rect.top - rowHeight * 3 / 2 - height).toInt()
    } else {
      (rect.bottom + rowHeight / 2).toInt()
    }
  }

  @JvmOverloads
  open fun displayWindow(update: Boolean = false) {
    var top: Int
    val cursor = editor.cursor
    top =
      if (cursor.isSelected) {
        val leftRect = editor.leftHandleDescriptor.position
        val rightRect = editor.rightHandleDescriptor.position
        val top1 = selectTop(leftRect)
        val top2 = selectTop(rightRect)
        min(top1, top2)
      } else {
        selectTop(editor.insertHandleDescriptor.position)
      }
    top = max(0, min(top, editor.height - height - 5))
    val handleLeftX = editor.getOffset(editor.cursor.leftLine, editor.cursor.leftColumn)
    val handleRightX = editor.getOffset(editor.cursor.rightLine, editor.cursor.rightColumn)
    val panelX = computePanelX(cursor, handleLeftX, handleRightX)
    setLocationAbsolutely(panelX, top)
    if (!update) {
      show()
    }
  }

  private fun computePanelX(cursor: Cursor, handleLeftX: Float, handleRightX: Float): Int {
    return if (cursor.isSelected) {
      ((handleLeftX + handleRightX) / 2f).toInt()
    } else {
      var x = (handleLeftX - (width / 2f)).toInt()
      if (x <= 0) {
        x = (handleRightX + SizeUtils.dp2px(10f)).toInt()
      } else if (x >= editor.width) {
        x = editor.width - SizeUtils.dp2px(10f)
      }
      x
    }
  }

  protected open fun fillMenu() {
    getMenu().clear()

    val data = onCreateActionData()

    val registry = getInstance()
    registry.registerActionExecListener(this)
    onFillMenu(registry, data)

    this.list.adapter = ActionsListAdapter(getMenu())
  }

  protected open fun onFillMenu(registry: ActionsRegistry, data: ActionData) {
    registry.fillMenu(FillMenuParams(data, onGetActionLocation(), getMenu()))
  }

  protected open fun onGetActionLocation() = location

  protected open fun onCreateActionData(): ActionData {
    val data = ActionData()
    data.put(Context::class.java, editor.context)
    data.put(IDEEditor::class.java, this.editor)
    data.put(
      CodeEditor::class.java,
      editor
    ) // For LSP actions, as they cannot access IDEEditor class
    data.put(File::class.java, editor.file)
    data.put(DiagnosticItem::class.java, getDiagnosticAtCursor())
    data.put(com.itsaky.androidide.models.Range::class.java, editor.cursorLSPRange)
    data.put(
      JavaLanguageServer::class.java,
      ILanguageServerRegistry.getDefault().getServer(JavaLanguageServer.SERVER_ID)
          as? JavaLanguageServer?
    )
    data.put(
      XMLLanguageServer::class.java,
      ILanguageServerRegistry.getDefault().getServer(XMLLanguageServer.SERVER_ID)
          as? XMLLanguageServer?
    )
    return data
  }

  protected open fun getMenu(): Menu = menu

  private fun getDiagnosticAtCursor(): DiagnosticItem? {
    val start = editor.cursorLSPRange.start
    return editor.languageClient?.getDiagnosticAt(editor.file, start.line, start.column)
  }

  override fun onExec(action: ActionItem, result: Any) {
    if (action !is EditorActionItem || action.dismissOnAction()) {
      dismiss()
    }
  }

  override fun show() {
    if (list.parent != null) {
      (list.parent as ViewGroup).removeView(list)
    }

    this.list.layoutManager = LinearLayoutManager(editor.context, RecyclerView.HORIZONTAL, false)

    fillMenu()

    measureActionsList()

    val height = list.measuredHeight
    val width = min(editor.width - SizeUtils.dp2px(32f), list.measuredWidth)
    setSize(width, height)
    super.show()
  }

  private fun measureActionsList() {
    val dp8 = SizeUtils.dp2px(8f)
    val dp16 = dp8 * 2
    this.list.measure(
      MeasureSpec.makeMeasureSpec(editor.width - dp16 * 2, MeasureSpec.AT_MOST),
      MeasureSpec.makeMeasureSpec((260 * editor.dpUnit).toInt() - dp16 * 2, MeasureSpec.AT_MOST)
    )
  }

  @SuppressLint("InflateParams")
  private fun findWidestItem(): Int {
    var widest = 0
    val text =
      LayoutInflater.from(editor.context).inflate(layout.layout_popup_menu_item, null)
          as MaterialButton
    val dp30 = SizeUtils.dp2px(30f)
    val paddingHorizontal = text.paddingStart + text.paddingEnd
    val drawablePadding = text.iconPadding
    val extraWidth = dp30 * 2 // 30dp for start and end drawables both

    for (i in 0 until getMenu().size()) {
      val item = getMenu().getItem(i)
      val title = item.title.toString()
      var width = paddingHorizontal + (drawablePadding * 2)
      width += text.paint.measureText(title).toInt()
      width += extraWidth

      if (width > widest) {
        widest = width
      }
    }

    return widest
  }

  private class ActionsListAdapter(val menu: Menu?, val forceShowTitle: Boolean = false) :
    RecyclerView.Adapter<VH>() {

    override fun getItemCount(): Int {
      return menu?.size() ?: 0
    }

    fun getItem(position: Int): MenuItem? = menu?.getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
      return VH(
        LayoutPopupMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
      val item = getItem(position) ?: return
      holder.binding.root.text = if (forceShowTitle) item.title else ""
      holder.binding.root.tooltipText = item.title
      holder.binding.root.icon =
        item.icon
          ?: run {
            holder.binding.root.text = item.title
            holder.binding.root.layoutParams.apply { width = ViewGroup.LayoutParams.WRAP_CONTENT }
            null
          }
      holder.binding.root.setOnClickListener { (item as MenuItemImpl).invoke() }
    }

    inner class VH(val binding: LayoutPopupMenuItemBinding) : ViewHolder(binding.root)
  }

  override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
    // Click event of MenuItems without SubMenu is consumed by the ActionsRegistry
    // So we only need to handle click event of SubMenus
    if (!item.hasSubMenu()) {
      return false
    }

    if (item.hasSubMenu() && item.subMenu is SubMenuBuilder) {
      (item.subMenu as SubMenuBuilder).setCallback(this)
    }

    this.editor.post {
      TransitionManager.beginDelayedTransition(this.list, ChangeBounds())
      this.list.layoutManager = LinearLayoutManager(editor.context)
      this.list.adapter = ActionsListAdapter(item.subMenu, true)

      measureActionsList()
      popup.update(findWidestItem(), this.list.measuredHeight)
    }

    return true
  }

  override fun onMenuModeChange(menu: MenuBuilder) {}
}
