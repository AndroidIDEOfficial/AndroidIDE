package com.itsaky.androidide.ui.inflater;

import com.itsaky.androidide.ui.view.IView;
import java.util.HashSet;
import java.util.Set;
import com.itsaky.androidide.ui.view.IAttribute;

public abstract class BaseLayoutInflater extends ILayoutInflater {
    
    private final Set<IInflateListener> inflateListeners = new HashSet<>();

    @Override
    public void registerInflateListener(IInflateListener listener) {
        inflateListeners.add(listener);
    }

    @Override
    public void unregisterListener(IInflateListener listener) {
        inflateListeners.remove(listener);
    }
    
    protected void preInflate () {
        for (IInflateListener listener : inflateListeners) {
            listener.onBeginInflate();
        }
    }
    
    protected void postApplyAttribute (IAttribute attr, IView view) {
        for (IInflateListener listener : inflateListeners) {
            listener.onApplyAttribute(attr, view);
        }
    }
    
    protected void postCreateView (IView view) {
        for (IInflateListener listener : inflateListeners) {
            listener.onInflateView(view, view.getParent());
        }
    }
    
    protected void postInflate (IView root) {
        for (IInflateListener listener : inflateListeners) {
            listener.onFinishInflate(root);
        }
    }
    
    protected Set<IInflateListener> getInflateListeners () {
        return this.inflateListeners;
    }
}
