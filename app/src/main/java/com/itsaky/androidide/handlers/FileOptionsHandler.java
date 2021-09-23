package com.itsaky.androidide.handlers;

import android.view.LayoutInflater;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.viewholders.FileTreeViewHolder;
import com.itsaky.androidide.databinding.LayoutCreateFileJavaBinding;
import com.itsaky.androidide.databinding.LayoutDialogTextInputBinding;
import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.fragments.sheets.OptionsListFragment;
import com.itsaky.androidide.models.SheetOption;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ProjectWriter;
import com.itsaky.toaster.Toaster;
import com.unnamed.b.atv.model.TreeNode;
import java.io.File;
import java.util.regex.Pattern;

public class FileOptionsHandler extends IDEHandler implements OptionsListFragment.OnOptionsClickListener {
    
    private final String RES_PATH_REGEX = "/.*/src/.*/res";
    private final String LAYOUTRES_PATH_REGEX = "/.*/src/.*/res/layout";
    private final String MENURES_PATH_REGEX = "/.*/src/.*/res/menu";
    private final String DRAWABLERES_PATH_REGEX = "/.*/src/.*/res/drawable";
    private final String JAVA_PATH_REGEX = "/.*/src/.*/java";
    
    public FileOptionsHandler(Provider provider) {
        super(provider);
    }

    @Override
    public void start() {
        if(activity() == null)
            throwNPE();
    }

    @Override
    public void stop() {
    }

    @Override
    public void onOptionsClick(SheetOption option) {
        if(option.extra != null && option.extra instanceof File) {
            final File f = (File) option.extra;
            switch(option.id) {
                case 0 :
                    ClipboardUtils.copyText("[AndroidIDE] Copied File Path", f.getAbsolutePath());
                    activity().getApp().toast(R.string.copied, Toaster.Type.SUCCESS);
                    break;
                case 1 :
                    renameFile(f);
                    break;
                case 2 :
                    delete(f);
                    break;
                case 3 :
                    createNewFile(f);
                    break;
                case 4 :
                    createNewFolder(f);
                    break;
            }
		}
    }
    
    private void createNewFile(File f) {
        createNewFile(f, false);
    }

    private void createNewFile(final File f, boolean forceUnknownType) {
        if(forceUnknownType) {
            createNewEmptyFile(f);
        } else {
            final boolean isJava = Pattern.compile(Pattern.quote(androidProject().getProjectPath()) + JAVA_PATH_REGEX).matcher(f.getAbsolutePath()).find();
            final boolean isRes = Pattern.compile(Pattern.quote(androidProject().getProjectPath()) + RES_PATH_REGEX).matcher(f.getAbsolutePath()).find();
            final boolean isLayoutRes = Pattern.compile(Pattern.quote(androidProject().getProjectPath()) + LAYOUTRES_PATH_REGEX).matcher(f.getAbsolutePath()).find();
            final boolean isMenuRes = Pattern.compile(Pattern.quote(androidProject().getProjectPath()) + MENURES_PATH_REGEX).matcher(f.getAbsolutePath()).find();
            final boolean isDrawableRes = Pattern.compile(Pattern.quote(androidProject().getProjectPath()) + DRAWABLERES_PATH_REGEX).matcher(f.getAbsolutePath()).find();
            if(isJava) {
                createJavaClass(f);
            } else if(isLayoutRes && f.getName().equals("layout")) {
                createLayoutRes(f);
            } else if(isMenuRes && f.getName().equals("menu")) {
                createMenuRes(f);
            } else if(isDrawableRes && f.getName().equals("drawable")) {
                createDrawableRes(f);
            } else if(isRes && f.getName().equals("res")) {
                createNewResource(f);
            } else {
                createNewEmptyFile(f);
            }
        }
    }

    private void createJavaClass(final File f) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity(), R.style.AppTheme_MaterialAlertDialog);
        final LayoutCreateFileJavaBinding binding = LayoutCreateFileJavaBinding.inflate(activity().getLayoutInflater());
        builder.setView(binding.getRoot());
        builder.setTitle(R.string.new_java_class);
        builder.setPositiveButton(R.string.text_create, (p1, p2) -> {
            p1.dismiss();
            final String name = binding.name.getEditText().getText().toString().trim();
            final String pkgName = ProjectWriter.getPackageName(f);
            if(pkgName == null || pkgName.trim().length() <= 0) {
                activity().getApp().toast(R.string.msg_get_package_failed, Toaster.Type.ERROR);
                return;
            } else {
                final int id = binding.typeGroup.getCheckedButtonId();
                if(id == binding.typeClass.getId()) {
                    createFile(f, name.endsWith(".java") ? name : name.concat(".java"), ProjectWriter.createJavaClass(pkgName, !name.contains(".") ? name : name.substring(0, name.lastIndexOf("."))));
                } else if(id == binding.typeInterface.getId()) {
                    createFile(f, name.endsWith(".java") ? name : name.concat(".java"), ProjectWriter.createJavaInterface(pkgName, !name.contains(".") ? name : name.substring(0, name.lastIndexOf("."))));
                } else if(id == binding.typeEnum.getId()) {
                    createFile(f, name.endsWith(".java") ? name : name.concat(".java"), ProjectWriter.createJavaEnum(pkgName, !name.contains(".") ? name : name.substring(0, name.lastIndexOf("."))));
                } else if(id == binding.typeActivity.getId()) {
                    createFile(f, name.endsWith(".java") ? name : name.concat(".java"), ProjectWriter.createActivity(pkgName, !name.contains(".") ? name : name.substring(0, name.lastIndexOf("."))));
                } else {
                    createFile(f, name, "");
                }

            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setCancelable(false);
        builder.create().show();
    }

    private void createLayoutRes(File f) {
        createNewFileWithContent(Environment.mkdirIfNotExits(f), ProjectWriter.createLayout(), ".xml");
    }

    private void createMenuRes(File f) {
        createNewFileWithContent(Environment.mkdirIfNotExits(f), ProjectWriter.createMenu(), ".xml");
    }

    private void createDrawableRes(File f) {
        createNewFileWithContent(Environment.mkdirIfNotExits(f), ProjectWriter.createDrawable(), ".xml");
    }

    private void createNewResource(File f) {
        final String[] labels = {
            activity(). getString(R.string.restype_drawable),
            activity(). getString(R.string.restype_layout),
            activity(). getString(R.string.restype_menu),
            activity(). getString(R.string.restype_other)
        };
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity(), R.style.AppTheme_MaterialAlertDialog);
        builder.setTitle(R.string.new_xml_resource);
        builder.setItems(labels, (p1, p2) -> {
            final int pos = p2;
            if(pos == 0) {
                createDrawableRes(new File(f, "drawable"));
            } else if(pos == 1) {
                createLayoutRes(new File(f, "layout"));
            } else if(pos == 2) {
                createMenuRes(new File(f, "menu"));
            } else if(pos == 3) {
                createNewFile(f, true);
            }
        });
        builder.create().show();
    }

    private void createNewEmptyFile(File f) {
        createNewFileWithContent(f, "");
    }

    private void createNewFileWithContent(File f, String content) {
        createNewFileWithContent(f, content, null);
    }

    private void createNewFileWithContent(File folder, String content, String extension) {
        final LayoutDialogTextInputBinding binding = LayoutDialogTextInputBinding.inflate(activity(). getLayoutInflater());
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity(), R.style.AppTheme_MaterialAlertDialog);

        binding.name.getEditText().setHint(R.string.file_name);

        builder.setTitle(R.string.new_file);
        builder.setMessage(activity(). getString(R.string.msg_can_contain_slashes).concat("\n\n").concat(activity(). getString(R.string.msg_newfile_dest, folder.getAbsolutePath())));
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.text_create, (p1, p2) -> {
            p1.dismiss();
            String name = binding.name.getEditText().getText().toString().trim();
            if(extension != null && extension.trim().length() > 0) {
                name = name.endsWith(extension) ? name : name.concat(extension);
            }
            createFile(folder, name, content);
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void createFile(File f, String name, String content) {
        if(name.length() > 0 && name.length() <= 40 && !name.startsWith("/")) {
            final File file = new File(f, name);
            if(file.exists()) {
                activity().getApp().toast(R.string.msg_file_exists, Toaster.Type.ERROR);
            } else {
                if(FileIOUtils.writeFileFromString(file, content)) {
                    jls().notifyFileCreated(file);
                   activity(). getApp().toast(R.string.msg_file_created, Toaster.Type.SUCCESS);
                    if(activity().getLastHoldTreeNode() != null) {
                        TreeNode node = new TreeNode(file);
                        node.setViewHolder(new FileTreeViewHolder(activity()));
                        activity().getLastHoldTreeNode().addChild(node);
                        activity().getFileTreeFragment().expandNode(activity().getLastHoldTreeNode());
                    } else {
                        activity().getFileTreeFragment().listProjectFiles();
                    }
                } else {
                    activity(). getApp().toast(R.string.msg_file_creation_failed, Toaster.Type.ERROR);
                }
            }
        } else {
            activity(). getApp().toast(R.string.msg_invalid_name, Toaster.Type.ERROR);
        }
    }

    private void createNewFolder(File f) {
        final LayoutDialogTextInputBinding binding = LayoutDialogTextInputBinding.inflate(activity(). getLayoutInflater());
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity(), R.style.AppTheme_MaterialAlertDialog);

        binding.name.getEditText().setHint(R.string.folder_name);

        builder.setTitle(R.string.new_folder);
        builder.setMessage(R.string.msg_can_contain_slashes);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.text_create, (p1, p2) -> {
            p1.dismiss();
            final String name = binding.name.getEditText().getText().toString().trim();
            if(name.length() > 0 && name.length() <= 40 && !name.startsWith("/")) {
                final File file = new File(f, name);
                if(file.exists()) {
                    activity(). getApp().toast(R.string.msg_folder_exists, Toaster.Type.ERROR);
                } else {
                    if(file.mkdirs()) {
                        activity(). getApp().toast(R.string.msg_folder_created, Toaster.Type.SUCCESS);
                        if(activity().getLastHoldTreeNode() != null) {
                            TreeNode node = new TreeNode(file);
                            node.setViewHolder(new FileTreeViewHolder(activity()));
                            activity().getLastHoldTreeNode().addChild(node);
                            activity().getFileTreeFragment().expandNode(activity().getLastHoldTreeNode());
                        } else {
                            activity().getFileTreeFragment().listProjectFiles();
                        }
                    } else {
                        activity().getApp().toast(R.string.msg_folder_creation_failed, Toaster.Type.ERROR);
                    }
                }
            } else {
                activity(). getApp().toast(R.string.msg_invalid_name, Toaster.Type.ERROR);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void delete(final File f) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity(), R.style.AppTheme_MaterialAlertDialog);
        builder
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, (p1, p2) -> {
            p1.dismiss();
            final boolean deleted = FileUtils.delete(f);
            activity(). getApp().toast(deleted ? R.string.deleted : R.string.delete_failed, deleted ? Toaster.Type.SUCCESS : Toaster.Type.ERROR);
            if(deleted) {
                jls().notifyFileDeleted(f);
                if(activity().getLastHoldTreeNode() != null) {
                    TreeNode parent = activity().getLastHoldTreeNode().getParent();
                    parent.deleteChild(activity().getLastHoldTreeNode());
                    activity().getFileTreeFragment().expandNode(parent);
                } else {
                    activity().getFileTreeFragment().listProjectFiles();
                }
                EditorFragment frag = activity().getPagerAdapter().findEditorByFile(f);
                if(frag != null) {
                    activity().closeFile(activity().getPagerAdapter().getFragments().indexOf(frag));
                }
            }
        })
        .setTitle(R.string.title_confirm_delete)
            .setMessage(activity().getString(R.string.msg_confirm_delete, String.format("%s [%s]", f.getName(), f.getAbsolutePath())))
            .setCancelable(false)
            .create().show();
    }

    private void renameFile(File f) {
        final LayoutDialogTextInputBinding binding = LayoutDialogTextInputBinding.inflate(LayoutInflater.from(activity()));
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity(), R.style.AppTheme_MaterialAlertDialog);

        binding.name.getEditText().setHint(activity(). getString(R.string.new_name));
        binding.name.getEditText().setText(f.getName());

        builder.setTitle(R.string.rename_file);
        builder.setMessage(R.string.msg_rename_file);
        builder.setView(binding.getRoot());
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(R.string.rename_file, (p1, p2) -> {
            p1.dismiss();
            String name = binding.name.getEditText().getText().toString().trim();
            boolean renamed = name != null && name.length() > 0 && name.length() <= 40 && FileUtils.rename(f, name);
            activity(). getApp().toast(renamed ? R.string.renamed : R.string.rename_failed, renamed ? Toaster.Type.SUCCESS : Toaster.Type.ERROR);
            if(renamed) {
                if(activity().getLastHoldTreeNode() != null) {
                    TreeNode parent = activity().getLastHoldTreeNode().getParent();
                    parent.deleteChild(activity().getLastHoldTreeNode());
                    TreeNode node = new TreeNode(new File(f.getParentFile(), name));
                    node.setViewHolder(new FileTreeViewHolder(activity()));
                    parent.addChild(node);
                    activity().getFileTreeFragment().expandNode(parent);
                } else {
                    activity().getFileTreeFragment().listProjectFiles();
                }
            }
        });
        builder.create().show();
	}
}
