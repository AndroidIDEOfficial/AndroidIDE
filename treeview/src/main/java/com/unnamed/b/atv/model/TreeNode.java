package com.unnamed.b.atv.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.unnamed.b.atv.R;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.unnamed.b.atv.view.TreeNodeWrapperView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Created by Bogdan Melnychuk on 2/10/15. */
public class TreeNode {

  public static final String NODES_ID_SEPARATOR = ":";

  private int mId;
  private int mLastId;
  private TreeNode mParent;
  private boolean mSelected;
  private boolean mSelectable = true;
  private final List<TreeNode> children;
  private BaseNodeViewHolder mViewHolder;
  private TreeNodeClickListener mClickListener;
  private TreeNodeLongClickListener mLongClickListener;
  private File mValue;
  private boolean mExpanded;

  public static TreeNode root() {
    return root(null);
  }

  public static TreeNode root(File value) {
    TreeNode root = new TreeNode(value);
    root.setSelectable(false);
    return root;
  }

  private int generateId() {
    return ++mLastId;
  }

  public TreeNode(File value) {
    children = Collections.synchronizedList(new ArrayList<TreeNode>());
    mValue = value;
  }

  public TreeNode addChild(TreeNode childNode) {
    childNode.mParent = this;
    childNode.mId = generateId();
    children.add(childNode);
    Collections.sort(children, new SortFileName());
    Collections.sort(children, new SortFolder());
    return this;
  }

  public TreeNode addChildren(TreeNode... nodes) {
    for (TreeNode n : nodes) {
      addChild(n);
    }
    return this;
  }

  public TreeNode addChildren(Collection<TreeNode> nodes) {
    for (TreeNode n : nodes) {
      addChild(n);
    }
    return this;
  }

  public TreeNode childAt(int index) {
    return children == null ? null : children.get(index);
  }

  public void deleteAllChildren() {
    children.clear();
  }

  public int deleteChild(TreeNode child) {
    for (int i = 0; i < children.size(); i++) {
      if (child.mId == children.get(i).mId) {
        children.remove(i);
        return i;
      }
    }
    return -1;
  }

  public List<TreeNode> getChildren() {
    return children == null ? Collections.synchronizedList(new ArrayList<TreeNode>()) : children;
  }

  public int size() {
    return children == null ? 0 : children.size();
  }

  public TreeNode getParent() {
    return mParent;
  }

  public int getId() {
    return mId;
  }

  public boolean isLeaf() {
    return size() == 0;
  }

  public TreeNode setValue(File file) {
    this.mValue = file;
    return this;
  }

  public File getValue() {
    return mValue;
  }

  public boolean isExpanded() {
    return mExpanded;
  }

  public TreeNode setExpanded(boolean expanded) {
    mExpanded = expanded;
    return this;
  }

  public void setSelected(boolean selected) {
    mSelected = selected;
  }

  public boolean isSelected() {
    return mSelectable && mSelected;
  }

  public void setSelectable(boolean selectable) {
    mSelectable = selectable;
  }

  public boolean isSelectable() {
    return mSelectable;
  }

  public String getPath() {
    final StringBuilder path = new StringBuilder();
    TreeNode node = this;
    while (node.mParent != null) {
      path.append(node.getId());
      node = node.mParent;
      if (node.mParent != null) {
        path.append(NODES_ID_SEPARATOR);
      }
    }
    return path.toString();
  }

  public int getLevel() {
    int level = 0;
    TreeNode root = this;
    while (root.mParent != null) {
      root = root.mParent;
      level++;
    }
    return level;
  }

  public boolean isLastChild() {
    if (!isRoot()) {
      int parentSize = mParent.children.size();
      if (parentSize > 0) {
        final List<TreeNode> parentChildren = mParent.children;
        return parentChildren.get(parentSize - 1).mId == mId;
      }
    }
    return false;
  }

  public TreeNode setViewHolder(BaseNodeViewHolder viewHolder) {
    mViewHolder = viewHolder;
    if (viewHolder != null) {
      viewHolder.mNode = this;
    }
    return this;
  }

  public TreeNode setClickListener(TreeNodeClickListener listener) {
    mClickListener = listener;
    return this;
  }

  public TreeNodeClickListener getClickListener() {
    return this.mClickListener;
  }

  public TreeNode setLongClickListener(TreeNodeLongClickListener listener) {
    mLongClickListener = listener;
    return this;
  }

  public TreeNodeLongClickListener getLongClickListener() {
    return mLongClickListener;
  }

  public BaseNodeViewHolder getViewHolder() {
    return mViewHolder;
  }

  public boolean isFirstChild() {
    if (!isRoot()) {
      List<TreeNode> parentChildren = mParent.children;
      return parentChildren.get(0).mId == mId;
    }
    return false;
  }

  public boolean isRoot() {
    return mParent == null;
  }

  public TreeNode getRoot() {
    TreeNode root = this;
    while (root.mParent != null) {
      root = root.mParent;
    }
    return root;
  }

  public interface TreeNodeClickListener {
    void onClick(TreeNode node, Object value);
  }

  public interface TreeNodeLongClickListener {
    boolean onLongClick(TreeNode node, Object value);
  }

  public abstract static class BaseNodeViewHolder<E> {
    protected AndroidTreeView tView;
    protected TreeNode mNode;
    private View mView;
    protected int containerStyle;
    protected Context context;

    public BaseNodeViewHolder(Context context) {
      this.context = context;
    }

    public View getView() {
      if (mView != null) {
        return mView;
      }
      final View nodeView = getNodeView();
      final TreeNodeWrapperView nodeWrapperView =
          new TreeNodeWrapperView(nodeView.getContext(), getContainerStyle());
      nodeWrapperView.insertNodeView(nodeView);
      mView = nodeWrapperView;

      return mView;
    }

    public void setTreeViev(AndroidTreeView treeViev) {
      this.tView = treeViev;
    }

    public AndroidTreeView getTreeView() {
      return tView;
    }

    public void setContainerStyle(int style) {
      containerStyle = style;
    }

    public View getNodeView() {
      return createNodeView(mNode, (E) mNode.getValue());
    }

    public ViewGroup getNodeItemsView() {
      return (ViewGroup) getView().findViewById(R.id.node_items);
    }

    public boolean isInitialized() {
      return mView != null;
    }

    public int getContainerStyle() {
      return containerStyle;
    }

    public abstract View createNodeView(TreeNode node, E value);

    public void toggle(boolean active) {
      // empty
    }

    public void toggleSelectionMode(boolean editModeEnabled) {
      // empty
    }
  }

  public class SortFileName implements Comparator<TreeNode> {
    @Override
    public int compare(TreeNode f1, TreeNode f2) {
      return f1.getValue().getName().compareTo(f2.getValue().getName());
    }
  }

  public class SortFolder implements Comparator<TreeNode> {
    @Override
    public int compare(TreeNode p1, TreeNode p2) {
      File f1 = p1.getValue();
      File f2 = p2.getValue();
      if (f1.isDirectory() == f2.isDirectory()) return 0;
      else if (f1.isDirectory() && !f2.isDirectory()) return -1;
      else return 1;
    }
  }
}
