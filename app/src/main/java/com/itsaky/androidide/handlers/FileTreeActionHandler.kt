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

package com.itsaky.androidide.handlers

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.IntentUtils
import com.itsaky.androidide.EditorActivity
import com.itsaky.androidide.R
import com.itsaky.androidide.R.string
import com.itsaky.androidide.adapters.viewholders.FileTreeViewHolder
import com.itsaky.androidide.app.StudioApp
import com.itsaky.androidide.databinding.LayoutCreateFileJavaBinding
import com.itsaky.androidide.databinding.LayoutDialogTextInputBinding
import com.itsaky.androidide.eventbus.events.Event
import com.itsaky.androidide.eventbus.events.file.FileCreationEvent
import com.itsaky.androidide.eventbus.events.file.FileDeletionEvent
import com.itsaky.androidide.eventbus.events.file.FileRenameEvent
import com.itsaky.androidide.eventbus.events.filetree.FileClickEvent
import com.itsaky.androidide.eventbus.events.filetree.FileLongClickEvent
import com.itsaky.androidide.events.ExpandTreeNodeRequestEvent
import com.itsaky.androidide.events.FileContextMenuItemClickEvent
import com.itsaky.androidide.events.ListProjectFilesRequestEvent
import com.itsaky.androidide.fragments.sheets.OptionsListFragment
import com.itsaky.androidide.models.SheetOption
import com.itsaky.androidide.projects.ProjectManager.getProjectDirPath
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ProjectWriter
import com.itsaky.toaster.Toaster.Type.ERROR
import com.itsaky.toaster.Toaster.Type.SUCCESS
import com.unnamed.b.atv.model.TreeNode
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN
import java.io.File
import java.util.Objects
import java.util.regex.Pattern.compile
import java.util.regex.Pattern.quote

/**
 * Handles events related to files in filetree.
 *
 * @author Akash Yadav
 */
@Suppress("unused")
class FileTreeActionHandler : BaseEventHandler() {

  private var lastHeld: TreeNode? = null

  companion object {
    const val TAG_FILE_OPTIONS_FRAGMENT = "file_options_fragment"

    const val RES_PATH_REGEX = "/.*/src/.*/res"
    const val LAYOUT_RES_PATH_REGEX = "/.*/src/.*/res/layout"
    const val MENU_RES_PATH_REGEX = "/.*/src/.*/res/menu"
    const val DRAWABLE_RES_PATH_REGEX = "/.*/src/.*/res/drawable"
    const val JAVA_PATH_REGEX = "/.*/src/.*/java"

    const val ID_COPY_PATH = 0
    const val ID_RENAME_FILE = 1
    const val ID_DELETE_FILE = 2
    const val ID_NEW_FILE = 3
    const val ID_NEW_FOLDER = 4
  }

  @Subscribe(threadMode = MAIN)
  fun onFileClicked(event: FileClickEvent) {
    if (!checkIsEditorActivity(event)) {
      logCannotHandle(event)
      return
    }

    if (event.file.isDirectory) {
      return
    }

    val context = event[Context::class.java]!! as EditorActivity
    if (event.file.name.endsWith(".apk")) {
      val intent: Intent = IntentUtils.getInstallAppIntent(event.file)
      context.startActivity(intent)
      return
    }
    context.openFile(event.file)
  }

  @Subscribe(threadMode = MAIN)
  fun onFileLongClicked(event: FileLongClickEvent) {
    if (!checkIsEditorActivity(event)) {
      logCannotHandle(event)
      return
    }

    this.lastHeld = event[TreeNode::class.java]
    val context = event[Context::class.java]!! as EditorActivity
    createFileOptionsFragment(event.file)
      .show(context.supportFragmentManager, TAG_FILE_OPTIONS_FRAGMENT)
  }

  @Subscribe(threadMode = MAIN)
  internal fun onFileOptionClicked(event: FileContextMenuItemClickEvent) {
    val option = event.option
    if (option.extra !is File) {
      return
    }

    val context = event[Context::class.java]!!
    val file = option.extra!! as File
    when (option.id) {
      ID_COPY_PATH -> {
        ClipboardUtils.copyText("[AndroidIDE] Copied File Path", file.absolutePath)
        StudioApp.getInstance().toast(string.copied, SUCCESS)
      }
      ID_RENAME_FILE -> renameFile(context, file)
      ID_DELETE_FILE -> delete(context, file)
      ID_NEW_FILE -> createNewFile(context, file)
      ID_NEW_FOLDER -> createNewFolder(context, file)
    }
  }

  private fun createFileOptionsFragment(file: File): OptionsListFragment {
    val fragment = OptionsListFragment()
    fragment.addOption(
      SheetOption(ID_COPY_PATH, R.drawable.ic_file_copy_path, string.copy_path, file)
    )
    fragment.addOption(
      SheetOption(ID_RENAME_FILE, R.drawable.ic_file_rename, string.rename_file, file)
    )
    fragment.addOption(SheetOption(ID_DELETE_FILE, R.drawable.ic_delete, string.delete_file, file))

    if (file.isDirectory) {
      fragment.addOption(SheetOption(ID_NEW_FILE, R.drawable.ic_new_file, string.new_file, file))
      fragment.addOption(
        SheetOption(ID_NEW_FOLDER, R.drawable.ic_new_folder, string.new_folder, file)
      )
    }

    return fragment
  }

  private fun createNewFile(context: Context, file: File) {
    createNewFile(context, file, false)
  }

  private fun createNewFile(context: Context, file: File, forceUnknownType: Boolean) {
    if (forceUnknownType) {
      createNewEmptyFile(context, file)
      return
    }

    val projectDir = getProjectDirPath()
    Objects.requireNonNull(projectDir)
    val isJava = compile(quote(projectDir) + JAVA_PATH_REGEX).matcher(file.absolutePath).find()
    val isRes = compile(quote(projectDir) + RES_PATH_REGEX).matcher(file.absolutePath).find()
    val isLayoutRes =
      compile(quote(projectDir) + LAYOUT_RES_PATH_REGEX).matcher(file.absolutePath).find()
    val isMenuRes =
      compile(quote(projectDir) + MENU_RES_PATH_REGEX).matcher(file.absolutePath).find()
    val isDrawableRes =
      compile(quote(projectDir) + DRAWABLE_RES_PATH_REGEX).matcher(file.absolutePath).find()

    if (isJava) {
      createJavaClass(context, file)
      return
    }

    if (isLayoutRes && file.name == "layout") {
      createLayoutRes(context, file)
      return
    }

    if (isMenuRes && file.name == "menu") {
      createMenuRes(context, file)
      return
    }

    if (isDrawableRes && file.name == "drawable") {
      createDrawableRes(context, file)
      return
    }

    if (isRes && file.name == "res") {
      createNewResource(context, file)
      return
    }

    createNewEmptyFile(context, file)
  }

  private fun createJavaClass(context: Context, file: File) {
    val builder = DialogUtils.newMaterialDialogBuilder(context)
    val binding: LayoutCreateFileJavaBinding =
      LayoutCreateFileJavaBinding.inflate(LayoutInflater.from(context))
    builder.setView(binding.root)
    builder.setTitle(string.new_java_class)
    builder.setPositiveButton(string.text_create) { dialogInterface, _ ->
      dialogInterface.dismiss()
      val name: String = binding.name.editText!!.text.toString().trim()
      val autoLayout = binding.checkButton.isChecked
      val pkgName = ProjectWriter.getPackageName(file)
      if (pkgName == null || pkgName.trim { it <= ' ' }.isEmpty()) {
        StudioApp.getInstance().toast(string.msg_get_package_failed, ERROR)
        return@setPositiveButton
      }

      val id: Int = binding.typeGroup.checkedButtonId
      val javaName = if (name.endsWith(".java")) name else "$name.java"
      val className = if (!name.contains(".")) name else name.substring(0, name.lastIndexOf("."))
      val created =
        when (id) {
          binding.typeClass.id ->
            createFile(context, file, javaName, ProjectWriter.createJavaClass(pkgName, className))
          binding.typeInterface.id ->
            createFile(
              context,
              file,
              javaName,
              ProjectWriter.createJavaInterface(pkgName, className)
            )
          binding.typeEnum.id ->
            createFile(context, file, javaName, ProjectWriter.createJavaEnum(pkgName, className))
          binding.typeActivity.id ->
            createFile(context, file, javaName, ProjectWriter.createActivity(pkgName, className))
          else -> createFile(context, file, name, "")
        }

      if (created && autoLayout) {
        val packagePath = pkgName.toString().replace(".", "/")
        createAutoLayout(context, file, name, packagePath)
      }
    }
    builder.setNegativeButton(android.R.string.cancel, null)
    builder.setCancelable(false)
    builder.create().show()
  }

  private fun createLayoutRes(context: Context, file: File) {
    createNewFileWithContent(
      context,
      Environment.mkdirIfNotExits(file),
      ProjectWriter.createLayout(),
      ".xml"
    )
  }

  private fun createAutoLayout(
    context: Context,
    directory: File,
    fileName: String,
    packagePath: String
  ) {
    val app = StudioApp.getInstance()
    val dir = directory.toString().replace("java/$packagePath", "res/layout/")
    val layoutName = ProjectWriter.createLayoutName(fileName.replace(".java", ".xml"))
    val newFileLayout = File(dir, layoutName)
    if (newFileLayout.exists()) {
      app.toast(string.msg_file_exists, ERROR)
      return
    }

    if (!FileIOUtils.writeFileFromString(newFileLayout, ProjectWriter.createLayout())) {
      app.toast(string.msg_file_creation_failed, ERROR)
      return
    }

    notifyFileCreated(newFileLayout, context)
  }

  private fun createMenuRes(context: Context, file: File) {
    createNewFileWithContent(
      context,
      Environment.mkdirIfNotExits(file),
      ProjectWriter.createMenu(),
      ".xml"
    )
  }

  private fun createDrawableRes(context: Context, file: File) {
    createNewFileWithContent(
      context,
      Environment.mkdirIfNotExits(file),
      ProjectWriter.createDrawable(),
      ".xml"
    )
  }

  private fun createNewResource(context: Context, file: File) {
    val labels =
      arrayOf(
        context.getString(string.restype_drawable),
        context.getString(string.restype_layout),
        context.getString(string.restype_menu),
        context.getString(string.restype_other)
      )
    val builder = DialogUtils.newMaterialDialogBuilder(context)
    builder.setTitle(string.new_xml_resource)
    builder.setItems(labels) { _, position ->
      when (position) {
        0 -> createDrawableRes(context, File(file, "drawable"))
        1 -> createLayoutRes(context, File(file, "layout"))
        2 -> createMenuRes(context, File(file, "menu"))
        3 -> createNewFile(context, file, true)
      }
    }
    builder.create().show()
  }

  private fun createNewEmptyFile(context: Context, file: File) {
    createNewFileWithContent(context, file, "")
  }

  private fun createNewFileWithContent(context: Context, file: File, content: String) {
    createNewFileWithContent(context, file, content, null)
  }

  private fun createNewFileWithContent(
    context: Context,
    folder: File,
    content: String,
    extension: String?,
  ) {
    val binding = LayoutDialogTextInputBinding.inflate(LayoutInflater.from(context))
    val builder = DialogUtils.newMaterialDialogBuilder(context)
    binding.name.editText!!.setHint(string.file_name)
    builder.setTitle(string.new_file)
    builder.setMessage(
      context.getString(string.msg_can_contain_slashes) +
        "\n\n" +
        context.getString(string.msg_newfile_dest, folder.absolutePath)
    )
    builder.setView(binding.root)
    builder.setCancelable(false)
    builder.setPositiveButton(string.text_create) { dialogInterface, _ ->
      dialogInterface.dismiss()
      var name = binding.name.editText!!.text.toString().trim()
      if (extension != null && extension.trim { it <= ' ' }.isNotEmpty()) {
        name = if (name.endsWith(extension)) name else name + extension
      }
      createFile(context, folder, name, content)
    }
    builder.setNegativeButton(android.R.string.cancel, null)
    builder.create().show()
  }

  private fun createFile(
    context: Context,
    directory: File,
    name: String,
    content: String
  ): Boolean {
    val app = StudioApp.getInstance()
    if (name.length !in 1..40 || name.startsWith("/")) {
      app.toast(string.msg_invalid_name, ERROR)
      return false
    }

    val newFile = File(directory, name)
    if (newFile.exists()) {
      app.toast(string.msg_file_exists, ERROR)
      return false
    }
    if (!FileIOUtils.writeFileFromString(newFile, content)) {
      app.toast(string.msg_file_creation_failed, ERROR)
      return false
    }

    notifyFileCreated(newFile, context)
    // TODO Notify language servers about file created event
    app.toast(string.msg_file_created, SUCCESS)
    if (lastHeld != null) {
      val node = TreeNode(newFile)
      node.viewHolder = FileTreeViewHolder(context)
      lastHeld!!.addChild(node)
      requestExpandHeldNode()
    } else {
      requestFileListing()
    }

    return true
  }

  private fun createNewFolder(context: Context, currentDir: File) {
    val app = StudioApp.getInstance()
    val binding = LayoutDialogTextInputBinding.inflate(LayoutInflater.from(context))
    val builder = DialogUtils.newMaterialDialogBuilder(context)
    binding.name.editText!!.setHint(string.folder_name)
    builder.setTitle(string.new_folder)
    builder.setMessage(string.msg_can_contain_slashes)
    builder.setView(binding.root)
    builder.setCancelable(false)
    builder.setPositiveButton(string.text_create) { dialogInterface, _ ->
      dialogInterface.dismiss()
      val name: String = binding.name.editText!!.text.toString().trim()
      if (name.length !in 1..40 || name.startsWith("/")) {
        app.toast(string.msg_invalid_name, ERROR)
        return@setPositiveButton
      }

      val newDir = File(currentDir, name)
      if (newDir.exists()) {
        app.toast(string.msg_folder_exists, ERROR)
        return@setPositiveButton
      }

      if (!newDir.mkdirs()) {
        app.toast(string.msg_folder_creation_failed, ERROR)
        return@setPositiveButton
      }

      app.toast(string.msg_folder_created, SUCCESS)
      if (lastHeld != null) {
        val node = TreeNode(newDir)
        node.viewHolder = FileTreeViewHolder(context)
        lastHeld!!.addChild(node)
        requestExpandHeldNode()
      } else {
        requestFileListing()
      }
    }
    builder.setNegativeButton(android.R.string.cancel, null)
    builder.create().show()
  }

  private fun delete(context: Context, file: File) {
    val app = StudioApp.getInstance()
    val builder = DialogUtils.newMaterialDialogBuilder(context)
    builder
      .setNegativeButton(string.no, null)
      .setPositiveButton(string.yes) { dialogInterface, _ ->
        dialogInterface.dismiss()
        @Suppress("DEPRECATION")
        val progressDialog =
          ProgressDialog.show(context, null, context.getString(string.please_wait), true, false)
        executeAsync({ FileUtils.delete(file) }) {
          progressDialog.dismiss()

          val deleted = it ?: false

          app.toast(
            if (deleted) string.deleted else string.delete_failed,
            if (deleted) SUCCESS else ERROR
          )
  
          if (!deleted) {
            return@executeAsync
          }
          
          notifyFileDeleted(file, context)
          // TODO Notify language servers about file delete event
          if (lastHeld != null) {
            val parent = lastHeld!!.parent
            parent.deleteChild(lastHeld)
            requestExpandNode(parent)
          } else {
            requestFileListing()
          }
  
          if (context is EditorActivity) {
            val frag = context.getEditorForFile(file)
            if (frag != null) {
              context.closeFile(context.findIndexOfEditorByFile(frag.file))
            }
          }
        }
      }
      .setTitle(string.title_confirm_delete)
      .setMessage(
        context.getString(
          string.msg_confirm_delete,
          String.format("%s [%s]", file.name, file.absolutePath)
        )
      )
      .setCancelable(false)
      .create()
      .show()
  }

  private fun renameFile(context: Context, file: File) {
    val app = StudioApp.getInstance()
    val binding: LayoutDialogTextInputBinding =
      LayoutDialogTextInputBinding.inflate(LayoutInflater.from(context))
    val builder = DialogUtils.newMaterialDialogBuilder(context)
    binding.name.editText!!.hint = context.getString(string.new_name)
    binding.name.editText!!.setText(file.name)
    builder.setTitle(string.rename_file)
    builder.setMessage(string.msg_rename_file)
    builder.setView(binding.root)
    builder.setNegativeButton(android.R.string.cancel, null)
    builder.setPositiveButton(string.rename_file) { dialogInterface, _ ->
      dialogInterface.dismiss()
      val name: String = binding.name.editText!!.text.toString().trim()
      val renamed = name.length in 1..40 && FileUtils.rename(file, name)
      app.toast(
        if (renamed) string.renamed else string.rename_failed,
        if (renamed) SUCCESS else ERROR
      )
      if (!renamed) {
        return@setPositiveButton
      }
      
      notifyFileRenamed(file, context)
      // TODO Notify language servers about file rename event
      if (lastHeld != null) {
        val parent = lastHeld!!.parent
        parent.deleteChild(lastHeld)
        val node = TreeNode(File(file.parentFile, name))
        node.viewHolder = FileTreeViewHolder(context)
        parent.addChild(node)
        requestExpandNode(parent)
      } else {
        requestFileListing()
      }
    }
    builder.create().show()
  }

  private fun requestFileListing() {
    EventBus.getDefault().post(ListProjectFilesRequestEvent())
  }

  private fun requestExpandHeldNode() {
    requestExpandNode(lastHeld!!)
  }

  private fun requestExpandNode(node: TreeNode) {
    EventBus.getDefault().post(ExpandTreeNodeRequestEvent(node))
  }

  private fun notifyFileRenamed(file: File, context: Context) {
    EventBus.getDefault().post(FileRenameEvent(file).putData(context))
  }

  private fun notifyFileCreated(file: File, context: Context) {
    EventBus.getDefault().post(FileCreationEvent(file).putData(context))
  }

  private fun notifyFileDeleted(file: File, context: Context) {
    EventBus.getDefault().post(FileDeletionEvent(file).putData(context))
  }

  private fun Event.putData(context: Context): Event {
    put(Context::class.java, context)
    if (context is EditorActivity) {
      put(BuildService::class.java, context.buildService)
    }
    return this
  }
}
