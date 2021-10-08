package com.itsaky.widgets.models;

public class Widget implements Comparable {
    
    public String name;
    public String simpleName;
    public boolean isViewGroup;

    public Widget(String name, String simpleName, boolean isViewGroup) {
        this.name = name;
        this.simpleName = simpleName;
        this.isViewGroup = isViewGroup;
    }

    @Override
    public int compareTo(Object p1) {
        if(p1 instanceof Widget) {
            Widget that = (Widget) p1;
            return this.simpleName.compareTo(that.simpleName);
        }
        return -1;
    }
    
}
