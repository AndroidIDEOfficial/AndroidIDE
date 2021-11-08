package com.itsaky.androidide.ui.view;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a {@link android.view.ViewGroup}
 *
 * @author Akash Yadav
 */
public interface IViewGroup extends IView {
    
    /**
     * Add this view to this group
     *
     * @param view The view to add
     */
    void addView (IView view);
    
    /**
     * Add this view to this group
     *
     * @param view The view to add
     * @param index Add this view to this index
     */
    void addView (IView view, int index);
    
    /**
     * Remove the view at the specified index
     *
     * @param index The index of the view to remove
     */
    void removeView (int index);
    
    /**
     * Remove the given if it exists in this group
     * 
     * @param view The view to remove
     */
    void removeView (IView view);
     
    /**
     * Remove all children in this group
     */
    void removeAll ();
    
    /**
     * Perform an action in every child in this group
     *
     * @param consumer The consumer which will consume the views
     */
    void forEachChild (Consumer <IView> consumer);
    
    /**
     * Get the number of child views this layout contains
     *
     * @return The child count
     */
    int getChildCount();
    
    /**
     * Get the list of children in this group
     *
     * @return The list of children
     */
    List<IView> getChildren ();
    
    /**
     * Get child at the provided index
     *
     * @param index The index of child
     * @throws IndexOutOfBoundsException If there is no child at the given index
     */
    IView getChildAt (int index);
}
