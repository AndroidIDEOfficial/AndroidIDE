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


package com.itsaky.androidide.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.itsaky.androidide.R;

public class GeneralPreferences extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener {
    
    public static final String KEY_OPEN_PROJECTS = "idepref_general_autoOpenProjects";
    public static final String KEY_CONFIRM_PROJECT_OPEN = "idepref_general_confirmProjectOpen";
    
    @Override
    public void onCreatePreferences(Bundle p1, String p2) {
        super.onCreatePreferences(p1, p2);
        if(getContext() == null) return;
        
        final PreferenceScreen screen = getPreferenceScreen();
		final SwitchPreference openProjects = new SwitchPreference(getContext());
        final SwitchPreference confirmProjectOpen = new SwitchPreference(getContext());
        
        openProjects.setKey(KEY_OPEN_PROJECTS);
        openProjects.setTitle(R.string.title_open_projects);
        openProjects.setIcon(R.drawable.ic_open_project);
        openProjects.setSummary(R.string.msg_open_projects);
        
        confirmProjectOpen.setKey(KEY_CONFIRM_PROJECT_OPEN);
        confirmProjectOpen.setTitle(R.string.title_confirm_project_open);
        confirmProjectOpen.setSummary(R.string.msg_confirm_project_open);
        confirmProjectOpen.setIcon(R.drawable.ic_open_project);
        
        openProjects.setChecked(getPrefManager().autoOpenProject());
        confirmProjectOpen.setChecked(getPrefManager().confirmProjectOpen());
        
        screen.addPreference(openProjects);
        screen.addPreference(confirmProjectOpen);
        
        setPreferenceScreen(screen);
        
        openProjects.setOnPreferenceChangeListener(this);
        confirmProjectOpen.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference p1, Object p2) {
        boolean checked = (Boolean) p2;
        if(p1.getKey().equals(KEY_OPEN_PROJECTS)) {
            getPrefManager().putBoolean(KEY_OPEN_PROJECTS, checked);
        } else if(p1.getKey().equals(KEY_CONFIRM_PROJECT_OPEN)) {
            getPrefManager().putBoolean(KEY_CONFIRM_PROJECT_OPEN, checked);
        }
        return true;
    }
}
