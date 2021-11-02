package com.itsaky.androidide.ui.view;

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
     * Get the number of child views this layout contains
     *
     * @return The child count
     */
    int getChildCount();
    
    /**
     * Get child at the provided index
     *
     * @param index The index of child
     * @throws IndexOutOfBoundsException If there is no child at the given index
     */
    IView getChildAt (int index);
}
