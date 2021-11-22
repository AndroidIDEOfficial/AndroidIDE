/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
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


package com.itsaky.androidide.language;

import com.itsaky.androidide.app.StudioApp;
import io.github.rosemoe.editor.langs.AbstractEditorLanguage;
import java.io.File;

public abstract class BaseLanguage extends AbstractEditorLanguage {
    
    public BaseLanguage() {
        this(null);
    }
    
    public BaseLanguage(File file) {
        super(file);
    }
    
    @Override
    public boolean useTab() {
        return false;
    }
    
    public int getTabSize() {
        return StudioApp.getInstance().getPrefManager().getEditorTabSize();
    }
}
