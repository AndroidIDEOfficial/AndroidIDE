/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/
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
