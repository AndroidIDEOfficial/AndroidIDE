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

package com.itsaky.androidide.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.toaster.Toaster;

import java.io.File;
import java.util.ArrayList;

import abhishekti7.unicorn.filepicker.UnicornFilePicker;
import abhishekti7.unicorn.filepicker.ui.FilePickerActivity;

public class BaseFragment extends Fragment {

  private FileChoserCallback dirCallback;
  ActivityResultLauncher<Intent> startForResult =
      registerForActivityResult(
          new ActivityResultContracts.StartActivityForResult(),
          new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
              if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();
                ArrayList<String> files = intent.getStringArrayListExtra("filePaths");
                if (files != null) {
                  if (files.size() == 1) {
                    File choseDir = new File(files.get(0));
                    if (choseDir.exists() && choseDir.isDirectory()) {
                      if (dirCallback != null) {
                        dirCallback.picketDictionary(choseDir);
                      }
                    } else {
                      StudioApp.getInstance()
                          .toast(R.string.msg_picked_isnt_dir, Toaster.Type.ERROR);
                    }
                  } else {
                    StudioApp.getInstance()
                        .toast(R.string.msg_pick_single_file, Toaster.Type.ERROR);
                  }
                }
              }
            }
          });

  protected StudioActivity getStudioActivity() {
    return (StudioActivity) getActivity();
  }

  protected void pickDirectory(FileChoserCallback dirCallback) {
    this.dirCallback = dirCallback;
    UnicornFilePicker.from(this)
        .addConfigBuilder()
        .addItemDivider(false)
        .selectMultipleFiles(false)
        .setRootDirectory(Environment.getExternalStorageDirectory().getAbsolutePath())
        .showHiddenFiles(true)
        .showOnlyDirectory(true)
        .theme(R.style.AppTheme_FilePicker)
        .build();
    Intent intent = new Intent(requireActivity(), FilePickerActivity.class);

    // .forResult(abhishekti7.unicorn.filepicker.utils.Constants.REQ_UNICORN_FILE);
    startForResult.launch(intent);
  }

  interface FileChoserCallback {
    void picketDictionary(File file);
  }
}
