package com.unnamed.b.atv.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.DrawableRes;

import com.unnamed.b.atv.R;
import com.unnamed.b.atv.holder.SimpleViewHolder;
import com.unnamed.b.atv.model.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Created by Bogdan Melnychuk on 2/10/15. */
@SuppressWarnings("rawtypes")
public class AndroidTreeView {

  public static final String NODES_PATH_SEPARATOR = ";";
  private final Context mContext;
  private final int nodeViewBackground;
  protected TreeNode mRoot;
  private boolean applyForRoot;
  private int containerStyle = 0;
  private TreeNode.BaseNodeViewHolder defaultViewHolder;
  private TreeNode.TreeNodeClickListener nodeClickListener;
  private TreeNode.TreeNodeLongClickListener nodeLongClickListener;
  private boolean mSelectionModeEnabled;
  private boolean use2dScroll = false;
  private boolean enableAutoToggle = true;

  public AndroidTreeView(Context context, TreeNode root, @DrawableRes int nodeBackground) {
    this.mRoot = root;
    this.mContext = context;
    this.nodeViewBackground = nodeBackground;
    this.defaultViewHolder = new SimpleViewHolder(context);
  }

  private static void expand(final View v) {
    v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    final int targetHeight = v.getMeasuredHeight();

    v.getLayoutParams().height = 0;
    v.setVisibility(View.VISIBLE);
    Animation a =
        new Animation() {
          @Override
          public boolean willChangeBounds() {
            return true;
          }

          @Override
          protected void applyTransformation(float interpolatedTime, Transformation t) {
            v.getLayoutParams().height =
                interpolatedTime == 1
                    ? LinearLayout.LayoutParams.WRAP_CONTENT
                    : (int) (targetHeight * interpolatedTime);
            v.requestLayout();
          }
        };

    // 1dp/ms
    a.setDuration(
        (int) ((targetHeight / v.getContext().getResources().getDisplayMetrics().density) / 2));
    v.startAnimation(a);
  }

  private static void collapse(final View v) {
    final int initialHeight = v.getMeasuredHeight();

    Animation a =
        new Animation() {
          @Override
          public boolean willChangeBounds() {
            return true;
          }

          @Override
          protected void applyTransformation(float interpolatedTime, Transformation t) {
            if (interpolatedTime == 1) {
              v.setVisibility(View.GONE);
            } else {
              v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
              v.requestLayout();
            }
          }
        };

    // 1dp/ms
    a.setDuration(
        (int) ((initialHeight / v.getContext().getResources().getDisplayMetrics().density) / 2));
    v.startAnimation(a);
  }

  public void setRoot(TreeNode mRoot) {
    this.mRoot = mRoot;
  }

  public void setDefaultContainerStyle(int style) {
    setDefaultContainerStyle(style, false);
  }

  public void setDefaultContainerStyle(int style, boolean applyForRoot) {
    containerStyle = style;
    this.applyForRoot = applyForRoot;
  }

  public void setUse2dScroll(boolean use2dScroll) {
    this.use2dScroll = use2dScroll;
  }

  public boolean is2dScrollEnabled() {
    return use2dScroll;
  }

  public void setUseAutoToggle(boolean enableAutoToggle) {
    this.enableAutoToggle = enableAutoToggle;
  }

  public boolean isAutoToggleEnabled() {
    return enableAutoToggle;
  }

  public void setDefaultViewHolder(TreeNode.BaseNodeViewHolder viewHolder) {
    defaultViewHolder = viewHolder;
  }

  public void setDefaultNodeClickListener(TreeNode.TreeNodeClickListener listener) {
    nodeClickListener = listener;
  }

  public void setDefaultNodeLongClickListener(TreeNode.TreeNodeLongClickListener listener) {
    nodeLongClickListener = listener;
  }

  public void expandAll() {
    expandNode(mRoot, true);
  }

  public void collapseAll() {
    for (var i = 0; i < mRoot.getChildren().size(); i++) {
      final var n = mRoot.childAt(i);
      collapseNode(n, true);
    }
  }

  public View getView(int style) {
    final ViewGroup view;
    if (style > 0) {
      ContextThemeWrapper newContext = new ContextThemeWrapper(mContext, style);
      view = use2dScroll ? new TwoDScrollView(newContext) : new ScrollView(newContext);
    } else {
      view = use2dScroll ? new TwoDScrollView(mContext) : new ScrollView(mContext);
    }

    Context containerContext = mContext;
    if (containerStyle != 0 && applyForRoot) {
      containerContext = new ContextThemeWrapper(mContext, containerStyle);
    }
    final LinearLayout viewTreeItems = new LinearLayout(containerContext, null, containerStyle);

    viewTreeItems.setId(R.id.tree_items);
    viewTreeItems.setOrientation(LinearLayout.VERTICAL);
    view.addView(viewTreeItems);

    view.setNestedScrollingEnabled(false);

    mRoot.setViewHolder(
        new TreeNode.BaseNodeViewHolder(mContext) {
          @Override
          public ViewGroup getNodeItemsView() {
            return viewTreeItems;
          }

          @Override
          public View createNodeView(TreeNode node, Object value) {
            return null;
          }
        });

    expandNode(mRoot, false);
    return view;
  }

  public View getView() {
    return getView(-1);
  }

  public void expandLevel(int level) {
    List<TreeNode> children = mRoot.getChildren();
    for (int i = 0; i < children.size(); i++) {
      TreeNode n = children.get(i);
      expandLevel(n, level);
    }
  }

  public void expandNode(TreeNode node) {
    expandNode(node, false);
  }

  public void collapseNode(TreeNode node) {
    collapseNode(node, false);
  }

  public String getSaveState() {
    final StringBuilder builder = new StringBuilder();
    getSaveState(mRoot, builder);
    if (builder.length() > 0) {
      builder.setLength(builder.length() - 1);
    }
    return builder.toString();
  }

  public void restoreState(String saveState) {
    if (!TextUtils.isEmpty(saveState)) {
      collapseAll();
      final String[] openNodesArray = saveState.split(NODES_PATH_SEPARATOR);
      final Set<String> openNodes = new HashSet<>(Arrays.asList(openNodesArray));
      restoreNodeState(mRoot, openNodes);
    }
  }

  public void toggleNode(TreeNode node) {
    if (node.isExpanded()) {
      collapseNode(node, false);
    } else {
      expandNode(node, false);
    }
  }

  public void collapseNode(TreeNode node, final boolean includeSubnodes) {
    node.setExpanded(false);
    TreeNode.BaseNodeViewHolder nodeViewHolder = getViewHolderForNode(node);
    nodeViewHolder.getNodeItemsView().setVisibility(View.GONE);
    nodeViewHolder.toggle(false);
    if (includeSubnodes) {
      List<TreeNode> children = node.getChildren();
      for (int i = 0; i < children.size(); i++) {
        TreeNode n = children.get(i);
        collapseNode(n, true);
      }
    }
  }

  public void expandNode(final TreeNode node, boolean includeSubnodes) {
    node.setExpanded(true);
    final TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(node);
    parentViewHolder.getNodeItemsView().removeAllViews();

    parentViewHolder.toggle(true);

    List<TreeNode> children = node.getChildren();
    for (int i = 0; i < children.size(); i++) {
      TreeNode n = children.get(i);
      addNode(parentViewHolder.getNodeItemsView(), n);

      if (n.isExpanded() || includeSubnodes) {
        expandNode(n, includeSubnodes);
      }
    }
    parentViewHolder.getNodeItemsView().setVisibility(View.VISIBLE);
  }

  @SuppressWarnings("unchecked")
  public <E> List<E> getSelectedValues(Class<E> clazz) {
    List<E> result = new ArrayList<>();
    List<TreeNode> selected = getSelected();
    for (TreeNode n : selected) {
      Object value = n.getValue();
      if (value != null && value.getClass().equals(clazz)) {
        result.add((E) value);
      }
    }
    return result;
  }

  public boolean isSelectionModeEnabled() {
    return mSelectionModeEnabled;
  }

  // ------------------------------------------------------------
  //  Selection methods

  public void setSelectionModeEnabled(boolean selectionModeEnabled) {
    if (!selectionModeEnabled) {
      // TODO fix double iteration over tree
      deselectAll();
    }
    mSelectionModeEnabled = selectionModeEnabled;

    List<TreeNode> children = mRoot.getChildren();
    for (int i = 0; i < children.size(); i++) {
      TreeNode node = children.get(i);
      toggleSelectionMode(node, selectionModeEnabled);
    }
  }

  public List<TreeNode> getSelected() {
    if (mSelectionModeEnabled) {
      return getSelected(mRoot);
    } else {
      return new ArrayList<TreeNode>();
    }
  }

  public void selectAll(boolean skipCollapsed) {
    makeAllSelection(true, skipCollapsed);
  }

  public void deselectAll() {
    makeAllSelection(false, false);
  }

  public void selectNode(TreeNode node, boolean selected) {
    if (mSelectionModeEnabled) {
      node.setSelected(selected);
      toogleSelectionForNode(node, true);
    }
  }

  private void toogleSelectionForNode(TreeNode node, boolean makeSelectable) {
    TreeNode.BaseNodeViewHolder holder = getViewHolderForNode(node);
    if (holder.isInitialized()) {
      getViewHolderForNode(node).toggleSelectionMode(makeSelectable);
    }
  }

  private TreeNode.BaseNodeViewHolder getViewHolderForNode(TreeNode node) {
    TreeNode.BaseNodeViewHolder viewHolder = node.getViewHolder();
    if (viewHolder == null) {
      viewHolder = defaultViewHolder;
    }
    if (viewHolder.getContainerStyle() <= 0) {
      viewHolder.setContainerStyle(containerStyle);
    }
    if (viewHolder.getTreeView() == null) {
      viewHolder.setTreeViev(this);
    }
    return viewHolder;
  }

  public void addNode(TreeNode parent, final TreeNode nodeToAdd) {
    parent.addChild(nodeToAdd);
    if (parent.isExpanded()) {
      final TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(parent);
      addNode(parentViewHolder.getNodeItemsView(), nodeToAdd);
    }
  }

  public void removeNode(TreeNode node) {
    if (node.getParent() != null) {
      TreeNode parent = node.getParent();
      int index = parent.deleteChild(node);
      if (parent.isExpanded() && index >= 0) {
        final TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(parent);
        parentViewHolder.getNodeItemsView().removeViewAt(index);
      }
    }
  }

  private void expandLevel(TreeNode node, int level) {
    if (node.getLevel() <= level) {
      expandNode(node, false);
    }
    List<TreeNode> children = node.getChildren();
    for (int i = 0, childrenSize = children.size(); i < childrenSize; i++) {
      TreeNode n = children.get(i);
      expandLevel(n, level);
    }
  }

  private void restoreNodeState(TreeNode node, Set<String> openNodes) {
    List<TreeNode> children = node.getChildren();
    for (int i = 0, childrenSize = children.size(); i < childrenSize; i++) {
      TreeNode n = children.get(i);
      if (openNodes.contains(n.getPath())) {
        expandNode(n);
        restoreNodeState(n, openNodes);
      }
    }
  }

  private void getSaveState(TreeNode root, StringBuilder sBuilder) {
    List<TreeNode> children = root.getChildren();
    for (int i = 0, childrenSize = children.size(); i < childrenSize; i++) {
      TreeNode node = children.get(i);
      if (node.isExpanded()) {
        sBuilder.append(node.getPath());
        sBuilder.append(NODES_PATH_SEPARATOR);
        getSaveState(node, sBuilder);
      }
    }
  }

  private void addNode(ViewGroup container, final TreeNode n) {
    final TreeNode.BaseNodeViewHolder viewHolder = getViewHolderForNode(n);
    final View nodeView = viewHolder.getView();
    nodeView.setBackgroundResource(nodeViewBackground);
    if (nodeView.getParent() != null && nodeView.getParent() instanceof ViewGroup) {
      ((ViewGroup) nodeView.getParent()).removeView(nodeView);
    }
    container.addView(nodeView);
    if (mSelectionModeEnabled) {
      viewHolder.toggleSelectionMode(true);
    }

    nodeView.setOnClickListener(
        v -> {
          if (n.getClickListener() != null) {
            n.getClickListener().onClick(n, n.getValue());
          } else if (nodeClickListener != null) {
            nodeClickListener.onClick(n, n.getValue());
          }
          if (enableAutoToggle) {
            toggleNode(n);
          }
        });

    nodeView.setOnLongClickListener(
        view -> {
          if (n.getLongClickListener() != null) {
            return n.getLongClickListener().onLongClick(n, n.getValue());
          } else if (nodeLongClickListener != null) {
            return nodeLongClickListener.onLongClick(n, n.getValue());
          }
          if (enableAutoToggle) {
            toggleNode(n);
          }
          return false;
        });
  }

  private void toggleSelectionMode(TreeNode parent, boolean mSelectionModeEnabled) {
    toogleSelectionForNode(parent, mSelectionModeEnabled);
    if (parent.isExpanded()) {
      List<TreeNode> children = parent.getChildren();
      for (int i = 0; i < children.size(); i++) {
        TreeNode node = children.get(i);
        toggleSelectionMode(node, mSelectionModeEnabled);
      }
    }
  }

  // TODO Do we need to go through whole tree? Save references or consider collapsed nodes as not
  // selected
  private List<TreeNode> getSelected(TreeNode parent) {
    List<TreeNode> result = new ArrayList<>();
    List<TreeNode> children = parent.getChildren();
    for (int i = 0; i < children.size(); i++) {
      TreeNode n = children.get(i);
      if (n.isSelected()) {
        result.add(n);
      }
      result.addAll(getSelected(n));
    }
    return result;
  }

  // -----------------------------------------------------------------
  // Add / Remove

  private void makeAllSelection(boolean selected, boolean skipCollapsed) {
    if (mSelectionModeEnabled) {
      List<TreeNode> children = mRoot.getChildren();
      for (int i = 0; i < children.size(); i++) {
        TreeNode node = children.get(i);
        selectNode(node, selected, skipCollapsed);
      }
    }
  }

  private void selectNode(TreeNode parent, boolean selected, boolean skipCollapsed) {
    parent.setSelected(selected);
    toogleSelectionForNode(parent, true);
    boolean toContinue = !skipCollapsed || parent.isExpanded();
    if (toContinue) {
      List<TreeNode> children = parent.getChildren();
      for (int i = 0; i < children.size(); i++) {
        TreeNode node = children.get(i);
        selectNode(node, selected, skipCollapsed);
      }
    }
  }
}
