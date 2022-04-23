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

package com.itsaky.androidide.views.editor

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.appcompat.view.menu.SubMenuBuilder
import androidx.core.content.ContextCompat
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.itsaky.androidide.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.ActionsRegistry
import com.itsaky.androidide.actions.ActionsRegistry.Companion.getInstance
import com.itsaky.androidide.actions.editor.SelectAllAction
import com.itsaky.androidide.app.StudioApp
import com.itsaky.androidide.language.IDELanguage
import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.java.JavaLanguageServer
import com.itsaky.lsp.models.DiagnosticItem
import com.itsaky.lsp.models.Range
import com.itsaky.lsp.util.DiagnosticUtil
import com.itsaky.lsp.xml.XMLLanguageServer
import io.github.rosemoe.sora.event.HandleStateChangeEvent
import io.github.rosemoe.sora.event.ScrollEvent
import io.github.rosemoe.sora.event.SelectionChangeEvent
import io.github.rosemoe.sora.event.SubscriptionReceipt
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.EditorTouchEventHandler
import io.github.rosemoe.sora.widget.base.EditorPopupWindow
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
    EditorPopupWindow(editor, FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED),
    ActionsRegistry.ActionExecListener,
    MenuBuilder.Callback {

    companion object {
        const val DELAY: Long = 200
    }

    private val touchHandler: EditorTouchEventHandler = editor.eventHandler
    private val receipts: MutableList<SubscriptionReceipt<*>> = mutableListOf()
    private val log = ILogger.newInstance(javaClass.simpleName)
    private val list: ListView = ListView(editor.context)
    private var mLastScroll: Long = 0
    private var mLastPosition: Int = 0

    private val menu: MenuBuilder = MenuBuilder(editor.context)
    protected open var location = ActionItem.Location.EDITOR_TEXT_ACTIONS

    open fun init() {
        subscribe()
        applyBackground()

        list.clipChildren = true
        list.clipToOutline = true
        list.isVerticalFadingEdgeEnabled = true
        list.isVerticalScrollBarEnabled = true
        list.setFadingEdgeLength(SizeUtils.dp2px(42f))
        list.divider = null
        list.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

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
            })
        receipts.add(
            editor.subscribeEvent(ScrollEvent::class.java) { _, _ -> this.onScrollEvent() })
        receipts.add(
            editor.subscribeEvent(HandleStateChangeEvent::class.java) { event, _ ->
                this.onHandleStateChanged(event)
            })
    }

    open fun unsubscribeEvents() {
        for (receipt in receipts) {
            receipt.unsubscribe()
        }
    }

    protected open fun onSelectionChanged(event: SelectionChangeEvent) {
        if (touchHandler.hasAnyHeldHandle()) {
            return
        }
        if (event.isSelected) {
            if (!isShowing) {
                editor.post(::displayWindow)
            }
            mLastPosition = -1
        } else {
            var show = false
            if (event.cause == SelectionChangeEvent.CAUSE_TAP &&
                event.left.index == mLastPosition &&
                !isShowing &&
                !editor.text.isInBatchEdit) {
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
        drawable.cornerRadius = 16f
        drawable.color =
            ColorStateList.valueOf(
                ContextCompat.getColor(editor.context, R.color.content_background))
        drawable.setStroke(
            SizeUtils.dp2px(1f), ContextCompat.getColor(editor.context, R.color.primaryLightColor))
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
                    if (!touchHandler.hasAnyHeldHandle() &&
                        System.currentTimeMillis() - mLastScroll > DELAY &&
                        touchHandler.scroller.isFinished) {
                        displayWindow()
                    } else {
                        editor.postDelayed(this, DELAY)
                    }
                }
            },
            DELAY)
    }

    private fun selectTop(rect: RectF): Int {
        val rowHeight = editor.rowHeight
        return if (rect.top - rowHeight * 3 / 2f > height) {
            (rect.top - rowHeight * 3 / 2 - height).toInt()
        } else {
            (rect.bottom + rowHeight / 2).toInt()
        }
    }

    open fun displayWindow() {
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
        val panelX = ((handleLeftX + handleRightX) / 2f).toInt()
        setLocationAbsolutely(panelX, top)
        show()
    }

    protected open fun fillMenu() {
        getMenu().clear()

        val data = onCreateActionData()

        val registry = getInstance()
        registry.registerActionExecListener(this)
        onFillMenu(registry, data)

        this.list.adapter = ActionsListAdapter(getMenu(), editor.context)
    }

    protected open fun onFillMenu(registry: ActionsRegistry, data: ActionData) {
        registry.fillMenu(data, onGetActionLocation(), getMenu())
    }

    protected open fun onGetActionLocation() = location

    protected open fun onCreateActionData(): ActionData {
        val data = ActionData()
        data.put(Context::class.java, editor.context)
        data.put(IDEEditor::class.java, this.editor)
        data.put(
            CodeEditor::class.java,
            editor) // For LSP actions, as they cannot access IDEEditor class
        data.put(File::class.java, editor.file)
        data.put(DiagnosticItem::class.java, getDiagnosticAtCursor())
        data.put(Range::class.java, editor.cursorRange)
        data.put(
            JavaLanguageServer::class.java,
            StudioApp.getInstance().javaLanguageServer as JavaLanguageServer)
        data.put(
            XMLLanguageServer::class.java,
            StudioApp.getInstance().xmlLanguageServer as XMLLanguageServer)
        return data
    }

    protected open fun getMenu(): Menu = menu

    private fun getDiagnosticAtCursor(): DiagnosticItem? {
        val language = editor.editorLanguage as? IDELanguage ?: return null
        val diagnostics = language.diagnostics
        return DiagnosticUtil.binarySearchDiagnostic(diagnostics, editor.cursorRange.start)
    }

    override fun onExec(action: ActionItem, result: Any) {
        if (action !is SelectAllAction) {
            dismiss()
        }
    }

    override fun show() {
        if (list.parent != null) {
            (list.parent as ViewGroup).removeView(list)
        }

        fillMenu()

        val dp8 = SizeUtils.dp2px(8f)
        val dp16 = dp8 * 2
        this.list.measure(
            View.MeasureSpec.makeMeasureSpec(editor.width - dp16 * 2, View.MeasureSpec.AT_MOST),
            View.MeasureSpec.makeMeasureSpec(
                (260 * editor.dpUnit).toInt() - dp16 * 2, View.MeasureSpec.AT_MOST))
        setSize(findWidestItem(), this.list.measuredHeight)
        super.show()
    }

    private fun findWidestItem(): Int {
        var widest = 0
        val text =
            LayoutInflater.from(editor.context).inflate(R.layout.layout_popup_menu_item, null) as
                MaterialTextView
        val dp30 = SizeUtils.dp2px(30f)
        val paddingHorizontal = text.paddingStart + text.paddingEnd
        val drawablePadding = text.compoundDrawablePadding
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

    private class ActionsListAdapter(val menu: Menu, val context: Context) : BaseAdapter() {

        override fun getCount(): Int = menu.size()

        override fun getItem(position: Int): MenuItem = menu.getItem(position)

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: MaterialTextView =
                if (convertView == null || convertView !is MaterialButton) {
                    (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                        .inflate(R.layout.layout_popup_menu_item, parent, false) as
                        MaterialTextView
                } else {
                    convertView as MaterialTextView
                }

            val item = getItem(position)
            view.text = item.title

            val start = item.icon ?: null
            var end: Drawable? = null
            if (item.hasSubMenu()) {
                end = ContextCompat.getDrawable(context, R.drawable.ic_submenu_arrow)
            }

            view.setCompoundDrawablesRelativeWithIntrinsicBounds(start, null, end, null)
            view.setOnClickListener { (item as MenuItemImpl).invoke() }

            return view
        }
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
            this.list.adapter = ActionsListAdapter(item.subMenu, editor.context)
        }

        return true
    }

    override fun onMenuModeChange(menu: MenuBuilder) {}
}
